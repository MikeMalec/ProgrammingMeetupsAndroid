package com.example.programmingmeetups.framework.datasource.network.event.model

data class EventCommentResponse(
    val pages: Int,
    val comments: List<ProgrammingEventCommentDto>
)