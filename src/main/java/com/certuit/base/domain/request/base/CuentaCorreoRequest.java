package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaCorreoRequest {
    int m_nIdCuentasCorreo;
    int m_nTipoCuenta;
    String m_sServidor;
    int m_nPuerto;
    String m_sUsuario;
    String m_sContrasenia;
    int m_nTipoCifrado;
    String m_sCreadoEl;
    String m_sCreadoPor;
    String m_sModificadoEl;
    String m_sModificadoPor;
}
