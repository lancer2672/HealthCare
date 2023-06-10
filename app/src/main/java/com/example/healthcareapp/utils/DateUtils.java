package com.example.healthcareapp.utils;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DateUtils {
        private static final String FORMAT = "dd/mm/yyyy";

        public static String convertCurrentDateToChartDate(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
            return dateFormat.format(date);
        }
        public static int getMonthFromDate(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH) + 1; // Add 1 to get the month number starting from 1
        }
}
