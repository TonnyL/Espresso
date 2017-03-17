package io.github.marktony.espresso.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/3/17.
 */

public class LicensesFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_licenses, container, false);

        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/license.html");

        return view;
    }

}
