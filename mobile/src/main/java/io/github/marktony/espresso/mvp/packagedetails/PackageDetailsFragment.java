package io.github.marktony.espresso.mvp.packagedetails;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.PackageStatus;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackageDetailsFragment extends Fragment
        implements PackageDetailsContract.View {

    private RecyclerView recyclerView;
    private AppCompatTextView textViewCompany;
    private AppCompatTextView textViewNumber;
    private AppCompatTextView textViewName;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CollapsingToolbarLayout toolbarLayout;

    private PackageStatusAdapter adapter;

    private PackageDetailsContract.Presenter presenter;

    private LocalBroadcastManager manager;
    private LocalReceiver receiver;

    public static final int RESULT_DELETE = 0;
    public static final int RESULT_SET_UNREAD = 1;

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

        // Register the local broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocalReceiver.PACKAGE_DETAILS_RECEIVER_ACTION);
        receiver = new LocalReceiver();
        manager = LocalBroadcastManager.getInstance(getContext());
        manager.registerReceiver(receiver, filter);

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
    public void onDestroyView() {
        super.onDestroyView();
        // DO NOT forget to unregister the broadcast receiver
        manager.unregisterReceiver(receiver);
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

        } else if (id == R.id.action_delete) {

            presenter.deletePackage();

        } else if (id == R.id.action_set_read_unread) {

            presenter.setPackageReadUnread();

        } else if (id == R.id.action_copy_code) {

            presenter.copyPackageNumber();

        } else if (id == R.id.action_share) {

            presenter.shareTo();

        }
        return true;
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
        textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);

    }

    @Override
    public void setPresenter(@NonNull PackageDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void showPackageStatus(@NonNull List<PackageStatus> list) {
        if (adapter == null) {
            adapter = new PackageStatusAdapter(getContext(), list);
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {

                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
    }

    @Override
    public void setCompanyName(@NonNull String companyName) {
        textViewCompany.setText(companyName);
    }

    @Override
    public void setPackageNumber(@NonNull String packageNumber) {
        textViewNumber.setText(packageNumber);
    }

    @Override
    public void setPackageName(@NonNull String name) {
        textViewName.setText(name);
    }

    @Override
    public void setToolbarBackground(@DrawableRes int resId) {
        toolbarLayout.setBackgroundResource(resId);
    }

    @Override
    public void setPackageUnread(@NonNull String packageId, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(PackageDetailsActivity.PACKAGE_ID, packageId);
        bundle.putInt(PackageDetailsActivity.PACKAGE_POSITION, position);
        intent.putExtras(bundle);
        getActivity().setResult(RESULT_SET_UNREAD, intent);
        getActivity().finish();
    }

    @Override
    public void shareTo(@NonNull Package pack) {
        String shareData = pack.getName()
                + "\n( "
                + pack.getNumber()
                + " "
                + pack.getCompanyChineseName()
                + " )\n"
                + getString(R.string.latest_status);
        if (pack.getData() != null && !pack.getData().isEmpty()) {
            for (PackageStatus ps : pack.getData()) {
                shareData = new StringBuilder().append(shareData)
                        .append(ps.getContext())
                        .append(" - ")
                        .append(ps.getFtime())
                        .append("\n").toString();
            }
        } else {
            shareData = shareData + getString(R.string.get_status_error);
        }

        try {
            Intent intent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareData);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));

        } catch (ActivityNotFoundException e) {
            Snackbar.make(fab, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deletePackage(@NonNull String packageId, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(PackageDetailsActivity.PACKAGE_ID, packageId);
        bundle.putInt(PackageDetailsActivity.PACKAGE_POSITION, position);
        intent.putExtras(bundle);
        getActivity().setResult(RESULT_DELETE, intent);
        getActivity().finish();
    }

    @Override
    public void copyPackageNumber(@NonNull String packageId) {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", packageId);
        manager.setPrimaryClip(data);
        Snackbar.make(fab, R.string.package_number_copied, Snackbar.LENGTH_SHORT).show();
    }

    public class LocalReceiver extends BroadcastReceiver {

        public static final String PACKAGE_DETAILS_RECEIVER_ACTION
                = "io.github.marktony.espresso.PACKAGE_DETAILS_RECEIVER_ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            setLoadingIndicator(false);
            if (intent.getBooleanExtra("result", false)) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
