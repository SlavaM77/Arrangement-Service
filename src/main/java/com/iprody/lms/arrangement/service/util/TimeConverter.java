package com.iprody.lms.arrangement.service.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class TimeConverter {

    public static Instant convertStringDateToInstant(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}
