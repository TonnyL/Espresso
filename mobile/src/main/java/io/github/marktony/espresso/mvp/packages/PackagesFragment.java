package io.github.marktony.espresso.mvp.packages;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.appwidget.AppWidgetProvider;
import io.github.marktony.espresso.mvp.addpackage.AddPackageActivity;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;
import io.github.marktony.espresso.mvp.search.SearchActivity;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackagesFragment extends Fragment
        implements PackagesContract.View {


    // View references
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private SwipeRefreshLayout refreshLayout;

    private View contentView;

    private PackagesAdapter adapter;

    private PackagesContract.Presenter presenter;

    private String selectedPackageNumber;

    public static final int REQUEST_OPEN_DETAILS = 0;

    // As a fragment, default constructor is needed.
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
        contentView = inflater.inflate(R.layout.fragment_packages, container, false);

        initViews(contentView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddPackageActivity.class),
                        AddPackageActivity.REQUEST_ADD_PACKAGE,
                        ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        // The function of BottomNavigationView is just as a filter.
        // We need not to build a fragment for each option.
        // Filter the data in presenter and then show it.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_all:
                        presenter.setFiltering(PackageFilterType.ALL_PACKAGES);
                        break;

                    case R.id.nav_on_the_way:
                        presenter.setFiltering(PackageFilterType.ON_THE_WAY_PACKAGES);
                        break;

                    case R.id.nav_delivered:
                        presenter.setFiltering(PackageFilterType.DELIVERED_PACKAGES);
                        break;

                }
                presenter.loadPackages();

                return true;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshPackages();
            }
        });

        // Set true to inflater the options menu.
        setHasOptionsMenu(true);

        return contentView;
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
        setLoadingIndicator(false);
        getActivity().sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.packages_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(getContext(), SearchActivity.class));
        } else if (id == R.id.action_mark_all_read) {
            presenter.markAllPacksRead();
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item == null || selectedPackageNumber == null) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_set_readable:
                presenter.setPackageReadable(
                        getSelectedPackageNumber(),
                        !item.getTitle().equals(getString(R.string.set_read)));
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_copy_code:
                copyPackageNumber();
                break;
            case R.id.action_share:
                presenter.setShareData(getSelectedPackageNumber());
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Init the views by findViewById.
     * @param view The container view.
     */
    @Override
    public void initViews(View view) {

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigationView);
        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        // ItemTouchHelper helps to handle the drag or swipe action.
        // In our app, we do nothing but return a false value
        // means the item does not support drag action.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // returning false means that we need not to handle the drag action
                return false;
            }

            //
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Make different reactions with different directions here.
                presenter.deletePackage(viewHolder.getLayoutPosition());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // The callback when releasing the view, hide the icons.
                // ViewHolder's ItemView is the default view to be operated.
                // Here we call getDefaultUIUtil's function clearView to pass
                // specified view.
                getDefaultUIUtil().clearView(((PackagesAdapter.PackageViewHolder) viewHolder).layoutMain);
                ((PackagesAdapter.PackageViewHolder) viewHolder).textViewRemove.setVisibility(View.GONE);
                ((PackagesAdapter.PackageViewHolder) viewHolder).imageViewRemove.setVisibility(View.GONE);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                // The callback when ViewHolder's status of drag or swipe action changed.
                if (viewHolder != null) {
                    // ViewHolder's ItemView is the default view to be operated.
                    getDefaultUIUtil().onSelected(((PackagesAdapter.PackageViewHolder) viewHolder).layoutMain);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, recyclerView, ((PackagesAdapter.PackageViewHolder) viewHolder).layoutMain, dX, dY, actionState, isCurrentlyActive);
                if (dX > 0) {
                    // Left swipe
                    ((PackagesAdapter.PackageViewHolder) viewHolder).wrapperView.setBackgroundResource(R.color.deep_orange);
                    ((PackagesAdapter.PackageViewHolder) viewHolder).imageViewRemove.setVisibility(View.VISIBLE);
                    ((PackagesAdapter.PackageViewHolder) viewHolder).textViewRemove.setVisibility(View.GONE);
                }

                if (dX < 0) {
                    // Right swipe
                    ((PackagesAdapter.PackageViewHolder) viewHolder).wrapperView.setBackgroundResource(R.color.deep_orange);
                    ((PackagesAdapter.PackageViewHolder) viewHolder).imageViewRemove.setVisibility(View.GONE);
                    ((PackagesAdapter.PackageViewHolder) viewHolder).textViewRemove.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // Be called by ItemTouchHelper's onDrawOver function.
                // Draw with a canvas object.
                // The pattern will be above the RecyclerView
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((PackagesAdapter.PackageViewHolder) viewHolder).layoutMain, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Set a presenter for this fragment(View),
     * @param presenter The presenter.
     */
    @Override
    public void setPresenter(@NonNull PackagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Set the SwipeRefreshLayout as a indicator.
     * And the SwipeRefreshLayout is refreshing means our app is loading.
     * @param active Loading or not.
     */
    @Override
    public void setLoadingIndicator(final boolean active) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(active);
            }
        });
    }

    /**
     * Hide a RecyclerView when it is empty and show a empty view
     * to tell the uses that there is no data currently.
     * @param toShow Hide or show.
     */
    @Override
    public void showEmptyView(boolean toShow) {
        if (toShow) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Show packages with recycler view.
     * @param list The data.
     */
    @Override
    public void showPackages(@NonNull final List<Package> list) {
        if (adapter == null) {
            adapter = new PackagesAdapter(getContext(), list);
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(getContext(), PackageDetailsActivity.class);
                    intent.putExtra(PackageDetailsActivity.PACKAGE_ID, list.get(position).getNumber());
                    startActivityForResult(intent, REQUEST_OPEN_DETAILS, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }

            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
        showEmptyView(list.isEmpty());
    }

    /**
     * Build up the share info and create a chooser to share the data.
     * @param pack The share info comes from.
     */
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
            shareData = shareData
                    + pack.getData().get(0).getContext()
                    + pack.getData().get(0).getFtime();
        } else {
            shareData = shareData + getString(R.string.get_status_error);
        }
        // DO NOT forget surround with try catch statement.
        // There may be no activity on users' device to handle this intent.
        try {
            Intent intent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareData);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));

        } catch (ActivityNotFoundException e) {
            Snackbar.make(fab, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when the package is removed and
     * give the user a change to undo the remove action.
     * @param packageName To build up the message.
     */
    @Override
    public void showPackageRemovedMsg(String packageName) {
        String msg = packageName
                + " "
                + getString(R.string.package_removed_msg);
        Snackbar.make(fab, msg, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.recoverPackage();
                    }
                })
                .show();
    }

    /**
     * Copy the package number to clipboard.
     */
    @Override
    public void copyPackageNumber() {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", getSelectedPackageNumber());
        manager.setPrimaryClip(data);
        Snackbar.make(fab, R.string.package_number_copied, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * When the network is not connected or slow,
     * can not refresh the data, show this message.
     */
    @Override
    public void showNetworkError() {
        Snackbar.make(fab, R.string.network_error, Snackbar.LENGTH_SHORT)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent().setAction(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    /**
     * Work with the activity which fragment attached to.
     * Store the number which is selected.
     * @param packId The selected package number.
     */
    public void setSelectedPackage(@NonNull String packId) {
        this.selectedPackageNumber = packId;
    }

    /**
     * Work with the activity which fragment attached to.
     * Get the number which is selected.
     * @return The selected package number.
     */
    public String getSelectedPackageNumber() {
        return selectedPackageNumber;
    }

}

