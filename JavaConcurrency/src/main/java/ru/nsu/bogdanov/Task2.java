package ru.nsu.bogdanov;

public class Task2 {

    public static void main(String[] args) {

        final int LOOP_TASK2_ITERATOR = 10;

        // Описание задачи для новой нити
        Runnable task = () -> {
            for (int i = 0; i <  LOOP_TASK2_ITERATOR; ++i) {
                System.out.println("Элемент цикла номер :" + i + " от новой нити");
            }
        };

        // Создание новой нити с указанной задачей
        Thread thread = new Thread(task);

        // Запуск новой нити
        thread.start();

        // Ожидание завершения новой нити перед продолжением главной нити
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Для больших проектов лучше использоваться логирования для отлавливания таких ошибок.
            // Сам IntelliJ об этом предупреждает
            e.printStackTrace();
        }

        System.out.println("Новая нить закончила");

        // Вывод строки из главной нити (потока)
        for (int i = 0; i <  LOOP_TASK2_ITERATOR; ++i) {
            System.out.println("Элемент цикла номер :" + i + " от главной нити");
        }
    }
}
