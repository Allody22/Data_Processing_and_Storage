package ru.nsu.mbogdanov.task13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс, представляющий набор вилок на столе.
 */
public class Forks {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    //Если true - вилка свободна
    private final boolean[] forksFree;

    /**
     * Создает экземпляр набора вилок.
     *
     * @param numberOfForks количество вилок на столе.
     */
    public Forks(int numberOfForks) {
        forksFree = new boolean[numberOfForks];
        //Изначально все вилки свободны
        for (int i = 0; i < numberOfForks; i++) {
            forksFree[i] = true;
        }
    }


    /**
     * Попытка взять указанные вилки.
     *
     * @param left  индекс левой вилки.
     * @param right индекс правой вилки.
     * @return true, если обе вилки успешно взяты, иначе false.
     */
    public boolean takeForks(int left, int right) {
        lock.lock();
        try {
            long timeout = (long) (Math.random() * 1000);
            while (!forksFree[left] || !forksFree[right]) {
                if(!condition.await(timeout, TimeUnit.MILLISECONDS)) {
                    // Если таймаут истек, возвращаем false
                    return false;
                }
            }
            forksFree[left] = false;
            forksFree[right] = false;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Освобождение указанных вилок.
     *
     * @param left  индекс левой вилки.
     * @param right индекс правой вилки.
     */
    public void releaseForks(int left, int right) {
        lock.lock();
        try {
            forksFree[left] = true;
            forksFree[right] = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
