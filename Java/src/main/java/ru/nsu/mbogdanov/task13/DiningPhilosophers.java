package ru.nsu.mbogdanov.task13;


/**
 * Класс, создающий общие условия для задачи и её запуска.
 */
public class DiningPhilosophers {

    private static final int PHILOSOPHERS_COUNT = 5;
    private final Forks forks = new Forks(PHILOSOPHERS_COUNT);
    private final Philosopher[] philosophers = new Philosopher[PHILOSOPHERS_COUNT];


    /**
     * Запуск симуляции обедающих философов.
     */
    public void startSimulation() {
        for (int i = 0; i < PHILOSOPHERS_COUNT; i++) {
            int leftFork = i;
            int rightFork = (i + 1) % PHILOSOPHERS_COUNT;

            if (i == PHILOSOPHERS_COUNT - 1) {
                philosophers[i] = new Philosopher(i, rightFork, leftFork, forks); // Последний философ берет сначала правую вилку
            } else {
                philosophers[i] = new Philosopher(i, leftFork, rightFork, forks);
            }
            philosophers[i].start();
        }
    }
}
