package com.certuit.base.domain.request.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Getter
@Setter
public class LoginRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    private String deviceId;

    private String webDeviceId;

    @JsonCreator
    public LoginRequest(@JsonProperty("email") String username, @JsonProperty("password") String password, @JsonProperty("deviceId") String deviceId, @JsonProperty("webDeviceId") String webDeviceId) {
        this.email = username;
        this.password = password;
        this.deviceId = deviceId;
        this.webDeviceId = webDeviceId;
    }
}
