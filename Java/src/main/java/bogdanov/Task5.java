package bogdanov;

public class Task5 {

    public static void main(String[] args) {
        // Задача для дочерней нити
        Runnable runnableThread = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Я не главная нить и я печатаю");
            }
            System.out.println("Сейчас нить завершится");
        };

        // Запуск задачи в новой нити
        Thread childThread = new Thread(runnableThread);
        childThread.start();

        // Главная нить ждет 2 секунды перед тем как прервать дочернюю нить
        try {
            Thread.sleep(2000);
        } catch (InterruptedException exception) {
            // catch блок активируется в случае, если текущая нить (главная нить)
            // будет прервана другой нитью во время своего сна (вызова Thread.sleep)
            System.out.println("Что-то пошло не так....");
        }

        // Прерываем дочернюю нить
        childThread.interrupt();
    }
}
