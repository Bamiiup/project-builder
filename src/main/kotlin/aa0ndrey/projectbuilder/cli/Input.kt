package aa0ndrey.projectbuilder.cli

import aa0ndrey.projectbuilder.core.task.ITaskFactory
import aa0ndrey.projectbuilder.core.task.TaskExecutorPoolBuilder

class Input(private val taskFactory: ITaskFactory, private val output: Output) {

    fun handle(input: String) {
        val inputParts = input.split(' ')

        if (inputParts.isEmpty()) {
            return
        }

        val commandName = inputParts[0]
        val commandArguments = inputParts.subList(1, inputParts.size)

        dispatchToCommandHandler(commandName, commandArguments)
    }

    private fun dispatchToCommandHandler(commandName: String, commandArguments: List<String>) {
        when(commandName) {
            "run" -> runTasks(commandName)
            else -> runTasks(commandName)
        }
    }

    private fun runTasks(taskName: String) {
        val pool = TaskExecutorPoolBuilder().apply {
            this@apply.taskFactory = this@Input.taskFactory
            this.initialTaskName = taskName
        }.build()

        output.add(pool)

        pool.start()
    }
}