package com.example.healthcareapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
public class DateUtils {
        private static final String FORMAT = "dd/mm/yyyy";

        public static String convertCurrentDateToChartDate(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
            return dateFormat.format(date);
        }
}
