package aa0ndrey.projectbuilder.core.task

open class TaskImpl(
    override val name: String,
    val run: Task.() -> Unit = {},
    override val dependencies: List<String> = listOf()
) : Task {

    override fun run() = run.invoke(this)

}