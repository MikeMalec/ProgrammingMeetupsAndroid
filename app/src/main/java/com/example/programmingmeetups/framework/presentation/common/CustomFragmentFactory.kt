package com.example.programmingmeetups.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.programmingmeetups.framework.presentation.auth.AuthFragment
import com.example.programmingmeetups.framework.presentation.events.createevent.CreateEventFragment
import com.example.programmingmeetups.framework.presentation.events.updateevent.UpdateEventFragment
import com.example.programmingmeetups.framework.presentation.profile.UserProfileFragment
import com.example.programmingmeetups.framework.utils.LOCALIZATION_DISPATCHER_IMPL
import com.example.programmingmeetups.framework.utils.frameworkrequests.FrameworkContentManager
import com.example.programmingmeetups.framework.utils.localization.LocalizationDispatcherInterface
import javax.inject.Inject
import javax.inject.Named

class CustomFragmentFactory @Inject constructor(
    val frameworkContentManager: FrameworkContentManager,
    @Named(LOCALIZATION_DISPATCHER_IMPL) val localizationDispatcherInterface: LocalizationDispatcherInterface
) :
    FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            AuthFragment::class.java.name -> AuthFragment(frameworkContentManager)
            CreateEventFragment::class.java.name -> CreateEventFragment(
                frameworkContentManager,
                localizationDispatcher = localizationDispatcherInterface
            )
            UpdateEventFragment::class.java.name -> UpdateEventFragment(frameworkContentManager)
            UserProfileFragment::class.java.name -> UserProfileFragment(frameworkContentManager)
            else -> super.instantiate(classLoader, className)
        }
    }
}