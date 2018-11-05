package aa0ndrey.projectbuilder.core.task

class TaskBuilderFactory(
    private val builderClassByTaskClass: Map<Class<*>, Class<*>> = mapOf(Task::class.java to TaskBuilder::class.java)
): ITaskBuilderFactory {

    override fun <T : ITask> getTaskBuilder(taskClass: Class<T>): ITaskBuilder {
        return builderClassByTaskClass[taskClass]!!.newInstance() as ITaskBuilder
    }
}