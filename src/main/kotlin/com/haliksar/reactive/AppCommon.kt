package com.haliksar.reactive

import com.haliksar.reactive.flow.AppFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppFlow().initStopDelay(300, this).apply {
        flows.add(flow {
            generate(200).forEachIndexed { index, item ->
                emit(index)
                delay(10)
            }
        })

        flows.add(flow {
            generate(100).forEachIndexed { index, item ->
                emit(index.toString())
                delay(10)
            }
        })

        flows.add(flow {
            generate(100).forEachIndexed { index, item ->
                emit(index.toString())
                delay(10)
            }
        })

        flows.add(flow {
            generate(100).forEachIndexed { index, item ->
                emit(index.toString())
                delay(10)
            }
        })
    }.start().join()
}