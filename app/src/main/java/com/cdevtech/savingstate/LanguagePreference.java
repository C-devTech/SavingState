package com.cdevtech.savingstate;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bills on 4/18/2016.
 */

public class LanguagePreference  extends DialogPreference {
    private final int DEFAULT_VALUE = 0;

    private Context context;
    private Spinner spinner;
    private Integer value;
    private int entriesId = 0;
    private List<String> entryValuesList;

    /*
     *  We declare the layout resource file as well as the
     *  text for the positive and negative dialog buttons.
     *
     *  If required, instead of using `setDialogLayoutResource()`
     *  to specify the layout, you can override `onCreateDialogView()`
     *  and generate the View to display in the dialog right there.
     */
    public LanguagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // setDialogLayoutResource(R.layout.language_dialog);

        this.context = context;

        setPositiveButtonText(context.getString(android.R.string.ok));
        setNegativeButtonText(context.getString(android.R.string.cancel));

        int entryValuesId = 0;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if (attrs.getAttributeName(i).equalsIgnoreCase("entries")) {
                entriesId = Integer.parseInt(attrs.getAttributeValue(i).substring(1));
            }
            if (attrs.getAttributeName(i).equalsIgnoreCase("entryValues")) {
                entryValuesId = Integer.parseInt(attrs.getAttributeValue(i).substring(1));
            }
        }

        if (entryValuesId != 0) {
            String[] entryValues = context.getResources().getStringArray(entryValuesId);

            // Checking an String in String Array by converting Array To ArrayList
            entryValuesList = Arrays.asList(entryValues);
        }
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setPadding(10, 10, 10, 10);

        //spinner = new Spinner(context);
        spinner = new android.support.v7.widget.AppCompatSpinner(context);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        spinner.setPadding(10, 10, 10, 10);

        if (entriesId != 0) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    context.getResources().getStringArray(entriesId));
            spinner.setAdapter(spinnerArrayAdapter);
        }

        layout.addView(spinner);

        return layout;
    }

    /*
     *  Bind data to our content views
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

      // spinner = (Spinner)view.findViewById(R.id.spinner1);

        // Set default/current/selected value if set
        if (value != null) {
            spinner.setSelection(value);
        }
    }

    /*
     *  Called when the dialog is closed.
     *  If the positive button was clicked then persist
     *  the data (save in SharedPreferences by calling `persistInt()`)
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        try {
            if (positiveResult) {
                value = spinner.getSelectedItemPosition();

                String entryValue = entryValuesList.get(value);
                if (callChangeListener(entryValue)) {
                    persistString(entryValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Set initial value of the preference. Called when
     *  the preference object is added to the screen.
     *
     *  If `restorePersistedValue` is true, the Preference
     *  value should be restored from the SharedPreferences
     *  else the Preference value should be set to defaultValue
     *  passed and it should also be persisted (saved).
     *
     *  `restorePersistedValue` will generally be false when
     *  you've specified `android:defaultValue` that calls
     *  `onGetDefaultValue()` (check below) and that in turn
     *  returns a value which is passed as the `defaultValue`
     *  to `onSetInitialValue()`.
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        try {
            if (restorePersistedValue) {
                if (defaultValue == null) {
                    value = entryValuesList.indexOf(
                            getPersistedString(entryValuesList.get(0)));
                } else {
                    value = entryValuesList.indexOf(
                            getPersistedString((String) defaultValue));
                }
            } else {
                value = entryValuesList.indexOf((String) defaultValue);
                persistString((String) defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Called when you set `android:defaultValue`
     *
     *  Just in case the value is undefined, you can return
     *  DEFAULT_VALUE so that it gets passed to `onSetInitialValue()`
     *  that gets saved in SharedPreferences.
     *
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }
}

