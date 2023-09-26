package ru.nsu.bogdanov;

public class Task1 {

    public static void main(String[] args) {

        final int LOOP_TASK1_ITERATOR = 10;

        // Описание задачи для новой нити
        Runnable task = () -> {
            for (int i = 0; i < LOOP_TASK1_ITERATOR; ++i) {
                System.out.println("Элемент цикла номер :" + i + " от новой нити");
            }
        };

        // Создание новой нити с указанной задачей
        Thread thread = new Thread(task);

        // Запуск новой нити
        thread.start();

        // Вывод строки из главной нити (потока)
        for (int i = 0; i < LOOP_TASK1_ITERATOR; ++i) {
            System.out.println("Элемент цикла номер :" + i + " от главной нити");
        }
    }
}
