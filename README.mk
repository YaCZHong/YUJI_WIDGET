### 关于协程的一些记录

* 关于顶级协程的理解：
  1、没有父协程的协程就是顶级协程；
  2、有父级协程，但该父级协程的Job类型为SupervisorJob，则该协程为顶级协程。

* 关于launch和async：
  1、launch和async协程中未捕获的异常会立即向上传播到作业层次结构；
  2、如果顶级协程是在launch启动的，则异常由 CoroutineExceptionHandler 处理或传递给线程的未捕获异常处理程序。如果顶级协程是用async启动的，则异常被封装在 Deferred 返回类型中，并在调用 .await() 时重新抛出。
