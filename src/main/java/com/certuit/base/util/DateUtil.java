package com.certuit.base.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static String formatDate(Date date){
        Locale esLocale = new Locale("es", "ES");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yy hh:mm a", esLocale);
        return formatter.format(date);
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Date[] getFirstAndLastDayOfMonth(Calendar calendar, int numberOfMonths, int year) {
        Date[] dates = new Date[2];
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MONTH, numberOfMonths - 1);
        calendar.set(Calendar.YEAR, year);
        LocalDateTime firstDayOfMonth = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .with(TemporalAdjusters.firstDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.YEAR, year);
        LocalDateTime lastDayOfMonth = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .with(TemporalAdjusters.lastDayOfMonth());
        dates[0] = Date.from(firstDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
        dates[1] = Date.from(lastDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
        return dates;
    }

}
