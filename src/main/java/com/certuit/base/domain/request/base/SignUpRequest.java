package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    private String name;
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    private String deviceId;
    @NotBlank
    private String deviceType;

    @NotBlank
    private String password;

    private String webDeviceId;

}
