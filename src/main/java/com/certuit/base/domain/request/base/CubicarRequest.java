package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CubicarRequest {
    private int idRemolque1;
    private int idRemolque2;
    private List<InformeGuiaRequest> guias;
    private List<PaqueteCubicar> paquetes;
}
