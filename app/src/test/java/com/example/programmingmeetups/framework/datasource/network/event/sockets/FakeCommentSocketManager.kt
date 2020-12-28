package com.example.programmingmeetups.framework.datasource.network.event.sockets

import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.*

class FakeCommentSocketManager() : EventCommentSocketManagerInterface {
    override val comments: Channel<ProgrammingEventCommentDto> = Channel(Channel.BUFFERED)

    override fun connect(eventId: String) {}

    override fun sendComment(token: String, eventId: String, comment: String) {
        comments.offer(
            ProgrammingEventCommentDto(
                User("id", "firstName", "lastName", "email", "desc", "img"),
                comment,
                "2020"
            )
        )
    }

    override fun disconnect() {}
}