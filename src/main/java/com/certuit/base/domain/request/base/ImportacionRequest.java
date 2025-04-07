package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ImportacionRequest {
    ArrayList<EmbarqueImportadoRequest> embarques = new ArrayList<>();
}
