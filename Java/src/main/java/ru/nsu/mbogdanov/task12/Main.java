package ru.nsu.mbogdanov.task12;

import java.util.LinkedList;

public class Main {

    //Всё по ООП - всё приватно
    //Здесь у нас и будут хранится все строки
    private static final LinkedList<String> list = new LinkedList<>();

    //Используем как монитор для синхронизации потоков
    public static final Object headLock = new Object();

    public static void main(String[] args) {
        Thread inputThread = new Thread(new InputHandler());
        Thread sorterThread = new Thread(new ListSorter());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("\nПолучили SIGINT. Заканчиваем работу")));

        inputThread.start();
        sorterThread.start();
    }

    // Геттер для списка
    public static synchronized LinkedList<String> getList() {
        return list;
    }

    // Сеттер для добавления элемента в начало списка
    public static synchronized void addToBeginning(String item) {
        list.addFirst(item);
    }
}

