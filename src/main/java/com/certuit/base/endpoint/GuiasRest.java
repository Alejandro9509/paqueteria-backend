package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.EmbarqueService;
import com.certuit.base.service.base.GuiaService;
import com.certuit.base.service.base.ViajesService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class GuiasRest {

    @Autowired
    DBConection dbConection;

    @Autowired
    GuiaService guiaService;

    @Autowired
    EmbarqueService embarqueService;

    @Autowired
    ViajesService viajeService;

    @GetMapping("/Guia/GetById/{idGuia}")
    public ResponseEntity<?> obtenerGuiaById(@PathVariable("idGuia") int idGuia, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "exec usp_GetGuiaByIdPQ " + idGuia;

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            int idEmbarque;
            if (rs.next()) {
                idEmbarque = 0;
                idEmbarque = rs.getInt("IdEmbarque");
                json.put("m_bEntregaEnSucursal", rs.getBoolean("EntregaEnSucursal"));
                json.put("m_bEmbarqueConCita", rs.getBoolean("EmbarqueConCita"));
                json.put("m_bRecoleccionConCita", rs.getBoolean("RecoleccionConCita"));
                json.put("m_nIdEmbarque", idEmbarque);
                json.put("m_sFolioEmbarque", rs.getString("FolioEmbarque"));
                json.put("m_nFolioRecoleccion", rs.getInt("FolioRecoleccion"));
                json.put("m_nFolioGuia", rs.getString("FolioGuia"));
                json.put("m_sFolioInforme", rs.getString("FolioInforme"));
                json.put("m_dFecha", rs.getDate("Fecha"));
                json.put("m_sHora", rs.getString("Hora"));
                json.put("m_sFechaHora", rs.getString("m_sFechaHora"));
                json.put("m_nIdEstatusGuia", rs.getInt("IdEstatusGuia"));
                json.put("m_nIdMoneda", rs.getInt("IdMoneda"));
                json.put("m_cTIpoCambio", rs.getFloat("TIpoCambio"));
                json.put("m_nIdTIpoCobro", rs.getInt("IdTIpoCobro"));
                json.put("m_sNOmbreRemitente", rs.getString("NOmbreRemitente"));
                json.put("m_sRFCRemitente", rs.getString("RFCRemitente"));
                json.put("m_sDomicilioRemitente", rs.getString("DomicilioRemitente"));
                json.put("m_nIdCodigoPostalRemitente", rs.getInt("IdCodigoPostalRemitente"));
                json.put("m_sCodigoPostalRemitente", rs.getString("CodigoPostalRemitente"));
                json.put("m_nCiudadRemitente", rs.getInt("IdCiudadRemitente"));
                json.put("m_sCorreoRemitente", rs.getString("CorreoRemitente"));
                json.put("m_sTelefonoRemitente", rs.getString("TelefonoRemitente"));
                json.put("m_sContactoRemitente", rs.getString("ContactoRemitente"));
                json.put("m_nIdCiudadOrigen", rs.getInt("IdCiudadOrigen"));
                json.put("m_sNombreDestinatario", rs.getString("NombreDestinatario"));
                json.put("m_sRFCDestinatario", rs.getString("RFCDestinatario"));
                json.put("m_sDomicilioDestinatario", rs.getString("DomicilioDestinatario"));
                json.put("m_nIdCodigoPostalDestinatario", rs.getInt("IdCodigoPostalDestinatario"));
                json.put("m_sCodigoPostalDestinatario", rs.getString("CodigoPostalDestinatario"));
                json.put("m_nIdCIudadDestinatario", rs.getInt("IdCIudadDestinatario"));
                json.put("m_sCorreoDestinatario", rs.getString("CorreoDestinatario"));
                json.put("m_sTelefonoDestinatario", rs.getString("TelefonoDestinatario"));
                json.put("m_sContactoDestinatario", rs.getString("ContactoDestinatario"));
                json.put("m_nIdCiudadDestino", rs.getInt("IdCiudadDestino"));
                json.put("m_dFechaEntrega", rs.getDate("FechaEntrega"));
                json.put("m_tHoraEntrega", rs.getTime("HoraEntrega"));
                json.put("m_nNoPaquetes", rs.getInt("NoPaquetes"));
                json.put("m_nNoSobres", rs.getInt("NoSobres"));
                json.put("m_nIdOperador", rs.getInt("IdOperador"));
                json.put("m_nIdUnidad", rs.getInt("IdUnidad"));
                json.put("m_dFechaSalida", rs.getDate("FechaSalida"));
                json.put("m_tHoraSalida", rs.getTime("HoraSalida"));
                json.put("m_dtFechaCancelacion", rs.getDate("FechaCancelacion"));
                json.put("m_nUsuarioCancelacion", rs.getInt("UsuarioCancelacion"));
                json.put("m_sMotivoCancelacion", rs.getString("MotivoCancelacion"));
                json.put("m_bEntregarMismoDomicilio", rs.getBoolean("EntregarMismoDomicilio"));
                json.put("m_dFechaLlegada", rs.getDate("FechaLlegada"));
                json.put("m_tHoraLlegada", rs.getTime("HoraLlegada"));
                json.put("m_nCodigoPostalEntrega", rs.getInt("CodigoPostalEntrega"));
                json.put("m_nIdCiudadEntrega", rs.getInt("IdCiudadEntrega"));
                json.put("m_nIdZonaEntrega", rs.getInt("IdZonaEntrega"));
                json.put("m_nIdSucursalDestino", rs.getInt("idSucursalDestino"));
                json.put("m_sSucursalDestino", rs.getString("sucursalDestino"));
                json.put("m_nIdSucursalOrigen", rs.getInt("idSucursalOrigen"));
                json.put("m_sSucursalorigen", rs.getString("sucursalOrigen"));
                json.put("m_sDomicilioEntrega", rs.getString("DomicilioEntrega"));
                json.put("m_sEntregarEn", rs.getString("EntregarEn"));
                json.put("m_sDatosAdicionalesis", rs.getString("DatosAdicionales"));
                json.put("m_nTracking", rs.getString("Tracking"));
                json.put("m_nIdGuia", rs.getInt("IdGuia"));
                json.put("IdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sSucursal", rs.getString("Sucursal"));
                json.put("m_sEstatusGuia", rs.getString("EstatusGuia"));
                json.put("m_sCiudadOrigen", rs.getString("CiudadOrigen"));
                json.put("m_sCiudadDestino", rs.getString("CiudadDestino"));
                json.put("m_nIdTipoServicio", rs.getInt("IdTipoServicio"));
                json.put("m_sTipoServicio", rs.getString("TipoServicio"));
                json.put("m_cValorDeclarado", rs.getFloat("ValorDeclarado"));
                json.put("m_xPorcentajeSeguro", rs.getInt("PorcentajeSeguro"));
                json.put("m_bQrValidado", rs.getBoolean("qr"));
                json.put("m_bPagado", rs.getBoolean("pagado"));
                json.put("m_nUltimaMillaOrden", rs.getInt("ultimaMillaOrden"));
                json.put("m_sCiudadRemitente", rs.getString("CiudadRemitente"));
                json.put("m_sCiudadDestinatario", rs.getString("CiudadDestinatario"));
                json.put("m_sFolioGuiaRelacionada", rs.getString("FolioGuiaRelacionado"));
                json.put("m_sCalleRemitente", rs.getString("CalleRemitente"));
                json.put("m_sNoIntRemitente", rs.getString("NoIntRemitente"));
                json.put("m_sNoExtRemitente", rs.getString("NoExtRemitente"));
                json.put("m_sColoniaRemitente", rs.getString("ColoniaRemitente"));
                json.put("m_sCalleDestinatario", rs.getString("CalleDestinatario"));
                json.put("m_sNoIntDestinatario", rs.getString("NoIntDestinatario"));
                json.put("m_sNoExtDestinatario", rs.getString("NoExtDestinatario"));
                json.put("m_sColoniaDestinatario", rs.getString("ColoniaDestinatario"));
                json.put("m_sFechaOcurre", rs.getString("FechaOcurre"));
                json.put("m_sHoraOcurre", rs.getString("HoraOcurre"));
                json.put("m_nIdUsuarioEntregaOcurre", rs.getInt("IdUsuarioEntregaOcurre"));
                json.put("m_sMontoRecibidoOcurre", rs.getString("MontoRecibidoOcurre"));
                json.put("m_sComentariosOcurre", rs.getString("ComentariosOcurre"));
                json.put("m_sCliente", rs.getString("Cliente"));
                json.put("m_nIdCliente", rs.getInt("IdCliente"));
                json.put("m_sReceptorGuia", rs.getString("m_sReceptorGuia"));
//                json.put("operadorEntrega", rs.getString("operadorEntrega"));
                json.put("m_sReferencia", rs.getString("m_sReferencia"));
                json.put("zonaEntrega", rs.getString("zonaEntrega"));
                json.put("tipoEntrega", rs.getString("tipoEntrega"));
                json.put("m_sLogoEtiqueta", rs.getString("LogoEtiqueta"));
                json.put("m_sObservaciones", rs.getString("m_sObservaciones"));
                if (idGuia > 0) {
                    json.put("m_arClsGuiaConceptos", guiaService.getListadoByGuiaId(idGuia, jdbcConnection));
                    json.put("m_arrClsDetalle", embarqueService.getListadoByIdEmabrque(idEmbarque, jdbcConnection));
                }

            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guias/GetByFiltro/{sFechaInicial}/{sFechaFinal}/{sSucursal}/{sEstatus}/{folio}/{origen}/{destino}/{cliente}")
    public ResponseEntity<?> getListdoGuiasFiltro(@PathVariable("sFechaInicial") String fechaInicial,
                                                  @PathVariable("sFechaFinal") String fechaFinal,
                                                  @PathVariable("sSucursal") int sucursal,
                                                  @PathVariable("sEstatus") int estatus,
                                                  @PathVariable("folio") String folio,
                                                  @PathVariable("origen") int origen,
                                                  @PathVariable("destino") int destino,
                                                  @PathVariable("cliente") int cliente,
                                                  @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                fechaInicial = !fechaInicial.equalsIgnoreCase("0") ? "'" + fechaInicial + "'" : null;
                fechaFinal = !fechaFinal.equalsIgnoreCase("0") ? "'" + fechaFinal + "'" : null;
                folio = !folio.equalsIgnoreCase("0") ? folio : null;
                if (folio != null) {
                    folio = folio.toUpperCase();
                }
                String query = "SELECT FORMAT(g.Fecha, 'dd-MM-yyyy') + ' ' + CONVERT(CHAR(5),CONVERT(TIME(0),g.Hora)) AS m_sFechaHora             \n" +
                        "     , g.IdEstatusGuia AS m_nIdEstatusGuia                \n" +
                        "     , cee.Estatus AS m_sEstatusGuia                \n" +
                        "     , cee.Color AS m_sColorEstatus             \n" +
                        "     , e.IdCiudadOrigen AS m_nIdCiudadOrigen                \n" +
                        "     , (SELECT OrigenDestino FROM CatOrigenesDestinos c WHERE c.IdOrigenDestino = e.IdCiudadOrigen) AS m_sCiudadOrigen             \n" +
                        "     , e.IdCiudadDestino AS m_nIdCiudadDestino                \n" +
                        "     , (SELECT OrigenDestino FROM CatOrigenesDestinos c WHERE c.IdOrigenDestino = e.IdCiudadDestino) AS m_sCiudadDestino             \n" +
                        "     , g.IdGuia AS m_nIdGuia                \n" +
                        "     , g.FolioGuia AS m_nFolioGuia             \n" +
                        "     , g.IdEmbarque AS m_nIdEmbarque                \n" +
                        "     , e.FolioEmbarque AS m_sFolioEmbarque\n" +
                        "     , e.EntregaEnSucursal AS m_bEntregaEnSucursal\n" +
                        "     , e.IdCliente AS m_nIdCliente                \n" +
                        "     , (SELECT c.NombreFiscal FROM CatClientes c WHERE c.IdCliente = e.IdCliente) AS m_sCliente             \n" +
                        "     , g.IdSucursal AS IdSucursal                \n" +
                        "     , cs.Sucursal AS m_sSucursal             \n" +
                        "     , (SELECT FolioInforme FROM ProInformePQ r WHERE r.IdInforme = g.IdInforme) AS m_sFolioInforme             \n" +
                        "     , g.FechaCancelacion AS m_dtFechaCancelacion             \n" +
                        "     , g.HoraCancelacion AS m_sHoraCancelacion             \n" +
                        "     , e.NombreDestinatario AS m_sNombreDestinatario             \n" +
                        "     , e.DomicilioDestinatario AS m_sDomicilioDestinatario             \n" +
                        "     , e.CalleDestinatario AS m_sCalleDestinatario             \n" +
                        "     , e.ColoniaDestinatario AS m_sColoniaDestinatario\n" +
                        "     , (SELECT CodigoPostal\n" +
                        "       FROM CatCodigosPostales cc WHERE cc.IdCodigoPostal = e.IdCodigoPostalDestinatario) AS m_sCodigoPostalDestinatario              \n" +
                        "     , g.Latitud AS m_sLatitud\n" +
                        "     , g.Longitud AS  m_sLongitud\n" +
                        "     , (SELECT Nombre FROM CatUsuarios WHERE IdUsuario = g.UsuarioCancelacion) AS m_sUsuarioCancelacion\n" +
                        "     , (CASE WHEN c.SaldoCredito > 0 OR c.DiasCredito <= 0 THEN 1 ELSE 0 END) AS m_bCreditoVencido\n" +
                        "     , (CASE WHEN c.SaldoCredito > 0 AND c.DiasCredito > 0 THEN 1 ELSE 0 END) AS m_bCreditoDisponible\n" +
                        "     , (CASE WHEN c.SaldoCredito = 0 AND c.DiasCredito = 0 THEN 1 ELSE 0 END) AS m_bSinCredito\n" +
                        "     , c.DiasCredito AS m_nDiasCredito\n" +
                        "     , us.Nombre AS m_sUsuarioDocumento\n" +
                        "     , g.Tracking AS m_sTracking\n" +
                        "     , (SELECT tc.Descripcion FROM CatTipoCobroPQ tc WHERE tc.IdTipoCobro = e.IdTipoCobro) AS m_sTipoCobro\n" +
                        "     , (SELECT tc.Descripcion FROM CatTipoCobroPQ tc WHERE tc.IdTipoCobro = e.IdTipoCobroInicial) AS m_sTipoCobroInicial\n" +
                        "     , e.IdTipoCobro AS m_nIdTipoCobro\n" +
                        "     ,(SELECT SUM(pgc.Importe - pgc.ImporteRetiene - pgc.Descuento) \n" +
                        "       FROM ProGuiaConceptoPQ pgc WHERE pgc.IdGuia = g.IdGuia) AS m_cSubtotal        \n" +
                        "     , (SELECT SUM(pgc.Total) FROM ProGuiaConceptoPQ pgc WHERE pgc.IdGuia = g.IdGuia) AS m_cTotal\n" +
                        "     , (SELECT SUM(pgc.Importe) FROM ProGuiaConceptoPQ pgc WHERE pgc.IdGuia = g.IdGuia) AS m_cImporte\n" +
                        "     , (SELECT SUM(pgc.ImporteIva) FROM ProGuiaConceptoPQ pgc WHERE pgc.IdGuia = g.IdGuia) AS m_cImporteIva\n" +
                        "     , (SELECT SUM(pgc.ImporteRetiene) FROM ProGuiaConceptoPQ pgc WHERE pgc.IdGuia = g.IdGuia) AS m_cImporteRetiene\n" +
                        "     , (SELECT SUM(pgc.Descuento) FROM ProGuiaConceptoPQ pgc WHERE pgc.IdGuia = g.IdGuia) AS m_cDescuento\n" +
                        "     , e.EntregaEnSucursal\n" +
                        "     , rem.Nombre AS m_sRemitente\n" +
                        "     , des.Nombre AS m_sDestinatario\n" +
                        "     , e.ValorDeclarado AS m_nValorDeclarado\n" +
                        "     , e.Observaciones AS m_sObservaciones\n" +
                        "     , e.Referencia AS m_sReferencia\n" +
                        "     , IIF(fac.FolioFiscalUUID IS NULL,0,1) isFacturaTimbrada\n" +
                        "     , (SELECT ROUND(SUM(ProEmbarqueDetallePQ.ctd),2)\n" +
                        "       FROM ProEmbarqueDetallePQ WHERE ProEmbarqueDetallePQ.IdEmbarque = g.IdEmbarque) AS m_nCajas\n" +
                        "     , ISNULL(\n" +
                        "        (SELECT TOP 1 ViajesCartasPorte.DocumentoFacturaCartaPorte \n" +
                        "         FROM (SELECT pv.IdViaje, pv.IdGuia, (SELECT TOP 1\n" +
                        "                                                  CASE cf.Serie \n" +
                        "                                                      WHEN '' THEN ctd.Documento + ' ' \n" +
                        "                                                                       + SUBSTRING('000000000000',1,12 - LEN(CAST(cf.Folio AS VARCHAR))) \n" +
                        "                                                                       + CAST(cf.Folio AS VARCHAR)\n" +
                        "                                                      ELSE ctd.Documento + ' ' \n" +
                        "                                                               + SUBSTRING('000000000000',1,12 - LEN(CAST(cf.Folio AS VARCHAR))) \n" +
                        "                                                               + CAST(cf.Folio AS VARCHAR) + '-' + cf.Serie \n" +
                        "                                                      END AS DocumentoFacturaCartaPorte\n" +
                        "                                              FROM ProFacturas AS pf\n" +
                        "                                                  INNER JOIN CatFolios AS cf ON pf.IdFolio = cf.IdFolio\n" +
                        "                                                  INNER JOIN CatTiposDocumentos AS ctd ON cf.IdTipoDocumento = ctd.IdTipoDocumento\n" +
                        "                                                  INNER JOIN ProViajesFacturas AS pvf ON pf.IdFactura = pvf.IdFactura\n" +
                        "                                              WHERE pvf.IdViaje = pv.IdViaje AND pvf.CanceladoEl IS NULL) AS DocumentoFacturaCartaPorte\n" +
                        "               FROM ProViajes AS pv\n" +
                        "                   INNER JOIN CatSucursales AS cs ON pv.IdSucursal = cs.IdSucursal\n" +
                        "                   INNER JOIN CatRutas AS cr ON pv.IdRuta = cr.IdRuta\n" +
                        "                   INNER JOIN CatOrigenesDestinos AS co ON cr.IdOrigen = co.IdOrigenDestino\n" +
                        "                   INNER JOIN CatOrigenesDestinos AS cd ON cr.IdDestino = cd.IdOrigenDestino\n" +
                        "                   ) AS ViajesCartasPorte\n" +
                        "              WHERE ViajesCartasPorte.IdGuia = g.IdGuia),'') AS m_sFactura\n" +
                        "     , CONCAT(cs.Abreviacion, '-',PV.Viaje) AS FolioERP\n" +
                        "     , ISNULL(CAST((SELECT TOP 1\n" +
                        "                       milla.m_dFecha\n" +
                        "                   FROM ProParadaUltimaMillaGuiasPQ guia\n" +
                        "                            INNER JOIN ParadaUltimaMillaPQ ruta ON ruta.m_nIdProParadaUltimaMilla = guia.m_nIdParadaUltimaMilla\n" +
                        "                            INNER JOIN ProUltimaMillaPQ milla ON milla.m_nIdUltimaMilla = ruta.m_nIdUltimaMilla\n" +
                        "                   WHERE guia.m_nIdGuia = g.IdGuia\n" +
                        "                   order by guia.IdParadaGuia desc) AS VARCHAR), 'NO ENVIADA') AS m_sFechaUltimaMilla\n" +
                        "FROM ProGuiaPQ g\n" +
                        "    INNER JOIN ProEmbarquePQ e ON g.IdEmbarque = e.IdEmbarque\n" +
                        "    INNER JOIN CatClientes c ON c.IdCliente = e.IdCliente\n" +
                        "    INNER JOIN CatEstatusGuiasPQ cee ON cee.IdEstatusGuias = g.IdEstatusGuia\n" +
                        "    INNER JOIN CatSucursales cs ON cs.IdSucursal = e.IdSucursal\n" +
                        "    INNER JOIN CatRemitentesDestinatarios rem ON rem.IdRemitenteDestinatario = e.IdRemitente\n" +
                        "    INNER JOIN CatRemitentesDestinatarios des ON des.IdRemitenteDestinatario = e.IdDestinatario\n" +
                        "    LEFT JOIN ProViajes PV ON g.IdGuia = PV.IdGuia\n" +
                        "    LEFT JOIN ProViajesFacturas AS pvf ON PV.IdViaje = pvf.IdViaje AND pvf.CanceladoPor IS NULL\n" +
                        "    LEFT JOIN ProFacturas fac ON fac.IdFactura=pvf.IdFactura\n" +
                        "    LEFT JOIN CatUsuarios us ON us.IdUsuario = g.IdUsuario\n" +
                        "WHERE (g.FolioGuia LIKE '%" + folio + "%') OR ((g.IdSucursal = " + sucursal + " OR " + sucursal + " = 0)\n" +
                        "           AND (g.IdEstatusGuia = " + estatus + " OR " + estatus + " = 0)\n" +
                        "           AND (g.Fecha >= " + fechaInicial + " OR " + fechaInicial + " IS NULL)\n" +
                        "           AND (g.Fecha <= " + fechaFinal + " OR " + fechaFinal + " IS NULL)\n" +
                        "           AND (e.IdCliente = " + cliente +" OR " + cliente + " = 0)\n" +
                        "           AND (e.IdCiudadOrigen = " + origen + " OR " + origen + " = 0)\n" +
                        "           AND (e.IdCiudadDestino = " + destino + " OR " + destino + " = 0)\n" +
                        "        AND " + "'" + folio + "'" + " = 'null' )\n" +
                        "ORDER BY g.Fecha DESC, g.Hora DESC";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                String jsonArray = UtilFuctions.convertArray(rs).toString();

                return ResponseEntity.ok(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    //    TIPOS DE BUSUEDA/SERVICIO = 3: ULTIMA MILLA
//  BUSQUEDA 3 = RETORNA LISTADO DE GUIAS Y RECOLECCIONES QUE CORRESPONDAN A UNA DE LAS ZONAS DADAS
    @PostMapping("/Guias/GetUltimaMilla")
    public ResponseEntity<?> getListdoUltimaMilla(@RequestBody UltimaMillaRequest request,
                                                  @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = " EXEC usp_ProUltimaMillaGetGuiasByZonaPQ '" + request.getZonas() + "', " + request.getTipoServicio();
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guias/ValidacionById/{id}")
    public ResponseEntity<?> getListadoGuiasFiltro(@PathVariable("id") int id,
                                                   @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "";
                JSONObject json = new JSONObject();
                //Confirmo que el el query no retorne filas vacias
        /*String query = "SELECT COUNT(*) as valor FROM ProViajes prv \n" +
                "inner join ProViajesFacturas pvf on prv.IdViaje=pvf.IdViaje\n" +
                "inner join ProFacturas pf on pf.IdFactura=pvf.IdFactura \n" +
                "inner join CatFolios catf on catf.IdFolio=pf.IdFolio  WHERE  pf.Estatus not in (2) and pf.CanceladoEl is null and prv.IdGuia="+id+"";*/

        /*String query = "SELECT IIF(COUNT(*) > 0, CAST(1 AS BIT), CAST(0 AS BIT)) as tieneViajeFacturadoERP FROM ProViajes prv \n" +
                "inner join ProViajesFacturas pvf on prv.IdViaje=pvf.IdViaje\n" +
                "inner join ProFacturas pf on pf.IdFactura=pvf.IdFactura \n" +
                "inner join CatFolios catf on catf.IdFolio=pf.IdFolio  WHERE  pf.Estatus not in (2) and pf.CanceladoEl is null and prv.IdGuia="+id+"";
        //Retorna los valores de la guia facturada
        String query2 = "select catf.Serie,catf.Folio,pf.Estatus,pf.CanceladoEl ,prv.IdGuia\n" +
                "from ProViajes prv \n" +
                "inner join ProViajesFacturas pvf on prv.IdViaje=pvf.IdViaje\n" +
                "inner join ProFacturas pf on pf.IdFactura=pvf.IdFactura \n" +
                "inner join CatFolios catf on catf.IdFolio=pf.IdFolio \n" +
                "where pf.Estatus not in (2) and pf.CanceladoEl is null and prv.IdGuia="+id+"";
        Statement statement = jdbcConnection.createStatement();
        Statement statement2 = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSet rs2 = statement2.executeQuery(query2);
        boolean esEditable = true;
        JSONObject json = new JSONObject();
        while(rs.next()){
            json.put("esEditable",!rs.getBoolean("tieneViajeFacturadoERP"));
            esEditable = !rs.getBoolean("tieneViajeFacturadoERP");
        }
        if(!esEditable) {
            json.put("valores",UtilFuctions.convertObject(rs2));
            return ResponseEntity.ok(json.toString());
        }*/
//            boolean esEditable = true;
//            query = "Select IIF(COUNT(*) > 0, CAST(1 AS BIT), CAST(0 AS BIT)) as tieneViajeActivoERP \n" +
//                    "                    From ProViajes \n" +
//                    "                    WHERE IdGuia = "+id+"\n" +
//                    "                        and CanceladoEl is null";
//            ResultSet rs = statement.executeQuery(query);
//            while(rs.next()){
//                json.put("esEditable",!rs.getBoolean("tieneViajeActivoERP"));
//                esEditable = !rs.getBoolean("tieneViajeActivoERP");
//            }
//            if (!esEditable){
//                json.put("motivo","Esta guía tiene relacionado un viaje en el módulo de Tráfico, deberá de cancelarlo para poder modificar esta guía");
//            }
                json.put("esEditable", true);

                return ResponseEntity.ok(json.toString());
            } catch (Exception err) {
                err.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al validar la guía. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guias/GetDetalles/{id}")
    public ResponseEntity<?> getComplementos(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select " +
                    "PG.Tracking, " +
                    "PG.FolioGuia," +
                    "PG.Fecha FechaCreacionGuia," +
                    "PG.ValorDeclarado," +
                    "PE.FolioEmbarque," +
                    "ctc.NumeroCliente," +
                    "ctc.RFC RFCCliente," +
                    "ctc.NombreFiscal NombreCliente," +
                    "concat(ctc.Calle,' ',ctc.NoExterior,' ', ctc.Colonia, ctc.CodigoPostal,ctc.Municipio) " +
                    "as DomicilioCliente," +
                    "PE.NombreRemitente," +
                    "PE.RFCRemitente," +
                    "PE.DomicilioRemitente," +
                    "PE.ContactoRemitente," +
                    "PE.TelefonoRemitente," +
                    "ccpostalrem.Municipio MunicipioRemitente," +
                    "ccpostalrem.CodigoPostal CodigoPostalRemiente, " +
                    "PE.NombreDestinatario," +
                    "PE.RFCDestinatario," +
                    "PE.DomicilioDestinatario," +
                    "PE.ContactoDestinatario," +
                    "PE.TelefonoDestinatario," +
                    "ccpostaldest.Municipio MunicipioDestinatario, " +
                    "ccpostaldest.CodigoPostal CodigoPostalDestinatario," +
                    "PE.EntregaEnSucursal," +
                    "PE.EntregarMismoDomicilio," +
                    "PE.DomicilioEntrega," +
                    "PE.EntregarEn," +
                    "PE.DatosAdicionales," +
                    "PR.FolioRecoleccion," +
                    "PR.RecoleccionDiferenteDomicilio," +
                    "PR.DomicilioDetalleRecoleccion," +
                    "PR.RecogerEnDetalleRecoleccion," +
                    "PR.DatosAdicionalesDetalleRecoleccion," +
                    "(select OrigenDestino from CatOrigenesDestinos catod " +
                    "where catod.IdOrigenDestino = PE.IdCiudadOrigen) as CiudadOrigen," +
                    "(select OrigenDestino from CatOrigenesDestinos catod " +
                    "where catod.IdOrigenDestino = PE.IdCiudadDestino) as CiudadDestino," +
                    "ctco.Descripcion TipoCobro," +
                    "cats.Sucursal," +
                    "concat(convert(varchar(10),catf.Folio),'-',catf.Serie) FolioFactura," +
                    "catseguro.Descripcion TipoSeguro," +
                    "PE.PorcentajeSeguro," +
                    "czpqr.CodigoZona ZonaOperativaRecoleccion," +
                    "cztqr.CodigoZona ZonaTarifaRecoleccion," +
                    "czpqe.CodigoZona ZonaOperativaEntrega," +
                    "cztqe.CodigoZona ZonaTarifaEntrega " +
                    "from ProGuiaPQ PG  " +
                    "inner join ProEmbarquePQ PE on PG.IdEmbarque = PE.IdEmbarque " +
                    "inner join CatClientes ctc on ctc.IdCliente=PE.IdCliente " +
                    "left join CatCodigosPostales ccpostalrem " +
                    "on ccpostalrem.IdCodigoPostal=PE.IdCodigoPostalRemitente " +
                    "left join CatCodigosPostales ccpostaldest " +
                    "on ccpostaldest.IdCodigoPostal=PE.IdCodigoPostalDestinatario " +
                    "left join ProRecoleccionPQ PR on PE.IdRecoleccion=PR.IdRecoleccion " +
                    "left Join CatTipoCobroPQ ctco on ctco.IdTipoCobro=PE.IdTipoCobro " +
                    "left join CatSucursales cats on cats.IdSucursal=PE.IdSucursal " +
                    "left join CatTipoSeguro catseguro on catseguro.IdTipoSeguro=PE.IdTipoSeguro " +
                    "left join CatZonasOperativasPQ czpqr on czpqr.IdZona=PR.IdZonaOperativa " +
                    "left join CatZonasTarifasPQ cztqr on cztqr.IdZona=PR.IdZonaTarifa " +
                    "left join CatZonasOperativasPQ czpqe on czpqe.IdZona=PE.IdZonaOperativa " +
                    "left join CatZonasTarifasPQ cztqe on cztqe.IdZona=PE.IdZonaTarifa " +
                    "left join ProViajes prv on prv.IdGuia=PG.IdGuia " +
                    "left join ProViajesFacturas pvf on prv.IdViaje=pvf.IdViaje " +
                    "left join ProFacturas pf on pf.IdFactura=pvf.IdFactura " +
                    "left join CatFolios catf on catf.IdFolio=pf.IdFolio  " +
                    "where PG.IdGuia= " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /*@GetMapping("/Guias/GetDetallesPaquetes/{id}")
    public ResponseEntity<?> getComplementosPaquetes(@PathVariable("id") int id,@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        String query = "select " +
                "PED.ctd," +
                "CTE.Nombre Embalaje," +
                "PED.Descripcion," +
                "PED.Peso," +
                "PED.Largo," +
                "PED.Ancho," +
                "PED.Alto, " +
                "catp.NoProducto, " +
                "catp.Descripcion Producto, " +
                "(case when PED.Tipo = 1 then 'SOBRE' ELSE 'PAQUETE' end) Tipo, " +
                "PED.IdEmbarqueDetalle, " +
                "(isnull(PED.Largo, 1) * isnull(PED.Ancho, 1) * isnull(PED.Alto, 1) * isnull(PED.ctd, 1) *0.0005) PesoVolumetrico," +
                "(isnull(PED.Peso, 1) * isnull(PED.ctd, 1)) PesoReal "+
                "from ProEmbarquePQ PE  " +
                "inner join ProEmbarqueDetallePQ PED on PED.IdEmbarque=PE.IdEmbarque " +
                "inner join CatEmbalajesPQ  CTE on CTE.IdEmbalaje=PED.IdTipoEmpaque " +
                "inner join CatProductosPQ catp on catp.IdProducto = PED.IdProducto " +
                "inner join ProGuiaPQ PG on PG.IdEmbarque = PE.IdEmbarque " +
                "where PG.IdGuia="+id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return ResponseEntity.ok(UtilFuctions.convertArray(rs).toString());
    }*/

    @GetMapping("/Guias/BitacoraPaquete/{idGuia}")
    public ResponseEntity<?> getFechaHora(@PathVariable("idGuia") int idGuia,
                                          @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "Select Fecha,Hora from ProGuiaPQ where IdGuia=" + idGuia;
            String validacionFechaLlegada = "Select COUNT(*) as valor from ProViajesPQ pviaje\n" +
                    "\n" +
                    "inner join ProViajeDetalleParadasPQ pviajeparada on pviajeparada.IdViaje=pviaje.IdViaje\n" +
                    "\n" +
                    "inner join ProInformePQ pinf on pinf.IdInforme=pviajeparada.IdInforme \n" +
                    "\n" +
                    "inner join ProInformeGuiaPQ pinfguia on pinfguia.IdInforme=pinf.IdInforme \n" +
                    "\n" +
                    "where pinfguia.IdGuia=" + idGuia;
            String query2 = "Select pviaje.FechaLLegada from ProViajesPQ pviaje\n" +
                    "\n" +
                    "inner join ProViajeDetalleParadasPQ pviajeparada on pviajeparada.IdViaje=pviaje.IdViaje\n" +
                    "\n" +
                    "inner join ProInformePQ pinf on pinf.IdInforme=pviajeparada.IdInforme \n" +
                    "\n" +
                    "inner join ProInformeGuiaPQ pinfguia on pinfguia.IdInforme=pinf.IdInforme \n" +
                    "\n" +
                    "where pinfguia.IdGuia=" + idGuia;

            String query3 = "Select pviaje.FechaSalida from ProViajesPQ pviaje\n" +
                    "\n" +
                    "inner join ProViajeDetalleParadasPQ pviajeparada on pviajeparada.IdViaje=pviaje.IdViaje\n" +
                    "\n" +
                    "inner join ProInformePQ pinf on pinf.IdInforme=pviajeparada.IdInforme \n" +
                    "\n" +
                    "inner join ProInformeGuiaPQ pinfguia on pinfguia.IdInforme=pinf.IdInforme \n" +
                    "\n" +
                    "where pinfguia.IdGuia=" + idGuia;

            Statement statement = jdbcConnection.createStatement();
            Statement statement2 = jdbcConnection.createStatement();
            Statement statement3 = jdbcConnection.createStatement();
            Statement statement4 = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            ResultSet rs2 = statement2.executeQuery(query2);
            ResultSet rs3 = statement3.executeQuery(query3);
            ResultSet rs4 = statement4.executeQuery(validacionFechaLlegada);
            Boolean isNotNull = false;

            JSONObject json = new JSONObject();
            JSONObject jsonDefault = new JSONObject();
            while (rs4.next()) {
                isNotNull = rs4.getInt("valor") != 1;
            }
            if (!isNotNull) {
                json.put("FechaHora", UtilFuctions.convertObject(rs));
                json.put("FechaLLegada", UtilFuctions.convertObject(rs2));
                json.put("FechaSalida", UtilFuctions.convertObject(rs3));
            } else {
                json.put("FechaHora", UtilFuctions.convertObject(rs));
                json.put("FechaLLegada", jsonDefault);
                json.put("FechaSalida", jsonDefault);
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guia/Seguimiento/{idGuia}")
    public ResponseEntity<?> getSeguimientoGuia(@PathVariable("idGuia") int idGuia,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select pe.EntregaEnSucursal from ProGuiaPQ pg \n" +
                    "inner join ProEmbarquePQ pe on pe.IdGuia=pg.IdGuia\n" +
                    "where pg.IdGuia=" + idGuia;

            //ENTREGA :Si es Entrega en ocurre  fecha entrega
            String query2 = "IF NOT EXISTS (\n" +
                    "   select FechaEntrega,ceg.Descripcion from ProGuiaPQ inner join CatEstatusGuiasPQ ceg " +
                    "on ceg.IdEstatusGuias = 18\n" +
                    "where IdGuia=" + idGuia + " and IdEstatusGuia=18\n" +
                    ")\n" +
                    "SELECT '' AS FechaEntrega;\n" +
                    "\n" +
                    "ELSE\n" +
                    "select FechaEntrega,ceg.Descripcion from ProGuiaPQ inner join CatEstatusGuiasPQ ceg " +
                    "on ceg.IdEstatusGuias = 18\n" +
                    "where IdGuia=" + idGuia + " and IdEstatusGuia=18";

            //EN RUTA ENTREGA A DOMICILIO: Si es ultima milla  fecha y hora de envío al operador
            String query3 = "IF NOT EXISTS (\n" +
                    "   select FechaEntrega,ceg.Descripcion from ProGuiaPQ inner join CatEstatusGuiasPQ ceg " +
                    "on ceg.IdEstatusGuias = 17\n" +
                    "where IdGuia=" + idGuia + " and IdEstatusGuia=17\n" +
                    ")\n" +
                    "SELECT '' AS FechaEntrega,'' AS Descripcion;\n" +
                    "\n" +
                    "ELSE\n" +
                    "select FechaEntrega,ceg.Descripcion from ProGuiaPQ inner join CatEstatusGuiasPQ ceg " +
                    "on ceg.IdEstatusGuias = 17\n" +
                    "where IdGuia=" + idGuia;

            Statement statement = jdbcConnection.createStatement();
            Statement statement2 = jdbcConnection.createStatement();
            Statement statement3 = jdbcConnection.createStatement();
            Statement statement4 = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            ResultSet rs2 = statement2.executeQuery(query2);
            ResultSet rs3 = statement3.executeQuery(query3);

            JSONObject json = new JSONObject();
            if (rs.next()) {
                //  EntregaEnSucursal= 1  ,  Significa que es  Entrega en Ocurre
                if (rs.getInt("EntregaEnSucursal") == 1) {
                    json.put("Entrega", UtilFuctions.convertObject(rs2));
                } else { //  EntregaEnSucursal= 0  ,  Significa que es  Entrega en Domiclio
                    json.put("EntregaDomicilio", UtilFuctions.convertObject(rs3));
                }
            }
            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guia/ValidarEliminar/{idGuia}")
    public ResponseEntity<?> getGuiaEliminar(@PathVariable("idGuia") int idGuia,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select IdEstatusGuia from ProGuiaPQ pq where pq.IdGuia = " + idGuia;

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            if (rs.next()) {
                //si esta cancelada permitira eliminar la guia
                json.put("sePuedeEliminar", rs.getInt("IdEstatusGuia") == 8);
            }
            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guia/ValidarCancelar/{idGuia}")
    public ResponseEntity<?> getGuiaCancelar(@PathVariable("idGuia") int idGuia,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "select ISNULL(IdInforme,0) as IdInforme from ProGuiaPQ pq where pq.IdGuia = "
                    + idGuia;
            String query2 = "select (select pi.FolioInforme FROM ProInformePQ pi where pi.IdInforme = pg.IdInforme)" +
                    " as FolioInforme from ProGuiaPQ pg where pg.IdGuia = " + idGuia;

            Statement statement = jdbcConnection.createStatement();
            Statement statement2 = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            ResultSet rs2 = statement2.executeQuery(query2);
            JSONObject json = new JSONObject();

            query = "select top 1 CONCAT(Abreviacion,'-',Viaje) folioViajeERP from ProGuiaPQ g " +
                    "inner join ProViajes v on v.IdGuia=g.IdGuia\n" +
                    "    inner join CatSucursales s on v.IdSucursal=s.IdSucursal where g.IdGuia=" + idGuia
                    + " and v.CanceladoEl is null";
            ResultSet folioViajeERP = jdbcConnection.createStatement().executeQuery(query);
            if (folioViajeERP.next()) {
                json.put("sePuedeCancelar", false);//si tiene viaje activo en ERP no se puede cancelar
                json.put("folioViajeERP", folioViajeERP.getObject("folioViajeERP"));
                return ResponseEntity.ok(json.toString());
            }

            if (rs.next()) {
                if (rs.getInt("IdInforme") == 0) {//si no hay idInforme se puede cancelar
                    json.put("sePuedeCancelar", true);
                } else {
                    if (rs2.next()) {
                        json.put("sePuedeCancelar", false);//si el id informe es mayor a 0 se puede cancelar
                        json.put("FolioInforme", rs2.getObject("FolioInforme"));
                    }
                }
            }
            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guia/GetBancos")
    public ResponseEntity<?> getBancos(@RequestHeader("RFC") String rfc) throws
            Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "select * from CatBancos";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdBanco", rs.getInt("IdBanco"));
                json.put("m_nCodigo", rs.getInt("Codigo"));
                json.put("m_sBanco", rs.getString("Banco"));
                json.put("m_sClaveSAT", rs.getString("ClaveSAT"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guia/GetListadoPendientes/{idOrigen}/{idDestino}/{tipoTimbrado}")
    public ResponseEntity<?> getBancos(@PathVariable("idOrigen") int idOrigen,
                                       @PathVariable("idDestino") int idDestino,
                                       @PathVariable("tipoTimbrado") int tipoTimbrado,
                                       @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "SELECT\n" +
                    "                    g.IdGuia as m_nIdGuia,\n" +
                    "                    ceg.Estatus as m_sEstatusGuia\n" +
                    "                    ,g.FolioGuia as m_nFolioGuia,\n" +
                    "                    IIF(f.IdFactura is null,0,1) isTimbrada,\n" +
                    "                    cod.OrigenDestino as m_sCiudadDestino,\n" +
                    "                    cts.Descripcion as m_sTipoServicio,\n" +
                    "                     ISNULL((select Sum(pgc.Importe - pgc.ImporteRetiene - pgc.Descuento) " +
                    "from ProGuiaConceptoPQ pgc where pgc.IdGuia = g.IdGuia and pgc.IdConceptoFacturacion = " +
                    "(SELECT IdConceptoFlete FROM CatParametrosConfiguracion2PQ)),0) as m_xTotal,\n" +
                    "                    e.Observaciones as m_sObservaciones,\n" +
                    "                       (select sum(ed.Peso * ed.ctd) from ProEmbarqueDetallePQ ed " +
                    "where e.IdEmbarque = ed.IdEmbarque) as m_xPeso\n" +
                    "                FROM ProGuiaPQ g\n" +
                    "                inner join ProEmbarquePQ e on g.IdEmbarque = e.IdEmbarque\n" +
                    "                left join ProInformeGuiaPQ pig on pig.IdGuia=g.IdGuia\n" +
                    "                left join ProInformePQ pipq on pipq.IdInforme=pig.IdInforme\n" +
                    "                left join CatEstatusGuiasPQ ceg on ceg.IdEstatusGuias = g.IdEstatusGuia\n" +
                    "                left join CatOrigenesDestinos cod on cod.IdOrigenDestino = e.IdCiudadDestino\n" +
                    "                left join CatTipoServicioPQ cts on g.IdTipoServicio = cts.IdTipoServicio\n" +
                    "                left join ProViajes v on g.IdGuia=v.IdGuia\n" +
                    "                left join ProViajesFacturas f on v.IdViaje = f.IdViaje\n" +
                    "WHERE e.IdEmbarque= g.IdEmbarque  \n" +
                    "and (g.IdEstatusGuia = 4 OR g.IdEstatusGuia = 6)  and (isnull(g.IdInforme,0) = 0 " +
                    "or (pipq.IdCiudadOrigen!= " + idOrigen + " and pipq.IdCiudadDestino!= " + idDestino + "))\n" +
                    "and e.IdCiudadOrigen = " + idOrigen + " AND e.IdCiudadDestino = " + idDestino
                    + " and e.TipoTimbrado =" + tipoTimbrado + " order by g.FolioGuia";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Guia/ActualizarCoordenadas/{nIdGuia}/{sLatitud}/{sLongitud}")
    public ResponseEntity<?> actualizarCoordenadas(
            @PathVariable("nIdGuia") int nIdGuia,
            @PathVariable("sLatitud") String sLatitud,
            @PathVariable("sLongitud") String sLongitud,
            @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "update ProGuiaPQ SET Latitud = '" + sLatitud + "', Longitud = '" + sLongitud
                    + "'  WHERE IdGuia = " + nIdGuia;
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);

            return ResponseEntity.ok("Se actualizaron las coordenadas correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Guia/EntregaOcurre/{nIdGuia}")
    public ResponseEntity<?> generarEntregaOcurre(
            @PathVariable("nIdGuia") int nIdGuia,
            @RequestBody EntregaOcurreRequest bodyRequest,
            @RequestHeader("RFC") String rfc) throws Exception {

        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProGuiaOcurrePQ " + nIdGuia + ", "
                        + bodyRequest.getM_nIdUsuarioEntregaOcurre() + ", "
                        + bodyRequest.getM_sMontoRecibidoOcurre() + ", '"
                        + bodyRequest.getM_sFechaOcurre() + "', '"
                        + bodyRequest.getM_sHoraOcurre() + "', '"
                        + bodyRequest.getM_sComentariosOcurre() + "', '"
                        + bodyRequest.getM_nIdTipoPago() + "', '', 0, 0,'"
                        + bodyRequest.getRecibe() + "'";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se actualizó la guía correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al realizar la entrega ocurre.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Guia/Agregar")
    public ResponseEntity<?> agregarGuia(@RequestBody GuiaRequest
                                                 guia, @RequestHeader("RFC") String rfc)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                if (guia.getM_nIdSucursal() == 0) {
                    throw new Exception("El identificador de surcursal es un campo requerido");
                }
                if (guia.getM_dFecha() != null) {
                    if (guia.getM_dFecha().isEmpty())
                        throw new Exception("La fecha es un campo requerido");
                }
                if (guia.getM_sHora() != null) {
                    if (guia.getM_sHora().isEmpty())
                        throw new Exception("La hora es un campo requerido");
                }
                if (guia.getM_nIdEstatusGuia() == 0) {
                    throw new Exception("El identificador de estatus de la guia es un campo requerido");
                }
                if (guia.getM_nIdEmbarque() == 0) {
                    throw new Exception("El identificador del embarque es un campo requerido");
                }
                if (guia.getM_nIdMoneda() == 0) {
                    throw new Exception("El identificador de moneda es un campo requerido");
                }

                String query = "";
                Statement statement = jdbcConnection.createStatement();
                JSONObject jsonValidacionFolios = guiaService.validarFoliosDisponiblesViajesERP(jdbcConnection, guia.getM_nIdSucursal());
                if (!jsonValidacionFolios.getBoolean("success")) {
                    if (jsonValidacionFolios.getInt("errorCode") == 1){
                        return ResponseEntity.status(500).body("Hubo un error al validar los folios disponibles. Comuníquese con las oficinas de GM Transport");
                    } else {
                        return ResponseEntity.status(500).body(jsonValidacionFolios.getString("message"));
                    }
                }

                query = "EXEC usp_ProGuiaAgregarPQ " + (guia.getM_nTracking() != null ? guia.getM_nTracking() : 0)
                        + "," + 0 + ",'" + guia.getM_dFecha() + "','" + guia.getM_sHora() + "'," + guia.getM_nIdEstatusGuia() + "," + guia.getM_nIdEmbarque() + "," + guia.getM_nIdMoneda()
                        + "," + guia.getM_nTIpoCambio() + "," +
                        "'" + guia.getM_dFecha() + " " + guia.getM_sHora() + "'," + guia.getM_nCreadoPor() + ",'"
                        + guia.getM_dFecha() + " " + guia.getM_sHora() + "'," + guia.getM_nModificadoPor() + "," + guia.getM_nIdSucursal() + ","
                        + guia.getM_nValorDeclarado() + "," + guia.getM_nidTipoServicio() + "," + 0 +
                        "," + guia.getM_nCreadoPor();
                statement.executeUpdate(query);
                String query2 = "SELECT IDENT_CURRENT( 'ProGuiaPQ' ) as id";
                Statement statement2 = jdbcConnection.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);
                int idGuia = 0;
                while (rs2.next()) {
                    idGuia = rs2.getInt("id");
                }
                String query3 = "SELECT FolioGuia as folio from ProGuiaPQ where IdGuia = " + idGuia + " ";
                Statement statement3 = jdbcConnection.createStatement();
                ResultSet rs3 = statement3.executeQuery(query3);
                String folio = "";
                while (rs3.next()) {
                    folio = rs3.getString("folio");
                }
                for (ProGuiaConceptosRequest conceptos : guia.getArClsGuiaConceptos()) {
                    conceptos.setM_nIdGuia(idGuia);
                    Boolean resultado = guiaService.agregarConceptosGuia(conceptos, jdbcConnection);
                    if (!resultado) {
                        guiaService.eliminarGuia(idGuia, jdbcConnection);
                    }
                }

                String query4 = "SELECT IdEmbarqueDetalle as id FROM ProEmbarqueDetallePQ where IdEmbarque = " + guia.getM_nIdEmbarque();
                Statement statement4 = jdbcConnection.createStatement();
                ResultSet rs4 = statement4.executeQuery(query4);
                int idEmbarqueDetalle = 0;
                while (rs4.next()) {
                    idEmbarqueDetalle = rs4.getInt("id");
                }

                String query5 = "EXEC usp_ProGuiaActualizarEmbarquePQ " + idEmbarqueDetalle;
                Statement statement5 = jdbcConnection.createStatement();
                statement5.executeUpdate(query5);

                try {
                    viajeService.insertarCartaPorte(idGuia, rfc, jdbcConnection);
                }catch (SQLException sqle) {
                    return ResponseEntity.status(500).body("No se pudo generar carta porte: " + sqle.getMessage());
                }

                query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                if (jsonObject != null) {
                    try {
                        guiaService.enviarCorreoTracking(jdbcConnection, statement, idGuia, rfc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.ok("Agregado Exitosamente, Folio: " + folio
                                + ", pero no se logró mandar el correo electrónico al cliente.");
                    }
                }
                return ResponseEntity.ok("Agregado Exitosamente, Folio:" + folio + ".");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al generar la guia.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Guia/Modificar")
    public ResponseEntity<?> modificarGuia(@RequestBody GuiaRequest guia,
                                           @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            Statement statement = jdbcConnection.createStatement();
            try {
                String query = "";
                query = "UPDATE e SET e.Observaciones = '" + guia.getM_sObservaciones()
                        + "' FROM ProEmbarquePQ e INNER JOIN ProGuiaPQ g ON e.IdEmbarque = g.IdEmbarque " +
                        "WHERE g.IdGuia = " + guia.getM_nIdGuia();
                statement.executeUpdate(query);
                query = "UPDATE ProViajes SET Observaciones = '" + guia.getM_sObservaciones()
                        + "' where IdGuia = " + guia.getM_nIdGuia();
                statement.executeUpdate(query);
//            JSONObject jsonValidacionCartaPorte = guiaService.validarCartaPorteRelacionada(statement, guia.getM_nIdGuia());
//            if (jsonValidacionCartaPorte == null) {
//                return ResponseEntity.status(500).body("Hubo un error al validar los viajes relacionados a la guía.");
//            }
//            if (jsonValidacionCartaPorte.getBoolean("tieneViajeRelacionado")) {
//                return ResponseEntity.status(500).body(jsonValidacionCartaPorte.getString("message"));
//            }


//            query = "DELETE FROM ProGuiaConceptoPQ WHERE IdGuia ="+guia.getM_nIdGuia();
//            statement.executeUpdate(query);

//            for (ProGuiaConceptosRequest conceptos:guia.getArClsGuiaConceptos()) {
//                conceptos.setM_nIdGuia(guia.getM_nIdGuia());
//                Boolean resultado = guiaService.agregarConceptosGuia(conceptos,jdbcConnection);
//                if(!resultado) {
//                    throw new Exception("Error al guardar los conceptos de facturacion");
//                }
//            }
                //SE REEMPLAZAN LOS CONCEPTOS DEL EMBARQUE DE LA GUIA CON LOS NUEVOS DE LA GUIA PARA QUE SEAN IGUALES
//            query = "DELETE FROM CatCotizacionConceptosPQ WHERE IdCotizador = (SELECT top 1 cotizacion.IdCotizacion\n" +
//                    "                                                          FROM CatCotizacion cotizacion\n" +
//                    "                                                                   INNER JOIN ProGuiaPQ guia ON cotizacion.IdEmbarque = guia.IdEmbarque\n" +
//                    "                                                          WHERE cotizacion.Activa = 1 AND guia.IdGuia = "+guia.getM_nIdGuia()+");\n" +
//                    "INSERT INTO CatCotizacionConceptosPQ(IdCotizador, IdConceptoFacturacion, Importe, IdImpuestoTraslada, ImporteIva,IdImpuestoRetiene, ImporteRetiene, Descuento)\n" +
//                    "    (SELECT (SELECT top 1 cotizacion.IdCotizacion FROM CatCotizacion cotizacion INNER JOIN ProGuiaPQ guia ON cotizacion.IdEmbarque = guia.IdEmbarque WHERE cotizacion.Activa = 1 AND guia.IdGuia = "+guia.getM_nIdGuia()+")\n" +
//                    "         ,concp.IdConceptoFacturacion, concp.Importe, concp.IdImpuestoTraslada, concp.ImporteIva,concp.IdImpuestoRetiene, concp.ImporteRetiene, concp.Descuento FROM ProGuiaConceptoPQ concp WHERE concp.IdGuia = "+guia.getM_nIdGuia()+");";
//            statement.executeUpdate(query);
//            viajeService.insertarCartaPorte(guia.getM_nIdGuia(),rfc,jdbcConnection);
                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al guardar los cambios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/Guia/Eliminar/{idGuia}/{idUsuario}")
    public ResponseEntity<?> eliminarGuia(
            @PathVariable("idGuia") int idGuia,
            @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_ProGuiaEliminarPQ " + idGuia;
                statement.executeUpdate(query);
                return ResponseEntity.ok("Registro eliminado con éxito");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al eliminar el registro.");
        }
    }

    @PutMapping("/Guia/Cancelar")
    public ResponseEntity<?> cancelarGuia(
            @RequestBody GuiaRequest guia,
            @RequestHeader("RFC") String rfc) throws Exception {
        try {

            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();

                GuiaRequest guiaRequest = new GuiaRequest();
                guiaRequest.setM_nIdGuia(guia.getM_nIdGuia());
                String query = "SELECT Tracking,FolioGuia FROM ProGuiaPQ WHERE IdGuia = " + guiaRequest.getM_nIdGuia();
                ResultSet rs = statement.executeQuery(query);

                while (rs.next()) {
                    guiaRequest.setM_nTracking(rs.getString("Tracking"));
                    guiaRequest.setM_sFolioGuia(rs.getString("FolioGuia"));
                }
                query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ false, '"
                        + guiaRequest.getM_nTracking() + "'";
                rs = statement.executeQuery(query);
                JSONObject jsonGuia = UtilFuctions.convertObject(rs);
                if (jsonGuia != null) {
                    guiaRequest.setM_sCorreoDestinatario(jsonGuia.getString("m_sCorreoDestinatario"));
                    guiaRequest.setM_sDomicilioDestinatario(
                            jsonGuia.getBoolean("m_bEntregaDiferenteDomicilio") ?
                                    jsonGuia.getString("m_sDomicilioDetalleEntrega") :
                                    rs.getString("m_sDomicilioDestinatario"));
                    guiaRequest.setM_dFecha(jsonGuia.getString("m_dFechaRegistro"));
                    guiaRequest.setM_sTipoServicio(jsonGuia.getString("m_sTipoServicio"));

                }

                guia.setFechaCancelacion(guia.getFechaCancelacion().replace("T", " "));
                query = "EXEC usp_ProGuiaCancelarPQ " + guia.getM_nIdGuia() + ", '" + guia.getMotivoCancelacion() + "'"
                        + ", " + guia.getIdUsuario() + ", '" + guia.getFechaCancelacion() + "', null";
                statement.executeUpdate(query);

                query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";

                statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);
                try {
                    viajeService.enviarCorreoCancelarGuia(jdbcConnection, statement, guia.getM_nIdGuia(),
                            guiaRequest, true, rfc, "Su orden ha sido cancelada");
                } catch (Exception e) {
                    return ResponseEntity.ok("Cancelado exitosamente, pero no se logró mandar el correo " +
                            "electrónico al cliente.");
                }

                return ResponseEntity.ok("Registro cancelado.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al guardar los cambios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/ReasignarGuia/{idParadaNueva}/{idParadaVieja}/{idGuia}")
    public ResponseEntity<?> reasignarGuia(
            @PathVariable("idParadaNueva") int idParadaNueva,
            @PathVariable("idParadaVieja") int idParadaVieja,
            @PathVariable("idGuia") int idGuia,
            @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "update  ProParadaUltimaMillaGuiasPQ set m_nIdParadaUltimaMilla = " + idParadaNueva
                        + " where m_nIdParadaUltimaMilla = " + idParadaVieja + " and m_nIdGuia = " + idGuia;
                statement.executeUpdate(query);
                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al guardar los cambios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Guias/CambiarTipoCobro")
    public ResponseEntity<?> cambiarTipoCobro
            (@RequestBody EmbarqueRequest
                     embarque, @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "update ProEmbarquePQ SET IdTipoCobro = " + embarque.getM_nIdTIpoCobro()
                        + " WHERE IdGuia = " + embarque.getM_nIdGuia();
                statement.executeUpdate(query);
                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios.");
        }
    }

    @PutMapping("/Guia/ActualizarCoordenadas")
    public ResponseEntity<?> actualizarCoordenadasGuia
            (@RequestBody GuiaRequest
                     guia, @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "update ProGuiaPQ SET Latitud = '" + guia.getM_sLatitud() + "', Longitud = '"
                        + guia.getM_sLongitud() + "' WHERE IdGuia = " + guia.getM_nIdGuia();
                statement.executeUpdate(query);
                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al guardar los cambios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Guia/CambiarEstatusGuia")
    public ResponseEntity<?> cambiarEstatus
            (@RequestBody GuiaRequest
                     guia, @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "";
                if (guia.getM_nIdEstatusGuia() == 14) {
                    query = "UPDATE ProGuiaPQ \n" +
                            " SET IdEstatusGuia = " + guia.getM_nIdEstatusGuia() + ",\n" +
                            "     Latitud = '" + guia.getM_sLatitudD() + "',\n" +
                            "     Longitud = '" + guia.getM_sLongitudD() + "'\n" +
                            " WHERE IdGuia = " + guia.getM_nIdGuia() + ";\n" +
                            "UPDATE ProEmbarquePQ\n" +
                            "SET EntregaEnSucursal = 0,\n" +
                            "    EntregarMismoDomicilio = 0,\n" +
                            "    DomicilioEntrega = '" + guia.getDomicilioEntrega() + "',\n" +
                            "    EntregarEn = '" + guia.getEntregarEn() + "',\n" +
                            "    CodigoMunicipioEntrega = '" + guia.getM_sCodigoMunicipioEntrega() + "',\n" +
                            "    CodigoPostalEntrega = " + guia.getCodigoPostalEntrega() + ",\n" +
                            "    IdEstadoEntrega = '" + guia.getM_nIdEstadoEntrega() + "',\n" +
                            "    IdZonaOperativa = " + guia.getM_nIdZonaOperativa() + ",\n" +
                            "    Latitud = '" + guia.getM_sLatitudD() + "',\n" +
                            "    Longitud = '" + guia.getM_sLongitudD() + "'\n" +
                            "    WHERE IdEmbarque = (SELECT IdEmbarque FROM ProGuiaPQ WHERE IdGuia = "
                            + guia.getM_nIdGuia() + ")";
                } else if (guia.getM_nIdEstatusGuia() == 7) {
                    query = "UPDATE ProGuiaPQ \n" +
                            " SET IdEstatusGuia = " + guia.getM_nIdEstatusGuia() + ",\n" +
                            "     Latitud = '',\n" +
                            "     Longitud = ''\n" +
                            " WHERE IdGuia = " + guia.getM_nIdGuia() + ";\n" +
                            "UPDATE ProEmbarquePQ\n" +
                            "SET EntregaEnSucursal = 1,\n" +
                            "    EntregarMismoDomicilio = 0,\n" +
                            "    DomicilioEntrega = '',\n" +
                            "    EntregarEn = '',\n" +
                            "    CodigoMunicipioEntrega = '',\n" +
                            "    CodigoPostalEntrega = 0,\n" +
                            "    IdEstadoEntrega = '',\n" +
                            "    IdZonaOperativa = " + guia.getM_nIdZonaOperativa() + ",\n" +
                            "    Latitud = '',\n" +
                            "    Longitud = ''\n" +
                            "    WHERE IdEmbarque = (SELECT IdEmbarque FROM ProGuiaPQ WHERE IdGuia = "
                            + guia.getM_nIdGuia() + ")";
                }
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se actualizó la guía correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al cambiar el estatus.");
        }
    }

    @GetMapping("/Guias/GetValidacionCFDITraslada/{id}")
    public ResponseEntity<?> validarCFDI
            (@PathVariable("id") int id,
             @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "select ISNULL(pf.FolioFiscalUUID,'') as FolioFiscalUUID, pg.FolioGuia " +
                        "as FolioGuia,peq.ValidarTimbradoIngreso from\n" +
                        "    ProGuiaPQ pg\n" +
                        "        inner join ProViajes pv on pv.IdGuia=pg.IdGuia\n" +
                        "        inner join ProEmbarquePQ peq on peq.IdEmbarque=pg.IdEmbarque\n" +
                        "        left join ProViajesFacturas pvf on pvf.IdViaje=pv.IdViaje and pvf.CanceladoPor is null\n" +
                        "        left join ProFacturas pf on pf.IdFactura = pvf.IdFactura and pf.Estatus != 2\n" +
                        "        left join ProViajesCartasPorte pvc on pvc.IdViaje = pv.IdViaje\n" +
                        "where pv.IdGuia in (select IdGuia from ProInformeGuiaPQ ig inner join ProInformePQ i " +
                        "on ig.IdInforme=i.IdInforme where i.IdInforme =" + id + ")";

                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);

                if (jsonObject.getBoolean("ValidarTimbradoIngreso")) {
                    String foliofical = jsonObject.getString("FolioFiscalUUID");
                    if (foliofical.equalsIgnoreCase("")) {
                        return ResponseEntity.status(500).body("El reporte no se puede generar ya que la guia:"
                                + jsonObject.getString("FolioGuia") + " no se encuentra timbrada");
                    }
                    return ResponseEntity.ok("Guia válida para timbrar");
                } else {
                    return ResponseEntity.ok("Guia no validada. La validación de factura de ingreso está desactivada.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error en el servicio de validar Guia");
        }
    }

    @PutMapping("/Guia/CambiarEstatusGuiaSAT")
    public ResponseEntity<?> cambiarEstatusSAT(@RequestBody GuiaRequest guia, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "";
                query = "UPDATE ProEmbarquePQ\n" +
                        "SET EntregaEnSucursal = 0,\n" +
                        "    EntregarMismoDomicilio = 0,\n" +
                        "    DomicilioEntrega = '" + guia.getDomicilioEntrega() + "',\n" +
                        "    EntregarEn = '" + guia.getEntregarEn() + "',\n" +
                        "    CodigoMunicipioEntrega = '" + guia.getM_sCodigoMunicipioEntrega() + "',\n" +
                        "    CodigoPostalEntrega = " + guia.getCodigoPostalEntrega() + ",\n" +
                        "    IdEstadoEntrega = '" + guia.getM_nIdEstadoEntrega() + "',\n" +
                        "    IdZonaOperativa = " + guia.getM_nIdZonaOperativa() + ",\n" +
                        "    DatosAdicionales = '" + guia.getDatosAdicionales() + "'\n" +
//                    "    Latitud = '" + guia.getM_sLatitudD() + "',\n" +
//                    "    Longitud = '" + guia.getM_sLongitudD() + "'\n" +
                        "    WHERE IdEmbarque = (SELECT IdEmbarque FROM ProGuiaPQ WHERE IdGuia = " + guia.getM_nIdGuia() + ")";

                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se actualizó la guía correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al cambiar el estatus.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Guia/EnviarCorreoStatus/{idGuia}")
    public ResponseEntity<?> enviarCorreoStatus(
                    @PathVariable("idGuia") int idGuia,
                    @RequestHeader("RFC") String rfc,
                    @RequestBody EnvioCorreosRequest request) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {

                String query = "select FolioGuia from ProGuiaPQ where IdGuia = " + idGuia;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);

                String folio = jsonObject.getString("FolioGuia");

                query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
                statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);

                try {
                    guiaService.enviarCorreoStatus(jdbcConnection, statement, idGuia,
                            request.getCorreos().toLowerCase().split(","), request.getCorreoDefault(), rfc,
                            request.getMensaje());
                } catch (
                        Exception e) {
                    return ResponseEntity.ok("Agregado Exitosamente, Folio: " + folio
                            + ", pero no se logró mandar el correo electrónico al cliente.");
                }
                return ResponseEntity.ok("Enviado Exitosamente, Folio:" + folio);
            } catch (
                    Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al generar la guia.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Guia/ReenviarCorreoCartaPorte/{idGuia}")
    public ResponseEntity<?> enviarCorreoInforme(
            @PathVariable("idGuia") int idGuia,
            @RequestHeader("RFC") String rfc,
            @RequestBody EnvioCorreosRequest request)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {

                String query = "select FolioGuia from ProGuiaPQ where IdGuia = " + idGuia;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                String folio = jsonObject.getString("FolioGuia");
                query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
                statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);
                try {
                    guiaService.reenviarCorreoTracking(jdbcConnection, statement, idGuia,
                            request.getCorreos().toLowerCase().split(","), request.getCorreoDefault(), rfc);
                } catch (
                        Exception e) {
                    return ResponseEntity.ok("Agregado Exitosamente, Folio: " + folio
                            + ", pero no se logró mandar el correo electrónico al cliente.");
                }
                return ResponseEntity.ok("Enviado Exitosamente, Folio:" + folio);
            } catch (
                    Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al generar la guia.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guias/GetPaquetes/{id}")
    public ResponseEntity<?> GetPaquetesByGuia
            (
                    @PathVariable("id") int id,
                    @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "SELECT IdEmbarque FROM ProGuiaPQ WHERE IdGuia =  " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject json = UtilFuctions.convertObject(rs);
                JSONArray jsonArray = embarqueService.getListadoByIdEmabrque(json.getInt("IdEmbarque"),
                        jdbcConnection);
                return ResponseEntity.ok(jsonArray.toString());
            } catch (
                    Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al recuperar los paquetes de la guía.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Guias/ValidarCartaPorteRelacionada/{id}")
    public ResponseEntity<?> validarCartaPorteRelacionada
            (
                    @PathVariable("id") int id,
                    @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "SELECT IdEmbarque FROM ProGuiaPQ WHERE IdGuia =  " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject json = UtilFuctions.convertObject(rs);
                JSONArray jsonArray = embarqueService.getListadoByIdEmabrque(json.getInt("IdEmbarque"),
                        jdbcConnection);
                return ResponseEntity.ok(jsonArray.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar los paquetes de la guía.");
        }
    }
}
