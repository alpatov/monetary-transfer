package com.monetary.transfer.exceptions.mappers.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Exception Data Transfer Object (DTO) model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    /**
     * Textual type of the exception.
     */
    private String error;

    /**
     * Description of the exception.
     */
    private String description;
}
