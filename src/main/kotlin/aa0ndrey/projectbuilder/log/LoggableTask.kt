package aa0ndrey.projectbuilder.log

import aa0ndrey.projectbuilder.core.task.ITask

class LoggableTask(
    override val name: String,
    override val run: ITask.() -> Unit,
    override val dependencies: List<String> = listOf(),
    override val addedDependencies: List<Pair<String, String>> = listOf(),
    override val exceptionLogPatterns: Collection<String> = listOf()
) : ILoggableTask {
    override var outputLog: String = ""
    override var errorLog: String = ""
}