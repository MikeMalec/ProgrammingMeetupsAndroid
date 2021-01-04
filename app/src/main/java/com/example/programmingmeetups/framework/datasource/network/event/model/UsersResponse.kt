package com.example.programmingmeetups.framework.datasource.network.event.model

import com.example.programmingmeetups.business.domain.model.User

data class UsersResponse(val pages: Int, val users: List<User>)