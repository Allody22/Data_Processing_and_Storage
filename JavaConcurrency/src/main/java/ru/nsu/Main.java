package ru.nsu;

import ru.nsu.components.Company;
import ru.nsu.components.Founder;

public class Main {

    /*
    Надо же написать main метод и посмотреть, как работает наша компания и её отделы.
     */
    public static void main(String[] args) {
        // Создаем компанию с 5 отделами.
        Company company = new Company(5);

        // Создаем объект Founder, связанный с этой компанией.
        Founder founder = new Founder(company);

        // Запускаем рабочие потоки.
        founder.start();

        // Максимальное число, которое можно получить, равно 15 (максимальное значение для одного отдела)
        // умноженное на количество отделов. Так как у нас 5 отделов, максимальное значение равно 75.
    }
}


