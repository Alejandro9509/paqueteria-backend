package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ZonaOperativaRequest {
    int m_nIdZona;
    String m_sCodigoZona;
    int m_nIdSucursal;
    int m_nIdPais;
    String m_sIdEstado;
    String m_sEstado;
    String m_sCodMunicipio;
    String m_sMunicipio;
    int m_nIdOrigenDestino;
    List<CodigoPostalRequest> m_arrCP;
    boolean m_bAplicaEntrega;
}