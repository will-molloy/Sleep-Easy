package wilmol.com.github.sleepeasy.tools;

import android.widget.TimePicker;

import wilmol.com.github.sleepeasy.Time12HourFormat;

/**
 * Tool used to set and get times from a TimePicker component.
 *
 * @author will 2016-09-08
 */
public class TimePickerTool {

    private TimePicker timePicker;

    public TimePickerTool(TimePicker timePicker){
        this.timePicker = timePicker;
    }

    /**
     * Gets the time the time picker and returns a Time12HourFormat object.
     */
    public Time12HourFormat get12HourTime() {
        // deprecated but only way for it to work on android 5.2 or earlier.
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        boolean isAM = true;
        if (hour >= 12){
            isAM = false;
            hour -= 12;
        }
        return new Time12HourFormat(hour,minute,isAM);
    }

    public void set12HourTime(Time12HourFormat time) {
        timePicker.setIs24HourView(false);
        int hour = time.hour();
        hour += time.isAM() ? 0 : 12;
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(time.minute());
    }

    public void set24hourTime(Time12HourFormat time) {
        timePicker.setIs24HourView(true);
        int hour = time.hour();
        hour += time.isAM() ? 0 : 12;
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(time.minute());
    }
}
