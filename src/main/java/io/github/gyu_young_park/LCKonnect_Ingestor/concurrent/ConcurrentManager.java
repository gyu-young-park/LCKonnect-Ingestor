package io.github.gyu_young_park.LCKonnect_Ingestor.concurrent;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1.LCKLeagueCrawler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentManager<T> {
    final private Logger LOGGER = LoggerFactory.getLogger(ConcurrentManager.class);
    final private ExecutorService threadPool;
    private List<SubmittedTask<T>> submittedTasks;

    public ConcurrentManager(int nThread) {
        threadPool = Executors.newFixedThreadPool(nThread);
    }

    public void submitTask(Task<T> task) {
        Future<T> future = threadPool.submit(task.callable);
        submittedTasks.add(new SubmittedTask<>(task, future));
    }

    public List<T> executeWithException() throws ExecutionException, InterruptedException, TimeoutException {
        List<T> results = new ArrayList<>();
        for (SubmittedTask<T> submittedTask: submittedTasks) {
            LOGGER.debug("Starts task[" + submittedTask.task.taskName + "]");
            results.add(submittedTask.future.get(submittedTask.task.timeout, submittedTask.task.timeUnit));
        }
        return results;
    }

    @AllArgsConstructor
    private static class SubmittedTask<T> {
        public Task<T> task;
        public Future<T> future;
    }

}
