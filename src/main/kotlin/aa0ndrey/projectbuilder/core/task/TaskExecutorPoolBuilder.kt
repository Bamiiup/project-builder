package aa0ndrey.projectbuilder.core.task

class TaskExecutorPoolBuilder {

    lateinit var initialTaskName: String
    lateinit var taskFactory: TaskFactory

    fun build(): TaskExecutorPool {
        val taskByName = LinkedHashMap<String, Task>()

        topologicalFillTaskByName(listOf(initialTaskName), taskByName)

        val taskExecutorByTaskName = HashMap<String, TaskExecutor>()

        taskByName.values.reversed().forEach { task ->
            val dependentTaskExecutors = task.dependencies.map {
                taskExecutorByTaskName[it] ?: throw RuntimeException("Cycle dependency!")
            }

            taskExecutorByTaskName += task.name to TaskExecutor(task, dependentTaskExecutors)
        }

        return TaskExecutorPool(taskExecutorByTaskName.values.toList())
    }

    //TODO: if there is cycle you get stack over flow. fix it
    private fun topologicalFillTaskByName(initialTaskNames: Collection<String>, filledTaskByName: LinkedHashMap<String, Task>) {
        val nextInitialTasks = mutableListOf<String>()

        initialTaskNames.forEach { taskName ->
            filledTaskByName -= taskName

            val task = createTask(taskName)

            filledTaskByName += taskName to task

            nextInitialTasks += task.dependencies
        }

        if (nextInitialTasks.size == 0) {
            return
        }

        topologicalFillTaskByName(nextInitialTasks, filledTaskByName)
    }

    private fun createTask(name: String) = taskFactory.createTask(name)
}
