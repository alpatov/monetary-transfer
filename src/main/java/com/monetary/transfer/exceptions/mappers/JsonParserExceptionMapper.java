package com.monetary.transfer.exceptions.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monetary.transfer.exceptions.mappers.models.ErrorDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapper of the {@link JsonProcessingException} to JSON output.
 */
@Provider
public class JsonParserExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(JsonProcessingException exception) {
        /*
         * Preparing meaningful exception description.
         */
        String description = exception
                .getOriginalMessage()
                .replaceAll("\\(.*\\)", "")
                .trim()
                .concat(".");
        /*
         * Building exception DTO
         */
        ErrorDto dto = ErrorDto.builder()
                .error("Malformed JSON")
                .description(description)
                .build();
        /*
         * Building response
         */
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(dto)
                .type("application/json")
                .build();
    }
}
