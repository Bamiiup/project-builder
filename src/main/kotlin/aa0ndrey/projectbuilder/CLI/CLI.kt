package aa0ndrey.projectbuilder.CLI

object CLI {
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

    }
}