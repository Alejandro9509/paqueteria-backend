package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
public class InformesRequets {
    @NotNull
    private int idOrigen;
    @NotNull
    private int idDestino;
    private int idRuta;
}
