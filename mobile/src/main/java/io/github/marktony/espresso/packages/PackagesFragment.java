package io.github.marktony.espresso.packages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.adapter.PackageStateAdapter;
import io.github.marktony.espresso.entity.PackageState;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackagesFragment extends Fragment
        implements PackagesContract.View {

    private PackagesContract.Presenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private PackageStateAdapter adapter;

    private FloatingActionButton fab;

    public PackagesFragment() {}

    public static PackagesFragment newInstance() {
        return new PackagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_packages, container, false);

        initViews(view);

        presenter.subscribe();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(fab, "Hello Espresso", Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }*/

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void initViews(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setPresenter(@NonNull PackagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        swipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showLoadingPackageStatesError() {
        Snackbar.make(fab, "Loading data error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPackageStates(List<PackageState> list) {
        if (adapter == null) {
            adapter = new PackageStateAdapter(getContext(), list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

}
