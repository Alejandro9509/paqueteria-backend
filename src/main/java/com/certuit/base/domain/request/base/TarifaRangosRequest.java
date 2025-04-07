package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TarifaRangosRequest {
    int idTarifa;
    int idCliente;
    String vigencia;
    float cuotaMensual;
    List<ViajeLocalRequest> viajesLocales;
    List<ViajeForaneoRequest> viajesForaneos;
    List<TarifaConceptoRequest> maniobras;
}

