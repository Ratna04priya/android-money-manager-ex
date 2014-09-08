package com.money.manager.ex.core;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.money.manager.ex.Constants;
import com.money.manager.ex.database.MoneyManagerOpenHelper;
import com.money.manager.ex.database.TableInfoTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alessandro Lazzari on 08/09/2014.
 */
public class DateUtils {
    private static final String LOGCAT = DateUtils.class.getSimpleName();

    /**
     * Convert string date into date object using pattern define to user
     *
     * @param ctx  context
     * @param date string to convert
     * @return date converted
     */
    public static Date getDateFromString(Context ctx, String date) {
        return getDateFromString(ctx, date, getUserDatePattern(ctx));
    }

    /**
     * Convert string date into date object using pattern params
     *
     * @param date    string to convert
     * @param pattern to use for convert
     * @return date object converted
     */
    public static Date getDateFromString(Context ctx, String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            Log.e(LOGCAT, e.getMessage());
        }
        return null;
    }

    /**
     * Convert date object to string from user pattern
     *
     * @param date
     * @return
     */
    public static String getStringFromDate(Context ctx, Date date) {
        return getStringFromDate(ctx, date, getUserDatePattern(ctx));
    }

    /**
     * @param date    object to convert in string
     * @param pattern pattern to use to convert
     * @return
     */
    public static String getStringFromDate(Context ctx, Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Convert date object in string SQLite date format
     *
     * @param date to convert
     * @return string formatted date SQLite
     */
    public static String getSQLiteStringDate(Context ctx, Date date) {

        return getStringFromDate(ctx, date, Constants.PATTERN_DB_DATE);
    }

    /**
     * Get pattern define from user
     *
     * @return pattern user define
     */
    public static String getUserDatePattern(Context ctx) {
        TableInfoTable infoTable = new TableInfoTable();
        MoneyManagerOpenHelper helper = new MoneyManagerOpenHelper(ctx);
        Cursor cursor = helper.getReadableDatabase().query(infoTable.getSource(), null, TableInfoTable.INFONAME + "=?", new String[]{"DATEFORMAT"}, null, null, null);
        String pattern = null;
        if (cursor != null && cursor.moveToFirst()) {
            pattern = cursor.getString(cursor.getColumnIndex(TableInfoTable.INFOVALUE));
            //replace part of pattern
            pattern = pattern.replace("%d", "dd").replace("%m", "MM").replace("%y", "yy").replace("%Y", "yyyy").replace("'", "''");
        }
        //close cursor and helper
        cursor.close();
        helper.close();

        return pattern;
    }

    /**
     * @param date    to start calculate
     * @param repeats type of repeating transactions
     * @return next Date
     */
    public static Date getDateNextOccurence(Date date, int repeats) {
        if (repeats >= 200) {
            repeats = repeats - 200;
        } // set auto execute without user acknowlegement
        if (repeats >= 100) {
            repeats = repeats - 100;
        } // set auto execute on the next occurrence
        // create object calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (repeats) {
            case 0: //none
                break;
            case 1: //weekly
                calendar.add(Calendar.DATE, 7);
                break;
            case 2: //bi_weekly
                calendar.add(Calendar.DATE, 14);
                break;
            case 3: //monthly
                calendar.add(Calendar.MONTH, 1);
                break;
            case 4: //bi_monthly
                calendar.add(Calendar.MONTH, 2);
                break;
            case 5: //quaterly
                calendar.add(Calendar.MONTH, 3);
                break;
            case 6: //half_year
                calendar.add(Calendar.MONTH, 6);
                break;
            case 7: //yearly
                calendar.add(Calendar.YEAR, 1);
                break;
            case 8: //four_months
                calendar.add(Calendar.MONTH, 4);
                break;
            case 9: //four_weeks
                calendar.add(Calendar.DATE, 28);
                break;
            case 10: //daily
                calendar.add(Calendar.DATE, 1);
                break;
            case 11: //in_x_days
            case 12: //in_x_months
            case 13: //every_x_days
            case 14: //every_x_months
        }
        return calendar.getTime();
    }
}
