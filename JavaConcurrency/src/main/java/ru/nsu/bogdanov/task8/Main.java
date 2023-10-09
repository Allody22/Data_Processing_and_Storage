package ru.nsu.bogdanov.task8;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество потоков:");

        int threadsNumber;
        try {
            threadsNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Ошибка ввода! Пожалуйста, введите корректное число потоков.");
            return;
        }

        threadsNumber = Math.min(threadsNumber, Runtime.getRuntime().availableProcessors());

        CalculationManager task = new CalculationManager(threadsNumber, 100000);
        task.execute();

        DecimalFormat df = new DecimalFormat("#.#############");

        // Создаем обработчик сигнала SIGINT (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            task.shutdown(); // Запрос на завершение потоков при получении сигнала SIGINT
            try {
                Thread.sleep(1000); // Добавляем небольшую задержку для завершения потоков
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Final calculated pi = " + df.format(task.getResult()));
        }));

        while (true) {
            // Вход в бесконечный цикл, ожидающий сигнал SIGINT
        }
    }
}