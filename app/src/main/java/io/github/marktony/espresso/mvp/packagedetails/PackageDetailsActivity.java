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

package io.github.marktony.espresso.mvp.packagedetails;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.data.source.remote.PackagesRemoteDataSource;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackageDetailsActivity extends AppCompatActivity{

    private PackageDetailsFragment fragment;

    public static final String PACKAGE_ID = "PACKAGE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        // Restore the status.
        if (savedInstanceState != null) {
            fragment = (PackageDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "PackageDetailsFragment");
        } else {
            fragment = PackageDetailsFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        // Create the presenter.
        new PackageDetailsPresenter(
                getIntent().getStringExtra(PACKAGE_ID),
                PackagesRepository.getInstance(
                        PackagesRemoteDataSource.getInstance(),
                        PackagesLocalDataSource.getInstance()),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "PackageDetailsFragment", fragment);
    }

}
