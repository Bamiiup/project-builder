package aa0ndrey.projectbuilder.core.task

interface ITaskFactory {
    fun createTask(taskName: String): Task
}