package aa0ndrey.projectbuilder.core.task

class TaskBuilder {
    lateinit var prototype: Task
    lateinit var addDependency: List<String>

    fun build(): Task {
        val dependencies = mutableSetOf<String>()
        dependencies += prototype.dependencies
        dependencies += addDependency

        return Task(
            name = prototype.name,
            run = prototype.run,
            dependencies = dependencies.toList()
        )
    }
}
