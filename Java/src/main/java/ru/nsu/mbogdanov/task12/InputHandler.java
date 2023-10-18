package ru.nsu.mbogdanov.task12;

import java.util.Scanner;

/**
 * Обработчик пользовательского ввода.
 * Считывает строки и добавляет их в связанный список.
 */
public class InputHandler implements Runnable {

    @Override
    public void run() {
        System.out.println("Начинайте печатать строки:");
        // try-with-resources для автоматического закрытия сканера
        try (Scanner scanner = new Scanner(System.in)) {
            //Работаем пока нас не остановили
            while (!Thread.currentThread().isInterrupted()) {
                String input = scanner.nextLine();

                if (input.isEmpty()) {
                    //Если строка пустая - то мы забираем себе власть над список и печатаем его
                    synchronized (Main.headLock) {
                        System.out.println(Main.getList());
                    }
                } else if (input.length() > 80) {
                    //Если строка больше 80 символов, то разрезаем
                    //Берём по 80 символов в отдельные новые строки, пока строка не будет удовлетворять условию
                    while (!input.isEmpty()) {
                        int lengthToTake = Math.min(80, input.length());
                        String sub = input.substring(0, lengthToTake);
                        synchronized (Main.headLock) {
                            Main.addToBeginning(sub);
                        }
                        input = input.substring(lengthToTake);
                    }
                } else {
                    //Если тупо хорошая строка - то тупо закидываем её в начало
                    synchronized (Main.headLock) {
                        Main.addToBeginning(input);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
