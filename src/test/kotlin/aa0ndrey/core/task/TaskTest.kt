package aa0ndrey.core.task

import aa0ndrey.projectbuilder.cli.Input
import aa0ndrey.projectbuilder.cli.Output
import aa0ndrey.projectbuilder.core.task.Task
import aa0ndrey.projectbuilder.core.task.TaskBuilderFactory
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import java.util.Collections.synchronizedList
import java.util.concurrent.CyclicBarrier

class TaskTest {
    @Test
    fun test() {
        val sequenceOfTaskCall = synchronizedList(mutableListOf<String>())
        val barrier = CyclicBarrier(2)

        val input = Input(TaskFactory(listOf(
            Task(
                name = "A",
                run = {
                    Thread.sleep(10)
                    sequenceOfTaskCall += name
                }
            ),
            Task(
                name = "B",
                run = {
                    sequenceOfTaskCall += name
                }
            ),
            Task(
                name = "C",
                dependencies = listOf("B"),
                addedDependencies = listOf("B" to "A"),
                run = {
                    sequenceOfTaskCall += name
                    barrier.await()
                }
            )
        )), TaskBuilderFactory(), Output())

        input.handle("C")
        barrier.await()
        assertArrayEquals(arrayOf("A", "B", "C"), sequenceOfTaskCall.toTypedArray())
    }
}