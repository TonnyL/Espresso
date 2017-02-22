package io.github.marktony.espresso.packages;

import android.content.Intent;
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
import io.github.marktony.espresso.addpack.AddPackageActivity;

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
                startActivity(new Intent(getContext(), AddPackageActivity.class));
            }
        });

        switchToFragment(allFragment, "AllFragment");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_all:
                        switchToFragment(allFragment, "AllFragment");
                        break;

                    case R.id.nav_on_the_way:
                        switchToFragment(onTheWayFragment, "OnTheWayFragment");
                        break;

                    case R.id.nav_delivered:
                        switchToFragment(deliveredFragment, "DeliveredFragment");
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
        if (allFragment.isAdded()) {
            manager.putFragment(outState, "AllFragment", allFragment);
        }
        if (onTheWayFragment.isAdded()) {
            manager.putFragment(outState, "OnTheWayFragment", onTheWayFragment);
        }
        if (deliveredFragment.isAdded()) {
            manager.putFragment(outState, "DeliveredFragment", deliveredFragment);
        }
    }
    
    private void initViews(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigationView);
    }

    private void switchToFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(R.id.frameLayout, fragment, tag);
        }
        transaction.show(fragment);
        transaction.commit();
    }

}
