package com.example.programmingmeetups.framework.presentation.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.programmingmeetups.business.data.cache.event.AndroidFakeEventCacheDataSourceImpl
import com.example.programmingmeetups.business.data.network.auth.AndroidFakeAuthRepository
import com.example.programmingmeetups.business.data.network.event.AndroidFakeEventNetworkDataSourceImpl
import com.example.programmingmeetups.business.interactors.auth.UpdateProfile
import com.example.programmingmeetups.business.interactors.event.create.CreateEvent
import com.example.programmingmeetups.business.interactors.event.deleteevent.DeleteEvent
import com.example.programmingmeetups.business.interactors.event.getcomments.GetEventComments
import com.example.programmingmeetups.business.interactors.event.join.JoinEvent
import com.example.programmingmeetups.business.interactors.event.leave.LeaveEvent
import com.example.programmingmeetups.business.interactors.event.update.UpdateEvent
import com.example.programmingmeetups.business.interactors.event.user.GetUserEvents
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.AndroidFakeRequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.event.sockets.EventCommentSocketManagerInterface
import com.example.programmingmeetups.framework.datasource.network.event.utils.EventValidator
import com.example.programmingmeetups.framework.datasource.preferences.AndroidFakePreferencesRepositoryImpl
import com.example.programmingmeetups.framework.presentation.auth.AuthFragment
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModel
import com.example.programmingmeetups.framework.presentation.events.createevent.CreateEventFragment
import com.example.programmingmeetups.framework.presentation.events.createevent.CreateEventViewModel
import com.example.programmingmeetups.framework.presentation.events.eventcomments.EventCommentsFragment
import com.example.programmingmeetups.framework.presentation.events.eventcomments.EventCommentsViewModel
import com.example.programmingmeetups.framework.presentation.events.showevent.EventFragment
import com.example.programmingmeetups.framework.presentation.events.showevent.EventViewModel
import com.example.programmingmeetups.framework.presentation.events.updateevent.UpdateEventFragment
import com.example.programmingmeetups.framework.presentation.events.updateevent.UpdateEventViewModel
import com.example.programmingmeetups.framework.presentation.events.userevents.UserEventsFragment
import com.example.programmingmeetups.framework.presentation.events.userevents.UserEventsViewModel
import com.example.programmingmeetups.framework.presentation.profile.UserProfileFragment
import com.example.programmingmeetups.framework.presentation.profile.UserProfileViewModel
import com.example.programmingmeetups.framework.utils.COMMENT_SOCKET_MANAGER_IMPL
import com.example.programmingmeetups.framework.utils.LOCALIZATION_DISPATCHER_IMPL
import com.example.programmingmeetups.framework.utils.frameworkrequests.FrameworkContentManager
import com.example.programmingmeetups.framework.utils.localization.LocalizationDispatcherInterface
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Named

class AndroidCustomFragmentFactory @Inject constructor(
    val authViewModel: AuthViewModel,
    val frameworkContentManager: FrameworkContentManager,
    @Named(LOCALIZATION_DISPATCHER_IMPL) val localizationDispatcherInterface: LocalizationDispatcherInterface,
    val androidFakePreferencesRepository: AndroidFakePreferencesRepositoryImpl,
    @Named(COMMENT_SOCKET_MANAGER_IMPL) val eventCommentSocketManagerInterface: EventCommentSocketManagerInterface
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
            EventCommentsFragment::class.java.name -> EventCommentsFragment(
                EventCommentsViewModel(
                    androidFakePreferencesRepository,
                    eventCommentSocketManagerInterface,
                    GetEventComments(AndroidFakeEventNetworkDataSourceImpl()),
                    Dispatchers.Main
                )
            )
            UpdateEventFragment::class.java.name -> UpdateEventFragment(
                frameworkContentManager,
                UpdateEventViewModel(
                    androidFakePreferencesRepository,
                    AndroidFakeRequestBodyFactoryImpl(),
                    EventValidator(),
                    UpdateEvent(
                        AndroidFakeEventNetworkDataSourceImpl(),
                        AndroidFakeEventCacheDataSourceImpl()
                    ),
                    DeleteEvent(
                        AndroidFakeEventCacheDataSourceImpl(),
                        AndroidFakeEventNetworkDataSourceImpl()
                    ),
                    Dispatchers.Main
                )
            )
            UserProfileFragment::class.java.name -> UserProfileFragment(
                frameworkContentManager,
                UserProfileViewModel(
                    androidFakePreferencesRepository,
                    AndroidFakeRequestBodyFactoryImpl(),
                    UpdateProfile(AndroidFakeAuthRepository()),
                    Dispatchers.Main
                )
            )
            else -> super.instantiate(classLoader, className)
        }
    }
}