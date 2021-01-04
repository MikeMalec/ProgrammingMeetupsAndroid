package com.example.programmingmeetups.framework.utils.pagination

import androidx.lifecycle.LiveData

interface PaginatorInterface<T> {
    fun paginate(token: String, id: String)
    val items: LiveData<List<T>>
    fun reset() {}
    fun stop() {}
}