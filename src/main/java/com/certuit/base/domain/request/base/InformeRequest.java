package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InformeRequest {
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
    int m_nIdViaje;
    String m_tHora;
    int m_nCreadoPor;
    int m_nTotalxCDestinatario;
    int m_nTotalxCCobrarRemitente;
    int m_nTotalPagoMostrador;
    int m_nTotalUnidadCompleta;
    int m_nTotalGeneral;
    int m_nIdUnidad;
    List<InformeGuiaRequest> m_arrClsProInformeGuia;
    int m_nDestinoSeleccionado;
    int m_nTipoTimbrado;

}
