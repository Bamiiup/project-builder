package aa0ndrey.projectbuilder.cli

import aa0ndrey.projectbuilder.core.MessageBroker
import aa0ndrey.projectbuilder.core.MessageBrokerListener
import aa0ndrey.projectbuilder.core.task.TaskExecutorPool
import java.util.Date

class Output {

    fun add(taskExecutorPool: TaskExecutorPool) {
        MessageBroker.listen("taskExecutorPool.${taskExecutorPool.id}.updated", updateListener)
    }

    private val updateListener = object : MessageBrokerListener {
        override fun receive(message: Any) {
            synchronized(this) {
                val taskExecutorPool = message as TaskExecutorPool

                print(taskExecutorPool)

                //TODO: move from here and add status for task executor pool
                if ((!taskExecutorPool.failedExecutors.isEmpty())
                    || (taskExecutorPool.finishedExecutors.size == taskExecutorPool.allExecutors.size)) {
                    MessageBroker.notListen("taskExecutorPool.${taskExecutorPool.id}.updated", this)
                }
            }
        }
    }

    private fun print(pool: TaskExecutorPool) {
        println()
        println(Date())
        if (!pool.finishedExecutors.isEmpty()) {
            println("COMPLETED:")
            pool.finishedExecutors.forEach {
                println(" - ${it.task.name}")
            }
        }
        if (!pool.runningExecutors.isEmpty()) {
            println("RUNNING:")
            pool.runningExecutors.forEach {
                println(" - ${it.task.name}")
            }
        }
        if (!pool.waitingExecutors.isEmpty()) {
            println("WAITING:")
            pool.waitingExecutors.forEach {
                println(" - ${it.task.name}")
            }
        }
        if (!pool.failedExecutors.isEmpty()) {
            println("FAILED:")
            pool.failedExecutors.forEach {
                println(" - ${it.task.name}")
                it.failedReason!!.printStackTrace()
            }
        }
    }
}