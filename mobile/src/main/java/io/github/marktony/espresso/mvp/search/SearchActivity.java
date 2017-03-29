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

package io.github.marktony.espresso.mvp.search;

import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.CompaniesRepository;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.data.source.local.CompaniesLocalDataSource;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.remote.PackagesRemoteDataSource;

public class SearchActivity extends AppCompatActivity {

    private SearchFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        // Set the animations.
        Explode explode = new Explode();
        explode.setDuration(500);
        explode.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(explode);

        if (savedInstanceState != null) {
            fragment = (SearchFragment) getSupportFragmentManager().getFragment(savedInstanceState, "SearchFragment");
        } else {
            fragment = SearchFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        new SearchPresenter(fragment,
                PackagesRepository.getInstance(PackagesRemoteDataSource.getInstance(), PackagesLocalDataSource.getInstance()),
                CompaniesRepository.getInstance(CompaniesLocalDataSource.getInstance()));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "SearchFragment", fragment);
    }
}
