package com.certuit.base.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class DateUtil {

    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-ES");
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yy hh:mm a", LOCALE_ES);

    /**
     * Formatea una fecha en formato español corto (ej: 07/abr/25 03:15 PM).
     */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    /**
     * Devuelve la diferencia en días entre dos fechas.
     */
    public static long getDifferenceDays(LocalDate start, LocalDate end) {
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    }

    /**
     * Devuelve el primer y último día de un mes dado.
     */
    public static LocalDate[] getFirstAndLastDayOfMonth(int month, int year) {
        LocalDate first = LocalDate.of(year, month, 1)
                .with(TemporalAdjusters.firstDayOfMonth());
        LocalDate last = first.with(TemporalAdjusters.lastDayOfMonth());
        return new LocalDate[]{first, last};
    }

    /**
     * Convierte un Date (legacy) a LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(java.util.Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    /**
     * Convierte LocalDateTime a Date
     */
    public static java.util.Date toDate(LocalDateTime dateTime) {
        return java.util.Date.from(dateTime.atZone(DEFAULT_ZONE).toInstant());
    }
}