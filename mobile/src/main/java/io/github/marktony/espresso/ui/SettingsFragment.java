package io.github.marktony.espresso.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.util.TimeUtil;

/**
 * Created by lizhaotailang on 2017/3/15.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference prefStartTime, prefsEndTime;

    private SharedPreferences sp;

    private int startHour, startMinute;
    private int endHour, endMinute;

    private static final String SILENT_MODE_START_HOUR = "do_not_disturb_mode_start_hour";
    private static final String SILENT_MODE_START_MINUTE = "do_not_disturb_mode_start_minute";
    private static final String SILENT_MODE_END_HOUR = "do_not_disturb_mode_end_hour";
    private static final String SILENT_MODE_END_MINUTE = "do_not_disturb_mode_end_minute";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_prefs);

        initPrefs();

        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        // Set the do-not-disturb-mode time initial range:
        // from 23:00 to 6:00
        startHour = sp.getInt(SILENT_MODE_START_HOUR, 23);
        startMinute = sp.getInt(SILENT_MODE_START_MINUTE, 0);
        prefStartTime.setSummary(TimeUtil.formatTimeIntToString(startHour, startMinute));

        endHour = sp.getInt(SILENT_MODE_END_HOUR, 6);
        endMinute = sp.getInt(SILENT_MODE_END_MINUTE, 0);
        prefsEndTime.setSummary(TimeUtil.formatTimeIntToString(endHour, endMinute));

        prefStartTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Show the time picker dialog
                TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        // Save the hour and minute value to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(SILENT_MODE_START_HOUR, hourOfDay);
                        editor.putInt(SILENT_MODE_START_MINUTE, minute);
                        editor.apply();
                        // Update ui
                        prefStartTime.setSummary(TimeUtil.formatTimeIntToString(hourOfDay, minute));
                    }
                    // The final params setting true means that it is 24 hours mode.
                }, startHour, startMinute, true);
                dialog.show(getActivity().getFragmentManager(), "StartTimePickerDialog");

                return true;
            }
        });

        prefsEndTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Show the time picker dialog
                TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        if (startHour == endHour && startMinute == endMinute) {
                            Toast.makeText(getContext(), R.string.set_end_time_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Save the hour and minute value to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(SILENT_MODE_END_HOUR, hourOfDay);
                        editor.putInt(SILENT_MODE_END_MINUTE, minute);
                        editor.apply();
                        prefsEndTime.setSummary(TimeUtil.formatTimeIntToString(hourOfDay, minute));
                    }
                    // The final params setting true means that it is 24 hours mode.
                }, endHour, endMinute, true);
                dialog.show(getActivity().getFragmentManager(), "StartTimePickerDialog");

                return true;
            }
        });

    }

    private void initPrefs() {
        prefStartTime = findPreference("do_not_disturb_mode_start");
        prefsEndTime = findPreference("do_not_disturb_mode_end");
    }

}
