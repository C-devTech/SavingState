package com.cdevtech.savingstate;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Locale;

/**
 * Created by bills on 3/11/2016.
 */

public class SettingsActivity extends AppCompatActivity {
    String langVal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Language settings is correct when activity is first started. After that it goes
        // back to the default language
        String currentLang = getBaseContext().getResources().getConfiguration().locale.getLanguage();

        if (savedInstanceState != null) {
            langVal = savedInstanceState.getString("LANGUAGE");

            if (!langVal.equals("EMPTY") && !currentLang.equals(langVal)) {
                Locale locale = new Locale(langVal);
                locale.setDefault(locale);
                getBaseContext().getResources().getConfiguration().locale = locale;
                getBaseContext().getResources().updateConfiguration(
                        getBaseContext().getResources().getConfiguration(),
                        getBaseContext().getResources().getDisplayMetrics());
            }
        } else {
            langVal = currentLang;
        }

        setContentView(R.layout.activity_settings);

        // Get a support ActionBar corresponding to this toolbar and enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();
    }

    // Called before Android kills an application or changes orientation, but doesn't protect you
    // if the user kills the app or restarts the device
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("LANGUAGE", langVal);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            // Define the xml file used for preferences
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
