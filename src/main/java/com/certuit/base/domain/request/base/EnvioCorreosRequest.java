package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnvioCorreosRequest {
    String correos;
    Boolean correoDefault;
    String mensaje;
}
