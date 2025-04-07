package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProGuiaConceptosRequest {
    Boolean m_bActivo;
    float m_cImporte;
    float m_cImporteIva;
    float m_cImporteRetiene;
    String m_dtCreadoEl;
    String m_dtModificadoEl;
    int m_nCreadoPor;
    int m_nIdConceptosFacturacion;
    int m_nIdGuia;
    int m_nIdGuiaConcepto;
    int m_nIdImpuestoRetiene;
    int m_nIdImpuestoTraslada;
    int m_nModificadoPor;
    float m_cDescuento;
}
