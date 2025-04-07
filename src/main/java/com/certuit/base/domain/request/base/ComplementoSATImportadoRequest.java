package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplementoSATImportadoRequest {
    int idEmbarque;
    int cantidad;
    String claveProductoServicio;
    String claveUnidadMedida;
    String claveFraccionArancelaria;
    String claveMaterialPeligroso;
    boolean esMaterialPeligroso;
    String claveEmbalaje;
    String descripcionEmbalaje;
    String peso;
    String numeroProducto = "";
    boolean esFarmaco=false;
    String claveSectorCofepris="";
    String nombreIngredienteActivo="";
    String nombreQuimico="";
    String denominacionGenericaProd="";
    String denominacionDistintivaProd="";
    String fabricante="";
    String fechaCaducidad="";
    String loteMedicamento="";
    String formaFarmaceutica="";
    String condicionesEspTransp="";
    String registroSanitarioFolioAutorizacion="";
    String numeroCAS="";
    String numRegSanPlagCOFEPRIS="";
    String datosFabricante="";
    String datosFormulador="";
    String datosMaquilador="";
    String usoAutorizado="";
}
