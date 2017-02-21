package io.github.marktony.espresso.packages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackagesFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    private PackagesBaseFragment allFragment, onTheWayFragment, deliveredFragment;

    public PackagesFragment() {}

    public static PackagesFragment newInstance() {
        return new PackagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            FragmentManager manager = getFragmentManager();
            allFragment = (PackagesBaseFragment) manager.getFragment(savedInstanceState, "AllFragment");
            onTheWayFragment = (PackagesBaseFragment) manager.getFragment(savedInstanceState, "OnTheWayFragment");
            deliveredFragment = (PackagesBaseFragment) manager.getFragment(savedInstanceState, "DeliveredFragment");
        } else {
            allFragment = PackagesBaseFragment.newInstance(PackagesBaseFragment.TYPE_ALL);
            onTheWayFragment = PackagesBaseFragment.newInstance(PackagesBaseFragment.TYPE_ON_THE_WAY);
            deliveredFragment = PackagesBaseFragment.newInstance(PackagesBaseFragment.TYPE_DELIVERED);
        }

        new PackagesPresenter(allFragment);
        new PackagesPresenter(onTheWayFragment);
        new PackagesPresenter(deliveredFragment);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_packages, container, false);

        initViews(view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(fab, "Hello Espresso", Snackbar.LENGTH_SHORT).show();
            }
        });

        switchToFragment(allFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_all:
                        switchToFragment(allFragment);
                        break;

                    case R.id.nav_on_the_way:
                        switchToFragment(onTheWayFragment);
                        break;

                    case R.id.nav_delivered:
                        switchToFragment(deliveredFragment);
                        break;

                }

                return true;
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getFragmentManager();
        manager.putFragment(outState, "AllFragment", allFragment);
        manager.putFragment(outState, "OnTheWayFragment", onTheWayFragment);
        manager.putFragment(outState, "DeliveredFragment", deliveredFragment);
    }

    private void initViews(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigationView);
    }

    private void switchToFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

}
