package aa0ndrey.projectbuilder.core.task

open class Task(
    val name: String,
    val run: Task.() -> Unit = {},
    val dependencies: List<String> = listOf(),
    val addedDependencies: List<Pair<String, String>> = listOf()
) {
    fun run() = run.invoke(this)
}
