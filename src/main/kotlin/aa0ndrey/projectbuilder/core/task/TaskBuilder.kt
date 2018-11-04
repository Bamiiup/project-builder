package aa0ndrey.projectbuilder.core.task

class TaskBuilder {
    lateinit var prototype: Task
    var addedDependencies = mutableListOf<String>()

    fun build(): Task {
        val dependencies = mutableSetOf<String>()
        dependencies += prototype.dependencies
        dependencies += addedDependencies

        return Task(
            name = prototype.name,
            run = prototype.run,
            dependencies = dependencies.toList()
        )
    }
}
