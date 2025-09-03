package io.github.gyu_young_park.LCKonnect_Ingestor.concurrent;

import io.github.gyu_young_park.LCKonnect_Ingestor.crawler.v1.LCKLeagueCrawler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentManager<T> implements AutoCloseable {
    final private Logger LOGGER = LoggerFactory.getLogger(ConcurrentManager.class);
    final private ExecutorService threadPool;
    final private String concurrentName;
    private List<SubmittedTask<T>> submittedTasks = new ArrayList<>();

    public ConcurrentManager(String concurrentName, int nThread) {
        this.concurrentName = concurrentName;
        this.threadPool = Executors.newFixedThreadPool(nThread);
    }

    public void submitTask(Task<T> task) {
        Future<T> future = threadPool.submit(task.callable);
        submittedTasks.add(new SubmittedTask<>(task, future));
    }

    public List<T> execute() throws ExecutionException, InterruptedException, TimeoutException {
        List<T> results = new ArrayList<>();
        try {
            for (SubmittedTask<T> submittedTask: submittedTasks) {
                LOGGER.debug("[{}]Starts task[{}]", this.concurrentName, submittedTask.task.taskName);
                results.add(submittedTask.future.get(submittedTask.task.timeout, submittedTask.task.timeUnit));
            }
        } finally {
            submittedTasks.clear();
        }
        return results;
    }

    @Override
    public void close() {
        gracefullyShutdown();
    }

    private void gracefullyShutdown() {
        threadPool.shutdown();
        try {
            if(!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    LOGGER.error("[{}]Failed to shutdown concurrent manager", this.concurrentName);
                }
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            LOGGER.debug("[{}] got interrupted", this.concurrentName);
        }
    }

    @AllArgsConstructor
    private static class SubmittedTask<T> {
        public Task<T> task;
        public Future<T> future;
    }

}
