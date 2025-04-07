package com.certuit.base.util;

import java.sql.Timestamp;

public class ClsCatConceptosFacturacion {
    public int m_nIdConceptosFacturacion ;
    public String m_sConcepto;
    public String m_sCodigo;
    public Boolean m_bActivo;
    public Boolean m_bCalculoIngreso;
    public Boolean m_bCalculoFlete;
    public int m_nIdUnidadMedidaSAT;
    public String m_sIdUnidadMedidaSAT;
    public int m_nIdProdServSAT;
    public String m_sIdProdServSAT;
    public String m_sClase;
    public String m_sUnidadMedida;
    public Timestamp m_dtCreadoEl;
    public Timestamp m_dtModificadoEl;
    public int m_nCreadoPor;
    public int m_nModificadoPor;
    public ClsCatConceptosFacturacionDetalle[] arClsDetalle;
    public String m_sUltimoError;
    public int m_nN_AGREGAR;
    public int m_nN_MODIFICAR;
    public int m_nN_ELIMINAR;

}
