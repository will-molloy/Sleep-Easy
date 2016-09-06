package wilmol.com.github.sleepeasy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    /**
     * Called when the Sleep Now? button is pressed.
     */
    public void sleepNow(View view){
        Intent intent = new Intent(this, SleepNowActivity.class);
        startActivity(intent);
    }
}
