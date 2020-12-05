package com.example.foret_app_prototype.helper;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class CalendarHelper {

    private static CalendarHelper instance = null;

    public static CalendarHelper getInstance() {
        if (instance == null) instance = new CalendarHelper();
        return instance;
    }

    private CalendarHelper() {
    }

    Calendar calendar;

    public String getCurrentTime() {
        calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);

        String currentTime = String.format("%02d월 %02d일 %02d시 %02d분", mm, dd, hh, mi);
        return currentTime;
    }

    public String getCurrentTimeFull() {
        calendar = Calendar.getInstance(Locale.KOREAN);
        //calendar.setTimeInMillis();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);

        String currentTime = String.format("%02d년 %02d월 %02d일 %02d시 %02d분", yy, mm, dd, hh, mi);
        return currentTime;
    }

    public String getRelativeTime(String uploadedTime) {

        //시간 변환 타입은 dd/mm/yyyy hh:mm  am/pm
        Calendar getTime = Calendar.getInstance(Locale.KOREAN);

        int currentYEAR = getTime.get(Calendar.YEAR);
        getTime.setTimeInMillis(Long.parseLong(uploadedTime));

        long beforeTime = Long.parseLong(uploadedTime);
        long currentTime = System.currentTimeMillis();

        int itemYEAR = Integer.parseInt(DateFormat.format("yy", beforeTime).toString());


        int makeGapTime = (int) (currentTime - beforeTime) / 60000;
        String time_text = "";
        if (makeGapTime < 1) {
            time_text = "방금 전";
        } else if (makeGapTime >= 1 && makeGapTime < 60) {
            time_text = makeGapTime + "분 전";
        } else if (makeGapTime / 60 >= 1 && makeGapTime / 60 <= 24) {
            time_text = (makeGapTime / 60) + "시간 전";
        } else if (currentYEAR == itemYEAR) {
            time_text = DateFormat.format("MM-dd hh:mm aa", beforeTime).toString();
        } else {
            time_text = DateFormat.format("yy-MM-dd hh:mm aa", beforeTime).toString();
        }

        return time_text;
    }

    public String getRelativeHourAndDaysAndWeek(String uploadedTime) {

        Calendar getTime = Calendar.getInstance(Locale.KOREAN);

        int currentYEAR = getTime.get(Calendar.YEAR);
        getTime.setTimeInMillis(Long.parseLong(uploadedTime));

        long beforeTime = Long.parseLong(uploadedTime);
        long currentTime = System.currentTimeMillis();

        int itemYEAR = Integer.parseInt(DateFormat.format("yy", beforeTime).toString());


        int makeGapTime = (int) (currentTime - beforeTime) / 60000;
        String time_text = "";
        if (makeGapTime < 1) {
            time_text = "방금 전";
        } else if (makeGapTime >= 1 && makeGapTime < 60) {
            time_text = makeGapTime + "분 전";

        } else if (makeGapTime / 60 >= 1 && makeGapTime / 60 <= 24) {
            time_text = (makeGapTime / 60) + "시간 전";

        } else if (makeGapTime / (60*24) >= 1 && makeGapTime / (60*24) <= 7) {
            time_text = (makeGapTime / (60*24)) + "일 전";

        } else if (makeGapTime / (60*24*7) >= 1 && makeGapTime / (60*24*7) <= 4) {
            time_text = (makeGapTime / 60) + "주일 전";

        } else if (currentYEAR == itemYEAR) {
            time_text = DateFormat.format("MM-dd hh:mm aa", beforeTime).toString();
        } else {
            time_text = DateFormat.format("yy-MM-dd hh:mm aa", beforeTime).toString();
        }

        return time_text;

    }
}
