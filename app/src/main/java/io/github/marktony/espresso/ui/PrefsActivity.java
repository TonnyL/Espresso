/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/3/15.
 */

public class PrefsActivity extends AppCompatActivity {

    public static final String EXTRA_FLAG= "EXTRA_FLAG";

    public static final int FLAG_SETTINGS = 0, FLAG_ABOUT = 1, FLAG_LICENSES = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        initViews();

        Intent intent = getIntent();
        Fragment fragment;

        if (intent.getIntExtra(EXTRA_FLAG, 0) == FLAG_SETTINGS) {
            setTitle(R.string.nav_settings);
            fragment = new SettingsFragment();
        } else if (intent.getIntExtra(EXTRA_FLAG, 0) == FLAG_ABOUT){
            setTitle(R.string.nav_about);
            fragment = new AboutFragment();
        } else if (intent.getIntExtra(EXTRA_FLAG, 0) == FLAG_LICENSES) {
            setTitle(R.string.licenses);
            fragment = new LicensesFragment();
        } else {
            throw new RuntimeException("Please set flag when launching PrefsActivity.");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.view_pager,fragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
