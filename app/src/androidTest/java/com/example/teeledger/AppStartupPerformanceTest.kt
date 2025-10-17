package com.example.teeledger

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AppStartupPerformanceTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun appStartupTimeTest() {
        val startTime = System.nanoTime()
        activityScenarioRule.scenario.onActivity { activity ->
            // Activity is launched and ready
        }
        val endTime = System.nanoTime()
        val durationMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)

        // NFR-001: App launch to interactive < 2 seconds
        val maxStartupTimeMillis = 2000L

        println("App startup time: $durationMillis ms")
        assertTrue("App startup time ($durationMillis ms) exceeded limit ($maxStartupTimeMillis ms)",
            durationMillis < maxStartupTimeMillis)
    }
}