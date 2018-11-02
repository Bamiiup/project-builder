package aa0ndrey.projectbuilder.cli

import aa0ndrey.projectbuilder.core.MessageBroker
import aa0ndrey.projectbuilder.core.MessageBrokerListener
import aa0ndrey.projectbuilder.core.task.TaskExecutorPool
import java.util.Date

class Output {

    fun add(taskExecutorPool: TaskExecutorPool) {
        MessageBroker.listen("taskExecutorPool.${taskExecutorPool.id}.updated", updateListener)
        print(taskExecutorPool)
    }

    private val updateListener = object : MessageBrokerListener {
        override fun receive(message: Any) {
            print(message as TaskExecutorPool)
        }
    }

    private fun print(pool: TaskExecutorPool) {
        println()
        println(Date())
        if (pool.isStarted) {
            println("COMPLETED:")
            pool.finishedExecutors.forEach {
                println(" - ${it.task.name}")
            }
            println("RUNNING:")
            pool.runningExecutors.forEach {
                println(" - ${it.task.name}")
            }
            println("WAITING:")
            pool.waitingExecutors.forEach {
                println(" - ${it.task.name}")
            }
            println("FAILED:")
            pool.failedExecutors.forEach {
                println(" - ${it.task.name}")
            }
        } else {
            println("ALL TASKS:")
            pool.allExecutors.forEach {
                println(" - ${it.task.name}")
            }
        }
    }
}