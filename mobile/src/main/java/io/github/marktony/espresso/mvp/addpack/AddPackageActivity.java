package io.github.marktony.espresso.mvp.addpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.source.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.PackagesRepository;

/**
 * Created by lizhaotailang on 2017/2/10.
 */

public class AddPackageActivity extends AppCompatActivity {

    private AddPackageFragment fragment;

    public static final int REQUEST_ADD_PACKAGE = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

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
                    .replace(R.id.container, fragment, "AddPackageFragment")
                    .commit();
        }

        new AddPackagePresenter(PackagesRepository.getInstance(PackagesLocalDataSource.getInstance()),
                fragment);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "AddPackageFragment", fragment);
    }

}
