package io.github.gyu_young_park.LCKonnect_Ingestor.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Task <T>{
    public String taskName;
    public int timeout;
    public TimeUnit timeUnit;
    public Callable<T> callable;

    public Task(String taskName, Callable<T> callable) {
        this.callable = callable;
        this.taskName = taskName;
        this.timeout = 10;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public Task(String taskName, Callable<T> callable,int timeout, TimeUnit timeUnit) {
        this(taskName, callable);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }
}
