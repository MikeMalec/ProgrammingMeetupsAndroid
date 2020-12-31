package com.example.programmingmeetups.framework.presentation.auth

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.programmingmeetups.R
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.di.AppModule
import com.example.programmingmeetups.framework.datasource.network.auth.data.response.AuthResponse
import com.example.programmingmeetups.framework.presentation.common.AndroidCustomFragmentFactory
import com.example.programmingmeetups.getOrAwaitValue
import com.example.programmingmeetups.launchFragmentInHiltContainer
import com.example.programmingmeetups.framework.utils.FILL_IN_ALL_FIELDS
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@UninstallModules(AppModule::class)
@MediumTest
@HiltAndroidTest
class AuthFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var androidCustomFragmentFactory: AndroidCustomFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun successfully_registers() = runBlockingTest {
        val uri = Uri.parse("test")
        var authViewModel: AuthViewModel? = null
        launchFragmentInHiltContainer<AuthFragment>(
            fragmentFactory = androidCustomFragmentFactory
        ) {
            authViewModel = this.authViewModel
            registerFragment.imageUri = uri
        }
        onView(withId(R.id.etFirstName)).perform(replaceText("firstName"))
        onView(withId(R.id.etLastName)).perform(replaceText("lastName"))
        onView(withId(R.id.etRegisterEmail)).perform(replaceText("email"))
        onView(withId(R.id.etRegisterPassword)).perform(replaceText("password"))
        onView(withId(R.id.registerBtn)).perform(click())
        val registerRequestResponse = authViewModel!!.registerRequestResponse.getOrAwaitValue()
        assertThat(registerRequestResponse.peekContent()).isEqualTo(
            Success(
                AuthResponse(
                    token = "token",
                    user = User(
                        "firstName",
                        "lastName",
                        "email",
                        "password",
                        "",
                        "image"
                    )
                )
            )
        )
        val token = authViewModel!!.token.getOrAwaitValue()
        assertThat(token).isEqualTo("token")
    }

    @Test
    fun fails_register_validation() = runBlockingTest {
        val uri = Uri.parse("test")
        var authViewModel: AuthViewModel? = null
        launchFragmentInHiltContainer<AuthFragment>(
            fragmentFactory = androidCustomFragmentFactory
        ) {
            authViewModel = this.authViewModel
            registerFragment.imageUri = uri
        }
        onView(withId(R.id.etFirstName)).perform(replaceText(""))
        onView(withId(R.id.etLastName)).perform(replaceText("lastName"))
        onView(withId(R.id.etRegisterEmail)).perform(replaceText("email"))
        onView(withId(R.id.etRegisterPassword)).perform(replaceText("password"))
        onView(withId(R.id.registerBtn)).perform(click())
        val registerValidationMessage = authViewModel!!.registerValidationMessage.getOrAwaitValue()
        assertThat(registerValidationMessage.peekContent()).isEqualTo(
            FILL_IN_ALL_FIELDS
        )
    }

    @Test
    fun successfully_logs_in() = runBlockingTest {
        var authViewModel: AuthViewModel? = null
        launchFragmentInHiltContainer<AuthFragment>(
            fragmentFactory = androidCustomFragmentFactory
        ) {
            authViewModel = this.authViewModel
        }
        onView(withId(R.id.authViewPager)).perform(swipeLeft())
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.etLoginEmail)).perform(replaceText("email"))
        onView(withId(R.id.etLoginPassword)).perform(replaceText("password"))
        onView(withId(R.id.loginBtn)).perform(click())
        onView(withId(R.id.loginBtn)).perform(click())
        val loginRequestResponse = authViewModel!!.loginRequestResponse.getOrAwaitValue(1)
        assertThat(loginRequestResponse.peekContent()).isEqualTo(
            Success(
                AuthResponse(
                    token = "token",
                    user = User(
                        "firstName",
                        "lastName",
                        "email",
                        "password",
                        "",
                        "image"
                    )
                )
            )
        )
        val token = authViewModel!!.token.getOrAwaitValue()
        assertThat(token).isEqualTo("token")
    }

    @Test
    fun login_fails_validation() = runBlockingTest {
        var authViewModel: AuthViewModel? = null
        launchFragmentInHiltContainer<AuthFragment>(
            fragmentFactory = androidCustomFragmentFactory
        ) {
            authViewModel = this.authViewModel
        }
        onView(withId(R.id.authViewPager)).perform(swipeLeft())
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.etLoginEmail)).perform(replaceText(""))
        onView(withId(R.id.etLoginPassword)).perform(replaceText("password"))
        onView(withId(R.id.loginBtn)).perform(click())
        onView(withId(R.id.loginBtn)).perform(click())
        val loginValidationMessage = authViewModel!!.loginValidationMessage.getOrAwaitValue()
        assertThat(loginValidationMessage.peekContent()).isEqualTo(
            FILL_IN_ALL_FIELDS
        )
    }
}