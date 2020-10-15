package com.haliksar.reactive.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.FlowPreview

// https://www.codeflow.site/ru/article/rxjava-schedulers
class AppRx {
    companion object {
        fun start() {
            AppRx().start()
        }
    }

    private fun generate(count: Int): List<Int> =
        mutableListOf<Int>().apply {
            repeat(count) { add((0..Int.MAX_VALUE).random()) }
        }

    @FlowPreview
    private fun start() {
        val rx = Observable.create<Int> {
            generate(10).forEachIndexed { index, item ->
                it.onNext(index)
                Thread.sleep(20)
            }
        }

        val rx2 = Observable.create<String> {
            generate(20).forEachIndexed { index, item ->
                it.onNext(index.toString())
                Thread.sleep(10)
            }
        }

        Observable
            .zip(rx, rx2, { a, b -> "$a + $b" })
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.trampoline())
            .subscribe {
                println(it)
            }
    }
}