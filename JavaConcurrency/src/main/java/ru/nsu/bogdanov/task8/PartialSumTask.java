package ru.nsu.bogdanov.task8;

import java.text.DecimalFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class PartialSumTask implements Runnable {
    private final CyclicBarrier barrier;
    private final int from;
    private final int length;
    private final AtomicBoolean shutdownRequested; // Флаг для запроса завершения потоков
    private volatile double partialSum = 0;

    public PartialSumTask(CyclicBarrier barrier, int from, int length, AtomicBoolean shutdownRequested) {
        this.barrier = barrier;
        this.from = from;
        this.length = length;
        this.shutdownRequested = shutdownRequested;
    }

    public double getResult() {
        return partialSum;
    }

    @Override
    public void run() {
        for (int i = this.from; i < this.from + this.length; ++i) {
            // Проверяем флаг запроса завершения перед каждой итерацией
            if (shutdownRequested.get()) {
                return;
            }

            double summand = 1.0 / (2.0 * i + 1.0);
            partialSum += (i % 2 != 0) ? (-1.0) * summand : summand;
        }

        DecimalFormat df = new DecimalFormat("#.#############");
        System.out.println(Thread.currentThread().getName() + ": Current partial sum = " + df.format(partialSum));

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}