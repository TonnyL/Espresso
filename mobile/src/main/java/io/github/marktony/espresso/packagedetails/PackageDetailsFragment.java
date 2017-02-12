package io.github.marktony.espresso.packagedetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.adapter.PackageStatusAdapter;
import io.github.marktony.espresso.entity.Package;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackageDetailsFragment extends Fragment
        implements PackageDetailsContract.View {

    private RecyclerView recyclerView;
    private AppCompatTextView textViewCompany;
    private AppCompatTextView textViewNumber;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;

    private PackageStatusAdapter adapter;

    private PackageDetailsContract.Presenter presenter;

    public PackageDetailsFragment() {}

    public static PackageDetailsFragment newInstance() {
        return new PackageDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        initViews(view);

        setHasOptionsMenu(true);

        presenter.subscribe();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.package_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViews(View view) {

        PackageDetailsActivity activity = (PackageDetailsActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textViewCompany = (AppCompatTextView) view.findViewById(R.id.textViewCompany);
        textViewNumber = (AppCompatTextView) view.findViewById(R.id.textViewPackageNumber);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

    }

    @Override
    public void setPresenter(@NonNull PackageDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showDetailError() {
        Snackbar.make(fab, "Error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPackageStatus(List<Package.Data> list) {
        if (adapter == null) {
            adapter = new PackageStatusAdapter(getContext(), list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCompanyName(String companyName) {
        textViewCompany.setText(companyName);
    }

    @Override
    public void setPackageNumber(String packageNumber) {
        textViewNumber.setText(packageNumber);
    }

}
