package aa0ndrey.projectbuilder.core.task

class TaskBuilder: ITaskBuilder {

    lateinit var name: String
    lateinit var run: ITask.() -> Unit
    override var dependencies = mutableListOf<String>()
    var addedDependencies = mutableListOf<Pair<String, String>>()

    override fun useAsPrototype(prototype: ITask) {
        name = prototype.name
        run = prototype.run
        dependencies = prototype.dependencies.toMutableList()
        addedDependencies = prototype.addedDependencies.toMutableList()
    }

    override fun build(): Task {
        return Task(name, run, dependencies, addedDependencies)
    }
}