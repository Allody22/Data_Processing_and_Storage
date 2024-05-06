package ru.nsu.task4.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.task4.payloads.requests.RouteSearchRequest;
import ru.nsu.task4.payloads.response.AirportsNamesResponse;
import ru.nsu.task4.payloads.response.ArrivalFlights;
import ru.nsu.task4.payloads.response.CitiesNamesResponse;
import ru.nsu.task4.payloads.response.DepartureFlights;
import ru.nsu.task4.services.intertaces.IAirportService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/v1/races")
@AllArgsConstructor
@Tag(name = "2. Airport Controller", description = "Получение рейсов и аэропортов.")
public class AirportController {

    private final IAirportService airportService;

    @Operation(
            summary = "Получаем все города, которые участвуют в перелётах.",
            description = "Получаем список городов, из которых отправляются рейсы или в которые прилетают",
            tags = {"cities", "flights"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список с названиями городов",
                    content = {@Content(schema = @Schema(implementation = CitiesNamesResponse[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Какая-то внутренняя ошибка на сервере", content = {@Content(schema = @Schema())})})
    @GetMapping("/get/available/cities")
    @Transactional
    public ResponseEntity<?> getAvailableCities() {
        return ResponseEntity.ok(airportService.getAllAvailableCities());
    }

    @Operation(
            summary = "Получаем все аэропорты, участвующие в перелётах.",
            description = "Получаем список аэропортов, из которых отправляются рейсы или в которые прилетают",
            tags = {"airports", "flights"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список со строковыми названиями аэропортов",
                    content = {@Content(schema = @Schema(implementation = AirportsNamesResponse[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Какая-то внутренняя ошибка на сервере", content = {@Content(schema = @Schema())})})
    @GetMapping("/get/available/airports")
    @Transactional
    public ResponseEntity<?> getAllAvailableAirports() throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getAllAvailableAirports());
    }

    @Operation(
            summary = "Получаем список всех аэропортов в указанном городе.",
            description = "Возвращает список аэропортов, расположенных в заданном городе." +
                    "Название города должно точно совпадать с русским или английским названием в базе данных.",
            tags = {"airports", "city"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список аэропортов в городе",
                    content = {@Content(schema = @Schema(implementation = AirportsNamesResponse[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Какая-то внутренняя ошибка на сервере", content = {@Content(schema = @Schema())})})
    @GetMapping("/get/airports/{city}")
    @Transactional
    public ResponseEntity<?> getAllAirportsInCity(@Valid @NotNull @PathVariable String city) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getAllAirportsInCity(city));
    }

    @Operation(
            summary = "Получаем расписания прилетающих самолётов.",
            description = "Получение списка всех рейсов, прилетающих в аэропорт по его названию.",
            tags = {"airports", "races"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список рейсов, прибывающих в аэропорт",
                    content = {@Content(schema = @Schema(implementation = ArrivalFlights[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Какая-то внутренняя ошибка на сервере", content = {@Content(schema = @Schema())})})
    @GetMapping("/get/arrival/{airport}")
    @Transactional
    public ResponseEntity<?> getAirportsArrivalRaces(@Valid @NotNull @PathVariable String airport) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getArrivalTimetableOfTheAirport(airport));
    }

    @Operation(
            summary = "Получаем расписания улетающих самолётов.",
            description = "Получение списка всех рейсов, улетающих из аэропорта по его названию.",
            tags = {"airports", "races"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список рейсов, улетающих из аэропорта",
                    content = {@Content(schema = @Schema(implementation = DepartureFlights[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Какая-то внутренняя ошибка на сервере", content = {@Content(schema = @Schema())})})
    @GetMapping("/get/departure/{airport}")
    @Transactional
    public ResponseEntity<?> getAirportsDepartureRaces(@Valid @NotNull @PathVariable String airport) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getDepartureTimetableOfTheAirport(airport));
    }


    //TODO доделать
    @Operation(summary = "Получение списка маршрутов между двумя точками с опциональными фильтрами",
            description = "Возвращает список маршрутов с учетом начальной и конечной точки, даты отправления, класса бронирования и максимального количества пересадок.",
            tags = {"races"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список найденных маршрутов",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RouteSearchRequest.class))}),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/search")
    public ResponseEntity<?> listRoutes(@RequestBody RouteSearchRequest request) {
        airportService.getRaces(request.getFrom(), request.getTo(), request.getDepartureDate(), request.getBookingClass(), request.getMaxConnections());
        return ResponseEntity.ok().body("Здесь будет возвращен список маршрутов согласно запросу");
    }
}
