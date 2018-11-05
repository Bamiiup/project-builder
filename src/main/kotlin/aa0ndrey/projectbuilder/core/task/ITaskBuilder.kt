package aa0ndrey.projectbuilder.core.task

interface ITaskBuilder {
    var dependencies: MutableList<String>
    fun useAsPrototype(prototype: ITask)
    fun build(): ITask
}