package com.example.foret_app_prototype.helper;

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
    // 싱글톤 객체 생성 시작
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
    // 싱글톤 객체 생성 끝

    /**
     * DCIM 디렉토리 하위에 새로 저장될 사진 파일의 이름을 생성한다.
     * @return 경로 문자열
     */
    public String getNewPhotoPath() {
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH) + 1;
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mi = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);

        String fileName = String.format("%04d-%02d-%02d %02d-%02d-%02d.jpg",
                yy, mm, dd, hh, mi, ss);

        // --> "/mnt/sdcard/DCIM"
        File dir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        if(!dir.exists()) {
            dir.mkdirs();
        }

        // --> /mnt/sdcard/DCIM/2014-10-28 23;52;00.jpg
        return dir.getAbsolutePath() + "/" + fileName;
    }

    public Bitmap getThumb(Activity activity, String path) {
        // 실제 이미지를 저장할 객체
        Bitmap bmp = null;

        /** 1) 화면 해상도 얻기 - for KitKat */
        // 해상도 관리 객체
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // 가로, 높이 크기 얻기
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        // 긴축을 골라내기
        int maxScale = deviceWidth;
        if(deviceWidth < deviceHeight) {
            maxScale = deviceHeight;
        }

        /** 2) 이미지 크기 얻기 */
        // 비트맵 이미지 로더의 옵션을 설정하기 위한 객체
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 비트맵을 바로 로드하지 말고 정보만 읽어오라고 설정
        options.inJustDecodeBounds = true;

        // 비트맵 파일 읽어오기 - 옵션에 의해서 정보만 읽어들이게 된다.
        BitmapFactory.decodeFile(path, options);

        // 폭과 넓이 중에서 긴 축을 기준으로 삼기 위하여 별도로 값을 복사
        int fscale = options.outHeight;
        if(options.outWidth > options.outHeight) {
            fscale = options.outWidth;
        }

        /** 3) 이미지 리사이징 */
        // 이미지의 넓이가 파라미터보다 크면
        if(maxScale < fscale) {
            // 이미지의 사이즈를 maxScale로 나누어서 샘플링 사이즈 계산
            // ex) 이미지의 긴축이 2400px일 경우, maxScale이 800이면,
            //      3으로 지정된다.
            int sampleSize = fscale / maxScale;

            // 새 비트맵 옵션 생성
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            // 샘플사이즈 설정 --> 3으로 지정하면 1/3 크기가 된다.
            options2.inSampleSize = sampleSize;
            // 실제로 비트맵을 읽어온다.
            bmp = BitmapFactory.decodeFile(path, options2);
        } else {
            // 사이즈가 적당하면 그냥 읽는다.
            bmp = BitmapFactory.decodeFile(path);
        }

        try
        {
            // 이미지를 상황에 맞게 회전시킨다
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

    /**
     * EXIF정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
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

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
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
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

}
