package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class CotizacionRequest {
    int idCotizacion;
    int idRecoleccion;
    int idEmbarque;
    int idOrigen;
    int idDestino;
    int idZonaEntrega;
    int idZonaRecoleccion;
    int idCliente;
    int idTipoTarifa;
    Boolean entregaEnSucursal;
    float valorDeclarado;
    float porcentajeSeguro;
    Boolean aplicaRecoleccion;
    int idSeguro;
    Boolean aplicaSeguro;
    Boolean recoleccionConCita;
    Boolean embarqueConCita;
    List<PaqueteCotizadorRequest> paquetesCotizacion = Collections.emptyList();
}

