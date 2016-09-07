package wilmol.com.github.sleepeasy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GivenTimeActivity extends Activity {

   private static Time12HourFormat _givenTime;

    public static void setTime(Time12HourFormat time){
        _givenTime = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_time);

        showWakeUpTimeMessage();
    }

    private void showWakeUpTimeMessage() {
        String message = "If you want to wake up at " + _givenTime + ".\n" +
                "You should go to bed at one of the following times:";
        TextView textView = (TextView) findViewById(R.id.wake_up_time_message);
        textView.setText(message);
    }

    /*
     * methods are shared between this class and Sleep Now.. also some fields (current time etc)
     *
     * use inheritance? abstract?
     *
     * put current time method in time12hour format class,
     */

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
