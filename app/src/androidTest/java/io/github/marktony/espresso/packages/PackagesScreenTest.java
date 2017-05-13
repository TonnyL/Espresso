package io.github.marktony.espresso.packages;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import io.github.marktony.espresso.mvp.packages.MainActivity;

/**
 * Created by lizhaotailang on 2017/5/12.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PackagesScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);



}
