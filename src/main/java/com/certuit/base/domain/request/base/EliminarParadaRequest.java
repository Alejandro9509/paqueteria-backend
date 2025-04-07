package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EliminarParadaRequest {
    int idParada;
    int idGuia;
    Boolean esRecoleccion;
}
