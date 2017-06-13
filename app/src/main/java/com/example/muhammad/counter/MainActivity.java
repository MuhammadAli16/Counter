package com.example.muhammad.counter;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int x = 0;
    int vibrateIntensity = 50;
    boolean hapticFeedback;
    boolean volumeCounter;


    public void feedback(int intensity) {
        if (hapticFeedback == true) {
            final Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            myVib.vibrate(vibrateIntensity);
        }
    }

    public void editCounter(String xy) {
        if (xy.equals("1")) {
            x++;
        } else if (xy.equals("-1")) {
            x--;
        }
        final TextView celValue = (TextView) findViewById(R.id.textView);
        celValue.setText(x + "");
    }

    // Save value
    public void SaveInt(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void LoadInt() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        x = sharedPreferences.getInt("counterValue", 0);
    }

    public void buttonOnClick(View v) {
        // Vibrate function
        feedback(vibrateIntensity);
        editCounter("1");
        SaveInt("counterValue", x);

    }

    public void DecrementButtonOnClick(View v) {
        // Vibrate function
        feedback(vibrateIntensity);
        editCounter("-1");
        SaveInt("counterValue", x);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (volumeCounter == true) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                // Vibrate function
                feedback(vibrateIntensity);
                editCounter("-1");
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                // Vibrate function
                feedback(vibrateIntensity);
                editCounter("1");
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
        // Load counter value and set
        LoadInt();
        final TextView celValue = (TextView) findViewById(R.id.textView);
        celValue.setText(x + "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        hapticFeedback = prefs.getBoolean("hapticFeedback", true);
        volumeCounter = prefs.getBoolean("volumeCounter", true);

        // on screen buttons
        boolean incrementOnScreen = prefs.getBoolean("enableOnScreenIncrement",true);
        boolean decrementOnScreen = prefs.getBoolean("enableOnScreenDecrement",true);
        final Button button = (Button) findViewById(R.id.button);
        final Button decrementButton = (Button) findViewById(R.id.decrement);
        if (incrementOnScreen == false) {
            button.setVisibility(View.GONE);
        }  else if (incrementOnScreen == true) {
            button.setVisibility(View.VISIBLE);
        }

        if (decrementOnScreen == false) {
            decrementButton.setVisibility(View.GONE);
        }  else if (decrementOnScreen == true) {
            decrementButton.setVisibility(View.VISIBLE);
        }

        //vibration intensity
        vibrateIntensity = Integer.parseInt(prefs.getString("hapticFeedbackIntensity","0"));
        System.out.println(vibrateIntensity + " internisituwy");

    }

}
