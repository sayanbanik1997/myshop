package com.google.sayanbanik1997.myshop;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Datetime {
    protected static String datetimeNowStr(){
        return LocalDate.now().getYear()+"-"+LocalDate.now().getMonthValue()+"-"+LocalDate.now().getDayOfMonth()+" "+ LocalTime.now().getHour()+"-"+LocalTime.now().getMinute()+"-"+LocalTime.now().getSecond();
    }
    protected static LocalDateTime parseDateTime(String dateTimeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime datetime = LocalDateTime.parse(dateTimeStr, formatter);
        return datetime;
    }
    protected static LocalDate parseDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }
//    protected static LocalTime parseTime(){
//
//    }
}
