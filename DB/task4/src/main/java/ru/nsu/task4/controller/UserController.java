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
import org.springframework.web.bind.annotation.*;
import ru.nsu.task4.payloads.requests.BookingRaceRequest;
import ru.nsu.task4.payloads.requests.CheckInRequest;
import ru.nsu.task4.payloads.response.BookingResponse;
import ru.nsu.task4.payloads.response.DataResponse;
import ru.nsu.task4.services.intertaces.IAirportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Tag(name = "1. User Controller", description = "Действия пользователей (покупка билета, проверка билета).")
public class UserController {

    private final IAirportService airportService;


    @Operation(summary = "Бронирование рейса.",
            description = "Передаётся ID рейса, класс билета и информация о человеке, необходимая для бронирования." +
                    "Запрос возвращает сгенерированный ID билета." +
                    "Человек получает ticketNumber из которого в будущем получит Boarding pass",
            tags = {"races", "booking"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рейс успешно забронирован, уникальный айди билета создан",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/races/book")
    public ResponseEntity<?> bookRace(@RequestBody BookingRaceRequest bookingRaceRequest) throws JsonProcessingException {
        return ResponseEntity.ok(airportService.createBooking(bookingRaceRequest));
    }

    @Operation(summary = "Регистрация на рейс",
            description = "Позволяет пассажиру выполнить онлайн регистрацию на рейс, " +
                    "используя информация, полученную после покупки. " +
                    "Пользователю возвращается его boarding pass и еще раз информация о месте, " +
                    "чтобы предоставить это всё конкретно при посадке. " +
                    "Человек получает Boarding Pass, который он просто покажет при посадке",
            tags = {"check-in"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Регистрация на рейс выполнена успешно." +
                    " Пользователь может лететь.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос, проверьте предоставленные данные"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/races/check-in")
    public ResponseEntity<?> checkInFlight(@RequestBody CheckInRequest checkInRequest) {
        return ResponseEntity.ok(airportService.checkInOnlineForAFlight(checkInRequest));
    }

}
