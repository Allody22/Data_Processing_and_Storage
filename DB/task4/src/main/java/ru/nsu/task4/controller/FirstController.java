package ru.nsu.task4.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.nsu.task4.model.PriceFullOneRaceAnalysis;
import ru.nsu.task4.payloads.response.DataResponse;
import ru.nsu.task4.services.FlightsPriceService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class FirstController {
    private final FlightsPriceService flightsPriceService;

    @Operation(
            summary = "Запрос для первичного заполнения таблицы price_full_one_race_analysis.",
            description = "Мы извлекаем всю известную информацию о полётах, билета, самолёт " +
                    "из таблиц созданных из базы данных по заданию и систематизируем всю эту информацию в нашей таблице.",
            tags = {"prices", "structure"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "True при успешном выполнении запроса",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500",description = "Какая-то внутренняя ошибка на сервере",content = {@Content(schema = @Schema())})})
    @PostMapping("/structure/price")
    @Transactional
    public ResponseEntity<?> structurePriceForAllRaces() {
        flightsPriceService.getFullPriceForOneRace();
        return ResponseEntity.ok(new DataResponse(true));
    }


    @Operation(
            summary = "Заполнение как можно большего количества информации в таблице price_full_one_race_analysis.",
            description = "Теперь мы уже извлекаем всю известную информацию о рейсах из нашей собственной таблице" +
                    "и примерно высчитываем информации о будущих рейсах, поездках.",
            tags = {"prices", "structure"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "True при успешном заполнение таблицы",
                    content = {@Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500",description = "Какая-то внутренняя ошибка на сервере",content = {@Content(schema = @Schema())})})
    @PostMapping("/calculate/price")
    @Transactional
    public ResponseEntity<?> calculatePriceForMissingRaces() {
        flightsPriceService.calculateMissingPrices();
        return ResponseEntity.ok(new DataResponse(true));
    }


    @Operation(
            summary = "Получаем всю информацию о рейсах, средняя цена за один билет которых неизвестна.",
            description = "Получаем всю информацию о рейсах, средняя цена за один билет которых неизвестна",
            tags = {"prices", "average"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Массив с информацией об этих массивах",
                    content = {@Content(schema = @Schema(implementation = PriceFullOneRaceAnalysis[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500",description = "Какая-то внутренняя ошибка на сервере",content = {@Content(schema = @Schema())})})
    @GetMapping("/get/races_without_average_price")
    @Transactional
    public ResponseEntity<?> getRacesWithoutAveragePrice() {
        return ResponseEntity.ok(flightsPriceService.findAllRacesWithoutAveragePrice());
    }


    @Operation(
            summary = "Получаем всю информацию о рейсах, полная цена за поездку о которых неизвестна.",
            description = "Получаем всю информацию о рейсах, полная цена за поездку о которых неизвестна",
            tags = {"prices", "average"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Массив с информацией об этих массивах",
                    content = {@Content(schema = @Schema(implementation = PriceFullOneRaceAnalysis[].class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500",description = "Какая-то внутренняя ошибка на сервере",content = {@Content(schema = @Schema())})})
    @GetMapping("/get/races_without_total_price")
    @Transactional
    public ResponseEntity<?> getRacesWithoutTotalPrice() {
        return ResponseEntity.ok(flightsPriceService.findAllRacesWithoutTotalPrice());
    }
}
