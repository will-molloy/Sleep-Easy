package wilmol.com.github.sleepeasy;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Represents a Time in a 12 Hour format.
 *
 * Note: this class has not been thoroughly tested yet @see TestTime12HourFormat.
 * However, it has been tested enough for what I intend it to be used for.
 *
 * @author will 2016-09-05
 */
public class Time12HourFormat {

    private int _hour;
    private int _minute;
    private boolean _isAM;

    /**
     * Creates a 12HourTimeFormat object
     * @param hour time in hours. Must be between 0 and 12.
     * @param minute time in mins. Must be between 0 and 60.
     * @param AM_PM true if AM. false if PM.
     *
     * @throws IllegalArgumentException if hours/minutes exceed their range.
     */
    public Time12HourFormat(int hour, int minute, boolean AM_PM){
        _hour = hour;
        _minute = minute;
        _isAM = AM_PM;
        if (hour > 11 || hour < 0){
            throw new IllegalArgumentException("Hour exceeds [0..11] range.");
        }
        if (minute > 60 || minute < 0) {
            throw new IllegalArgumentException("Minute exceeds [0..60] range.");
        }
    }

    /**
     * Adds nintey minutes to the current time for the specified amount of iterations.
     * E.g. time.addNinteyMinutesXTimes(2) adds 3 hours to time.
     * Returns a new Time12HourFormatObject with 90*x mins added.
     */
    public Time12HourFormat addNinteyMinutesXTimes(int x) {
        Time12HourFormat time = new Time12HourFormat(_hour, _minute, _isAM);
        while (x-- > 0) {
            time = time.addNinteyMinutes();
        }
        return time;
    }

    /**
     * Adds nintey minutes to the Time12HourFormat object.
     * Returns a new Time12HourFormatObject with 90 mins added.
     */
    private Time12HourFormat addNinteyMinutes(){
        Time12HourFormat time = new Time12HourFormat(_hour, _minute, _isAM);
        return time.add(new Time12HourFormat(1,30,true));
    }

    /**
     * Adds a 12hour time to this 12hour time.
     * Returns a new Time12HourFormat object with the specified time added.
     */
    public Time12HourFormat add(Time12HourFormat time){
        int totalMinutes = getTotalMinutes(this, time, true);
        return extractHoursAndMinutesAndGenerate12HourTime(totalMinutes);
    }

    /**
     * Adds or subtracts two times (subtracting time2 FROM time1) depending the boolean adding.
     * @return the total minutes left over.
     * It is assumed that time1 is greater than time2 when subtracting (else overlaps into the previous day).
     */
    private int getTotalMinutes(Time12HourFormat time1, Time12HourFormat time2, boolean adding) {
        // convert hours to minutes, +12 if PM
        int time1Hour = time1._hour;
        time1Hour += time1._isAM ? 0 : 12;

        int time2Hour = time2._hour;
        time2Hour += time2._isAM ? 0 : 12;

        int totalMinutes = 0;
        if (adding){
            // if adding time1 and time2
            totalMinutes += time1._minute + time2._minute;
            totalMinutes += (time1Hour + time2Hour) * 60;
        } else {
            // if subtracting time2 from time1
            time1Hour += 24;
            totalMinutes += time1._minute - time2._minute;
            totalMinutes += (time1Hour - time2Hour) * 60;
        }

        return totalMinutes;
    }

    private Time12HourFormat extractHoursAndMinutesAndGenerate12HourTime(int totalMinutes) {
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;

        // get am/pm, initially it is AM if one time is AM
        boolean isAM = true;

        // keep subtracting from hours until less than 12, while rotating through AM/PM
        while (hour >= 12) {
            hour -= 12;
            isAM = !isAM;
        }

        return new Time12HourFormat(hour, minute, isAM);
    }

    public Time12HourFormat subtractNinteyMinutesXTimes(int x) {
        Time12HourFormat time = new Time12HourFormat(_hour, _minute, _isAM);
        while (x-- > 0){
            time = time.subtractNinteyMinutes();
        }
        return time;
    }

    private Time12HourFormat subtractNinteyMinutes() {
        Time12HourFormat time = new Time12HourFormat(_hour, _minute, _isAM);
        return time.subtract(new Time12HourFormat(1, 30, true));
    }

    /**
     * Subtracts the specified amount of time from this time object.
     */
    public Time12HourFormat subtract(Time12HourFormat time) {
        int totalMinutes = getTotalMinutes(this, time, false);
        return extractHoursAndMinutesAndGenerate12HourTime(totalMinutes);
    }

    /**
     * Returns a double of the hours/minutes from a 12 hour time.
     * E.g. (0,15,true) will return 0.25.
     */
    protected double getHoursFrom12HourTime() {
        double totalMins = _hour;
        totalMins += _isAM ? 0 : 12; // +12 if pm
        totalMins *= 60;
        totalMins += _minute;
        totalMins /= 60; // now totalHours
        return totalMins;
    }

    /**
     * Returns the current time in a 12 hour format.
     */
    public static Time12HourFormat getCurrentTime(){
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timeZone);

        boolean isAM = calendar.get(Calendar.AM_PM) == Calendar.AM;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour >= 12) {
            hour -= 12; // For phones with 24 hour time. (Calendar class gets 12 hour time.. but my samsung s3 doesn't ?)
        }

        return new Time12HourFormat(hour, minute, isAM);
    }

    // getters
    public int hour() {
        return _hour;
    }

    public int minute() {
        return _minute;
    }

    public boolean isAM() {
        return _isAM;
    }

    @Override
    public String toString(){
        String minute = _minute + "";
        String hour = "";
        if (_minute < 10){
            minute = "0" + minute;
        }

        if (_hour == 0){
            hour = 12 + ""; // to show 12am / 12pm instead of 0am / 0pm respectively
        } else {
            hour = _hour + "";
        }
        String AM_PM = _isAM ? "AM" : "PM";
        return hour + ":" + minute + AM_PM;
    }

}
