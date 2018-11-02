package aa0ndrey.projectbuilder.core.task

open class Task(
    val name: String,
    val run: Task.() -> Unit = {},
    val dependencies: List<String> = listOf()
) {
    fun run() = run.invoke(this)
}