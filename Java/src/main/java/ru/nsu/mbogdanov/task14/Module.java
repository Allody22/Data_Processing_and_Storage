package ru.nsu.mbogdanov.task14;

import java.util.concurrent.Semaphore;

/**
 * Класс, представляющий модуль, состоящий из деталей A и B.
 */
public class Module extends FactoryComponent {

    private final Semaphore partASemaphore;
    private final Semaphore partBSemaphore;

    /**
     * Конструктор для модуля.
     *
     * @param moduleName      Имя модуля.
     * @param moduleSemaphore Семафор для управления производством модуля.
     * @param partASemaphore  Семафор для детали A.
     * @param partBSemaphore  Семафор для детали B.
     * @param delayTime       Время задержки производства.
     */
    public Module(String moduleName, Semaphore moduleSemaphore, Semaphore partASemaphore, Semaphore partBSemaphore, long delayTime) {
        super(moduleName, moduleSemaphore, delayTime);
        this.partASemaphore = partASemaphore;
        this.partBSemaphore = partBSemaphore;
    }

    @Override
    protected void produceComponent() throws InterruptedException {
        partASemaphore.acquire(); // ожидание детали A
        partBSemaphore.acquire(); // ожидание детали B
        System.out.println(componentName + " №" + counter.incrementAndGet() + " сделан");
    }
}
