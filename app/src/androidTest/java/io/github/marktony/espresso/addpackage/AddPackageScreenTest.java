package io.github.marktony.espresso.addpackage;

import android.os.Build;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.mvp.addpackage.AddPackageActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by lizhaotailang on 2017/5/14.
 * Tests the components of {@link AddPackageActivity} layout.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddPackageScreenTest {

    private String validPackageNumber;
    private String invalidPackageNumber;

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<AddPackageActivity> mAddPackageActivityTestRule
            = new ActivityTestRule<>(AddPackageActivity.class);

    @Before
    public void initNumbers() {
        validPackageNumber = "958381347318";
        invalidPackageNumber = "12345";
    }

    @Before
    public void grantCameraPermission() {
        // In M+, trying to call a number will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.CAMERA");
        }
    }

    @Test
    public void test_AddPackageScreenDisplayed() {
        // Check that the toolbar title was correct.
        onView(withText(R.string.activity_add_package))
                .check(matches(withParent(withId(R.id.toolbar))));
    }

    @Test
    public void clickOnFab_ShowErrorTip() {
        // Click the floating action button without inputting anything.
        onView(withId(R.id.fab)).perform(click());

        // Check that the snack bar was displayed.
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.wrong_number_and_check)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void typeValidNumber_ShowHomeScreen() {
        onView(withId(R.id.editTextNumber))
                .check(matches(isCompletelyDisplayed()));

        // Type the valid number.
        onView(withId(R.id.editTextNumber))
                .perform(typeText(validPackageNumber), closeSoftKeyboard());

        // Click the floating action button.
        onView(withId(R.id.fab)).perform(click());

        // Check that the package name edit text was filled automatically.
        String name = mAddPackageActivityTestRule.getActivity().getString(R.string.package_name_default_pre)
                + validPackageNumber.substring(0, 4);
        onView(withId(R.id.editTextName))
                .check(matches(withText(name)));

    }

    @Test
    public void typeInvalidNumber_ShowErrorTip() {
        onView(withId(R.id.editTextNumber))
                .check(matches(isCompletelyDisplayed()));

        // Type the valid number.
        onView(withId(R.id.editTextNumber))
                .perform(typeText(invalidPackageNumber), closeSoftKeyboard());

        // Click the floating action button.
        onView(withId(R.id.fab)).perform(click());

        // Check that the package name edit text was filled automatically.
        String name = mAddPackageActivityTestRule.getActivity().getString(R.string.package_name_default_pre)
                + invalidPackageNumber.substring(0, 4);
        onView(withId(R.id.editTextName))
                .check(matches(withText(name)));

        // Check that the snack bar with error message was displayed.
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.wrong_number_and_check)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void typeEmptyNumber_ShowErrorTip() {
        onView(withId(R.id.editTextNumber))
                .check(matches(isCompletelyDisplayed()));

        // Type empty.
        onView(withId(R.id.editTextNumber))
                .perform(typeText(""), closeSoftKeyboard());

        // Click the floating action button.
        onView(withId(R.id.fab)).perform(click());

        // Check that the progress bar was gone.
        onView(withId(R.id.progressBar))
                .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Check that the package name edit text was filled automatically.
        onView(withId(R.id.editTextName))
                .check(matches(withText("")));

        // Check that the snack bar with error message was displayed.
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.wrong_number_and_check)))
                .check(matches(isDisplayed()));
    }

}
