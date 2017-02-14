package io.github.marktony.espresso.addpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackageActivity extends AppCompatActivity {

    private AddPackageFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        if (savedInstanceState != null) {
            fragment = (AddPackageFragment) getSupportFragmentManager().getFragment(savedInstanceState, "AddPackageFragment");
        } else {
            fragment = AddPackageFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, "AddPackageFragment")
                    .commit();
        }

        new AddPackagePresenter(fragment);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "AddPackageFragment", fragment);
        }
    }

}
