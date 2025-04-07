package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JustificacionConceptosRequest {
    int m_nIdConceptoFacturacion;
    int m_nTarifa;
    String m_sOrigen;
    String m_sDestino;
    String m_sCliente;
    Boolean m_bConvenio;
    float m_xImporte;
    float m_xImporteFinal;
    float m_xSeguro;
    float m_xPesoTotal;
    int m_nCantidad;
    float m_xDescuento;
}
