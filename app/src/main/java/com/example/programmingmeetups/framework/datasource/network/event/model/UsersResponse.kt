package com.example.programmingmeetups.framework.datasource.network.event.model

import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.framework.utils.pagination.PaginationResponse

data class UsersResponse(val pages: Int, val users: List<User>) : PaginationResponse<User> {
    override fun getAmountOfPages(): Int {
        return pages
    }

    override fun getItems(): List<User> {
        return users
    }

}