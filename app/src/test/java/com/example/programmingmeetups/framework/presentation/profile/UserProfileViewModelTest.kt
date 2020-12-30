package com.example.programmingmeetups.framework.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.programmingmeetups.MainCoroutineRule
import com.example.programmingmeetups.business.data.network.auth.FakeAuthRepository
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.interactors.auth.UpdateProfile
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.FakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.preferences.FakePreferencesRepositoryImpl
import com.example.programmingmeetups.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var preferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var requestBodyFactory: FakeRequestBodyFactoryImpl
    private lateinit var updateProfile: UpdateProfile

    @Before
    fun setup() {
        preferencesRepository = FakePreferencesRepositoryImpl()
        requestBodyFactory = FakeRequestBodyFactoryImpl()
        fakeAuthRepository = FakeAuthRepository()
        updateProfile = UpdateProfile(fakeAuthRepository)
    }

    @Test
    fun updateProfile_success_confirmProfileUpdated() = runBlockingTest {
        userProfileViewModel = UserProfileViewModel(
            preferencesRepository,
            requestBodyFactory,
            updateProfile,
            mainCoroutineRule.dispatcher
        )
        val user = User("id", "firstName", "lastName", "email", "desc", "image")
        preferencesRepository.saveUserInfo(user)
        userProfileViewModel.updateProfile("new description")
        val userInfo = userProfileViewModel.userProfile.getOrAwaitValueTest()
        assertThat(userInfo).isEqualTo(
            User(
                "id",
                "firstName",
                "lastName",
                "email",
                "new description",
                "image"
            )
        )
    }

    @Test
    fun updateProfile_fail_confirmProfileNotUpdated() = runBlockingTest {
        userProfileViewModel = UserProfileViewModel(
            preferencesRepository,
            requestBodyFactory,
            updateProfile,
            mainCoroutineRule.dispatcher
        )
        val user = User("id", "firstName", "lastName", "email", "desc", "image")
        preferencesRepository.saveUserInfo(user)
        fakeAuthRepository.throwsException = true
        userProfileViewModel.updateProfile("new description")
        val userInfo = userProfileViewModel.userProfile.getOrAwaitValueTest()
        assertThat(userInfo).isEqualTo(
            user
        )
    }
}