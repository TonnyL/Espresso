package io.github.marktony.espresso.addpack;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.zxing.CaptureActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackageFragment extends Fragment
        implements AddPackageContract.View{

    public final static int SCANNING_REQUEST_CODE = 1;

    private TextInputLayout inputLayoutNumber;
    private TextInputLayout inputLayoutName;
    private TextInputEditText editTextNumber;
    private TextInputEditText editTextName;
    private AppCompatTextView textViewErrorAndStatus;

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

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_package, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home || id == R.id.action_cancel) {
            getActivity().onBackPressed();
        } else if (id == R.id.action_scan) {
            checkPermissionOrToScan();
        } else if (id == R.id.action_go) {
            presenter.addNumber(editTextNumber.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViews(View view) {
        AddPackageActivity activity = (AddPackageActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutName = (TextInputLayout) view.findViewById(R.id.inputLayoutName);
        inputLayoutNumber = (TextInputLayout) view.findViewById(R.id.inputLayoutNumber);
        editTextName = (TextInputEditText) view.findViewById(R.id.editTextName);
        editTextNumber = (TextInputEditText) view.findViewById(R.id.editTextNumber);
        textViewErrorAndStatus = (AppCompatTextView) view.findViewById(R.id.textViewErrorAndStatus);

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
                        presenter.addNumber(editTextNumber.getText().toString());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanningActivity();
                } else {
                    AlertDialog dialog = new  AlertDialog.Builder(getContext())
                            .create();
                    dialog.setMessage("需要授予权限才能运行！");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
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
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNING_REQUEST_CODE);
    }

    @Override
    public void showAddingNumberError() {

    }

    @Override
    public void showNumberError() {
        Toast.makeText(getContext(), "单号错误，请检查后重试", Toast.LENGTH_SHORT).show();
    }
}
