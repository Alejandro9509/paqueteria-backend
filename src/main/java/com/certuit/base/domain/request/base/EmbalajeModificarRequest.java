package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbalajeModificarRequest {
    String m_sCodigo;
    String m_sNombre;
    String m_sDescripcion;
    String m_nModificadoPor;
    String m_sCreadoPor;
    int m_nIdEmbalaje;
}
