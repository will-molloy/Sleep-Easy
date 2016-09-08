package wilmol.com.github.sleepeasy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import wilmol.com.github.sleepeasy.tools.AlarmAppOpener;

/**
 * Abstract class used to show bed/Wakeup times depending on the concrete implementation.
 *
 * Has the methods for dealing with the [set alarm] and [options] buttons since these are shared
 * between implementations.
 *
 * @author will 2016-09-08
 */
public abstract class AbstractSleepActivity extends AppCompatActivity {

    protected static Time12HourFormat TIME_TO_FALL_ASLEEP;
    protected Time12HourFormat _currentTime;

    protected void updateFieldState() {
        TIME_TO_FALL_ASLEEP = OptionsActivity.getTimeToFallAsleep();
        _currentTime = Time12HourFormat.getCurrentTime();
    }

    /**
     * Returns a double of the hours/minutes from a 12 hour time.
     * E.g. (0,15,true) will return 0.25.
     */
    protected double getHoursFrom12HourTime(Time12HourFormat time) {
        double hour = time.hour();
        hour += time.isAM() ? 0 : 12;
        hour *= 60;
        hour += time.minute();
        hour /= 60;
        return hour;
    }

    abstract void getTextViewAndDisplayExplanation();

    protected void displayExplanationMessage(TextView textView) {
        int hour = TIME_TO_FALL_ASLEEP.hour();
        int minute = TIME_TO_FALL_ASLEEP.minute();

        if (!TIME_TO_FALL_ASLEEP.isAM()) {
            hour += 12; // want to show plain hours and minutes here
        }

        String explanation = "These times ensure you\'ll rise at the end of a 90-minute sleep cycle. \n\n"
                + "A good night\'s sleep consists of 5-6 complete sleep cycles. \n\n"
                + "(This is taking into consideration that it takes " + minute;
        explanation += minute == 1 ? " minute" : " minutes";
        if (hour > 0) {
            explanation += " and " + hour;
            explanation += hour == 1 ? " hour" : " hours";
        }
        explanation += " to fall asleep.)";

        textView.setText(explanation);
    }

    /**
     * Called when the options button is pressed:
     * opens the options Activity so the user can edit a few settings.
     */
    public final void options(View view) {
        // required so back button goes to the correct Activity
        OptionsActivity.setPreviousActivityClass(this.getClass());

        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    /**
     * ALWAYS go back to the main screen if back button is pressed.
     * (don't want user going back and forth between options page)
     */
    @Override
    public final void onBackPressed(){
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the set alarm button is pressed:
     * Opens the Alarm clock app (if one exists) on an Android phone.
     * If a clock app does not exists, opens the market store after a prompt.
     */
    public final void setAlarm(View view) {
        Context context = view.getContext();
        AlarmAppOpener alarmAppOpener = new AlarmAppOpener(context, this);
        alarmAppOpener.openAlarmAppIfOneExistsOtherwiseOpenStore();
    }
}
