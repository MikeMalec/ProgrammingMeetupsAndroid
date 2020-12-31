package com.example.programmingmeetups.business.interactors.event.map

import com.example.programmingmeetups.business.data.network.event.EventNetworkDataSource
import com.example.programmingmeetups.business.data.util.safeApiCall
import com.example.programmingmeetups.business.domain.model.ProgrammingEvent
import com.example.programmingmeetups.business.domain.util.Resource
import com.example.programmingmeetups.di.EventNetworkDataSourceImplementation
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchEvents @Inject constructor(
    @EventNetworkDataSourceImplementation val eventNetworkDataSource: EventNetworkDataSource
) {
    suspend fun fetchEvents(
        token: String,
        position: LatLng,
        radius: Double,
        dispatcher: CoroutineDispatcher
    ): Resource<List<ProgrammingEvent>?> {
        val response = safeApiCall(dispatcher) {
            eventNetworkDataSource.fetchEvents(token, position, radius)
        }
        return response
    }
}