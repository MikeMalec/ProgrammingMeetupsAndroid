package com.example.programmingmeetups.framework.datasource.network.event

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.interactors.event.user.GetUserEvents
import com.example.programmingmeetups.framework.utils.pagination.Paginator
import kotlinx.coroutines.CoroutineDispatcher

class UserEventPaginator constructor(
    getUserEvents: GetUserEvents,
    dispatcher: CoroutineDispatcher
) : Paginator<ProgrammingEvent>(getUserEvents, dispatcher)