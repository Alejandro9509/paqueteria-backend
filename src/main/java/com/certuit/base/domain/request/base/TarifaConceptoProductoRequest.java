package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarifaConceptoProductoRequest {
    int idTarifa;
    int idConceptoFacturacion;
    int idProducto;
}
