package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeguroRequest {
    int idTipoSeguro;
    int idCliente;
    float porcentajeSeguro;
    boolean aplicaSeguro;
   String aseguradora;
    String poliza;
}
