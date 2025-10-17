package com.example.teeledger

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMainActivityViewsDisplayed() {
        // Check if the RecyclerView for balances is displayed
        onView(withId(R.id.balances_recycler_view))
            .check(matches(isDisplayed()))

        // Check if the FloatingActionButton for adding transactions is displayed
        onView(withId(R.id.add_transaction_fab))
            .check(matches(isDisplayed()))
    }
}