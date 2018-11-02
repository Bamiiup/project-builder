package aa0ndrey.projectbuilder.core.task

import aa0ndrey.projectbuilder.core.MessageBroker
import aa0ndrey.projectbuilder.core.MessageBrokerListener
import java.util.UUID

class TaskExecutorPool(private val allExecutors: List<TaskExecutorImpl>) {

    val runningExecutors = LinkedHashSet<TaskExecutorImpl>()
    val waitingExecutors = LinkedHashSet<TaskExecutorImpl>()
    val failedExecutors = LinkedHashSet<TaskExecutorImpl>()
    val finishedExecutors = LinkedHashSet<TaskExecutorImpl>()
    val id: String = UUID.randomUUID().toString()

    init {
        allExecutors.forEach { executor ->
            MessageBroker.listen("taskExecutor.${executor.id}.done.${executor.task.name}", doneReceiver)
            MessageBroker.listen("taskExecutor.${executor.id}.failed.${executor.task.name}", failedReceiver)
            MessageBroker.listen("taskExecutor.${executor.id}.run.${executor.task.name}", runReceiver)
        }
    }

    private val doneReceiver = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this@TaskExecutorPool) {
                val executor = message as TaskExecutorImpl
                runningExecutors -= executor
                finishedExecutors += executor
                MessageBroker.send("taskExecutorPool.$id.updated", this)
            }
        }
    }

    private val failedReceiver = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this@TaskExecutorPool) {
                val executor = message as TaskExecutorImpl
                runningExecutors -= executor
                failedExecutors += executor
                stop()
                MessageBroker.send("taskExecutorPool.$id.updated", this)
            }
        }
    }

    private val runReceiver = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this@TaskExecutorPool) {
                val executor = message as TaskExecutorImpl
                waitingExecutors -= executor
                runningExecutors += executor
                MessageBroker.send("taskExecutorPool.$id.updated", this)
            }
        }
    }

    @Synchronized
    fun start() {
        allExecutors.forEach { it.listeningDependencies() }
        waitingExecutors += allExecutors
        MessageBroker.send("taskExecutorPool.$id.updated", this)
        waitingExecutors.forEach { it.start() }
    }

    @Synchronized
    fun stop() {
        allExecutors.forEach { it.stop() }
    }
}