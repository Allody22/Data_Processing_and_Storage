package ru.nsu.components;

import java.util.concurrent.CyclicBarrier;

/**
 * Класс Worker представляет собой задачу, которую нужно выполнить для каждого отдела.
 */
public class Worker implements Runnable {
    private final Department department;

    private final CyclicBarrier barrier;

    /**
     * Конструктор для Worker.
     *
     * @param department Отдел, для которого выполняется задача.
     * @param barrier    CyclicBarrier для синхронизации всех Worker'ов.
     *                   CyclicBarrier позволяет нескольким потокам ожидать друг друга, чтобы
     *                   продолжить выполнение, гарантируя, что все Worker'ы завершат свою работу
     *                   перед продолжением основного потока.
     */
    public Worker(Department department, CyclicBarrier barrier) {
        this.department = department;
        this.barrier = barrier;
    }

    /**
     * Запускает выполнение задачи отдела и ожидает на барьере после завершения.
     */
    @Override
    public void run() {
        department.performCalculations();
        try {
            barrier.await();  // Ожидание, пока все Worker'ы не достигнут барьера.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
