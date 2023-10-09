package ru.nsu.mbogdanov.task8;

import java.util.concurrent.Phaser;

/**
 * Класс для многопоточного вычисления частичной суммы ряда Лейбница
 */
public class PartSumTask implements Runnable {
    // Фазер для синхронизации потоков
    private final Phaser synchronizationPhaser;

    // Начальное значение диапазона
    private final long start;

    // Количество элементов в диапазоне
    private final long count;

    // Общий шаг между элементами
    private final long overallStep;

    // Результат частичного суммирования
    private double partialSumValue = 0;

    /**
     * Конструктор класса
     *
     * @param phaser Фазер для синхронизации.
     * @param start Начальное значение диапазона.
     * @param count Количество элементов в диапазоне.
     * @param overallStep Общий шаг между элементами.
     */
    public PartSumTask(Phaser phaser, long start, long count, long overallStep) {
        this.synchronizationPhaser = phaser;
        this.start = start;
        this.count = count;
        this.overallStep = overallStep;

        // Регистрируем текущий поток в фейзере
        synchronizationPhaser.register();
    }

    /**
     * Возвращает части суммы ряда Лейбница для данного потока.
     *
     * @return Части суммы.
     */
    public double getPartialSum() {
        return partialSumValue;
    }

    @Override
    public void run() {
        // Вычисляем частичную сумму, пока все потоки не завершат работу
        do {
            // Определяем текущий шаг на основе текущей фазы
            long currentStep = this.overallStep * synchronizationPhaser.getPhase();
            // Вычисляем частичную сумму для текущего диапазона
            for (long i = this.start + currentStep; i < this.start + count + currentStep; ++i) {
                double currentTerm = 1.0 / (2.0 * i + 1.0);
                partialSumValue += i % 2 == 0 ? currentTerm : -currentTerm;
            }
            // Уведомляем фазер о завершении текущей итерации
            synchronizationPhaser.arriveAndAwaitAdvance();
        } while (!synchronizationPhaser.isTerminated());
    }
}
