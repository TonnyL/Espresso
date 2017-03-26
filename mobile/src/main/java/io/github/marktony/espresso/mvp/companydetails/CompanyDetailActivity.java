package io.github.marktony.espresso.mvp.companydetails;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.CompaniesRepository;
import io.github.marktony.espresso.data.source.local.CompaniesLocalDataSource;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompanyDetailActivity extends AppCompatActivity {

    private CompanyDetailFragment fragment;

    public static final String COMPANY_ID = "COMPANY_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Explode explode = new Explode();
        explode.setDuration(500);
        explode.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(explode);

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

        new CompanyDetailPresenter(
                fragment,
                CompaniesRepository.getInstance(CompaniesLocalDataSource.getInstance()),
                getIntent().getStringExtra(COMPANY_ID));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CompanyDetailFragment", fragment);
        }
    }
}
