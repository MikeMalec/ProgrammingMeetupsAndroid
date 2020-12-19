package com.example.programmingmeetups.business.domain.util

class Event<out T>(private val value: T) {
    private var fetchedValue = false

    fun getContent(): T? {
        if (!fetchedValue) {
            fetchedValue = true
            return value
        }
        return null
    }

    fun peekContent() = value
}