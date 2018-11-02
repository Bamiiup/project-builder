package aa0ndrey.projectbuilder.core.task

import aa0ndrey.projectbuilder.core.MessageBroker
import aa0ndrey.projectbuilder.core.MessageBrokerListener
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class TaskExecutorImpl(task: Task, dependentTaskExecutors: Collection<TaskExecutorImpl>) : MessageBrokerListener {

    val id: String = UUID.randomUUID().toString()

    val task: Task

    private val doneTaskExecutors = ConcurrentHashMap<String, TaskExecutorImpl>()

    private val dependentTaskExecutors: Map<String, TaskExecutorImpl>

    private var canBeRun = false

    private var isStarted = false

    private val runningThread = Thread { run() }

    init {
        this.dependentTaskExecutors = dependentTaskExecutors.associateBy { it.id }
        this.task = task
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
        if (canBeRun) {
            run()
        }
    }

    @Synchronized
    fun stop() {
        isStarted = false

        if (runningThread.isAlive) {
            runningThread.interrupt()
        }

        dependentTaskExecutors.forEach { entry ->
            val taskExecutor = entry.value
            MessageBroker.notListen("taskExecutor.${taskExecutor.id}.done", this)
        }
    }

    @Synchronized
    override fun receive(message: Any) {
        handleTaskDone(message as TaskExecutorImpl)
    }

    private fun handleTaskDone(taskExecutor: TaskExecutorImpl) {
        doneTaskExecutors += taskExecutor.id to taskExecutor
        if (dependentTaskExecutors.keys.containsAll(doneTaskExecutors.keys)) {
            canBeRun = true
            if (isStarted) {
                run()
            }
        }
    }

    private fun run() {
        MessageBroker.send("taskExecutor.$id.run.${task.name}", this)
        try {
            task.run()
            MessageBroker.send("taskExecutor.$id.done.${task.name}", this)
        } catch (e: Exception) {
            MessageBroker.send("taskExecutor.$id.failed.${task.name}", this)
        }
    }
}