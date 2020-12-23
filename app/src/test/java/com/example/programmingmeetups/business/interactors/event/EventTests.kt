package com.example.programmingmeetups.business.interactors.event

import com.example.programmingmeetups.business.interactors.event.create.CreateEventTest
import com.example.programmingmeetups.business.interactors.event.map.SynchronizeProgrammingEventsTest
import com.example.programmingmeetups.business.interactors.event.user.GetUserEventsTest
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@InternalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    CreateEventTest::class,
    SynchronizeProgrammingEventsTest::class,
    GetUserEventsTest::class
)
class EventTests