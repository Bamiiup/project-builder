package aa0ndrey.projectbuilder.core.task

class TaskExecutorPoolBuilder(
    val initialTaskName: String,
    val taskFactory: ITaskFactory,
    val taskBuilderFactory: ITaskBuilderFactory
) {

    fun build(): TaskExecutorPool {
        val taskByName = createRawTasks(initialTaskName).also {
            addDependencies(it)
        }.let {
            topologicalSort(listOf(initialTaskName), it)
        }.entries.reversed().map { it.key to it.value }.toMap()

        val taskExecutorByTaskName = HashMap<String, TaskExecutor>()

        taskByName.values.forEach { task ->
            val dependentTaskExecutors = task.dependencies.map {
                taskExecutorByTaskName[it] ?: throw RuntimeException("Cycle dependency!")
            }

            taskExecutorByTaskName += task.name to TaskExecutor(task, dependentTaskExecutors)
        }

        return TaskExecutorPool(taskExecutorByTaskName.values.toList())
    }

    private fun createRawTasks(
        initialTaskName: String,
        taskByName: MutableMap<String, ITask> = mutableMapOf()
    ): MutableMap<String, ITask> {

        if (initialTaskName in taskByName) {
            return taskByName
        }

        val task = taskFactory.createTask(initialTaskName)
        taskByName += task.name to task
        task.dependencies.forEach { createRawTasks(it, taskByName) }
        task.addedDependencies.forEach { createRawTasks(it.second, taskByName) }
        return taskByName
    }

    private fun addDependencies(taskByName: MutableMap<String, ITask>) {
        val taskBuilderByName = mutableMapOf<String, ITaskBuilder>()
        taskByName.flatMap { it.value.addedDependencies }.distinct().forEach { (from, to) ->
            val taskBuilder = taskBuilderByName.getOrPut(from) {
                val prototype = taskByName[from]!!
                return@getOrPut taskBuilderFactory.getTaskBuilder(prototype::class.java).apply {
                    useAsPrototype(prototype)
                }
            }

            taskBuilder.dependencies.add(to)
        }

        taskBuilderByName.forEach { taskByName[it.key] = it.value.build() }
    }

    //TODO: if there is cycle you get stack over flow. fix it
    private fun topologicalSort(
        initialTaskNames: Collection<String>,
        taskByName: Map<String, ITask>,
        result: LinkedHashMap<String, ITask> = LinkedHashMap()
    ): LinkedHashMap<String, ITask> {

        val nextInitialTasks = mutableListOf<String>()

        initialTaskNames.forEach { taskName ->
            result -= taskName

            val task = taskByName[taskName]!!

            result[taskName] = task

            nextInitialTasks += task.dependencies
        }

        if (nextInitialTasks.isEmpty()) {
            return result
        }

        return topologicalSort(nextInitialTasks, taskByName, result)
    }
}
