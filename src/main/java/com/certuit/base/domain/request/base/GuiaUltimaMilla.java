package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuiaUltimaMilla {
    int idGuia = 0;
    int orden = 0;
    String lat = "";
    String lng = "";
    boolean esRecoleccion = false;
    String horaEstimada = "00:00";
    float kilometros = 0;

    int idNuevaGuia = 0;
    int idEstatus = 0;
    boolean nuevoEsRecoleccion = false;
    int idParadaGuia = 0;
}
