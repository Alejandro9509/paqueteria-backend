package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConceptoFacturacionImportadoRequest {
    int m_nIdConceptosFacturacion = 0;
    float m_cImporte = 0;
    int m_nIdImpuestoTraslada = 0;
    float m_cImporteIva = 0;
    int m_nIdImpuestoRetiene = 0;
    float m_cImporteRetiene = 0;

    int m_nIdCotizacion = 0;
}
