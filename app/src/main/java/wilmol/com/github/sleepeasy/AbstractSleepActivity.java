package wilmol.com.github.sleepeasy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import wilmol.com.github.sleepeasy.tools.AlarmAppOpener;
import wilmol.com.github.sleepeasy.tools.Time12HourFormat;

/**
 * Abstract class used to show bed/Wakeup times depending on the concrete implementation.
 *
 * Has the methods for dealing with the [set alarm] and [settings] buttons since these are shared
 * between implementations.
 *
 * @author will 2016-09-08
 */
public abstract class AbstractSleepActivity extends AppCompatActivity {

    protected static Time12HourFormat TIME_TO_FALL_ASLEEP;
    protected Time12HourFormat _currentTime;

    protected void updateFieldState() {
        TIME_TO_FALL_ASLEEP = SettingsActivity.getTimeToFallAsleep();
        _currentTime = Time12HourFormat.getCurrentTime();
    }

    /**
     * Hook method to be implemented by subclasses.
     * Get the text view that the explanation text is to be showed in.
     */
    protected abstract TextView getTextViewForExplanationText();

    /**
     * Method to display the explanation message.
     */
    protected final void displayExplanationMessage() {
        TextView textView = getTextViewForExplanationText();

        int hour = TIME_TO_FALL_ASLEEP.hour();
        int minute = TIME_TO_FALL_ASLEEP.minute();

        if (!TIME_TO_FALL_ASLEEP.isAM()) {
            hour += 12; // want to show plain hours and minutes here
        }

        String explanation = "These times ensure you\'ll rise at the end of a 90-minute sleep cycle. \n\n"
                + "A good night\'s sleep consists of 5-6 complete sleep cycles. \n\n";

        if (minute > 0 || hour > 0) {

            explanation += "(This is taking into consideration that it takes ";
            if (minute > 0){ // showing minute
                explanation += minute;
                explanation += minute == 1 ? " minute" : " minutes";
                if (hour > 0){ // showing hour and minute
                    explanation += " and "  + hour;
                }
            } else if (hour > 0){ // showing only hour
                explanation += hour;
            }
            if (hour > 0){
                explanation += hour == 1 ? " hour" : " hours";
            }
            explanation += " to fall asleep.)";

        } else { // not showing hour or minute
            explanation += "(This is assuming you fall asleep instantly.)";
        }

        textView.setText(explanation);
    }

    /**
     * Create and show the times to be displayed to the user (wake up or bed times)
     */
    protected final void createAndShowTimes() {
        TextView textView = getTextViewToDisplayTimes();
        textView.setText("");
        ArrayList<String> timesToShow = computeAndTimesAndAddToArrayList();
        boolean timesOverLap = getTimesOverlap();

        appendTimesToTextViewWithColour(timesToShow, textView, timesOverLap);
    }

    protected abstract TextView getTextViewToDisplayTimes();

    protected abstract ArrayList<String> computeAndTimesAndAddToArrayList();

    protected abstract boolean getTimesOverlap();

    /**
     * Show the times using colour depending on the implementaion the order of the colour
     * (green, yellow, red) will be different
     */
    protected final void appendTimesToTextViewWithColour(ArrayList<String> timesToShow, TextView textView, boolean timesOverlap) {
        int i = initialValueForAppendingTimes();
        int timesToShowSize = timesToShow.size();

        for (String s : timesToShow) {
            i = nextValueForAppendingTimes(i);
            if (i == 1) {
                textView.append(Html.fromHtml("<font color=#7bea7b>" + s + "</font>")); // green
            } else if (i < timesToShowSize / 2) {
                textView.append(Html.fromHtml("<font color=#90EE90>" + s + "</font>")); // yellow/green
            } else if (i <= timesToShowSize * 3 / 4) {
                textView.append(Html.fromHtml("<font color=#EEEE90>" + s + "</font>")); // yellow
            } else if (i == timesToShowSize) {
                textView.append(Html.fromHtml("<font color=#ea8a7b>" + s + "</font>")); // red
            } else {
                textView.append(Html.fromHtml("<font color=#EE9D90>" + s + "</font>")); // lighter red
            }
            if (orShouldBeAddedBetweenTimes(i)) {
                textView.append(Html.fromHtml("<i><small>" + "&nbsp;or&nbsp;" + "</i></small>"));    // italics
            }
        }

        if (timesOverlap) {
            appendTimesOverlapDayMsg(textView);
        }
    }

    protected abstract int initialValueForAppendingTimes();

    protected abstract int nextValueForAppendingTimes(int value);

    protected abstract boolean orShouldBeAddedBetweenTimes(int value);

    protected abstract void appendTimesOverlapDayMsg(TextView textView);

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


    /**
     * Called when the settings button is pressed:
     * opens the settings Activity so the user can edit a few settings.
     */
    public final void settings(View view) {
        // required so back button goes to the correct Activity
        SettingsActivity.setPreviousActivityClass(this.getClass());

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * ALWAYS go back to the main screen if back button is pressed.
     * (don't want user going back and forth between settings page)
     */
    @Override
    public final void onBackPressed() {
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }
}
