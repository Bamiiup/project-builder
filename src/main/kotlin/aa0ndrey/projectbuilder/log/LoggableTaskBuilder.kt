package aa0ndrey.projectbuilder.log

import aa0ndrey.projectbuilder.core.task.ITask
import aa0ndrey.projectbuilder.core.task.ITaskBuilder

class LoggableTaskBuilder: ITaskBuilder {

    lateinit var name: String
    lateinit var run: ITask.() -> Unit
    override var dependencies = mutableListOf<String>()
    var addedDependencies = mutableListOf<Pair<String, String>>()
    var exceptionLogPatterns = mutableListOf<String>()

    override fun useAsPrototype(prototype: ITask) {
        if (prototype !is LoggableTask) {
            throw RuntimeException("Prototype is not ${LoggableTask::class.java.simpleName}")
        }

        name = prototype.name
        run = prototype.run
        dependencies = prototype.dependencies.toMutableList()
        addedDependencies = prototype.addedDependencies.toMutableList()
        exceptionLogPatterns = prototype.exceptionLogPatterns.toMutableList()
    }

    override fun build(): LoggableTask {
        return LoggableTask(name, run, dependencies, addedDependencies, exceptionLogPatterns)
    }

}