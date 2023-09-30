package ru.nsu.bogdanov.task7;

import java.text.DecimalFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Задача для вычисления части суммы ряда Лейбница для определенного диапазона значений.
 */
public class PartialSumTask implements Runnable {

    private final CyclicBarrier barrier;

    private final int from;

    private final int length;

    private volatile double partialSum = 0;

    /**
     * Конструктор задачи PartialSumTask.
     *
     * @param barrier Барьер для синхронизации потоков.
     * @param from    Начальное значение диапазона.
     * @param length  Длина диапазона.
     */
    public PartialSumTask(CyclicBarrier barrier, int from, int length) {
        this.barrier = barrier;
        this.from = from;
        this.length = length;
    }

    /**
     * Возвращает части суммы ряда Лейбница для данного потока.
     *
     * @return Части суммы.
     */
    public double getResult() {
        return partialSum;
    }

    /**
     * Выполняет вычисления для заданного диапазона значений.
     */
    @Override
    public void run() {
        // Цикл обрабатывает заданный диапазон значений для этого потока.
        for (int i = this.from; i < this.from + this.length; ++i) {
            // Вычисление отдельного члена ряда Лейбница.
            // Чем больше членов ряда мы учитываем, тем точнее будет наше приближение к π.
            double summand = 1.0 / (2.0 * i + 1.0);

            // Если i четное, мы добавляем член ряда, иначе вычитаем.
            // Это основывается на формуле ряда Лейбница: π/4 = 1 - 1/3 + 1/5 - 1/7 + 1/9 - ...
            partialSum += (i % 2 != 0) ? (-1.0) * summand : summand;
        }

        // Выводим сообщение о том, какой поток и какую сумму частичную посчитал.
        // Можно было бы писать о том, что поток просто досчитал, но чисто в научных целях хочется посмотреть конкретное число
        //Для вывода значений в нормальном формате используем специальный класс
        DecimalFormat df = new DecimalFormat("#.#############");
        //Каждый элемент ряда Лейбница всё меньше и меньше => чем больше поток, тем меньше цифра
        //Не забываем, что элементы ряда Лейбница еще надо будет умножить на 4, поэтому всё нормально со значениями
        System.out.println(Thread.currentThread().getName() + ": Current partial sum = " + df.format(partialSum));


        // Ожидание у барьера, пока все другие потоки не завершат свои вычисления.
        // Это гарантирует, что главный поток не продолжит выполнение, пока все потоки не закончат работу.
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}
