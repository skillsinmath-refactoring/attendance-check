package com.refactoring_android.math_skill

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.refactoring_android.math_skill.login.LoginActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class LoginUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginSuccess() {
        onView(withId(R.id.emailInput)).perform(typeText("testUser"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput)).perform(typeText("testPassword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
    }
    @Test
    fun testLoginFailure() {
        val decorViewRef = AtomicReference<View>()
        activityRule.scenario.onActivity { activity ->
            decorViewRef.set(activity.window.decorView)
        }

        // 입력
        onView(withId(R.id.emailInput)).perform(typeText("wrongUser"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput)).perform(typeText("wrongPassword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // ⏱️ Toast가 뜰 시간을 잠깐 기다려줌
        Thread.sleep(2000)

        // Toast 메시지 확인
        onView(withText("인증 정보가 일치하지 않습니다."))
            .inRoot(withDecorView(not(decorViewRef.get())))
            .check(matches(isDisplayed()))
    }

}
