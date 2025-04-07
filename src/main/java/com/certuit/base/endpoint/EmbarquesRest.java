package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.certuit.base.util.UtilFuctions.*;

@RestController
@RequestMapping(value = "/api")
public class EmbarquesRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    EmbarqueService embarqueService;
    @Autowired
    PaquetesService paquetesService;
    @Autowired
    ComplementosSATService complementosService;
    @Autowired
    ConceptosService conceptosService;
    @Autowired
    CotizadorService cotizadorService;
    @Autowired
    CotizacionRest cotizadorRest;

    @GetMapping("/Embarque/GetByFiltro/{sFechaInicial}/{sFechaFinal}/{sSucursal}/{sEstatus}/{folio}/{origen}/{destino}/{cliente}")
    public ResponseEntity<?> getListadoEmbarquesFiltro(@PathVariable("sFechaInicial") String fechaInicial,
                                                       @PathVariable("sFechaFinal") String fechaFinal,
                                                       @PathVariable("sSucursal") int sucursal,
                                                       @PathVariable("sEstatus") String estatus,
                                                       @PathVariable("folio") String folio,
                                                       @PathVariable("origen") int origen,
                                                       @PathVariable("destino") int destino,
                                                       @PathVariable("cliente") int cliente,
                                                       @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc))
        {
            fechaInicial = !fechaInicial.equalsIgnoreCase("0") ? "'" + fechaInicial + "'" : null;
            fechaFinal = !fechaFinal.equalsIgnoreCase("0") ? "'" + fechaFinal + "'" : null;
            folio = !folio.equalsIgnoreCase("0") ? folio : null;
            if(folio != null){
                folio   = folio.toUpperCase();
            }
            String query = "SELECT\n" +
                    "            FORMAT(FechaRegistro, 'dd-MM-yyyy') + ' ' +  " +
                    "convert(char(5),convert(time(0),HoraRegistro)) as m_sFechaHora\n" +
                    "                 ,pp.IdEstatusEmbarque as m_nIdEstatusEmbarque\n" +
                    "                 ,CEEP.Estatus as m_sEstatusEmbarque\n" +
                    "                 ,CEEP.Color as m_sColorEstatus\n" +
                    "                 ,pp.IdCiudadOrigen as m_nIdCiudadOrigen\n" +
                    "                 ,codo.OrigenDestino as m_sCiudadOrigen\n" +
                    "                 ,pp.IdCiudadDestino as m_nIdCiudadDestino\n" +
                    "                 ,codd.OrigenDestino as m_sCiudadDestino\n" +
                    "                 ,pp.IdEmbarque as m_nIdEmbarque\n" +
                    "                 ,pp.FolioEmbarque as m_sFolioEmbarque\n" +
                    "                 ,pp.Observaciones as m_sObservaciones\n" +
                    "                 ,pp.IdCliente as m_nIdCliente\n" +
                    "                 , C.NombreFiscal as m_sNombreCliente\n" +
                    "                    ,pp.IdSucursal as IdSucursal\n" +
                    "                 ,CS.Sucursal as m_sSucursal\n" +
                    "                    ,PGP.FolioGuia as m_sFolioGuia\n" +
                    "                    ,PGP.IdGuia as m_nIdGuia\n" +
                    "                    ,PGP.IdEstatusGuia as m_sEstatusGuia\n" +
                    "                 ,pp.IdRecoleccion as m_nIdRecoleccion\n" +
                    "                    ,(case when IdRecoleccion = 0 then CAST(0 AS BIT) else CAST(1 AS BIT) end) " +
                    "as m_bEsRecolecta\n" +
                    "                 ,(select max(pg.FolioRecoleccion) from ProRecoleccionPQ pg " +
                    "where pg.IdRecoleccion = pp.IdRecoleccion) as m_sFolioRecoleccion\n" +
                    "                 ,pi.FolioInforme as m_sFolioInforme\n" +
                    "                 ,us.Nombre as m_sUsuarioDocumento\n" +
                    "                 ,pp.FechaCancelacion as m_dtFechaCancelacion\n" +
                    "                 ,cu.Nombre as m_sUsuarioCancelacion" +
                    "                 ,pp.Referencia as m_sReferencia\n" +
                    "                 FROM ProEmbarquePQ pp\n" +
                    "                 left join CatClientes C on pp.IdCliente = C.IdCliente\n" +
                    "                 left join ProGuiaPQ PGP on pp.IdGuia = PGP.IdGuia\n" +
                    "                 left join CatSucursales CS on pp.IdSucursal = CS.IdSucursal\n" +
                    "                 left join CatEstatusEmbarquePQ CEEP " +
                    "on pp.IdEstatusEmbarque = CEEP.IdEstatusEmbarque\n" +
                    "                 left join CatOrigenesDestinos codd " +
                    "on pp.IdCiudadDestino = codd.IdOrigenDestino\n" +
                    "                 left join CatOrigenesDestinos codo " +
                    "on pp.IdCiudadOrigen = codo.IdOrigenDestino\n" +
                    "                 left join ProInformePQ pi on pi.IdInforme = pp.IdInforme\n" +
                    "                 left join CatUsuarios us on pp.IdUsuario = us.IdUsuario" +
                    "                 left join CatUsuarios cu on pp.UsuarioCancelacion = cu.IdUsuario" +
                    " WHERE (pp.FolioEmbarque LIKE '%" + folio + "%')" +
                    " OR ((pp.IdSucursal = " + sucursal + " OR " + sucursal + " = 0)" +
                    " AND (pp.IdEstatusEmbarque = " + estatus + " OR " + estatus + " = 0)" +
                    " AND ((pp.FechaRegistro >= " + fechaInicial + " OR " + fechaInicial + " IS NULL))" +
                    " AND (pp.FechaRegistro <= " + fechaFinal + " OR " + fechaFinal + " IS NULL)" +
                    " AND (pp.IdCliente = " + cliente + " OR " + cliente + " = 0)" +
                    " AND (pp.IdCiudadOrigen = " + origen + " OR " + origen + " = 0)" +
                    " AND (pp.IdCiudadDestino = " + destino + " OR " + destino + " = 0)" + " AND '" +  folio + "'"
                    +" = 'null' )" +
                    " ORDER BY pp.FechaRegistro DESC, pp.HoraRegistro DESC";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Embarque/GetById/{id}")
    public ResponseEntity<?> getEmbarqueId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT e.IdEmbarque              as m_nIdEmbarque\n" +
                    "     , e.FolioEmbarque           as m_sFolioEmbarque\n" +
                    "     , e.Fecha                   as m_dFecha\n" +
                    "     , e.Hora                    as m_tHora\n" +
                    "     , e.FechaRegistro           as m_dFechaRegistro\n" +
                    "     , e.HoraRegistro            as m_tHoraRegistro\n" +
                    "     , e.IdEstatusEmbarque       as m_nIdEstatusEmbarque\n" +
                    "     , cet.Estatus               as m_sEstatusEmbarque\n" +
                    "     , e.IdSucursal              as IdSucursal\n" +
                    "     , cs.Sucursal               as m_sSucursal\n" +
                    "     , e.EmbarqueConCita         as m_bEmbarqueConCita\n" +
                    "     , e.FechaCita               as m_sFechaCita\n" +
                    "     , LEFT(e.HoraCitaMinima, 5) as m_sHoraCitaMinima\n" +
                    "     , LEFT(e.HoraCitaMaxima, 5) as m_sHoraCitaMaxima\n" +
                    "     , e.IdMoneda                as m_nIdMoneda\n" +
                    "     , e.TipoCambio              as m_cTIpoCambio\n" +
                    "     , e.IdTipoCobro             as m_nIdTIpoCobro\n" +
                    "     , e.IdCliente               as m_nIdCliente\n" +
                    "     , e.ValorDeclarado          as m_xValorDeclarado\n" +
                    "     , e.IdTipoSeguro            as m_nIdTipoSeguro\n" +
                    "     , e.PorcentajeSeguro        as m_xPorcentajeSeguro\n" +
                    "     , e.AplicaSeguro            as m_bAplicaSeguro\n" +
                    "     , e.Observaciones           as m_sObservaciones\n" +
                    "    --REMITENTE\n" +
                    "     , e.IdRemitente             as m_nIdRemitente\n" +
                    "     , e.NombreRemitente         as m_sNombreRemitente\n" +
                    "     , e.RFCRemitente            as m_sRFCRemitente\n" +
                    "     , e.DomicilioRemitente      as m_sDomicilioRemitente\n" +
                    "     , e.IdCodigoPostalRemitente as m_nIdCodigoPostalRemitente\n" +
                    "     , rem.CodigoPostal          as m_sCodigoPostalRemitente\n" +
                    "     , e.IdCiudadRemitente       as m_nIdCiudadRemitente\n" +
                    "     , cdRem.Ciudad              as m_sCiudadRemitente\n" +
                    "     , e.CorreoRemitente         as m_sCorreoRemitente\n" +
                    "     , e.TelefonoRemitente       as m_sTelefonoRemitente\n" +
                    "     , e.ContactoRemitente       as m_sContactoRemitente\n" +
                    "     , e.IdEstadoRemitente       as m_nIdEstadoRemitente\n" +
                    "     , edoRem.Estado             as m_sEstadoRemitente\n" +
                    "     , e.MunicipioRemitente      as m_sCodigoMunicipioRemitente\n" +
                    "     , rem.Municipio             as m_sMunicipioRemitente\n" +
                    "     , e.AliasRemitente          as m_sAliasRemitente\n" +
                    "     , e.CalleRemitente          as m_sCalleRemitente\n" +
                    "     , e.NoIntRemitente          as m_sNoIntRemitente\n" +
                    "     , e.NoExtRemitente          as m_sNoExtRemitente\n" +
                    "     , e.ColoniaRemitente        as m_sColoniaRemitente\n" +
                    "     , e.IdCiudadOrigen          as m_nIdCiudadOrigen\n" +
                    "     , cdOrigen.OrigenDestino    as m_sCiudadOrigen\n" +
                    "     , e.IdZonaRemitente         as m_nIdZonaRemitente\n" +
                    "     , psRem.Pais                as m_sPaisRemitente\n" +
                    "    --DESTINATARIO\n" +
                    "     , e.IdDestinatario             as m_nIdDestinatario\n" +
                    "     , e.NombreDestinatario         as m_sNombreDestinatario\n" +
                    "     , e.RFCDestinatario            as m_sRFCDestinatario\n" +
                    "     , e.DomicilioDestinatario      as m_sDomicilioDestinatario\n" +
                    "     , e.IdCodigoPostalDestinatario as m_nIdCodigoPostalDestinatario\n" +
                    "     , dest.CodigoPostal            as m_sCodigoPostalDestinatario\n" +
                    "     , e.IdCiudadDestinatario       as m_nIdCiudadDestinatario\n" +
                    "     , cdDes.Ciudad                 as m_sCiudadDestinatario\n" +
                    "     , e.CorreoDestinatario         as m_sCorreoDestinatario\n" +
                    "     , e.TelefonoDestinatario       as m_sTelefonoDestinatario\n" +
                    "     , e.ContactoDestinatario       as m_sContactoDestinatario\n" +
                    "     , e.IdEstadoDestinatario       as m_nIdEstadoDestinatario\n" +
                    "     , edoDes.Estado                as m_sEstadoDestinatario\n" +
                    "     , e.MunicipioDestinatario      as m_sCodigoMunicipioDestinatario\n" +
                    "     , dest.Municipio               as m_sMunicipioDestinatario\n" +
                    "     , e.AliasDestinatario          as m_sAliasDestinatario\n" +
                    "     , e.CalleDestinatario          as m_sCalleDestinatario\n" +
                    "     , e.NoIntDestinatario          as m_sNoIntDestinatario\n" +
                    "     , e.NoExtDestinatario          as m_sNoExtDestinatario\n" +
                    "     , e.ColoniaDestinatario        as m_sColoniaDestinatario\n" +
                    "     , e.IdCiudadDestino            as m_nIdCiudadDestino\n" +
                    "     , cdDestino.OrigenDestino      as m_sCiudadDestino\n" +
                    "     , e.IdZonaDestinatario         as m_nIdZonaDestinatario\n" +
                    "     , psDes.Pais                   as m_sPaisDestinatario\n" +
                    "     , e.IdComplemento         as m_nIdComplemento\n" +
                    "     , e.IdTipoDocumento                   as m_nIdTipoDocumento\n" +
                    "    --ENTREGA\n" +
                    "     , e.EntregaEnSucursal      as m_bEntregaEnSucursal\n" +
                    "     , e.IdSucursalEntrega      as m_nIdSucursalEntrega\n" +
                    "     , e.EntregarMismoDomicilio as EntregarMismoDomicilio\n" +
                    "     , cpEnt.IdCodigoPostal     as m_nIdCodigoPostalEntrega\n" +
                    "     , cpEnt.CodigoPostal       as m_sCodigoPostalEntrega\n" +
                    "     , cpEnt.Localidad          as m_sLocalidadEntrega\n" +
                    "     , cpEnt.Colonia            as m_sColoniaEntrega\n" +
                    "     , cpEnt.Municipio          as m_sMunicipioEntrega\n" +
                    "     , e.CodigoMunicipioEntrega as m_sCodigoMunicipioEntrega\n" +
                    "     , e.IdCiudadEntrega        as m_nIdCiudadEntrega\n" +
                    "     , e.IdEstadoEntrega        as m_nIdEstadoEntrega\n" +
                    "     , edoEnt.Estado            as m_sEstadoEntrega\n" +
                    "     , edoEnt.IdPais            as m_nIdPaisEntrega\n" +
                    "     , psEnt.Pais               as m_sPaisEntrega\n" +
                    "     , e.IdZonaEntrega          as m_nIdZonaEntrega\n" +
                    "     , e.DomicilioEntrega       as DomicilioEntrega\n" +
                    "     , e.EntregarEn             as EntregarEn\n" +
                    "     , e.DatosAdicionales       as DatosAdicionalesis\n" +
                    "     , e.Latitud                as m_sLatitud\n" +
                    "     , e.Longitud               as m_sLongitud\n" +
                    "     , zonaOpEnt.IdZona         as m_nIdZonaOperativa\n" +
                    "     , zonaOpEnt.CodigoZona     as m_sZonaOperativa\n" +
                    "     --RECOLECCION\n" +
                    "     , pr.IdRecoleccion                    as m_nIdRecoleccion\n" +
                    "     , ISNULL(pr.FolioRecoleccion, '')     as m_sFolioRecoleccion\n" +
                    "     , zonaTRec.CodigoZona                 as m_sZonaTarifaRecoleccion\n" +
                    "     , pr.IdZonaOperativa                  as m_nIdZonaOperativaRecoleccion\n" +
                    "     , zonaTRec.IdZona                     as m_nIdZonaTarifaRecoleccion\n" +
                    "     , pr.RecoleccionConCita               as m_bRecoleccionConCita\n" +
                    "     , pr.RecoleccionDiferenteDomicilio    as m_bRecoleccionDiferenteDomicilio\n" +
                    "     , IIF(e.IdRecoleccion = 0,\n" +
                    "           CAST(0 AS BIT), CAST(1 AS BIT)) as m_bEsRecoleccion\n" +
                    "    --OTROS\n" +
                    "     , pi.FolioInforme      as m_sFolioInforme\n" +
                    "     , g.FolioGuia          as m_sFolioGuia\n" +
                    "     , e.NoPaquetes         as m_nNoPaquetes\n" +
                    "     , e.FechaCancelacion   as m_sFechaCancelacion\n" +
                    "     , e.UsuarioCancelacion as m_sUsuarioCancelacion\n" +
                    "     , e.MotivoCancelacion  as m_sMotivoCancelacion\n" +
                    "     , cc.IdCotizacion      as m_nIdCotizacion\n" +
                    "     , e.ValidarTimbradoIngreso      as m_bValidarTimbraoIngreso\n" +
                    "     , e.CitaPendiente      as m_bCitaPendiente\n" +
                    "     , ISNULL(e.Referencia,'')      as m_sReferencia\n" +
                    "    --SIN USO APARENTE\n" +
                    "     , e.NoSobres           as m_nNoSobres\n" +
                    "     , e.IdOperador         as m_nIdOperador\n" +
                    "     , e.IdUnidad           as m_nIdUnidad\n" +
                    "     , e.FechaSalida        as m_dFechaSalida\n" +
                    "     , e.HoraSalida         as m_tHoraSalida\n" +
                    "     , e.FechaEntrega       as m_dFechaEntrega\n" +
                    "     , e.HoraEntrega        as m_tHoraEntrega\n" +
                    "     , e.FechaLlegada       as m_dFechaLlegada\n" +
                    "     , e.HoraLlegada        as m_tHoraLlegada\n" +
                    "     , e.TipoTimbrado        as m_nTipoTimbrado\n" +
                    "     , e.IdEmbarqueRelacionado                         as m_nIdEmbarqueRelacionado\n" +
//                "     , (Select de.FolioEmbarque\n" +
//                "        From ProEmbarquePQ de\n" +
//                "        where de.IdEmbarque = e.IdEmbarqueRelacionado) as m_sFolioRelacionado\n" +
//                "     , (Select top(1) de.IdGuia\n" +
//                "        From ProEmbarquePQ de\n" +
//                "        where de.IdEmbarque = e.IdEmbarqueRelacionado) as m_nIdGuiaRelacionada\n" +
//                "     , (Select de.FolioGuia\n" +
//                "        From ProGuiaPQ de\n" +
//                "        where de.IdEmbarque = e.IdEmbarqueRelacionado) as m_sFolioGuiaRelacionada\n" +
                    "     , e.IdZonaTarifa                                  as m_nIdZonaTarifa\n" +
                    "     , (Select de.CodigoZona\n" +
                    "        From CatZonasTarifasPQ de\n" +
                    "        where de.IdZona = e.IdZonaTarifa)              as m_sZonaTarifaEntrega\n" +
                    "     , e.idRuta                                        as m_nIdRuta\n" +
                    "     , IIF(e.IdEstatusEmbarque = 21 or e.IdGuia > 0, CAST(0 AS BIT), CAST(1 AS BIT)) as m_bSePuedeCancelar\n" +
                    "FROM ProEmbarquePQ e\n" +
                    "         left join ProGuiaPQ g on g.IdGuia = e.IdGuia\n" +
                    "         left join ProRecoleccionPQ pr on pr.IdRecoleccion = e.IdRecoleccion\n" +
                    "         left join ProInformePQ pi on pi.IdInforme = e.IdInforme\n" +
                    "         left join CatCotizacion cc on cc.IdCotizacion = (select top 1 c.IdCotizacion\n" +
                    "                                                          from CatCotizacion c\n" +
                    "                                                          where c.IdEmbarque = e.IdEmbarque\n" +
                    "                                                            and Activa = 1\n" +
                    "                                                          order by c.IdCotizacion desc)\n" +
                    "         left join CatEstatusEmbarquePQ cet on cet.IdEstatusEmbarque = e.IdEstatusEmbarque\n" +
                    "         left join CatZonasTarifasPQ zonaTRec on zonaTRec.IdZona = pr.IdZonaTarifa\n" +
                    "         left join CatZonasOperativasPQ zonaOpEnt on zonaOpEnt.IdZona = e.IdZonaOperativa\n" +
                    "         LEFT JOIN CatRemitentesDestinatarios rem ON rem.IdRemitenteDestinatario = e.IdRemitente\n" +
                    "         LEFT JOIN CatRemitentesDestinatarios dest ON dest.IdRemitenteDestinatario = e.IdDestinatario\n" +
                    "         LEFT JOIN CatEstados edoRem on edoRem.IdEstado = rem.IdEstado\n" +
                    "         LEFT JOIN CatEstados edoDes on edoDes.IdEstado = dest.IdEstado\n" +
                    "         LEFT JOIN CatEstados edoEnt on edoEnt.IdEstado = e.IdEstadoEntrega\n" +
                    "         LEFT JOIN CatPaises psRem on edoRem.IdPais = psRem.IdPais\n" +
                    "         LEFT JOIN CatPaises psDes on edoDes.IdPais = psDes.IdPais\n" +
                    "         LEFT JOIN CatPaises psEnt on edoEnt.IdPais = psEnt.IdPais\n" +
                    "         LEFT JOIN CatCodigosPostales cpEnt ON cpEnt.IdCodigoPostal = e.CodigoPostalEntrega\n" +
                    "LEFT JOIN CatSucursales cs on e.IdSucursal = cs.IdSucursal\n" +
                    "LEFT JOIN CatCiudades cdRem on cdRem.IdCiudad = e.IdCiudadRemitente\n" +
                    "LEFT JOIN CatCiudades cdDes on cdRem.IdCiudad = e.IdCiudadDestinatario\n" +
                    "LEFT JOIN CatOrigenesDestinos cdOrigen on cdOrigen.IdOrigenDestino = e.IdCiudadOrigen\n" +
                    "LEFT JOIN CatOrigenesDestinos cdDestino on cdDestino.IdOrigenDestino = e.IdCiudadDestino\n" +
                    "WHERE e.IdEmbarque = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);
            jsonObject.put("m_arrPaquetes", embarqueService.getPaquetesEmbarque(id, 2, jdbcConnection));
            jsonObject.put("m_arrSobres", embarqueService.getPaquetesEmbarque(id, 1, jdbcConnection));
            jsonObject.put("m_arrClsComplementoSAT", embarqueService.getConceptosSATEmbarque(id, jdbcConnection));
            jsonObject.put("m_arrConceptos", embarqueService.getConceptosCotizacionEmbarque(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Embarque/{id}/notificacion")
    public ResponseEntity<?> enviarNotificacion(@RequestHeader("RFC") String rfc,
                                                @PathVariable("id") int id,
                                                @RequestBody EnvioCorreosRequest request)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select  CCC.CorreoElectronico AS 'Correos' " +
                    "from ProEmbarquePQ PIP inner join CatClientes CC on PIP.IdCliente = CC.IdCliente " +
                    "inner join CatClientesContactos CCC on CC.IdCliente = CCC.IdCliente  where PIP.IdEmbarque = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);
            if (jsonObject != null) {
                String[] correo = new String[1];
                correo[0] = "arenteria@certuit.com";
                embarqueService.enviarCorreoEmbarque(id, jdbcConnection, jsonObject.getString("Correos").split(","), rfc, request.getMensaje());

                return ResponseEntity.ok("Se envió el correo con éxito");
            } else {
                return ResponseEntity.ok("Correo de cliente no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }


    @GetMapping("/Embarques/GetBySucursalMoneda/{sucursal}/{idMoneda}/{idGuia}")
    public ResponseEntity<?> obtenerEmbarqueMoneda(@PathVariable("sucursal") int sucursal,
                                                   @PathVariable("idMoneda") int idMoneda,
                                                   @PathVariable int idGuia,
                                                   @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    " IdEmbarque " +
                    " ,FolioEmbarque " +
                    " FROM ProEmbarquePQ e " +
                    " where e.IdEstatusEmbarque < 17 and IdSucursal =" + sucursal + " and IdMoneda = " + idMoneda + " " +
                    " and (not exists (select 1 from ProGuiaPQ g where g.IdGuia = e.IdGuia and IdGuia <> " + idGuia + ") )";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdEmbarque", rs.getInt("IdEmbarque"));
                json.put("m_sFolioEmbarque", rs.getString("FolioEmbarque"));
                array.put(json);
            }
            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/SisEstatus/GetListadoEmbarque")
    public ResponseEntity<?> obtenerEstatusEmbarque(@RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdEstatusEmbarque as m_nIdEstatusEmbarque,\n" +
                    "\tAbreviacion as m_sAbreviacion,\n" +
                    "\tEstatus as m_sEstatus,\n" +
                    "\tDescripcion as m_sDescripcion,\n" +
                    "\tColor as m_sColor\n" +
                    "\tFROM CatEstatusEmbarquePQ";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/SisEstatus/getListadoInformes")
    public ResponseEntity<?> obtenerEstatusInformes(@RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdEstatusInforme as m_nIdEstatusInforme,\n" +
                    "\tAbreviacion as m_sAbreviacion,\n" +
                    "\tEstatus as m_sEstatus,\n" +
                    "\tDescripcion as m_sDescripcion,\n" +
                    "\tColor as m_sColor\n" +
                    "\tFROM CatEstatusInformePQ";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Embarques/RecopilacionComplementosGuia/{id}")
    public ResponseEntity<?> getComplementosGuia(@PathVariable("id") int id,
                                                 @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select pgpq.FolioGuia,ctcf.ConceptoFacturacion,pgcpq.Importe,pgcpq.ImporteIva," +
                    "pgcpq.ImporteRetiene \n" +
                    "\n" +
                    ",concat(cto.OrigenDestino,'-',ctd.OrigenDestino) OrigenDestino\n" +
                    "\n" +
                    "from ProEmbarquePQ pepq\n" +
                    "\n" +
                    "inner join ProGuiaPQ pgpq on pgpq.IdEmbarque=pepq.IdEmbarque \n" +
                    "\n" +
                    "inner join ProGuiaConceptoPQ pgcpq on pgcpq.IdGuia=pgpq.IdGuia\n" +
                    "\n" +
                    "inner join CatConceptosFacturacion ctcf on ctcf.IdConceptoFacturacion=pgcpq.IdConceptoFacturacion\n" +
                    "\n" +
                    "left join CatOrigenesDestinos cto on cto.IdOrigenDestino=pepq.IdCiudadOrigen\n" +
                    "\n" +
                    "left join CatOrigenesDestinos ctd on ctd.IdOrigenDestino=pepq.IdCiudadDestino\n" +
                    "\n" +
                    "where pepq.IdEmbarque in (select IdEmbarque from ProEmbarquePQ pe\n" +
                    "\n" +
                    "where pe.IdEmbarque= " + id + "  or pe.IdEmbarqueRelacionado=" + id + " ) \n" +
                    "\n" +
                    "or \n" +
                    "\n" +
                    "pepq.IdEmbarqueRelacionado in  (select IdEmbarque from ProEmbarquePQ pe\n" +
                    "\n" +
                    "where pe.IdEmbarque=" + id + "  or pe.IdEmbarqueRelacionado=" + id + " )\n" +
                    "\n" +
                    "or \n" +
                    "\n" +
                    "pepq.IdEmbarque in (select IdEmbarqueRelacionado from ProEmbarquePQ pe\n" +
                    "\n" +
                    "where pe.IdEmbarque=" + id + "  or pe.IdEmbarqueRelacionado=" + id + " )";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Embarques/Agregar")
    public ResponseEntity<?> postEmbarque(@RequestBody EmbarqueRequest embRequest,
                                          @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        int esEntregarMismoDomicilio;
        if (embRequest.isEntregarMismoDomicilio()) {
            esEntregarMismoDomicilio = 1;
        } else {
            esEntregarMismoDomicilio = 0;
        }
        int esEntregaConCita;
        if (embRequest.isM_bEmbarqueConCita()) {
            esEntregaConCita = 1;
        } else {
            esEntregaConCita = 0;
        }
        int esEntregaSucursal;
        if (embRequest.isM_bEntregaEnSucursal()) {
            esEntregaSucursal = 1;
        } else {
            esEntregaSucursal = 0;
        }
        int aplicaSeguro;
        if (embRequest.isM_bAplicaSeguro()) {
            aplicaSeguro = 1;
        } else {
            aplicaSeguro = 0;
        }
        int citaPendiente;
        if (embRequest.isM_bCitaPendiente()) {
            citaPendiente = 1;
        } else {
            citaPendiente = 0;
        }
        int hayRecoleccion;
        if (embRequest.isM_bEsRecolecta()) {
            hayRecoleccion = 1;
        } else {
            hayRecoleccion = 0;
        }
        int embarqueConCita;
        if (embRequest.isM_bEmbarqueConCita()) {
            embarqueConCita = 1;
        } else {
            embarqueConCita = 0;
        }
        try (Connection jdbcConnection = dbConection.getconnection(rfc))
        {
            String query = "SELECT IdCliente FROM CatClientes WHERE Activo=1 AND IdCliente=" + embRequest.getM_nIdCliente();
            JSONObject cliente = convertObject(jdbcConnection.createStatement().executeQuery(query));
            if (cliente == null) {
                return ResponseEntity.status(500)
                        .body("El cliente no existe o se inactivó, favor de validar en catálogo de clientes.");
            }

            query = "EXEC usp_ProEmbarqueAgregarPQ null," + embRequest.getM_nIdRecoleccion() + ",null,null" +
                    ",null,null,'" + embRequest.getM_dFecha() + "','" + embRequest.getM_sHora() + "',"
                    + embRequest.getM_nIdEstatusEmbarque() + "," + embRequest.getM_nIdMoneda() + ","
                    + embRequest.getM_cTIpoCambio() + "," + embRequest.getM_nIdTIpoCobro() + ",'"
                    + embRequest.getM_sNOmbreRemitente() + "','" + embRequest.getM_sRFCRemitente() + "','"
                    + embRequest.getM_sDomicilioRemitente() + "'," + embRequest.getM_nIdCodigoPostalRemitente() + ","
                    + embRequest.getM_nIdCiudadRemitente() + ",'" + embRequest.getM_sCorreoRemitente() + "','"
                    + embRequest.getM_sTelefonoRemitente() + "','" + embRequest.getM_sContactoRemitente() + "',"
                    + embRequest.getM_nIdCiudadOrigen() + ",'" + embRequest.getM_sNombreDestinatario() + "','"
                    + embRequest.getM_sRFCDestinatario() + "','" + embRequest.getM_sDomicilioDestinatario() + "',"
                    + embRequest.getM_nIdCodigoPostalDestinatario() + "," + embRequest.getM_nIdCIudadDestinatario()
                    + ",'" + embRequest.getM_sCorreoDestinatario() + "','" + embRequest.getM_sTelefonoDestinatario()
                    + "','" + embRequest.getM_sContactoDestinatario() + "'," + embRequest.getM_nIdCiudadDestino()
                    + ",'" + embRequest.getM_dFechaEntrega() + "'" + ",'" + embRequest.getM_tHoraEntrega() + "',"
                    + embRequest.getM_nNoPaquetes() + "," + embRequest.getM_nNoSobres() + ","
                    + embRequest.getM_nIdOperador() + "," + embRequest.getM_nIdUnidad() + ","
                    + esEntregarMismoDomicilio + ",'" + embRequest.getFechaLlegada() + "','"
                    + embRequest.getHoraLlegada() + "'," + embRequest.getCodigoPostalEntrega() + ","
                    + embRequest.getIdCiudadEntrega() + "," + embRequest.getIdZonaEntrega() + ",'"
                    + embRequest.getDomicilioEntrega() + "','" + embRequest.getEntregarEn() + "','"
                    + embRequest.getDatosAdicionales() + "'," + embRequest.getIdSucursal() + ", "
                    + esEntregaSucursal + ", " + embRequest.getM_nIdSucursalEntrega() + ", "
                    + embRequest.getM_nIdCliente() + ", " + embRequest.getM_nIdZonaRemitente() + ","
                    + embRequest.getM_nIdZonaDestinatario() + "," + embRequest.getM_nIdRemitente() + ", "
                    + embRequest.getM_nIdDestinatario() + ",'" + embRequest.getM_sAliasRemitente() + "','"
                    + embRequest.getM_sAliasDestinatario() + "'" + ",'" + embRequest.getM_sCalleRemitente() + "','"
                    + embRequest.getM_sNoIntRemitente() + "','" + embRequest.getM_sNoExtRemitente() + "','"
                    + embRequest.getM_sColoniaRemitente() + "'" + ",'" + embRequest.getM_sCalleDestinatario() + "','"
                    + embRequest.getM_sNoIntDestinatario() + "','" + embRequest.getM_sNoExtDestinatario() + "','"
                    + embRequest.getM_sColoniaDestinatario() + "', " + esEntregaConCita + ", '"
                    + embRequest.getM_sFechaCita() + "', '" + embRequest.getM_sHoraCitaMinima() + "','"
                    + embRequest.getM_sHoraCitaMaxima() + "', " + embRequest.getM_nIdEmbarqueRelacionado() + ", '"
                    + embRequest.getM_sLatitudD() + "', '" + embRequest.getM_sLongitudD() + "'" + ","
                    + embRequest.getM_nIdZonaOperativa() + "," + embRequest.getM_nIdZonaTarifa() + ",'"
                    + embRequest.getM_nIdEstadoRemitente() + "','" + embRequest.getM_nIdEstadoDestinatario() + "','"
                    + embRequest.getM_sMunicipioRemitente() + "','" + embRequest.getM_sMunicipioDestinatario() + "',"
                    + embRequest.getM_nIdZonaOperativaRecoleccion() + "," + embRequest.getM_nIdZonaTarifaRecoleccion()
                    + ",'" + embRequest.getM_nIdEstadoEntrega() + "','" + embRequest.getM_sCodigoMunicipioEntrega()
                    + "', " + embRequest.getValorDeclarado() + ", " + embRequest.getM_nIdTipoSeguro() + ","
                    + embRequest.getM_xPorcentajeSeguro() + "," + aplicaSeguro + "," + citaPendiente + ","
                    + embRequest.getM_nIdRuta() + "," + embRequest.getCreadoPor() + ", '"
                    + embRequest.getM_sObservaciones() + "'," + embRequest.getM_nIdComplemento() + ", "
                    + embRequest.getM_nIdTipoDocumento() + ", " + (embRequest.isM_bValidarTimbradoIngreso() ? 1 : 0)
                    + ", " + embRequest.getM_nTipoTimbrado() + addString(embRequest.getM_sReferencia());
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs >= 0) {
                    query = "SELECT IDENT_CURRENT( 'ProEmbarquePQ' ) as id";
                    ResultSet resultSet = statement.executeQuery(query);
                    JSONObject jsonResult = convertObject(resultSet);
                    embRequest.setM_nIdEmbarque(jsonResult.getInt("id"));
                    query = "select RecoleccionConCita From ProRecoleccionPQ where IdRecoleccion = "
                            + embRequest.getM_nIdRecoleccion();
                    resultSet = statement.executeQuery(query);
                    jsonResult = convertObject(resultSet);
                    if (jsonResult != null) {
                        embRequest.setM_bRecoleccionConCita(jsonResult.getBoolean("RecoleccionConCita"));
                    } else {
                        embRequest.setM_bRecoleccionConCita(false);
                    }
                    if (embRequest.getM_arrClsDetalle().size() > 0) {
                        paquetesService.agregarPaquetesAEmbarque(embRequest, statement);
                    }
                    if (embRequest.getM_arrClsComplementoSAT().size() > 0) {
                        complementosService.agregarComplementosAEmbarque(embRequest, jdbcConnection);
                    }
                    if (embRequest.getM_arrConceptos().size() > 0) {
                        CotizacionRequest cotizacionRequest = new CotizacionRequest();
                        cotizacionRequest.setIdOrigen(embRequest.getM_nIdCiudadOrigen());
                        cotizacionRequest.setIdDestino(embRequest.getM_nIdCiudadDestino());
                        cotizacionRequest.setIdZonaEntrega(embRequest.getIdZonaEntrega());
                        cotizacionRequest.setIdCliente(embRequest.getM_nIdCliente());
                        cotizacionRequest.setEntregaEnSucursal(embRequest.isM_bEntregaEnSucursal());
                        cotizacionRequest.setValorDeclarado(embRequest.getValorDeclarado());
                        cotizacionRequest.setAplicaRecoleccion(embRequest.isM_bEsRecolecta());
                        cotizacionRequest.setIdSeguro(embRequest.getM_nIdTipoSeguro());
                        cotizacionRequest.setAplicaSeguro(embRequest.isM_bAplicaSeguro());
                        cotizacionRequest.setPorcentajeSeguro(embRequest.getM_xPorcentajeSeguro());
                        cotizacionRequest.setIdEmbarque(embRequest.getM_nIdEmbarque());
                        cotizacionRequest.setIdRecoleccion(embRequest.getM_nIdRecoleccion());
                        cotizacionRequest.setIdZonaRecoleccion(embRequest.getM_nIdZonaOperativaRecoleccion());
                        cotizacionRequest.setRecoleccionConCita(embRequest.isM_bRecoleccionConCita());
                        cotizacionRequest.setEmbarqueConCita(embRequest.isM_bEmbarqueConCita());

                        List<PaqueteCotizadorRequest> paquetesCotizacion = new java.util.ArrayList<>(Collections.emptyList());
                        for (PaqueteRequest paquete : embRequest.getM_arrClsDetalle()) {
                            PaqueteCotizadorRequest paqueteCotizadorRequest = new PaqueteCotizadorRequest();
                            paqueteCotizadorRequest.setEmbajale(paquete.getClaveEmbalaje());
                            paqueteCotizadorRequest.setTipo(paquete.getM_nTipo());
                            paqueteCotizadorRequest.setDescripcion(paquete.getM_sDescripcion());
                            paqueteCotizadorRequest.setPeso(paquete.getM_xPeso());
                            paqueteCotizadorRequest.setLargo(paquete.getM_xLargo());
                            paqueteCotizadorRequest.setAncho(paquete.getM_xAncho());
                            paqueteCotizadorRequest.setAlto(paquete.getM_xAlto());
                            paqueteCotizadorRequest.setVolumen(paquete.getM_xVolumen());
                            paqueteCotizadorRequest.setIdTipoEmpaque(paquete.getM_nIdTIpoEmpaque());
                            paqueteCotizadorRequest.setValorDeclarado(paquete.getM_cValorDeclarado());
                            paqueteCotizadorRequest.setObservaciones(paquete.getM_sObservaciones());
                            paqueteCotizadorRequest.setCtd(paquete.getCtd());
                            paqueteCotizadorRequest.setIdProducto(paquete.getM_nIdProducto());
                            paqueteCotizadorRequest.setActivo(false);
                            paquetesCotizacion.add(paqueteCotizadorRequest);
                        }
                        cotizacionRequest.setPaquetesCotizacion(paquetesCotizacion);
                        rs = cotizadorService.agregarCotizacionEmbarque(cotizacionRequest, statement);
                        embRequest.setM_nIdCotizacion(rs);
                        conceptosService.agregarConceptosAEmbarque(embRequest, statement);

                    }

                    return getEmbarqueId(embRequest.getM_nIdEmbarque(), rfc);
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Embarques/Modificar/{idEmbarque}")
    public ResponseEntity<?> putEmbarque(@PathVariable("idEmbarque") int idEmbarque,
                                         @RequestBody EmbarqueRequest embRequest,
                                         @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        int esEntregarMismoDomicilio;
        if (embRequest.isEntregarMismoDomicilio()) {
            esEntregarMismoDomicilio = 1;
        } else {
            esEntregarMismoDomicilio = 0;
        }
        int esEntregaConCita;
        if (embRequest.isM_bEmbarqueConCita()) {
            esEntregaConCita = 1;
        } else {
            esEntregaConCita = 0;
        }
        int esEntregaSucursal;
        if (embRequest.isM_bEntregaEnSucursal()) {
            esEntregaSucursal = 1;
        } else {
            esEntregaSucursal = 0;
        }
        int aplicaSeguro;
        if (embRequest.isM_bAplicaSeguro()) {
            aplicaSeguro = 1;
        } else {
            aplicaSeguro = 0;
        }
        int citaPendiente;
        if (embRequest.isM_bCitaPendiente()) {
            citaPendiente = 1;
        } else {
            citaPendiente = 0;
        }
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdCliente FROM CatClientes WHERE Activo=1 AND IdCliente="
                    + embRequest.getM_nIdCliente();
            JSONObject cliente = convertObject(jdbcConnection.createStatement().executeQuery(query));
            if (cliente == null) {
                return ResponseEntity.status(500)
                        .body("El cliente no existe o se inactivó, favor de validar en catálogo de clientes.");
            }
            query = "EXEC usp_ProEmbarqueModificarPQ " + embRequest.getM_nIdEmbarque() + ",null,null,null"
                    + "," + embRequest.getM_nIdEstatusEmbarque() + "," + embRequest.getM_nIdMoneda()
                    + "," + embRequest.getM_cTIpoCambio()
                    + "," + embRequest.getM_nIdTIpoCobro() + ",'" + embRequest.getM_sNOmbreRemitente()
                    + "','" + embRequest.getM_sRFCRemitente() + "','" + embRequest.getM_sDomicilioRemitente()
                    + "'," + embRequest.getM_nIdCodigoPostalRemitente()
                    + "," + embRequest.getM_nIdCiudadRemitente() + ",'" + embRequest.getM_sCorreoRemitente()
                    + "','" + embRequest.getM_sTelefonoRemitente() + "','" + embRequest.getM_sContactoRemitente()
                    + "'," + embRequest.getM_nIdCiudadOrigen()
                    + ",'" + embRequest.getM_sNombreDestinatario() + "','" + embRequest.getM_sRFCDestinatario()
                    + "','" + embRequest.getM_sDomicilioDestinatario()
                    + "'," + embRequest.getM_nIdCodigoPostalDestinatario() + "," + embRequest.getM_nIdCIudadDestinatario()
                    + ",'" + embRequest.getM_sCorreoDestinatario() + "','" + embRequest.getM_sTelefonoDestinatario()
                    + "','" + embRequest.getM_sContactoDestinatario() + "'," + embRequest.getM_nIdCiudadDestino()
                    + ",'" + embRequest.getM_dFechaEntrega() + "'"
                    + ",'" + embRequest.getM_tHoraEntrega() + "'," + embRequest.getM_nNoPaquetes()
                    + "," + embRequest.getM_nNoSobres()
                    + "," + embRequest.getM_nIdOperador() + "," + embRequest.getM_nIdUnidad()
                    + "," + esEntregarMismoDomicilio + ",'" + embRequest.getFechaLlegada()
                    + "','" + embRequest.getHoraLlegada() + "'," + embRequest.getCodigoPostalEntrega()
                    + "," + embRequest.getIdCiudadEntrega()
                    + "," + embRequest.getIdZonaEntrega() + ",'" + embRequest.getDomicilioEntrega()
                    + "','" + embRequest.getEntregarEn()
                    + "','" + embRequest.getDatosAdicionales() + "'," + embRequest.getIdSucursal()
                    + ", " + esEntregaSucursal + ", " + embRequest.getM_nIdSucursalEntrega() + ", null, "
                    + embRequest.getM_nIdCliente() + ", " + embRequest.getM_nIdZonaRemitente()
                    + "," + embRequest.getM_nIdZonaDestinatario() + "," + embRequest.getM_nIdRemitente()
                    + ", " + embRequest.getM_nIdDestinatario() + ",'" + embRequest.getM_sAliasRemitente()
                    + "','" + embRequest.getM_sAliasDestinatario() + "'"
                    + ",'" + embRequest.getM_sCalleRemitente() + "','" + embRequest.getM_sNoIntRemitente()
                    + "','" + embRequest.getM_sNoExtRemitente() + "','" + embRequest.getM_sColoniaRemitente() + "'"
                    + ",'" + embRequest.getM_sCalleDestinatario() + "','" + embRequest.getM_sNoIntDestinatario()
                    + "','" + embRequest.getM_sNoExtDestinatario() + "','" + embRequest.getM_sColoniaDestinatario()
                    + "', " + esEntregaConCita + ", '" + embRequest.getM_sFechaCita()
                    + "', '" + embRequest.getM_sHoraCitaMinima() + "','" + embRequest.getM_sHoraCitaMaxima()
                    + "', '" + embRequest.getM_sLatitudD() + "', '" + embRequest.getM_sLongitudD() + "'"
                    + "," + embRequest.getM_nIdZonaOperativa() + "," + embRequest.getM_nIdZonaTarifa()
                    + ",'" + embRequest.getM_nIdEstadoRemitente() + "','" + embRequest.getM_nIdEstadoDestinatario()
                    + "','" + embRequest.getM_sMunicipioRemitente() + "','" + embRequest.getM_sMunicipioDestinatario()
                    + "'," + embRequest.getM_nIdZonaOperativaRecoleccion()
                    + "," + embRequest.getM_nIdZonaTarifaRecoleccion()
                    + ",'" + embRequest.getM_nIdEstadoEntrega() + "','" + embRequest.getM_sCodigoMunicipioEntrega()
                    + "', " + embRequest.getValorDeclarado() + ", " + embRequest.getM_nIdTipoSeguro()
                    + "," + embRequest.getM_xPorcentajeSeguro() + "," + aplicaSeguro + "," + citaPendiente
                    + "," + embRequest.getM_nIdRuta()
                    + ", '" + embRequest.getM_sObservaciones()
                    + "'," + embRequest.getM_nIdComplemento() + ", " + embRequest.getM_nIdTipoDocumento()
                    + ", " + (embRequest.isM_bValidarTimbradoIngreso() ? 1 : 0) + "," + embRequest.getM_nTipoTimbrado()
                    + addString(embRequest.getM_sReferencia());
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs >= 0) {
                    if (embRequest.getM_arrClsDetalle().size() > 0) {
                        paquetesService.agregarPaquetesAEmbarque(embRequest, statement);
                    }
                    if (embRequest.getM_arrClsComplementoSAT().size() > 0) {
                        complementosService.agregarComplementosAEmbarque(embRequest, jdbcConnection);
                    }
                    if (embRequest.getM_arrConceptos().size() > 0) {
                        conceptosService.agregarConceptosAEmbarque(embRequest, statement);
                    }

                    return ResponseEntity.ok("Se guardó el embarque correctamente");
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/Embarques/Eliminar/{idEmbarque}/{idUsuario}")
    public ResponseEntity<?> deleteEmbarque(@PathVariable("idEmbarque") int idEmbarque,
                                            @PathVariable("idUsuario") int idUsuario,
                                            @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProEmbarqueEliminarPQ " + idEmbarque + "";
            String query2 = "select ExisteGuia = count(*)\n" +
                    "\t\tfrom dbo.ProEmbarquePQ g\n" +
                    "\t\twhere g.IdEmbarque = " + idEmbarque + " and (IdGuia is not null and IdGuia > 0)";
            String query3 = "select ExisteGuia = count(*)\n" +
                    "\t\tfrom dbo.ProEmbarquePQ g\n" +
                    "\t\twhere g.IdEmbarque = " + idEmbarque + " and IdEstatusEmbarque <> 21;";
            try {

                Statement statement2 = jdbcConnection.createStatement();
                ResultSet rs2 = statement2.executeQuery(query2);
                JSONObject existeGuia = convertObject(rs2);
                if (existeGuia.getInt("ExisteGuia") > 0) {
                    return ResponseEntity.status(500)
                            .body("No es posible eliminar el embarque ya que tiene una guia asignada");
                }
                Statement statement3 = jdbcConnection.createStatement();
                ResultSet rs3 = statement3.executeQuery(query3);
                JSONObject estatusEmbarque = convertObject(rs3);
                if (estatusEmbarque.getInt("ExisteGuia") > 0) {
                    return ResponseEntity.status(500).body("Para eliminar el embarque es necesario que este cancelado");
                }
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al eliminar el embarque");
            }
            return ResponseEntity.ok("Eliminado exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Embarques/Cancelar/{idEmbarque}")
    public ResponseEntity<?> cancelarEmbarque(@RequestBody EmbarqueCancelarRequest cancelado,
                                              @PathVariable("idEmbarque") int idEmbarque,
                                              @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProEmbarqueCancelarPQ  " + idEmbarque + "," + cancelado.getUsuarioCancelacion()
                    + ", '" + cancelado.getFechaCancelacion() + "', '" + cancelado.getMotivoCancelacion() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al cancelar el embarque");
            }

            return ResponseEntity.ok("Cancelado exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Embarques/validar-importacion")
    public ResponseEntity<?> validarEmbarquesImportados(@RequestBody ImportacionRequest request,
                                                        @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        JSONObject jsonAux = new JSONObject();
        JSONArray jsonArrayAux = new JSONArray();
        JSONArray jsonListadoEmbarquesFinal = new JSONArray();
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs;
                String query = "";
                for (int i = 0; i < request.getEmbarques().size(); i++) {
                    for (int j = i + 1; j < request.getEmbarques().size(); j++) {
                        if (request.getEmbarques().get(i).getNumeroEmbarque() == request.getEmbarques().get(j).getNumeroEmbarque()) {
                            return ResponseEntity.ok("Los números de embarques no puden ser duplicados en la hoja con embarques");
                        }
                    }
                }
                for (EmbarqueImportadoRequest embarque : request.getEmbarques()) {
                    JSONObject jsonEmbarqueEstatus = new JSONObject();
                    jsonEmbarqueEstatus.put("success", true);
                    jsonEmbarqueEstatus.put("message", "");
                    jsonEmbarqueEstatus.put("data", new JSONObject());
                    jsonEmbarqueEstatus.put("numeroEmbarque", embarque.getNumeroEmbarque());
                    JSONObject jsonEmbarque = new JSONObject();
                    try {
                        //AQUI EL ID CLIENTE QUE VIENE ES EL DE LA PLANTILLA. ES PARA LO UNICO QUE SE USARÁ,
                        // MÁS ADELANTE SERÁ REEMPLAZADO POR EL QUE SERÁ RESPONSABLE DE PAGO
                        query = "SELECT top 1 IdTipoPlantilla, " +
                                "IIF(IdTipoPlantilla = 2, UsarNumeroEquivalenciaDestinatario, " +
                                "CAST(0 as bit)) as UsarNumeroEquivalenciaDestinatario," +
                                "IIF(IdTipoPlantilla = 2, UsarNumeroEquivalenciaResponsablePago, " +
                                "CAST(0 as bit)) as UsarNumeroEquivalenciaResponsablePago " +
                                "FROM CatPlantillasImportacionPQ where IdCliente = " + embarque.getIdCliente();
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
//                        if (embarque.getValorDeclarado() <= 0) {
//                            jsonEmbarqueEstatus.put("success", false);
//                            jsonEmbarqueEstatus.put("message", "El Valor Declarado no puede ser igual o menor a 0");
//                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
//                            continue;
//                        }
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró el tipo de plantilla usado. " +
                                    "Verifique que en el catálogo de plantillas.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idTipoPlantilla", jsonAux.getInt("IdTipoPlantilla"));
                            jsonEmbarque.put("usarNumeroEquivalenciaDestinatario",
                                    jsonAux.getBoolean("UsarNumeroEquivalenciaDestinatario"));
                            jsonEmbarque.put("usarNumeroEquivalenciaResponsablePago",
                                    jsonAux.getBoolean("UsarNumeroEquivalenciaResponsablePago"));
                        }

                        //Dato REFERENCIA
                        jsonEmbarque.put("referencia", embarque.getReferencia());

                        //Datos de moneda tipo plantilla Lineal = 2, segmentada = 1
                        if (embarque.getMoneda().equals("DOLARES")) {
                            embarque.setMoneda("DÓLARES");
                        }
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            query = "SELECT top 1 idMoneda=IdMoneda, moneda=Moneda FROM CatMonedasPQ " +
                                    "where UPPER(Moneda) = UPPER('" + embarque.getMoneda() + "')";
                        } else {
                            query = "SELECT top 1 idMoneda=IdMoneda, moneda=Moneda FROM CatParametrosConfiguracion2PQ\n" +
                                    "                                              inner JOIN CatMonedasPQ " +
                                    "on IdMoneda = MonedaEmbarque";
                        }
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró el valor moneda ingresado. " +
                                    "Verifique que el registro exista en el ERP.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idMoneda", jsonAux.getInt("idMoneda"));
                            jsonEmbarque.put("moneda", jsonAux.getString("moneda"));
                        }

                        //Datos de tipo de cambio
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            query = "SELECT top 1 idTipoCambio=IdTipoCambio, tipoCambio=TipoCambio " +
                                    "FROM CatTiposCambio where UPPER(cast(TipoCambio as float)) = " +
                                    "UPPER(CAST('" + embarque.getTipoCambio() + "' as float))";
                        } else {
                            query = "SELECT top 1 idTipoCambio=IdTipoCambio, tipoCambio=TipoCambio " +
                                    "FROM CatTiposCambio order by IdTipoCambio desc";
                        }
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró el valor tipo de cambio ingresado. " +
                                    "Verifique que el registro exista en el ERP.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idTipoCambio", jsonAux.getInt("idTipoCambio"));
                            jsonEmbarque.put("tipoCambio", jsonAux.getDouble("tipoCambio"));
                        }

                        //Datos de tipo de cobro*/
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            if (embarque.getTipoCobro().equals("CREDITO")) {
                                embarque.setTipoCobro("CRÉDITO");
                            }
                            query = "SELECT top 1 idTipoCobro=IdTipoCobro, tipoCobro=Descripcion " +
                                    "FROM CatTipoCobroPQ where UPPER(Descripcion) = " +
                                    "UPPER('" + embarque.getTipoCobro() + "')";
                        } else {
                            query = "SELECT top 1 idTipoCobro=IdTipoCobro, tipoCobro=Descripcion " +
                                    "FROM CatParametrosConfiguracion2PQ\n" +
                                    "inner join CatTipoCobroPQ on IdTipoCobro = TipoCobro";
                        }
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró el valor tipo de cobro ingresado. " +
                                    "Verifique que el registro exista en el ERP.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idTipoCobro", jsonAux.getInt("idTipoCobro"));
                            jsonEmbarque.put("tipoCobro", jsonAux.getString("tipoCobro"));
                        }

                        //Datos de  cliente/Responsable pago
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            query = "SELECT numeroCliente=NumeroCliente, idCliente=IdCliente, cliente=NombreFiscal,\n " +
                                    "aplicaSeguro=(IIF(IdTipoSeguro = 1,CAST(0 AS BIT),CAST(1 AS BIT)))\n" +
                                    "FROM CatClientes where IdCliente = " + embarque.getIdCliente();
                        } else {
                            if (jsonEmbarque.getBoolean("usarNumeroEquivalenciaResponsablePago")) {
                                query = "SELECT numeroCliente=cliente.NumeroCliente,\n" +
                                        "       idCliente=cliente.IdCliente,\n" +
                                        "       cliente=cliente.NombreFiscal,\n" +
                                        "       aplicaSeguro=(IIF(cliente.IdTipoSeguro = 1,CAST(0 AS BIT),CAST(1 AS BIT)))\n" +
                                        "FROM CatClientes cliente\n" +
                                        "inner JOIN CatRemitentesDestinatarios rd on cliente.IdCliente = rd.IdCliente\n" +
                                        "where rd.NoEquivalencia =  " + embarque.getResponsablePago();
                            } else {
                                query = "SELECT numeroCliente=cliente.NumeroCliente,\n" +
                                        "       idCliente=cliente.IdCliente,\n" +
                                        "       cliente=cliente.NombreFiscal,\n" +
                                        "       aplicaSeguro=(IIF(cliente.IdTipoSeguro = 1,CAST(0 AS BIT),CAST(1 AS BIT)))\n" +
                                        "FROM CatClientes cliente\n" +
                                        "inner JOIN CatRemitentesDestinatarios rd on cliente.IdCliente = rd.IdCliente\n" +
                                        "where rd.Numero =  " + embarque.getResponsablePago();
                            }

                        }
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró cliente ingresado. " +
                                    "Verifique que el registro exista en el ERP.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("numeroCliente", jsonAux.getInt("numeroCliente"));
                            jsonEmbarque.put("idCliente", jsonAux.getInt("idCliente"));
                            jsonEmbarque.put("cliente", jsonAux.getString("cliente"));
                            jsonEmbarque.put("aplicaSeguro", jsonAux.getBoolean("aplicaSeguro"));
                            embarque.setIdCliente(jsonAux.getInt("idCliente"));
                        }

                        //Datos de tipo de seguro */
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            query = "SELECT top 1 \n" +
                                    "idTipoSeguro=IdTipoSeguro," +
                                    "tipoSeguro=Descripcion," +
                                    "porcentajeSeguro=Porcentaje, " +
                                    "aplicaTarifa=AplicaTarifa " +
                                    "FROM CatTipoSeguro where UPPER(Descripcion) = UPPER('" + embarque.getTipoSeguro() + "')";
                        } else {
                            query = "SELECT top 1   \n" +
                                    "idTipoSeguro=CatClientes.IdTipoSeguro,\n" +
                                    "tipoSeguro=Descripcion,\n" +
                                    "porcentajeSeguro=CatClientes.PorcentajeSeguro,\n" +
                                    "aplicaTarifa=AplicaTarifa,\n" +
                                    "aseguradora=ISNULL(Aseguradora, ''),\n" +
                                    "poliza=ISNULL(Poliza,'')\n" +
                                    "FROM CatClientes\n" +
                                    "inner JOIN CatTipoSeguro on CatClientes.IdTipoSeguro = CatTipoSeguro.IdTipoSeguro\n" +
                                    "where IdCliente = " + embarque.getIdCliente();
                        }
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró seguro registrado para el cliente. " +
                                    "Verifique el registro en el catalogo de seguros.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            if (embarque.getTipoSeguro().equalsIgnoreCase("Seguro Obligatorio")
                                    && embarque.getPorcentajeSeguro() <= 0) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "Los embarques con Seguro Obligatorio necesitan " +
                                        "un porcentaje de pago mayor o igual a 0.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            }
                            jsonEmbarque.put("idTipoSeguro", jsonAux.getInt("idTipoSeguro"));
                            jsonEmbarque.put("tipoSeguro", jsonAux.getString("tipoSeguro"));
                            jsonEmbarque.put("valorDeclarado", embarque.getValorDeclarado());

                            if (jsonAux.getInt("idTipoSeguro") == 1) {
                                jsonEmbarque.put("observaciones", "Aseguradora: "
                                        + jsonAux.getString("aseguradora") + ", Póliza:"
                                        + jsonAux.getString("poliza"));
                            } else {
                                jsonEmbarque.put("observaciones", embarque.getObservaciones());
                            }
                            if (Objects.equals(jsonAux.getString("aplicaTarifa"), "Si")) {
                                jsonEmbarque.put("porcentajeSeguro", jsonAux.getDouble("porcentajeSeguro"));
                            } else {
                                jsonEmbarque.put("porcentajeSeguro", 0);
                            }
                        }

                        //Datos validar timbrado guia*/
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            jsonEmbarque.put("validarTimbradoFactura", embarque.isValidarTimbradoFactura());
                        } else {
                            query = "select top 1 ValidarTimbradoIngreso from CatParametrosConfiguracion2PQ;";
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró indicador de validación timbrado " +
                                        "de facturas. Verifique que el registro en parametros de configuración.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("validarTimbradoFactura", jsonAux.getBoolean("ValidarTimbradoIngreso"));
                            }
                        }

                        //Datos tipo de servicio*/
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            if (!embarque.getTipoServicio().equalsIgnoreCase("CONSOLIDADO") &&
                                    !embarque.getTipoServicio().equalsIgnoreCase("PAQUETERIA") &&
                                    !embarque.getTipoServicio().equalsIgnoreCase("PAQUETERÍA")) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró el tipo de servicio ingresado.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("tipoServicio", embarque.getTipoServicio());
                                if (embarque.getTipoServicio().equalsIgnoreCase("CONSOLIDADO")) {
                                    jsonEmbarque.put("idTipoServicio", 1);
                                }
                                if (embarque.getTipoServicio().equalsIgnoreCase("PAQUETERIA") ||
                                        embarque.getTipoServicio().equalsIgnoreCase("PAQUETERÍA")) {
                                    jsonEmbarque.put("idTipoServicio", 2);
                                }
                            }
                            //DE MOMENTO SE USARÁ SOLO 1 PARA PRODUCCION
                        } else {
                            query = "select top 1 TipoTimbrado from CatParametrosConfiguracion2PQ;";
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró tipo de servicio en parametros de" +
                                        " configuracion. Verifique que el registro En parametros de configuracion.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idTipoServicio", jsonAux.getInt("TipoTimbrado"));
                            }
                        }

                        //Datos de remitente*/
                        query = "Select top 1  \n" +
                                "    idRemitente=IdRemitenteDestinatario\n" +
                                "    ,nombreRemitente=Nombre\n" +
                                "    ,aliasRemitente=ISNULL(Alias, '')\n" +
                                "    ,rfcRemitente=RFC\n" +
                                "    ,idCodigoPostalRemitente=ISNULL(cp.IdCodigoPostal,0)\n" +
                                "    ,codigoPostalRemitente=rd.CodigoPostal\n" +
                                "    ,idEstadoRemitente=ISNULL(rd.IdEstado, '')\n" +
                                "    ,estadoRemitente=ISNULL(estado.Estado, '')\n" +
                                "    ,idMunicipioRemitente=ISNULL(cp.CodigoMunicipio, 0)\n" +
                                "    ,municipioRemitente=ISNULL(rd.Municipio, '')\n" +
                                "    ,coloniaRemitente=ISNULL(rd.Colonia, '')\n" +
                                "    ,localidadRemitente=ISNULL(rd.Localidad, '')\n" +
                                "    ,calleRemitente=Calle\n" +
                                "    ,numeroInteriorRemitente=ISNULL(NoInterior, '')\n" +
                                "    ,numeroExteriorRemitente=ISNULL(NoExterior, '')\n" +
                                "    ,domicilioRemitente=(rd.Calle + ' ' + rd.NoExterior + ', ' + rd.Colonia)\n" +
                                "    ,paisRemitente=ISNULL(Pais, '')\n" +
                                "    ,telefonoRemitente=ISNULL(rd.Telefono, '')\n" +
                                "    ,correoRemitente=ISNULL(rd.CorreoElectronico, '')\n" +
                                "    ,contactoRemitente=ISNULL(rd.Contacto, '')\n" +
                                "    ,latitudRemitente=ISNULL(rd.Latitud, '')\n" +
                                "    ,longitudRemitente=ISNULL(rd.Longitud, '')\n" +
                                "    ,idCliente=ISNULL(rd.IdCliente, '') \n" +
                                "from CatRemitentesDestinatarios rd \n" +
                                "left join CatCodigosPostales cp \n" +
                                "    on cp.IdCodigoPostal=(select top 1 cp2.IdCodigoPostal \n" +
                                "                          from CatCodigosPostales cp2 \n" +
                                "                          where UPPER(cp2.IdEstado) = UPPER(rd.IdEstado) \n" +
                                "                          and UPPER(cp2.Colonia) = UPPER(rd.Colonia) \n" +
                                "                          and UPPER(cp2.Municipio) = UPPER(rd.Municipio)) \n" +
                                "left join CatEstados estado on estado.IdEstado = rd.IdEstado \n" +
                                "left join CatPaises pais on pais.IdPais = estado.IdPais \n" +
                                "where Numero =" + embarque.getNumeroRemitente();
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró remitente ingresado. " +
                                    "Verifique que el registro exista en el ERP.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idRemitente", jsonAux.getInt("idRemitente"));
                            jsonEmbarque.put("nombreRemitente", jsonAux.getString("nombreRemitente"));
                            jsonEmbarque.put("aliasRemitente", jsonAux.getString("aliasRemitente"));
                            jsonEmbarque.put("rfcRemitente", jsonAux.getString("rfcRemitente"));
                            jsonEmbarque.put("telefonoRemitente", jsonAux.getString("telefonoRemitente"));
                            jsonEmbarque.put("correoRemitente", jsonAux.getString("correoRemitente"));
                            jsonEmbarque.put("contactoRemitente", jsonAux.getString("contactoRemitente"));
                        /*if (jsonEmbarque.getInt("idTipoPlantilla") == 2 && embarque.isEsRecoleccion()){
                            jsonEmbarque.put("idCliente", jsonAux.getInt("idCliente"));
                            embarque.setIdCliente(jsonAux.getInt("idCliente"));
                        }*/
                            if (jsonAux.getInt("idCodigoPostalRemitente") == 0) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "El estado, municipio y colonia del remitente no " +
                                        "coinciden con los del listado de codigos postales. Verifique lso datos en el ERP.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idCodigoPostalRemitente", jsonAux.getInt("idCodigoPostalRemitente"));
                                jsonEmbarque.put("codigoPostalRemitente", jsonAux.getString("codigoPostalRemitente"));
                                jsonEmbarque.put("idEstadoRemitente", jsonAux.getString("idEstadoRemitente"));
                                jsonEmbarque.put("estadoRemitente", jsonAux.getString("estadoRemitente"));
                                jsonEmbarque.put("idMunicipioRemitente", jsonAux.getInt("idMunicipioRemitente"));
                                jsonEmbarque.put("municipioRemitente", jsonAux.getString("municipioRemitente"));
                                jsonEmbarque.put("coloniaRemitente", jsonAux.getString("coloniaRemitente"));
                                jsonEmbarque.put("localidadRemitente", jsonAux.getString("localidadRemitente"));
                                jsonEmbarque.put("calleRemitente", jsonAux.getString("calleRemitente"));
                                jsonEmbarque.put("numeroInteriorRemitente", jsonAux.getString("numeroInteriorRemitente"));
                                jsonEmbarque.put("numeroExteriorRemitente", jsonAux.getString("numeroExteriorRemitente"));
                                jsonEmbarque.put("domicilioRemitente", jsonAux.getString("domicilioRemitente"));
                                jsonEmbarque.put("paisRemitente", jsonAux.getString("paisRemitente"));
                                if (jsonEmbarque.getInt("idTipoPlantilla") == 2 && embarque.isEsRecoleccion()
                                        && !embarque.isRecoleccionDiferenteDomicilio()) {
                                    jsonEmbarque.put("latitud", jsonAux.getString("latitudRemitente"));
                                    jsonEmbarque.put("longitud", jsonAux.getString("longitudRemitente"));
                                    if (Objects.equals(jsonAux.getString("latitudRemitente"), "")
                                            || Objects.equals(jsonAux.getString("longitudRemitente"), "")) {
                                        jsonEmbarque.put("solicitarCoordenadas", true);
                                    }
                                }
                            }
                        }

                        //Datos de origen*/
                        query = "SELECT DISTINCT idZonaRecoleccion=z.IdZona," +
                                "idOrigen=z.IdOrigenDestino," +
                                "origen=od.OrigenDestino, " +
                                "zonaRecoleccion=CodigoZona\n" +
                                "FROM CatZonasOperativasCPPQ zcp " +
                                "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                                "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                                "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino " +
                                "WHERE cp.CodigoPostal =  '" + jsonAux.getString("codigoPostalRemitente") + "'";
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró origen para el remitente ingresado. " +
                                    "Verifique que la zona operativa a la que pertenece el remitente tenga un origen registrado.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idOrigen", jsonAux.getInt("idOrigen"));
                            jsonEmbarque.put("origen", jsonAux.getString("origen"));
                            jsonEmbarque.put("idZonaRecoleccion", jsonAux.getInt("idZonaRecoleccion"));
                            jsonEmbarque.put("zonaRecoleccion", jsonAux.getString("zonaRecoleccion"));
                        }

                        //Datos destinatario*/
                        if (jsonEmbarque.getBoolean("usarNumeroEquivalenciaDestinatario")) {
                            query = "Select top 1 \n" +
                                    "    idDestinatario=IdRemitenteDestinatario\n" +
                                    "    ,nombreDestinatario=Nombre\n" +
                                    "    ,aliasDestinatario=ISNULL(Alias, '')\n" +
                                    "    ,rfcDestinatario=RFC\n" +
                                    "    ,idCodigoPostalDestinatario=ISNULL(cp.IdCodigoPostal,0)\n" +
                                    "    ,codigoPostalDestinatario=rd.CodigoPostal\n" +
                                    "    ,idEstadoDestinatario=ISNULL(rd.IdEstado, '')\n" +
                                    "    ,estadoDestinatario=ISNULL(estado.Estado, '')\n" +
                                    "    ,idMunicipioDestinatario=ISNULL(cp.CodigoMunicipio, 0)\n" +
                                    "    ,municipioDestinatario=ISNULL(cp.Municipio, '')\n" +
                                    "    ,coloniaDestinatario=ISNULL(rd.Colonia, '')\n" +
                                    "    ,localidadDestinatario=ISNULL(rd.Localidad, '')\n" +
                                    "    ,calleDestinatario=Calle\n" +
                                    "    ,numeroInteriorDestinatario=ISNULL(NoInterior, '')\n" +
                                    "    ,numeroExteriorDestinatario=ISNULL(NoExterior, '')\n" +
                                    "    ,domicilioDestinatario=(rd.Calle + ' ' + rd.NoExterior + ', ' + rd.Colonia)\n" +
                                    "    ,paisDestinatario=ISNULL(Pais, '')\n" +
                                    "    ,telefonoDestinatario=ISNULL(rd.Telefono, '')\n" +
                                    "    ,correoDestinatario=ISNULL(rd.CorreoElectronico, '')\n" +
                                    "    ,contactoDestinatario=ISNULL(rd.Contacto, '')\n" +
                                    "    ,latitudDestinatario=ISNULL(rd.Latitud, '')\n" +
                                    "    ,longitudDestinatario=ISNULL(rd.Longitud, '')\n" +
                                    "    ,idCliente=ISNULL(rd.IdCliente, '') \n" +
                                    "from CatRemitentesDestinatarios rd\n" +
                                    "left join CatCodigosPostales cp\n" +
                                    "    on cp.IdCodigoPostal=(select top 1 cp2.IdCodigoPostal\n" +
                                    "                          from CatCodigosPostales cp2\n" +
                                    "                          where UPPER(cp2.Colonia) = UPPER(rd.Colonia)\n" +
                                    "                            and UPPER(cp2.Localidad) = UPPER(rd.Localidad))\n" +
                                    "left join CatEstados estado on estado.IdEstado = rd.IdEstado\n" +
                                    "left join CatPaises pais on pais.IdPais = estado.IdPais \n" +
                                    "where NoEquivalencia = '" + embarque.getNumeroDestinatario() + "'";
                        } else {
                            query = "Select top 1 \n" +
                                    "    idDestinatario=IdRemitenteDestinatario\n" +
                                    "    ,nombreDestinatario=Nombre\n" +
                                    "    ,aliasDestinatario=ISNULL(Alias, '')\n" +
                                    "    ,rfcDestinatario=RFC\n" +
                                    "    ,idCodigoPostalDestinatario=ISNULL(cp.IdCodigoPostal,0)\n" +
                                    "    ,codigoPostalDestinatario=rd.CodigoPostal\n" +
                                    "    ,idEstadoDestinatario=ISNULL(rd.IdEstado, '')\n" +
                                    "    ,estadoDestinatario=ISNULL(estado.Estado, '')\n" +
                                    "    ,idMunicipioDestinatario=ISNULL(cp.CodigoMunicipio, 0)\n" +
                                    "    ,municipioDestinatario=ISNULL(cp.Municipio, '')\n" +
                                    "    ,coloniaDestinatario=ISNULL(rd.Colonia, '')\n" +
                                    "    ,localidadDestinatario=ISNULL(rd.Localidad, '')\n" +
                                    "    ,calleDestinatario=Calle\n" +
                                    "    ,numeroInteriorDestinatario=ISNULL(NoInterior, '')\n" +
                                    "    ,numeroExteriorDestinatario=ISNULL(NoExterior, '')\n" +
                                    "    ,domicilioDestinatario=(rd.Calle + ' ' + rd.NoExterior + ', ' + rd.Colonia)\n" +
                                    "    ,paisDestinatario=ISNULL(Pais, '')\n" +
                                    "    ,telefonoDestinatario=ISNULL(rd.Telefono, '')\n" +
                                    "    ,correoDestinatario=ISNULL(rd.CorreoElectronico, '')\n" +
                                    "    ,contactoDestinatario=ISNULL(rd.Contacto, '')\n" +
                                    "    ,latitudDestinatario=ISNULL(rd.Latitud, '')\n" +
                                    "    ,longitudDestinatario=ISNULL(rd.Longitud, '')\n" +
                                    "    ,idCliente=ISNULL(rd.IdCliente, '') \n" +
                                    "from CatRemitentesDestinatarios rd\n" +
                                    "left join CatCodigosPostales cp\n" +
                                    "    on cp.IdCodigoPostal=(select top 1 cp2.IdCodigoPostal\n" +
                                    "                          from CatCodigosPostales cp2\n" +
                                    "                          where UPPER(cp2.Colonia) = UPPER(rd.Colonia)\n" +
                                    "                            and UPPER(cp2.Localidad) = UPPER(rd.Localidad))\n" +
                                    "left join CatEstados estado on estado.IdEstado = rd.IdEstado\n" +
                                    "left join CatPaises pais on pais.IdPais = estado.IdPais \n" +
                                    "where Numero = " + embarque.getNumeroDestinatario();
                        }

                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró destinatario ingresado. Verifique que " +
                                    "el registro exista en el ERP.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idDestinatario", jsonAux.getInt("idDestinatario"));
                            jsonEmbarque.put("nombreDestinatario", jsonAux.getString("nombreDestinatario"));
                            jsonEmbarque.put("aliasDestinatario", jsonAux.getString("aliasDestinatario"));
                            jsonEmbarque.put("rfcDestinatario", jsonAux.getString("rfcDestinatario"));
                            jsonEmbarque.put("telefonoDestinatario", jsonAux.getString("telefonoDestinatario"));
                            jsonEmbarque.put("correoDestinatario", jsonAux.getString("correoDestinatario"));
                            jsonEmbarque.put("contactoDestinatario", jsonAux.getString("contactoDestinatario"));
                        /*if (jsonEmbarque.getInt("idTipoPlantilla") == 2 && !embarque.isEsRecoleccion()){
                            jsonEmbarque.put("idCliente", jsonAux.getInt("idCliente"));
                            embarque.setIdCliente(jsonAux.getInt("idCliente"));
                        }*/
                            if (jsonAux.getInt("idCodigoPostalDestinatario") == 0) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "El estado, municipio y colonia del destinatario " +
                                        "no coinciden con los del listado de codigos postales. Verifique lso datos en el ERP.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idCodigoPostalDestinatario", jsonAux.getInt("idCodigoPostalDestinatario"));
                                jsonEmbarque.put("codigoPostalDestinatario", jsonAux.getString("codigoPostalDestinatario"));
                                jsonEmbarque.put("idEstadoDestinatario", jsonAux.getString("idEstadoDestinatario"));
                                jsonEmbarque.put("estadoDestinatario", jsonAux.getString("estadoDestinatario"));
                                jsonEmbarque.put("idMunicipioDestinatario", jsonAux.getInt("idMunicipioDestinatario"));
                                jsonEmbarque.put("municipioDestinatario", jsonAux.getString("municipioDestinatario"));
                                jsonEmbarque.put("coloniaDestinatario", jsonAux.getString("coloniaDestinatario"));
                                jsonEmbarque.put("localidadDestinatario", jsonAux.getString("localidadDestinatario"));
                                jsonEmbarque.put("calleDestinatario", jsonAux.getString("calleDestinatario"));
                                jsonEmbarque.put("numeroInteriorDestinatario", jsonAux.getString("numeroInteriorDestinatario"));
                                jsonEmbarque.put("numeroExteriorDestinatario", jsonAux.getString("numeroExteriorDestinatario"));
                                jsonEmbarque.put("domicilioDestinatario", jsonAux.getString("domicilioDestinatario"));
                                jsonEmbarque.put("paisDestinatario", jsonAux.getString("paisDestinatario"));
                                if (jsonEmbarque.getInt("idTipoPlantilla") == 2 && !embarque.isEsRecoleccion()
                                        && !embarque.isEntregaEnSucursal() && !embarque.isEntregaDiferenteDomicilio()) {
                                    jsonEmbarque.put("latitud", jsonAux.getString("latitudDestinatario"));
                                    jsonEmbarque.put("longitud", jsonAux.getString("longitudDestinatario"));
                                    if (Objects.equals(jsonAux.getString("latitudDestinatario"), "")
                                            || Objects.equals(jsonAux.getString("longitudDestinatario"), "")) {
                                        jsonEmbarque.put("solicitarCoordenadas", true);
                                    }
                                }
                            }
                        }

                        //Datos de destino*/
                        query = "SELECT DISTINCT idZonaEntrega=z.IdZona," +
                                "idDestino=z.IdOrigenDestino," +
                                "destino=od.OrigenDestino, " +
                                "zonaEntrega=CodigoZona\n" +
                                "FROM CatZonasOperativasCPPQ zcp " +
                                "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                                "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                                "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino " +
                                "WHERE cp.CodigoPostal =  '" + jsonAux.getString("codigoPostalDestinatario") + "'";
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se encontró destino para el destinatario ingresado. " +
                                    "Verifique que la zona operativa a la que pertenece el destinatario tenga un destino registrado.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("idDestino", jsonAux.getInt("idDestino"));
                            jsonEmbarque.put("destino", jsonAux.getString("destino"));
                            jsonEmbarque.put("idZonaEntrega", jsonAux.getInt("idZonaEntrega"));
                            jsonEmbarque.put("zonaEntrega", jsonAux.getString("zonaEntrega"));
                        }

                        //Datos de ruta*/
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            query = "select TOP 1" +
                                    "idRuta=ct.IdRuta," +
                                    "ruta=ct.DescripcionRuta " +
                                    "from CatRutas ct  " +
                                    "where ct.Activa=1 and ct.IdOrigen=" + jsonEmbarque.getInt("idOrigen") +
                                    " and ct.IdDestino=" + jsonEmbarque.getInt("idDestino") +
                                    " and ct.IdCliente=" + jsonEmbarque.getInt("idCliente");
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                query = "select TOP 1" +
                                        "idRuta=ct.IdRuta," +
                                        "ruta=ct.DescripcionRuta " +
                                        "from CatRutas ct  " +
                                        "where ct.Activa=1 and ct.IdOrigen=" + jsonEmbarque.getInt("idOrigen") +
                                        " and ct.IdDestino=" + jsonEmbarque.getInt("idDestino") +
                                        " and ct.IdCliente=(select IdCliente from CatClientes where NombreFiscal " +
                                        "LIKE '%PUBLICO EN GENERAL%')";
                                rs = statement.executeQuery(query);
                                jsonAux = convertObject(rs);
                                if (jsonAux == null) {
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "No se encontró ruta para el origen, destino " +
                                            "y cliente ingresado. Verifique las registradas en el ERP.");
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    continue;
                                } else {
                                    jsonEmbarque.put("idRuta", jsonAux.getInt("idRuta"));
                                    jsonEmbarque.put("ruta", jsonAux.getString("ruta"));
                                }
                            } else {
                                jsonEmbarque.put("idRuta", jsonAux.getInt("idRuta"));
                                jsonEmbarque.put("ruta", jsonAux.getString("ruta"));
                            }
                        } else {
                            query = "select " +
                                    "idRuta=ct.IdRuta," +
                                    "ruta=ct.DescripcionRuta " +
                                    "from CatRutas ct  " +
                                    "where ct.Activa=1 and ct.IdOrigen=" + jsonEmbarque.getInt("idOrigen") +
                                    " and ct.IdDestino=" + jsonEmbarque.getInt("idDestino") +
                                    " and ct.IdCliente=" + jsonEmbarque.getInt("idCliente");
                            rs = statement.executeQuery(query);
                            jsonArrayAux = convertArray(rs);
                            if (jsonArrayAux == null || jsonArrayAux.toList().size() == 0) {
                                query = "select " +
                                        "idRuta=ct.IdRuta," +
                                        "ruta=ct.DescripcionRuta " +
                                        "from CatRutas ct  " +
                                        "where ct.Activa=1 and ct.IdOrigen=" + jsonEmbarque.getInt("idOrigen") +
                                        " and ct.IdDestino=" + jsonEmbarque.getInt("idDestino") +
                                        " and ct.IdCliente=(select IdCliente from CatClientes where NombreFiscal " +
                                        "LIKE '%PUBLICO EN GENERAL%')";
                                rs = statement.executeQuery(query);
                                jsonArrayAux = convertArray(rs);
                                if (jsonArrayAux == null) {
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "No se encontró ruta para el origen, destino, " +
                                            "cliente ingresado o público en general. Verifique las registradas en el ERP.");
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    continue;
                                } else {
                                    if (jsonArrayAux.toList().size() == 0) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontraron rutas para el origen, " +
                                                "destino y cliente ingresado o público en general. Verifique las " +
                                                "registradas en el ERP.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        continue;
                                    }
                                    if (jsonArrayAux.toList().size() == 1) {
                                        jsonEmbarque.put("idRuta",
                                                jsonArrayAux.getJSONObject(0).getInt("idRuta"));
                                        jsonEmbarque.put("ruta",
                                                jsonArrayAux.getJSONObject(0).getString("ruta"));
                                    }
                                    if (jsonArrayAux.toList().size() > 1) {
                                        jsonEmbarque.put("rutas", jsonArrayAux);
                                    }
                                }
                            } else {
                                if (jsonArrayAux.toList().size() == 0) {
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "No se encontraron rutas para el origen, " +
                                            "destino y cliente ingresado. Verifique las registradas en el ERP.");
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    continue;
                                }
                                if (jsonArrayAux.toList().size() == 1) {
                                    jsonEmbarque.put("idRuta", jsonArrayAux.getJSONObject(0).getInt("idRuta"));
                                    jsonEmbarque.put("ruta", jsonArrayAux.getJSONObject(0).getString("ruta"));
                                }
                                if (jsonArrayAux.toList().size() > 1) {
                                    jsonEmbarque.put("rutas", jsonArrayAux);
                                }
                            }
                        }

                        //Validando tipo de entrega*/
                        if (embarque.isEntregaEnSucursal() && embarque.isEntregaDiferenteDomicilio()) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "Verifique que sólo haya un tipo de entrega definido.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarque.put("entregaEnSucursal", embarque.isEntregaEnSucursal());
                            jsonEmbarque.put("entregaDiferenteDomicilio", embarque.isEntregaDiferenteDomicilio());
                        }

                        //Datos de sucursal entrega */
                        if (embarque.isEntregaEnSucursal() && !embarque.isEntregaDiferenteDomicilio()) {
                            query = "SELECT top 1 sucursalEntrega=Sucursal, idSucursalEntrega=IdSucursal, " +
                                    "codigoPostalSucursal=CodigoPostal \n" +
                                    "FROM CatSucursales where UPPER(Sucursal) = UPPER('"
                                    + embarque.getSucursalEntrega() + "')";
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró la sucursal ingresada. " +
                                        "Verifique que el registro exista en el ERP.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idSucursalEntrega", jsonAux.getInt("idSucursalEntrega"));
                                jsonEmbarque.put("sucursalEntrega", jsonAux.getString("sucursalEntrega"));
                            }

                            query = "SELECT DISTINCT idZonaEntrega=z.IdZona," +
                                    "zonaEntrega=CodigoZona\n" +
                                    "FROM CatZonasOperativasCPPQ zcp " +
                                    "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                                    "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                                    "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino " +
                                    "WHERE cp.CodigoPostal =  '" + jsonAux.getString("codigoPostalSucursal") + "'";
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró zona operativa para el código " +
                                        "postal de la sucursal ingresada. Verifique que el código postal tenga una " +
                                        "zona operativa asignada.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idZonaEntrega", jsonAux.getInt("idZonaEntrega"));
                                jsonEmbarque.put("zonaEntrega", jsonAux.getString("zonaEntrega"));
                            }
                        }

                        //Datos de entrega en diferente domicilio*/
                        if (!embarque.isEntregaEnSucursal() && embarque.isEntregaDiferenteDomicilio()) {
                            query = "select top 1\n" +
                                    "    idCodigoPostalDiferenteDomicilio=IdCodigoPostal,\n" +
                                    "    codigoPostalDiferenteDomicilio=CodigoPostal,\n" +
                                    "    coloniaDiferenteDomicilio=ISNULL(cp.Colonia,''),\n" +
                                    "    localidadDiferenteDomicilio=ISNULL(cp.Localidad,''),\n" +
                                    "    municipioDiferenteDomicilio=ISNULL(cp.Municipio,''),\n" +
                                    "    idMunicipioDiferenteDomicilio=ISNULL(cp.CodigoMunicipio,0),\n" +
                                    "    idEstadoDiferenteDomicilio=ISNULL(cp.IdEstado,''),\n" +
                                    "    estadoDiferenteDomicilio=estado.Estado,\n" +
                                    "    idPaisDiferenteDomicilio=estado.IdPais,\n" +
                                    "    paisDiferenteDomicilio=pais.Pais\n" +
                                    "from CatCodigosPostales cp\n" +
                                    "left join CatEstados estado on estado.IdEstado=cp.IdEstado\n" +
                                    "left join CatPaises pais on estado.IdPais=pais.IdPais\n" +
                                    "where CodigoPostal = '" + embarque.getCodigoPostalDiferenteDomicilio() + "'\n" +
                                    " and upper(Colonia) = upper('" + embarque.getColoniaDiferenteDomicilio() + "')";
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró el codigo postal de entrega " +
                                        "ingresado. Verifique que el registro exista en el ERP.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idCodigoPostalDiferenteDomicilio",
                                        jsonAux.getInt("idCodigoPostalDiferenteDomicilio"));
                                jsonEmbarque.put("codigoPostalDiferenteDomicilio",
                                        jsonAux.getString("codigoPostalDiferenteDomicilio"));
                                jsonEmbarque.put("coloniaDiferenteDomicilio",
                                        jsonAux.getString("coloniaDiferenteDomicilio"));
                                jsonEmbarque.put("localidadDiferenteDomicilio",
                                        jsonAux.getString("localidadDiferenteDomicilio"));
                                jsonEmbarque.put("idMunicipioDiferenteDomicilio",
                                        jsonAux.getInt("idMunicipioDiferenteDomicilio"));
                                jsonEmbarque.put("municipioDiferenteDomicilio",
                                        jsonAux.getString("municipioDiferenteDomicilio"));
                                jsonEmbarque.put("idEstadoDiferenteDomicilio",
                                        jsonAux.getString("idEstadoDiferenteDomicilio"));
                                jsonEmbarque.put("estadoDiferenteDomicilio",
                                        jsonAux.getString("estadoDiferenteDomicilio"));
                                jsonEmbarque.put("idPaisDiferenteDomicilio",
                                        jsonAux.getInt("idPaisDiferenteDomicilio"));
                                jsonEmbarque.put("calleNumeroDiferenteDomicilio",
                                        embarque.getCalleNumeroDiferenteDomicilio());
                                jsonEmbarque.put("entregarEn", embarque.getEntregarEn());
                                jsonEmbarque.put("datosAdicionalesEntrega", embarque.getDatosAdicionalesEntrega());
                                if (jsonEmbarque.getInt("idTipoPlantilla") == 2 && !embarque.isEsRecoleccion()) {
                                    jsonEmbarque.put("latitud", "");
                                    jsonEmbarque.put("longitud", "");
                                    jsonEmbarque.put("solicitarCoordenadas", true);
                                }
                            }

                            //Datos de zona diferente domicilio
                            query = "SELECT DISTINCT idZonaEntrega=z.IdZona," +
                                    "zonaEntrega=CodigoZona\n" +
                                    "FROM CatZonasOperativasCPPQ zcp " +
                                    "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                                    "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                                    "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino " +
                                    "WHERE cp.CodigoPostal =  '"
                                    + jsonAux.getString("codigoPostalDiferenteDomicilio") + "'";
                            rs = statement.executeQuery(query);
                            jsonAux = convertObject(rs);
                            if (jsonAux == null) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", "No se encontró zona operativa para el código " +
                                        "postal de entrega ingresado. Verifique que el código postal tenga una zona " +
                                        "operativa asignada.");
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            } else {
                                jsonEmbarque.put("idZonaEntrega", jsonAux.getInt("idZonaEntrega"));
                                jsonEmbarque.put("zonaEntrega", jsonAux.getString("zonaEntrega"));
                            }
                        }
                        jsonEmbarque.put("esRecoleccion", embarque.isEsRecoleccion());
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            if (embarque.isEsRecoleccion()) {
                                if (embarque.getLatitud().equals("") || embarque.getLongitud().equals("")) {
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "Agregue coordenandas para la recolección.");
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    continue;
                                } else {
                                    jsonEmbarque.put("latitud", embarque.getLatitud());
                                    jsonEmbarque.put("longitud", embarque.getLongitud());
                                }

                                //Datos de recoleccion en diferente domicilio*/
                                if (embarque.isRecoleccionDiferenteDomicilio()) {
                                    query = "select top 1\n" +
                                            "    idCodigoPostalDiferenteDomicilio=IdCodigoPostal,\n" +
                                            "    codigoPostalDiferenteDomicilio=CodigoPostal,\n" +
                                            "    coloniaDiferenteDomicilio=ISNULL(cp.Colonia,''),\n" +
                                            "    localidadDiferenteDomicilio=ISNULL(cp.Localidad,''),\n" +
                                            "    municipioDiferenteDomicilio=ISNULL(cp.Municipio,''),\n" +
                                            "    idMunicipioDiferenteDomicilio=ISNULL(cp.CodigoMunicipio,0),\n" +
                                            "    idEstadoDiferenteDomicilio=cp.IdEstado,\n" +
                                            "    estadoDiferenteDomicilio=estado.Estado,\n" +
                                            "    idPaisDiferenteDomicilio=estado.IdPais,\n" +
                                            "    paisDiferenteDomicilio=estado.IdPais\n" +
                                            "from CatCodigosPostales cp\n" +
                                            "left join CatEstados estado on estado.IdEstado=cp.IdEstado\n" +
                                            "left join CatPaises pais on estado.IdPais=pais.IdPais\n" +
                                            "where CodigoPostal = '" + embarque.getCodigoPostalDiferenteDomicilioRecoleccion() + "'\n" +
                                            " and upper(Colonia) = upper('" + embarque.getColoniaDiferenteDomicilioRecoleccion() + "')";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró el codigo postal de " +
                                                "recolección ingresado. Verifique que el registro exista en el ERP.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        continue;
                                    } else {
                                        jsonEmbarque.put("idCodigoPostalDiferenteDomicilioRecoleccion",
                                                jsonAux.getInt("idCodigoPostalDiferenteDomicilio"));
                                        jsonEmbarque.put("codigoPostalDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("codigoPostalDiferenteDomicilio"));
                                        jsonEmbarque.put("coloniaDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("coloniaDiferenteDomicilio"));
                                        jsonEmbarque.put("localidadDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("localidadDiferenteDomicilio"));
                                        jsonEmbarque.put("idMunicipioDiferenteDomicilioRecoleccion",
                                                jsonAux.getInt("idMunicipioDiferenteDomicilio"));
                                        jsonEmbarque.put("municipioDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("municipioDiferenteDomicilio"));
                                        jsonEmbarque.put("idEstadoDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("idEstadoDiferenteDomicilio"));
                                        jsonEmbarque.put("estadoDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("estadoDiferenteDomicilio"));
                                        jsonEmbarque.put("idPaisDiferenteDomicilioRecoleccion",
                                                jsonAux.getInt("idPaisDiferenteDomicilio"));
                                        jsonEmbarque.put("paisDiferenteDomicilioRecoleccion",
                                                jsonAux.getString("paisDiferenteDomicilio"));
                                        jsonEmbarque.put("calleNumeroDiferenteDomicilioRecoleccion",
                                                embarque.getCalleNumeroDiferenteDomicilioRecoleccion());
                                        jsonEmbarque.put("recogerEn", embarque.getRecogerEn());
                                        jsonEmbarque.put("datosAdicionalesRecoleccion",
                                                embarque.getDatosAdicionalesRecoleccion());
                                        if (jsonEmbarque.getInt("idTipoPlantilla") == 2) {
                                            jsonEmbarque.put("latitud", "");
                                            jsonEmbarque.put("longitud", "");
                                            jsonEmbarque.put("solicitarCoordenadas", true);
                                        }
                                    }

                                    //Datos de zona diferente domicilio
                                    query = "SELECT DISTINCT idZonaRecoleccion=z.IdZona," +
                                            "zonaRecoleccion=CodigoZona\n" +
                                            "FROM CatZonasOperativasCPPQ zcp " +
                                            "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                                            "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                                            "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino"
                                            + " WHERE cp.CodigoPostal =  '"
                                            + jsonAux.getString("codigoPostalDiferenteDomicilioRecoleccion") + "'";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró zona operativa para el " +
                                                "código postal de recolección ingresado. Verifique que el " +
                                                "código postal tenga una zona operativa asignada.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        continue;
                                    } else {
                                        jsonEmbarque.put("idZonaRecoleccion", jsonAux.getInt("idZonaRecoleccion"));
                                        jsonEmbarque.put("zonaRecoleccion", jsonAux.getString("zonaRecoleccion"));
                                    }
                                }
                            } else {
                                if (!embarque.isEntregaEnSucursal()) {
                                    if (embarque.getLatitud().equals("") || embarque.getLongitud().equals("")) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "Agregue coordenandas para la entrega.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        continue;
                                    } else {
                                        jsonEmbarque.put("latitud", embarque.getLatitud());
                                        jsonEmbarque.put("longitud", embarque.getLongitud());
                                    }
                                }
                            }
                        }

                        //Datos de cita */
                        jsonEmbarque.put("conCita", embarque.isConCita());
                        if (embarque.isConCita()) {
                            jsonEmbarque.put("citaPendiente", embarque.isCitaPendiente());
                            if (!embarque.isCitaPendiente()) {
                                jsonEmbarque.put("fechaCita", embarque.getFechaCita());
                                jsonEmbarque.put("horaCitaMinima", embarque.getHoraCitaMinima());
                                jsonEmbarque.put("horaCitaMaxima", embarque.getHoraCitaMaxima());
                            }
                        }

                        //Datos de paquetes
                        JSONArray jsonListadoPaquetes = new JSONArray();
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            for (PaqueteImportadoRequest paquete : embarque.getPaquetes()) {
                                try {
                                    JSONObject jsonPaquete = new JSONObject();
                                    //Datos de producto
                                    query = "Select top 1\n" +
                                            "    idProducto=IdProducto,\n" +
                                            "    noProducto=NoProducto\n" +
                                            "from CatProductosPQ\n" +
                                            "where IdProducto = " + paquete.getNumeroProducto();
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un producto ingresado. " +
                                                "Verifique que esté registrado en paquetería.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    } else {
                                        paquete.setIdProducto(jsonAux.getInt("idProducto"));
                                        if (paquete.getIdProducto() == 1) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se puede usar el producto " +
                                                    "genérico para la importación.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonPaquete.put("idProducto", jsonAux.getInt("idProducto"));
                                        jsonPaquete.put("noProducto", jsonAux.getInt("noProducto"));
                                    }

                                    //Datos de embalaje
                                    query = "Select top 1\n" +
                                            "    idEmbalaje=IdEmbalaje,\n" +
                                            "    embalaje=Nombre\n" +
                                            "from CatEmbalajesPQ\n" +
                                            "where UPPER(Nombre) = UPPER('" + paquete.getEmbalaje() + "')";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un embalaje de producto " +
                                                "ingresado. Verifique que esté registrado en paquetería.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }
                                    paquete.setIdEmbalaje(jsonAux.getInt("idEmbalaje"));
                                    jsonPaquete.put("idEmbalaje", jsonAux.getInt("idEmbalaje"));
                                    jsonPaquete.put("embalaje", jsonAux.getString("embalaje"));
                                    jsonPaquete.put("cantidad", paquete.getCantidad());
                                    jsonPaquete.put("largo", paquete.getLargo());
                                    jsonPaquete.put("alto", paquete.getAlto());
                                    jsonPaquete.put("ancho", paquete.getAncho());
                                    jsonPaquete.put("peso", paquete.getPeso());
                                    jsonPaquete.put("volumen", paquete.getVolumen());
                                    jsonPaquete.put("descripcion", paquete.getDescripcion());
                                    jsonPaquete.put("observaciones", paquete.getObservaciones());

                                    jsonListadoPaquetes.put(jsonPaquete);
                                } catch (Exception errorPaquetes) {
                                    errorPaquetes.printStackTrace();
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "Hubo un error al validar un paquete " +
                                            "del embarque." + embarque.getNumeroEmbarque());
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    break;
                                }
                            }
                        } else {
                            for (PaqueteImportadoRequest paquete : embarque.getPaquetes()) {
                                try {
                                    JSONObject jsonPaquete = new JSONObject();
                                    //Datos de producto
                                    query = "Select top 1\n" +
                                            "    idProducto=IdProducto,\n" +
                                            "    noProducto=NoProducto,\n" +
                                            "    descripcion=Descripcion,\n" +
                                            "    idEmbalaje=IdEmbalaje,\n" +
                                            "    embalaje=Embalaje,\n" +
                                            "    largo=Largo,\n" +
                                            "    alto=Alto,\n" +
                                            "    ancho=Ancho,\n" +
                                            "    peso=Peso,\n" +
                                            "    volumen=Largo*Alto*Ancho\n" +
                                            "from CatProductosPQ\n" +
                                            "where IdProducto = " + paquete.getNumeroProducto();
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un producto ingresado. " +
                                                "Verifique que esté registrado en paquetería.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    } else {
                                        paquete.setIdProducto(jsonAux.getInt("idProducto"));
                                        if (paquete.getIdProducto() == 1) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se puede usar el producto " +
                                                    "genérico para la importación.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonPaquete.put("idProducto", jsonAux.getInt("idProducto"));
                                        jsonPaquete.put("noProducto", jsonAux.getInt("noProducto"));
                                        paquete.setIdEmbalaje(jsonAux.getInt("idEmbalaje"));
                                        paquete.setLargo((float) jsonAux.getDouble("largo"));
                                        paquete.setAlto((float) jsonAux.getDouble("alto"));
                                        paquete.setAncho((float) jsonAux.getDouble("ancho"));
                                        paquete.setPeso((float) jsonAux.getDouble("peso"));
                                        paquete.setVolumen((float) jsonAux.getDouble("volumen"));
                                        jsonPaquete.put("idEmbalaje", jsonAux.getInt("idEmbalaje"));
                                        jsonPaquete.put("embalaje", jsonAux.getString("embalaje"));
                                        jsonPaquete.put("cantidad", paquete.getCantidad());
                                        jsonPaquete.put("largo", jsonAux.getDouble("largo"));
                                        jsonPaquete.put("alto", jsonAux.getDouble("alto"));
                                        jsonPaquete.put("ancho", jsonAux.getDouble("ancho"));
                                        jsonPaquete.put("peso", jsonAux.getDouble("peso"));
                                        jsonPaquete.put("volumen", jsonAux.getDouble("volumen"));
                                        jsonPaquete.put("descripcion", jsonAux.getString("descripcion"));
                                        jsonPaquete.put("observaciones", paquete.getObservaciones());

                                        jsonListadoPaquetes.put(jsonPaquete);
                                    }
                                } catch (Exception errorPaquetes) {
                                    errorPaquetes.printStackTrace();
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "Hubo un error al validar un paquete del " +
                                            "embarque." + embarque.getNumeroEmbarque());
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    break;
                                }
                            }
                        }
                        if (!jsonEmbarqueEstatus.getBoolean("success")) {
                            continue;
                        }
                        jsonEmbarque.put("paquetes", jsonListadoPaquetes);

                        //Datos de complementos SAT
                        JSONArray jsonListadoComplementos = new JSONArray();
                        if (jsonEmbarque.getInt("idTipoPlantilla") == 1) {
                            for (ComplementoSATImportadoRequest complemento : embarque.getComplementosSAT()) {
                                try {
                                    JSONObject jsonComplementos = new JSONObject();
                                    jsonComplementos.put("cantidad", complemento.getCantidad());
                                    jsonComplementos.put("peso", complemento.getPeso());
                                    //Datos de producto
                                    query = "select claveProductoServicio=ClaveSAT,\n" +
                                            "       descripcionProductoServicio=Descripcion,\n" +
                                            "       catalogoSatProducto=CatalogoSAT,\n" +
                                            "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                            "THEN CAST(0 as bit) ELSE CAST(1 as bit) END)\n" +
                                            "from CatGeneralesSAT\n" +
                                            "where CatalogoSAT = 'c_ClaveProdServCP'\n" +
                                            "  and ClaveSAT = '" + complemento.getClaveProductoServicio() + "'";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT con " +
                                                "la clave de producto ingresada. Verifique que esté registrado en el ERP.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }
                                    jsonComplementos.put("claveProductoServicio",
                                            jsonAux.getString("claveProductoServicio"));
                                    jsonComplementos.put("descripcionProductoServicio",
                                            jsonAux.getString("descripcionProductoServicio"));
                                    jsonComplementos.put("esMaterialPeligroso",
                                            jsonAux.getBoolean("esMaterialPeligroso"));

                                    //Datos de medida
                                    query = "select claveUnidadMedida=ClaveSAT,\n" +
                                            "       descripcionUnidadMedida=Descripcion,\n" +
                                            "       catalogoSatUnidadMedida=CatalogoSAT,\n" +
                                            "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                            "THEN 0 ELSE 1 END)\n" +
                                            "from CatGeneralesSAT\n" +
                                            "where CatalogoSAT = 'c_ClaveUnidad'\n" +
                                            "  and ClaveSAT = '" + complemento.getClaveUnidadMedida() + "'";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT con " +
                                                "la clave de unidad ingresada. Verifique que esté registrado en el ERP.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }
                                    jsonComplementos.put("claveUnidadMedida",
                                            jsonAux.getString("claveUnidadMedida"));
                                    jsonComplementos.put("descripcionUnidadMedida",
                                            jsonAux.getString("descripcionUnidadMedida"));

                                    if (jsonComplementos.getBoolean("esMaterialPeligroso") != complemento.isEsMaterialPeligroso()) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "El valor Material peligroso ingresado " +
                                                "no coincide con el especificado por el SAT para la clave del " +
                                                "producto: " + complemento.getClaveProductoServicio());
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }

                                    if (jsonComplementos.getBoolean("esMaterialPeligroso")) {
                                        //Datos de material peligroso
                                        query = "select claveMaterialPeligroso=ClaveSAT,\n" +
                                                "       descripcionMaterialPeligroso=Descripcion,\n" +
                                                "       catalogoSatMaterialPeligroso=CatalogoSAT,\n" +
                                                "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                                "THEN 0 ELSE 1 END)\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_MaterialPeligroso'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveMaterialPeligroso() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT " +
                                                    "con la clave de material peligroso ingresada. Verifique que " +
                                                    "esté registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveMaterialPeligroso",
                                                jsonAux.getString("claveMaterialPeligroso"));
                                        jsonComplementos.put("descripcionMaterialPeligroso",
                                                jsonAux.getString("descripcionMaterialPeligroso"));

                                        //Datos de embalaje
                                        query = "select claveEmbalaje=ClaveSAT,\n" +
                                                "       descripcionSatEmbalaje=Descripcion,\n" +
                                                "       catalogoSatEmbalaje=CatalogoSAT,\n" +
                                                "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                                "THEN 0 ELSE 1 END)\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_TipoEmbalaje'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveEmbalaje() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT " +
                                                    "con la clave de tipo embalaje ingresada. " +
                                                    "Verifique que esté registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveEmbalaje",
                                                jsonAux.getString("claveEmbalaje"));
                                        jsonComplementos.put("descripcionSatEmbalaje",
                                                jsonAux.getString("descripcionSatEmbalaje"));
                                        jsonComplementos.put("descripcionEmbalaje",
                                                complemento.getDescripcionEmbalaje());

                                        //Datos de fracción arancelaria
                                        query = "select claveFraccionArancelaria=ClaveSAT,\n" +
                                                "       descripcionFraccionArancelaria=Descripcion,\n" +
                                                "       catalogoSatFraccionArancelaria=CatalogoSAT,\n" +
                                                "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                                "THEN 0 ELSE 1 END)\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_FraccionArancelaria'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveFraccionArancelaria() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT" +
                                                    " con la clave de fracción arancelaria ingresada. Verifique que " +
                                                    "esté registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveFraccionArancelaria", jsonAux.getString("claveFraccionArancelaria"));
                                        jsonComplementos.put("descripcionFraccionArancelaria", jsonAux.getString("descripcionFraccionArancelaria"));
                                    }
                                    //DATOS DE FARMACO
                                    if (complemento.isEsFarmaco()) {

                                        //Datos de SectorCOFEPRIS
                                        query = "select claveSectorCOFEPRIS=ClaveSAT,\n" +
                                                "       descripcionSectorCOFEPRIS=Descripcion\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_SectorCOFEPRIS'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveSectorCofepris() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT" +
                                                    " con la clave de sector Cofepris ingresada. Verifique que esté " +
                                                    "registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveSectorCofepris",
                                                jsonAux.getString("claveSectorCOFEPRIS"));
                                        jsonComplementos.put("descripcionSectorCofepris",
                                                jsonAux.getString("descripcionSectorCOFEPRIS"));
                                        int sectorNumero = Integer.parseInt(complemento.getClaveSectorCofepris());
                                        if (sectorNumero >= 1 && sectorNumero <= 3) {
                                            //Datos de formaFarmaceutica
                                            query = "select claveFormaFarmaceutica=ClaveSAT,\n" +
                                                    "       descripcionFormaFarmaceutica=Descripcion\n" +
                                                    "from CatGeneralesSAT\n" +
                                                    "where CatalogoSAT = 'c_FormaFarmaceutica'\n" +
                                                    "  and ClaveSAT = '" + complemento.getFormaFarmaceutica() + "'";
                                            rs = statement.executeQuery(query);
                                            jsonAux = convertObject(rs);
                                            if (jsonAux == null) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "No se encontró un complemento " +
                                                        "de SAT con la clave de forma farmacéutica ingresada. " +
                                                        "Verifique que esté registrado en el ERP.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            jsonComplementos.put("formaFarmaceutica",
                                                    jsonAux.getString("claveFormaFarmaceutica"));
                                            jsonComplementos.put("descripcionFormaFarmaceutica",
                                                    jsonAux.getString("descripcionFormaFarmaceutica"));

                                            //Datos de CondicionesEspecialesTransp
                                            query = "select claveCondiconEspecial=ClaveSAT,\n" +
                                                    "       descripcionCondicionEspecial=Descripcion\n" +
                                                    "from CatGeneralesSAT\n" +
                                                    "where CatalogoSAT = 'c_CondicionesEspeciales'\n" +
                                                    "  and ClaveSAT = '" + complemento.getCondicionesEspTransp() + "'";
                                            rs = statement.executeQuery(query);
                                            jsonAux = convertObject(rs);
                                            if (jsonAux == null) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "No se encontró un complemento " +
                                                        "de SAT con la clave de condición especial de transporte " +
                                                        "ingresada. Verifique que esté registrado en el ERP.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            jsonComplementos.put("condicionesEspTransp",
                                                    jsonAux.getString("claveCondiconEspecial"));
                                            jsonComplementos.put("descripcionCondicionEspecial",
                                                    jsonAux.getString("descripcionCondicionEspecial"));

                                            if (complemento.getFabricante().length() == 0
                                                    || complemento.getDenominacionDistintivaProd().length() > 240) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de fabricante debe " +
                                                        "tener de 1 a 240 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            jsonComplementos.put("fabricante", complemento.getFabricante());
                                            if (complemento.getFechaCaducidad().contains("T")) {
                                                complemento.setFechaCaducidad(complemento.getFechaCaducidad()
                                                        .split("T")[0]);
                                            }
                                            if (!complemento.getFechaCaducidad().contains("/")
                                                    && !complemento.getFechaCaducidad().contains("-")) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "La fecha de caducidad debe estar" +
                                                        " en formato dd/MM/AAAA o AAAA-MM-dd.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            if (complemento.getLoteMedicamento().length() == 0
                                                    || complemento.getLoteMedicamento().length() > 10) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de lote de medicamento" +
                                                        " debe tener de 1 a 10 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            jsonComplementos.put("loteMedicamento", complemento.getLoteMedicamento());

                                            if (complemento.getFechaCaducidad().contains("/")) {
                                                String[] arr = complemento.getFechaCaducidad().split("/");
                                                if (arr[0].length() != 2 || arr[1].length() != 2 || arr[2].length() != 4) {
                                                    jsonEmbarqueEstatus.put("success", false);
                                                    jsonEmbarqueEstatus.put("message", "La fecha de caducidad debe" +
                                                            " estar en formato dd/MM/AAAA o AAAA-MM-dd.");
                                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                    break;
                                                }
                                            }
                                            if (complemento.getFechaCaducidad().contains("-")) {
                                                String[] arr = complemento.getFechaCaducidad().split("-");
                                                if (arr[0].length() != 4 || arr[1].length() != 2 || arr[2].length() != 2) {
                                                    jsonEmbarqueEstatus.put("success", false);
                                                    jsonEmbarqueEstatus.put("message", "La fecha de caducidad debe" +
                                                            " estar en formato dd/MM/AAAA o AAAA-MM-dd.");
                                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                    break;
                                                }
                                            }
                                            jsonComplementos.put("fechaCaducidad", complemento.getFechaCaducidad());

                                            if (sectorNumero == 1 || sectorNumero == 3) {
                                                if (complemento.getDenominacionGenericaProd().length() == 0
                                                        || complemento.getDenominacionGenericaProd().length() > 50) {
                                                    jsonEmbarqueEstatus.put("success", false);
                                                    jsonEmbarqueEstatus.put("message", "La denominación genérica" +
                                                            " debe tener de 1 a 50 caracteres.");
                                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                    break;
                                                }
                                                if (complemento.getDenominacionDistintivaProd().length() == 0
                                                        || complemento.getDenominacionDistintivaProd().length() > 50) {
                                                    jsonEmbarqueEstatus.put("success", false);
                                                    jsonEmbarqueEstatus.put("message", "La denominación distintiva" +
                                                            " debe tener de 1 a 50 caracteres.");
                                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                    break;
                                                }
                                                jsonComplementos.put("denominacionDistintivaProd", complemento.getDenominacionDistintivaProd());
                                                jsonComplementos.put("denominacionGenericaProd", complemento.getDenominacionGenericaProd());

                                                if (complemento.getRegistroSanitarioFolioAutorizacion().length() == 0
                                                        || complemento.getRegistroSanitarioFolioAutorizacion().length() > 15) {
                                                    jsonEmbarqueEstatus.put("success", false);
                                                    jsonEmbarqueEstatus.put("message", "El campo de registro" +
                                                            " sanitario/folio de autorización debe tener de 1 a 15 " +
                                                            "caracteres.");
                                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                    break;
                                                }
                                            }
                                        }
                                        if (sectorNumero == 4) {
                                            if (complemento.getNumeroCAS().length() == 0 || complemento.getNumeroCAS().length() > 15) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de número CAS debe " +
                                                        "tener de 1 a 15 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                        }
                                        if (sectorNumero == 5) {
                                            if (complemento.getNumRegSanPlagCOFEPRIS().length() == 0 || complemento.getNumRegSanPlagCOFEPRIS().length() > 60) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de NumRegSanPlagCOFEPRIS" +
                                                        " debe tener de 1 a 60 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            if (complemento.getDatosFabricante().length() == 0 || complemento.getDatosFabricante().length() > 600) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de datos de fabricante" +
                                                        " debe tener de 1 a 600 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            if (complemento.getDatosFormulador().length() == 0 || complemento.getDatosFormulador().length() > 600) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de datos de formulador" +
                                                        " debe tener de 1 a 600 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            if (complemento.getDatosMaquilador().length() == 0 || complemento.getDatosMaquilador().length() > 600) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de datos de maquilador" +
                                                        " debe tener de 1 a 600 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                            if (complemento.getUsoAutorizado().length() == 0 || complemento.getUsoAutorizado().length() > 1000) {
                                                jsonEmbarqueEstatus.put("success", false);
                                                jsonEmbarqueEstatus.put("message", "El campo de uso autorizado debe" +
                                                        " tener de 1 a 1000 caracteres.");
                                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                                break;
                                            }
                                        }
                                        //Revisar condiciones de longitud
                                        jsonComplementos.put("datosFabricante", complemento.getDatosFabricante());
                                        jsonComplementos.put("datosFormulador", complemento.getDatosFormulador());
                                        jsonComplementos.put("datosMaquilador", complemento.getDatosMaquilador());
                                        jsonComplementos.put("usoAutorizado", complemento.getUsoAutorizado());

                                        jsonComplementos.put("nombreIngredienteActivo", complemento.getNombreIngredienteActivo());
                                        jsonComplementos.put("nombreQuimico", complemento.getNombreQuimico());
                                        jsonComplementos.put("numRegSanPlagCOFEPRIS", complemento.getNumRegSanPlagCOFEPRIS());
                                        jsonComplementos.put("numeroCAS", complemento.getNumeroCAS());
                                        jsonComplementos.put("registroSanitarioFolioAutorizacion", complemento.getRegistroSanitarioFolioAutorizacion());
                                    }
                                    jsonListadoComplementos.put(jsonComplementos);
                                } catch (Exception errorPaquetes) {
                                    errorPaquetes.printStackTrace();
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "Hubo un error al validar un complemento sat del embarque: " + embarque.getNumeroEmbarque());
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    break;
                                }
                            }
                        } else {
                            for (ComplementoSATImportadoRequest complemento : embarque.getComplementosSAT()) {
                                try {
                                    JSONObject jsonComplementos = new JSONObject();
                                    jsonComplementos.put("cantidad", complemento.getCantidad());
                                    query = "Select top 1\n" +
                                            "    peso=Peso\n" +
                                            "from CatProductosPQ\n" +
                                            "where IdProducto = " + complemento.getNumeroProducto();
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    jsonComplementos.put("peso", jsonAux.getDouble("peso"));
                                    //Datos de producto
                                    query = "select claveProductoServicio=ClaveSAT,\n" +
                                            "       descripcionProductoServicio=Descripcion,\n" +
                                            "       catalogoSatProducto=CatalogoSAT,\n" +
                                            "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                            "THEN CAST(0 as bit) ELSE CAST(1 as bit) END)\n" +
                                            "from CatGeneralesSAT\n" +
                                            "where CatalogoSAT = 'c_ClaveProdServCP'\n" +
                                            "  and ClaveSAT = '" + complemento.getClaveProductoServicio() + "'";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT con" +
                                                " la clave de producto ingresada. Verifique que esté registrado en el ERP.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }
                                    jsonComplementos.put("claveProductoServicio",
                                            jsonAux.getString("claveProductoServicio"));
                                    jsonComplementos.put("descripcionProductoServicio",
                                            jsonAux.getString("descripcionProductoServicio"));
                                    jsonComplementos.put("esMaterialPeligroso",
                                            jsonAux.getBoolean("esMaterialPeligroso"));

                                    //Datos de medida
                                    query = "select claveUnidadMedida=ClaveSAT,\n" +
                                            "       descripcionUnidadMedida=Descripcion,\n" +
                                            "       catalogoSatUnidadMedida=CatalogoSAT,\n" +
                                            "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                            "THEN 0 ELSE 1 END)\n" +
                                            "from CatGeneralesSAT\n" +
                                            "where CatalogoSAT = 'c_ClaveUnidad'\n" +
                                            "  and ClaveSAT = '" + complemento.getClaveUnidadMedida() + "'";
                                    rs = statement.executeQuery(query);
                                    jsonAux = convertObject(rs);
                                    if (jsonAux == null) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT" +
                                                " con la clave de unidad ingresada. Verifique que esté registrado en el ERP.");
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }
                                    jsonComplementos.put("claveUnidadMedida",
                                            jsonAux.getString("claveUnidadMedida"));
                                    jsonComplementos.put("descripcionUnidadMedida",
                                            jsonAux.getString("descripcionUnidadMedida"));

                                    if (jsonComplementos.getBoolean("esMaterialPeligroso") != complemento.isEsMaterialPeligroso()) {
                                        jsonEmbarqueEstatus.put("success", false);
                                        jsonEmbarqueEstatus.put("message", "El valor Material peligroso ingresado no" +
                                                " coincide con el especificado por el SAT para la clave del producto: "
                                                + complemento.getClaveProductoServicio());
                                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                        break;
                                    }

                                    if (jsonComplementos.getBoolean("esMaterialPeligroso")) {
                                        //Datos de material peligroso
                                        query = "select claveMaterialPeligroso=ClaveSAT,\n" +
                                                "       descripcionMaterialPeligroso=Descripcion,\n" +
                                                "       catalogoSatMaterialPeligroso=CatalogoSAT,\n" +
                                                "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                                "THEN 0 ELSE 1 END)\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_MaterialPeligroso'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveMaterialPeligroso() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT" +
                                                    " con la clave de material peligroso ingresada. Verifique que" +
                                                    " esté registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveMaterialPeligroso",
                                                jsonAux.getString("claveMaterialPeligroso"));
                                        jsonComplementos.put("descripcionMaterialPeligroso",
                                                jsonAux.getString("descripcionMaterialPeligroso"));

                                        //Datos de embalaje
                                        query = "select claveEmbalaje=ClaveSAT,\n" +
                                                "       descripcionSatEmbalaje=Descripcion,\n" +
                                                "       catalogoSatEmbalaje=CatalogoSAT,\n" +
                                                "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                                "THEN 0 ELSE 1 END)\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_TipoEmbalaje'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveEmbalaje() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT" +
                                                    " con la clave de tipo embalaje ingresada. Verifique que esté" +
                                                    " registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveEmbalaje",
                                                jsonAux.getString("claveEmbalaje"));
                                        jsonComplementos.put("descripcionSatEmbalaje",
                                                jsonAux.getString("descripcionSatEmbalaje"));
                                        jsonComplementos.put("descripcionEmbalaje",
                                                complemento.getDescripcionEmbalaje());

                                        //Datos de fracción arancelaria
                                        query = "select claveFraccionArancelaria=ClaveSAT,\n" +
                                                "       descripcionFraccionArancelaria=Descripcion,\n" +
                                                "       catalogoSatFraccionArancelaria=CatalogoSAT,\n" +
                                                "       esMaterialPeligroso=(CASE WHEN MaterialPeligroso = '0' " +
                                                "THEN 0 ELSE 1 END)\n" +
                                                "from CatGeneralesSAT\n" +
                                                "where CatalogoSAT = 'c_FraccionArancelaria'\n" +
                                                "  and ClaveSAT = '" + complemento.getClaveFraccionArancelaria() + "'";
                                        rs = statement.executeQuery(query);
                                        jsonAux = convertObject(rs);
                                        if (jsonAux == null) {
                                            jsonEmbarqueEstatus.put("success", false);
                                            jsonEmbarqueEstatus.put("message", "No se encontró un complemento de SAT" +
                                                    " con la clave de fracción arancelaria ingresada. Verifique que" +
                                                    " esté registrado en el ERP.");
                                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                            break;
                                        }
                                        jsonComplementos.put("claveFraccionArancelaria",
                                                jsonAux.getString("claveFraccionArancelaria"));
                                        jsonComplementos.put("descripcionFraccionArancelaria",
                                                jsonAux.getString("descripcionFraccionArancelaria"));
                                    }

                                    jsonListadoComplementos.put(jsonComplementos);
                                } catch (Exception errorPaquetes) {
                                    errorPaquetes.printStackTrace();
                                    jsonEmbarqueEstatus.put("success", false);
                                    jsonEmbarqueEstatus.put("message", "Hubo un error al validar un complemento" +
                                            " sat del embarque: " + embarque.getNumeroEmbarque());
                                    jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                    break;
                                }
                            }
                        }
                        if (!jsonEmbarqueEstatus.getBoolean("success")) {
                            continue;
                        }
                        jsonEmbarque.put("complementosSAT", jsonListadoComplementos);

                        //Datos de tarifa
                        JSONArray jsonConceptos = new JSONArray();
                        query = "select TipoTarifaTarifas from CatParametrosConfiguracion2PQ";
                        rs = statement.executeQuery(query);
                        jsonAux = convertObject(rs);
                        if (jsonAux == null) {
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se ha definido un tipo de tarifa." +
                                    " Definala en parametros de configuración de paquetería.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            CotizacionRequest cotizacion = new CotizacionRequest();
                            cotizacion.setIdCotizacion(0);
                            cotizacion.setIdRecoleccion(0);
                            cotizacion.setIdEmbarque(0);
                            cotizacion.setIdOrigen(jsonEmbarque.getInt("idOrigen"));
                            cotizacion.setIdDestino(jsonEmbarque.getInt("idDestino"));
                            cotizacion.setIdZonaEntrega(jsonEmbarque.getInt("idZonaEntrega"));
                            cotizacion.setIdCliente(jsonEmbarque.getInt("idCliente"));
                            cotizacion.setIdTipoTarifa(jsonAux.getInt("TipoTarifaTarifas"));
                            cotizacion.setEntregaEnSucursal(jsonEmbarque.getBoolean("entregaEnSucursal"));
                            cotizacion.setValorDeclarado((float) jsonEmbarque.getDouble("valorDeclarado"));
                            cotizacion.setPorcentajeSeguro((float) jsonEmbarque.getDouble("porcentajeSeguro"));
                            cotizacion.setIdSeguro(jsonEmbarque.getInt("idTipoSeguro"));
                            cotizacion.setAplicaSeguro(jsonEmbarque.getBoolean("aplicaSeguro"));
                            if (jsonEmbarque.getBoolean("esRecoleccion")) {
                                cotizacion.setIdZonaRecoleccion(jsonEmbarque.getInt("idZonaRecoleccion"));
                                cotizacion.setAplicaRecoleccion(true);
                                cotizacion.setRecoleccionConCita(jsonEmbarque.getBoolean("conCita"));
                                cotizacion.setEmbarqueConCita(false);
                            } else {
                                cotizacion.setIdZonaRecoleccion(0);
                                cotizacion.setAplicaRecoleccion(false);
                                cotizacion.setRecoleccionConCita(false);
                                cotizacion.setEmbarqueConCita(jsonEmbarque.getBoolean("conCita"));
                            }

                            List<PaqueteCotizadorRequest> paquetesCotizador = new ArrayList<>();
                            for (PaqueteImportadoRequest paquete : embarque.getPaquetes()) {
                                PaqueteCotizadorRequest pqCotizador = new PaqueteCotizadorRequest();
                                pqCotizador.setTipo(2);
                                pqCotizador.setPeso(paquete.getPeso());
                                pqCotizador.setLargo(paquete.getLargo());
                                pqCotizador.setAncho(paquete.getAncho());
                                pqCotizador.setAlto(paquete.getAlto());
                                pqCotizador.setVolumen(paquete.getVolumen());
                                pqCotizador.setIdTipoEmpaque(paquete.getIdEmbalaje());
                                pqCotizador.setValorDeclarado(0);
                                pqCotizador.setActivo(true);
                                pqCotizador.setCtd(paquete.getCantidad());
                                pqCotizador.setIdProducto(paquete.getIdProducto());
                                paquetesCotizador.add(pqCotizador);
                            }
                            cotizacion.setPaquetesCotizacion(paquetesCotizador);
                            ResponseEntity<?> response = cotizadorRest.postCotizacion(cotizacion, rfc);
                            jsonConceptos = new JSONArray(response.getBody().toString());
                            jsonEmbarque.put("conceptosFacturacion", jsonConceptos);
                            boolean conceptosValidos = true;
                            String errorMessage = "";
                            for (int i = 0; i < jsonConceptos.length(); i++) {
                                JSONObject obj = jsonConceptos.getJSONObject(i);
                                if (obj.getBoolean("m_bError")) {
                                    conceptosValidos = false;
                                    errorMessage = obj.getString("m_sDetalles");
                                    break;
                                }
                            }
                            if (!conceptosValidos) {
                                jsonEmbarqueEstatus.put("success", false);
                                jsonEmbarqueEstatus.put("message", errorMessage);
                                jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                                continue;
                            }
                        }

                        if (jsonListadoPaquetes.length() == 0) {
                            jsonEmbarqueEstatus.put("data", new JSONObject());
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No hay paquetes relacionados al embarque. " +
                                    "Los paquetes son necesarios para crear el embarque. Verifique la hoja " +
                                    "de paquetes contenga la columna para relacionar con el embarque.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else if (jsonListadoComplementos.length() == 0) {
                            jsonEmbarqueEstatus.put("data", new JSONObject());
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No hay complementos del SAT relacionados al embarque." +
                                    " Los complementos del SAT son necesarios para crear el embarque. Verifique la " +
                                    "hoja de complementos de SAT contenga la columna para relacionar con el embarque.");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else if (jsonConceptos.length() == 0) {
                            jsonEmbarqueEstatus.put("data", new JSONObject());
                            jsonEmbarqueEstatus.put("success", false);
                            jsonEmbarqueEstatus.put("message", "No se pudieron calcular conceptos de facturacion para" +
                                    " el embarque. Verifique que las caracteristicas del embarque cuadren con una" +
                                    " tarifa registrada..");
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                            continue;
                        } else {
                            jsonEmbarqueEstatus.put("data", jsonEmbarque);
                            jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                        }

                    } catch (Exception err) {
                        err.printStackTrace();
                        jsonEmbarqueEstatus.put("success", false);
                        jsonEmbarqueEstatus.put("message", "Hubo un error al validar el embarque "
                                + embarque.getNumeroEmbarque());
                        jsonListadoEmbarquesFinal.put(jsonEmbarqueEstatus);
                    }
                }

                return ResponseEntity.ok(jsonListadoEmbarquesFinal.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al validar la informacion");
        }
    }

    @PostMapping("/Embarques/agregar-importados")
    public ResponseEntity<?> agregarEmbarquesImportados
            (@RequestHeader("RFC") String rfc, @RequestBody ImportacionRequest request) throws
            SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                boolean sonRecolecciones = false;
                ResultSet rs;
                String actualQuery = "";
                String querys = "";
                String inicioQuery = "BEGIN TRANSACTION;\n" +
                        "    BEGIN TRY\n" +
                        "        declare @FOLIO int, @IdEmbarque int,@IdCotizacion int,@FOLIOSTR varchar(20), " +
                        "@IdRecoleccion int;\n";
                String finQuery = "\nCOMMIT TRANSACTION;\n" +
                        "    END TRY\n" +
                        "    BEGIN CATCH\n" +
                        "        ROLLBACK TRANSACTION;\n" +
                        "    END CATCH;";
                for (EmbarqueImportadoRequest embarque : request.getEmbarques()) {
                    sonRecolecciones = embarque.isEsRecoleccion();
                    if (embarque.isEsRecoleccion()) {
//                    continue;
                        querys = querys + "If (SELECT ISNULL(p.FoliosPorSucursal,0) " +
                                "FROM CatParametrosConfiguracion2PQ p) = 1\n" +
                                "            begin\n" +
                                "                SET @FOLIO = (SELECT ISNULL(MAX(Recoleccion), 0) + 1 " +
                                "FROM ProRecoleccionPQ WHERE IdSucursal = " + embarque.getIdSucursalRegistro() + ");\n" +
                                "            end\n" +
                                "        else\n" +
                                "            begin\n" +
                                "                EXEC @FOLIO = GenerarSecuenciaPQ @proceso=1;\n" +
                                "\n" +
                                "            end\n" +
                                "        select @FOLIOSTR = [dbo].[GeneraFolioPQ](1, @FOLIO, "
                                + embarque.getIdSucursalRegistro() + ");\n" +
                                "INSERT INTO ProRecoleccionPQ(FolioRecoleccion,IdEmbarque,Fecha,Hora,FechaRegistro,HoraRegistro,IdEstatusRecoleccion,IdSucursal, IdUsuario\n" +
                                "                           ,Moneda, TipoCambio, IdTipoDecobro\n" +
                                "                           ,IdRemitente,AliasRemitente,NombreRemitente,RFCRemitente,CorreoRemitente,TelefonoRemitente,ContactoRemitente\n" +
                                "                           ,DomicilioRemitente,IdCodigoPostalRemitente,IdCiudadRemitente,CalleRemitente,NoIntRemitente,NoExtRemitente,ColoniaRemitente,IdEstadoRemitente, MunicipioRemitente\n" +
                                "                           ,IdCiudadOrigen\n" +
                                "                           ,IdDestinatario,AliasDestinatario,NombreDestinatario,RFCDestinatario,CorreoDestinatario,TelefonoDestinatario,ContactoDestinatario\n" +
                                "                           ,DomicilioDestinatario, IdCodigoPostalDestinatario, IdCiudadDestinatario,CalleDestinatario,NoIntDestinatario,NoExtDestinatario,ColoniaDestinatario,IdEstadoDestinatario, MunicipioDestinatario\n" +
                                "                           ,IdCiudadDestino,IdZonaOperativaEntrega\n" +
                                "                           ,EntregaDiferenteDomicilio,IdCPDetalleEntrega,IdCiudadDetalleEntrega,DomicilioDetalleEntrega,EntregarEnDetalleEntrega,DatosAdicionalesDetalleEntrega,IdEstadoEntrega,CodigoMunicipioEntrega\n" +
                                "                           ,EntregaSucursal,IdSucursalEntrega\n" +
                                "                           ,IdCliente,IdTipoSeguro, PorcentajeSeguro, AplicaSeguro,ValorDeclarado,Observaciones\n" +
                                "                           ,RecoleccionConCita, FechaCita, HoraCitaMinima, HoraCitaMaxima,CitaPendiente\n" +
                                "                           ,Latitud, Longitud\n" +
                                "                           ,RecoleccionDiferenteDomicilio,IdCPDetalleRecoleccion\n" +
                                "                           ,DomicilioDetalleRecoleccion,RecogerEnDetalleRecoleccion\n" +
                                "                           ,DatosAdicionalesDetalleRecoleccion, IdZonaOperativa, IdCertificado, IdEstatusUltimaMilla, Recoleccion, Referencia\n" +
                                "                           ,NoPaquetes,NoSobres,IdOperador,IdUnidad,IdRemolque,IdGuia\n" +
                                "                           )\n" +
                                "VALUES (@FOLIOSTR,0" + addString(embarque.getFechaRegistro()) + addString(embarque.getHoraRegistro()) + addString(embarque.getFechaRegistro()) + addString(embarque.getHoraRegistro()) + ",(select top 1 EstatusRecoleccion from CatParametrosConfiguracion2PQ)" + addInt(embarque.getIdSucursalRegistro()) + addInt(embarque.getIdUsuarioRegistro())
                                + addInt(embarque.getIdMoneda()) + addInt(embarque.getIdTipoCambio()) + addInt(embarque.getIdTipoCobro())
                                + addInt(embarque.getIdRemitente()) + addString(embarque.getAliasRemitente()) + addString(embarque.getNombreRemitente()) + addString(embarque.getRfcRemitente()) + addString(embarque.getCorreoRemitente()) + addString(embarque.getTelefonoRemitente()) + addString(embarque.getContactoRemitente())
                                + addString(embarque.getDomicilioRemitente()) + addInt(embarque.getIdCodigoPostalRemitente()) + addInt(0) + addString(embarque.getCalleRemitente()) + addString(embarque.getNumeroInteriorRemitente()) + addString(embarque.getNumeroExteriorRemitente()) + addString(embarque.getColoniaRemitente()) + addString(embarque.getIdEstadoRemitente()) + addString(embarque.getIdMunicipioRemitente())
                                + addInt(embarque.getIdOrigen())
                                + addInt(embarque.getIdDestinatario()) + addString(embarque.getAliasDestinatario()) + addString(embarque.getNombreDestinatario()) + addString(embarque.getRfcDestinatario()) + addString(embarque.getCorreoDestinatario()) + addString(embarque.getTelefonoDestinatario()) + addString(embarque.getContactoDestinatario())
                                + addString(embarque.getDomicilioDestinatario()) + addInt(embarque.getIdCodigoPostalDestinatario()) + addInt(0) + addString(embarque.getCalleDestinatario()) + addString(embarque.getNumeroInteriorDestinatario()) + addString(embarque.getNumeroExteriorDestinatario()) + addString(embarque.getColoniaDestinatario()) + addString(embarque.getIdEstadoDestinatario()) + addString(embarque.getIdMunicipioDestinatario())
                                + addInt(embarque.getIdDestino()) + addInt(embarque.getIdZonaEntrega())
                                + addBoolean(embarque.isEntregaDiferenteDomicilio()) + addInt(embarque.getIdCodigoPostalDiferenteDomicilio()) + addInt(0) + addString(embarque.getCalleNumeroDiferenteDomicilio()) + addString(embarque.getEntregarEn()) + addString(embarque.getDatosAdicionalesEntrega()) + addString(embarque.getIdEstadoDiferenteDomicilio()) + addString(embarque.getIdMunicipioDiferenteDomicilio())
                                + addBoolean(embarque.isEntregaEnSucursal()) + addInt(embarque.getIdSucursalEntrega())
                                + addInt(embarque.getIdCliente()) + addInt(embarque.getIdTipoSeguro()) + addFloat(embarque.getPorcentajeSeguro()) + addBoolean(embarque.isAplicaSeguro()) + addFloat(embarque.getValorDeclarado()) + addString(embarque.getObservaciones())
                                + addBoolean(embarque.isConCita()) + (!embarque.isConCita() || embarque.isCitaPendiente() ? ",null" : addString(embarque.getFechaCita())) + (!embarque.isConCita() || embarque.isCitaPendiente() ? ",null" : addString(embarque.getHoraCitaMinima())) + (!embarque.isConCita() || embarque.isCitaPendiente() ? ",null" : addString(embarque.getHoraCitaMaxima())) + addBoolean(embarque.isCitaPendiente())
                                + addString(embarque.getLatitud()) + addString(embarque.getLongitud())
                                + addBoolean(embarque.isRecoleccionDiferenteDomicilio()) + addInt(embarque.getIdCodigoPostalDiferenteDomicilioRecoleccion())
                                + addString(embarque.getCalleNumeroDiferenteDomicilioRecoleccion()) + addString(embarque.getRecogerEn())
                                + addString(embarque.getDatosAdicionalesRecoleccion()) + addInt(embarque.getIdZonaRecoleccion()) + ",(select TOP 1 c.IdCertificado from CatCertificados c WHERE EstatusActivo = 1), 1,@FOLIO" + addString(embarque.getReferencia()) + ",0,0,0,0,0,0"
                                + ");\n";
                        querys = querys + "set @IdRecoleccion = (SELECT IDENT_CURRENT( 'ProRecoleccionPQ' ));\n" +
                                "update ProRecoleccionPQ set Tracking=(SELECT CONCAT('R', ABS(CAST(CAST(NEWID() AS VARBINARY) AS INT)), @IdRecoleccion)) where IdRecoleccion = @IdRecoleccion;\n" +
                                "insert into BitacoraSeguimientoPQ (IdRegistro, Descripcion, Fecha, Hora, Proceso)\n" +
                                "values (@IdRecoleccion, 'Se registro la recolección ' + @FOLIOSTR " + addString(embarque.getFechaRegistro()) + addString(embarque.getHoraRegistro()) + ", 1);\n";

                        for (PaqueteImportadoRequest paquete : embarque.getPaquetes()) {
                            querys = querys +
                                    "insert into ProRecoleccionPaquetePQ (IdRecoleccion,IdTipo,Cantidad,IdProducto," +
                                    "Largo,Ancho,Alto,Volumen,Peso,IdTipoEmbalaje,Descripcion,Observaciones," +
                                    "ValorDeclarado\n" +
                                    "                                  )\n" +
                                    "values (@IdRecoleccion,2" + addFloat(paquete.getCantidad())
                                    + addInt(paquete.getIdProducto())
                                    + addFloat(paquete.getLargo()) + addFloat(paquete.getAncho())
                                    + addFloat(paquete.getAlto()) + addFloat(paquete.getVolumen())
                                    + addFloat(paquete.getPeso()) + addInt(paquete.getIdEmbalaje())
                                    + addString(paquete.getDescripcion()) + addString(paquete.getObservaciones())
                                    + ",0);\n";
                        }
                        for (ComplementoSATImportadoRequest complemento : embarque.getComplementosSAT()) {
                            querys = querys +
                                    "insert into ProRecoleccionComplementosSATPQ (IdRecoleccion,Cantidad,Peso\n" +
                                    "                                          ,ClaveProductoServicio,ClaveUnidad\n" +
                                    "                                          ,EsMaterialPeligroso" +
                                    ",ClaveMaterialPeligroso,ClaveEmbalaje,DescripcionEmbalaje" +
                                    ",ClaveFraccionArancelaria\n" +
                                    "                                          ,UUIDComercioExterior,TipoEmbalaje" +
                                    ",ClaveSectorCofepris,NombreIngredienteActivo,NombreQuimico," +
                                    "DenominacionGenericaProd,DenominacionDistintivaProd,Fabricante,FechaCaducidad" +
                                    ",LoteMedicamento,FormaFarmaceutica,CondicionesEspTransp," +
                                    "RegistroSanitarioFolioAutorizacion,NumeroCAS,NumRegSanPlagCOFEPRIS" +
                                    ",DatosFabricante,DatosFormulador,DatosMaquilador,UsoAutorizado)\n" +
                                    "values (@IdRecoleccion" + addInt(complemento.getCantidad())
                                    + addFloat(Float.parseFloat(complemento.getPeso()))
                                    + addString(complemento.getClaveProductoServicio())
                                    + addString(complemento.getClaveUnidadMedida())
                                    + addBoolean(complemento.isEsMaterialPeligroso())
                                    + addString(complemento.getClaveMaterialPeligroso())
                                    + addString(complemento.getClaveEmbalaje())
                                    + addString(complemento.getDescripcionEmbalaje())
                                    + addString(complemento.getClaveFraccionArancelaria())
                                    + addString(complemento.getClaveSectorCofepris())
                                    + addString(complemento.getNombreIngredienteActivo())
                                    + addString(complemento.getNombreQuimico())
                                    + addString(complemento.getDenominacionGenericaProd())
                                    + addString(complemento.getDenominacionDistintivaProd())
                                    + addString(complemento.getFabricante())
                                    + addString(complemento.getFechaCaducidad())
                                    + addString(complemento.getLoteMedicamento())
                                    + addString(complemento.getFormaFarmaceutica())
                                    + addString(complemento.getCondicionesEspTransp())
                                    + addString(complemento.getRegistroSanitarioFolioAutorizacion())
                                    + addString(complemento.getNumeroCAS())
                                    + addString(complemento.getNumRegSanPlagCOFEPRIS())
                                    + addString(complemento.getDatosFabricante())
                                    + addString(complemento.getDatosFormulador())
                                    + addString(complemento.getDatosMaquilador())
                                    + addString(complemento.getUsoAutorizado())
                                    + ",'', 'null');\n";
                        }
                        querys = querys +
                                "update CatCotizacion set IdRecoleccion = @IdRecoleccion,Activa=1 " +
                                "where IdCotizacion = " + embarque.getIdCotizacion() + ";\n";

                        for (ConceptoFacturacionImportadoRequest concepto : embarque.getConceptosFacturacion()) {
                            querys = querys +
                                    "insert into CatCotizacionConceptosPQ(IdCotizador, IdConceptoFacturacion," +
                                    " Importe, IdImpuestoTraslada, ImporteIva,IdImpuestoRetiene, ImporteRetiene," +
                                    " Descuento)\n" +
                                    "values (" + embarque.getIdCotizacion()
                                    + addInt(concepto.getM_nIdConceptosFacturacion()) + addFloat(concepto.getM_cImporte())
                                    + addInt(concepto.getM_nIdImpuestoTraslada()) + addFloat(concepto.getM_cImporteIva())
                                    + addInt(concepto.getM_nIdImpuestoRetiene()) + addFloat(concepto.getM_cImporteRetiene())
                                    + ",0);\n";
                        }
                        if (!embarque.isRecoleccionDiferenteDomicilio()) {
                            querys = querys + "update CatRemitentesDestinatarios\n" +
                                    "                SET Latitud  = '" + embarque.getLatitud() + "',\n" +
                                    "                    Longitud = '" + embarque.getLongitud() + "'\n" +
                                    "                Where IdRemitenteDestinatario = " + embarque.getIdRemitente() + ";\n";
                        }
                    } else {
                        querys = querys + "If (SELECT ISNULL(p.FoliosPorSucursal,0) " +
                                "FROM CatParametrosConfiguracion2PQ p) = 1\n" +
                                "            begin\n" +
                                "                SET @FOLIO = (SELECT ISNULL(MAX(Embarque), 0) + 1 " +
                                "FROM ProEmbarquePQ WHERE IdSucursal = " + embarque.getIdSucursalRegistro() + ");\n" +
                                "            end\n" +
                                "        else\n" +
                                "            begin\n" +
                                "                EXEC @FOLIO = GenerarSecuenciaPQ @proceso=2;\n" +
                                "            end\n" +
                                "select @FOLIOSTR = [dbo].[GeneraFolioPQ](2, @FOLIO, "
                                + embarque.getIdSucursalRegistro() + ");\n" +
                                "INSERT INTO ProEmbarquePQ(FolioEmbarque,IdRecoleccion,Fecha,Hora,FechaRegistro," +
                                "HoraRegistro,IdEstatusEmbarque,IdSucursal, IdUsuario\n" +
                                "                          ,IdMoneda, TipoCambio, IdTipoCobro\n" +
                                "                          ,IdRemitente,AliasRemitente,NombreRemitente," +
                                "RFCRemitente,CorreoRemitente,TelefonoRemitente,ContactoRemitente\n" +
                                "                          ,DomicilioRemitente,IdCodigoPostalRemitente," +
                                "IdCiudadRemitente,CalleRemitente,NoIntRemitente,NoExtRemitente,ColoniaRemitente," +
                                "IdEstadoRemitente, MunicipioRemitente\n" +
                                "                          ,IdCiudadOrigen\n" +
                                "                          ,IdDestinatario,AliasDestinatario,NombreDestinatario," +
                                "RFCDestinatario,CorreoDestinatario,TelefonoDestinatario,ContactoDestinatario\n" +
                                "                          ,DomicilioDestinatario, IdCodigoPostalDestinatario, " +
                                "IdCiudadDestinatario,CalleDestinatario,NoIntDestinatario,NoExtDestinatario," +
                                "ColoniaDestinatario,IdEstadoDestinatario, MunicipioDestinatario\n" +
                                "                          ,IdCiudadDestino,IdZonaOperativa\n" +
                                "                          ,EntregarMismoDomicilio,CodigoPostalEntrega," +
                                "IdCiudadEntrega,DomicilioEntrega,EntregarEn,DatosAdicionales,IdEstadoEntrega," +
                                "CodigoMunicipioEntrega\n" +
                                "                          ,EntregaEnSucursal,IdSucursalEntrega\n" +
                                "                          ,IdCliente,IdTipoSeguro, PorcentajeSeguro, " +
                                "AplicaSeguro,ValorDeclarado,Observaciones\n" +
                                "                          ,EmbarqueConCita, FechaCita, HoraCitaMinima, " +
                                "HoraCitaMaxima,CitaPendiente\n" +
                                "                          ,Latitud, Longitud\n" +
                                "                          ,idRuta\n" +
                                "                          ,ValidarTimbradoIngreso,TipoTimbrado, Embarque, Referencia\n" +
                                "                          ,FechaEntrega, HoraEntrega, NoPaquetes, NoSobres, " +
                                "IdOperador, IdUnidad, IdEmbarqueRelacionado, IdZonaTarifaRecoleccion,IdZonaTarifa," +
                                "IdZonaRemitente,IdZonaDestinatario,IdZonaEntrega,IdZonaOperativaRecoleccion," +
                                "IdComplemento,IdTipoDocumento\n" +
                                "                          )\n" +
                                "VALUES (@FOLIOSTR,0" + addString(embarque.getFechaRegistro())
                                + addString(embarque.getHoraRegistro()) + addString(embarque.getFechaRegistro())
                                + addString(embarque.getHoraRegistro()) + addInt(embarque.getIdEstatus())
                                + addInt(embarque.getIdSucursalRegistro()) + addInt(embarque.getIdUsuarioRegistro())
                                + addInt(embarque.getIdMoneda()) + addInt(embarque.getIdTipoCambio())
                                + addInt(embarque.getIdTipoCobro())
                                + addInt(embarque.getIdRemitente()) + addString(embarque.getAliasRemitente())
                                + addString(embarque.getNombreRemitente()) + addString(embarque.getRfcRemitente())
                                + addString(embarque.getCorreoRemitente()) + addString(embarque.getTelefonoRemitente())
                                + addString(embarque.getContactoRemitente())
                                + addString(embarque.getDomicilioRemitente())
                                + addInt(embarque.getIdCodigoPostalRemitente()) + addInt(0)
                                + addString(embarque.getCalleRemitente())
                                + addString(embarque.getNumeroInteriorRemitente())
                                + addString(embarque.getNumeroExteriorRemitente())
                                + addString(embarque.getColoniaRemitente())
                                + addString(embarque.getIdEstadoRemitente())
                                + addString(embarque.getIdMunicipioRemitente())
                                + addInt(embarque.getIdOrigen())
                                + addInt(embarque.getIdDestinatario())
                                + addString(embarque.getAliasDestinatario())
                                + addString(embarque.getNombreDestinatario())
                                + addString(embarque.getRfcDestinatario())
                                + addString(embarque.getCorreoDestinatario())
                                + addString(embarque.getTelefonoDestinatario())
                                + addString(embarque.getContactoDestinatario())
                                + addString(embarque.getDomicilioDestinatario())
                                + addInt(embarque.getIdCodigoPostalDestinatario()) + addInt(0)
                                + addString(embarque.getCalleDestinatario())
                                + addString(embarque.getNumeroInteriorDestinatario())
                                + addString(embarque.getNumeroExteriorDestinatario())
                                + addString(embarque.getColoniaDestinatario())
                                + addString(embarque.getIdEstadoDestinatario())
                                + addString(embarque.getIdMunicipioDestinatario())
                                + addInt(embarque.getIdDestino()) + addInt(embarque.getIdZonaEntrega())
                                + addBoolean(!embarque.isEntregaDiferenteDomicilio())
                                + addInt(embarque.getIdCodigoPostalDiferenteDomicilio()) + addInt(0)
                                + addString(embarque.getCalleNumeroDiferenteDomicilio())
                                + addString(embarque.getEntregarEn())
                                + addString(embarque.getDatosAdicionalesEntrega())
                                + addString(embarque.getIdEstadoDiferenteDomicilio())
                                + addString(embarque.getIdMunicipioDiferenteDomicilio())
                                + addBoolean(embarque.isEntregaEnSucursal())
                                + addInt(embarque.getIdSucursalEntrega())
                                + addInt(embarque.getIdCliente()) + addInt(embarque.getIdTipoSeguro())
                                + addFloat(embarque.getPorcentajeSeguro()) + addBoolean(embarque.isAplicaSeguro())
                                + addFloat(embarque.getValorDeclarado()) + addString(embarque.getObservaciones())
                                + addBoolean(embarque.isConCita())
                                + (!embarque.isConCita() || embarque.isCitaPendiente() ? ",null" :
                                addString(embarque.getFechaCita())) + (!embarque.isConCita() ||
                                embarque.isCitaPendiente() ? ",null" : addString(embarque.getHoraCitaMinima()))
                                + (!embarque.isConCita() || embarque.isCitaPendiente() ? ",null" :
                                addString(embarque.getHoraCitaMaxima())) + addBoolean(embarque.isCitaPendiente())
                                + addString(embarque.getLatitud()) + addString(embarque.getLongitud())
                                + addInt(embarque.getIdRuta())
                                + addBoolean(embarque.isValidarTimbradoFactura()) + addInt(embarque.getIdTipoServicio())
                                + ",@FOLIO" + addString(embarque.getReferencia())
                                + ",'1900-01-01', '00:00:00.0000000', 0, 0, 0, 0, 0, 0,0,0,0,0,0,0,0);\n";
                        querys = querys + "set @IdEmbarque = (SELECT IDENT_CURRENT( 'ProEmbarquePQ' ));\n" +
                                "update ProEmbarquePQ set Tracking=" +
                                "(SELECT CONCAT('G', ABS(CAST(CAST(NEWID() AS VARBINARY) AS INT)), @@IDENTITY)) " +
                                "where IdEmbarque = @IdEmbarque;\n" +
                                "insert into BitacoraSeguimientoPQ (IdRegistro, Descripcion, Fecha, Hora, Proceso)\n" +
                                "values (@IdEmbarque, 'Se registro el embarque ' + @FOLIOSTR "
                                + addString(embarque.getFechaRegistro()) + addString(embarque.getHoraRegistro())
                                + ", 2);\n";
                        for (PaqueteImportadoRequest paquete : embarque.getPaquetes()) {
                            querys = querys +
                                    "insert into ProEmbarqueDetallePQ (IdEmbarque,Tipo,ctd,IdProducto,Largo,Ancho," +
                                    "Alto,Volumen,Peso,IdTipoEmpaque,Descripcion,Observaciones\n" +
                                    "                                  ,Activo,ClaveSATProducto,ClaveSATUnidad," +
                                    " ClaveEmbalaje,ValorDeclarado)\n" +
                                    "values (@IdEmbarque,2" + addFloat(paquete.getCantidad())
                                    + addInt(paquete.getIdProducto())
                                    + addFloat(paquete.getLargo()) + addFloat(paquete.getAncho())
                                    + addFloat(paquete.getAlto()) + addFloat(paquete.getVolumen())
                                    + addFloat(paquete.getPeso()) + addInt(paquete.getIdEmbalaje())
                                    + addString(paquete.getDescripcion())
                                    + addString(paquete.getObservaciones())
                                    + ",0,'null','null', 'null',0);\n";
                        }
                        for (ComplementoSATImportadoRequest complemento : embarque.getComplementosSAT()) {
                            querys = querys +
                                    "insert into ProEmbarqueComplementosSATPQ (IdEmbarque,Cantidad,Peso\n" +
                                    "                                          ,ClaveProductoServicio,ClaveUnidad\n" +
                                    "                                          ,EsMaterialPeligroso," +
                                    "ClaveMaterialPeligroso,ClaveEmbalaje,DescripcionEmbalaje,ClaveFraccionArancelaria\n" +
                                    "                                          ,UUIDComercioExterior," +
                                    "TipoEmbalaje,ClaveSectorCofepris,NombreIngredienteActivo,NombreQuimico," +
                                    "DenominacionGenericaProd,DenominacionDistintivaProd,Fabricante,FechaCaducidad," +
                                    "LoteMedicamento,FormaFarmaceutica,CondicionesEspTransp," +
                                    "RegistroSanitarioFolioAutorizacion,NumeroCAS,NumRegSanPlagCOFEPRIS," +
                                    "DatosFabricante,DatosFormulador,DatosMaquilador,UsoAutorizado)\n" +
                                    "values (@IdEmbarque" + addInt(complemento.getCantidad())
                                    + addFloat(Float.parseFloat(complemento.getPeso()))
                                    + addString(complemento.getClaveProductoServicio())
                                    + addString(complemento.getClaveUnidadMedida())
                                    + addBoolean(complemento.isEsMaterialPeligroso())
                                    + addString(complemento.getClaveMaterialPeligroso())
                                    + addString(complemento.getClaveEmbalaje())
                                    + addString(complemento.getDescripcionEmbalaje())
                                    + addString(complemento.getClaveFraccionArancelaria()) + ",'','null'"
                                    + addString(complemento.getClaveSectorCofepris())
                                    + addString(complemento.getNombreIngredienteActivo())
                                    + addString(complemento.getNombreQuimico())
                                    + addString(complemento.getDenominacionGenericaProd())
                                    + addString(complemento.getDenominacionDistintivaProd())
                                    + addString(complemento.getFabricante())
                                    + addString(complemento.getFechaCaducidad())
                                    + addString(complemento.getLoteMedicamento())
                                    + addString(complemento.getFormaFarmaceutica())
                                    + addString(complemento.getCondicionesEspTransp())
                                    + addString(complemento.getRegistroSanitarioFolioAutorizacion())
                                    + addString(complemento.getNumeroCAS())
                                    + addString(complemento.getNumRegSanPlagCOFEPRIS())
                                    + addString(complemento.getDatosFabricante())
                                    + addString(complemento.getDatosFormulador())
                                    + addString(complemento.getDatosMaquilador())
                                    + addString(complemento.getUsoAutorizado())
                                    + ");\n";
                        }
                        querys = querys +
                                "insert into CatCotizacion(IdEmbarque,IdOrigen,IdDestino,IdZonaEntrega,IdCliente," +
                                "EntregaSucursal,ValorDeclarado\n" +
                                "                          ,IdSeguro,AplicaSeguro,PorcentajeSeguro, " +
                                "EmbarqueConCita, Activa\n" +
                                "                          ,AplicaRecoleccion,IdZonaRecoleccion,IdRecoleccion, " +
                                "RecoleccionConCita)\n" +
                                "values (@IdEmbarque" + addInt(embarque.getIdOrigen())
                                + addInt(embarque.getIdDestino())
                                + addInt(embarque.getIdZonaEntrega())
                                + addInt(embarque.getIdCliente())
                                + addBoolean(embarque.isEntregaEnSucursal())
                                + addFloat(embarque.getValorDeclarado())
                                + addInt(embarque.getIdTipoSeguro())
                                + addBoolean(embarque.isAplicaSeguro())
                                + addFloat(embarque.getPorcentajeSeguro())
                                + addBoolean(embarque.isConCita()) +
                                ",1,0,0,0,0);\n" +
                                "set @IdCotizacion = (SELECT IDENT_CURRENT( 'CatCotizacion' ));\n";

                        for (ConceptoFacturacionImportadoRequest concepto : embarque.getConceptosFacturacion()) {
                            querys = querys +
                                    "insert into CatCotizacionConceptosPQ(IdCotizador, IdConceptoFacturacion," +
                                    " Importe, IdImpuestoTraslada, ImporteIva,IdImpuestoRetiene, ImporteRetiene," +
                                    " Descuento)\n" +
                                    "values (@IdCotizacion " + addInt(concepto.getM_nIdConceptosFacturacion())
                                    + addFloat(concepto.getM_cImporte())
                                    + addInt(concepto.getM_nIdImpuestoTraslada())
                                    + addFloat(concepto.getM_cImporteIva())
                                    + addInt(concepto.getM_nIdImpuestoRetiene())
                                    + addFloat(concepto.getM_cImporteRetiene())
                                    + ",0);\n";
                        }
                        if (!embarque.isEntregaDiferenteDomicilio() && !embarque.isEntregaEnSucursal()) {
                            querys = querys + "update CatRemitentesDestinatarios\n" +
                                    "                SET Latitud  = '" + embarque.getLatitud() + "',\n" +
                                    "                    Longitud = '" + embarque.getLongitud() + "'\n" +
                                    "                Where IdRemitenteDestinatario = " + embarque.getIdDestinatario()
                                    + ";\n";
                        }
                    }
                }
                String queryFinal = inicioQuery + querys + finQuery;
                statement.executeUpdate(queryFinal);
                

                if (sonRecolecciones) {
                    return ResponseEntity.ok("Se importaron las recolecciones con éxito");
                } else {
                    return ResponseEntity.ok("Se importaron los embarques con éxito");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información." +
                        " Intente más tarde.");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al agregar los registros. Intente de nuevo.");
        }
    }
}


