package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import wilmol.com.github.sleepeasy.tools.TimePickerTool;

/**
 * The main screen.
 *
 * Gives the user an option to sleep now (calculator will use current time)
 * OR
 * Manually enter a wake up time (calculator will count backwards in sleep cycles).
 *
 * @author will 2016-09-05
 */
public class StartScreenActivity extends AppCompatActivity {

    // most common wake up time source: http://www.edisonresearch.com/wake-me-up-series-2/ 6:30am/7:00am
    private static Time12HourFormat wakeUpTime = new Time12HourFormat(7, 0, true); // default 7am
    private static Class previousActivityClass;
    private TimePickerTool timePickerTool;

    public static void setPreviousActivityClass(Class activityClass) {
        previousActivityClass = activityClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePickerTool = new TimePickerTool(timePicker);

        refreshTimePicker();
        syncWakeUpTimeAndTimePicker();
    }

    private void refreshTimePicker() {
        if (wakeUpTime.hour() == 12 && !wakeUpTime.isAM()){
            // case if TimePicker is showing 12:xxAM but isAM is false,
            // need to recreate object using hour as 0 and isAM as true.
            // (Otherwise, Time12HourFormat inverts isAM twice (so keeps as false)
            // therefore showing 12pm).

            // The reason for this:
            //  TimePicker uses a 24 hour format even though it is showing a 12 hour one and I didn't
            //  know this until I found this fault.
            //  I want to show 12am not 0am
            //  The Time12HourFormat class accepts '12' as an hour but really shouldn't
            wakeUpTime = new Time12HourFormat(0,wakeUpTime.minute(),true);
        }
        timePickerTool.set12HourTime(wakeUpTime);
    }

    private void syncWakeUpTimeAndTimePicker() {
        wakeUpTime = timePickerTool.get12HourTime();
        SleepAtGivenTimeActivity.setTime(wakeUpTime);
    }

    /**
     * Goes to the suggested bed times activity.
     */
    public void sleepAtGivenTime(View view) {
        // (GivenTimeActivity is set in onPause() method)
        Intent intent = new Intent(this, SleepAtGivenTimeActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the Sleep Now button is pressed.
     */
    public void sleepNow(View view){
        Intent intent = new Intent(this, SleepNowActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the settings button is pressed.
     */
    public void settings(View view) {
        SettingsActivity.setPreviousActivityClass(this.getClass()); // required so back button comes back here
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();

        syncWakeUpTimeAndTimePicker();
        refreshTimePicker();
    }

    @Override
    public void onBackPressed() {
        syncWakeUpTimeAndTimePicker(); // user could change time picker before pressing back button

        // if it was the given activity class that the back button is going to, make sure the given time is updated
        // i.e. recreate the GivenTimeActivity
        if (previousActivityClass != null && previousActivityClass.equals(SleepAtGivenTimeActivity.class)) {
            Intent intent = new Intent(this, SleepAtGivenTimeActivity.class);
            startActivity(intent);
        } else {
            // else go back as normal
            super.onBackPressed();
        }
    }
}
