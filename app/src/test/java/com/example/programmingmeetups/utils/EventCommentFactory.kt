package com.example.programmingmeetups.utils

import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto

class EventCommentFactory {
    fun createComments(amount: Int): List<ProgrammingEventCommentDto> {
        return (0 until amount).map {
            ProgrammingEventCommentDto(
                User(
                    "id",
                    "firstName",
                    "lastName",
                    "email",
                    "desc",
                    "image"
                ), "comment #$amount", "2020"
            )
        }
    }
}