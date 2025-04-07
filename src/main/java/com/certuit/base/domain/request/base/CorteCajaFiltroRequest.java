package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorteCajaFiltroRequest {
    Boolean busquedaPorUsuario = false;
    int idOperador = 0;
    int idUsuario = 0;
    String fecha = null;
}
