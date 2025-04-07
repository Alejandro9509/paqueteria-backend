package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaRequest {
    private int idFactura;
    private float importe;
    private int idMoneda;
    private String referencia;
    private int idSucursal;
    private int idContraReciboCliente;
    private boolean esFactoraje;
    private float importeCompensacion;
    private boolean documentoConFactoraje;
    private int idCliente;
    private String metodoPago;

    @Override
    public String toString() {
        return
                "<LOOP_ProFacturas>" +
                "<ATT_IdFactura>" +  this.idFactura + "</ATT_IdFactura>" +
                "<ATT_Importe>" + this.importe + "</ATT_Importe>" +
                "<ATT_IdMoneda>" + this.idMoneda + "</ATT_IdMoneda>" +
                "<ATT_Referencia/>" +
                "<ATT_IdSucursal>" + this.idSucursal + "</ATT_IdSucursal>" +
                "<ATT_IdContraReciboCliente>" + this.idContraReciboCliente + "</ATT_IdContraReciboCliente>" +
                "<ATT_EsFactoraje>" + (this.esFactoraje ? 1 : 0) + "</ATT_EsFactoraje>" +
                "<ATT_ImporteCompensacion>" + this.importeCompensacion + "</ATT_ImporteCompensacion>" +
                "<ATT_DocumentoConFactoraje>" + (this.documentoConFactoraje ? 1 : 0) + "</ATT_DocumentoConFactoraje>" +
                "<ATT_IdCliente>" + this.idCliente + "</ATT_IdCliente>" +
                "<ATT_MetodoPago>" + this.metodoPago + "</ATT_MetodoPago>" +
                "</LOOP_ProFacturas>";
    }
}
