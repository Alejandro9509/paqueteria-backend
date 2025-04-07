package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViajeForaneoRequest {
    int idViajeForaneo;
    int idOrigen;
    int idDestino;
    int idTipoMedida;
    int idTarifa;
    float fleteMinimo;
    List<ViajeForaneoGrupo> grupos;
}
