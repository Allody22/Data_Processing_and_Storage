package ru.nsu;

public class Task1 {

    public static void main(String[] args) {
        // Описание задачи для новой нити
        Runnable task = () -> {
            for (int i = 0; i < 10; ++i) {
                System.out.println("Элемент цикла номер :" + i + " от новой нити");
            }
        };

        // Создание новой нити с указанной задачей
        Thread thread = new Thread(task);

        // Запуск новой нити
        thread.start();

        // Вывод строки из главной нити (потока)
        for (int i = 0; i < 10; ++i) {
            System.out.println("Элемент цикла номер :" + i + " от главной нити");
        }
    }
}
