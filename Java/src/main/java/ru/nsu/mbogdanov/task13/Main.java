package ru.nsu.mbogdanov.task13;

/**
 * Тут мы запускаем наших философов.
 */
public class Main {
    public static void main(String[] args) {
        // Создание и запуск симуляции
        DiningPhilosophers simulation = new DiningPhilosophers();
        simulation.startSimulation();
    }
}