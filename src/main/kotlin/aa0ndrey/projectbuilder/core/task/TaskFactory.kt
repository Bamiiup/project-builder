package aa0ndrey.projectbuilder.core.task

interface TaskFactory {
    fun createTask(taskName: String): Task
}