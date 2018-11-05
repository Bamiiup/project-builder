package aa0ndrey.core.log

import aa0ndrey.core.task.TaskFactory
import aa0ndrey.projectbuilder.cli.Input
import aa0ndrey.projectbuilder.cli.Output
import aa0ndrey.projectbuilder.core.task.ITask
import aa0ndrey.projectbuilder.log.ILoggableTask
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CyclicBarrier

class LoggableTaskTest {

    @Test
    fun taskTest() {
        val exceptionPtr = arrayOfNulls<RuntimeException>(1)
        val barrier = CyclicBarrier(2)

        val input = Input(TaskFactory(listOf(
            object: ILoggableTask {
                override val outputLog: String
                    get() = "some text, but all is ok"
                override val errorLog: String
                    get() = "there is error"
                override val exceptionLogPatterns: Collection<String>
                    get() = listOf("error")
                override val name: String
                    get() = "LoggableTask"
                override val run: ITask.() -> Unit
                    get() = {
                        try {
                            checkIfLogsDontHavePatterns()
                        } catch (e: RuntimeException) {
                            exceptionPtr[0] = e
                            throw e
                        } finally {
                            barrier.await()
                        }
                    }
                override val dependencies: List<String>
                    get() = listOf()
                override val addedDependencies: List<Pair<String, String>>
                    get() = listOf()
            }
        )), Output())

        input.handle("LoggableTask")
        barrier.await()
        assertEquals("Task match exception pattern: error. See error log to get more details", exceptionPtr[0]!!.message)
    }
}