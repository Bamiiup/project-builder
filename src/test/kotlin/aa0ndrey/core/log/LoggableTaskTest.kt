package aa0ndrey.core.log

import aa0ndrey.projectbuilder.log.LoggableTask
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class LoggableTaskTest {

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()

    @Test
    fun taskTest() {

        expectedException.expect(RuntimeException::class.java)
        expectedException.expectMessage("Task match exception pattern: 'error'. See log to get more details")

        LoggableTask(
            name = "LoggableTask",
            run = {
                (this as LoggableTask).writeLog("hello! Here is error!")
            },
            exceptionLogPatterns = listOf("error")
        ).run()
    }
}