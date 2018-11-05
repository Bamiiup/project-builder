package aa0ndrey.projectbuilder.log

import aa0ndrey.projectbuilder.core.task.ITask

open class LoggableTask(
    override val name: String,
    override val run: ITask.() -> Unit,
    override val dependencies: List<String> = listOf(),
    override val addedDependencies: List<Pair<String, String>> = listOf(),
    override val exceptionLogPatterns: Collection<String> = listOf()
) : ILoggableTask {

    private var _log = StringBuilder()

    open fun writeLog(text: String) {
        _log.append(text)
    }

    override fun getLog(): String {
        return _log.toString()
    }

    override fun run() {
        run.invoke(this)
        checkIfLogsDontHavePatterns()
    }
}