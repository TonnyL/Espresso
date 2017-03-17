package io.github.marktony.espresso.mvp.companydetails;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompanyDetailActivity extends AppCompatActivity {

    private CompanyDetailFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        if (savedInstanceState != null) {
            fragment = (CompanyDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CompanyDetailFragment");
        } else {
            fragment = CompanyDetailFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, "CompanyDetailFragment")
                    .commit();
        }

        new CompanyDetailPresenter(fragment);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CompanyDetailFragment", fragment);
        }
    }
}
