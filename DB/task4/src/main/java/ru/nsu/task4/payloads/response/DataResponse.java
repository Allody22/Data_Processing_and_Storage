package ru.nsu.task4.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {

    @Schema(description = "Результат операции.", example = "true")
    private boolean data;
}
