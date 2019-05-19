package com.monetary.transfer.exceptions.mappers;

import com.monetary.transfer.exceptions.TypedWebApplicationException;
import com.monetary.transfer.exceptions.mappers.models.ErrorDto;
import com.monetary.transfer.resources.ErrorMessagesRepository;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapper of the {@link TypedWebApplicationException} to JSON output.
 */
@Provider
public class TypedWebApplicationExceptionMapper implements ExceptionMapper<TypedWebApplicationException> {

    /**
     * Error messages repository
     */
    @Inject
    public ErrorMessagesRepository errors;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(TypedWebApplicationException exception) {
        Response response = exception.getResponse();
        Response.Status status = response.getStatusInfo().toEnum();
        /*
         * Mapping error description to resource file
         */
        String description = errors.lookup(exception.getMessage());
        /*
         * Building exception DTO
         */
        ErrorDto dto = ErrorDto
                .builder()
                .error(exception.getType())
                .description(description)
                .build();
        /*
         * Building response
         */
        return Response
                .status(status)
                .entity(dto)
                .type("application/json")
                .build();
    }
}
