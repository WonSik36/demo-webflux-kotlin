package me.wonsik.reactor

import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.util.concurrent.atomic.AtomicLong


class Practice1 {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val numberFromFiveToSeven = Flux.range(5, 3)
            val numberFromFiveToSevenWithError = numberFromFiveToSeven
                    .handle<Int> { it, sink ->
                        if (it == 7) {
                            sink.error(RuntimeException("Something wrong"))
                            return@handle
                        }

                        sink.next(it)
                    }

            numberFromFiveToSeven.subscribe(
                    { println(it) },
                    { println(it.message) },
                    { println("complete") }
            )

            numberFromFiveToSevenWithError.subscribe(
                    {
                        println(it)
                        Thread.sleep(1000)
                    },
                    { println(it.message) },
                    { println("complete") },
                    { it.request(10) }
            )

            Flux.generate({ AtomicLong() },
                    { state: AtomicLong, sink: SynchronousSink<String?> ->
                        val i = state.getAndIncrement()
                        sink.next("3 x " + i + " = " + 3 * i)
                        if (i == 10L) sink.complete()
                        state
                    }) { state: AtomicLong -> println("state: $state") }
                    .subscribe { println(it) }
        }
    }
}

class Practice2 {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Flux.just(1, 2, 0)
                .map { i -> "100 / " + i + " = " + (100 / i) }
                .onErrorResume { th -> Flux.just(3, 4, 0).map { "100 / $it = ${100 / it}" } }
                .doOnError { println(it) }
                .subscribe { println(it) }
        }
    }
}