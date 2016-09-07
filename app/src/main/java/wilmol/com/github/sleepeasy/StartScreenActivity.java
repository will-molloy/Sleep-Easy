package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        // Get current time -- put thisin Time12HourFormat class, also used in Sleep Now!
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

        Time12HourFormat _currentTime = new Time12HourFormat(hour, minute, isAM);
        Time12HourFormat defaultWakeUpTime = _currentTime.add(new Time12HourFormat(8,0,true)); // add 8 hours by default

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
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        boolean isAM = hour <= 12;

        // set and start given time activity
        Time12HourFormat time12HourFormat = new Time12HourFormat(hour, minute, isAM);
        GivenTimeActivity.setTime(time12HourFormat);

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
