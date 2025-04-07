package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaqueteImportadoRequest {
    int idEmbarque = 0;
    int idEmbalaje = 0;
    String embalaje = "";
    int idProducto = 0;
    String numeroProducto = "";
    float alto = 0;
    float ancho = 0;
    float largo = 0;
    float peso = 0;
    float volumen = 0;
    float cantidad = 0;
    String descripcion = "";
    String observaciones = "";
}
