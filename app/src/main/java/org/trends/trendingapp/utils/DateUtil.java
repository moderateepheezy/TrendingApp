package org.trends.trendingapp.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple utility class that formats the date based on the formatting needs.
 */
public class DateUtil {

    private static final String TAG = DateUtil.class.getSimpleName();
    private static final String DATE_FORMAT = "yyyy-MM-dd T HH:mm:ss";
    private static final String SERVER_TIME_ZONE = "GMT";

    /**
     * Simple helper enum that will allow to format any date the way you want.
     */
    public enum DateFormat {

        COMPLETE_DATE_FORMAT("E, MMM dd yyyy @ h:mm a"),
        DAY_FORMAT("MMM dd @ h:mm a"); // This one is an example of how we can use this enum to get ("Month day @ some time")

        private String format;

        DateFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

    }

    /**
     * Parsing date to the calendar object with the response and returns the Object for given timezone.
     *
     * @param dateAsString - string to parse into calendar
     * @param timezone     - timezone to convert parsed date to.
     * @return - calendar
     * @throws ParseException - thrown if the dateString fails to parse.
     */
    public static Calendar parseISODate(String dateAsString, String timezone) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(SERVER_TIME_ZONE));
        try {

            Calendar date = Calendar.getInstance();

            if (dateAsString != null && !(dateAsString.equalsIgnoreCase(""))) {

                date.setTime(simpleDateFormat.parse(dateAsString));
                date.setTimeZone(TimeZone.getTimeZone(timezone));

            }

            return date;

        } catch (ParseException e) {

            Log.d(TAG, "Failed to parse date: " + dateAsString + " to iso format: " + DATE_FORMAT);
            throw e;

        }

    }

    /**
     * Formats the passed in calendar to the DateFormat specified.
     *
     * @param date   - calendar to format
     * @param format - format to use
     * @return - formatted string
     */
    public static String formatDate(Calendar date, DateFormat format) {

        switch (format) {
            case COMPLETE_DATE_FORMAT:
                format = DateFormat.COMPLETE_DATE_FORMAT;
                break;
            case DAY_FORMAT:
                format = DateFormat.DAY_FORMAT;
                break;
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat(format.getFormat(), Locale.US);

        return dateFormatter.format(date.getTime());
    }

}
