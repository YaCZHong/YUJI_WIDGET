### 关于协程的一些记录

* 关于launch和async：
  1、launch和async协程中未捕获的异常会立即向上传播到作业层次结构；
  2、如果顶级协程是在launch启动的，则异常由 CoroutineExceptionHandler 处理或传递给线程的未捕获异常处理程序。如果顶级协程是用async启动的，则异常被封装在 Deferred 返回类型中，并在调用 .await() 时重新抛出。



## 常见的协程异常处理



### try-catch相关

#### 1、try-catch处理协程内部异常

try-catch经常用来处理协程的内部异常，将异常扼杀在摇篮里，所以异常不会向上传播到作业层次结构中。

```
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            println("下面将会抛出异常，但是可以通过try-catch捕获到")
            throw IllegalArgumentException()
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}
```

**（注：try-catch并不一定能捕获到异常，如下面这种情况）**

```
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            launch {
                println("下面将会抛出异常，但是不能通过try-catch捕获到")
                throw IllegalArgumentException()
            }
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}
```

这是因为try-catch内部的launch抛出异常时，将沿着作业层次结构传播，不会向协程的外部抛出异常。所以，try-catch能够捕获到异常的条件是：异常所在的协程与try-catch一样，并且被try-catch包裹，才能被try-catch捕获。（如下所示，抛出的异常将会被捕获到，即使通过launch新创建了一个协程，但是异常满足所在协程与try-catch一致，并被try-catch包裹的条件。）

```
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            launch {
                delay(2000)
                print("嘀嘀嘀")
            }
            println("下面将会抛出异常，但是可以通过try-catch捕获到")
            throw IllegalArgumentException()
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}
```



#### 2、try-catch处理coroutineScope与supervisorScope抛出的异常

- coroutineScope

对于coroutineScope来说，它自身的作业类型是Job类型，所以下面代码①中子协程launch抛出的异常会沿着作业层次结构传递到它这里，它自身又会将异常重新抛出，所以try-catch可以捕获到异常。当然，如果是coroutineScope自身的异常，同样也可以捕获到，如代码②。

```
代码①
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            println("下面将会抛出异常，但是可以通过try-catch捕获到")
            coroutineScope {
                launch {
                    throw IllegalArgumentException()
                }
            }
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}

代码②
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            println("下面将会抛出异常，但是可以通过try-catch捕获到")
            coroutineScope {
                throw IllegalArgumentException()
            }
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}
```

- supervisorScope

对于supervisorScope来说，它的作业类型是SupervisorJob类型，子协程launch中抛出的异常不会通过作业层次结构传递给它，所以try-catch无法捕获到该异常。但是，如果是supervisorScope自身的异常，同样也可以捕获到，如代码②。

```
代码①
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            println("下面将会抛出异常，但是不能通过try-catch捕获到")
            supervisorScope {
                launch {
                    throw IllegalArgumentException()
                }
            }
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}

代码②
fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            println("下面将会抛出异常，但是可以通过try-catch捕获到")
            supervisorScope {
                throw IllegalArgumentException()
            }
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}
```



#### 3、try-catch处理.await()抛出的异常

我们知道，通过async创建的协程会返回一个deferred对象，我们可以调用.await()方法来获取协程的执行结果。但是如果在async协程内部抛出异常时，在特定条件下，是与launch不一样的处理方式的。如下代码①所示，代码运行是不会报错的，而代码②则会报错。

```
代码①
fun main() {
    val scope = CoroutineScope(Job())

    scope.async {
        throw IllegalArgumentException()
    }

    Thread.sleep(100000)
}

代码②
fun main() {
    val scope = CoroutineScope(Job())

    scope.launch {
        throw IllegalArgumentException()
    }

    Thread.sleep(100000)
}
```

虽然在上面的例子中launch与async抛出异常时，异常都会向上传递到根作用域，从而取消父作业和父作业下的子作业，但是launch产生的异常最终会由父作业的CoroutineExceptionHandler来进行处理，如果没有CoroutineExceptionHandler，则会直接报错；而async则相反，协程内部抛出的异常不会由CoroutineExceptionHandler来进行处理，而是将异常封装在deferred对象当中，并在调用.await()时重新将异常抛出。所以要捕获到async抛出的异常，通常如下处理：

```
fun main() {
    val scope = CoroutineScope(Job())

    val deferred = scope.async {
        delay(1000)//加上延时是为了抛出异常晚一点，不然下面的协程压根就不会运行，不理解请重读上面的描述
        throw IllegalArgumentException()
    }
    
    scope.launch {
        try {
            println("下面将会抛出异常，但是可以通过try-catch捕获到")
            deferred.await()
        } catch (e: Exception) {
            println("捕获到异常：$e")
        }
    }

    Thread.sleep(100000)
}
```

但是，出现如上面所示的区别是有条件的，就是async必须是顶级协程，如果不是顶级协程，则跟launch是一样的情况。比如下面的代码，则会直接抛出异常

```
fun main() {
    val scope = CoroutineScope(Job())

    scope.launch {
        async {
            throw IllegalArgumentException()
        }
    }

    Thread.sleep(100000)
}
```

如果给根作用域加上CoroutineExceptionHandler，则异常最终会由CoroutineExceptionHandler处理

```
fun main() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }

    val scope = CoroutineScope(Job() + handler)

    scope.launch {
        async {
            throw IllegalArgumentException()
        }
    }

    Thread.sleep(100000)
}
```

##### 拓展小知识

猜一猜下面的代码的运行结果？

```
fun main() {

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }

    val scope = CoroutineScope(Job() + handler)

    scope.launch {
        supervisorScope {
            async {
                throw IllegalArgumentException()
            }
        }
    }

    Thread.sleep(100000)
}
```

是不是以为会由CoroutineExceptionHandler处理，打印出日志？

实际上，运行之后，啥也没打印。

再试试这段代码

```
fun main() {

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }

    val scope = CoroutineScope(Job() + handler)

    scope.launch {
        supervisorScope {
            val deferred = async {
                throw IllegalArgumentException()
            }

            try {
                println("下面将会抛出异常，但是可以通过try-catch捕获到")
                deferred.await()
            } catch (e: Exception) {
                println("捕获到异常：$e")
            }
        }
    }

    Thread.sleep(100000)
}
```

我们可以看到，结果是跟顶级协程一样。而如果你将上述代码中的supervisorScope替换成coroutineScope，结果又会不一样。对此，我的理解是：

- 没有父协程的协程就是顶级协程；
- 有父级协程，但该父级协程的Job类型为SupervisorJob，则该协程为可以等同于顶级协程。

而上述中supervisorScope自身协程的Job类型为SupervisorJob类型，而coroutineScope自身协程的Job类型为普通Job类型。



### CoroutineExceptionHandler相关

引用官网的一段话

`CoroutineExceptionHandler` is a last-resort mechanism for global "catch all" behavior. You cannot recover from the exception in the `CoroutineExceptionHandler`. The coroutine had already completed with the corresponding exception when the handler is called. Normally, the handler is used to log the exception, show some kind of error message, terminate, and/or restart the application.

If you need to handle exception in a specific part of the code, it is recommended to use `try`/`catch` around the corresponding code inside your coroutine. This way you can prevent completion of the coroutine with the exception (exception is now caught), retry the operation, and/or take other arbitrary actions。

有道翻译过来大概为：CoroutineExceptionHandler是一种不得已的全局捕获机制。在CoroutineExceptionHandler的异常处理程序中，是无法从异常中恢复的，因为当调用CoroutineExceptionHandler的异常处理程序时，协程也伴随相应的异常而结束。通常，CoroutineExceptionHandler用于输出异常日志，显示某错误消息，终止或重新启动应用程序。

如果您需要在代码的特定部分处理异常，建议在协程内部使用try-catch包裹相应代码。这样，您可以避免协程的异常终止，进行重试操作或采取其他措施。



#### CoroutineExceptionHandler必须在协程作用域或顶级协程中使用

在协程作用域中使用，如代码①；在顶级协程中使用，如代码②。

```
代码①
fun main() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }
    val scope = CoroutineScope(Job() + handler)
    
    scope.launch {
        throw IllegalArgumentException()
    }

    Thread.sleep(100000)
}

代码②
fun main() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }
    val scope = CoroutineScope(Job())

    scope.launch(handler) {
        throw IllegalArgumentException()
    }

    Thread.sleep(100000)
}
```

除了以上两个地方，在其他地方使用是不生效的。如以下代码，仍会抛异常报错

```
fun main() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }
    val scope = CoroutineScope(Job())

    scope.launch {
        launch(handler) {
            throw IllegalArgumentException()
        }
    }

    Thread.sleep(100000)
}
```

##### 小提示：

以下代码，CoroutineExceptionHandler起作用了吗？运行以下看看吧，如果答错，就再好好看一下本篇内容吧

```
fun main() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("$coroutineContext==>$throwable")
    }
    val scope = CoroutineScope(Job())

    scope.launch {
        supervisorScope {
            launch(handler) {
                throw IllegalArgumentException()
            }
        }
    }

    Thread.sleep(100000)
}
```



### try-catch与CoroutineExceptionHandler使用场景的区别

假设我们并行启动两个协程，他们俩都相互依赖，其中一个失败了，那么另一个的完成也就没有意义了。如果我们在每个协程中使用`try-catch`来处理异常，则异常不会传播到父级，因此其他协程也不会被取消。而这将造成资源的浪费。在这种情况下，我们应该使用`CoroutineExceptionHandler`。相反，如果两个协程不需要相互依赖，类似请求两个不同的接口来填充两个互不关联的布局，这时，因为其中一个失败了而取消另一个，就不应该了，这种情况就需要使用try-catch在协程内部来处理相关异常。



参考文章：https://www.lukaslechner.com/why-exception-handling-with-kotlin-coroutines-is-so-hard-and-how-to-successfully-master-it/
