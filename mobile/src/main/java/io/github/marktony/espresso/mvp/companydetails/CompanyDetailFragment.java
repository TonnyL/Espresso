package io.github.marktony.espresso.mvp.companydetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompanyDetailFragment extends Fragment
        implements CompanyDetailContract.View {

    // View references.
    private FloatingActionButton fab;
    private AppCompatTextView textViewCompanyName;
    private AppCompatTextView textViewTel;
    private AppCompatTextView textViewWebsite;
    private View layoutTel, layoutWebsite;

    private CompanyDetailContract.Presenter presenter;

    public CompanyDetailFragment() {}

    public static CompanyDetailFragment newInstance() {
        return new CompanyDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_details, container, false);

        initViews(view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layoutTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = textViewTel.getText().toString();
                if (!tel.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tel));
                    getActivity().startActivity(intent);
                }
            }
        });

        layoutWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
    public void initViews(View view) {

        CompanyDetailActivity activity = (CompanyDetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        textViewCompanyName = (AppCompatTextView) view.findViewById(R.id.textViewCompany);
        textViewTel = (AppCompatTextView) view.findViewById(R.id.textViewCompanyPhoneNumber);
        textViewWebsite = (AppCompatTextView) view.findViewById(R.id.textViewCompanyWebsite);
        layoutTel = view.findViewById(R.id.layoutCompanyPhoneNumber);
        layoutWebsite = view.findViewById(R.id.layoutCompanyOfficialWebsite);
    }

    @Override
    public void setPresenter(@NonNull CompanyDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setCompanyName(String name) {
        textViewCompanyName.setText(name);
    }

    @Override
    public void setCompanyTel(String tel) {
        textViewTel.setText(tel);
    }

    @Override
    public void setCompanyWebsite(String website) {
        textViewWebsite.setText(website);
    }

    @Override
    public void showErrorMsg() {

    }

}
