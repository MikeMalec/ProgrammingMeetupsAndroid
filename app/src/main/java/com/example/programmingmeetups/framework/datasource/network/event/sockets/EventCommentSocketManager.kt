package com.example.programmingmeetups.framework.datasource.network.event.sockets

import android.util.Log
import com.example.programmingmeetups.framework.datasource.network.event.model.ProgrammingEventCommentDto
import com.example.programmingmeetups.utils.JOIN_EVENT_COMMENTS
import com.example.programmingmeetups.utils.NEW_COMMENT
import com.example.programmingmeetups.utils.SOCKET_URL
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.Transport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class EventCommentSocketManager constructor(private val gson: Gson) :
    EventCommentSocketManagerInterface {
    private lateinit var socket: Socket
    private var job = Job()

    override val comments = Channel<ProgrammingEventCommentDto>(Channel.BUFFERED)

    override fun connect(eventId: String) {
        socket = IO.socket(SOCKET_URL)
        socket.connect()
        socket.emit(JOIN_EVENT_COMMENTS, eventId)
        socket.on(NEW_COMMENT) { args: Array<out Any>? -> onNewComment(args) }
        setErrorListener()
    }

    private fun onNewComment(args: Array<out Any>?) {
        args?.also {
            val comment = gson.fromJson(it[0].toString(), ProgrammingEventCommentDto::class.java)
            CoroutineScope(Dispatchers.IO + job).launch {
                comments.send(comment)
            }
        }
    }

    override fun sendComment(token: String, eventId: String, comment: String) {
        socket.emit(NEW_COMMENT, token, eventId, comment)
    }

    override fun disconnect() {
        job.cancel()
        socket.disconnect()
    }

    private fun setErrorListener() {
        socket.io().on(Manager.EVENT_TRANSPORT, Emitter.Listener { args ->
            val transport: Transport = args[0] as Transport
            transport.on(Transport.EVENT_ERROR, Emitter.Listener { args ->
                val e = args[0] as Exception
                Log.e("XXX", "Transport error $e")
                e.printStackTrace()
                e.cause!!.printStackTrace()
            })
        })
    }
}