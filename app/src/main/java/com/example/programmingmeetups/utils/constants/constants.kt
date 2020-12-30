package com.example.programmingmeetups.utils

const val SOCKET_URL = "http://192.168.0.105:7000/"
const val BASE_URL = "http://192.168.0.105:7000/api/"
const val IMAGES_URL = "http://192.168.0.105:7000/uploads/"

//DI
const val PREFERENCES_IMPLEMENTATION = "PREFERENCES_IMPLEMENTATION"
const val AUTH_REPOSITORY_IMPL = "AUTH_REPOSITORY_IMPL"
const val AUTH_SERVICE_IMPL = "AUTH_SERVICE_IMPL"
const val REQUEST_BODY_FACTORY_IMPL = "REQUEST_BODY_FACTORY_IMPL"
const val AUTH_FACTORY_IMPL = "AUTH_FACTORY_IMPL"
const val FAKE_AUTH_FACTORY_IMPL = "FAKE_AUTH_FACTORY_IMPL"
const val MARKER_GLIDE = "MARKER_GLIDE"
const val IO_DISPATCHER = "IO_DISPATCHER"
const val LOCATION_MANAGER_IMPL = "LOCATION_MANAGER_IMPL"
const val LOCALIZATION_DISPATCHER_IMPL = "LOCALIZATION_DISPATCHER_IMPL"
const val FAKE_LOCALIZATION_DISPATCHER_IMPL = "FAKE_LOCALIZATION_DISPATCHER_IMPL"
const val COMMENT_SOCKET_MANAGER_IMPL = "COMMENT_SOCKET_MANAGER_IMPL"

//DI TESTING
const val FAKE_PREFERENCES = "FAKE_PREFERENCES"

//PREFERENCES
const val PREFERENCES_TOKEN = "PREFERENCES_TOKEN"
const val PREFERENCES_IMAGE = "PREFERENCES_IMAGE"
const val PREFERENCES_FIRST_NAME = "PREFERENCES_FIRST_NAME"
const val USER_ID = "USER_ID"
const val PREFERENCES_LAST_NAME = "PREFERENCES_LAST_NAME"
const val PREFERENCES_EMAIL = "PREFERENCES_EMAIL"
const val PREFERENCES_PASSWORD = "PREFERENCES_PASSWORD"
const val PREFERENCES_DESCRIPTION = "PREFERENCES_DESCRIPTION"
const val PREFERENCES_NAME = "ProgrammingMeetupsPreferences"

//DB
const val PROGRAMMING_MEETUPS_DB = "PROGRAMMING_MEETUPS_DB"
const val PROGRAMMING_EVENTS_TABLE = "PROGRAMMING_EVENTS_TABLE"

//FRAGMENTS
const val REGISTER_FRAGMENT = "Register"
const val LOGIN_FRAGMENT = "Login"
const val NETWORK_TIMEOUT = 6000L
const val CACHE_TIMEOUT = 6000L
const val ERROR_UNKNOWN = "Error Unknown"
const val ERROR_TIMEOUT = "Timeout Error"

//UI
const val INVALID_CREDENTIALS = "Invalid Credentials"
const val FILL_IN_ALL_FIELDS = "Fill in all fields"
const val SOMETHING_WENT_WRONG = "Something went wrong"
const val CHOOSE_DATE_FROM_FUTURE = "Choose date from future"
const val STORAGE_REQUEST = 1
const val LOCATION_REQUEST = 2
const val DATE_BOTTOM_DIALOG = "DATE_BOTTOM_DIALOG"
const val TAG_BOTTOM_DIALOG = "TAG_BOTTOM_DIALOG"
const val ENTER_TAG = "Enter tag"
const val PROGRAMMING_EVENT_CREATED = "Event has been created!"
const val EDIT_EVENT = "EDIT EVENT"
const val JOIN_EVENT = "JOIN EVENT"
const val LEAVE_EVENT = "LEAVE EVENT"
const val PARTICIPANTS_DIALOG = "PARTICIPANTS_DIALOG"
const val DELETE_EVENT = "Delete Event"
const val DELETE = "Delete"
const val Cancel = "Cancel"
const val PROFILE_SUCCESSFULLY_UPDATED = "Profile successfully updated"

//Rationales
const val LOCATION_RATIONALE = "Location permission is necessary to use this app"

//Map
const val LOCATION_UPDATE_INTERVAL = 300000L
const val FASTEST_LOCATION_INTERVAL = 2000L
const val MAP_ZOOM = 20f

//SOCKETS
const val NEW_COMMENT = "NEW_COMMENT"
const val JOIN_EVENT_COMMENTS = "JOIN_EVENT_COMMENTS"