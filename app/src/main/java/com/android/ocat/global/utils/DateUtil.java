package com.android.ocat.global.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 时间格式化工具类
 */
public class DateUtil {
    public static Date string2Date(String str) {
        if (str == null) {
            return null;
        }
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(str);
        return dateTime.toDate();
    }

    public static Date string2Date(String str, String format) {
        if (str == null) {
            return null;
        }
        DateTime dateTime = DateTimeFormat.forPattern(format).parseDateTime(str);
        return dateTime.toDate();
    }

    public static String date2String(Date date) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public static String date2String(Date date, String format) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }
}
