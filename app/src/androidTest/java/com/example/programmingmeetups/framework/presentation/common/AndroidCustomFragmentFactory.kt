package com.example.programmingmeetups.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.programmingmeetups.framework.presentation.auth.AuthFragment
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModel
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager
import javax.inject.Inject

class AndroidCustomFragmentFactory @Inject constructor(
    val authViewModel: AuthViewModel,
    val frameworkContentManager: FrameworkContentManager
) :
    FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            AuthFragment::class.java.name ->
                AuthFragment(
                    frameworkContentManager,
                    authViewModel
                )
            else -> super.instantiate(classLoader, className)
        }
    }
}