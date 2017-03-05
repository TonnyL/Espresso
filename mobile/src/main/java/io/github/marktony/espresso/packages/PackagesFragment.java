package io.github.marktony.espresso.packages;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import io.github.marktony.espresso.addpack.AddPackageActivity;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;
import io.github.marktony.espresso.packagedetails.PackageDetailsActivity;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackagesFragment extends Fragment
        implements PackagesContract.View {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private PackageAdapter adapter;

    private PackagesContract.Presenter presenter;

    private String selectedPackageNumber;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddPackageActivity.class), AddPackageActivity.REQUEST_ADD_PACKAGE);
            }
        });

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
                presenter.loadPackages(false);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.packages_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

        } else if (id == R.id.action_mark_all_read) {

        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item == null || selectedPackageNumber == null) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_set_read_unread:
                presenter.setPackageReadUnread(getSelectedPackageNumber());
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_copy_code:
                ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("text", getSelectedPackageNumber());
                manager.setPrimaryClip(data);
                Snackbar.make(fab, R.string.package_number_copied, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                presenter.setShareData(getSelectedPackageNumber());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initViews(View view) {

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigationView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // returning false means that we need not to handle the drag action
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.deletePackage(viewHolder.getLayoutPosition());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((PackageAdapter.PackageViewHolder) viewHolder).layoutMain);
                ((PackageAdapter.PackageViewHolder) viewHolder).textViewRemove.setVisibility(View.GONE);
                ((PackageAdapter.PackageViewHolder) viewHolder).imageViewRemove.setVisibility(View.GONE);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected(((PackageAdapter.PackageViewHolder) viewHolder).layoutMain);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, recyclerView, ((PackageAdapter.PackageViewHolder) viewHolder).layoutMain, dX, dY, actionState, isCurrentlyActive);
                if (dX > 0) {
                    ((PackageAdapter.PackageViewHolder) viewHolder).wrapperView.setBackgroundResource(R.color.deep_orange);
                    ((PackageAdapter.PackageViewHolder) viewHolder).imageViewRemove.setVisibility(View.VISIBLE);
                    ((PackageAdapter.PackageViewHolder) viewHolder).textViewRemove.setVisibility(View.GONE);
                }

                if (dX < 0) {
                    ((PackageAdapter.PackageViewHolder) viewHolder).wrapperView.setBackgroundResource(R.color.deep_orange);
                    ((PackageAdapter.PackageViewHolder) viewHolder).imageViewRemove.setVisibility(View.GONE);
                    ((PackageAdapter.PackageViewHolder) viewHolder).textViewRemove.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((PackageAdapter.PackageViewHolder) viewHolder).layoutMain, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void setPresenter(@NonNull PackagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        refreshLayout.setRefreshing(active);
    }

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

    @Override
    public void showPackages(@NonNull final List<Package> list) {
        if (adapter == null) {
            adapter = new PackageAdapter(getContext(), list);
            adapter.setOnRecyclerViewItemOnClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(getContext(), PackageDetailsActivity.class);
                    intent.putExtra(PackageDetailsActivity.PACKAGE_ID, list.get(position).getNumber());
                    startActivity(intent);
                }

            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
        showEmptyView(list.isEmpty());
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
            shareData = shareData
                    + pack.getData().get(0).getContext()
                    + pack.getData().get(0).getFtime();
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
    public void showPackageRemovedMsg(String packageName) {
        String msg = packageName
                + " "
                + getString(R.string.package_removed_msg);
        Snackbar.make(fab, msg, Snackbar.LENGTH_SHORT)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.recoverPackage();
                    }
                })
                .show();
    }

    public void setSelectedPackage(@NonNull String packId) {
        this.selectedPackageNumber = packId;
    }

    public String getSelectedPackageNumber() {
        return selectedPackageNumber;
    }
}
