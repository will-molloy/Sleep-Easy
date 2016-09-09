package wilmol.com.github.sleepeasy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import wilmol.com.github.sleepeasy.tools.Time12HourFormat;

/**
 * Activity for when the user wants to sleep now.
 * Creates a table of recommended wake up times and gives the user the option to set an alarm.
 *
 * @author will 2016-09-05
 */
public class SleepNowActivity extends AbstractSleepActivity {

    private static final int MAX_SLEEP_CYCLE_SHOWN = 6;
    private static final int SLEEP_CYCLES_TO_SHOW = 4;

    private boolean _wakeUpTimesOverlapNextDay;

    private BroadcastReceiver _broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_now);

        updateFieldsAndRefreshMessages();
    }

    private void updateFieldsAndRefreshMessages() {
        // updates the _currentTime and TIME_TO_FALL_ASLEEP fields
        super.updateFieldState();

        _wakeUpTimesOverlapNextDay = determineIfWakeUpTimesOverlapTheDay();

        // update messages
        displayInitialMessageAndExplanation();
        createAndShowWakeUpTimes();
    }

    /**
     * Returns true if the maximum wake up times exceeds the current time by 24 hours.
     */
    public boolean determineIfWakeUpTimesOverlapTheDay() {
        Time12HourFormat maxWakeUpTimeShown = _currentTime.addNinteyMinutesXTimes(MAX_SLEEP_CYCLE_SHOWN);
        maxWakeUpTimeShown.add(TIME_TO_FALL_ASLEEP);

        double maxWakeUpHour = maxWakeUpTimeShown.getHoursFrom12HourTime();
        double timeToFallAsleepHour = TIME_TO_FALL_ASLEEP.getHoursFrom12HourTime();
        double hour = maxWakeUpHour + timeToFallAsleepHour;

        return hour >= 24;
    }

    private void displayInitialMessageAndExplanation() {
        String message = "It is currently " + _currentTime + ".\n\n" +
                "You should wake up at one of the following times:";

        TextView initialMessageText = (TextView) findViewById(R.id.current_time_message);
        initialMessageText.setText(message);

        displayExplanationMessage();
    }

    TextView getTextViewForExplanationText() {
        return (TextView) findViewById(R.id.explanation_text_sleep_now);
    }

    private void createAndShowWakeUpTimes() {

        TextView textView = (TextView) findViewById(R.id.wakeup_times);
        String message = "";

        // add a sleep cycle (90mins) to each time, incrementing the factor each iteration
        // i.e. adds 90mins, 180mins, 270mins ...
        for (int i = MAX_SLEEP_CYCLE_SHOWN - SLEEP_CYCLES_TO_SHOW + 1; i <= MAX_SLEEP_CYCLE_SHOWN; i++) {

            Time12HourFormat tempTime = _currentTime; // cache current time

            tempTime = tempTime.addNinteyMinutesXTimes(i); // calculate new time
            tempTime = tempTime.add(TIME_TO_FALL_ASLEEP);

            String time = tempTime.toString(); // append to text area
            message += time + " or ";
        }
        message = message.substring(0, message.length() - 4); // remove last " or "
        if (_wakeUpTimesOverlapNextDay) {
            message += (" (on the next day.)");
        }
        textView.setText(message);
    }

    /*
     * Implementation to update screen every minute on the minute
     * thanks to http://stackoverflow.com/a/13059819
     */
    @Override
    public void onStart() {
        super.onStart();
        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0)
                    // Update the time and wake up time message every minute on the minute
                    updateFieldsAndRefreshMessages();
            }
        };
        registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (_broadcastReceiver != null)
            unregisterReceiver(_broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the current time and wake up time message when coming back.
        updateFieldsAndRefreshMessages();
    }

}
