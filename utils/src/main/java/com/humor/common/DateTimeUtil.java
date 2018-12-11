package com.humor.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangshaoze
 * @date 2018/11/21 3:02 PM
 */
public class DateTimeUtil {

    public  static  final  String  STANDARD_FORMAT  =  "yyyy-MM-dd HH：mm：ss" ;

    public  static  SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_FORMAT);

    public static Date strToDate(String dateTimeStr){
        try {
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToStr(Date date){
        if(date!=null){
            return sdf.format(date);
        }
        return null;

    }
}
