package com.monetary.transfer.exceptions;

import lombok.Getter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Basic class for building family of categorised exceptions.
 */
public abstract class TypedWebApplicationException extends WebApplicationException {

    /**
     * Textual description of the exception category.
     */
    @Getter
    private final String type;

    /**
     * Constructs an instance based on the provided message.
     *
     * @param message exception's textual description or code to lookup in
     *                string resource file.
     */
    TypedWebApplicationException(Response.Status status, String type, String message) {
        super(message, status);
        this.type = type;
    }
}
