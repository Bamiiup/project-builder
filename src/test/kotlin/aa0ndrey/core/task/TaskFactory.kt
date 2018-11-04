package aa0ndrey.core.task

import aa0ndrey.projectbuilder.core.task.ITaskFactory
import aa0ndrey.projectbuilder.core.task.Task

class TaskFactory(private val taskByName: Map<String, Task>) : ITaskFactory {

    constructor(tasks: Collection<Task>) : this(tasks.associateBy { it.name })

    override fun createTask(taskName: String): Task {
        return taskByName[taskName]!!
    }
}