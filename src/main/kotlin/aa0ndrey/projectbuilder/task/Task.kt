package aa0ndrey.projectbuilder.task

import java.util.UUID

interface Task {
    val name: String
    val id: UUID
    val dependencies: List<Dependency>

    fun run(): Result
}