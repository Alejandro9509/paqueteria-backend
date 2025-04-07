package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TarifaConceptoRequest {
    int idTarifaConcepto;
    int idConceptoFacturacion;
    int idUnidadMedida;
    float minimo = 0;
    float maximo = 0;
    float importe = 0;
    int idTipoCalculo = 0;
    int idViajeLocal;
    int idViajeForaneoGrupo;
    int idTarifa;
    float porcentaje = 0;
    List<TarifaConceptoProductoRequest> productos;
}
