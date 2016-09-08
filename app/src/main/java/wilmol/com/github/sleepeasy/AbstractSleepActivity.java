package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Abstract class used to show Sleep/Wakeup times depending on the concrete implementation.
 *
 */
public abstract class AbstractSleepActivity extends AppCompatActivity {

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

}
