package com.example.mockproject.utils.common;

import com.example.mockproject.utils.constant.DateFormat;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.exception.ApiRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static SimpleDateFormat dtoFormat = new SimpleDateFormat(DateFormat.DATETIME);
    public static SimpleDateFormat entityFormat = new SimpleDateFormat(DateFormat.ISO8601);

    /**
     * This method convert String (dd/MM/yyyy HH:mm:ss) to Date (yyyy-MM-dd HH:mm:ss)
     *
     * @param inDate String
     * @return Date
     */
    public static Date convertStringToDate(String inDate) {
        dtoFormat.setLenient(false);
        Date outDate = null;
        if (inDate != null) {
            try {
                Date date = dtoFormat.parse(inDate);
                outDate = entityFormat.parse(entityFormat.format(date));
            } catch (ParseException e) {
                throw new ApiRequestException(Message.MESSAGE_026);
            }
        }

        return outDate;
    }

    /**
     * This method convert Date (yyyy-MM-dd) to String with dateformat
     *
     * @param inDate     Date
     * @param dateFormat dateformat
     * @return String
     */
    public static String convertDateToString(Date inDate, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(inDate);
    }

    /**
     * This method convert Date (yyyy-MM-dd) to String (dd/MM/yyyy)
     *
     * @param inDate Date
     * @return String
     */
    public static String convertDateToString(Date inDate) {
        return dtoFormat.format(inDate);
    }

    public static boolean dateCompareTo(Date from, Date to) {
        return from.compareTo(to) < 0;
    }
    public static boolean checkAge(String date) {
        Date dob = convertStringToDate(date);
        LocalDate localDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = LocalDate.now().getYear() - localDate.getYear();
        if (age >= 18 && age <= 60) {
            return true;
        }
        return false;
    }
}
