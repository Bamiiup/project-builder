package aa0ndrey.projectbuilder.core.task

class TaskBuilderFactory : ITaskBuilderFactory {

    private val builderClassByTaskClass = mutableMapOf<Class<*>, Class<*>>()

    override fun <T: ITask, B: ITaskBuilder> putTaskBuilder(taskClass: Class<T>, taskBuilder: Class<B>) {
        builderClassByTaskClass[taskClass] = taskBuilder
    }

    override fun <T: ITask> getTaskBuilder(taskClass: Class<T>): ITaskBuilder {
        return builderClassByTaskClass[taskClass]!!.newInstance() as ITaskBuilder
    }
}