package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class InformesModificarRequest {
    String m_dFecha;
    String m_sFolioInforme;
    int m_nIdCiudadDestino;
    int m_nIdCiudadOrigen;
    int m_nIdEstatusInforme;
    int m_nIdInforme;
    int m_nIdOperador;
    int m_nIdRemolque1;
    int m_nIdRemolque2;
    String m_sPlacasRemolque1;
    String m_sPlacasRemolque2;
    String m_sPlacasDolly;
    int m_nIdRuta;
    int m_nIdSucursalEmisora;
    int m_nIdSucursalReceptora;
    int m_nIdDolly;
    String m_tHora;
    int m_nCreadoPor;
   ArrayList<InformeGuiaRequest> m_arrClsProInformeGuia;

}
