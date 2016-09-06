package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    public void setAlarm(View view) {
        // get spinner selected item
        // set alarm. -- alarm settings will be in options menu (volume, mp3 etc)
        // seperate class for reuse in other screen -- soon
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
