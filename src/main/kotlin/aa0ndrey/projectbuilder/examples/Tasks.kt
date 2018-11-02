package aa0ndrey.projectbuilder.examples

import aa0ndrey.projectbuilder.core.task.Task

val taskByName = listOf(
    Task(
        name = "hello1",
        run = {
            Thread.sleep(1000L)
            println("Hello1")
        }
    ),
    Task(
        name = "hello2",
        dependencies = listOf("hello1"),
        run = {
            Thread.sleep(5000L)
            println("Hello2")
        }
    ),
    Task(
        name = "hello3",
        dependencies = listOf("hello2", "hello1"),
        run = {
            Thread.sleep(1000L)
            println("Hello3")
        }
    ),
    Task(
        name = "another",
        run = {
            Thread.sleep(2000L)
            throw RuntimeException("Reason")
        }
    ),
    Task(
        name = "hello4",
        dependencies = listOf("hello3", "another"),
        run = { println("Hello4") }
    )
).associateBy { it.name }