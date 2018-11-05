package aa0ndrey.core.task

import aa0ndrey.projectbuilder.core.task.ITask
import aa0ndrey.projectbuilder.core.task.ITaskFactory

class TaskFactory(private val taskByName: Map<String, ITask>) : ITaskFactory {

    constructor(tasks: Collection<ITask>) : this(tasks.associateBy { it.name })

    override fun createTask(taskName: String): ITask {
        return taskByName[taskName]!!
    }
}