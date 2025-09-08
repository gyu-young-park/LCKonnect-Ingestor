package io.github.gyu_young_park.LCKonnect_Ingestor.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private String message;
}
