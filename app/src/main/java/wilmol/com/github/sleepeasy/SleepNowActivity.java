package wilmol.com.github.sleepeasy;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wilmol.com.github.sleepeasy.tools.AlarmAppOpener;

/**
 * Activity for when the user wants to sleep now.
 * Creates a table of recommended wake up times and gives the user the option to set an alarm.
 *
 * @author will 2016-09-05
 */
public class SleepNowActivity extends AbstractSleepActivity {

    private Time12HourFormat _currentTime;

    private static final int SLEEP_CYCLES_TO_SHOW = 6;
    private static Time12HourFormat TIME_TO_FALL_ASLEEP;

    private boolean _wakeUpTimesOverlapNextDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_now);

        // get the current time, in here so that it is updated each time the Activity is loaded
        _currentTime = Time12HourFormat.getCurrentTime();
        TIME_TO_FALL_ASLEEP = OptionsActivity.getTimeToFallAsleep();

        determineIfWakeUpTimesOverlapTheDay();
        displayInitialMessageAndExplanation();
        createAndShowWakeUpTimes();
    }

    private void determineIfWakeUpTimesOverlapTheDay() {
        Time12HourFormat maxWakeUpTime = _currentTime.addNinteyMinutesXTimes(SLEEP_CYCLES_TO_SHOW);
        double maxWakeUpHour = getHoursFrom12HourTime(maxWakeUpTime);

        double timeToFallAsleepHour = getHoursFrom12HourTime(TIME_TO_FALL_ASLEEP);
        double hour = maxWakeUpHour + timeToFallAsleepHour;

        _wakeUpTimesOverlapNextDay = hour >= 24;
    }

    /**
     * Returns a double of the hours/minutes from a 12 hour time.
     * E.g. (0,15,true) will return 0.25.
     */
    private double getHoursFrom12HourTime(Time12HourFormat time){
        double hour = time.hour();
        hour += time.isAM() ? 0 : 12;
        hour *= 60;
        hour += time.minute();
        hour /= 60;
        return hour;
    }

    private void displayInitialMessageAndExplanation() {
        String message = "It is currently " + _currentTime + ".\n" +
                "You should wake up at one of the following times:";

        TextView initialMessageText = (TextView) findViewById(R.id.current_time);
        initialMessageText.setText(message);

        displayExplanation();
    }

    private void displayExplanation() {
        int hour = TIME_TO_FALL_ASLEEP.hour();
        int minute = TIME_TO_FALL_ASLEEP.minute();

        if (!TIME_TO_FALL_ASLEEP.isAM()) {
            hour += 12; // want to show plain hours and minutes here
        }

        String explanation = "These times ensure you\'ll rise at the end of a 90-minute sleep cycle. \n\n"
                + "A good night\'s sleep consists of 5-6 complete sleep cycles. \n\n"
                + "(This is taking into consideration that it takes " + minute;
        explanation += minute == 1 ? " minute" : " minutes";
        if (hour > 0){
            explanation += " and " + hour;
            explanation += hour == 1 ?  " hour" : " hours";
        }
        explanation += " to fall asleep.)";

        TextView explanationText = (TextView) findViewById(R.id.explanation_text);
        explanationText.setText(explanation);
    }

    private void createAndShowWakeUpTimes() {

        TextView textView = (TextView) findViewById(R.id.wakeup_times);
        String message = "";

        // add a sleep cycle (90mins) to each time, incrementing the factor each iteration
        // i.e. adds 90mins, 180mins, 270mins ...
        for (int i = 1; i <= SLEEP_CYCLES_TO_SHOW; i++) {

            Time12HourFormat tempTime = _currentTime; // cache current time

            tempTime = tempTime.addNinteyMinutesXTimes(i); // calculate new time
            tempTime = tempTime.add(TIME_TO_FALL_ASLEEP);

            String time = tempTime.toString(); // append to text area
            message += time + " or ";
        }
        message = message.substring(0, message.length()-4); // remove last " or "
        if (_wakeUpTimesOverlapNextDay){
            message += (" (on the next day.)");
        }
        textView.setText(message);
    }

    /**
     * Called when the set alarm button is pressed:
     * Opens the Alarm clock app (if one exists) on an Android phone.
     * If a clock app does not exists, opens the market store after a prompt.
     */
    public void setAlarm(View view) {
        Context context = view.getContext();
        AlarmAppOpener alarmAppOpener = new AlarmAppOpener(context, this);
        alarmAppOpener.openAlarmAppIfOneExistsOtherwiseOpenStore();
    }

}
