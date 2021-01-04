package com.example.programmingmeetups.framework.presentation.events.users

import androidx.hilt.lifecycle.ViewModelInject
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.presentation.events.common.ViewModelWithToken
import com.example.programmingmeetups.framework.utils.IO_DISPATCHER
import com.example.programmingmeetups.framework.utils.PREFERENCES_IMPLEMENTATION
import com.example.programmingmeetups.framework.utils.USER_EVENTS_PAGINATOR
import com.example.programmingmeetups.framework.utils.pagination.PaginatorInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

class EventUserViewModel @ViewModelInject constructor(
    @Named(PREFERENCES_IMPLEMENTATION) preferencesRepository: PreferencesRepository,
    @Named(USER_EVENTS_PAGINATOR) val userEventPaginator:
    PaginatorInterface<ProgrammingEvent>,
    @Named(IO_DISPATCHER)
    var dispatcher: CoroutineDispatcher
) : ViewModelWithToken(preferencesRepository),
    PaginatorInterface<ProgrammingEvent> by userEventPaginator {
    val events = userEventPaginator.items
}