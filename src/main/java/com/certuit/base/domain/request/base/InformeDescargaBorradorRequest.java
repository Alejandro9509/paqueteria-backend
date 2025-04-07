package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformeDescargaBorradorRequest {
    private int m_nIdGuia;
    private int m_nIdEmbarqueDetalle;
    private int index;
}
