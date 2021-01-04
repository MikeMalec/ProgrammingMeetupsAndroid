package com.example.programmingmeetups.framework.utils.pagination

interface PaginationResponse<T> {
    fun getAmountOfPages(): Int
    fun getItems(): List<T>
}