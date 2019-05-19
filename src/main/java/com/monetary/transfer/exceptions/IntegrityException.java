package com.monetary.transfer.exceptions;

import javax.ws.rs.core.Response;

/**
 * Data integrity violation exception.
 */
public class IntegrityException extends TypedWebApplicationException {

    /**
     * Constructs an instance based on the provided message.
     *
     * @param message exception's textual description or code to lookup in
     *                string resource file.
     */
    public IntegrityException(String message) {
        super(Response.Status.BAD_REQUEST, "Integrity violation", message);
    }
}
