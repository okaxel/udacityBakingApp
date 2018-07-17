package hu.drorszagkriszaxel.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {

        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().countingIdlingResource);

    }

    @After
    public void tearDown() {

        IdlingRegistry.getInstance().unregister(mActivityTestRule.getActivity().countingIdlingResource);

    }

    @Test
    public void mainActivityTestRecipeNames() {

        onView(withId(R.id.recipe_container)).check(new RecipeNamesTest(0,"Nutella Pie"));
        // onView(withId(R.id.recipe_container)).check(new RecipeNamesTest(1,"Brownies"));
        // onView(withId(R.id.recipe_container)).check(new RecipeNamesTest(2,"Yellow Cake"));
        // onView(withId(R.id.recipe_container)).check(new RecipeNamesTest(3,"Cheesecake"));

    }

    @Test
    public void mainActivityTestRecipeCount() {

        onView(withId(R.id.recipe_container)).check(new RecipeCountTest(4));

    }

    private class RecipeNamesTest implements ViewAssertion {

        private int position;
        private String text;

        RecipeNamesTest(int position, String text) {

            this.position = position;
            this.text = text;

        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {

            if (noViewFoundException!= null) throw noViewFoundException;

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();

            assertTrue(adapter.getItemCount() > position);

            ViewGroup viewGroup = (ViewGroup) recyclerView.findViewHolderForAdapterPosition(position).itemView;
            TextView textView = viewGroup.findViewById(R.id.recipe_name);

            assertEquals(text,textView.getText());

        }

    }

    private class RecipeCountTest implements ViewAssertion {

        int count;

        RecipeCountTest(int count) {

            this.count = count;

        }


        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {

            if (noViewFoundException!= null) throw noViewFoundException;

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();

            assertEquals(adapter.getItemCount(), count);

        }

    }

}
