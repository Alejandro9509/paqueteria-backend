package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TarifaRequest {
    int idTarifa;
    String codigo;
    int idCliente;
    int idOrigen;
    float fleteMinimo;
    int creadoPor;
    String creadoEl;
    int tipo;
    List<TarifaViajeRequest> viajes;
}
