package ru.nsu.task4.services.intertaces;

import ru.nsu.task4.payloads.requests.BookingRaceRequest;
import ru.nsu.task4.payloads.requests.CheckInRequest;
import ru.nsu.task4.payloads.response.ArrivalFlights;
import ru.nsu.task4.payloads.response.BoardingPassResponse;
import ru.nsu.task4.payloads.response.BookingResponse;
import ru.nsu.task4.payloads.response.DepartureFlights;

import java.util.Date;
import java.util.List;

public interface IAirportService {

    /**
     * Получение всех доступных городов отправления и назначения.
     */
    void getAllAvailableCities();


    /**
     * Получение всех доступных аэропортов отправки и прибытия.
     */
    void getAllAvailableAirports();

    /**
     * Получение списка всех аэропортов в городе.
     *
     * @param city имя города.
     */
    void getAllAirportsInCity(String city);


    /**
     * Получение списка рейсов, прилетающих в аэропорт.
     * Включает дни недели, время прибытия, номер рейса и пункт отправления.
     *
     * @param airport название аэропорта.
     * @return Список рейсов, прибывающих в аэропорт.
     */
    List<ArrivalFlights> getArrivalTimetableOfTheAirport(String airport);

    /**
     * Получение списка рейсов, улетающих из аэропорта.
     * Включает дни недели, время отправления, номер рейса и пункт назначения.
     *
     * @param airport название аэропорта.
     * @return Список рейсов, вылетающих из аэропорта.
     */
    List<DepartureFlights> getDepartureTimetableOfTheAirport(String airport);

    /**
     * Получение списка маршрутов между двумя точками с опциональными фильтрами.
     *
     * @param from           начальная точка (город или аэропорт).
     * @param to             конечная точка (город или аэропорт).
     * @param departureDate  дата отправления.
     * @param bookingClass   класс билета (Economy, Comfort, Business).
     * @param maxConnections максимальное количество пересадок (0 для прямых рейсов).
     */
    void getRaces(String from, String to, Date departureDate, String bookingClass, Integer maxConnections);

    /**
     * Создание бронирования для выбранного маршрута для одного пассажира.
     *
     * @param bookingRaceRequest Вся информация, необходимая для бронирования билета.
     * @return сгенерированная информация о забронированном месте.
     */
    BookingResponse createBooking(BookingRaceRequest bookingRaceRequest);

    /**
     * ЧекИн перед посадкой на рейс для проверки информации.
     *
     * @param checkInRequest информация
     * @return информация, которую человек покажет уже в самолёте.
     */
    BoardingPassResponse checkInOnlineForAFlight(CheckInRequest checkInRequest);
}
