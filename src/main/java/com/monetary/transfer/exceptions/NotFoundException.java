package com.monetary.transfer.exceptions;

import javax.ws.rs.core.Response;

/**
 * Exception that is triggered when a resource has not been found.
 */
public class NotFoundException extends TypedWebApplicationException {

    /**
     * Constructs an instance based on the provided message.
     *
     * @param message exception's textual description or code to lookup in
     *                string resource file.
     */
    public NotFoundException(String message) {
        super(Response.Status.NOT_FOUND, "Not found", message);
    }
}
