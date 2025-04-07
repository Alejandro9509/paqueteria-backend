package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformesCancelarRequest {
    String motivoCancelacion;
    String fechaCancelacion;
    int usuarioCancelacion;
}
