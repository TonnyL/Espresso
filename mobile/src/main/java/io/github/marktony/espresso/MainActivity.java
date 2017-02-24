package io.github.marktony.espresso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.github.marktony.espresso.companies.CompaniesFragment;
import io.github.marktony.espresso.companies.CompaniesPresenter;
import io.github.marktony.espresso.packages.PackagesFragment;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView navigationView;

    private PackagesFragment packagesFragment;
    private CompaniesFragment companiesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        if (savedInstanceState != null) {
            packagesFragment = (PackagesFragment) getSupportFragmentManager().getFragment(savedInstanceState, "PackagesFragment");
            companiesFragment = (CompaniesFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CompaniesFragment");
        } else {
            packagesFragment = PackagesFragment.newInstance();
            companiesFragment = CompaniesFragment.newInstance();
        }

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

        showPackagesFragment();

        new CompaniesPresenter(companiesFragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            showPackagesFragment();

        } else if (id == R.id.nav_companies) {

            showCompaniesFragment();

        } else if (id == R.id.nav_switch_theme) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (packagesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "PackagesFragment", packagesFragment);
        }
        if (companiesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CompaniesFragment", companiesFragment);
        }
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void showPackagesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(packagesFragment);
        fragmentTransaction.hide(companiesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.app_name));
        navigationView.setCheckedItem(R.id.nav_home);

    }

    private void showCompaniesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(companiesFragment);
        fragmentTransaction.hide(packagesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.nav_companies));
        navigationView.setCheckedItem(R.id.nav_companies);

    }

}
