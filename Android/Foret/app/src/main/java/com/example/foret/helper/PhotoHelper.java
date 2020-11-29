package com.example.foret.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class PhotoHelper {

    private static PhotoHelper current;

    public static PhotoHelper getInstance() {
        if(current == null) {
            current = new PhotoHelper();
        }

        return current;
    }

    public static void freeInstance() {
        current = null;
    }

    public PhotoHelper() {
        super();
    }


    public String getNewPhotoPath() {
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH) + 1;
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mi = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);

        String fileName = String.format("%04d-%02d-%02d-%02d-%02d-%02d.jpg",
                yy, mm, dd, hh, mi, ss);


        File dir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        if(!dir.exists()) {
            dir.mkdirs();
        }


        return dir.getAbsolutePath() + "/" + fileName;
    }

    public Bitmap getThumb(Activity activity, String path) {

        Bitmap bmp = null;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        // 긴축을 골라내기
        int maxScale = deviceWidth;
        if(deviceWidth < deviceHeight) {
            maxScale = deviceHeight;
        }


        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int fscale = options.outHeight;
        if(options.outWidth > options.outHeight) {
            fscale = options.outWidth;
        }

        if(maxScale < fscale) {
            int sampleSize = fscale / maxScale;

            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = sampleSize;
            bmp = BitmapFactory.decodeFile(path, options2);
        } else {
            bmp = BitmapFactory.decodeFile(path);
        }

        try
        {
            ExifInterface exif = new ExifInterface(path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            bmp = rotate(bmp, exifDegree);
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"오류발생: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        return bmp;
    }

    public int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }
    public Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

}
