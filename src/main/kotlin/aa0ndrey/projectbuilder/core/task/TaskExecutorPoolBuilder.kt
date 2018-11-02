package aa0ndrey.projectbuilder.core.task

class TaskExecutorPoolBuilder {

    lateinit var initialTaskName: String
    lateinit var taskFactory: TaskFactory

    fun build(): TaskExecutorPool {
        val taskByName = LinkedHashMap<String, Task>()

        topologicalFillTaskByName(listOf(initialTaskName), taskByName)

        val taskExecutorByTaskName = HashMap<String, TaskExecutorImpl>()

        val taskExecutors = taskByName.values.reversed().map { task ->
            val dependentTaskExecutors = task.dependencies.map {
                taskExecutorByTaskName[it] ?: throw RuntimeException("Cycle dependency!")
            }

            TaskExecutorImpl(task, dependentTaskExecutors)
        }

        return TaskExecutorPool(taskExecutors)
    }

    private fun topologicalFillTaskByName(initialTaskNames: Collection<String>, filledTaskByName: LinkedHashMap<String, Task>) {
        val nextInitialTasks = mutableListOf<String>()

        initialTaskNames.forEach { taskName ->
            if (filledTaskByName.contains(taskName)) {
                return@forEach
            }

            val task = createTask(taskName)

            filledTaskByName += taskName to task

            nextInitialTasks += task.dependencies
        }

        topologicalFillTaskByName(nextInitialTasks, filledTaskByName)
    }

    private fun createTask(name: String) = taskFactory.createTask(name)
}