package ru.nsu.mbogdanov.task8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * Тут мы управляем процессом параллельного вычисления числа ПИ.
 */
public class CalculationManager implements Runnable {

    // Фазер для синхронизации потоков.
    private final Phaser synchronizationPhaser;

    // Список задач для вычисления частичных сумм.
    private final List<PartSumTask> partialSumTasks;

    // Переменная для хранения промежуточного результата вычисления π.
    private volatile double piValue = 0.0;

    /**
     * Конструктор класса.
     *
     * @param phaser            Фазер для синхронизации.
     * @param threadCount       Количество потоков.
     * @param elementsPerThread Количество элементов, которое каждый поток должен обработать.
     */
    public CalculationManager(Phaser phaser, long threadCount, long elementsPerThread) {
        this.synchronizationPhaser = phaser;
        this.partialSumTasks = new ArrayList<>();
        // Создаем задачи для каждого потока
        for (int i = 0; i < threadCount; ++i) {
            partialSumTasks.add(new PartSumTask(phaser, i * elementsPerThread, elementsPerThread, threadCount * elementsPerThread));
        }
    }

    /**
     * Получить приближенное значение π.
     *
     * @return Значение числа π.
     */
    public double getApproximatedPi() {
        piValue = 0.0;
        // Суммируем результаты всех задач.
        for (PartSumTask task : partialSumTasks) {
            piValue += 4.0 * task.getPartialSum();
        }
        return piValue;
    }

    @Override
    public void run() {
        // Регистрируем основной поток в фазере.
        synchronizationPhaser.register();
        // Запускаем все задачи в отдельных потоках.
        partialSumTasks.forEach(task -> new Thread(task).start());
        // Ожидаем завершения всех потоков.
        do {
            synchronizationPhaser.arriveAndAwaitAdvance();
        } while (!synchronizationPhaser.isTerminated());
    }

    /**
     * Запуск процесса вычисления.
     */
    public void execute() {
        run();
    }
}
