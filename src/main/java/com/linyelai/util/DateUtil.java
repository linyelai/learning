package com.linyelai.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static LocalDateTime getCurrentDate(){

        return LocalDateTime.now();
    }

    public static LocalDateTime addTime(LocalDateTime date,long second){
        LocalDateTime expireDate = date.plus(second, ChronoUnit.SECONDS);
        return expireDate;
    }
}
