package com.monetary.transfer.exceptions.mappers;

import com.monetary.transfer.exceptions.mappers.models.ErrorDto;
import com.monetary.transfer.resources.ErrorMessagesRepository;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper of the {@link ConstraintViolationException} to JSON output.
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    /**
     * Error messages repository.
     */
    @Inject
    public ErrorMessagesRepository errors;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        /*
         * Building exceptionDto entity
         */
        List<ErrorDto> violations = exception
                .getConstraintViolations()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        /*
         * Reducing the complexity of the message if only one constraint was
         * violated.
         */
        Object entity = violations.size() == 1
                ? violations.get(0)
                : violations;
        /*
         * Building response
         */
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(entity)
                .build();
    }

    private ErrorDto mapToDto(ConstraintViolation<?> violation) {
        String description = errors.lookup(violation.getMessage());
        return ErrorDto
                .builder()
                .error("Constraint violation")
                .description(description)
                .build();
    }
}
