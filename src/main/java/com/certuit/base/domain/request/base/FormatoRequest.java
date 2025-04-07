package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormatoRequest {
    String formato;
    int tipoProceso;
    int idUsuario;
    boolean activo;
    String fecha;
    String modificadoEl;
}
