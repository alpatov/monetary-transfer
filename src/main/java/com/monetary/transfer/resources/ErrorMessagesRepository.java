package com.monetary.transfer.resources;

import javax.inject.Singleton;

/**
 * Error messages repository.
 */
@Singleton
public final class ErrorMessagesRepository extends AbstractStringResource {

    public ErrorMessagesRepository() {
        super("strings.errors");
    }
}
