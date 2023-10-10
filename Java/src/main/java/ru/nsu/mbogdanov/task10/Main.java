package ru.nsu.mbogdanov.task10;

public class Main {

    public static void main(String[] args) {
        // По условию мы 10 раз печатаем от каждой нити строки
        final int LOOP_TASK1_ITERATOR = 10;
        SyncPrinter printer = new SyncPrinter();

        // Описание задачи для новой нити
        Runnable task = () -> {
            for (int i = 0; i < LOOP_TASK1_ITERATOR; i++) {
                printer.printFromChild(i);
            }
        };

        // Создание новой нити с указанной задачей
        Thread thread = new Thread(task);
        thread.start();

        // Вывод строки из главной нити (потока)
        for (int i = 0; i < LOOP_TASK1_ITERATOR; i++) {
            printer.printFromMain(i);
        }
    }
}
