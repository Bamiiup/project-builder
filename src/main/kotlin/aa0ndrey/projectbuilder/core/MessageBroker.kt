package aa0ndrey.projectbuilder.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

object MessageBroker {

    private val listenersByChannel = ConcurrentHashMap<String, ConcurrentLinkedQueue<MessageBrokerListener>>()

    fun listen(channel: String, listener: MessageBrokerListener) {
        listenersByChannel.getOrPut(channel, { ConcurrentLinkedQueue() }) += listener
    }

    fun notListen(channel: String, listener: MessageBrokerListener) {
        val listeners = listenersByChannel.getOrPut(channel, { ConcurrentLinkedQueue() })

        listeners -= listener

        if (listeners.size == 0) {
            listenersByChannel.remove(channel)
        }
    }

    fun send(channel: String, message: Any) {
        listenersByChannel.getOrPut(channel, { ConcurrentLinkedQueue() }).forEach { it.receive(message) }
    }
}

interface MessageBrokerListener {
    fun receive(message: Any)
}