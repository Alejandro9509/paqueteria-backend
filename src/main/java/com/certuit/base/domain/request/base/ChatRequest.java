package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private String mensaje;
    private int idOperador;
    private int idUsuario;
    private String fechaHora;
    private boolean esOperador;
}
