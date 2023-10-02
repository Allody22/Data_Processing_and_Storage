package ru.nsu.bogdanov;

public class Task4 {

    public static void main(String[] args) {
        // Задача для дочерней нити
        Runnable runnableThread = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Я не главная нить и я печатаю");
            }
        };

        // Запуск задачи в новой нити
        Thread childThread = new Thread(runnableThread);
        childThread.start();

        // Главная нить ждет 2 секунды перед тем как прервать дочернюю нить
        try {
            Thread.sleep(2000);
        } catch (InterruptedException exception) {
            // catch блок активируется в случае, если текущая нить (главная нить)
            // будет прервана другой нитью во время своего сна (вызова Thread.sleep)
            System.out.println("Main thread was interrupted unexpectedly.");
        }

        // Прерываем дочернюю нить
        childThread.interrupt();
    }
}
