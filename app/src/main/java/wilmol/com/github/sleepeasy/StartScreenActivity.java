package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        computeDefaultWakeUpTimeAndSetTimePicker();
    }

    /**
     *  By default: add 5 sleep cycles (90mins*5) and time to sleep.
     */
    private void computeDefaultWakeUpTimeAndSetTimePicker() {
        Time12HourFormat currentTime = Time12HourFormat.getCurrentTime();
        Time12HourFormat defaultWakeUpTime = currentTime.addNinteyMinutesXTimes(5);
        Time12HourFormat timeToFallAsleep = OptionsActivity.getTimeToFallAsleep();
        defaultWakeUpTime.add(timeToFallAsleep);

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setHour(defaultWakeUpTime.hour());
        timePicker.setMinute(defaultWakeUpTime.minute());
        timePicker.setIs24HourView(false);
    }

    /**
     * Goes to the suggested sleep times activity.
     */
    public void calculateGivenTime(View view){
        // Get time from time picker
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        TimePickerTool timePickerTool = new TimePickerTool(timePicker);
        Time12HourFormat timePickersTime = timePickerTool.get12HourTime();

        // set and start given time activity
        GivenTimeActivity.setTime(timePickersTime);
        Intent intent = new Intent(this, GivenTimeActivity.class);
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
     * Called when the options button is pressed.
     */
    public void options(View view) {
        OptionsActivity.setPreviousActivityClass(this.getClass()); // required so back button comes back here
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
