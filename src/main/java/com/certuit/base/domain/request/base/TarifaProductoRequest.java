package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarifaProductoRequest {
    int idTarifaProducto;
    int idProducto;
    int idViajeLocal;
    int idViajeForaneoGrupo;
}
