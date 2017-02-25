package io.github.marktony.espresso.packagedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.PackagesRepository;

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

        if (savedInstanceState != null) {
            fragment = (PackageDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "PackageDetailsFragment");
        } else {
            fragment = PackageDetailsFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        new PackageDetailsPresenter(getIntent().getStringExtra(PACKAGE_ID),
                // only for test
                new PackagesRepository(),
                fragment);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "PackageDetailsFragment", fragment);
        }
    }
}
