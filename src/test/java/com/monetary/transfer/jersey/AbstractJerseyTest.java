package com.monetary.transfer.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.monetary.transfer.exceptions.mappers.models.ErrorDto;
import com.monetary.transfer.resources.ErrorMessagesRepository;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AbstractJerseyTest extends JerseyTest {

    protected final ErrorMessagesRepository errors = new ErrorMessagesRepository();

    private List<String> retrieveErrorDescriptions(Response response) {
        ObjectMapper mapper = new ObjectMapper();
        String json = response.readEntity(String.class);
        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, ErrorDto.class);
            List<ErrorDto> errors = mapper.readValue(json, type);
            return errors
                        .parallelStream()
                        .map(ErrorDto::getDescription)
                        .distinct()
                        .collect(Collectors.toList());
        } catch (IOException e) {
            try {
                ErrorDto error = mapper.readValue(json, ErrorDto.class);
                return Collections.singletonList(error.getDescription());
            } catch (Exception ignore) {}
        }
        return Collections.emptyList();
    }

    /**
     * Reads JSON resource file content.
     *
     * @param resource path to resource file relatively to resource folder
     * @return file content as string
     * @throws IOException in case of IO related errors
     */
    private String readJsonResource(String resource) throws IOException {
        URL url = ClassLoader.getSystemResource(resource);
        try (InputStream stream = url.openStream()) {
            return new String(stream.readNBytes(stream.available()));
        }
    }

    /**
     * Performs constraint violation control test.
     *
     * <p>The purpose of test is to verify that API performs input validation
     * before processing.
     *
     * @param target relative path to REST-endpoint
     * @param method HTTP method
     * @param json json object to send to the API
     * @param status response status
     * @param errorMessages an array of the resource identifiers of errorMessages
     *                      message that must be displayed as a reaction to the
     *                      constraint violation
     */
    protected final void assertConstraintViolation(String target, String method, Object json, Response.Status status, String... errorMessages) {
        /*
         * Executing request to the API endpoint
         */
        Response response = target(target)
                .request()
                .method(method, Entity.json(json));
        assertEquals(
                errors.lookup("test.error.constraint.violation.unchecked"),
                status,
                response.getStatusInfo().toEnum()
        );
        if (errorMessages.length > 0) {
            try {
                /*
                 * Checking for expected error messages
                 */
                List<String> errorDescriptions = retrieveErrorDescriptions(response);
                assertTrue(
                        errors.lookup("test.error.response.description.required"),
                        Arrays.stream(errorMessages)
                                .map(errors::lookup)
                                .allMatch(errorDescriptions::contains)
                );
            } catch (Exception e) {
                e.printStackTrace(System.err);
                fail(errors.lookup("test.error.response.description.required"));
            }
        }
    }

    /**
     * Performs constraint violation control test.
     *
     * <p>The purpose of test is to verify that API performs input validation
     * before processing.
     *
     * @param target relative path to REST-endpoint
     * @param method HTTP method
     * @param jsonResource jsonResource data file to send to the API
     * @param status response status
     * @param errorMessages an array of the resource identifiers of errorMessages
     *                      message that must be displayed as a reaction to the
     *                      constraint violation
     */
    protected final void assertConstraintViolation(String target, String method, String jsonResource, Response.Status status, String... errorMessages) throws IOException {
        Object json = readJsonResource(jsonResource);
        assertConstraintViolation(target, method, json, status, errorMessages);
    }
}
