package ru.nsu.mbogdanov.task11;

/**
 * Главный где мы приказываем нитям печатать.
 */
public class Main {

    public static void main(String[] args) {
        // Количество итераций для печати по условию
        final int LOOP_ITERATOR = 10;

        // Создание экземпляра класса для синхронизированной печати
        SyncPrinter printer = new SyncPrinter();

        // Задача для дочернего потока
        Runnable task = () -> {
            for (int i = 0; i < LOOP_ITERATOR; i++) {
                printer.printFromChild(i);
            }
        };

        // Запуск дочернего потока
        Thread thread = new Thread(task);
        thread.start();

        // Печать главным потоком
        for (int i = 0; i < LOOP_ITERATOR; i++) {
            printer.printFromMain(i);
        }
    }
}