package io.github.marktony.espresso.addpack;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.zxing.CaptureActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackageFragment extends Fragment
        implements AddPackageContract.View {

    public final static int SCANNING_REQUEST_CODE = 1;

    private TextInputEditText editTextNumber, editTextName;
    private AppCompatTextView textViewScanCode;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private NestedScrollView scrollView;

    private AddPackageContract.Presenter presenter;

    public AddPackageFragment() {}

    public static AddPackageFragment newInstance() {
        return new AddPackageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);
                }

                String name = editTextName.getText().toString();
                String number = editTextNumber.getText().toString().replaceAll("\\s*", "");

                // 对运单号长度进行验证
                // check the length of the input number
                if (number.length() < 5) {
                    showNumberError();
                    return;
                }
                // 检查用户输入运单号是否只包含了数字和字母
                // check the number if only contains numbers and characters
                for (char c : number.toCharArray()) {
                    if (!Character.isLetterOrDigit(c)) {
                        showNumberError();
                        return;
                    }
                }

                // 如果用户未输入快递名称，则使用默认名称：快递(Package) + 运单号前4位
                if (name.isEmpty()) {
                    name = getString(R.string.package_name_default_pre) + number.substring(0, 4);
                }

                editTextName.setText(name);
                presenter.savePackage(editTextNumber.getText().toString(), name);
            }
        });

        textViewScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOrToScan();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

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

    @Override
    public void initViews(View view) {

        AddPackageActivity activity = (AddPackageActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextName = (TextInputEditText) view.findViewById(R.id.editTextName);
        editTextNumber = (TextInputEditText) view.findViewById(R.id.editTextNumber);
        textViewScanCode = (AppCompatTextView) view.findViewById(R.id.textViewScanCode);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);

    }

    @Override
    public void setPresenter(@NonNull AddPackageContract.Presenter presenter) {
        this.presenter = presenter;
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanningActivity();
                } else {
                    AlertDialog dialog = new  AlertDialog.Builder(getContext())
                            .create();
                    dialog.setMessage(getString(R.string.require_permission));
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                break;
            default:

        }
    }

    private void checkPermissionOrToScan() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CAMERA}, 1);
        } else {
            startScanningActivity();
        }
    }

    private void startScanningActivity() {
        try {
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNING_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showNumberExistError() {
        Snackbar.make(fab, R.string.package_exist, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showNumberError() {
        Snackbar.make(fab, R.string.wrong_number_and_check, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
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
    public void showSuccess() {
        Snackbar.make(fab, R.string.add_to_list_successfully, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPackagesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

}
