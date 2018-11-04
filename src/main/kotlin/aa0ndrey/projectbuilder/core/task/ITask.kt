package aa0ndrey.projectbuilder.core.task

interface ITask {
    val name: String
    val run: Task.() -> Unit
    val dependencies: List<String>
    val addedDependencies: List<Pair<String, String>>
    fun run()
}