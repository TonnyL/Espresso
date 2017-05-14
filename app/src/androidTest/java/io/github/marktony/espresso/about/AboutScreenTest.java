package io.github.marktony.espresso.about;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.ui.PrefsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by lizhaotailang on 2017/5/13.
 * Tests for the {@link io.github.marktony.espresso.ui.AboutFragment}.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AboutScreenTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<PrefsActivity> mPrefsActivityTestRule
            = new ActivityTestRule<PrefsActivity>(PrefsActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, PrefsActivity.class);
                    intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_ABOUT);
                    return intent;
                }
    };

    @Test
    public void test_AboutScreenDisplayed() {
        onView(allOf(withParent(withId(R.id.toolbar)),
                withText(R.string.nav_about)))
                .check(matches(isDisplayed()));
    }

}
