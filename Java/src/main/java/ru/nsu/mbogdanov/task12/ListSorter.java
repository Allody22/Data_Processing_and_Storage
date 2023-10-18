package ru.nsu.mbogdanov.task12;

import java.util.LinkedList;

/**
 * Обработчик сортировки списка.
 * Сортирует список каждые 5 секунд.
 */
public class ListSorter implements Runnable {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //Раз в 5 секунд по условию работает
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            //Как можно - забираем власть и сортируем список
            synchronized (Main.headLock) {
                bubbleSort(Main.getList());
            }
        }
    }

    private void bubbleSort(LinkedList<String> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
