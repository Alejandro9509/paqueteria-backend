package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViajeSalidaRequest {
    int m_nIdViajeSalida;
    int m_nIdViaje;
    int m_nIdRuta;
    float m_nCV1Km;
    float m_nCV2Km;
    float m_nCV1Millas;
    float m_nCV2Millas;
    boolean m_bCV1Estatus;
    boolean m_bCV2Estatus;
    String m_dFechaSalida;
    String m_tHoraSalida;
    int m_nIdEstatusSalida;
    float m_nKmViaje;
    float m_nMillasViaje;
    String m_sMotivoRetraso;
    int m_nIdCiudadOrigen;
    int m_nIdCiudadDestino;

}
