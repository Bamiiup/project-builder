package aa0ndrey.projectbuilder.log

import aa0ndrey.projectbuilder.core.task.ITask

interface ILoggableTask : ITask {
    val exceptionLogPatterns: Collection<String>

    fun getLog(): String

    fun checkIfLogsDontHavePatterns() {
        exceptionLogPatterns.forEach { pattern ->
            val exceptionText = "Task match exception pattern: '$pattern'. See log to get more details"
            checkIfLogDontHavePattern(pattern, getLog(), exceptionText)
        }
    }

    private fun checkIfLogDontHavePattern(pattern: String, log: String, exceptionText: String) {
        val findResult = Regex(pattern).find(log)
        if (findResult != null) {
            throw RuntimeException(exceptionText)
        }
    }
}