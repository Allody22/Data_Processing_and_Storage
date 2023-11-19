package ru.nsu.mbogdanov.task14;

import java.util.concurrent.Semaphore;

/**
 * Класс, представляющий отдельную деталь (A, B, C).
 */
public class Part extends FactoryComponent {

    /**
     * Конструктор для детали.
     *
     * @param partName       Имя детали.
     * @param semaphore      Семафор для управления производством.
     * @param productionTime Время задержки производства.
     */
    public Part(String partName, Semaphore semaphore, long productionTime) {
        super(partName, semaphore, productionTime);
    }

    @Override
    protected void produceComponent() throws InterruptedException {
        Thread.sleep(delayTime); // имитация производства детали
        System.out.println(componentName + " №" + counter.incrementAndGet() + " сделана");
    }
}
