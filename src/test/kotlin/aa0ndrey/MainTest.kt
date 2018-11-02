package aa0ndrey

import aa0ndrey.projectbuilder.core.task.TaskImpl
import org.junit.Test

class MainTest {
    @Test
    fun go() {
        TaskImpl(
            name = "name",
            run = {}
        )
    }
}
