package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelarRequest {
    private String motivo;
    private int tipo;
    private String fecha;
    private String hora;
    private int usuarioId;
}
