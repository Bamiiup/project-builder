package aa0ndrey.projectbuilder.core.task

interface Task {
    val name: String
    val dependencies: List<String>

    fun run()
}