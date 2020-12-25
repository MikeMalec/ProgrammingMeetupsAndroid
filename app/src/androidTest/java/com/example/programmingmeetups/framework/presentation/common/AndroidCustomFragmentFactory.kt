package com.example.programmingmeetups.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.programmingmeetups.business.data.cache.event.AndroidFakeEventCacheDataSourceImpl
import com.example.programmingmeetups.business.data.network.event.AndroidFakeEventNetworkDataSourceImpl
import com.example.programmingmeetups.business.interactors.event.create.CreateEvent
import com.example.programmingmeetups.business.interactors.event.join.JoinEvent
import com.example.programmingmeetups.business.interactors.event.leave.LeaveEvent
import com.example.programmingmeetups.business.interactors.event.user.GetUserEvents
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.AndroidFakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.event.utils.EventValidator
import com.example.programmingmeetups.framework.datasource.preferences.AndroidFakePreferencesRepositoryImpl
import com.example.programmingmeetups.framework.presentation.auth.AuthFragment
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModel
import com.example.programmingmeetups.framework.presentation.events.createevent.CreateEventFragment
import com.example.programmingmeetups.framework.presentation.events.createevent.CreateEventViewModel
import com.example.programmingmeetups.framework.presentation.events.showevent.EventFragment
import com.example.programmingmeetups.framework.presentation.events.showevent.EventViewModel
import com.example.programmingmeetups.framework.presentation.events.userevents.UserEventsFragment
import com.example.programmingmeetups.framework.presentation.events.userevents.UserEventsViewModel
import com.example.programmingmeetups.utils.FAKE_LOCALIZATION_DISPATCHER_IMPL
import com.example.programmingmeetups.utils.LOCALIZATION_DISPATCHER_IMPL
import com.example.programmingmeetups.utils.frameworkrequests.FrameworkContentManager
import com.example.programmingmeetups.utils.localization.LocalizationDispatcherInterface
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Named

class AndroidCustomFragmentFactory @Inject constructor(
    val authViewModel: AuthViewModel,
    val frameworkContentManager: FrameworkContentManager,
    @Named(LOCALIZATION_DISPATCHER_IMPL) val localizationDispatcherInterface: LocalizationDispatcherInterface,
    val androidFakePreferencesRepository: AndroidFakePreferencesRepositoryImpl
) :
    FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            AuthFragment::class.java.name ->
                AuthFragment(
                    frameworkContentManager,
                    authViewModel
                )
            CreateEventFragment::class.java.name -> CreateEventFragment(
                frameworkContentManager,
                CreateEventViewModel(
                    AndroidFakePreferencesRepositoryImpl(), AndroidFakeRequestBodyFactoryImpl(),
                    CreateEvent(
                        AndroidFakeEventNetworkDataSourceImpl(),
                        AndroidFakeEventCacheDataSourceImpl()
                    ),
                    EventValidator(),
                    Dispatchers.Main
                ),
                localizationDispatcherInterface
            )
            UserEventsFragment::class.java.name -> UserEventsFragment(
                UserEventsViewModel(
                    AndroidFakePreferencesRepositoryImpl(),
                    GetUserEvents(AndroidFakeEventCacheDataSourceImpl()),
                    Dispatchers.Main
                )
            )
            EventFragment::class.java.name -> EventFragment(
                EventViewModel(
                    androidFakePreferencesRepository,
                    JoinEvent(
                        AndroidFakeEventCacheDataSourceImpl(),
                        AndroidFakeEventNetworkDataSourceImpl()
                    ),
                    LeaveEvent(
                        AndroidFakeEventCacheDataSourceImpl(),
                        AndroidFakeEventNetworkDataSourceImpl()
                    ),
                    Dispatchers.Main
                )
            )
            else -> super.instantiate(classLoader, className)
        }
    }
}