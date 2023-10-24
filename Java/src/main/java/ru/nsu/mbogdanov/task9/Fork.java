package ru.nsu.mbogdanov.task9;

/**
 * Вилку для философов.
 */
public class Fork {
    private final int id;

    /**
     * Конструктор для создания вилки с конкретным айди.
     *
     * @param id Айди вилки.
     */
    public Fork(int id) {
        this.id = id;
    }

    /**
     * Получение идентификатора вилки.
     *
     * @return Идентификатор вилки.
     */
    public int getId() {
        return id;
    }
}
