package io.github.marktony.espresso.ui;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/3/15.
 */

public class AboutFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_prefs);
    }

}
