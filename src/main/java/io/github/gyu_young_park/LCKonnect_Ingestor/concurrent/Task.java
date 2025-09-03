package io.github.gyu_young_park.LCKonnect_Ingestor.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Task <T>{
    public String taskName;
    public int timeout;
    public TimeUnit timeUnit;
    public Callable<T> callable;

    public Task(Callable<T> callable,String taskName) {
        this.callable = callable;
        this.taskName = taskName;
        this.timeout = 0;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public Task(Callable<T> callable, String taskName,int timeout, TimeUnit timeUnit) {
        this(callable, taskName);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }
}
