package ru.nsu.bogdanov.task8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class CalculationManager {
    private final CyclicBarrier barrier;
    private final int threadsNumber;
    private final int partialSumLength;
    private volatile double pi = 0.0;
    private AtomicBoolean shutdownRequested = new AtomicBoolean(false); // Флаг для запроса завершения потоков

    public CalculationManager(int threadsNumber, int partialSumLength) {
        this.threadsNumber = threadsNumber;
        this.partialSumLength = partialSumLength;
        this.barrier = new CyclicBarrier(threadsNumber + 1);
    }

    public double getResult() {
        return pi;
    }

    public void execute() {
        List<PartialSumTask> tasks = new ArrayList<>();

        for (int i = 0; i < threadsNumber; ++i) {
            tasks.add(new PartialSumTask(barrier, i * partialSumLength, partialSumLength, shutdownRequested));
            new Thread(tasks.get(i)).start();
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        for (PartialSumTask task : tasks) {
            pi += 4.0 * task.getResult();
        }
    }

    public void shutdown() {
        shutdownRequested.set(true); // Устанавливаем флаг запроса завершения
    }
}