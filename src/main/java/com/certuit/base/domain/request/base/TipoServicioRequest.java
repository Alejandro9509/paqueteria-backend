package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoServicioRequest {
    String m_sDescripcion;
    float m_cCosto;
    int m_nDiashabiles;
    boolean m_bActivo;
    String m_dtCreadoEl;
    int m_nCreadoPor;
    String m_dtModificadoEl;
    int m_nModificadoPor;
    int m_nIdUsuario;
}
