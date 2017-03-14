package io.github.marktony.espresso.mvp.packagedetails;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.PackageStatus;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class PackageDetailsFragment extends Fragment
        implements PackageDetailsContract.View {

    private RecyclerView recyclerView;

    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CollapsingToolbarLayout toolbarLayout;

    private PackageDetailsAdapter adapter;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNameDialog();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshPackage();
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
        inflater.inflate(R.menu.package_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            exit();

        } else if (id == R.id.action_delete) {

            showDeleteAlertDialog();

        } else if (id == R.id.action_set_readable) {

            presenter.setPackageUnread();

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
    public void showPackageStatus(@NonNull Package p) {
        if (adapter == null) {
            adapter = new PackageDetailsAdapter(getContext(), p);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(p.getData());
        }
    }

    @Override
    public void setToolbarBackground(@DrawableRes int resId) {
        toolbarLayout.setBackgroundResource(resId);
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
    public void copyPackageNumber(@NonNull String packageId) {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", packageId);
        manager.setPrimaryClip(data);
        Snackbar.make(fab, R.string.package_number_copied, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void exit() {
        getActivity().onBackPressed();
    }

    /**
     * Show a dialog when user select the DELETE option menu item.
     */
    private void showDeleteAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(R.string.delete);
        dialog.setMessage(getString(R.string.delete_package_massage));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.deletePackage();
            }
        });
        dialog.show();
    }

    /**
     * Show the dialog which contains an EditText.
     */
    private void showEditNameDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(getString(R.string.edit_name));

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_package_name, null);
        final AppCompatEditText editText = (AppCompatEditText) view.findViewById(R.id.editTextName);
        editText.setText(presenter.getPackageName());
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = editText.getText().toString();
                if (input.isEmpty()) {
                    showInputIsEmpty();
                } else {
                    presenter.updatePackageName(input);
                }
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showInputIsEmpty() {
        Snackbar.make(fab, R.string.input_empty, Snackbar.LENGTH_SHORT).show();
    }

}
