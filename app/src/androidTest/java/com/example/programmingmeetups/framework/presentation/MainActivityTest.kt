package com.example.programmingmeetups.framework.presentation

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.programmingmeetups.R
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.datasource.preferences.AndroidFakePreferencesRepositoryImpl
import com.example.programmingmeetups.framework.utils.FAKE_PREFERENCES
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@UninstallModules(AppModule::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named(FAKE_PREFERENCES)
    lateinit var androidFakePreferencesRepositoryImpl: AndroidFakePreferencesRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun token_not_saved_in_data_store_navigates_to_auth_fragment() {
        val scenario = launchActivity<MainActivity>()
        onView(withId(R.id.registerBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun token_saved_in_data_store_navigates_to_map_fragment() {
        androidFakePreferencesRepositoryImpl.token = "token"
        val scenario = launchActivity<MainActivity>()
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
    }
}