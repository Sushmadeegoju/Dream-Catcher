package edu.vt.cs5254.dreamcatcher

import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DreamListFragmentTest {

    private lateinit var scenario: FragmentScenario<DreamListFragment>

    @Before
    fun setUp() {
        scenario = FragmentScenario.launchInContainer(DreamListFragment::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun firstTenDreamsInitiallyVisible() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("Dream #0")))))
            .check(matches(atPosition(1, hasDescendant(withText("Dream #1")))))
            .check(matches(atPosition(2, hasDescendant(withText("Dream #2")))))
            .check(matches(atPosition(3, hasDescendant(withText("Dream #3")))))
            .check(matches(atPosition(4, hasDescendant(withText("Dream #4")))))
            .check(matches(atPosition(5, hasDescendant(withText("Dream #5")))))
            .check(matches(atPosition(6, hasDescendant(withText("Dream #6")))))
            .check(matches(atPosition(7, hasDescendant(withText("Dream #7")))))
            .check(matches(atPosition(8, hasDescendant(withText("Dream #8")))))
            .check(matches(atPosition(9, hasDescendant(withText("Dream #9")))))
    }

    @Test
    fun dreamReflectionCountsDisplayedProperly() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(3, hasDescendant(withText("Reflections: 3")))))
            .check(matches(atPosition(4, hasDescendant(withText("Reflections: 0")))))
            .check(matches(atPosition(5, hasDescendant(withText("Reflections: 1")))))
            .check(matches(atPosition(6, hasDescendant(withText("Reflections: 2")))))
            .check(matches(atPosition(7, hasDescendant(withText("Reflections: 3")))))
    }

    @Test
    fun scrollRequiredToDisplayDreamFifty() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(not(atPosition(50, hasDescendant(withText("Dream #50"))))))
            .perform(scrollToPosition<DreamHolder>(50))
            .check(matches(atPosition(50, hasDescendant(withText("Dream #50")))))
    }

    @Test
    fun scrollToFiftyCantSeeFirstOrLast() {
        onView(withId(R.id.dream_recycler_view))
            .perform(scrollToPosition<DreamHolder>(50))
            .check(matches(not(atPosition(0, hasDescendant(withText("Dream #0"))))))
            .check(matches(not(atPosition(99, hasDescendant(withText("Dream #99"))))))
    }

    @Test
    fun lastFiveDreamsOnlyVisibleAfterScrollToBottom() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(not(atPosition(99, hasDescendant(withText("Dream #99"))))))
            .perform(scrollToPosition<DreamHolder>(99))
            .check(matches(atPosition(95, hasDescendant(withText("Dream #95")))))
            .check(matches(atPosition(96, hasDescendant(withText("Dream #96")))))
            .check(matches(atPosition(97, hasDescendant(withText("Dream #97")))))
            .check(matches(atPosition(98, hasDescendant(withText("Dream #98")))))
            .check(matches(atPosition(99, hasDescendant(withText("Dream #99")))))
    }

    @Test
    fun dreamFourShowsNoReflectionsAndDeferredImage() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(4, hasDescendant(withText("Dream #4")))))
            .check(matches(atPosition(4, hasDescendant(withText("Reflections: 0")))))
            .check(
                matches(
                    atPosition(
                        4,
                        hasDescendant(
                            allOf(
                                withId(R.id.list_item_image),
                                matchesDrawable(R.drawable.dream_deferred_icon),
                                withEffectiveVisibility(Visibility.VISIBLE)
                            )
                        )
                    )
                )
            )
    }

    @Test
    fun dreamFiveShowsOneReflectionAndFulfilledImage() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(5, hasDescendant(withText("Dream #5")))))
            .check(matches(atPosition(5, hasDescendant(withText("Reflections: 1")))))
            .check(
                matches(
                    atPosition(
                        5,
                        hasDescendant(
                            allOf(
                                withId(R.id.list_item_image),
                                matchesDrawable(R.drawable.dream_fulfilled_icon),
                                withEffectiveVisibility(Visibility.VISIBLE)
                            )
                        )
                    )
                )
            )
    }

    @Test
    fun dreamSixHasTwoReflectionsAndImageGone() {
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(6, hasDescendant(withText("Dream #6")))))
            .check(matches(atPosition(6, hasDescendant(withText("Reflections: 2")))))
            .check(
                matches(
                    atPosition(
                        6,
                        hasDescendant(
                            allOf(
                                withId(R.id.list_item_image),
                                withEffectiveVisibility(Visibility.GONE)
                            )
                        )
                    )
                )
            )
    }

    @Test
    fun scrollToBottomThenTopRetainsProperImages() {
        onView(withId(R.id.dream_recycler_view))
            .perform(scrollToPosition<DreamHolder>(99))
            .perform(scrollToPosition<DreamHolder>(0))

            .check(matches(atPosition(4, hasDescendant(withText("Dream #4")))))
            .check(
                matches(
                    atPosition(
                        4,
                        hasDescendant(
                            allOf(
                                withId(R.id.list_item_image),
                                matchesDrawable(R.drawable.dream_deferred_icon),
                                withEffectiveVisibility(Visibility.VISIBLE)
                            )
                        )
                    )
                )
            )
            .check(matches(atPosition(5, hasDescendant(withText("Dream #5")))))
            .check(
                matches(
                    atPosition(
                        5,
                        hasDescendant(
                            allOf(
                                withId(R.id.list_item_image),
                                matchesDrawable(R.drawable.dream_fulfilled_icon),
                                withEffectiveVisibility(Visibility.VISIBLE)
                            )
                        )
                    )
                )
            )
    }


    @Test
    fun changeDreamSixToDeferredWithThreeReflections() {
        val createdScenario: FragmentScenario<DreamListFragment> = launchFragmentInContainer(
            initialState = Lifecycle.State.CREATED
        )

        createdScenario.withFragment {
            val dlfVM: DreamListViewModel by viewModels()
            val testDream = dlfVM.dreams[6].copy(
                title = "New Dream #6",
            ).apply {
                entries = dlfVM.dreams[6].entries +
                        listOf(
                            DreamEntry(
                                kind = DreamEntryKind.REFLECTION,
                                text = "Third Reflection",
                                dreamId = id
                            ),
                            DreamEntry(
                                kind = DreamEntryKind.DEFERRED,
                                dreamId = id
                            )
                        )
            }
            dlfVM.dreams.set(6, testDream)
        }

        createdScenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(6, hasDescendant(withText("New Dream #6")))))
            .check(matches(atPosition(6, hasDescendant(withText("Reflections: 3")))))
            .check(
                matches(
                    atPosition(
                        6,
                        hasDescendant(
                            allOf(
                                withId(R.id.list_item_image),
                                matchesDrawable(R.drawable.dream_deferred_icon)
                            )
                        )
                    )
                )
            )

        createdScenario.close()
    }


    private fun matchesDrawable(resourceID: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("an ImageView with resourceID: ")
                description.appendValue(resourceID)
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                val expBM = imageView.context.resources
                    .getDrawable(resourceID, null).toBitmap()
                return imageView.drawable?.toBitmap()?.sameAs(expBM) ?: false
            }
        }
    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}