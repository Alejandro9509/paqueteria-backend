package com.certuit.base.domain.request.base;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuiaRequest {
    int m_nIdGuia;
    String m_nTracking;
    int m_nTIpoCambio;
    String m_sFolioGuia;
    String m_dFecha;
    String m_sHora;
    int m_nIdEstatusGuia;
    int m_nIdEmbarque;
    int m_nIdMoneda;
    int m_nCreadoPor;
    int m_nModificadoPor;
    int m_nIdSucursal;
    int m_nValorDeclarado;
    int m_nidTipoServicio;
    String m_sTipoServicio;
    String m_sCorreoDestinatario = "";
    String m_sDomicilioDestinatario = "";
    List<ProGuiaConceptosRequest> arClsGuiaConceptos;
    ArrayList<PaqueteRequest> m_arrPaquetes;

    String m_sLatitudD = "";
    String m_sLongitudD = "";
    int m_nIdZonaOperativa = 0;
    int codigoPostalEntrega = 0;
    int idCiudadEntrega = 0;
    String domicilioEntrega = "";
    String entregarEn = "";
    String datosAdicionales = "";
    String m_nIdEstadoEntrega = "";
    String m_sCodigoMunicipioEntrega = "";
    int idSucursalEntrega = 0;
    String motivoCancelacion;
    String fechaCancelacion;
    String horaCancelacion;
    int idUsuario;

    String m_sLatitud = "";
    String m_sLongitud = "";

    String m_sObservaciones = "";
    String m_sLogo = "";
    String m_sNombreCliente= "";
}
