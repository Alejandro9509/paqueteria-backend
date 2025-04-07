package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoAgregarRequest {
    int m_nNoProducto;
    String m_sDescripcion;
    boolean m_bActivo;
    float m_xLargo;
    float m_xAncho;
    float m_xAlto;
    float m_xPeso;
    int m_nIdEmbalaje;
    String m_sEmbalaje;
    boolean m_bPredeterminado;
}
