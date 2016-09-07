package wilmol.com.github.sleepeasy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Activity for when the user wants to sleep now.
 * Creates a table of recommended wake up times and gives the user the option to set an alarm.
 *
 * @author will 2016-09-05
 */
public class SleepNowActivity extends AppCompatActivity {

    private Time12HourFormat _currentTime;
    private static int MINS_TO_FALL_ASLEEP = 15;
    private static final int TIMES_TO_SHOW = 6;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_now);

        getCurrentTimeAndDisplayMessage();
        createAndShowWakeUpTimesSpinner();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Gets the current time in a 12 hour format:
     * hour, minute stored as ints
     * _currentTimeAM_PM stored as a String
     * then displays the initial message.
     */
    private void getCurrentTimeAndDisplayMessage() {
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

        _currentTime = new Time12HourFormat(hour, minute, isAM);
        displayMessage();
    }

    public static void setMinsToFallAsleep(int mins) {
        MINS_TO_FALL_ASLEEP = mins;
    }

    public static int getMinsToFallAsleep() { return MINS_TO_FALL_ASLEEP; }

    private void createAndShowWakeUpTimesSpinner() {

        TextView textView = (TextView) findViewById(R.id.wakeup_times);
        String message = "";

        // add a sleep cycle (90mins) to each time, incrementing the factor each time
        // i.e. adds 90mins, 180mins, 270mins ...
        for (int i = 1; i <= TIMES_TO_SHOW; i++) {

            Time12HourFormat tempTime = _currentTime; // cache current time

            tempTime = tempTime.addNinteyMinutesXTimes(i); // calculate new time
            tempTime = tempTime.add(new Time12HourFormat(0, MINS_TO_FALL_ASLEEP, true));

            String time = tempTime.toString(); // append to text area
            message += time + " or ";
        }
        message = message.substring(0, message.length()-4); // remove last " or "
        textView.setText(message);
    }

    private void displayMessage() {
        String message = "It is currently " + _currentTime + ".\n" +
                "You should wake up at one of the following times:";
        TextView textView = (TextView) findViewById(R.id.current_time);
        textView.setText(message);

        String explanation = "These times ensure you\'ll rise at the end of a 90-minute sleep cycle. \n\n"
                + "A good night\'s sleep consists of 5-6 complete sleep cycles. \n\n"
                + "(This is taking into consideration that it takes " + MINS_TO_FALL_ASLEEP + " minutes to fall asleep.)";
        TextView textview2 = (TextView) findViewById(R.id.explanation_text);
        textview2.setText(explanation);
    }

    /**
     * Opens the Alarm clock app (if one exists) on an Android phone.
     * If a clock app does not exists, opens the market store after a prompt.
     *
     * Thanks to http://stackoverflow.com/a/4281243 for majority of the code.
     */
    public void setAlarm(View view) {
        Context context = view.getContext();
        AlarmAppOpener alarmAppOpener = new AlarmAppOpener(context, this);
        alarmAppOpener.openAlarmAppIfOneExistsOtherwiseOpenStore();
    }

    public void options(View view) {
        OptionsActivity.setPreviousActivityClass(this.getClass()); // required so back button comes back here
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    /**
     * ALWAYS go back to the main screen if back button is pressed.
     * (don't want user going back and forth between options page)
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

}
