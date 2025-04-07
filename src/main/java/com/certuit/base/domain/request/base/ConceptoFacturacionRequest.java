package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConceptoFacturacionRequest {
    int m_nIdGuiaConcepto = 0;
    int m_nIdGuia = 0;
    float m_cImporte = 0;
    int m_nIdImpuestoTraslada = 0;
    float m_cImporteIva = 0;
    int m_nIdImpuestoRetiene = 0;
    float m_cImporteRetiene = 0;
    String m_dtCreadoEl = null;
    int m_nCreadoPor = 0;
    String m_dtModificadoEl = null;
    int m_nModificadoPor = 0;
    boolean m_bActivo = false;
    int m_nIdConceptoFacturacion = 0;
    float m_cTotal = 0;
    float m_cPorcentajeRetiene = 0;
    float m_cPorcentajeTraslada = 0;
    String m_sConcepto = "";
    String m_sUnidadMedida = "";
    String m_sClaveSatProducto = "";
    String m_sClaveSatUnidad = "";
    boolean m_bIncluirLiquidacion = false;
    boolean m_bIncluirFlete = false;


    //Justificacion
    String m_sTarifa = "";
    String m_sOrigen = "";
    String m_sDestino = "";
    String m_sConvenio = "";
    String m_sImporte = "";

    float m_c_Descuento = 0;

    int m_nIdCotizacion = 0;
}
