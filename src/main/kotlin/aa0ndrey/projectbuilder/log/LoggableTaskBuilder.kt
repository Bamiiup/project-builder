package aa0ndrey.projectbuilder.log

import aa0ndrey.projectbuilder.core.task.ITask
import aa0ndrey.projectbuilder.core.task.ITaskBuilder

class LoggableTaskBuilder: ITaskBuilder {

    lateinit var name: String
    lateinit var run: ITask.() -> Unit
    override var dependencies = mutableListOf<String>()
    var addedDependencies = mutableListOf<Pair<String, String>>()
    var exceptionLogPatterns = mutableListOf<String>()
    var errorLog = ""
    var outputLog = ""

    override fun useAsPrototype(prototype: ITask) {
        if (prototype !is LoggableTask) {
            throw RuntimeException("Prototype is not ${LoggableTask::class.java.simpleName}")
        }

        name = prototype.name
        run = prototype.run
        dependencies = prototype.dependencies.toMutableList()
        addedDependencies = prototype.addedDependencies.toMutableList()
        exceptionLogPatterns = prototype.exceptionLogPatterns.toMutableList()
        errorLog = prototype.errorLog
        outputLog = prototype.outputLog
    }

    override fun build(): LoggableTask {
        return LoggableTask(name, run, dependencies, addedDependencies, exceptionLogPatterns).apply {
            this@apply.errorLog = this@LoggableTaskBuilder.errorLog
            this@apply.outputLog = this@LoggableTaskBuilder.outputLog
        }
    }

}