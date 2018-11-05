package aa0ndrey.core.log

import aa0ndrey.core.task.TaskFactory
import aa0ndrey.projectbuilder.cli.Input
import aa0ndrey.projectbuilder.cli.Output
import aa0ndrey.projectbuilder.core.task.TaskBuilderFactory
import aa0ndrey.projectbuilder.log.LoggableTask
import aa0ndrey.projectbuilder.log.LoggableTaskBuilder
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CyclicBarrier

class LoggableTaskTest {

    @Test
    fun taskTest() {
        val exceptionPtr = arrayOfNulls<RuntimeException>(1)
        val barrier = CyclicBarrier(2)

        val input = Input(TaskFactory(listOf(
            LoggableTask(
                name = "LoggableTask",
                run = {
                    try {
                        (this as LoggableTask).checkIfLogsDontHavePatterns()
                    } catch(e: RuntimeException) {
                        exceptionPtr[0] = e
                        throw e
                    } finally {
                        barrier.await()
                    }
                },
                exceptionLogPatterns = listOf("error")
            ).apply {
                outputLog = "some text, but all is ok"
                errorLog = "there is error"
            }
        )), TaskBuilderFactory().apply {
            putTaskBuilder(LoggableTask::class.java, LoggableTaskBuilder::class.java)
        }, Output())

        input.handle("LoggableTask")
        barrier.await()
        assertEquals("Task match exception pattern: error. See error log to get more details", exceptionPtr[0]!!.message)
    }
}