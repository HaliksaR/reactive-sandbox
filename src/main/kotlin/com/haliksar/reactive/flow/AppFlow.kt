package com.haliksar.reactive.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking



class AppFlow : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    companion object {
        fun start() {
            AppFlow().start()
        }
    }

    private fun generate(count: Int): List<Int> =
        mutableListOf<Int>().apply {
            repeat(count) { add((0..Int.MAX_VALUE).random()) }
        }

    private fun start() = runBlocking {
        val flow = flow {
            generate(20).forEachIndexed { index, item ->
                emit(index)
                delay(10)
            }
        }

        val flow2 = flow {
            generate(10).forEachIndexed { index, item ->
                emit(index.toString())
                delay(10)
            }
        }

        flow.combine(flow2) { a, b -> "$a + $b" }
            .onEach {
                println(Thread.currentThread().toString())
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                println(Thread.currentThread().toString())
                println(it)
            }.launchIn(this)
    }
}