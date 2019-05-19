package com.monetary.transfer.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.monetary.transfer.controllers.TransferApi;
import com.monetary.transfer.exceptions.mappers.ConstraintViolationExceptionMapper;
import com.monetary.transfer.exceptions.mappers.JsonParserExceptionMapper;
import com.monetary.transfer.exceptions.mappers.TypedWebApplicationExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

import javax.ws.rs.ApplicationPath;

/**
 * Application configuration class.
 */
@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {

    {
        /*
         * Registering packages to scan for components
         */
        packages("com.revolut.monetary.transfer");

        /*
         * Registering features
         */
        register(ValidationFeature.class);
        register(JacksonJaxbJsonProvider.class);
        register(TypedWebApplicationExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);
        register(JsonParserExceptionMapper.class);

        /*
         * Registering resources
         */
        register(TransferApi.class);

        /*
         * Registering Dependency Injection Binder
         */
        register(new InjectionConfig());
    }
}
