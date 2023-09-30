package ru.nsu.bogdanov.task7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Класс менеджера вычислений, предназначен для параллельного вычисления числа π с использованием ряда Лейбница.
 */
public class CalculationManager {
    private final CyclicBarrier barrier;
    private final int threadsNumber;
    private final int partialSumLength;
    private volatile double pi = 0.0;

    /**
     * Конструктор класса CalculationManager.
     *
     * @param threadsNumber    Количество потоков для параллельного вычисления.
     * @param partialSumLength Количество итераций для каждого потока.
     */
    public CalculationManager(int threadsNumber, int partialSumLength) {
        this.threadsNumber = threadsNumber;
        this.partialSumLength = partialSumLength;

        // Создаем CyclicBarrier, который будет ждать завершения всех потоков,
        // а также учитывает главный поток, поэтому +1.
        this.barrier = new CyclicBarrier(threadsNumber + 1);
    }

    /**
     * Метод для получения вычисленного значения числа π.
     *
     * @return Приближенное значение числа π.
     */
    public double getResult() {
        return pi;
    }

    /**
     * Инициализирует и выполняет вычисления приближенного значения числа π.
     * Для этого создает потоки, запускает их на выполнение, а затем ожидает их завершения
     * и суммирует результаты.
     */
    public void execute() {
        List<PartialSumTask> tasks = new ArrayList<>();

        // Создаем потоки для вычисления частичных сумм ряда Лейбница.
        for (int i = 0; i < threadsNumber; ++i) {
            tasks.add(new PartialSumTask(barrier, i * partialSumLength, partialSumLength));
            new Thread(tasks.get(i)).start();
        }

        // Ожидаем завершения всех потоков перед продолжением выполнения.
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Собираем результаты из всех потоков и вычисляем общее приближенное значение числа π.
        for (PartialSumTask task : tasks) {
            pi += 4.0 * task.getResult();
        }
    }
}
