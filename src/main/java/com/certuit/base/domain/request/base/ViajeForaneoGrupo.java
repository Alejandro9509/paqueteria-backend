package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViajeForaneoGrupo {
    int idViajeForaneoGrupo;
    String nombre;
    int idViajeForaneo;
    List<ViajeZonaRequest> zonas;
    List<TarifaConceptoRequest> conceptos;
    List<TarifaProductoRequest> productos;
}
