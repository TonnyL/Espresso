/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.mvp.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;
import io.github.marktony.espresso.mvp.companydetails.CompanyDetailActivity;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;

/**
 * Created by lizhaotailang on 2017/3/18.
 */

public class SearchFragment extends Fragment
        implements SearchContract.View {

    private SearchView searchView;
    private RecyclerView recyclerView;

    private SearchResultsAdapter adapter;

    private SearchContract.Presenter presenter;

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initViews(view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.search(newText);
                return true;
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideImm();
            getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        SearchActivity activity = (SearchActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setIconified(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showResult(final List<Package> packages, final List<Company> companies) {
        if (packages == null || companies == null) {
            return;
        }
        if (adapter == null) {
            adapter = new SearchResultsAdapter(getContext(), packages, companies);
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {

                    if (adapter.getItemViewType(position) == SearchResultsAdapter.ItemWrapper.TYPE_PACKAGE) {

                        Intent intent = new Intent(getContext(), PackageDetailsActivity.class);
                        intent.putExtra(PackageDetailsActivity.PACKAGE_ID, packages.get(adapter.getOriginalIndex(position)).getNumber());
                        startActivity(intent);

                    } else if (adapter.getItemViewType(position) == SearchResultsAdapter.ItemWrapper.TYPE_COMPANY) {

                        Intent intent = new Intent(getContext(), CompanyDetailActivity.class);
                        intent.putExtra(CompanyDetailActivity.COMPANY_ID, companies.get(adapter.getOriginalIndex(position)).getId());
                        startActivity(intent);

                    }
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(packages, companies);
        }
    }

    /**
     * Hide the input method like soft keyboard, etc... when they are active.
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }

}
