package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorteCajaGuiaRequest {
    int m_nIdGuia;
    int m_nIdCorte;
    float m_nTotal;
}
