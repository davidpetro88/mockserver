package com.poc.mockserver;


import com.poc.mockserver.model.UserDetail;
import com.poc.mockserver.model.UserModel;
import com.poc.mockserver.model.UserResponseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private RestTemplate restTemplate;

    public UserResponseModel login(UserModel userModel) {
        return restTemplate.postForEntity(
                "http://localhost:1080/login", userModel, UserResponseModel.class).getBody();
    }

    public List<UserDetail> users() {
        return List.of(Objects.requireNonNull(
                restTemplate.getForEntity("http://localhost:1080/users", UserDetail[].class).getBody()));
    }

}
