package com.certuit.base.domain.request.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginApp2Request {
    @NotBlank
    String username;

    String password;

    String deviceId;

    int id;

    /*@JsonCreator
    public LoginApp2Request(@JsonProperty("username") String username, @JsonProperty("password") String password,
                            @JsonProperty("deviceId") String deviceId,  @JsonProperty("webDeviceId") int id) {
        System.out.println(username);
        this.username = username;
        this.password = password;
        this.deviceId = deviceId;
        this.id = id;
    }*/
}
