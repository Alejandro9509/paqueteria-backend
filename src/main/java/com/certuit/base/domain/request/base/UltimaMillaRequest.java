package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UltimaMillaRequest {
    String zonas = "";
    int tipoServicio = 0;

    int idUltimaMilla = 0;
    int idSucursal = 0;
    String fecha = "";
    String hora = "";
    int creadoPor = 0;
    List<ZonaOperativaRequest> arrZonas = new ArrayList<>();
    List<RutaRequest> rutas = new ArrayList<>();
}
