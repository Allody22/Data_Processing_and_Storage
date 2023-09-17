package ru.nsu;

import java.util.Arrays;
import java.util.List;

public class Task3 {

    //Класс, реализующий Runnable интерфейс
    public static class PrintList<T> implements Runnable {
        //Этот список мы будем печатать
        private final List<T> list;

        //Ну это конструктор этого класса
        public PrintList(List<T> list) {
            this.list = list;
        }

        //Вот собственно это функцию мы и реализуем
        @Override
        public void run() {
            list.forEach(System.out::println);  // печатаем каждую нить
        }
    }

    public static void main(String[] args) {
        List<Runnable> tasks = Arrays.asList(
                //Каждая нить должна печатать какие-то строки
                new PrintList<>(Arrays.asList("1 ", "ПЕРВАЯ ", "ONE")),
                new PrintList<>(Arrays.asList("2 ", "ВТОРАЯ ", "SECOND")),
                new PrintList<>(Arrays.asList("3 ", "ТРЕТЬЯ ", "THIRD")),
                new PrintList<>(Arrays.asList("4", "ЧЁТВЕРТАЯ ", "FOUR"))
        );

        tasks.stream()
                // создаем новую нить для каждой задачи и запускаем её
                .map(Thread::new)
                .forEach(Thread::start);
    }
}
