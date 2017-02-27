package io.github.marktony.espresso.companydetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.packagedetails.PackageDetailsActivity;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class CompanyDetailFragment extends Fragment
        implements CompanyDetailContract.View {

    private LinearLayout layoutPhoneNumber, layoutWebsite;

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

        layoutPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    public void initViews(View view) {

        PackageDetailsActivity activity = (PackageDetailsActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutWebsite = (LinearLayout) view.findViewById(R.id.layoutCompanyOfficialWebsite);
        layoutPhoneNumber = (LinearLayout) view.findViewById(R.id.layoutCompanyPhoneNumber);

    }

    @Override
    public void setPresenter(@NonNull CompanyDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
