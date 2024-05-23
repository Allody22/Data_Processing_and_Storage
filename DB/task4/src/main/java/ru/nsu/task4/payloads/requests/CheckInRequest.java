package ru.nsu.task4.payloads.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequest {

    @Schema(description = "Ссылка на созданную сущность бронирования.", example = "06B046", required = true)
    private String bookRef;
}
