package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import wilmol.com.github.sleepeasy.tools.TimePickerTool;

/**
 * Options the user can set;
 * For example the time to fall asleep (more coming soon?)
 *
 * @author will 2016-09-06
 */
public class OptionsActivity extends AppCompatActivity {

    private static Class previousActivityClass;

    // Settings the user can change
    private static Time12HourFormat TIME_TO_FALL_ASLEEP = new Time12HourFormat(0,15,true);

    // Getters to get these settings
    public static Time12HourFormat getTimeToFallAsleep() { return TIME_TO_FALL_ASLEEP; }

    // time picker and tool
    private TimePickerTool timePickerTool;

    /**
     * REQUIRED:
     * Set the previous Activity so that the back button will go back to the correct Activity
     * after saving changes.
     */
    public static void setPreviousActivityClass(Class activityClass) {
        previousActivityClass = activityClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker_options);
        timePickerTool = new TimePickerTool(timePicker);

        timePickerTool.set24hourTime(TIME_TO_FALL_ASLEEP);
        setTimeToFallAsleep();
    }

    private void setTimeToFallAsleep() {
        TIME_TO_FALL_ASLEEP = timePickerTool.get12HourTime();
    }

    /**
     * Save changes and go to previous Activity.
     */
    @Override
    public void onBackPressed() {

        // save changes
        setTimeToFallAsleep();

        // Go back to previous page
        try {
            Intent intent = new Intent(this, previousActivityClass);
            startActivity(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Previous activity not set");
        }
    }

}
