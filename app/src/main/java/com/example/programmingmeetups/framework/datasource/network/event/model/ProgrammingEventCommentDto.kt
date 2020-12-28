package com.example.programmingmeetups.framework.datasource.network.event.model

import com.example.programmingmeetups.business.domain.model.User

data class ProgrammingEventCommentDto(
    val user: User,
    val comment: String,
    val createdAt: String
)