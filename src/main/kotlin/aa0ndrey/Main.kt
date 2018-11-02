package aa0ndrey

import aa0ndrey.projectbuilder.cli.Input
import aa0ndrey.projectbuilder.cli.Output
import aa0ndrey.projectbuilder.examples.TaskFactoryImpl
import aa0ndrey.projectbuilder.examples.taskByName
import java.util.Scanner

fun main(args: Array<String>) {
    val input = Input(TaskFactoryImpl(taskByName), Output())

    val scanner = Scanner(System.`in`)
    while (true) {
        try {
            val line = scanner.nextLine()

            input.handle(line)
        } catch (e: Exception) {
            continue
        }
    }
}

