package com.poc.mockserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.poc.mockserver.model.UserDetail;
import com.poc.mockserver.model.UserModel;
import com.poc.mockserver.model.UserResponseModel;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class PathBuilder {

    static List<UserDetail> userDetailResponse;
    static UserResponseModel userResponse;
    static ObjectMapper objectMapperPath;

    static {
        objectMapperPath = new ObjectMapper().registerModule(new JavaTimeModule());
        try {

            userDetailResponse = List.of(objectMapperPath.readValue(new FileReader("./src/test/resources/users_response.json", Charset.defaultCharset()), UserDetail[].class));
            userResponse = objectMapperPath.readValue(new FileReader("./src/test/resources/user_response.json", Charset.defaultCharset()), UserResponseModel.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static void login(ClientAndServer mockServer) {
        mockServer.when(
                request()
                        .withPath("/login")
        )
                .forward(
                        httpRequest ->
                                request()
                                        .withPath(httpRequest.getPath())
                                        .withMethod("POST")
                                        .withBody(httpRequest.getBody()),
                        (httpRequest, httpResponse) -> {
                            userResponse.setUsername(objectMapperPath.readValue(httpRequest.getBodyAsString(),
                                    UserModel.class).getUsername());
                            return httpResponse
                                    .withContentType(MediaType.APPLICATION_JSON)
                                    .withStatusCode(200)
                                    .withHeader("x-response-test", "x-response-test")
                                    .withBody(objectMapperPath.writeValueAsString(userResponse));
                        }
                );
    }

    public static void users(ClientAndServer mockServer) {
        try {
            mockServer
                    .when(
                            request()
                                    .withMethod("GET")
                                    .withPath("/users")
                    )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withContentType(MediaType.APPLICATION_JSON)
                                    .withBody(objectMapperPath.writeValueAsString(userDetailResponse))
                    );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
