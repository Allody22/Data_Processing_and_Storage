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
import ru.nsu.task4.payloads.response.*;
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
    public ResponseEntity<?> getAvailableCities(@RequestHeader("lang") String lang) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getAllAvailableCities(lang));
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
    public ResponseEntity<?> getAllAvailableAirports(@RequestHeader("lang") String lang) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getAllAvailableAirports(lang));
    }

    @Operation(
            summary = "Получаем список всех аэропортов в указанном городе.",
            description = "Возвращает список аэропортов, расположенных в заданном городе." +
                    "Название города должно точно совпадать с русским или английским названием в базе данных.",
            tags = {"airports", "city"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список аэропортов в городе",
                    content = {@Content(schema = @Schema(implementation = AirportNameResponse[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Какая-то внутренняя ошибка на сервере", content = {@Content(schema = @Schema())})})
    @GetMapping("/get/airports/{city}")
    @Transactional
    public ResponseEntity<?> getAllAirportsInCity(@RequestHeader("lang") String lang, @Valid @NotNull @PathVariable String city) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getAllAirportsInCity(lang, city));
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
    public ResponseEntity<?> getAirportsArrivalRaces(@RequestHeader("lang") String lang, @Valid @NotNull @PathVariable String airport) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getArrivalTimetableOfTheAirport(lang, airport));
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
    public ResponseEntity<?> getAirportsDepartureRaces(@RequestHeader("lang") String lang, @Valid @NotNull @PathVariable String airport) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.getDepartureTimetableOfTheAirport(lang, airport));
    }

    @Operation(summary = "Получение списка маршрутов между двумя точками с опциональными фильтрами",
            description = "Возвращает список маршрутов с учетом начальной и конечной точки, даты отправления, класса бронирования и максимального количества пересадок.",
            tags = {"races"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список найденных маршрутов",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SearchResultResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/search")
    public ResponseEntity<?> listRoutes(@RequestHeader("lang") String lang, @RequestBody RouteSearchRequest request) throws JsonProcessingException {
        String ticketClass = request.getBookingClass();
        if (!ticketClass.equals("Economy") && !ticketClass.equals("Business") && !ticketClass.equals("Comfort")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Категория билетов '" + ticketClass + "' не существует."));
        }
        String pointFrom = request.getFrom();
        String pointTo = request.getTo();
        if (!isSameLanguage(pointFrom,pointTo)){
            return ResponseEntity.badRequest().body(new MessageResponse("Пожалуйста, используйте один язык для задания пункта отбытия и прибытия"));
        }
        var ans = airportService.getRaces(lang, pointFrom, pointTo, request.getDepartureDate(), ticketClass, request.getMaxConnections());
        return ResponseEntity.ok().body(ans);
    }

    private static boolean isSameLanguage(String str1, String str2) {
        Character.UnicodeBlock block1 = getUnicodeBlock(str1);
        Character.UnicodeBlock block2 = getUnicodeBlock(str2);
        return block1 != null && block1.equals(block2);
    }


    private static Character.UnicodeBlock getUnicodeBlock(String str) {
        for (char ch : str.toCharArray()) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
            if (block != null) {
                return block;
            }
        }
        return null;
    }

}
