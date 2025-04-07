package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EtiquetaRequest {
    int idPaquete = 0;
    int idGuia = 0;
    int rangoInicio = 0;
    int rangoFin = 0;
    int idImpresion = 0;
}
