package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ParametrosConfiguracionRequest {
    int estatusRecoleccion;
    int estatusEmbarque;
    int monedaEmbarque;
    int tipoCambioEmbarque;
    int estatusGuia;
    int tipoTarifaTarifas;
    float costoCitaTarifas;
    boolean cobrarCita;
    boolean detectarTipoCobro;
    boolean limpiarProducto;
    int tipoCobro;
    String tiposCobroActivos;
    String correoFacturacionViaje;
    String correoFacturacionUltimaMilla;
    int idConceptoFlete;
    int idConceptoCarga;
    int idConceptoDescarga;
    int idConceptoRecoleccion;
    int idConceptoEntrega;
    int idConceptoSeguro;
    int idConceptoCita;
    boolean validarInforme;
    boolean timbradoPruebaGuia;
    int idComplemento;
    int tipoTimbrado;
    boolean validarTimbrado = false;
    boolean validarTimbradoIngreso = false;
    boolean modificarValorEmbarque = false;
    boolean cobrarConceptoCarga = false;
    boolean cobrarConceptoDescarga = false;
    boolean imprimirEtiquetasIndividuales=false;
    int horasLimiteEntregasUltimaMilla = 24;
    float porcentualSeguroDefecto=0;
    boolean cobrarConceptoManiobras = false;
    int idConceptoManiobras = 0;

    ArrayList<TipoDocumentoRequest> documentos = new ArrayList<>();
}
