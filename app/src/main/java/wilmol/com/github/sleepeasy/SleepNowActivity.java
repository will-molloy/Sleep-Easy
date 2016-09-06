package wilmol.com.github.sleepeasy;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class SleepNowActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Time12HourFormat _currentTime;
    private static final int MINS_TO_FALL_ASLEEP = 15;
    private static final int TIMES_TO_SHOW = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_now);

        getCurrentTimeAndDisplayMessage();
        createAndShowWakeUpTimesSpinner();
    }

    /**
     * Gets the current time in a 12 hour format:
     * hour, minute stored as ints
     * _currentTimeAM_PM stored as a String
     * then displays the initial message.
     */
    private void getCurrentTimeAndDisplayMessage() {
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timeZone);

        boolean isAM = calendar.get(Calendar.AM_PM) == Calendar.AM;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour > 12){
            hour -= 12;       // For phones with 24 hour time. (Calendar class gets 12 hour time.. but my samsung s3 doesnt ?)
        }
        if (hour == 0){
            hour = 12;       // special case if 0:00am/0:00pm, I want to show 12:00am/12:00pm respectively.
            isAM = !isAM;    // isAM is inverted because.. Time12HourFormat.toString() inverts isAM if the hour is 12
            // this is because the java.util.Calendar class is using hours [0..11] (and i'm using [1..12])
        }

       _currentTime = new Time12HourFormat(hour, minute, isAM);
        displayMessage();
    }

    private void displayMessage() {
        String message = "It is currently " + _currentTime + ".\n";
        TextView textView = (TextView) findViewById(R.id.current_time);
        textView.setText(message);
    }

    private void createAndShowWakeUpTimesSpinner() {

        /* put arraylist generation in seperate method similar to test method */
        // Drop down elements -- list of TIMES_TO_SHOW times
        List<String> wakeUpTimes = new ArrayList<String>();

        // add a sleep cycle (90mins) to each time, incrementing the factor each time
        // i.e. adds 90mins, 180mins, 270mins ...
        for (int i = 1; i <= TIMES_TO_SHOW; i++) {
            Time12HourFormat tempTime = _currentTime.copy();
            tempTime = tempTime.addNinteyMinutesXTimes(i);
            tempTime = tempTime.add(new Time12HourFormat(0,MINS_TO_FALL_ASLEEP,true));
            wakeUpTimes.add(tempTime.toString());
        }

        // Get spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wakeUpTimes);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adding the adapter to the spinner
        spinner.setAdapter(dataAdapter);

        // Default spinner selection - selects 8 hour sleep by default
        spinner.setSelection(TIMES_TO_SHOW-2);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    /**
     * Opens the Alarm clock app (if one exists) on an Android phone.
     * If a clock app does not exists, opens the market store after a prompt.
     *
     * Thanks to http://stackoverflow.com/a/4281243 for majority of the code.
     */
    public void setAlarm(View view) {
        Context context = view.getContext();
        PackageManager packageManager = context.getPackageManager();
        Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

// Verify clock implementation
        String clockImpls[][] = {
                {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
                {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
                {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
                {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
                {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"} ,
                {"Sony Ericsson Xperia Z", "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock" },
                {"ASUS Tablets", "com.asus.deskclock", "com.asus.deskclock.DeskClock"}

        };

        boolean foundClockImpl = false;

        for(int i=0; i<clockImpls.length; i++) {
            String vendor = clockImpls[i][0];
            String packageName = clockImpls[i][1];
            String className = clockImpls[i][2];
            try {
                ComponentName cn = new ComponentName(packageName, className);
                ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                alarmClockIntent.setComponent(cn);
                foundClockImpl = true;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }

        if (foundClockImpl) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);

            startActivity(alarmClockIntent);
        } else {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("No alarm found, open store?");
            alertDialogBuilder.setCancelable(true);

            alertDialogBuilder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Bring user to the market to choose an app
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(Uri.parse("market://details?id=" + "com.package.name"));
                            startActivity(intent);
                        }
                    });

            alertDialogBuilder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void goToMainScreen(View view) {
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }

    public void options(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
