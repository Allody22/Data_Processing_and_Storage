package ru.nsu.mbogdanov.task11;

import java.util.concurrent.Semaphore;

/**
 * Класс для синхронизированного вывода строк из двух потоков с использованием семафоров.
 */
class SyncPrinter {

    /**
     * Семафор для главного потока, инициализирован одним разрешением.
     */
    private final Semaphore mainSemaphore = new Semaphore(1);

    /**
     * Семафор для дочернего потока, инициализирован нулем разрешений.
     */
    private final Semaphore childSemaphore = new Semaphore(0);

    /**
     * Метод для печати строки главным потоком.
     *
     * @param index номер текущей строки.
     */
    public void printFromMain(int index) {
        try {
            // Главный поток пытается получить разрешение
            mainSemaphore.acquire();
            System.out.println(index + " от главной нити");

            // Разрешаем дочернему потоку продолжить
            childSemaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Метод для печати строки дочерним потоком.
     *
     * @param index номер текущей строки.
     */
    public void printFromChild(int index) {
        try {
            // Дочерний поток пытается получить разрешение
            childSemaphore.acquire();
            System.out.println(index + " от новой нити");

            // Разрешаем главному потоку продолжить
            mainSemaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}