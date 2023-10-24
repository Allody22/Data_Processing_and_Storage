package ru.nsu.mbogdanov.task9;

/**
 * Класс, создающий общие условия для задачи и её запуска.
 */
public class DiningPhilosophers {

    //Сколько по условию задачи философов
    private static final int PHILOSOPHERS_COUNT = 5;

    //Количество вилок для левой или правой руки
    private final Fork[] forks = new Fork[PHILOSOPHERS_COUNT];

    //Сама сущность философа
    private final Philosopher[] philosophers = new Philosopher[PHILOSOPHERS_COUNT];


    /**
     * Запуск симуляции обедающих философов.
     */
    public void startSimulation() {
        // Инициализация вилок
        for (int i = 0; i < PHILOSOPHERS_COUNT; i++) {
            forks[i] = new Fork(i);
        }

        // Создание и запуск потоков философов
        for (int i = 0; i < PHILOSOPHERS_COUNT; i++) {
            Fork leftFork = forks[i]; // Философ всегда начинает с левой вилки.
            Fork rightFork = forks[(i + 1) % PHILOSOPHERS_COUNT];
            if (i == PHILOSOPHERS_COUNT - 1) {
                philosophers[i] = new Philosopher(i, rightFork, leftFork); // Последний философ берет сначала правую вилку
            } else {
                philosophers[i] = new Philosopher(i, leftFork, rightFork);
            }
            philosophers[i].start();
        }
    }
}
