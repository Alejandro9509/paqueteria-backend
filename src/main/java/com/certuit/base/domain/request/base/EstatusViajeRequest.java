package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstatusViajeRequest {
    String m_sEstatus;
    String m_sColor;
    int m_sColorLetra;
    String m_sAbreviacion;
    Boolean m_bcarga;
    String m_sCreadoPor;
    int m_sModificadoPor;
    String m_sTipoEstatus;
    Boolean m_barchivo;
    Boolean m_bnoSeguimiento;
}
