package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoCobroRequest {
    int codigo;
    String descripcion;
    String idTipoPago;
    boolean bloqueaUltimaMilla;
    boolean solicitaMonto;
    int creadoPor;
    int modificadoPor;
}
