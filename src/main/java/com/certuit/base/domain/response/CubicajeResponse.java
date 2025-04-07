package com.certuit.base.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CubicajeResponse {
    private boolean success;
    private String mensaje;
    private float utilizacion;
}
