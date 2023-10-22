package ru.nsu.mbogdanov.task13;

/**
 * Класс, представляющий философа, который участвует в обеде.
 */
public class Philosopher extends Thread {
    private final int id;

    private final int leftFork;
    private final int rightFork;
    private final Forks forks;

    /**
     * Создает экземпляр философа.
     *
     * @param id        уникальный идентификатор философа.
     * @param leftFork  индекс левой вилки.
     * @param rightFork индекс правой вилки.
     * @param forks     экземпляр класса Forks, представляющий все вилки на столе.
     */
    Philosopher(int id, int leftFork, int rightFork, Forks forks) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.forks = forks;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                if (!forks.takeForks(leftFork, rightFork)) {
                    // Если вилки не взяты, просто продолжаем размышлять
                    continue;
                }
                eat();
                forks.releaseForks(leftFork, rightFork);
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
