package com.haliksar.reactive.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

interface Generator {
    fun generate(count: Int): List<Int>
}

class GeneratorImpl : Generator {
    override fun generate(count: Int): List<Int> =
        mutableListOf<Int>().apply {
            repeat(count) { add((0..Int.MAX_VALUE).random()) }
        }
}

interface Flowable {
    val flows: MutableList<Flow<*>>
}

class FlowableImpl : Flowable {
    override val flows: MutableList<Flow<*>> = mutableListOf()
}

class AppFlow(
    context: CoroutineContext = Dispatchers.IO
) : CoroutineScope by CoroutineScope(context),
    Generator by GeneratorImpl(),
    Flowable by FlowableImpl() {

    private val setWorkerNames = mutableSetOf<String>()

    fun initStopDelay(delay: Long?, scope: CoroutineScope): AppFlow {
        delay?.let {
            scope.launch {
                delay(it)
                this@AppFlow.cancel()
                println(Thread.currentThread().toString())
                println(setWorkerNames.joinToString("\n"))
            }
        }
        return this
    }

    fun start(): Job =
        combine(*flows.toTypedArray()) { a ->
            val result = StringBuilder()
            a.forEach { result.append(it) }
            result.toString()
        }.onEach {
            setWorkerNames.add(Thread.currentThread().name)
            println(Thread.currentThread().toString())
        }.flowOn(Dispatchers.IO)
            .onEach {
                setWorkerNames.add(Thread.currentThread().name)
                println(Thread.currentThread().toString())
                println(it)
            }.launchIn(this)

}