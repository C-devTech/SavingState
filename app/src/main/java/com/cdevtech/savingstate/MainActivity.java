package com.cdevtech.savingstate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText notesEditText;
    private static final int SETTINGS_INFO = 1;

    // You can define which key-value pairs are passed to onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        notesEditText = (EditText) findViewById(R.id.notes_edit_text);

        // Make sure there is data to retrieve
        if (savedInstanceState != null) {
            String notes = savedInstanceState.getString("NOTES");

            notesEditText.setText(notes);
        }

        // GetPreferences ==> GetSharedPreferences with the current class name:
        // getSharedPreferences( getLocalClassName(), mode);

        // getDefaultSharedPreferences ==> getSharedPreferences with default preference-file name
        // all activities in the same app context can access it

        // Retrieve the String stored in shared preferences or "EMPTY" if nothing
        // GetPreferences is based on
        // String sPNotes = getPreferences(Context.MODE_PRIVATE).getString("NOTES", "EMPTY");

        //if (!sPNotes.equals("EMPTY")) {
        //    notesEditText.setText(sPNotes);
        // }

        // Uses a default preference-file name. This default is set per application, so all
        // activities in the same app context can access it easily as in the following example:
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String notes = settings.getString("NOTES", "EMPTY");
        if (!notes.equals("EMPTY")) {
            notesEditText.setText(notes);
        }

        // Update the text formatting saved from before
        updateNoteText();

        // Update the Language Settings
        setLanguage();
    }

    // Called before Android kills an application or changes orientation, but doesn't protect you
    // if the user kills the app or restarts the device
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Save the value in the EditText using the key NOTES
        outState.putString("NOTES", notesEditText.getText().toString());

        super.onSaveInstanceState(outState);
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
            Intent intentPreferences = new Intent(getApplicationContext(),
                    SettingsActivity.class);

            // Start the activity and then pass results to onActivityResult
            startActivityForResult(intentPreferences, SETTINGS_INFO);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called after the settings intent closes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Check that the intent that called here had id SETTINGS_INFO
        if (requestCode == SETTINGS_INFO) {
            updateNoteText();

            setLanguage();
        }
    }

    // Called if the app is forced to close
    @Override
    protected void onStop() {
        saveSettings();

        super.onStop();
    }

    // Will save key value pairs to SharedPreferences
    private void saveSettings() {

        // SharedPreferences allow you to save data even if the user kills the app
        // MODE_PRIVATE : Preferences shared only by your app
        // MODE_WORLD_READABLE : All apps can read
        // MODE_WORLD_WRITABLE : All apps can write
        // edit() allows us to enter key vale pairs
        // SharedPreferences.Editor sPEditor = getPreferences(Context.MODE_PRIVATE).edit();

        // Add the key "NOTES" and assign it to the value
        // sPEditor.putString("NOTES", notesEditText.getText().toString());

        // Save the shared preferences
        // sPEditor.commit();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("NOTES", notesEditText.getText().toString());
        editor.commit();
    }

    // Set the language as set by the user
    private void setLanguage() {
        // Shared key value pairs are here
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the value stored in the list preference or give a value of 16
        String langVal = sharedPreferences.getString("pref_language", "EMPTY");

        Configuration config = getBaseContext().getResources().getConfiguration();

        if (!langVal.equals("EMPTY") && !config.locale.getLanguage().equals(langVal)) {
            Locale locale = new Locale(langVal);
            locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(
                    config, getBaseContext().getResources().getDisplayMetrics());

            // Since new language, force the activity to restart
            recreate();
        }
    }

    // Update the text changes in the EditText box
    private void updateNoteText() {
        // Shared key value pairs are here
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Check if the checkbox was clicked
        if (sharedPreferences.getBoolean("pref_text_bold", false)) {

            // Set the text to bold
            notesEditText.setTypeface(null, Typeface.BOLD_ITALIC);
        } else {

            // If not checked set the text to normal
            notesEditText.setTypeface(null, Typeface.NORMAL);
        }

        // Get the value stored in the list preference or give a value of 16
        String textSizeStr = sharedPreferences.getString("pref_text_size", "16");

        // Convert the string returned to a float
        float textSizeFloat = Float.parseFloat(textSizeStr);

        // Set the text size for the EditText box
        notesEditText.setTextSize(textSizeFloat);
    }
}
