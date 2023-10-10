package ru.nsu.mbogdanov.task9;

/**
 * Сущность для одного философа.
 */
public class Philosopher extends Thread {

    private final int id;

    private final Fork leftFork;

    private final Fork rightFork;

    /**
     * Конструктор для создания философа с указанным айди и вилками.
     *
     * @param id        Идентификатор философа.
     * @param leftFork  Левая вилка.
     * @param rightFork Правая вилка.
     */
    public Philosopher(int id, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    /**
     * Метод, который выполняется при запуске потока философа.
     * По условию наша задача не кончается, поэтому тут бесконечный while
     */
    @Override
    public void run() {
        try {
            while (true) {
                think();
                // Захват левой вилки
                synchronized (leftFork) {
                    System.out.println("Философ с айди: " + id + " поднял левую вилку с айди: " + leftFork.getId());
                    // Захват правой вилки
                    synchronized (rightFork) {
                        System.out.println("Философ с айди: " + id + " поднял правку вилку с айди: " + rightFork.getId());
                        eat();
                        System.out.println("Философ с айди: " + id + " покушал и опустил правку вилку с айди: " + rightFork.getId());
                    }
                    System.out.println("Философ с айди: " + id + " опустил левую вилку с айди: " + leftFork.getId());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Размышление философа.
     *
     * @throws InterruptedException если поток прерван во время размышления.
     */
    private void think() throws InterruptedException {
        System.out.println("Философ с айди: " + id + " думает");
        Thread.sleep((long) (Math.random() * 1000));
    }

    /**
     * Тот самый обед философа.
     *
     * @throws InterruptedException если поток прерван во время еды.
     */
    private void eat() throws InterruptedException {
        System.out.println("Философ с айди: " + id + " кушает");
        Thread.sleep((long) (Math.random() * 1000));
    }
}
