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

package io.github.marktony.espresso.mvp.companydetails;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.customtabs.CustomTabsHelper;

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

    private CompanyDetailContract.Presenter presenter;

    private String tel;
    private String website;

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
                share();
            }
        });

        textViewTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tel != null && !tel.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + tel));
                    getActivity().startActivity(intent);
                }
            }
        });

        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (website != null) {
                    CustomTabsHelper.openUrl(getContext(), website);
                }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {

        CompanyDetailActivity activity = (CompanyDetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = view.findViewById(R.id.fab);
        textViewCompanyName = view.findViewById(R.id.textViewCompany);
        textViewTel = view.findViewById(R.id.textViewCompanyPhoneNumber);
        textViewWebsite = view.findViewById(R.id.textViewCompanyWebsite);
    }

    @Override
    public void setPresenter(@NonNull CompanyDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setCompanyName(String name) {
        String companyName = getString(R.string.company_name) + "\n" + name;
        Spannable spannable = new SpannableStringBuilder(companyName);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, companyName.length() - name.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.NORMAL), companyName.length() - name.length(), companyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewCompanyName.setText(spannable);
    }

    @Override
    public void setCompanyTel(String tel) {
        this.tel = tel;
        String companyTel = getString(R.string.phone_number) + "\n" + tel;
        Spannable spannable = new SpannableStringBuilder(companyTel);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, companyTel.length() - tel.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new URLSpan(tel), companyTel.length() - tel.length(), companyTel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewTel.setText(spannable);
    }

    @Override
    public void setCompanyWebsite(String website) {
        this.website = website;
        String ws = getString(R.string.official_website) + "\n" + website;
        Spannable spannable = new SpannableStringBuilder(ws);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, ws.length() - website.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new URLSpan(website), ws.length() - website.length(), ws.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewWebsite.setText(spannable);
    }

    @Override
    public void showErrorMsg() {
        Snackbar.make(fab, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
    }

    public void share() {
        String content = textViewCompanyName.getText().toString()
                + "\n"
                + textViewTel.getText().toString()
                + "\n"
                + textViewWebsite.getText().toString();
        try {
            Intent intent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        } catch (ActivityNotFoundException e) {
            Snackbar.make(fab, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
        }
    }

}
