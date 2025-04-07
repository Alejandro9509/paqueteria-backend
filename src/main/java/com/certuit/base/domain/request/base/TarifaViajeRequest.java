package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TarifaViajeRequest {
    int idTarifa;
    int idOrigen;
    float fleteMinimo;
    List<OrigenDestinoRequest> destinos;
    List<ProductoRequest> productos;
}
