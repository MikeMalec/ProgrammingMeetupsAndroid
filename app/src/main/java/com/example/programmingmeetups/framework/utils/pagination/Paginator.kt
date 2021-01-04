package com.example.programmingmeetups.framework.utils.pagination

import androidx.lifecycle.MutableLiveData
import com.example.programmingmeetups.business.domain.util.Resource
import kotlinx.coroutines.*

abstract class Paginator<T> constructor(
    val action: PaginationAction,
    val dispatcher: CoroutineDispatcher
) : PaginatorInterface<T> {
    private val job = Job()

    private val fetchedItems = mutableListOf<T>()
    private val _items: MutableLiveData<List<T>> = MutableLiveData()
    override val items = _items

    private var currentPage = 0
    private var pages = 1
    private var loading = false

    override fun paginate(token: String, id: String) {
        if (currentPage <= pages && !loading) {
            loading = true
            currentPage++
            CoroutineScope(dispatcher + job).launch {
                val response = action.action(token, id, currentPage, dispatcher)
                withContext(Dispatchers.Main) {
                    loading = false
                }
                if (response is Resource.Success<*>) {
                    response.data?.also {
                        dispatchResponse(response.data as PaginationResponse<T>)
                    }
                }
            }
        }
    }

    private fun dispatchResponse(response: PaginationResponse<T>) {
        pages = response.getAmountOfPages()
        fetchedItems.addAll(response.getItems())
        _items.postValue(fetchedItems)
    }

    override fun reset() {
        currentPage = 0
        pages = 1
        loading = false
        fetchedItems.clear()
        _items.postValue(fetchedItems)
    }

    override fun stop() {
        job.cancel()
    }
}