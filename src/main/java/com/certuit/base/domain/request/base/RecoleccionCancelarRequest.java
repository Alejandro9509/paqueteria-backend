package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoleccionCancelarRequest {
   String motivoCancelacion;
   int usuarioCancelacion;
   String fechaCancelacion;
}
