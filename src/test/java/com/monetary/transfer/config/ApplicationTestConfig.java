package com.monetary.transfer.config;

import com.monetary.transfer.controllers.AccountApiExtension;

import javax.ws.rs.ApplicationPath;

/**
 * REST API configuration for testing purposes.
 */
@ApplicationPath("/")
public class ApplicationTestConfig extends ApplicationConfig {

    {
        /*
         * Registering account REST API extension for creation of accounts
         * during testing.
         */
        register(AccountApiExtension.class);
    }
}
