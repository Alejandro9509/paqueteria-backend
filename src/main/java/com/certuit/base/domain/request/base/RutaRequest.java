package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RutaRequest {
    int idOperador = 0;
    int idUnidad = 0;
    int idRemolque1 = 0;
    int idRemolque2 = 0;
    int idDolly = 0;
    List<GuiaUltimaMilla> guias = new ArrayList<>();
    int m_nIdParadaUltimaMilla = 0;

//  SE USA PARA ORDENAR GUIAS
    List<GuiaUltimaMilla> guiasDescartadasDeRuta = new ArrayList<>();
}
