package com.example.programmingmeetups.business.interactors.auth

import com.example.programmingmeetups.framework.datasource.network.auth.utils.AuthValidatorTest
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelTest
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@InternalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    RegisterTest::class,
    LoginTest::class,
    AuthValidatorTest::class,
    AuthViewModelTest::class
)
class AuthTests