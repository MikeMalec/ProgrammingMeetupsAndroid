package com.example.programmingmeetups.framework.datasource.network.event.model

import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.framework.utils.pagination.PaginationResponse

data class UserEventsResponse(val pages: Int, val events: List<ProgrammingEventDto>)

data class UserEventsPaginationResponse(val pages: Int, val events: List<ProgrammingEvent>) :
    PaginationResponse<ProgrammingEvent> {
    override fun getAmountOfPages(): Int {
        return pages
    }

    override fun getItems(): List<ProgrammingEvent> {
        return events
    }

}