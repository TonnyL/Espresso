package io.github.marktony.espresso.onboarding;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import io.github.marktony.espresso.ui.onboarding.OnboardingActivity;

/**
 * Created by lizhaotailang on 2017/5/13.
 * Tests for the {@link android.support.v4.view.ViewPager} and
 * other layout components in {@link OnboardingActivity}.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class OnboardingScreenTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    ActivityTestRule<OnboardingActivity> mOnboardingActivityTestRule
            = new ActivityTestRule<>(OnboardingActivity.class);

}
