package wilmol.com.github.sleepeasy;

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
     * Returns a string in the format hh:mmAM|PM
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
     * Adds nintey minutes to the Time12HourFormat object
     * @return a String of the new Time added with 90 mins.
     */
    private Time12HourFormat addNinteyMinutes(){
        Time12HourFormat time = new Time12HourFormat(_hour, _minute, _isAM);
        return time.add(new Time12HourFormat(1,30,true));
    }

    /**
     * Adds nintey minutes to the current time for the specified amount of times.
     * E.g. time.addNinteyMinutesXTimes(2) adds 3 hours to time.
     */
    public Time12HourFormat addNinteyMinutesXTimes(int x) {
        Time12HourFormat time = new Time12HourFormat(_hour, _minute, _isAM);
        while (x-- > 0){
            time = time.addNinteyMinutes();
        }
        return time;
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

}
