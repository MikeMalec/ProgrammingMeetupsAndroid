package com.example.programmingmeetups.framework.utils.pagination

import com.example.programmingmeetups.business.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher

abstract class PaginationAction {
    abstract suspend fun action(
        token: String,
        id: String,
        currentPage: Int,
        dispatcher: CoroutineDispatcher
    ): Resource<*>
}