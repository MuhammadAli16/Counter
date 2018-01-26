package com.example.muhammad.counter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 *   @author Muhammad Ali
**/

public class MainActivity extends AppCompatActivity {

    Counter counter;
    final String STOREDCOUNTER = "saveCounter";
    final int INCREMENTCOUNTER = 1;
    final int DECREMENTCOUNTER = -1;

    TextView celValue = null;

    //int counterValue = 0;
    int vibrateIntensity;
    boolean hapticFeedback;
    boolean volumeCounter;


    public void feedback() {
        if (hapticFeedback) {
            final Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            myVib.vibrate(vibrateIntensity);
        }
    }

    public void editCounter(int val) {
        if (val == 1) {
            counter.adjustCounter(INCREMENTCOUNTER);
        } else if (val == -1) {
            counter.adjustCounter(DECREMENTCOUNTER);
        }

        updateDisplayValue();
        // Vibrate function + save
        feedback();
        saveInt(STOREDCOUNTER, counter.getCounter());
    }

    public void updateDisplayValue(){
        celValue.setText(counter.getCounterStr());
    }


    // Save value
    public void saveInt(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void loadInt() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int counterVal = sharedPreferences.getInt(STOREDCOUNTER, 0);
        counter.setCounter(counterVal);
    }

    public void incrementButtonOnClick(View v) {
        editCounter(INCREMENTCOUNTER);
    }

    public void decrementButtonOnClick(View v) {
        editCounter(DECREMENTCOUNTER);
    }

    // Volume button increment/decrement
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (volumeCounter) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                editCounter(DECREMENTCOUNTER);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                editCounter(INCREMENTCOUNTER);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        celValue = (TextView) findViewById(R.id.textView);

        counter = new Counter();

        // Load counter value and set
        loadInt();
        updateDisplayValue();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void editCounterValue(){
        //AlertDialog.Builder alert = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Value");


        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(9)});

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty() ) {
                    int newCounterVal = Integer.parseInt(input.getText().toString());
                    counter.setCounter(newCounterVal);
                    updateDisplayValue();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.edit_values) {
            editCounterValue();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load counter value and settings
        loadInt();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        hapticFeedback = prefs.getBoolean("hapticFeedback", true);
        volumeCounter = prefs.getBoolean("volumeCounter", true);
        //vibration intensity
        vibrateIntensity = Integer.parseInt(prefs.getString("hapticFeedbackIntensity","0"));

        // on screen buttons
        boolean incrementOnScreen = prefs.getBoolean("enableOnScreenIncrement",true);
        boolean decrementOnScreen = prefs.getBoolean("enableOnScreenDecrement",true);
        final Button button = (Button) findViewById(R.id.increment);
        final Button decrementButton = (Button) findViewById(R.id.decrement);

        if (!incrementOnScreen) button.setVisibility(View.GONE);
        else button.setVisibility(View.VISIBLE);

        if (!decrementOnScreen) decrementButton.setVisibility(View.GONE);
        else decrementButton.setVisibility(View.VISIBLE);



    }

}
