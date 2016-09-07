package wilmol.com.github.sleepeasy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Options the user can set;
 * For example the time to fall asleep (more coming soon?)
 *
 * @author will 2016-09-06
 */
public class OptionsActivity extends AppCompatActivity {

    private static Class previousActivityClass;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * REQUIRED:
     * Set the previous Activity so that the back button will go back to the correct Activity
     * after saving changes.
     */
    public static void setPreviousActivityClass(Class activityClass) {
        previousActivityClass = activityClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setTimeToFallAsleep(Context context) throws IllegalArgumentException {
        int mins;

        EditText editText = (EditText) findViewById(R.id.time_to_sleep);
        String text = editText.getText().toString();

        try {
            if (text.equals("")) {
                text = SleepNowActivity.getMinsToFallAsleep() + ""; // go to default if left blank
            }
            mins = Integer.parseInt(text);
            if (mins > 60) {
                throw new IllegalArgumentException("Minutes exceed sixty.");
            } else if (mins < 0) {
                throw new IllegalArgumentException("Negative minutes.");
            }
        } catch (NumberFormatException e) { // int out of range from parseInt(String)
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
        } catch (IllegalArgumentException e) {
            createAndShowInvalidValueDialogue(context, e.getMessage());
            return; // DONT go back maybe set the editText to default (15?)
        }

        // Go back to previous page
        try {
            Intent intent = new Intent(this, previousActivityClass);
            startActivity(intent);
        } catch (NullPointerException e) {
            System.err.println("Previous activity not set");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Options Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://wilmol.com.github.sleepeasy/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Options Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://wilmol.com.github.sleepeasy/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
