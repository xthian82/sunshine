package snowtech.com.py.sunshine.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Casa on 15/08/2015.
 */
public class LocationEditTextPreference extends EditTextPreference {
    private int mMinLength;
    private static final int DEFAULT_MIN_LOCATION_LENGTH = 3;
    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LocationEditTextPreference,
                0,
                0);

        try {
            mMinLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength, DEFAULT_MIN_LOCATION_LENGTH);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void showDialog(Bundle bundle) {
        super.showDialog(bundle);
        final Dialog d = getDialog();
        final EditText editText = this.getEditText();

        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (d instanceof AlertDialog) {
                            AlertDialog dialog = (AlertDialog)d;
                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                            if (editText.getText().length() < mMinLength) {
                                positiveButton.setEnabled(false);
                            } else {
                                positiveButton.setEnabled(true);
                            }
                        }
                    }
                }
        );
    }
}
