package aa0ndrey.projectbuilder.log

import aa0ndrey.projectbuilder.core.task.ITask

interface ILoggableTask : ITask {
    val outputLog: String
    val errorLog: String
    val exceptionLogPatterns: Collection<String>

    fun checkIfLogsDontHavePatterns() {
        exceptionLogPatterns.forEach { pattern ->
            val exceptionTextPattern = "Task match exception pattern: $pattern. See %s log to get more details"
            checkIfLogDontHavePattern(pattern, outputLog, String.format(exceptionTextPattern, "output"))
            checkIfLogDontHavePattern(pattern, errorLog, String.format(exceptionTextPattern, "error"))
        }
    }

    private fun checkIfLogDontHavePattern(pattern: String, log: String, exceptionText: String) {
        val findResult = Regex(pattern).find(log)
        if (findResult != null) {
            throw RuntimeException(exceptionText)
        }
    }
}