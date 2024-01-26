package com.dailystudy.dtmsapi.util;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {
    private static final String defaultFormat = "yyyyMMdd";
    private static final String normalFormat = "yyyy-MM-dd";
    private static final String defaultTimeFormat = "yyyyMMddHHmmss";

	public static String dateTime(String pattern) {
		return dateTime(pattern, 0);
	}
	
	public static String dateTime(String pattern, int addDays) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newDateTime;
        if(addDays >= 0) {
            newDateTime = now.plusDays(addDays);
        } else {
            newDateTime = now.minusDays(-addDays);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return newDateTime.format(dateTimeFormatter);
	}

    public static String date(String pattern) {
        return date(pattern, 0);
    }

    public static String date(String pattern, String date) {
        return date(pattern, 0, date);
    }

    public static String date(String pattern, int addDays) {
       return date(pattern, addDays, null);
    }

    public static String date(String pattern, int addDays, String originDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        LocalDate date = LocalDate.now();
        if(originDate != null) {
            date = LocalDate.parse(originDate, dateTimeFormatter);
        }

        LocalDate localDate;
        if(addDays >= 0) {
            localDate = date.plusDays(addDays);
        } else {
            localDate = date.minusDays(-addDays);
        }

        return localDate.format(dateTimeFormatter);
    }

    public static int getDateMonth(String pattern, String originDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        LocalDate date = LocalDate.parse(originDate, dateTimeFormatter);

        return date.getMonthValue();
    }

    public static String getNormalCurrentDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(normalFormat);

        return LocalDate.now().format(dateTimeFormatter);
    }

    public static String getCurrentDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(defaultFormat);

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentYear() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentMonth() {
        return getCurrentMonth("MM");
    }

    public static String getCurrentMonth(String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentDay() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd");

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentHours() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH");

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentMinutes() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm");

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentSeconds() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ss");

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(defaultTimeFormat);

        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static long getCurrentTimestamp() {
	    return getTimeStampOfDateTime(defaultTimeFormat, dateTime(defaultTimeFormat));
    }

    public static String getDateOfTimestamp(String pattern, long timestamp)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        return new Timestamp(timestamp * 1000).toLocalDateTime().format(dateTimeFormatter);
    }

    public static long getTimeStampOfDateTime(String pattern, String dateTime)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);

        return Timestamp.valueOf(localDateTime).getTime() / 1000;
    }

    public static long getTimeStampOfDate(String pattern, String date)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

        return Timestamp.valueOf(localDate.atTime(LocalTime.MIDNIGHT)).getTime() / 1000;
    }

    public static String timestampToDayName(long timestamp) {
        LocalDateTime localDateTime = new Timestamp(timestamp * 1000).toLocalDateTime();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

    public static String dateToDayName(String pattern, String date) {
        return dateToDayName(pattern, date, TextStyle.SHORT, Locale.KOREAN);
    }

    public static String dateToDayName(String pattern, String date, TextStyle textStyle, Locale locale) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);

        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        return dayOfWeek.getDisplayName(textStyle, locale);
    }

    public static String currentDateToDayName(TextStyle textStyle, Locale locale) {
        return LocalDateTime.now().getDayOfWeek().getDisplayName(textStyle, locale);
    }
}
