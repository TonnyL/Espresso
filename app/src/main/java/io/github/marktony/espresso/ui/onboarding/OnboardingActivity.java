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

package io.github.marktony.espresso.ui.onboarding;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Arrays;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.CompaniesRepository;
import io.github.marktony.espresso.data.source.local.CompaniesLocalDataSource;
import io.github.marktony.espresso.mvp.addpackage.AddPackageActivity;
import io.github.marktony.espresso.mvp.packages.MainActivity;
import io.github.marktony.espresso.mvp.search.SearchActivity;
import io.github.marktony.espresso.util.SettingsUtil;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private AppCompatButton buttonFinish;
    private ImageButton buttonPre;
    private ImageButton buttonNext;
    private ImageView[] indicators;

    private int bgColors[];

    private int currentPosition;

    private static final int MSG_DATA_INSERT_FINISH = 1;

    private Handler handler = new Handler(new HandlerCallback());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(SettingsUtil.KEY_FIRST_LAUNCH, true)) {

            setContentView(R.layout.activity_onboarding);

            new InitCompaniesDataTask().execute();

            initViews();

            initData();

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int colorUpdate = (Integer) new ArgbEvaluator().evaluate(positionOffset, bgColors[position], bgColors[position == 2 ? position : position + 1]);
                    viewPager.setBackgroundColor(colorUpdate);
                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                    updateIndicators(position);
                    viewPager.setBackgroundColor(bgColors[position]);
                    buttonPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                    buttonNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                    buttonFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            buttonFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putBoolean(SettingsUtil.KEY_FIRST_LAUNCH, false);
                    ed.apply();
                    navigateToMainActivity();
                    enableShortcuts();
                }
            });

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition += 1;
                    viewPager.setCurrentItem(currentPosition, true);
                }
            });

            buttonPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition -= 1;
                    viewPager.setCurrentItem(currentPosition, true);
                }
            });

        } else {

            navigateToMainActivity();

            finish();
        }

    }

    private void initViews() {
        OnboardingPagerAdapter pagerAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setText(R.string.onboarding_finish_button_description_wait);
        buttonFinish.setEnabled(false);
        buttonNext = findViewById(R.id.imageButtonNext);
        buttonPre = findViewById(R.id.imageButtonPre);
        indicators = new ImageView[] {
                findViewById(R.id.imageViewIndicator0),
                findViewById(R.id.imageViewIndicator1),
                findViewById(R.id.imageViewIndicator2) };
    }

    private void initData() {
        bgColors = new int[]{ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.cyan_500),
                ContextCompat.getColor(this, R.color.light_blue_500)};
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.onboarding_indicator_selected : R.drawable.onboarding_indicator_unselected
            );
        }
    }

    private class HandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_DATA_INSERT_FINISH:

                    buttonFinish.setText(R.string.onboarding_finish_button_description);
                    buttonFinish.setEnabled(true);
                    break;
            }
            return true;
        }
    }

    private class InitCompaniesDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            CompaniesRepository.getInstance(CompaniesLocalDataSource.getInstance())
                    .initData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            handler.sendEmptyMessage(MSG_DATA_INSERT_FINISH);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void enableShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager manager =  getSystemService(ShortcutManager.class);
            ShortcutInfo addPkg = new ShortcutInfo.Builder(this, "shortcut_add_package")
                    .setLongLabel(getString(R.string.shortcut_label_add_package))
                    .setShortLabel(getString(R.string.shortcut_label_add_package))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_add))
                    .setIntent(
                            new Intent(OnboardingActivity.this, AddPackageActivity.class)
                                    .setAction(Intent.ACTION_VIEW)
                                    .addCategory(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
                    )
                    .build();
            ShortcutInfo scanCode = new ShortcutInfo.Builder(this, "shortcut_scan_code")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_filter_center_focus))
                    .setLongLabel(getString(R.string.shortcut_label_scan_code))
                    .setShortLabel(getString(R.string.shortcut_label_scan_code))
                    .setIntent(
                            new Intent(OnboardingActivity.this, AddPackageActivity.class)
                                    .setAction("io.github.marktony.espresso.mvp.addpackage.AddPackageActivity")
                                    .addCategory(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
                    )
                    .build();
            ShortcutInfo search = new ShortcutInfo.Builder(this, "shortcut_search")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_search))
                    .setLongLabel(getString(R.string.shortcut_label_search))
                    .setShortLabel(getString(R.string.shortcut_label_search))
                    .setIntent(
                            new Intent(OnboardingActivity.this, SearchActivity.class)
                                    .setAction(Intent.ACTION_VIEW)
                                    .addCategory(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
                    )
                    .build();
            manager.setDynamicShortcuts(Arrays.asList(addPkg, scanCode, search));
        }

    }

    private void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
