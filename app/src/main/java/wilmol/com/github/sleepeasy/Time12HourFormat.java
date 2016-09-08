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
        if (hour > 12 || hour < 0){
            throw new IllegalArgumentException("Hour exceeds [0..12] range.");
        }
        if (minute > 60 || minute < 0) {
            throw new IllegalArgumentException("Minute exceeds [0..60] range.");
        }
    }

    /**
     * Adds a 12hour time to this 12hour time.
     * Returns a new Time12HourFormat object with the specified time added.
     */
    public Time12HourFormat add(Time12HourFormat time){
        // convert hours to minutes, +12 if PM
        int time1hour = time._hour;
        time1hour += time._isAM ? 0 : 12;

        int time2hour = _hour;
        time2hour += _isAM ? 0 : 12;

        // compute total minutes
        int totalMinutes = time._minute + _minute;
        totalMinutes += (time1hour + time2hour) * 60;

        // extract new hours and minutes
        int hour = totalMinutes /60;
        int minute = totalMinutes % 60;

        // get am/pm, initially it is AM if one time is AM
        boolean isAM = true;

        // keep subtracting from hours until less than 12, while rotating through AM/PM
        while (hour > 12){
            hour -= 12;
            isAM = !isAM;
        }

        return new Time12HourFormat(hour, minute, isAM);
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
     * Subtracts the specified amount of time from this time object.
     */
    public Time12HourFormat subtract(Time12HourFormat time) {
        // time1 = time taken away from time2
        // convert hours to minutes, +12 if PM
        int time1hour = time._hour;
        time1hour += time._isAM ? 0 : 12;

        int time2hour = _hour;
        time2hour += _isAM ? 0 : 12;
        time2hour += 24; // add 24 hours to this time

        // compute total minutes
        int totalMinutes = _minute - time._minute;
        totalMinutes += (time2hour - time1hour) * 60;

        // extract new hours and minutes
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;

        // get am/pm, initially it is AM if one time is AM
        boolean isAM = true;

        // keep subtracting from hours until less than 12, while rotating through AM/PM
        while (hour > 12) {
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

    @Override
    public String toString(){
        String minute = _minute + "";
        if (_minute < 10){
            minute = "0" + minute;
        }
        String AM_PM = null;
        if (_hour == 12){
            AM_PM = !_isAM ? "AM" : "PM";    // flip AM/PM if it is the 12th hour.
        } else {                             // CANNOT invert the field itself;
            AM_PM = _isAM ? "AM" : "PM";     // this causes a fault when copying an object.
        }
        return _hour + ":" + minute + AM_PM;
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

        if (hour > 12) {
            hour -= 12;       // For phones with 24 hour time. (Calendar class gets 12 hour time.. but my samsung s3 doesn't ?)
        }
        if (hour == 0) {
            hour = 12;       // special case if 0:00am/0:00pm, I want to show 12:00am/12:00pm respectively.
            isAM = !isAM;    // isAM is inverted because.. Time12HourFormat.toString() inverts isAM if the hour is 12
            // this is because the java.util.Calendar class is using hours [0..11] (and i'm using [1..12])
        }

        return new Time12HourFormat(hour, minute, isAM);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time12HourFormat)) {
            return false;
        } else {
            Time12HourFormat time = (Time12HourFormat) obj;
            return this.toString().equals(time.toString());
        }
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

}
