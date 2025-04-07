package com.certuit.base.domain.request.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAppRequest {

    private String username;
    private int id;

    private String deviceId;

    @JsonCreator
    public LoginAppRequest(@JsonProperty("username") String username, @JsonProperty("deviceId") String deviceId) {
        this.username = username;
        this.deviceId = deviceId;
    }


}
