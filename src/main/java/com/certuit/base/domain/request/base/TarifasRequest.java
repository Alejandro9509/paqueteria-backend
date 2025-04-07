package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarifasRequest {
    String m_sMoneda;
    String m_sCodigo;
    String m_sSimbolo;
    String m_sAbreviacion;
    int m_nCreadoPor;
}
