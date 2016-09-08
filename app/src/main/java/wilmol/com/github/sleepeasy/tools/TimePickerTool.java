package wilmol.com.github.sleepeasy.tools;

import android.widget.TimePicker;

import wilmol.com.github.sleepeasy.R;
import wilmol.com.github.sleepeasy.Time12HourFormat;

/**
 * Created by will on 8/09/16.
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
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        boolean isAM = true;
        if ( hour > 12){
            isAM = false;
            hour -= 12;
        }
        return new Time12HourFormat(hour,minute,isAM);
    }
}
