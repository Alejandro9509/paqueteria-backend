package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformeGuiaRequest {
   String m_sTipoServicio;
   int m_xPeso;
   String m_nFolioGuia;
   int m_xTotal;
   int m_nIdGuia;
   String m_sCiudadDestino;
   String m_sEstatusGuia;
   int m_nIdInforme;
   Boolean select;
}
