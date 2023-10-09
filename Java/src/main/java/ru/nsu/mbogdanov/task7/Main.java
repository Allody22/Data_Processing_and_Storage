package ru.nsu.mbogdanov.task7;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Создаем объект Scanner для чтения ввода пользователя
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите количество потоков:");

        int threadsNumber;
        try {
            // Пытаемся считать число потоков, которое ввел пользователь
            threadsNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Ошибка ввода! Пожалуйста, введите корректное число потоков.");
            return;
        }

        // Ограничиваем количество потоков до количества доступных процессорных ядер
        // для оптимизации производительности и избежания излишней многопоточности.
        // Очень много поток с очень крутым вычислением получится сделать только на очень-очень крутом компе
        threadsNumber = Math.min(threadsNumber, Runtime.getRuntime().availableProcessors());

        // Создаем объект для управления вычислениями и выполняем вычисления
        CalculationManager task = new CalculationManager(threadsNumber, 100000);
        task.execute();

        // Выводим результаты вычислений и сравниваем со значением π из библиотеки джавы
        System.out.println("Library pi = " + Math.PI);

        //Наше значение числа π с рядом Лейбница будет 100% отличаться от Math.PI.
        // Чем больше итераций (членов ряда) добавляется в вычисление, тем ближе результат к реальному значению π.
        // Но это всё еще вещественные числа, поэтому результат всегда будет немного отличаться от значения π.
        System.out.println("Calculated pi = " + task.getResult());
    }
}