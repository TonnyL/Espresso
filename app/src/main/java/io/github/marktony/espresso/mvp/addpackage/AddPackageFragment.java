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

package io.github.marktony.espresso.mvp.addpackage;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.util.Random;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.zxing.CaptureActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackageFragment extends Fragment
        implements AddPackageContract.View {

    public final static int SCANNING_REQUEST_CODE = 1;
    public final static int REQUEST_CAMERA_PERMISSION_CODE = 0;

    public static final String ACTION_SCAN_CODE = "io.github.marktony.espresso.mvp.addpackage.AddPackageActivity";

    // View references.
    private TextInputEditText editTextNumber, editTextName;
    private AppCompatTextView textViewScanCode;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private NestedScrollView scrollView;

    private AddPackageContract.Presenter presenter;

    private int[] colorRes;

    public AddPackageFragment() {}

    public static AddPackageFragment newInstance() {
        return new AddPackageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorRes = new int[]{
                R.color.cyan_500, R.color.amber_500,
                R.color.pink_500, R.color.orange_500,
                R.color.light_blue_500, R.color.lime_500,
                R.color.green_500};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_package, container, false);

        initViews(view);

        addLayoutListener(scrollView, editTextName);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideImm();

                String name = editTextName.getText().toString();
                String number = editTextNumber.getText().toString().replaceAll("\\s*", "");

                // Check the length of the input number
                if (number.length() < 5 || number.replace(" ", "").isEmpty()) {
                    showNumberError();
                    return;
                }

                // Check the number if only contains numbers and characters.
                for (char c : number.toCharArray()) {
                    if (!Character.isLetterOrDigit(c)) {
                        showNumberError();
                        return;
                    }
                }

                // If the user has not input anything, just use the default name:
                // (Package(In default language environment) / 快递(In Chinese environment))
                // + the beginning 4 chars of the package number
                if (name.isEmpty()) {
                    name = getString(R.string.package_name_default_pre) + number.substring(0, 4);
                }

                editTextName.setText(name);
                // Set a random color as avatar background
                presenter.savePackage(editTextNumber.getText().toString(), name, colorRes[new Random().nextInt(colorRes.length)]);
            }
        });

        textViewScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOrToScan();
            }
        });

        String action = getActivity().getIntent().getAction();
        if (action != null && action.equals(ACTION_SCAN_CODE)) {
            checkPermissionOrToScan();
        }

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

    /**
     * Scroll the screen to avoid edit text being covered by imm such as the soft keyboard.
     * It is better to set the height as 150 because some devices
     * has the navigation bar. The height 100 might not trigger the scrolling action.
     * @param main The scroll view.
     * @param scroll The view to show.
     */
    private void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 150) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, scrollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return true;
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {

        AddPackageActivity activity = (AddPackageActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextName = view.findViewById(R.id.editTextName);
        editTextNumber = view.findViewById(R.id.editTextNumber);
        textViewScanCode = view.findViewById(R.id.textViewScanCode);
        fab = view.findViewById(R.id.fab);
        progressBar = view.findViewById(R.id.progressBar);
        scrollView = view.findViewById(R.id.scrollView);

    }

    /**
     * Bind presenter to fragment(view).
     * @param presenter The presenter. See at {@link AddPackagePresenter}.
     */
    @Override
    public void setPresenter(@NonNull AddPackageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Handle the scanning result.
     * @param requestCode The request code. See at {@link AddPackageFragment#SCANNING_REQUEST_CODE}.
     * @param resultCode The result code.
     * @param data The result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        editTextNumber.setText(bundle.getString("result"));
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * To handle the permission grant result.
     * If the user denied the permission, show a dialog to explain
     * the reason why the app need such permission and lead he/her
     * to the system settings to grant permission.
     * @param requestCode The request code. See at {@link AddPackageFragment#REQUEST_CAMERA_PERMISSION_CODE}
     * @param permissions The wanted permissions.
     * @param grantResults The results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanningActivity();
                } else {
                    hideImm();
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.permission_denied)
                            .setMessage(R.string.require_permission)
                            .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Go to the detail settings of our application
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                break;
            default:

        }
    }

    /**
     * Check whether the camera permission has been granted.
     * If not, request it. Or just launch the camera to scan barcode or QR code.
     */
    private void checkPermissionOrToScan() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Notice: Do not use the below code.
            // ActivityCompat.requestPermissions(getActivity(),
            // new String[] {Manifest.permission.CAMERA}, 1);
            // Such code may still active the request permission dialog
            // but even the user has granted the permission,
            // app will response nothing.
            // The below code works perfect.
            requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            startScanningActivity();
        }
    }

    /**
     * Launch the camera
     */
    private void startScanningActivity() {
        try {
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNING_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show massage that the package is existed.
     */
    @Override
    public void showNumberExistError() {
        Snackbar.make(fab, R.string.package_exist, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Show massage that number is invalid.
     */
    @Override
    public void showNumberError() {
        Snackbar.make(fab, R.string.wrong_number_and_check, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Set the refresh layout as an indicator whether is refreshing or not.
     * @param loading Whether is loading.
     */
    @Override
    public void setProgressIndicator(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Finish current activity.
     */
    @Override
    public void showPackagesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    /**
     * Show message that the network is in error.
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
     * Hide the input method like soft keyboard, etc... when they are active.
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);
        }
    }

}
