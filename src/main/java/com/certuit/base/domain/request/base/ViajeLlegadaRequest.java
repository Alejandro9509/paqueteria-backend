package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViajeLlegadaRequest {
    int m_nIdViajeLlegada;
    int m_nIdViaje;
    int m_nIdRuta;
    float m_nCV1Km;
    float m_nCV2Km;
    float m_nCV1Millas;
    float m_nCV2Millas;
    String m_dFechaSalida;
    String m_tHoraSalida;
    String m_dFechaLlegada;
    String m_tHoraLlegada;
    float m_nKmViaje;
    float m_nMillasViaje;
    String m_sMotivoRetraso;
    int m_nIdCiudadOrigen;
    int m_nIdCiudadDestino;
    float m_nLiquidacion;
    int m_nIdEstatusUnidad;
    int m_nIdTipoCambio;
    int m_nIdEstatusLlegada;
    float m_nPesoCarga;

}