package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CorteCajaRequest {
    int m_nIdCorte;
    int m_nIdSucursal;
    int m_nIdDestino;
    int m_nIdTipoMoneda;
    int m_nIdTipoPago;
    float m_cTotal;
    int m_nIdEstatusCorte;
    String m_sFechaRegistro;
    String m_sHoraRegistro;
    int m_nIdUsuario;
    int m_nIdOperador;
    ArrayList<CorteCajaGuiaRequest> m_arrGuias;
}
