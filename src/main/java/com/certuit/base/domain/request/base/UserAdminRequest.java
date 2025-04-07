package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserAdminRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    @NotBlank
    private String phone;
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private Long rol;
    private Long office;
    @NotNull
    private List<Long> departments;

    public UserAdminRequest() {
    }
}
