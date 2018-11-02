package aa0ndrey.projectbuilder.core.task

import aa0ndrey.projectbuilder.core.MessageBroker
import aa0ndrey.projectbuilder.core.MessageBrokerListener
import java.util.UUID

//TODO: make notListen when it is not needed
class TaskExecutorPool(val allExecutors: List<TaskExecutor>) {

    val runningExecutors = LinkedHashSet<TaskExecutor>()
    val waitingExecutors = LinkedHashSet<TaskExecutor>()
    val failedExecutors = LinkedHashSet<TaskExecutor>()
    val finishedExecutors = LinkedHashSet<TaskExecutor>()
    val id: String = UUID.randomUUID().toString()
    var isStarted = false

    private val doneReceiver = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this@TaskExecutorPool) {
                val executor = message as TaskExecutor
                runningExecutors -= executor
                finishedExecutors += executor
                MessageBroker.send("taskExecutorPool.$id.updated", this@TaskExecutorPool)
            }
        }
    }

    private val failedReceiver = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this@TaskExecutorPool) {
                val executor = message as TaskExecutor
                runningExecutors -= executor
                failedExecutors += executor
                stop()
                MessageBroker.send("taskExecutorPool.$id.updated", this@TaskExecutorPool)
            }
        }
    }

    private val runReceiver = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this@TaskExecutorPool) {
                val executor = message as TaskExecutor
                waitingExecutors -= executor
                runningExecutors += executor
                MessageBroker.send("taskExecutorPool.$id.updated", this@TaskExecutorPool)
            }
        }
    }

    init {
        allExecutors.forEach { executor ->
            MessageBroker.listen("taskExecutor.${executor.id}.done", doneReceiver)
            MessageBroker.listen("taskExecutor.${executor.id}.failed", failedReceiver)
            MessageBroker.listen("taskExecutor.${executor.id}.run", runReceiver)
        }
    }

    @Synchronized
    fun start() {
        isStarted = true
        allExecutors.forEach { it.listeningDependencies() }
        waitingExecutors += allExecutors
        MessageBroker.send("taskExecutorPool.$id.updated", this)
        waitingExecutors.forEach { it.start() }
    }

    @Synchronized
    fun stop() {
        isStarted = false
        allExecutors.forEach { it.stop() }
    }
}