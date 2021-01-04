package com.example.programmingmeetups.framework.presentation.events.showevent

sealed class EventAction {
    object EditEvent : EventAction() {
        override fun toString(): String {
            return "Edit Event"
        }
    }

    object JoinEvent : EventAction() {
        override fun toString(): String {
            return "Join Event"
        }
    }

    object LeaveEvent : EventAction() {
        override fun toString(): String {
            return "Leave Event"
        }
    }
}