package com.aya.bicycle006.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Single on 2016/3/29.
 */
public class DateUtils {
    private static SimpleDateFormat ApiTimeFormatTZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static SimpleDateFormat ApiDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat ApiDateFormatS = new SimpleDateFormat("yyyy/M/d");
    private static SimpleDateFormat ApiTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static long MS_OF_ONE_DAY = 24 * 60 * 60 * 1000;

    public static String toDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    /**
     * 计算年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAge(Date birthDay) {
        Calendar calendar = Calendar.getInstance();

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthDay);
        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow < monthBirth) {
            age--;
        } else if (monthNow == monthBirth) {
            if (dayOfMonthNow <= dayOfMonthBirth) {
                age--;
            }
        }
        return age;
    }

    public static Date parseApiTime(String timeStr) {
        Date date = null;
        try {
            date = ApiTimeFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parseApiDate(String timeStr) {
        Date date = null;
        try {
            date = ApiTimeFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 解析gank时间
     *
     * @param timeStr
     * @return
     */
    public static Date parseApiDateTZ(String timeStr) {
        Date date = null;
        try {
            date = ApiTimeFormatTZ.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatApiTime(Date date) {
        return ApiTimeFormat.format(date);
    }

    public static String formatApiDate(Date date) {
        return ApiDateFormat.format(date);
    }

    public static String formatApiTime(Calendar calendar) {
        return ApiTimeFormat.format(calendar.getTime());
    }

    public static String formatApiDate(Calendar calendar) {
        return ApiDateFormat.format(calendar.getTime());
    }

    public static String formatApiDateS(Date date) {
        return ApiDateFormatS.format(date);
    }

    public static String formatApiDateS(Calendar calendar) {
        return ApiDateFormatS.format(calendar);
    }

    /***
     * 比较两个Calender日期
     *
     * @param c1
     * @param c2
     * @return c1-c2
     */
    public static long compareDate(Calendar c1, Calendar c2) {
        long date1 = c1.getTimeInMillis() / MS_OF_ONE_DAY;
        long date2 = c2.getTimeInMillis() / MS_OF_ONE_DAY;
        return date1 - date2;
    }

    /**
     * 对比两个日期
     *
     * @param d1 yyyy-MM-dd
     * @param d2 yyyy-MM-dd
     * @return d1 - d2
     */
    public static long compareDate(Date d1, Date d2) {
        long date1 = parseApiDate(formatApiDate(d1)).getTime() / MS_OF_ONE_DAY;
        long date2 = parseApiDate(formatApiDate(d2)).getTime() / MS_OF_ONE_DAY;
        return date1 - date2;
    }

    /**
     * 根据开始日期，结束日期获取和列表中相同的日期
     *
     * @param startDate
     * @param endDate
     * @param holidays  “yyyy-MM-dd”
     * @return
     */
    public static ArrayList<Calendar> getSelectedHolidays(Date startDate, Date endDate, ArrayList<String> holidays) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        return getSelectedHolidays(start, end, holidays);
    }

    /**
     * 根据开始日期和结束日期，获取和列表相同的日期
     *
     * @param startDate
     * @param endDate
     * @param holidays  “yyyy-MM-dd”
     * @return
     */
    public static ArrayList<Calendar> getSelectedHolidays(Calendar startDate, Calendar endDate, ArrayList<String> holidays) {
        ArrayList<Calendar> selectedHolidays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate.getTimeInMillis());
        while (DateUtils.compareDate(endDate, calendar) > 0) {
            String dateStr = DateUtils.ApiDateFormat.format(calendar.getTime());
            for (int i = 0; i < holidays.size(); i++) {
                if (dateStr.equals(holidays.get(i))) {
                    Calendar selectedHoliday = Calendar.getInstance();
                    selectedHoliday.setTimeInMillis(calendar.getTimeInMillis());
                    selectedHolidays.add(selectedHoliday);
                    break;
                }
            }
            calendar.add(Calendar.DATE, 1);
        }
        return selectedHolidays;
    }

    public static Date getLastdayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date getNextDayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static boolean isTheSameDay(Date d1, Date d2) {
        int d = (int) DateUtils.compareDate(d1, d2);
        if (d == 0) {
            return true;
        }
        return false;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
