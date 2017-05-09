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

package io.github.marktony.espresso.mvp.packagedetails;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import io.github.marktony.espresso.data.source.PackagesRepository;

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
        View view = inflater.inflate(R.layout.fragment_package_details, container, false);

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
    public void onDestroy() {
        super.onDestroy();
        PackagesRepository.destroyInstance();
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

    /**
     * Init views.
     * @param view The root view of fragment.
     */
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

    /**
     * Bind the presenter to view.
     * @param presenter The presenter. See at {@link PackageDetailsPresenter}
     */
    @Override
    public void setPresenter(@NonNull PackageDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Set the refresh layout as an indicator. Change the indicator's loading status.
     * @param loading Whether is loading.
     */
    @Override
    public void setLoadingIndicator(final boolean loading) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(loading);
            }
        });
    }

    /**
     * When the network is slow or is not connected, show this message.
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
     * Show the package details.
     * @param p The package. See at {@link Package}
     */
    @Override
    public void showPackageDetails(@NonNull Package p) {
        if (adapter == null) {
            adapter = new PackageDetailsAdapter(getContext(), p);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(p.getData());
        }
    }

    /**
     * Set different image to toolbar layout as banner.
     * @param resId The resource id of image.
     */
    @Override
    public void setToolbarBackground(@DrawableRes int resId) {
        toolbarLayout.setBackgroundResource(resId);
    }

    /**
     * Build the share info and call the system intent chooser to share.
     * @param pack The package to share. See more at {@link Package}
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

    /**
     * Copy the package number(id) to clipboard.
     * @param packageId The package number.
     */
    @Override
    public void copyPackageNumber(@NonNull String packageId) {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", packageId);
        manager.setPrimaryClip(data);
        Snackbar.make(fab, R.string.package_number_copied, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Finish the activity.
     */
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

    /**
     * Show the message that the user's input is empty.
     */
    private void showInputIsEmpty() {
        Snackbar.make(fab, R.string.input_empty, Snackbar.LENGTH_SHORT).show();
    }

}
