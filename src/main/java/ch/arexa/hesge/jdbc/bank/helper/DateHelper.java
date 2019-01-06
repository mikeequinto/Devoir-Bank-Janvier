package ch.arexa.hesge.jdbc.bank.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {


    public static Date toDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    }

    public static String dateToString(Date date) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static java.sql.Date toSqlDate(Date d){
        return new java.sql.Date(d.getTime());
    }

    public  static Date fromSqlDate(java.sql.Date d){
        return new Date(d.getTime());
    }

}
