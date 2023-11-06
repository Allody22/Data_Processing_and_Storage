package ru.nsu.mbogdanov.task10;

/**
 * Класс для синхронизированного вывода строк из двух нитей.
 */
public class SyncPrinter {

    /**
     * Объект для синхронизации.
     */
    private final Object LOCK = new Object();

    // Теперь только один флаг, который определяет очередность печати
    private boolean isMainTurn = true;

    /**
     * Метод для вывода строки из главной нити.
     * Пока мы не разрешили потоку печатать - он не будет.
     *
     * @param index номер текущей строки.
     */
    public void printFromMain(int index) {
        //Вот тут мьютекст - то есть один из инструментов синхронизации
        synchronized (LOCK) {
            while (!isMainTurn) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(index + " от главной нити");
            isMainTurn = false;
            LOCK.notifyAll();
        }
    }

    /**
     * Метод для вывода строки из дочерней нити.
     * Пока мы не разрешили потоку печатать - он не будет.
     *
     * @param index номер текущей строки.
     */
    public void printFromChild(int index) {
        //Вот тут мьютекст - то есть один из инструментов синхронизации
        synchronized (LOCK) {
            while (isMainTurn) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(index + " от новой нити");
            isMainTurn = true;
            LOCK.notifyAll();
        }
    }
}
