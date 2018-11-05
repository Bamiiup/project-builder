package aa0ndrey.projectbuilder.core.task

open class Task(
    override val name: String,
    override val run: ITask.() -> Unit = {},
    override val dependencies: List<String> = listOf(),
    override val addedDependencies: List<Pair<String, String>> = listOf()
): ITask {
    override fun run() = run.invoke(this)
}
