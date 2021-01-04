package com.example.programmingmeetups.framework.datasource.network.event.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.programmingmeetups.business.interactors.event.synchronizeuserevents.SynchronizeUserEvents
import com.example.programmingmeetups.framework.datasource.preferences.PreferencesRepository
import com.example.programmingmeetups.framework.utils.IO_DISPATCHER
import com.example.programmingmeetups.framework.utils.PREFERENCES_IMPLEMENTATION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class EventSynchronizationService : LifecycleService() {

    @Inject
    @Named(PREFERENCES_IMPLEMENTATION)
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var synchronizeUserEvents: SynchronizeUserEvents

    @Inject
    @Named(IO_DISPATCHER)
    lateinit var dispatcher: CoroutineDispatcher

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        synchronizeEvents()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun synchronizeEvents() {
        lifecycleScope.launch(dispatcher) {
            preferencesRepository.getToken().collect { token ->
                token?.also {
                    synchronizeUserEvents.synchronizeUserEvents(it, dispatcher)
                }
            }
        }
    }
}