package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
