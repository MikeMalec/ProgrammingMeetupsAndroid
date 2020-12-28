package com.example.programmingmeetups.framework.datasource.network.event.sockets

import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import kotlinx.coroutines.channels.Channel

interface EventCommentSocketManagerInterface {
    val comments: Channel<ProgrammingEventCommentDto>
    fun connect(eventId: String)
    fun sendComment(token: String, eventId: String, comment: String)
    fun disconnect()
}