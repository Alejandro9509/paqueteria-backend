package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class EmbarqueRequest {
    int m_nIdEmbarque = 0;
    int m_nIdRecoleccion = 0;
    int m_nIdGuia = 0;
    String m_sFolioEmbarque = "";
    String m_sFolioGuia = "";
    String m_sColorEstatus = "";
    String m_sFolioRecoleccion = "";
    String m_nFolioInforme = "";
    String m_dFecha = "";
    String m_tHora = "";
    String m_sHora = "";
    String m_dFechaRegistro = "";
    String m_tHoraRegistro = "";
    int m_nIdEstatusEmbarque = 0;
    int m_nIdMoneda = 0;
    int m_cTIpoCambio = 0;
    int m_nIdTIpoCobro = 0;
    String m_sNOmbreRemitente = "";
    String m_sRFCRemitente = "";
    String m_sDomicilioRemitente = "";
    int m_nIdCodigoPostalRemitente = 0;
    String m_sCodigoPostalRemitente = "";
    int m_nCiudadRemitente = 0;
    String m_sCiudadRemitente = "";
    boolean m_bEsRecolecta = false;
    String m_sCorreoRemitente = "";
    String m_sTelefonoRemitente = "";
    String m_sContactoRemitente = "";
    int m_nIdCiudadOrigen = 0;
    String m_sCiudadOrigen = "";
    String m_sFechaHora = "";
    int m_nIdRemitente = 0;
    String m_sAliasRemitente = "";
    String m_sStringZone = "";

    String m_nIdEstadoRemitente = "";
    String m_nIdEstadoDestinatario = ""; 
    String m_sMunicipioRemitente = "";
    String m_sMunicipioDestinatario = "";

    int m_nIdDestinatario = 0;
    String m_sAliasDestinatario = "";
    String m_sNombreDestinatario = "";
    String m_sRFCDestinatario = "";
    String m_sDomicilioDestinatario = "";
    int m_nIdCodigoPostalDestinatario = 0;
    String m_sCodigoPostalDestinatario = "";
    String m_sCorreoDestinatario = "";
    int m_nIdCIudadDestinatario = 0;
    String m_sCIudadDestinatario = "";
    String m_sTelefonoDestinatario = "";
    String m_sContactoDestinatario = "";
    int m_nIdCiudadDestino = 0;
    String m_sCiudadDestino = "";
    String m_dFechaEntrega = "";
    String m_tHoraEntrega = "";
    int m_nNoPaquetes = 0;
    int m_nNoSobres = 0;
    int m_nIdOperador = 0;
    int m_nIdCiudadRemitente = 0;
    int m_nIdUnidad = 0;
    String m_dFechaSalida = "";
    String m_tHoraSalida = "";
    String m_sSucursal = "";
    String m_sEstatusEmbarque = "";
    String m_dtFechaCancelacion = "";
    String m_sUsuarioCancelacion = "";
    String m_sMotivoCancelacion = "";
    boolean entregarMismoDomicilio = false;
    String fechaLlegada = "";
    String horaLlegada = "";
    int codigoPostalEntrega = 0;
    int idCiudadEntrega = 0;
    int idZonaEntrega = 0;
    String domicilioEntrega = "";
    String entregarEn = "";
    String datosAdicionales = "";
    int idSucursal = 0;
    int creadoPor = 0;
    String m_sNombreCliente = "";
    int m_nIdCliente = 0;
    int m_nIdZonaRemitente = 0;
    int m_nIdZonaDestinatario = 0;

    String m_sCalleRemitente = "";
    String m_sNoIntRemitente = "";
    String m_sNoExtRemitente = "";
    String m_sColoniaRemitente = "";

    String m_sCalleDestinatario = "";
    String m_sNoIntDestinatario = "";
    String m_sNoExtDestinatario = "";
    String m_sColoniaDestinatario = "";

    int m_nIdZonaOperativaRecoleccion = 0;
    int m_nIdZonaTarifaRecoleccion = 0;
    String m_nIdEstadoEntrega = "";
    String m_sCodigoMunicipioEntrega = "";

    int m_nSePuedeCancelar = 0;
    boolean m_bEntregaEnSucursal = false;
    int m_nIdSucursalEntrega = 0;
    ArrayList<PaqueteRequest> m_arrPaquetes;
    ArrayList<PaqueteRequest> m_arrClsDetalle;
    ArrayList<ComplementoSATRequest> m_arrClsComplementoSAT;

    boolean m_bEmbarqueConCita = false;
    String m_sFechaCita = "";
    String m_sHoraCitaMinima = "";
    String m_sHoraCitaMaxima = "";
    boolean m_bCitaPendiente = false;

    boolean m_bRecoleccionConCita = false;

    int m_nIdEmbarqueRelacionado = 0;
    String m_sFolioEmbarqueRelacionado = "";
    int m_nIdGuiaRelacionada = 0;
    String m_sFolioGuiaRelacionada = "";
    String m_sLatitudD = "";
    String m_sLongitudD = "";
    int m_nIdZonaOperativa = 0;
    int m_nIdZonaTarifa = 0;

    //Seguro
    float valorDeclarado = 0;
    int m_nIdTipoSeguro = 0;
    float m_xPorcentajeSeguro = 0;
    boolean m_bAplicaSeguro = false;
    int m_nIdComplemento = 0;
    int m_nIdTipoDocumento = 0;
    boolean m_bValidarTimbradoIngreso = false;

    //Cotizacion
    ArrayList<ConceptoFacturacionRequest> m_arrConceptos;
    int m_nIdCotizacion = 0;

    //Rutas
    int m_nIdRuta = 0;

    //observaciones
    String m_sObservaciones = "";

    int m_nIdUsuario = 0;
    int m_nTipoTimbrado = 0;
    String m_sReferencia = "";

}
