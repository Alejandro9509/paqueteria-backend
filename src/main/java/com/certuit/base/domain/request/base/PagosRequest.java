package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagosRequest {
    private String fechaHora;
    private int idCuentaBancaria;
    private float importe;
    private float tipoCambio;
    private String referenciaBancaria;
    private int idConceptoCobranza;
    private int idCliente;
    private float importeSaldoFavor;
    private int creadoPor;
    private String creadoEl;
    private List<FacturaRequest> facturas;
    private String dsProFacturas;
    private String idPeticion;
    private boolean pagoConContraRecibo; // Bandera de pago con contra recibo 0= Pago normal / 1 = pago con contra recibo
    private boolean cobranzaExterna;
    private int idCertificado;
    private String claveMetodoPago;
    private String rfcEmisorCtaOrd;
    private String nomBancoOrdExt;
    private String ctaOrdenante;
    private String tipoCodPago;
    private String certPago;
    private String cadPago;
    private String selloPago;
    private String fechaCobro;

    @Override
    public String toString() {
        return "'" + this.fechaHora + "', " + this.idCuentaBancaria + ", " + this.importe + ", " + this.tipoCambio
                + ", '" + this.referenciaBancaria + "', " + this.idConceptoCobranza + ", " + this.idCliente
                + ", " + this.importeSaldoFavor + ", " + this.creadoPor + ", '" + this.creadoEl
                + "', '<WINDEV_TABLE>" + getFacturas() + "</WINDEV_TABLE>', '" + this.idPeticion + "', " + (this.pagoConContraRecibo ? 1 : 0)
                + ", " + (this.cobranzaExterna ? 1 : 0) + ", 1, '" + this.claveMetodoPago
                + "', '" + this.rfcEmisorCtaOrd + "', '" + this.nomBancoOrdExt + "', '" + this.ctaOrdenante
                + "', '" + this.tipoCodPago + "', '" + this.certPago + "', '" + this.cadPago + "', '" + this.selloPago
                + "', 0, 1, '', 0, '', 0, 1, NULL, NULL, '" + this.fechaCobro + "', 0, " + (1 / this.tipoCambio) + ", '', ''";
    }

    private String getFacturas() {
        String cadenaXML = "";
        for (FacturaRequest factura : this.facturas) {
            cadenaXML += factura.toString();
        }
        return cadenaXML;
    }
}
