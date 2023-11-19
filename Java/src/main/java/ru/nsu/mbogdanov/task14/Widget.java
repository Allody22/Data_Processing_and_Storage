package ru.nsu.mbogdanov.task14;

import java.util.concurrent.Semaphore;

/**
 * Класс, представляющий винтик, состоящий из детали C и модуля.
 */
public class Widget extends FactoryComponent {

    private final Semaphore moduleSemaphore;
    private final Semaphore partCSemaphore;

    /**
     * Конструктор для Widget.
     *
     * @param widgetName      Имя винтика.
     * @param widgetSemaphore Семафор для управления производством винтика.
     * @param moduleSemaphore Семафор для модуля.
     * @param partCSemaphore  Семафор для детали C.
     */
    public Widget(String widgetName, Semaphore widgetSemaphore, Semaphore moduleSemaphore, Semaphore partCSemaphore) {
        super(widgetName, widgetSemaphore, 0);
        this.moduleSemaphore = moduleSemaphore;
        this.partCSemaphore = partCSemaphore;
    }

    @Override
    protected void produceComponent() throws InterruptedException {
        moduleSemaphore.acquire(); // ожидание модуля
        partCSemaphore.acquire(); // ожидание детали C
        System.out.println(componentName + " №" + counter.incrementAndGet() + " сделан");
    }
}
