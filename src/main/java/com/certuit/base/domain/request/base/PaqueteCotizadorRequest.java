package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaqueteCotizadorRequest {
    int idCotizador;
    int idPaquete;
    String embajale;//no se usa
    int tipo;
    String descripcion;//no se usa
    float peso;
    float largo;
    float ancho;
    float alto;
    float volumen;
    int idTipoEmpaque;
    float valorDeclarado;//no se usa
    String observaciones;//no se usa
    Boolean activo;//dudo de su uso
    float ctd;
    int idProducto;
    String producto;
    String guia;
    String claveEmbalaje;
}
