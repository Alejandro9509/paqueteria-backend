package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaqueteRequest {
    //RECOLECCION
    int m_nIdPaquete; 
    float m_rPeso; 
    float m_rLargo; 
    float m_rAncho;
    float m_rAlto;
    float m_rVolumen;
    int m_nIdTipoEmbalaje;
    float m_cyValorDeclarado;
    String m_sDescripcion;
    float m_nCantidad;
    String m_sObservaciones;
    int m_nIdRecoleccion;
    int m_nIdProducto;
    int m_nIdTipo;
    String m_sClaveSATProducto;
    String m_sClaveSATUnidad;
    String m_nProductoSAT;
    String m_nUnidadSAT;
    String m_sEmbalajeSAT;
    String m_sClaveEmbalaje;

    //EMBARQUE
    float m_cValorDeclarado;
    int m_nIdEmbarque;
    int m_nIdEmbarqueDetalle;
    int m_nIdTIpoEmpaque;
    int m_nTipo;
    float m_xAlto;
    float m_xAncho;
    float m_xLargo;
    float m_xPeso;
    float m_xVolumen;
    float ctd;
    String claveSATProducto;
    String claveSATUnidad;
    String claveEmbalaje;
}
