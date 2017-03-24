package io.github.marktony.espresso.mvp.companies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompaniesFragment extends Fragment
        implements CompaniesContract.View {

    private RecyclerView recyclerView;

    private CompaniesContract.Presenter presenter;

    private CompaniesAdapter adapter;

    public CompaniesFragment() {}

    public static CompaniesFragment newInstance() {
        return new CompaniesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_companies_list, container, false);

        initViews(view);


        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.companies_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            presenter.subscribe();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCompaniesList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setPresenter(@NonNull CompaniesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showGetCompaniesError() {
        Snackbar.make(recyclerView, "获取失败", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showCompanies(List<Company> list) {
        if (adapter == null) {
            adapter = new CompaniesAdapter(getContext(), list);
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {

                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

}
