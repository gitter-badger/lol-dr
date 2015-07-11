package com.ouchadam.loldr.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class SimpleDate {

    private static final SimpleDateFormat NAMED_DAY_MONTH_DAY_YEAR = new SimpleDateFormat("HH:mm EEE d MMMM yyyy", Locale.UK);

    private final Date date;

    public static SimpleDate from(long utcTimeStamp) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(utcTimeStamp));
        return new SimpleDate(date);
    }

    private SimpleDate(Date date) {
        this.date = date;
    }

    public long differenceFromNowInMillis() {
        return date.getTime() - System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return NAMED_DAY_MONTH_DAY_YEAR.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleDate that = (SimpleDate) o;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }

}
