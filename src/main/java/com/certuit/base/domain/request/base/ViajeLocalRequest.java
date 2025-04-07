package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViajeLocalRequest {
    int idViajeLocal;
    int idSucursal;
    int idConcepto;
    int idTarifa;
    int idTipoMedida;
    List<ViajeZonaRequest> zonas;
    List<TarifaConceptoRequest> conceptos;
    List<TarifaProductoRequest> productos;
}
