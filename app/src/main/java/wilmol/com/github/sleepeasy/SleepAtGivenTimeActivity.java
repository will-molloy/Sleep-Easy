package wilmol.com.github.sleepeasy;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for when the user wants to sleep at a certain wake up time.
 * Creates a list of recommended bed times and allows the user to set an alarm.
 *
 * @author will 2016-09-08
 */
public class SleepAtGivenTimeActivity extends AbstractSleepActivity {

    private static final int MAX_SLEEP_CYCLE_SHOWN = 6;
    private static final int SLEEP_CYCLES_TO_SHOW = 4;
    private static Time12HourFormat _givenTime;
    private boolean _bedTimesOverlapNextDay;

    public static void setTime(Time12HourFormat time) {
        _givenTime = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_time);

        // update fields so the displayed time is up to date
        super.updateFieldState();

        _bedTimesOverlapNextDay = determineIfBedTimesOverlapTheDay();

        showWakeUpTimeAndExplanationMessage();
        createAndShowBedTimes();

        StartScreenActivity.setPreviousActivityClass(this.getClass());
    }

    /**
     * Returns true if the time spent from going to bed and
     * waking up is greater than 24 hours.
     */
    private boolean determineIfBedTimesOverlapTheDay() {

        double timeToFallAsleepHour = TIME_TO_FALL_ASLEEP.getHoursFrom12HourTime();
        double hour = timeToFallAsleepHour + (MAX_SLEEP_CYCLE_SHOWN - SLEEP_CYCLES_TO_SHOW) * 1.5;

        return hour >= 24;
    }

    private void showWakeUpTimeAndExplanationMessage() {
        String message = "In order to wake up at " + _givenTime + ".\n\n" +
                "You should go to bed at one of the following times:";
        TextView textView = (TextView) findViewById(R.id.wake_up_time_message);
        textView.setText(message);

        displayExplanationMessage();
    }

    private void createAndShowBedTimes() {
        TextView textView = (TextView) findViewById(R.id.bed_times);
        String message = "";
        List<String> timesToShow = new ArrayList<String>();

        for (int i = MAX_SLEEP_CYCLE_SHOWN; i > MAX_SLEEP_CYCLE_SHOWN - SLEEP_CYCLES_TO_SHOW; i--) {

            Time12HourFormat tempTime = _givenTime; // cache current time

            tempTime = tempTime.subtractNinteyMinutesXTimes(i); // calculate new time by subtracting 90mins * x
            tempTime = tempTime.subtract(TIME_TO_FALL_ASLEEP);  // subtract time to sleep

            message += tempTime.toString() + " or ";
        }

        message = message.substring(0, message.length() - 4); // remove last " or "

        if (_bedTimesOverlapNextDay) {
            message += " (on the previous day.)";
        }

        textView.setText(message);
    }

    TextView getTextViewForExplanationText() {
        return (TextView) findViewById(R.id.explanation_text_given_time);
    }
}
