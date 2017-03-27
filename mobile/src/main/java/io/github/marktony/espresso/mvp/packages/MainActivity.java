package io.github.marktony.espresso.mvp.packages;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.appwidget.AppWidgetProvider;
import io.github.marktony.espresso.data.source.CompaniesRepository;
import io.github.marktony.espresso.data.source.local.CompaniesLocalDataSource;
import io.github.marktony.espresso.data.source.remote.PackagesRemoteDataSource;
import io.github.marktony.espresso.mvp.companies.CompaniesFragment;
import io.github.marktony.espresso.mvp.companies.CompaniesPresenter;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.ui.PrefsActivity;
import io.github.marktony.espresso.util.PushUtil;
import io.github.marktony.espresso.util.SettingsUtil;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private PackagesFragment packagesFragment;

    private CompaniesFragment companiesFragment;
    private PackagesPresenter packagesPresenter;

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        // Begin the slide animation.
        Slide slide = new Slide();
        slide.setDuration(500);
        slide.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setExitTransition(slide);

        Explode explode = new Explode();
        explode.setDuration(500);
        explode.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(explode);

        initViews();

        // Init the fragments.
        if (savedInstanceState != null) {
            packagesFragment = (PackagesFragment) getSupportFragmentManager().getFragment(savedInstanceState, "PackagesFragment");
            companiesFragment = (CompaniesFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CompaniesFragment");
        } else {
            packagesFragment = (PackagesFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
            if (packagesFragment == null) {
                packagesFragment = PackagesFragment.newInstance();
            }

            companiesFragment = (CompaniesFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
            if (companiesFragment == null) {
                companiesFragment = CompaniesFragment.newInstance();
            }
        }


        // Add the fragments.
        if (!packagesFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, packagesFragment, "PackagesFragment")
                    .commit();
        }

        if (!companiesFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, companiesFragment, "CompaniesFragment")
                    .commit();
        }

        // Make sure the data in repository is the latest.
        // Also to void the repo only contains a package
        // when user has already gone to detail page
        // by check a notification or widget.
        PackagesRepository.destroyInstance();
        // Init the presenters.
        packagesPresenter = new PackagesPresenter(packagesFragment,
                PackagesRepository.getInstance(
                        PackagesRemoteDataSource.getInstance(),
                        PackagesLocalDataSource.getInstance()));

        new CompaniesPresenter(companiesFragment,
                CompaniesRepository.getInstance(CompaniesLocalDataSource.getInstance()));

        // Get data from Bundle.
        if (savedInstanceState != null) {
            PackageFilterType currentFiltering = (PackageFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            packagesPresenter.setFiltering(currentFiltering);
        }

        // Show the default fragment.
        showPackagesFragment();

        PushUtil.startReminderService(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getApplicationContext()));
    }

    /**
     * Close the drawer when a back click is called.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle different items of the navigation drawer
     * @param item The selected item.
     * @return Selected or not.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            showPackagesFragment();

        } else if (id == R.id.nav_companies) {

            showCompaniesFragment();

        } else if (id == R.id.nav_switch_theme) {

            /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                    == Configuration.UI_MODE_NIGHT_YES) {
                sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, false).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, true).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
            recreate();*/

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
            intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_SETTINGS);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
            intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_ABOUT);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Store the state when the activity may be recycled.
     * @param outState The state data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, packagesPresenter.getFiltering());
        super.onSaveInstanceState(outState);

        // Store the fragments' states.
        if (packagesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "PackagesFragment", packagesFragment);
        }
        if (companiesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CompaniesFragment", companiesFragment);
        }
    }

    /**
     * Init views by calling findViewById.
     */
    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * Show the packages list fragment.
     */
    private void showPackagesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(packagesFragment);
        fragmentTransaction.hide(companiesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.app_name));
        navigationView.setCheckedItem(R.id.nav_home);

    }

    /**
     * Show the companies list fragment.
     */
    private void showCompaniesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(companiesFragment);
        fragmentTransaction.hide(packagesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.nav_companies));
        navigationView.setCheckedItem(R.id.nav_companies);

    }

    /**
     * Pass the selected package number to fragment.
     * @param id The selected package number.
     */
    public void setSelectedPackageId(@NonNull String id) {
        packagesFragment.setSelectedPackage(id);
    }

}
