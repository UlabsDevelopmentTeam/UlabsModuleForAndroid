package com.ulabs.ulabsmodule.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OH-Biz on 2017-09-08.
 */

public class AlarmWatcher{
    /**
     * AlarmWatcher class
     * AlarmManager class를 wrapping한 util class
     * 이외에 시간 변환과 관련된 util들도 같이 정의되었다.
     * @singleton
     * */
    private static AlarmWatcher watcher;
    private AlarmManager alarmManager;
    private static final long ONE_HOUR_MILLIS = 1000*60*60;
    private static final long ONE_DAY_MILLIS = ONE_HOUR_MILLIS * 24;

    private AlarmWatcher(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static AlarmWatcher getInstance(Context context){
        if(watcher == null){
            watcher = new AlarmWatcher(context);
        }

        return watcher;
    }

    /**
     * startAlarm
     * 현재시간을 기준으로 delay변수 만큼 시간 값을 추가하여, 알람을 발생시킨다.
     * pendingIntent -> 알람에서 실행할 내용이 들어있는 intentSender.
     * delay -> 추가할 시간 delay (ms단위)
     * */
    public void startAlarm(PendingIntent pendingIntent, int delay) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }
    }

    /**
     * startAlarmRepeating
     * 현재시간을 기준으로 delay(ms)만큼 시간 값을 추가하여 알람을 발생시키고,
     * nextTime값(ms)만큼 알람을 반복시킨다. (Scheduling)
     * */
    public void startAlarmRepeating(PendingIntent pendingIntent, int delay, long nextTime){
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, nextTime, pendingIntent);
    }
    /**
     * startEduCodePushAlarm
     * yyyy-MM-dd HH:mm:ss형태의 string 시간값을 ms단위로 변환시켜서 알람을 발생시킨다.
     * time -> yyyy-MM-dd HH:mm:ss format의 string
     * */
    public void startEduCodePushAlarm(PendingIntent pendingIntent, String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = format.parse(time);
            long date_millis = date.getTime();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date_millis, pendingIntent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, date_millis, pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * startEduCodePushAlarmRepeating
     * firstTimeMillis값의 시간(ms)에 알람을 반복시키고 (nextTimeMillis값의 시간(ms) - firstTimeMillis값의 시간(ms)) 의 주기를 가지고 반복시킨다.(Scheduling)
     * */
    public void startEduCodePushAlarmRepeating(PendingIntent pendingIntent, long firstTimeMillis, long nextTimeMillis){
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstTimeMillis, nextTimeMillis - firstTimeMillis, pendingIntent);
    }

    /**
     * stopAlarm
     * AlarmManager에 등록되어 있는 Scheduling을 취소한다.
     * */
    public void stopAlarm(PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
    }

    /**
     * getTomorrowMillis
     * yyyy-MM-dd HH:mm:ss 형태의 string 시간를 이용하여,
     * 해당 날짜의 다음날 자정(00:00)인 시간을 ms단위로 리턴한다.
     * */
    public long getTomorrowMillis(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_0clock = today.substring(0,11) + "00:00:00";

        try {
            Date today_0clock = format.parse(time_0clock);
            long today_0clock_millis = today_0clock.getTime();
            long tomorrow_millis = today_0clock_millis + ONE_DAY_MILLIS;
            return tomorrow_millis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long getTomorrowMillis(String today, String HHmmssFormat){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_0clock = today.substring(0,11) + HHmmssFormat;

        try {
            Date today_0clock = format.parse(time_0clock);
            long today_0clock_millis = today_0clock.getTime();
            long tomorrow_millis = today_0clock_millis + ONE_DAY_MILLIS;
            return tomorrow_millis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getYesterdayString(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_0clock = today.substring(0,11) + "00:00:00";

        try {
            Date today_0clock = format.parse(time_0clock);
            long today_0clock_millis = today_0clock.getTime();
            long yesterday_millis = today_0clock_millis - ONE_DAY_MILLIS;
            Date yesterday = new Date(yesterday_millis);
            return format.format(yesterday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getEndOfYesterdayString(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_0clock = today.substring(0,11) + "23:59:59";

        try {
            Date today_0clock = format.parse(time_0clock);
            long today_0clock_millis = today_0clock.getTime();
            long yesterday_millis = today_0clock_millis - ONE_DAY_MILLIS;
            Date yesterday = new Date(yesterday_millis);
            return format.format(yesterday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * getYesterday
     * yyyy-MM-dd HH:mm:ss 형태의 string 날짜를 이용하여,
     * 해당 날짜의 하루 전 자정(00:00)인 시간을 ms단위로 리턴한다.
     * */
    public long getYesterdayMillis(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_0clock = today.substring(0,11) + "00:00:00";

        try {
            Date today_0clock = format.parse(time_0clock);
            long today_0clock_millis = today_0clock.getTime();
            long yesterday_millis = today_0clock_millis - ONE_DAY_MILLIS;
            return yesterday_millis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public long getEndOfYesterdayMillis(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_end = today.substring(0,11) + "23:59:59";

        try {
            Date date_today = format.parse(time_end);
            long today_millis = date_today.getTime();
            return today_millis - ONE_DAY_MILLIS;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * getTodayCertainTimeMillis
     * yyyy-MM-dd HH:mm:ss 형태의 string을 이용하여 이 형태의 날짜에서
     * HH:mm:ss형태의 원하는 시간값을 ms형태로 리턴한다.
     * */
    public long getTodayCertainTimeMillis(String today, String HHmmssFormat){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_certain = today.substring(0,11) + HHmmssFormat;

        try {
            Date today_certain = format.parse(time_certain);
            return today_certain.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public String getTodayCertainTimeString(String today, String HHmmssFormat){
        return today.substring(0,11) + HHmmssFormat;
    }

    public Date getTodayCertainTimeDateObject(String today, String HHmmssFormat){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_certain = today.substring(0,11) + HHmmssFormat;
        try {
            return format.parse(time_certain);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * getDateStringFormat
     * ms형태의 시간값을 yyyy-MM-dd HH:mm:ss 형태의 string 시간값으로 변환해준다.
     * */
    public String getDateStringFormat(long time_millis){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time_millis);
        return format.format(date);
    }

}
