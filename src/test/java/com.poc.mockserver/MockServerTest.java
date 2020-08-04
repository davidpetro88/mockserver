package com.poc.mockserver;

import com.poc.mockserver.model.UserDetail;
import com.poc.mockserver.model.UserModel;
import com.poc.mockserver.model.UserResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class MockServerTest {

    private static ClientAndServer mockServer;

    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(1080);
        PathBuilder.login(mockServer);
        PathBuilder.users(mockServer);
    }

    @Test
    public void test_login() {

        UserModel userModel = UserModel.builder().password("usdhuh_82318").username("zancheta").build();

        //a
        Customer customer = new Customer(new RestTemplate());
        UserResponseModel login = customer.login(userModel);

        //Asserts
        assertEquals(userModel.getUsername(), login.getUsername());
    }

    @Test
    public void test_users () {

        Customer customer = new Customer(new RestTemplate());
        List<UserDetail> users = customer.users();

        //Asserts
        assertEquals(PathBuilder.userDetailResponse.size(), users.size());
    }

    @AfterEach
    public void stopMockServer() {
        mockServer.stop();
    }
}
