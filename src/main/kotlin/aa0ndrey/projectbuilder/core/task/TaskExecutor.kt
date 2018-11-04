package aa0ndrey.projectbuilder.core.task

import aa0ndrey.projectbuilder.core.MessageBroker
import aa0ndrey.projectbuilder.core.MessageBrokerListener
import java.util.*
import java.util.concurrent.ConcurrentHashMap

//TODO: make notListen when it is not needed
class TaskExecutor(val task: ITask, dependentTaskExecutors: Collection<TaskExecutor>) : MessageBrokerListener {

    val id: String = UUID.randomUUID().toString()

    var failedReason: Throwable? = null

    private val doneTaskExecutors = ConcurrentHashMap<String, TaskExecutor>()

    private val dependentTaskExecutors: Map<String, TaskExecutor> = dependentTaskExecutors.associateBy { it.id }

    private var isStarted = false

    private val runThread = Thread {
        MessageBroker.send("taskExecutor.$id.run", this)
        try {
            task.run()
            MessageBroker.send("taskExecutor.$id.done", this)
        } catch (e: Throwable) {
            failedReason = e
            MessageBroker.send("taskExecutor.$id.failed", this)
        }
    }

    @Synchronized
    fun listeningDependencies() {
        dependentTaskExecutors.forEach { entry ->
            val taskExecutor = entry.value
            MessageBroker.listen("taskExecutor.${taskExecutor.id}.done", this)
        }
    }

    @Synchronized
    fun start() {
        isStarted = true
        if (canBeRun()) {
            runThread.start()
        }
    }

    @Synchronized
    fun stop() {
        isStarted = false

        if (runThread.isAlive) {
            runThread.interrupt()
        }

        dependentTaskExecutors.forEach { entry ->
            val taskExecutor = entry.value
            MessageBroker.notListen("taskExecutor.${taskExecutor.id}.done", this)
        }
    }

    @Synchronized
    override fun receive(message: Any) {
        handleTaskDone(message as TaskExecutor)
    }

    private fun handleTaskDone(taskExecutor: TaskExecutor) {
        doneTaskExecutors += taskExecutor.id to taskExecutor
        if (canBeRun() && isStarted) {
            runThread.start()
        }
    }

    private fun canBeRun() = doneTaskExecutors.keys.containsAll(dependentTaskExecutors.keys)
}