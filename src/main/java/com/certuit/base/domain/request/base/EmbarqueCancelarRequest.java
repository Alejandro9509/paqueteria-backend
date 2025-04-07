package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbarqueCancelarRequest {
    String motivoCancelacion;
    String fechaCancelacion;
    String usuarioCancelacion;
}
