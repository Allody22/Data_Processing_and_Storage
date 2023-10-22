package ru.nsu.mbogdanov.task8;

import java.util.Scanner;
import java.util.concurrent.Phaser;

public class Main {

    // Флаг для контроля выполнения потоков. Пока он true, потоки продолжают работать.
    public static volatile boolean keepRunning = true;

    // Отслеживаем максимальную фаза у потоков
    public static volatile int maxPhase = 0;

    public static void main(String[] args) {

        // Создаем объект Scanner для чтения ввода пользователя.
        Scanner inputScanner = new Scanner(System.in);
        // Просим пользователя указать количество потоков.
        System.out.println("Пожалуйста, введите кол-во потоков:");

        int numberOfThreads;
        try {
            // Считываем число потоков, которое ввел пользователь.
            numberOfThreads = inputScanner.nextInt();
        } catch (Exception e) {
            // Если пользователь вводит не корректное значение, выводим ошибку.
            System.out.println("Вы как-то плохо ввели количество потоков.");
            return;
        }

        // Ограничиваем количество потоков до количества доступных процессорных ядер.
        numberOfThreads = Math.min(numberOfThreads, Runtime.getRuntime().availableProcessors());

        inputScanner.close();

        Phaser synchronizationPhaser = new Phaser() {
            protected boolean onAdvance(int phase, int parties) {
                //Хотим узнать какая фаза максимальная
                return phase >= maxPhase && !keepRunning;
            }
        };

        // Создаем задачу для расчета числа Pi.
        CalculationManager task = new CalculationManager(synchronizationPhaser, numberOfThreads, 100000);

        // Добавляем хук на завершение (это поток, который запустится при завершении ЖВМ) работы приложения.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronizationPhaser.register();
            keepRunning = false;
            do {
                synchronizationPhaser.arriveAndAwaitAdvance();
            } while (!synchronizationPhaser.isTerminated());

            // Выводим стандартное значение числа Pi и рассчитанное.
            System.out.println("Library   pi = " + Math.PI);
            System.out.println("Estimated pi = " + task.getApproximatedPi());
        }));

        // Запускаем задачу на выполнение.
        task.execute();

    }
}
