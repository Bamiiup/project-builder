package aa0ndrey.projectbuilder.examples

import aa0ndrey.projectbuilder.core.task.TaskFactory
import aa0ndrey.projectbuilder.core.task.Task

class TaskFactoryImpl(private val taskByName: Map<String, Task>): TaskFactory {
    override fun createTask(taskName: String): Task {
        return taskByName[taskName]!!
    }
}