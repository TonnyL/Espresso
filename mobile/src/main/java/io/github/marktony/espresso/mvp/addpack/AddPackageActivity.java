package io.github.marktony.espresso.mvp.addpack;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.data.source.remote.PackagesRemoteDataSource;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackageActivity extends AppCompatActivity {

    private AddPackageFragment fragment;

    public static final int REQUEST_ADD_PACKAGE = 1;

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
            fragment = (AddPackageFragment) getSupportFragmentManager().getFragment(savedInstanceState, "AddPackageFragment");
        } else {
            fragment = AddPackageFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, "AddPackageFragment")
                    .commit();
        }

        // Create the presenter.
        new AddPackagePresenter(PackagesRepository.getInstance(
                PackagesRemoteDataSource.getInstance(),
                PackagesLocalDataSource.getInstance()),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "AddPackageFragment", fragment);
    }

}
