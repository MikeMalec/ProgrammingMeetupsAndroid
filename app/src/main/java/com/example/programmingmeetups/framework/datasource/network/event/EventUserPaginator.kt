package com.example.programmingmeetups.framework.datasource.network.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.programmingmeetups.business.domain.model.User
import com.example.programmingmeetups.business.domain.util.Resource.Success
import com.example.programmingmeetups.business.interactors.event.geteventusers.GetEventUsers
import com.example.programmingmeetups.framework.datasource.network.event.model.UsersResponse
import com.example.programmingmeetups.framework.utils.IO_DISPATCHER
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
import javax.inject.Named

class EventUserPaginator @Inject constructor(
    val getEventUsers: GetEventUsers,
    @Named(IO_DISPATCHER) var dispatcher: CoroutineDispatcher
) : UserPaginator {

    private val job = Job()

    private val fetchedEventUsers = mutableListOf<User>()
    private val _eventUsers: MutableLiveData<List<User>> = MutableLiveData()
    override val eventUsers = _eventUsers
    private var currentPage = 0
    private var pages = 1
    private var loading = false

    override fun paginate(token: String, eventId: String) {
        if (currentPage <= pages && !loading) {
            loading = true
            currentPage++
            CoroutineScope(dispatcher + job).launch {
                val response = getEventUsers.getEventUsers(token, eventId, currentPage, dispatcher)
                withContext(Main) {
                    loading = false
                }
                if (response is Success) {
                    response.data?.also {
                        dispatchResponse(response.data)
                    }
                }
            }
        }
    }

    private fun dispatchResponse(usersResponse: UsersResponse) {
        pages = usersResponse.pages
        fetchedEventUsers.addAll(usersResponse.users)
        _eventUsers.postValue(fetchedEventUsers)
    }

    fun reset() {
        currentPage = 0
        pages = 1
        loading = false
        fetchedEventUsers.clear()
        _eventUsers.postValue(fetchedEventUsers)
    }

    fun stop() {
        job.cancel()
    }
}

interface UserPaginator {
    fun paginate(token: String, eventId: String)
    val eventUsers: LiveData<List<User>>
}