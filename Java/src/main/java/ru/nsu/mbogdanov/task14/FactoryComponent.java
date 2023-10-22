package ru.nsu.mbogdanov.task14;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Абстрактный класс для компонентов производства.
 */
public abstract class FactoryComponent implements Runnable {

    protected final String componentName;
    protected final Semaphore semaphore;
    protected final long delayTime;

    /**
     * Счетчик для отслеживания производства компонентов
     */
    protected static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Конструктор для FactoryComponent.
     *
     * @param componentName - Имя детальки.
     * @param semaphore     - Семафор для управления производством.
     * @param delayTime     - Время задержки производства.
     */
    public FactoryComponent(String componentName, Semaphore semaphore, long delayTime) {
        this.componentName = componentName;
        this.semaphore = semaphore;
        this.delayTime = delayTime;
    }

    @Override
    public void run() {
        try {
            while (true) {
                produceComponent();
                semaphore.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Абстрактный метод для производства компонентов.
     *
     * @throws InterruptedException Исключение при прерывании потока.
     */
    protected abstract void produceComponent() throws InterruptedException;
}
