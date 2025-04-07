package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class RecoleccionRequest {
    String m_sFolioRecoleccion = "";
    boolean m_bCitaPendiente = false;
    int m_nIdSucursal = 0;
    int m_nIdEmbarque = 0;
    int m_nIdRecoleccion = 0;
    int m_nIdGuia = 0;
    int m_nIdInforme = 0;
    int m_nCreadoPor = 0;
    String m_sFecha = "";
    String m_sHora = "";
    int m_nIdEstatusRecoleccion = 0;
    int m_nMoneda = 0;
    int m_rTipoCambio = 0;
    int m_nIdTipoDeCobro = 0;

    int m_nIdCliente = 0;
    boolean m_bAplicaSeguro = false;
    int m_nIdTipoSeguro = 0;
    float m_xPorcentajeSeguro = 0;
    float valorDeclarado = 0;
    String m_sObservaciones = "";

    int m_nIdRemitente = 0;
    String m_sNombreRemitente = "";
    String m_sAliasRemitente = "";
    String m_sRFCRemitente = "";
    String m_sDomicilioRemitente = "";
    String m_nIdEstadoRemitente = "";
    String m_sMunicipioRemitente = "";
    int m_sIdCodigoPostalRemitente = 0;
    int m_nIdCiudadRemitente = 0;
    String m_sCorreoRemitente = "";
    String m_sTelefonoRemitente = "";
    String m_sContactoRemitente = "";
    String m_sCalleRemitente = "";
    String m_sNoIntRemitente = "";
    String m_sNoExtRemitente = "";
    String m_sColoniaRemitente = "";
    int m_nIdZonaRemitente = 0;
    int m_nIdCiudadOrigen = 0;

    String m_nIdEstadoRecoleccion = "";
    String m_sCodigoMunicipioRecoleccion = "";
    boolean m_bRecoleccionDiferenteDomicilio = false;
    String m_dFechaDetalleRecoleccion = "";
    String m_tHoraDetalleRecoleccion = "";
    int m_nIdCPDetalleRecoleccion = 0;
    int m_nIdCiudadDetalleRecoleccion = 0;
    int m_nIdZonaDetalleRecoleccion = 0;
    String m_sDomicilioDetalleRecoleccion = "";
    String m_sRecogerEnDetalleRecoleccion = "";
    String m_sDatosAdicionalesDetalleRecoleccion = "";

    String m_sLatitudR = "";
    String m_sLongitudR = "";

    int m_nIdZonaOperativa = 0;
    int m_nIdZonaTarifa = 0;

    int m_nIdDestinatario = 0;
    String m_sNombreDestinatario = "";
    String m_sAliasDestinatario = "";
    String m_sRFCDestinatario = "";
    String m_sDomicilioDestinatario = "";
    String m_nIdEstadoDestinatario = "";
    String m_sMunicipioDestinatario = "";
    int m_sIdCodigoPostalDestinatario = 0;
    int m_nIdCiudadDestinatario = 0;
    String m_sCorreoDestinatario = "";
    String m_sTelefonoDestinatario = "";
    String m_sContactoDestinatario = "";
    String m_sCalleDestinatario = "";
    String m_sNoIntDestinatario = "";
    String m_sNoExtDestinatario = "";
    String m_sColoniaDestinatario = "";
    int m_nIdZonaDestinatario = 0;
    int m_nIdCiudadDestino = 0;

    String m_nIdEstadoEntrega = "";
    String m_sCodigoMunicipioEntrega = "";
    boolean m_bEntregaDiferenteDomicilio = false;
    int m_nIdCPDetalleEntrega = 0;
    int m_nIdCiudadDetalleEntrega = 0;
    int m_nIdZonaDetalleEntrega = 0;
    String m_sDomicilioDetalleEntrega = "";
    String m_sEntregarEnDetalleEntrega = "";
    String m_sDatosAdicionalesDetalleEntrega = "";

    boolean m_bEntregaEnSucursal = false;
    int m_nIdSucursalEntrega = 0;

    int m_nIdZonaOperativaEntrega = 0;
    int m_nIdZonaTarifaEntrega = 0;

    boolean m_bRecoleccionConCita = false;
    String m_sFechaCita = "";
    String m_sHoraCitaMinima = "";
    String m_sHoraCitaMaxima = "";
    String m_sReferencia = "";

    ArrayList<PaqueteRequest> m_parrPaquetes;
    //Sobres
    ArrayList<PaqueteRequest> m_parrSobres;
    //Complemento SAT
    ArrayList<ComplementoSATRequest> m_arrClsComplementoSAT;
    ArrayList<ConceptoFacturacionRequest> m_arrConceptos;
    int m_nIdCotizacion = 0;

    int m_nNoPaquetes = 0;
    int m_nNoSobres = 0;
    int m_nIdOperador = 0;
    int m_nIdUnidad = 0;
    int m_nIdRemolque = 0;

    String m_dFechaSalida = "";
    String m_dFechaLlegada = "";
    String m_tHoraSalida = "";
    String m_tHoraLlegada = "";
}
