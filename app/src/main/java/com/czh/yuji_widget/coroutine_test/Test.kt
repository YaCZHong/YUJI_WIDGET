package com.czh.yuji_widget.coroutine_test

import kotlinx.coroutines.*

fun main() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }
    val scope = CoroutineScope(Job())

    scope.launch {
        launch(SupervisorJob() + handler) {
            throw IllegalArgumentException()
        }
    }

    Thread.sleep(100000)
}





