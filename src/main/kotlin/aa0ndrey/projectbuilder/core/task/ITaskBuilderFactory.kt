package aa0ndrey.projectbuilder.core.task

interface ITaskBuilderFactory {
    fun <T: ITask, B: ITaskBuilder> putTaskBuilder(taskClass: Class<T>, taskBuilder: Class<B>)
    fun <T: ITask> getTaskBuilder(taskClass: Class<T>): ITaskBuilder
}