package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntregaOcurreRequest {
    int nIdGuia;
    int m_nIdUsuarioEntregaOcurre;
    String m_sFechaOcurre;
    String m_sHoraOcurre;
    String m_sComentariosOcurre;
    float m_sMontoRecibidoOcurre;
    String m_nIdTipoPago;
    String recibe;
}
