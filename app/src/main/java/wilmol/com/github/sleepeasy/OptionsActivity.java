package wilmol.com.github.sleepeasy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Options the user can set;
 * For example the time to fall asleep (more coming soon?)
 *
 * @author will 2016-09-06
 */
public class OptionsActivity extends AppCompatActivity {

    private static Class previousActivityClass;

    /**
     * REQUIRED:
     * Set the previous Activity so that the back button will go back to the correct Activity
     * after saving changes.
     */
    public static void setPreviousActivityClass(Class activityClass){
        previousActivityClass = activityClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    private void setTimeToFallAsleep(Context context) throws IllegalArgumentException {
        int mins = 15; // default (necessary if user enters invalid int)

        EditText editText = (EditText) findViewById(R.id.time_to_sleep);
        String text = editText.getText().toString();

        try {
            mins = Integer.parseInt(text);
            if (mins > 60) {
                throw new IllegalArgumentException("Minutes exceed sixty.");
            } else if (mins < 0) {
                throw new IllegalArgumentException("Negative minutes.");
            }
        } catch (NumberFormatException e){ // int out of range from parseInt(String)
            throw new IllegalArgumentException("Invalid value!");
        }
        SleepNowActivity.setMinsToFallAsleep(mins);
    }

    private void createAndShowInvalidValueDialogue(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Save changes and go to previous Activity.
     */
    @Override
    public void onBackPressed() {
        View view = findViewById(R.id.options_view);
        Context context = view.getContext();

        // save changes
        try {
            setTimeToFallAsleep(context);
        } catch (IllegalArgumentException e){
            createAndShowInvalidValueDialogue(context, e.getMessage());
            return; // DONT go back maybe set the editText to default (15?)
        }

        // Go back to previous page
        try {
            Intent intent = new Intent(this, previousActivityClass);
            startActivity(intent);
        } catch (NullPointerException e){
            System.err.println("Previous activity not set");
        }
    }
}
