package edu.vt.cs5254.dreamcatcher

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class P2BTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun initialViewShowsFirstTenFromPrePopulatedData() {

        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("Learn mobile app development")))))
            .check(matches(atPosition(1, hasDescendant(withText("See aurora borealis/australis")))))
            .check(matches(atPosition(2, hasDescendant(withText("Ride in a hot air balloon")))))
            .check(matches(atPosition(3, hasDescendant(withText("Foster or adopt a pet")))))
            .check(matches(atPosition(4, hasDescendant(withText("Earn a graduate degree")))))
            .check(matches(atPosition(5, hasDescendant(withText("Try skydiving")))))
            .check(matches(atPosition(6, hasDescendant(withText("Swim with dolphins")))))
            .check(matches(atPosition(7, hasDescendant(withText("Start a fire without matches")))))
            .check(matches(atPosition(8, hasDescendant(withText("Travel to every continent")))))
            .check(matches(atPosition(9, hasDescendant(withText("Try water skiing")))))
    }

    @Test
    fun scrollingToBottomShowsLastTenFromPrePopulatedData() {
        onView(withId(R.id.dream_recycler_view))
            .perform(scrollToPosition<DreamHolder>(19))
            .check(matches(atPosition(10, hasDescendant(withText("Start a vegetable garden")))))
            .check(matches(atPosition(11, hasDescendant(withText("Learn web app development")))))
            .check(matches(atPosition(12, hasDescendant(withText("Write a book")))))
            .check(matches(atPosition(13, hasDescendant(withText("Visit Stonehenge")))))
            .check(matches(atPosition(14, hasDescendant(withText("Knit a scarf")))))
            .check(matches(atPosition(15, hasDescendant(withText("Try snow skiing")))))
            .check(matches(atPosition(16, hasDescendant(withText("Visit the Pyramids of Giza")))))
            .check(matches(atPosition(17, hasDescendant(withText("Learn how to juggle")))))
            .check(matches(atPosition(18, hasDescendant(withText("Tour the Tower of London")))))
            .check(matches(atPosition(19, hasDescendant(withText("Finish the DreamCatcher app")))))
    }

    @Test
    fun dreamFourCheckDetails() {
        onView(withText("Earn a graduate degree"))
            .perform(click())
        onView(withId(R.id.title_text))
            .check(matches(withText("Earn a graduate degree")))
        onView(withId(R.id.last_updated_text))
            .check(matches(withText("Last updated 2022-09-10 at 11:12:13 AM")))
        checkVisibleEntryText(0, "CONCEIVED")
        checkEntryGone(1)
        onView(withId(R.id.deferred_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(isEnabled()))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(isEnabled()))
    }

    @Test
    fun dreamFourRotateClickDeferThenBackRotateAndConfirmList() {
        onView(withText("Earn a graduate degree"))
            .perform(click())
        scenario.recreate()
        changeCheckboxState(CHECKBOX.DEFERRED, true)
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "DEFERRED")
        checkEntryGone(2)
        onView(withId(R.id.last_updated_text))
            .check(matches(not(withText("Last updated 2022-09-10 at 11:12:13 AM"))))
        pressBack()
        scenario.recreate()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0,
                        allOf(
                            hasDescendant(withText("Earn a graduate degree")),
                            hasDescendant(withText("Reflections: 0")),
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
            )
    }

    @Test
    fun dreamThreeNoImageInList() {
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        3,
                        allOf(
                            hasDescendant(withText("Foster or adopt a pet")),
                            hasDescendant(withText("Reflections: 3")),
                            hasDescendant(
                                allOf(
                                    withId(R.id.list_item_image),
                                    withEffectiveVisibility(Visibility.GONE)
                                )
                            )
                        )
                    )
                )
            )
    }
    @Test
    fun dreamNineChangeTitleClickFulfilledThenBackAndConfirmList() {
        onView(withText("Try water skiing"))
            .perform(click())
        changeCheckboxState(CHECKBOX.FULFILLED, true)
        onView(withId(R.id.title_text))
            .perform(replaceText("Try surfing"))
            .perform(closeSoftKeyboard())
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "One")
        checkVisibleEntryText(2, "FULFILLED")
        checkEntryGone(3)
        onView(withId(R.id.last_updated_text))
            .check(matches(not(withText("Last updated 2022-09-10 at 11:12:13 AM"))))
        onView(withId(R.id.title_text))
            .check(matches(withText("Try surfing")))
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0,
                        allOf(
                            hasDescendant(withText("Try surfing")),
                            hasDescendant(withText("Reflections: 1")),
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
            )
    }

    @Test
    fun dreamTwoThenBackThenDreamTwoCheckSameLastUpdated() {
        onView(withText("Ride in a hot air balloon"))
            .perform(click())
        onView(withId(R.id.title_text))
            .check(matches(withText("Ride in a hot air balloon")))
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "One")
        checkVisibleEntryText(2, "Two")
        checkVisibleEntryText(3, "DEFERRED")
        checkEntryGone(4)
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        2, allOf(
                            hasDescendant(withText("Ride in a hot air balloon")),
                            hasDescendant(withText("Reflections: 2"))
                        )
                    )
                )
            )
        onView(withText("Ride in a hot air balloon"))
            .perform(click())
        onView(withId(R.id.last_updated_text))
            .check(matches(withText("Last updated 2022-09-10 at 11:12:13 AM")))
    }

    @Test
    fun dreamSixFabGoneUntilUncheckFulfilled() {
        onView(withText("Swim with dolphins"))
            .perform(click())
        onView(withId(R.id.title_text))
            .check(matches(withText("Swim with dolphins")))
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "One")
        checkVisibleEntryText(2, "Two")
        checkVisibleEntryText(3, "FULFILLED")
        checkEntryGone(4)
        onView(withId(R.id.add_reflection_button))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        changeCheckboxState(CHECKBOX.FULFILLED, false)
        checkEntryGone(3)
        onView(withId(R.id.add_reflection_button))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun dreamTwoAddReflectionAfterDeferredThenUncheckRecheckDeferred() {
        onView(withText("Ride in a hot air balloon"))
            .perform(click())
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "One")
        checkVisibleEntryText(2, "Two")
        checkVisibleEntryText(3, "DEFERRED")
        checkEntryGone(4)
        onView(withId(R.id.deferred_checkbox))
            .check(matches(isChecked()))
            .check(matches(isEnabled()))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(not(isEnabled())))
        addReflection("Identified a facility")
        checkVisibleEntryText(3, "DEFERRED")
        checkVisibleEntryText(4, "Identified a facility")
        changeCheckboxState(CHECKBOX.DEFERRED, false)
        checkVisibleEntryText(3, "Identified a facility")
        checkEntryGone(4)
        changeCheckboxState(CHECKBOX.DEFERRED, true)
        checkVisibleEntryText(3, "Identified a facility")
        checkVisibleEntryText(4, "DEFERRED")
    }

    @Test
    fun dreamFourAddFourReflectionsAndCheckList() {
        onView(withText("Earn a graduate degree"))
            .perform(click())
        checkVisibleEntryText(0, "CONCEIVED")
        checkEntryGone(1)
        addReflection("First")
        addReflection("Second")
        addReflection("Third")
        addReflection("Fourth")
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "First")
        checkVisibleEntryText(2, "Second")
        checkVisibleEntryText(3, "Third")
        checkVisibleEntryText(4, "Fourth")
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText("Earn a graduate degree")),
                            hasDescendant(withText("Reflections: 4"))
                        )
                    )
                )
            )
    }

    @Test
    fun dreamEightUncheckDeferredCheckFulfilledConfirmLastUpdated() {
        onView(withText("Travel to every continent"))
            .perform(click())
        onView(withId(R.id.last_updated_text))
            .check(matches(withText("Last updated 2022-09-10 at 11:12:13 AM")))
        changeCheckboxState(CHECKBOX.DEFERRED, false)
        val timestamp1 = getText(withId(R.id.last_updated_text))
        assertNotEquals("Last updated 2022-09-10 at 11:12:13 AM", timestamp1)
        sleep(1000)
        changeCheckboxState(CHECKBOX.FULFILLED, true)
        val timestamp2 = getText(withId(R.id.last_updated_text))
        assertNotEquals(timestamp1, timestamp2)
    }

    @Test
    fun dreamSevenChangeTitleCheckLastUpdatedRotateBackCheckList() {
        onView(withText("Start a fire without matches"))
            .perform(click())
        onView(withId(R.id.title_text))
            .perform(replaceText("Start a fire"))
        scenario.recreate()
        onView(withId(R.id.last_updated_text))
            .check(matches(not(withText("Last updated 2022-09-10 at 11:12:13 AM"))))
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText("Start a fire")),
                            hasDescendant(withText("Reflections: 3"))
                        )
                    )
                )
            )
        scenario.recreate()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText("Start a fire")),
                            hasDescendant(withText("Reflections: 3"))
                        )
                    )
                )
            )
    }

    @Test
    fun dreamEighteenAddTwoReflectionsCheckLastUpdatedRotateBackCheckList() {
        onView(withId(R.id.dream_recycler_view)).perform(scrollToPosition<DreamHolder>(19))
        onView(withText("Tour the Tower of London"))
            .perform(click())
        addReflection("extra one")
        addReflection("extra two")
        scenario.recreate()

        onView(withId(R.id.last_updated_text))
            .check(matches(not(withText("Last updated 2022-09-10 at 11:12:13 AM"))))
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText("Tour the Tower of London")),
                            hasDescendant(withText("Reflections: 4"))
                        )
                    )
                )
            )
        scenario.recreate()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText("Tour the Tower of London")),
                            hasDescendant(withText("Reflections: 4"))
                        )
                    )
                )
            )
    }

    @Test
    fun dreamTwoExceedFiveEntriesNoDisplayButReflectionCountCorrect() {
        onView(withText("Ride in a hot air balloon"))
            .perform(click())
        addReflection("Three")
        addReflection("Four")
        addReflection("Five")
        addReflection("Six")
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "One")
        checkVisibleEntryText(2, "Two")
        checkVisibleEntryText(3, "DEFERRED")
        checkVisibleEntryText(4, "Three")
        scenario.recreate()
        checkVisibleEntryText(0, "CONCEIVED")
        checkVisibleEntryText(1, "One")
        checkVisibleEntryText(2, "Two")
        checkVisibleEntryText(3, "DEFERRED")
        checkVisibleEntryText(4, "Three")
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(
                matches(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText("Ride in a hot air balloon")),
                            hasDescendant(withText("Reflections: 6"))
                        )
                    )
                )
            )
    }

    @Test
    fun reverseOrderOfDreamsZeroOneTwo() {
        onView(withText("See aurora borealis/australis"))
            .perform(click())
        changeCheckboxState(CHECKBOX.DEFERRED, true)
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("See aurora borealis/australis")))))
            .check(matches(atPosition(1, hasDescendant(withText("Learn mobile app development")))))
            .check(matches(atPosition(2, hasDescendant(withText("Ride in a hot air balloon")))))
        onView(withText("Ride in a hot air balloon"))
            .perform(click())
        changeCheckboxState(CHECKBOX.DEFERRED, false)
        pressBack()
        onView(withId(R.id.dream_recycler_view))
            .check(matches(atPosition(0, hasDescendant(withText("Ride in a hot air balloon")))))
            .check(matches(atPosition(1, hasDescendant(withText("See aurora borealis/australis")))))
            .check(matches(atPosition(2, hasDescendant(withText("Learn mobile app development")))))
    }


    //  ------ PRIVATE HELPER METHODS BELOW HERE ------

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

    private fun addReflection(reflectionText: String) {
        onView(withId(R.id.add_reflection_button))
            .perform(click())
        onView(withId(R.id.reflection_text))
            .perform(replaceText(reflectionText))
        onView(withText("Add"))
            .inRoot(isDialog())
            .perform(click())
    }

    @IdRes
    private fun entryIdForNum(entryNum: Int): Int {
        return listOf(
            R.id.entry_0_button,
            R.id.entry_1_button,
            R.id.entry_2_button,
            R.id.entry_3_button,
            R.id.entry_4_button,
        )[entryNum]
    }

    private fun checkVisibleEntryText(entryNum: Int, expectedText: String) {
        onView(withId(entryIdForNum(entryNum)))
            .check(matches(withText(expectedText)))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    private fun checkEntryGone(entryNum: Int) {
        onView(withId(entryIdForNum(entryNum)))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    enum class CHECKBOX(@IdRes val id: Int) {
        DEFERRED(R.id.deferred_checkbox),
        FULFILLED(R.id.fulfilled_checkbox);

        fun other(): CHECKBOX = when (this) {
            DEFERRED -> FULFILLED
            FULFILLED -> DEFERRED
        }

    }

    private fun changeCheckboxState(checkBox: CHECKBOX, newCheckedState: Boolean) {
        val thisCB = onView(withId(checkBox.id))
        val thatCB = onView(withId(checkBox.other().id))
        if (newCheckedState) {
            thisCB.check(matches(isEnabled()))
            thisCB.check(matches(not(isChecked())))
            thisCB.perform(click())
            thisCB.check(matches(isChecked()))
            thisCB.check(matches(isEnabled()))
            thatCB.check(matches(not(isChecked())))
            thatCB.check(matches(not(isEnabled())))
        } else {
            thisCB.check(matches(isEnabled()))
            thisCB.check(matches(isChecked()))
            thisCB.perform(click())
            thisCB.check(matches(not(isChecked())))
            thisCB.check(matches(isEnabled()))
            thatCB.check(matches(not(isChecked())))
            thatCB.check(matches(isEnabled()))
        }
    }

    private fun getText(matcher: Matcher<View>): String {
        var text = ""
        onView(matcher).perform(
            object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(TextView::class.java)
                }

                override fun getDescription(): String {
                    return "Fetching text from TextView"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    text = (view as TextView).text.toString()
                }
            }
        )
        return text
    }
}