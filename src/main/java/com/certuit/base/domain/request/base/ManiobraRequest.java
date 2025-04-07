package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ManiobraRequest {
    List<TarifaConceptoRequest> conceptos;
}
