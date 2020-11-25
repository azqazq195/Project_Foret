package com.example.foret.helper;

import java.util.Locale;

public class Calendar {

    java.util.Calendar calendar;

    public String getCurrentTime() {
        calendar = java.util.Calendar.getInstance();
        int yy = calendar.get(java.util.Calendar.YEAR);
        int mm = calendar.get(java.util.Calendar.MONTH) + 1;
        int dd = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        int hh = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int mi = calendar.get(java.util.Calendar.MINUTE);

        String currentTime = String.format("%02d월 %02d일 %02d시 %02d분", mm,dd, hh, mi);
        return currentTime;
    }

    public String getCurrentTimeFull() {
        calendar = java.util.Calendar.getInstance(Locale.KOREAN);
        //calendar.setTimeInMillis();
        int yy = calendar.get(java.util.Calendar.YEAR);
        int mm = calendar.get(java.util.Calendar.MONTH) + 1;
        int dd = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        int hh = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int mi = calendar.get(java.util.Calendar.MINUTE);

        String currentTime = String.format("%02d일 %02d시 %02d분", dd, hh, mi);
        return currentTime;
    }
}
