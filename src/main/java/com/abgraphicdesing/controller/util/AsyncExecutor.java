package com.abgraphicdesing.controller.util;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javafx.concurrent.Task;

public class AsyncExecutor {
  public static <T> void execute(
    Callable<T> action,
    Consumer<T> onSuccess,
    Consumer<Throwable> onError
  ){
    Task<T> task = new Task<>() {
      @Override
      protected T call() throws Exception{
        return  action.call();
      } 
    };

    task.setOnSucceeded(e -> 
      onSuccess.accept(task.getValue())
    );

    task.setOnFailed(e ->
      onError.accept(task.getException())
    );

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
  }
}
