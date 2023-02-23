package edu.vt.cs5254.dreamcatcher

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.vt.cs5254.dreamcatcher.DreamEntryKind.*
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DreamDetailFragmentTest {

    private lateinit var scenario: FragmentScenario<DreamDetailFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun initialTitleAndButtons() {
        onView(withId(R.id.title_text))
            .check(matches(withText("My First Dream")))
        onView(withId(R.id.entry_0_button))
            .check(matches(withText("CONCEIVED")))
        onView(withId(R.id.entry_1_button))
            .check(matches(withText("Reflection One")))
        onView(withId(R.id.entry_2_button))
            .check(matches(withText("Reflection Two")))
        onView(withId(R.id.entry_3_button))
            .check(matches(withText("DEFERRED")))
        onView(withId(R.id.entry_4_button))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun initialCheckboxes() {
        onView(withId(R.id.deferred_checkbox))
            .check(matches(isChecked()))
            .check(matches(isEnabled()))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun uncheckDeferred() {
        onView(withId(R.id.deferred_checkbox))
            .perform(click())
            .check(matches(not(isChecked())))
            .check(matches(isEnabled()))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(isEnabled()))
        onView(withId(R.id.entry_3_button))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun uncheckDeferredCheckDeferred() {
        onView(withId(R.id.deferred_checkbox))
            .perform(click())
            .perform(click())
            .check(matches(isChecked()))
            .check(matches(isEnabled()))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.entry_3_button))
            .check(matches(withText("DEFERRED")))
    }

    @Test
    fun uncheckDeferredCheckFulfilled() {
        onView(withId(R.id.deferred_checkbox))
            .perform(click())
        onView(withId(R.id.fulfilled_checkbox))
            .perform(click())
            .check(matches(isChecked()))
            .check(matches(isEnabled()))
        onView(withId(R.id.deferred_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.entry_3_button))
            .check(matches(withText("FULFILLED")))
    }

    @Test
    fun uncheckDeferredCheckFulfilledUncheckFulfilled() {
        onView(withId(R.id.deferred_checkbox))
            .perform(click())
        onView(withId(R.id.fulfilled_checkbox))
            .perform(click())
            .perform(click())
            .check(matches(not(isChecked())))
            .check(matches(isEnabled()))
        onView(withId(R.id.deferred_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(isEnabled()))
        onView(withId(R.id.entry_3_button))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun dreamInViewModelChangedByEditText() {
        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertEquals("My First Dream", ddfVM.dream.title)
        }

        onView(withId(R.id.title_text))
            .perform(replaceText("Different Dream"))

        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertEquals("Different Dream", ddfVM.dream.title)
        }
    }

    @Test
    fun dreamInViewModelChangedByCheckboxes() {
        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertFalse(ddfVM.dream.isFulfilled)
            assertTrue(ddfVM.dream.isDeferred)
            assertEquals(4, ddfVM.dream.entries.size)
            assertTrue(ddfVM.dream.entries[3].text.isEmpty())
            assertEquals(DEFERRED, ddfVM.dream.entries[3].kind)
        }

        onView(withId(R.id.deferred_checkbox))
            .perform(click())

        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertFalse(ddfVM.dream.isFulfilled)
            assertFalse(ddfVM.dream.isDeferred)
            assertEquals(3, ddfVM.dream.entries.size)
        }

        onView(withId(R.id.fulfilled_checkbox))
            .perform(click())

        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertTrue(ddfVM.dream.isFulfilled)
            assertFalse(ddfVM.dream.isDeferred)
            assertEquals(4, ddfVM.dream.entries.size)
            assertTrue(ddfVM.dream.entries[3].text.isEmpty())
            assertEquals(FULFILLED, ddfVM.dream.entries[3].kind)
        }

        onView(withId(R.id.fulfilled_checkbox))
            .perform(click())

        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertFalse(ddfVM.dream.isFulfilled)
            assertFalse(ddfVM.dream.isDeferred)
            assertEquals(3, ddfVM.dream.entries.size)
        }
    }

    @Test
    fun changesPersistAfterRotation() {
        onView(withId(R.id.deferred_checkbox))
            .perform(click())
        onView(withId(R.id.fulfilled_checkbox))
            .perform(click())
        onView(withId(R.id.title_text))
            .perform(replaceText("Another Dream"))

        scenario.recreate()

        onView(withId(R.id.deferred_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(isChecked()))
            .check(matches(isEnabled()))
        onView(withId(R.id.title_text))
            .check(matches(withText("Another Dream")))
        onView(withId(R.id.entry_3_button))
            .check(matches(withText("FULFILLED")))

        scenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            assertTrue(ddfVM.dream.isFulfilled)
            assertFalse(ddfVM.dream.isDeferred)
            assertEquals("Another Dream", ddfVM.dream.title)
            assertEquals(4, ddfVM.dream.entries.size)
            assertTrue(ddfVM.dream.entries[3].text.isEmpty())
            assertEquals(FULFILLED, ddfVM.dream.entries[3].kind)
        }
    }

    @Test
    fun displayFiveEntries() {

        val localScenario: FragmentScenario<DreamDetailFragment> = launchFragmentInContainer(
            initialState = Lifecycle.State.CREATED
        )

        localScenario.withFragment {
            val ddfVM: DreamDetailViewModel by viewModels()
            ddfVM.dream = ddfVM.dream.copy(title = "A Very Big Dream").apply {
                entries = listOf(
                    DreamEntry(kind = CONCEIVED, dreamId = ddfVM.dream.id),
                    DreamEntry(kind = REFLECTION, text = "Aaa", dreamId = ddfVM.dream.id),
                    DreamEntry(kind = REFLECTION, text = "Bbb", dreamId = ddfVM.dream.id),
                    DreamEntry(kind = REFLECTION, text = "Ccc", dreamId = ddfVM.dream.id),
                    DreamEntry(kind = FULFILLED, dreamId = ddfVM.dream.id)
                )
            }
        }

        localScenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.title_text))
            .check(matches(withText("A Very Big Dream")))
        onView(withId(R.id.entry_0_button))
            .check(matches(withText("CONCEIVED")))
        onView(withId(R.id.entry_1_button))
            .check(matches(withText("Aaa")))
        onView(withId(R.id.entry_2_button))
            .check(matches(withText("Bbb")))
        onView(withId(R.id.entry_3_button))
            .check(matches(withText("Ccc")))
        onView(withId(R.id.entry_4_button))
            .check(matches(withText("FULFILLED")))
        onView(withId(R.id.deferred_checkbox))
            .check(matches(not(isChecked())))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.fulfilled_checkbox))
            .check(matches(isChecked()))
            .check(matches(isEnabled()))

        localScenario.close()
    }
}

