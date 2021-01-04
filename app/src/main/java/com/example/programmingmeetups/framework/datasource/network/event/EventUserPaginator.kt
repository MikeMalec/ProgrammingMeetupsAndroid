package com.example.programmingmeetups.framework.datasource.network.event

import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.interactors.event.geteventusers.GetEventUsers
import com.example.programmingmeetups.framework.utils.pagination.Paginator
import kotlinx.coroutines.*

class EventUserPaginator constructor(
    getEventUsers: GetEventUsers,
    dispatcher: CoroutineDispatcher
) : Paginator<User>(getEventUsers, dispatcher)
