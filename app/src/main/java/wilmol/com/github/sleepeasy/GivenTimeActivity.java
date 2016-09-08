package wilmol.com.github.sleepeasy;

import android.os.Bundle;
import android.widget.TextView;

public class GivenTimeActivity extends AbstractSleepActivity {

   private static Time12HourFormat _givenTime;

    public static void setTime(Time12HourFormat time){
        _givenTime = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_time);

        showWakeUpTimeMessage();
        createAndShowBedTimes();
    }

    private void showWakeUpTimeMessage() {
        String message = "If you want to wake up at " + _givenTime + ".\n" +
                "You should go to bed at one of the following times:";
        TextView textView = (TextView) findViewById(R.id.wake_up_time_message);
        textView.setText(message);
    }

    private void createAndShowBedTimes() {
    }




}
