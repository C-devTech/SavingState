package com.cdevtech.savingstate;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;

/**
 * Created by bills on 4/18/2016.
 */

public class LanguagePreference  extends DialogPreference {
    private final int DEFAULT_VALUE = 0;

    private Integer value;
    private Spinner spinner;

    /*
     * We declare the layout resource file as well as the
     * text for the positive and negative dialog buttons.
     *
     * If required, instead of using `setDialogLayoutResource()`
     * to specify the layout, you can override `onCreateDialogView()`
     * and generate the View to display in the dialog right there.
     */
    public LanguagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.language_dialog);

        setPositiveButtonText(context.getString(android.R.string.ok));
        setNegativeButtonText(context.getString(android.R.string.cancel));
    }

    /*
     * Bind data to our content views
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        spinner = (Spinner) view.findViewById(R.id.spinner1);

        // Set default/current/selected value if set
        if (value != null) {
            spinner.setSelection(value);
        }
    }

    /*
     * Called when the dialog is closed.
     * If the positive button was clicked then persist
     * the data (save in SharedPreferences by calling `persistInt()`)
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            value = spinner.getSelectedItemPosition();

            if (callChangeListener(value)) {
                persistInt(value);
            }
        }
    }

    /*
     * Set initial value of the preference. Called when
     * the preference object is added to the screen.
     *
     * If `restorePersistedValue` is true, the Preference
     * value should be restored from the SharedPreferences
     * else the Preference value should be set to defaultValue
     * passed and it should also be persisted (saved).
     *
     * `restorePersistedValue` will generally be false when
     * you've specified `android:defaultValue` that calls
     * `onGetDefaultValue()` (check below) and that in turn
     * returns a value which is passed as the `defaultValue`
     * to `onSetInitialValue()`.
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            if (defaultValue == null) {
                value = getPersistedInt(DEFAULT_VALUE);
            } else {
                value = getPersistedInt((int) defaultValue);
            }
        } else {
            value = (int) defaultValue;
            persistInt(value);
        }
    }

    /*
     * Called when you set `android:defaultValue`
     *
     * Just in case the value is undefined, you can return
     * DEFAULT_VALUE so that it gets passed to `onSetInitialValue()`
     * that gets saved in SharedPreferences.
     *
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }
}

