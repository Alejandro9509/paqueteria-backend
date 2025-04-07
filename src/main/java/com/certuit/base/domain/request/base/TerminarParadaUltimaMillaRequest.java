package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminarParadaUltimaMillaRequest {

    boolean m_bEsRecoleccion;
    String m_sMotivo;
    String m_nIdTipoPago;
    String m_sReceptor;
    float m_nMontoRecibido;
    boolean m_bQRValidado;
    String horaEntrega;

}
