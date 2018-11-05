package aa0ndrey.projectbuilder.core.task

interface ITaskBuilderFactory {
    fun <T: ITask> getTaskBuilder(taskClass: Class<T>): ITaskBuilder
}