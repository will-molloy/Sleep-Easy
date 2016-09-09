package wilmol.com.github.sleepeasy;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import wilmol.com.github.sleepeasy.tools.Time12HourFormat;

/**
 * Activity for when the user wants to sleep at a certain wake up time.
 * Creates a list of recommended bed times and allows the user to set an alarm.
 *
 * @author will 2016-09-08
 */
public class SleepAtGivenTimeActivity extends AbstractSleepActivity {

    private static final int MAX_SLEEP_CYCLE_SHOWN = 6;
    private static final int SLEEP_CYCLES_TO_SHOW = 6;
    private static Time12HourFormat _givenTime;

    public static void setTime(Time12HourFormat time) {
        _givenTime = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_time);

        // update fields so time to fall asleep is up to date
        super.updateFieldState();

        showWakeUpTimeAndExplanationMessage();
        super.createAndShowTimes();

        StartScreenActivity.setPreviousActivityClass(this.getClass());
    }

    private void showWakeUpTimeAndExplanationMessage() {
        String message = "In order to wake up at " + _givenTime + ".";
        TextView textView = (TextView) findViewById(R.id.wake_up_time_message);
        textView.setText(message);

        super.displayExplanationMessage();
    }

    @Override
    protected TextView getTextViewForExplanationText() {
        return (TextView) findViewById(R.id.explanation_text_given_time);
    }

    @Override
    protected TextView getTextViewToDisplayTimes() {
        return (TextView) findViewById(R.id.bed_times);
    }

    @Override
    protected ArrayList<String> computeAndTimesAndAddToArrayList() {
        ArrayList<String> timesToShow = new ArrayList<String>();

        for (int i = MAX_SLEEP_CYCLE_SHOWN; i > MAX_SLEEP_CYCLE_SHOWN - SLEEP_CYCLES_TO_SHOW; i--) {

            Time12HourFormat tempTime = _givenTime; // cache current time

            tempTime = tempTime.subtractNinteyMinutesXTimes(i); // calculate new time by subtracting 90mins * x
            tempTime = tempTime.subtract(TIME_TO_FALL_ASLEEP);  // subtract time to sleep

            timesToShow.add(tempTime.toString());
        }
        return timesToShow;
    }

    /**
     * Returns true if the time spend in bed is greater than 24 hours.
     */
    @Override
    protected boolean getTimesOverlap() {
    //    double timeToFallAsleepHour = TIME_TO_FALL_ASLEEP.getHoursFrom12HourTime();
    //    double hour = timeToFallAsleepHour + (MAX_SLEEP_CYCLE_SHOWN - SLEEP_CYCLES_TO_SHOW) * 1.5;

    //    return hour >= 24;
        return false; // (above code always returns false, was needed when I only showed 3 times)
    }

    @Override
    protected int initialValueForAppendingTimes() {
        return 0;
    }

    @Override
    protected int nextValueForAppendingTimes(int value) {
        return ++value;
    }

    @Override
    protected boolean orShouldBeAddedBetweenTimes(int value) {
        return value < SLEEP_CYCLES_TO_SHOW;
    }

    @Override
    protected void appendTimesOverlapDayMsg(TextView textView) {
        textView.append(" (on the previous day.)");
    }
}
