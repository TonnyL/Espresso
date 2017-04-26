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

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import io.github.marktony.espresso.BuildConfig;
import io.github.marktony.espresso.R;
import io.github.marktony.espresso.customtabs.CustomTabsHelper;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by lizhaotailang on 2017/3/15.
 */

public class AboutFragment extends PreferenceFragmentCompat {

    private Preference prefRate, prefLicenses, prefThx1, prefThx2,
                prefSourceCode, prefSendAdvices, prefDonate;

    private Preference prefVersion;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_prefs);

        initPrefs();

        prefVersion.setSummary(BuildConfig.VERSION_NAME);

        // Rate
        prefRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex){
                    showError();
                }
                return true;
            }
        });

        // Licenses
        prefLicenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), PrefsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_LICENSES);
                startActivity(intent);
                return true;
            }
        });

        // Thanks 1
        prefThx1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomTabsHelper.openUrl(getContext(), getString(R.string.thanks_1_url));
                return true;
            }
        });

        // Thanks 2
        prefThx2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomTabsHelper.openUrl(getContext(), getString(R.string.thanks_2_url));
                return true;
            }
        });

        // Source code
        prefSourceCode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CustomTabsHelper.openUrl(getContext(), getString(R.string.source_code_desc));
                return true;
            }
        });

        // Send advices
        prefSendAdvices.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse(getString(R.string.mail_account));
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    showError();
                }
                return true;
            }
        });

        // Donate
        prefDonate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle(R.string.donate);
                dialog.setMessage(getString(R.string.donate_msg));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // add the alipay account to clipboard
                        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", getString(R.string.donate_account));
                        manager.setPrimaryClip(clipData);
                        dialog.dismiss();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            }
        });

    }

    /**
     * Init the preferences.
     */
    private void initPrefs() {
        prefVersion = findPreference("version");
        prefRate = findPreference("rate");
        prefLicenses = findPreference("licenses");
        prefThx1 = findPreference("thanks_1");
        prefThx2 = findPreference("thanks_2");
        prefSourceCode = findPreference("source_code");
        prefSendAdvices = findPreference("send_advices");
        prefDonate = findPreference("donate");
    }

    private void showError() {
        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
    }

}
