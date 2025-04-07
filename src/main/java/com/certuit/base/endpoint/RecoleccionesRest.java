package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.*;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.certuit.base.util.UtilFuctions.convertObject;

@RestController
@RequestMapping(value = "/api")
public class RecoleccionesRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    RecoleccionService recoleccionService;
    @Autowired
    PaquetesService paquetesService;
    @Autowired
    ComplementosSATService complementosService;
    @Autowired
    ConceptosService conceptosService;

    @GetMapping("/Recoleccion/GetByFiltro/{sFechaInicial}/{sFechaFinal}/{sSucursal}/{sEstatus}/{folio}/{origen}/{destino}/{cliente}")
    public ResponseEntity<?> getListadoEmbarquesFiltro(@PathVariable("sFechaInicial") String fechaInicial,
                                                       @PathVariable("sFechaFinal") String fechaFinal,
                                                       @PathVariable("sSucursal") int sucursal,
                                                       @PathVariable("sEstatus") String estatus,
                                                       @PathVariable("folio") String folio,
                                                       @PathVariable("origen") int origen,
                                                       @PathVariable("destino") int destino,
                                                       @PathVariable("cliente") int cliente,
                                                       @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            fechaInicial = !fechaInicial.equalsIgnoreCase("0") ? "'" + fechaInicial + "'" : null;
            fechaFinal = !fechaFinal.equalsIgnoreCase("0") ? "'" + fechaFinal + "'" : null;
            folio = !folio.equalsIgnoreCase("0") ? folio : null;
            if (folio != null) {
                folio = folio.toUpperCase();
            }
            String query = "SELECT " +
                    " FORMAT(pr.FechaRegistro, 'dd-MM-yyyy') + ' ' +  convert(char(5),convert(time(0),pr.HoraRegistro)) as m_sFechaHora,\n" +
                    "                pr.IdRecoleccion as m_nIdRecoleccion,\n" +
                    "                pr.FolioRecoleccion as m_sFolioRecoleccion,\n" +
                    "                pr.Observaciones as m_sObservaciones,\n" +
                    "                e.FolioEmbarque as m_sFolioEmbarque,\n" +
                    "                cz.CodigoZona as m_sZonaRecoleccion,\n" +
                    "                pr.IdSucursal as m_nIdSucursal,\n" +
                    "                (select CASE WHEN pr.Timbrado IS NOT NULL THEN pr.Timbrado ELSE 0 END) AS m_nTimbrado,\n" +
                    "                cs.Sucursal as m_sSucursal,\n" +
                    "                er.Estatus as m_sEstatusRecoleccion,\n" +
                    "                cerr.Color as m_sColorEstatus,\n" +
                    "                pr.IdEstatusRecoleccion as m_nIdEstatusRecoleccion,\n" +
                    "                pr.RecoleccionConCita as m_bRecoleccionConCita,\n" +
                    "                pr.FechaCita as m_sFechaCita,\n" +
                    "                pr.IdInforme as m_nIdInforme,\n" +
                    "                pr.IdGuia as m_nIdGuia,\n" +
                    "                pr.IdEmbarque as m_nIdEmbarque,\n" +
                    "                pr.HoraCitaMaxima  as m_sHoraCitaMaxima,\n" +
                    "                pr.HoraCitaMinima as m_sHoraCitaMinima,\n" +
                    "                ISNULL(FORMAT(pr.FechaCita, 'dd-MM-yyyy') + ' ' +  " +
                    "convert(char(5),convert(time(0),pr.HoraCitaMinima)),'NO APLICA') as m_sFechaHoraDetalleRec,\n" +
                    "                cc.NombreFiscal as m_sNombreCliente ,\n" +
                    "                codo.OrigenDestino as m_sCiudadOrigen,\n" +
                    "                codd.OrigenDestino as m_sCiudadDestino,\n" +
                    "                us.Nombre as m_sUsuarioDocumento,\n" +
                    "                pr.Referencia as m_sReferencia\n" +
                    "                FROM ProRecoleccionPQ pr " +
                    "                LEFT JOIN ProEmbarquePQ e ON pr.IdEmbarque  = e.IdEmbarque\n" +
                    "                LEFT JOIN CatZonasOperativasPQ cz ON pr.IdZonaOperativa  = cz.IdZona\n" +
                    "                LEFT JOIN CatSucursales cs ON pr.IdSucursal = cs.IdSucursal\n" +
                    "                LEFT JOIN CatEstatusRecoleccionPQ er ON pr.IdEstatusRecoleccion = er.IdEstatusRecoleccion\n" +
                    "                LEFT JOIN CatClientes cc ON pr.IdCliente = cc.IdCliente\n" +
                    "                LEFT JOIN CatOrigenesDestinos codd ON pr.IdCiudadDestino = codd.IdOrigenDestino\n" +
                    "                LEFT JOIN CatOrigenesDestinos codo ON pr.IdCiudadOrigen = codo.IdOrigenDestino\n" +
                    "                LEFT JOIN CatUsuarios us ON pr.IdUsuario = us.IdUsuario\n" +
                    "                LEFT JOIN CatEstatusRecoleccionPQ cerr ON pr.IdEstatusRecoleccion = cerr.IdEstatusRecoleccion\n" +
                    "WHERE (pr.FolioRecoleccion LIKE '%" + folio + "%')" +
                    " OR ((pr.IdSucursal = " + sucursal + " OR " + sucursal + " = 0) " +
                    "AND (pr.IdEstatusRecoleccion = " + estatus + " OR " + estatus + " = 0)" +
                    " AND (pr.Fecha >= " + fechaInicial + " OR " + fechaInicial + " IS NULL) " +
                    "AND (pr.Fecha <= " + fechaFinal + " OR " + fechaFinal + " IS NULL) " +
                    " AND (pr.IdCliente = " + cliente + " OR " + cliente + " = 0)" +
                    "AND (pr.IdCiudadOrigen = " + origen + " OR " + origen + " = 0) " +
                    "AND (pr.IdCiudadDestino = " + destino + " OR " + destino + " = 0) and " + "'" + folio + "'" + " = 'null' )" +
                    "ORDER BY pr.Fecha DESC, pr.Hora DESC";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/SisEstatus/getListadoRecoleccion")
    public ResponseEntity<?> getOrigenDestino(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdEstatusRecoleccion as m_nIdEstatusRecoleccion," +
                    "Abreviacion as m_sAbreviacion, " +
                    "Estatus as m_sEstatus, " +
                    "Descripcion as m_sDescripcion, " +
                    "Color as m_sColor " +
                    "FROM CatEstatusRecoleccionPQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Recoleccion/{id}/notificacion")
    public ResponseEntity<?> enviarNotificacion(@RequestHeader("RFC") String rfc,
                                                @PathVariable("id") int id,
                                                @RequestBody EnvioCorreosRequest request)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select  CCC.CorreoElectronico AS 'Correos' from ProRecoleccionPQ PIP " +
                    "inner join CatClientes CC on PIP.IdCliente = CC.IdCliente " +
                    "inner join CatClientesContactos CCC on CC.IdCliente = CCC.IdCliente " +
                    "where PIP.IdRecoleccion = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);
            String[] correo = new String[1];
            correo[0] = "arenteria@certuit.com";
            recoleccionService.enviarCorreoRecoleccion(id, jdbcConnection, correo, rfc, request.getMensaje());
            return ResponseEntity.ok("Se envió el correo con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /*@GetMapping("/Recoleccion/getComplementoPaquetes/{idRecoleccion}")
    public ResponseEntity<?> getComplementoPaquetes(@PathVariable("idRecoleccion") int idRecoleccion,@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        String query = "select CTE.Nombre,PRP.Descripcion,PRP.Peso,PRP.Largo,PRP.Ancho,PRP.Alto,PRP.Cantidad,concat(cppq.NoProducto,'-',cppq.Descripcion) Producto, \n" +
                "\n" +
                "(isnull(PRP.Largo,1)*isnull(PRP.Ancho,1)*isnull(PRP.Alto,1)*isnull(PRP.Cantidad,1)*0.0005)  PesoVolumetrico, (isnull(PRP.Peso,1)*isnull(PRP.Cantidad,1)) PesoReal\n" +
                "\n" +
                "from ProRecoleccionPQ PR \n" +
                "\n" +
                "inner join ProRecoleccionPaquetePQ PRP on PRP.IdRecoleccion=PR.IdRecoleccion\n" +
                "\n" +
                "inner join CatEmbalajesPQ  CTE on CTE.IdEmbalaje=PRP.IdTipoEmbalaje\n" +
                "\n" +
                "inner join CatProductosPQ cppq on cppq.IdProducto=PRP.IdProducto\n" +
                "\n" +
                "where PR.IdRecoleccion="+idRecoleccion+"";

        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return ResponseEntity.ok(UtilFuctions.convertArray(rs).toString());
    }*/

    @GetMapping("/Recoleccion/GetById/{id}")
    public ResponseEntity<?> getRecoleccionId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            if (jdbcConnection == null) {
                return ResponseEntity.status(500).body("No se pudo conectar con el servidor. Intente de nuevo.");
            }
            String query = "SELECT \n" +
                    "    e.IdRecoleccion as m_nIdRecoleccion, \n" +
                    "    e.IdSucursal as m_nIdSucursal, \n" +
                    "    e.IdEmbarque as m_nIdEmbarque, \n" +
                    "    e.IdInforme as m_nIdInforme, \n" +
                    "    e.Fecha as m_dFecha, \n" +
                    "    e.Hora as m_tHora, \n" +
                    "    e.IdEstatusRecoleccion as m_nIdEstatusRecoleccion, \n" +
                    "    e.Moneda as m_nMoneda, \n" +
                    "    e.TipoCambio as m_rTipoCambio, \n" +
                    "    e.IdTipoDecobro as m_nIdTipoDeCobro, \n" +
                    "    e.NombreRemitente as m_sNombreRemitente, \n" +
                    "    e.NombreDestinatario as m_sNombreDestinatario, \n" +
                    "    e.RFCRemitente as m_sRFCRemitente, \n" +
                    "    e.RFCDestinatario as m_sRFCDestinatario, \n" +
                    "    e.DomicilioRemitente as m_sDomicilioRemitente, \n" +
                    "    e.DomicilioDestinatario as m_sDomicilioDestinatario, \n" +
                    "    e.IdEstadoRemitente as m_nIdEstadoRemitente, \n" +
                    "    e.IdEstadoDestinatario as m_nIdEstadoDestinatario, \n" +
                    "    e.MunicipioRemitente as m_sMunicipioRemitente, \n" +
                    "    e.MunicipioDestinatario as m_sMunicipioDestinatario, \n" +
                    "    e.IdCodigoPostalRemitente as m_sIdCodigoPostalRemitente, \n" +
                    "    rem.CodigoPostal as m_sCodigoPostalRemitente, \n" +
                    "    e.IdCodigoPostalDestinatario as m_sIdCodigoPostalDestinatario, \n" +
                    "    dest.CodigoPostal as m_sCodigoPostalDestinatario, \n" +
                    "    e.IdCiudadRemitente as m_nIdCiudadRemitente, \n" +
                    "    e.IdCiudadDestinatario as m_nIdCiudadDestinatario, \n" +
                    "    e.CorreoRemitente as m_sCorreoRemitente, \n" +
                    "    e.CorreoDestinatario as m_sCorreoDestinatario, \n" +
                    "    e.TelefonoRemitente as m_sTelefonoRemitente, \n" +
                    "    e.TelefonoDestinatario as m_sTelefonoDestinatario, \n" +
                    "    e.ContactoRemitente as m_sContactoRemitente, \n" +
                    "    e.ContactoDestinatario as m_sContactoDestinatario, \n" +
                    "    e.IdCiudadOrigen as m_nIdCiudadOrigen, \n" +
                    "    e.IdCiudadDestino as m_nIdCiudadDestino, \n" +
                    "    e.IdOperador as m_nIdOperador, \n" +
                    "    e.IdUnidad as m_nIdUnidad, \n" +
                    "    e.IdRemolque as m_nIdRemolque, \n" +
                    "    e.FechaSalida as m_dFechaSalida, \n" +
                    "    e.FechaLlegada as m_dFechaLlegada, \n" +
                    "    e.HoraSalida as m_tHoraSalida, \n" +
                    "    e.HoraLlegada as m_tHoraLlegada, \n" +
                    "    e.FechaDetalleRecoleccion as m_dFechaDetalleRecoleccion, \n" +
                    "    e.HoraDetalleRecoleccion as m_tHoraDetalleRecoleccion, \n" +
                    "    e.IdCPDetalleRecoleccion as m_nIdCPDetalleRecoleccion, \n" +
                    "    e.IdCiudadDetalleRecoleccion as m_nIdCiudadDetalleRecoleccion, \n" +
                    "    e.DomicilioDetalleRecoleccion as m_sDomicilioDetalleRecoleccion, \n" +
                    "    e.DatosAdicionalesDetalleRecoleccion as m_sDatosAdicionalesDetalleRecoleccion, \n" +
                    "    e.IdCiudadDetalleEntrega as m_nIdCiudadDetalleEntrega, \n" +
                    "    e.IdCPDetalleEntrega as m_nIdCPDetalleEntrega, \n" +
                    "    e.IdZonaDetalleEntrega as m_nIdZonaDetalleEntrega, \n" +
                    "    e.DomicilioDetalleEntrega as m_sDomicilioDetalleEntrega, \n" +
                    "    e.EntregarEnDetalleEntrega as m_sEntregarEnDetalleEntrega, \n" +
                    "    e.DatosAdicionalesDetalleEntrega as m_sDatosAdicionalesDetalleEntrega, \n" +
                    "    e.FechaRecoleccionSalidaRecoleccion as m_dFechaRecoleccionSalidaRecoleccion, \n" +
                    "    e.HoraRecoleccionSalidaRecoleccion as m_tHoraRecoleccionSalidaRecoleccion, \n" +
                    "    e.IdZonaDetalleRecoleccion as m_nIdZonaDetalleRecoleccion, \n" +
                    "    e.RecogerEnDetalleRecoleccion as m_sRecogerEnDetalleRecoleccion, \n" +
                    "    e.MotivoCancelacion as m_sMotivoCancelacion, \n" +
                    "    e.FechaCancelacion as m_dtFechaCancelacion, \n" +
                    "    e.UsuarioCancelacion as m_nUsuarioCancelacion, \n" +
                    "    e.IdGuia as m_nIdGuia, \n" +
                    "    e.FolioRecoleccion as m_sFolioRecoleccion, \n" +
                    "    e.FechaRegistro as m_dFechaRegistro, \n" +
                    "    e.HoraRegistro as m_tHoraRegistro, \n" +
                    "    e.NoSobres as m_nNoSobres, \n" +
                    "    e.NoPaquetes as m_nNoPaquetes, \n" +
                    "    e.IdCliente as m_nIdCliente, \n" +
                    "    e.RecoleccionDiferenteDomicilio as m_bRecoleccionDiferenteDomicilio, \n" +
                    "    e.EntregaDiferenteDomicilio as m_bEntregaDiferenteDomicilio, \n" +
                    "    e.IdZonaRemitente as m_nIdZonaRemitente, \n" +
                    "    e.IdZonaDestinatario as m_nIdZonaDestinatario, \n" +
                    "    e.IdRemitente as m_nIdRemitente, \n" +
                    "    e.AliasRemitente as m_sAliasRemitente, \n" +
                    "    e.IdDestinatario as m_nIdDestinatario, \n" +
                    "    e.AliasDestinatario as m_sAliasDestinatario, \n" +
                    "    e.RecoleccionConCita as m_bRecoleccionConCita, \n" +
                    "    e.FechaCita as m_sFechaCita, \n" +
                    "    LEFT(e.HoraCitaMinima,5) as m_sHoraCitaMinima, \n" +
                    "    LEFT(e.HoraCitaMaxima,5) as m_sHoraCitaMaxima, \n" +
                    "    e.CalleRemitente as m_sCalleRemitente, \n" +
                    "    e.NoIntRemitente as m_sNoIntRemitente, \n" +
                    "    e.NoExtRemitente as m_sNoExtRemitente, \n" +
                    "    e.ColoniaRemitente as m_sColoniaRemitente, \n" +
                    "    e.CalleDestinatario as m_sCalleDestinatario, \n" +
                    "    e.NoIntDestinatario as m_sNoIntDestinatario, \n" +
                    "    e.NoExtDestinatario as m_sNoExtDestinatario, \n" +
                    "    e.ColoniaDestinatario as m_sColoniaDestinatario, \n" +
                    "    e.IdZonaOperativa as m_nIdZonaOperativa, \n" +
                    "    e.IdZonaTarifa as m_nIdZonaTarifa, \n" +
                    "    e.IdZonaOperativaEntrega as m_nIdZonaOperativaEntrega, \n" +
                    "    e.IdZonaTarifaEntrega as m_nIdZonaTarifaEntrega, \n" +
                    "    e.IdEstadoRecoleccion as m_nIdEstadoRecoleccion, \n" +
                    "    e.CodigoMunicipioRecoleccion as m_sCodigoMunicipioRecoleccion, \n" +
                    "    e.IdEstadoEntrega as m_nIdEstadoEntrega, \n" +
                    "    edoEnt.IdPais as m_nIdPaisEntrega, \n" +
                    "    e.CodigoMunicipioEntrega as m_sCodigoMunicipioEntrega, \n" +
                    "    e.Latitud as m_sLatitud, \n" +
                    "    e.Longitud as m_sLongitud, \n" +
                    "    e.ValorDeclarado as m_xValorDeclarado, \n" +
                    "    e.IdSucursalEntrega as m_nIdSucursalEntrega, \n" +
                    "    e.EntregaSucursal as m_bEntregaSucursal \n" +
                    "    ,e.IdTipoSeguro as m_nIdTipoSeguro \n" +
                    "    ,e.PorcentajeSeguro as m_xPorcentajeSeguro \n" +
                    "    ,e.AplicaSeguro as m_bAplicaSeguro \n" +
                    "    ,cc.IdCotizacion as m_nIdCotizacion  \n" +
                    "    ,e.CitaPendiente as m_bCitaPendiente \n" +
                    "    ,g.FolioGuia as m_sFolioGuia \n" +
                    "    ,pr.FolioEmbarque as m_sFolioEmbarque  \n" +
                    "    ,e.Observaciones as m_sObservaciones  \n" +
                    "    ,e.Observaciones as m_sObservaciones  \n" +
                    "    ,edoRem.Estado as m_sEstadoRemitente \n" +
                    "    ,edoDes.Estado as m_sEstadoDestinatario \n" +
                    "    ,edoEnt.Estado as m_sEstadoEntrega \n" +
                    "    ,edoReco.Estado as m_sEstadoRecoleccion \n" +
                    "    ,edoReco.IdPais as m_nIdPaisRecoleccion \n" +
                    "    ,psRem.Pais as m_sPaisRemitente \n" +
                    "    ,psDes.Pais as m_sPaisDestinatario \n" +
                    "    ,psReco.Pais as m_sPaisRecoleccion \n" +
                    "    ,psEnt.Pais as m_sPaisEntrega \n" +
                    "    ,rem.Municipio as m_sMunicipioRemitente \n" +
                    "    ,dest.Municipio as m_sMunicipioDestinatario \n" +
                    "    ,cpRec.CodigoPostal as m_sCodigoPostalRecoleccion \n" +
                    "    ,cpRec.Municipio as m_sMunicipioRecoleccion \n" +
                    "    ,cpRec.Localidad as m_sLocalidadRecoleccion \n" +
                    "    ,cpRec.Colonia as m_sColoniaRecoleccion \n" +
                    "    ,cpEnt.CodigoPostal as m_sCodigoPostalEntrega \n" +
                    "    ,cpEnt.Municipio as m_sMunicipioEntrega \n" +
                    "    ,cpEnt.Localidad as m_sLocalidadEntrega \n" +
                    "    ,cpEnt.Colonia as m_sColoniaEntrega \n" +
                    "    ,zonaEnt.CodigoZona as m_sCodigoZonaEntrega\n" +
                    "    ,zonaEnt.AplicaEntrega as m_bAplicaEntrega\n" +
                    "    ,e.Receptor as m_sReceptorRecoleccion\n" +
                    "    ,e.Referencia as m_sReferencia\n" +
                    "    FROM ProRecoleccionPQ e\n" +
                    "    left join ProGuiaPQ g on g.IdGuia = (select IdGuia from ProEmbarquePQ pe " +
                    "where pe.IdEmbarque = e.IdEmbarque)  \n" +
                    "    left join ProEmbarquePQ pr on pr.IdEmbarque = e.IdEmbarque  \n" +
                    "    left join CatCotizacion cc on cc.IdCotizacion = (select top 1 c.IdCotizacion " +
                    "from CatCotizacion c where c.IdRecoleccion = e.IdRecoleccion and c.Activa = 1  " +
                    "order by IdCotizacion desc) \n" +
                    "    LEFT JOIN CatRemitentesDestinatarios rem ON rem.IdRemitenteDestinatario = e.IdRemitente \n" +
                    "    LEFT JOIN CatRemitentesDestinatarios dest ON dest.IdRemitenteDestinatario = e.IdDestinatario \n" +
                    "    LEFT JOIN CatEstados edoRem on edoRem.IdEstado = rem.IdEstado \n" +
                    "    LEFT JOIN CatEstados edoDes on edoDes.IdEstado = dest.IdEstado \n" +
                    "    LEFT JOIN CatEstados edoReco on edoReco.IdEstado = e.IdEstadoRecoleccion \n" +
                    "    LEFT JOIN CatEstados edoEnt on edoEnt.IdEstado = e.IdEstadoEntrega \n" +
                    "    LEFT JOIN CatPaises psRem on edoRem.IdPais = psRem.IdPais \n" +
                    "    LEFT JOIN CatPaises psDes on edoDes.IdPais = psDes.IdPais \n" +
                    "    LEFT JOIN CatPaises psReco on edoReco.IdPais = psReco.IdPais \n" +
                    "    LEFT JOIN CatPaises psEnt on edoEnt.IdPais = psEnt.IdPais \n" +
                    "    LEFT JOIN CatCodigosPostales cpRec ON cpRec.IdCodigoPostal = e.IdCPDetalleRecoleccion \n" +
                    "    LEFT JOIN CatCodigosPostales cpEnt ON cpEnt.IdCodigoPostal = e.IdCPDetalleEntrega \n" +
                    "    LEFT JOIN CatZonasOperativasPQ zonaEnt ON zonaEnt.IdZona = e.IdZonaOperativaEntrega\n" +
                    "    LEFT JOIN CatClientes cliente ON cliente.IdCliente = e.IdCliente\n" +
                    "    WHERE e.IdRecoleccion =" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);
            query = "Select m_nIdCliente=IdCliente,m_nNumeroCliente=NumeroCliente,m_sNombreFiscal=NombreFiscal\n" +
                    ",m_bCreditoVencido = IIF(Credito <= 0 and DiasCredito > 0,CAST(1 AS BIT), CAST(0 AS BIT))\n" +
                    ",m_bSinCredito = IIF(DiasCredito = 0,CAST(1 AS BIT), CAST(0 AS BIT))\n" +
                    ",m_nDiasCredito = DiasCredito\n" +
                    "From CatClientes where IdCliente =" + jsonObject.getInt("m_nIdCliente");
            jsonObject.put("cliente", convertObject(statement.executeQuery(query)));
            jsonObject.put("m_parrPaquetes", recoleccionService.getPaquetesRecoleccion(id, jdbcConnection, rfc));
            jsonObject.put("m_parrSobres", new ArrayList<>());
            jsonObject.put("m_arrClsComplementoSAT", recoleccionService.getConceptosSATRecoleccion(id, jdbcConnection));
            jsonObject.put("m_arrConceptos", recoleccionService.getConceptosCotizacionRecoleccion(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Recoleccion/Agregar")
    public ResponseEntity<?> postRecoleccion(@RequestBody RecoleccionRequest recoRequest,
                                             @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {

        int esRecoleccionDiferenteDomicilio;
        if (recoRequest.isM_bRecoleccionDiferenteDomicilio()) {
            esRecoleccionDiferenteDomicilio = 1;
        } else {
            esRecoleccionDiferenteDomicilio = 0;
        }
        int esEntregaDiferenteDomicilio;
        if (recoRequest.isM_bEntregaDiferenteDomicilio()) {
            esEntregaDiferenteDomicilio = 1;
        } else {
            esEntregaDiferenteDomicilio = 0;
        }
        int esRecoleccionConCita;
        if (recoRequest.isM_bRecoleccionConCita()) {
            esRecoleccionConCita = 1;
        } else {
            esRecoleccionConCita = 0;
        }
        int esEntregaSucursal;
        if (recoRequest.isM_bEntregaEnSucursal()) {
            esEntregaSucursal = 1;
        } else {
            esEntregaSucursal = 0;
        }
        int aplicaSeguro;
        if (recoRequest.isM_bAplicaSeguro()) {
            aplicaSeguro = 1;
        } else {
            aplicaSeguro = 0;
        }
        int citaPendiente;
        if (recoRequest.isM_bCitaPendiente()) {
            citaPendiente = 1;
        } else {
            citaPendiente = 0;
        }
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdCliente FROM CatClientes WHERE Activo=1 AND IdCliente="
                    + recoRequest.getM_nIdCliente();
            JSONObject cliente = convertObject(jdbcConnection.createStatement().executeQuery(query));
            if (cliente == null) {
                return ResponseEntity.status(500).body("El cliente no existe o se inactivó, " +
                        "favor de validar en catálogo de clientes.");
            }
            query = "EXEC usp_ProRecoleccionAgregarPQ " +
                    recoRequest.getM_nIdSucursal() + ","
                    + recoRequest.getM_nIdEmbarque() + ","
                    + recoRequest.getM_nIdInforme() + ","
                    + recoRequest.getM_nIdGuia() + ",'"
                    + recoRequest.getM_sFecha() + "','"
                    + recoRequest.getM_sHora() + "','"
                    + recoRequest.getM_sFecha() + "','"
                    + recoRequest.getM_sHora() + "',"
                    + recoRequest.getM_nIdEstatusRecoleccion() + ","
                    + recoRequest.getM_nMoneda() + ", "
                    + recoRequest.getM_rTipoCambio() + ","
                    + recoRequest.getM_nIdTipoDeCobro() + ",'"
                    + recoRequest.getM_sNombreRemitente() + "','"
                    + recoRequest.getM_sNombreDestinatario() + "','"
                    + recoRequest.getM_sRFCRemitente() + "','"
                    + recoRequest.getM_sRFCDestinatario() + "','"
                    + recoRequest.getM_sDomicilioRemitente() + "'" + ",'"
                    + recoRequest.getM_sDomicilioDestinatario() + "', "
                    + recoRequest.getM_sIdCodigoPostalRemitente() + ", "
                    + recoRequest.getM_sIdCodigoPostalDestinatario() + ", "
                    + recoRequest.getM_nIdCiudadRemitente() + ","
                    + recoRequest.getM_nIdCiudadDestinatario() + ",'"
                    + recoRequest.getM_sCorreoRemitente() + "','"
                    + recoRequest.getM_sCorreoDestinatario() + "','"
                    + recoRequest.getM_sTelefonoRemitente() + "'" + ",'"
                    + recoRequest.getM_sTelefonoDestinatario() + "', '"
                    + recoRequest.getM_sContactoRemitente() + "', '"
                    + recoRequest.getM_sContactoDestinatario() + "', "
                    + recoRequest.getM_nIdCiudadOrigen() + ", "
                    + recoRequest.getM_nIdCiudadDestino() + ", "
                    + recoRequest.getM_nNoPaquetes() + ", "
                    + recoRequest.getM_nNoSobres() + ", "
                    + recoRequest.getM_nIdOperador() + ", "
                    + recoRequest.getM_nIdUnidad() + ", "
                    + recoRequest.getM_nIdRemolque() + ",'"
                    + recoRequest.getM_dFechaSalida() + "', '"
                    + recoRequest.getM_dFechaLlegada() + "', '"
                    + recoRequest.getM_tHoraSalida() + "', '"
                    + recoRequest.getM_tHoraLlegada() + "'" + ",'"
                    + recoRequest.getM_dFechaDetalleRecoleccion() + "', '"
                    + recoRequest.getM_tHoraDetalleRecoleccion() + "', "
                    + recoRequest.getM_nIdCPDetalleRecoleccion() + ", "
                    + recoRequest.getM_nIdCiudadDetalleRecoleccion() + ", "
                    + recoRequest.getM_nIdZonaDetalleRecoleccion() + ", '"
                    + recoRequest.getM_sDomicilioDetalleRecoleccion() + "', '"
                    + recoRequest.getM_sRecogerEnDetalleRecoleccion() + "', '"
                    + recoRequest.getM_sDatosAdicionalesDetalleRecoleccion() + "'"
                    + "," + recoRequest.getM_nIdCPDetalleEntrega() + ", "
                    + recoRequest.getM_nIdCiudadDetalleEntrega() + ", "
                    + recoRequest.getM_nIdZonaDetalleEntrega() + ", '"
                    + recoRequest.getM_sDomicilioDetalleEntrega() + "', '"
                    + recoRequest.getM_sEntregarEnDetalleEntrega() + "', '"
                    + recoRequest.getM_sDatosAdicionalesDetalleEntrega() + "', "
                    + recoRequest.getM_nIdCliente() + ", "
                    + esRecoleccionDiferenteDomicilio + ", "
                    + esEntregaDiferenteDomicilio + ","
                    + recoRequest.getM_nIdZonaRemitente() + ", "
                    + recoRequest.getM_nIdZonaDestinatario() + ", "
                    + recoRequest.getM_nIdRemitente() + ", "
                    + recoRequest.getM_nIdDestinatario() + ", '"
                    + recoRequest.getM_sAliasRemitente() + "', '"
                    + recoRequest.getM_sAliasDestinatario() + "'" + ","
                    + esRecoleccionConCita + ", '"
                    + recoRequest.getM_sFechaCita() + "', '"
                    + recoRequest.getM_sHoraCitaMinima() + "', '"
                    + recoRequest.getM_sHoraCitaMaxima() + "'" + ",'"
                    + recoRequest.getM_sCalleRemitente() + "', '"
                    + recoRequest.getM_sNoIntRemitente() + "', '"
                    + recoRequest.getM_sNoExtRemitente() + "', '"
                    + recoRequest.getM_sColoniaRemitente() + "'," + "'"
                    + recoRequest.getM_sCalleDestinatario() + "', '"
                    + recoRequest.getM_sNoIntDestinatario() + "', '"
                    + recoRequest.getM_sNoExtDestinatario() + "', '"
                    + recoRequest.getM_sColoniaDestinatario() + "', '"
                    + recoRequest.getM_sLatitudR() + "', '"
                    + recoRequest.getM_sLongitudR() + "'" + ","
                    + recoRequest.getM_nIdZonaOperativa() + ", "
                    + recoRequest.getM_nIdZonaTarifa() + ", '"
                    + recoRequest.getM_nIdEstadoRemitente() + "', '"
                    + recoRequest.getM_nIdEstadoDestinatario() + "', '"
                    + recoRequest.getM_sMunicipioRemitente() + "', '"
                    + recoRequest.getM_sMunicipioDestinatario() + "'" + ", "
                    + recoRequest.getM_nIdZonaOperativaEntrega() + ", "
                    + recoRequest.getM_nIdZonaTarifaEntrega() + ", '"
                    + recoRequest.getM_nIdEstadoRecoleccion() + "', '"
                    + recoRequest.getM_sCodigoMunicipioRecoleccion() + "', '"
                    + recoRequest.getM_nIdEstadoEntrega() + "', '"
                    + recoRequest.getM_sCodigoMunicipioEntrega() + "', "
                    + recoRequest.getValorDeclarado() + ", "
                    + recoRequest.getM_nIdSucursalEntrega() + ", "
                    + esEntregaSucursal + ", "
                    + recoRequest.getM_nIdTipoSeguro() + ", "
                    + recoRequest.getM_xPorcentajeSeguro() + ", "
                    + aplicaSeguro + ", " + citaPendiente + ", '"
                    + recoRequest.getM_sObservaciones() + "', '"
                    + recoRequest.getM_sReferencia() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs >= 0) {
                    query = "SELECT IDENT_CURRENT( 'ProRecoleccionPQ' ) as id";
                    ResultSet resultSet = statement.executeQuery(query);
                    JSONObject jsonResult = convertObject(resultSet);
                    recoRequest.setM_nIdRecoleccion(jsonResult.getInt("id"));
                    if (recoRequest.getM_parrPaquetes().size() > 0) {
                        paquetesService.agregarPaquetesARecoleccion(recoRequest, statement);
                    }
                    if (recoRequest.getM_arrClsComplementoSAT().size() > 0) {
                        complementosService.agregarComplementosARecoleccion(recoRequest, jdbcConnection);
                    }
                    if (recoRequest.getM_arrConceptos().size() > 0) {
                        conceptosService.agregarConceptosARecoleccion(recoRequest, statement);
                    }
                    return getRecoleccionId(recoRequest.getM_nIdRecoleccion(), rfc);
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al guardar la recolección");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar la recolección");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Recoleccion/Modificar/{idRecoleccion}")
    public ResponseEntity<?> putRecoleccion(@RequestBody RecoleccionRequest recoRequest,
                                            @PathVariable("idRecoleccion") int idRecoleccion,
                                            @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        int esRecoleccionDiferenteDomicilio;
        if (recoRequest.isM_bRecoleccionDiferenteDomicilio()) {
            esRecoleccionDiferenteDomicilio = 1;
        } else {
            esRecoleccionDiferenteDomicilio = 0;
        }
        int esEntregaDiferenteDomicilio;
        if (recoRequest.isM_bEntregaDiferenteDomicilio()) {
            esEntregaDiferenteDomicilio = 1;
        } else {
            esEntregaDiferenteDomicilio = 0;
        }
        int esRecoleccionConCita;
        if (recoRequest.isM_bRecoleccionConCita()) {
            esRecoleccionConCita = 1;
        } else {
            esRecoleccionConCita = 0;
        }
        int esEntregaSucursal;
        if (recoRequest.isM_bEntregaEnSucursal()) {
            esEntregaSucursal = 1;
        } else {
            esEntregaSucursal = 0;
        }
        int aplicaSeguro;
        if (recoRequest.isM_bAplicaSeguro()) {
            aplicaSeguro = 1;
        } else {
            aplicaSeguro = 0;
        }
        int citaPendiente;
        if (recoRequest.isM_bCitaPendiente()) {
            citaPendiente = 1;
        } else {
            citaPendiente = 0;
        }
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdCliente FROM CatClientes WHERE Activo=1 AND IdCliente="
                    + recoRequest.getM_nIdCliente();
            JSONObject cliente = convertObject(jdbcConnection.createStatement().executeQuery(query));
            if (cliente == null) {
                return ResponseEntity.status(500)
                        .body("El cliente no existe o se inactivó, favor de validar en catálogo de clientes.");
            }
            query = "EXEC usp_ProRecoleccionModificarPQ " +
                    recoRequest.getM_nIdSucursal() + ","
                    + recoRequest.getM_nIdEmbarque() + ","
                    + recoRequest.getM_nIdInforme() + ","
                    + recoRequest.getM_nIdGuia() + ",'"
                    + recoRequest.getM_sFecha() + "','"
                    + recoRequest.getM_sHora() + "','"
                    + recoRequest.getM_sFecha() + "','"
                    + recoRequest.getM_sHora() + "',"
                    + recoRequest.getM_nIdEstatusRecoleccion() + ","
                    + recoRequest.getM_nMoneda() + ", "
                    + recoRequest.getM_rTipoCambio() + ","
                    + recoRequest.getM_nIdTipoDeCobro() + ",'"
                    + recoRequest.getM_sNombreRemitente() + "','"
                    + recoRequest.getM_sNombreDestinatario() + "','"
                    + recoRequest.getM_sRFCRemitente() + "','"
                    + recoRequest.getM_sRFCDestinatario() + "','"
                    + recoRequest.getM_sDomicilioRemitente() + "'" + ",'"
                    + recoRequest.getM_sDomicilioDestinatario() + "', "
                    + recoRequest.getM_sIdCodigoPostalRemitente() + ", "
                    + recoRequest.getM_sIdCodigoPostalDestinatario() + ", "
                    + recoRequest.getM_nIdCiudadRemitente() + ","
                    + recoRequest.getM_nIdCiudadDestinatario() + ",'"
                    + recoRequest.getM_sCorreoRemitente() + "','"
                    + recoRequest.getM_sCorreoDestinatario() + "','"
                    + recoRequest.getM_sTelefonoRemitente() + "'" + ",'"
                    + recoRequest.getM_sTelefonoDestinatario() + "', '"
                    + recoRequest.getM_sContactoRemitente() + "', '"
                    + recoRequest.getM_sContactoDestinatario() + "', "
                    + recoRequest.getM_nIdCiudadOrigen() + ", "
                    + recoRequest.getM_nIdCiudadDestino() + ", "
                    + recoRequest.getM_nNoPaquetes() + ", "
                    + recoRequest.getM_nNoSobres() + ", "
                    + recoRequest.getM_nIdOperador() + ", "
                    + recoRequest.getM_nIdUnidad() + ", "
                    + recoRequest.getM_nIdRemolque() + ",'"
                    + recoRequest.getM_dFechaSalida() + "', '"
                    + recoRequest.getM_dFechaLlegada() + "', '"
                    + recoRequest.getM_tHoraSalida() + "', '"
                    + recoRequest.getM_tHoraLlegada() + "'" + ",'"
                    + recoRequest.getM_dFechaDetalleRecoleccion() + "', '"
                    + recoRequest.getM_tHoraDetalleRecoleccion() + "', "
                    + recoRequest.getM_nIdCPDetalleRecoleccion() + ", "
                    + recoRequest.getM_nIdCiudadDetalleRecoleccion() + ", "
                    + recoRequest.getM_nIdZonaDetalleRecoleccion() + ", '"
                    + recoRequest.getM_sDomicilioDetalleRecoleccion() + "', '"
                    + recoRequest.getM_sRecogerEnDetalleRecoleccion() + "', '"
                    + recoRequest.getM_sDatosAdicionalesDetalleRecoleccion() + "'" + ","
                    + recoRequest.getM_nIdCPDetalleEntrega() + ", "
                    + recoRequest.getM_nIdCiudadDetalleEntrega() + ", "
                    + recoRequest.getM_nIdZonaDetalleEntrega() + ", '"
                    + recoRequest.getM_sDomicilioDetalleEntrega() + "', '"
                    + recoRequest.getM_sEntregarEnDetalleEntrega() + "', '"
                    + recoRequest.getM_sDatosAdicionalesDetalleEntrega() + "', "
                    + recoRequest.getM_nIdRecoleccion() + ", '"
                    + recoRequest.getM_sFolioRecoleccion() + "', "
                    + recoRequest.getM_nIdCliente() + ", "
                    + esRecoleccionDiferenteDomicilio + ", "
                    + esEntregaDiferenteDomicilio + ","
                    + recoRequest.getM_nIdZonaRemitente() + ", "
                    + recoRequest.getM_nIdZonaDestinatario() + ", "
                    + recoRequest.getM_nIdRemitente() + ", "
                    + recoRequest.getM_nIdDestinatario() + ", '"
                    + recoRequest.getM_sAliasRemitente() + "', '"
                    + recoRequest.getM_sAliasDestinatario() + "'" + ","
                    + esRecoleccionConCita + ", '"
                    + recoRequest.getM_sFechaCita() + "', '"
                    + recoRequest.getM_sHoraCitaMinima() + "', '"
                    + recoRequest.getM_sHoraCitaMaxima() + "'" + ",'"
                    + recoRequest.getM_sCalleRemitente() + "', '"
                    + recoRequest.getM_sNoIntRemitente() + "', '"
                    + recoRequest.getM_sNoExtRemitente() + "', '"
                    + recoRequest.getM_sColoniaRemitente() + "'," + "'"
                    + recoRequest.getM_sCalleDestinatario() + "', '"
                    + recoRequest.getM_sNoIntDestinatario() + "', '"
                    + recoRequest.getM_sNoExtDestinatario() + "', '"
                    + recoRequest.getM_sColoniaDestinatario() + "', '"
                    + recoRequest.getM_sLatitudR() + "', '"
                    + recoRequest.getM_sLongitudR() + "'" + ","
                    + recoRequest.getM_nIdZonaOperativa() + ", "
                    + recoRequest.getM_nIdZonaTarifa() + ", '"
                    + recoRequest.getM_nIdEstadoRemitente() + "', '"
                    + recoRequest.getM_nIdEstadoDestinatario() + "', '"
                    + recoRequest.getM_sMunicipioRemitente() + "', '"
                    + recoRequest.getM_sMunicipioDestinatario() + "'" + ", "
                    + recoRequest.getM_nIdZonaOperativaEntrega() + ", "
                    + recoRequest.getM_nIdZonaTarifaEntrega() + ", '"
                    + recoRequest.getM_nIdEstadoRecoleccion() + "', '"
                    + recoRequest.getM_sCodigoMunicipioRecoleccion() + "', '"
                    + recoRequest.getM_nIdEstadoEntrega() + "', '"
                    + recoRequest.getM_sCodigoMunicipioEntrega() + "', "
                    + recoRequest.getValorDeclarado() + ", "
                    + recoRequest.getM_nIdSucursalEntrega() + ", "
                    + esEntregaSucursal + ", "
                    + recoRequest.getM_nIdTipoSeguro() + ", "
                    + recoRequest.getM_xPorcentajeSeguro() + ", "
                    + aplicaSeguro + ", "
                    + citaPendiente + ", '"
                    + recoRequest.getM_sObservaciones() + "', '"
                    + recoRequest.getM_sReferencia() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs >= 0) {
                    if (recoRequest.getM_parrPaquetes().size() > 0) {
                        paquetesService.modificarPaquetesRecoleccion(recoRequest, statement);
                    }
                    if (recoRequest.getM_arrClsComplementoSAT().size() > 0) {
                        complementosService.modificarComplementosRecoleccion(recoRequest, jdbcConnection);
                    }
                    if (recoRequest.getM_arrConceptos().size() > 0) {
                        conceptosService.agregarConceptosARecoleccion(recoRequest, statement);
                    }
                    return ResponseEntity.ok("Se guardó la recolección correctamente");
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al guardar la recolección");
                }
            } catch (Exception e) {
                e.printStackTrace();

                return ResponseEntity.status(500).body("Ocurrió un problema al guardar la recolección");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Recoleccion/Cancelar/{idRec}")
    public ResponseEntity<?> cancelarRecoleccion(@RequestBody RecoleccionCancelarRequest cancelado,
                                                 @PathVariable("idRec") int idRec,
                                                 @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        if (idRec <= 0) {
            return ResponseEntity.status(500).body("Identificador de recoleccion es un campo requerido");
        }
        if (cancelado.getMotivoCancelacion() == "") {
            return ResponseEntity.status(500).body("El motivo de cancelacion es un campo requerido");
        }
        if (cancelado.getUsuarioCancelacion() <= 0) {
            return ResponseEntity.status(500).body("Usuario que lo cancela es un campo requerido");
        }
        if (cancelado.getFechaCancelacion() == "") {
            return ResponseEntity.status(500).body("La fecha de cancelacion es un campo requerido");
        }

        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProRecoleccionCancelarPQ  " + idRec + ", '"
                        + cancelado.getMotivoCancelacion() + "', '" + cancelado.getFechaCancelacion() + "', "
                        + cancelado.getUsuarioCancelacion() + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al cancelar la recolección");
        }

        return ResponseEntity.ok("Cancelado exitoso");
    }

    @PutMapping("/Recoleccion/ActualizarCoordenadas/{idRec}/{lat}/{lon}")
    public ResponseEntity<?> actualizarCoordenadas(@PathVariable("idRec") int idRec,
                                                   @PathVariable("lat") String lat,
                                                   @PathVariable("lon") String lon,
                                                   @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "update ProRecoleccionPQ SET Latitud = " + lat + ", Longitud = " + lon
                        + "  WHERE IdRecoleccion = " + idRec;
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurió un problema al actualizar la información");
        }
        return ResponseEntity.ok("Cambio realizado exitosamente");
    }

    @DeleteMapping("/Recoleccion/Eliminar/{idRec}/{idUsuario}")
    public ResponseEntity<?> eliminarRecoleccion(@PathVariable("idRec") int idRec,
                                                 @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProRecoleccionEliminarPQ " + idRec + "";
                String query2 = "select IdEstatusRecoleccion from ProRecoleccionPQ where IdRecoleccion = " + idRec + "";
                Statement statement2 = jdbcConnection.createStatement();
                ResultSet rs = statement2.executeQuery(query2);

                JSONObject jsonObject = convertObject(rs);
                if (jsonObject.getInt("IdEstatusRecoleccion") != 5) {
                    return ResponseEntity.status(500).body("Para eliminar debe estar cancelado");
                }
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar");
        }

        return ResponseEntity.ok("Eliminado exitoso");
    }

    @GetMapping("/Recoleccion/GetCancelarById/{idRec}")
    public ResponseEntity<?> cancelarPorIdRecoleccion(@PathVariable("idRec") int idRec,
                                                      @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        JSONObject jsonObject1 = new JSONObject();
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "select IdEstatusRecoleccion,ISNULL(IdEmbarque,0) AS IdEmbarque " +
                        "from ProRecoleccionPQ where IdRecoleccion = " + idRec + "";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = convertObject(rs);
                int idEmbarque = jsonObject.getInt("IdEmbarque");
                int idEstatus = jsonObject.getInt("IdEstatusRecoleccion");
                boolean sePuedeCancelar = false;
                if (idRec != 0) {
                    if (idEmbarque == 0 && (idEstatus == 1) || idEstatus == 2) {
                        sePuedeCancelar = true;
                    } else {
                        sePuedeCancelar = false;
                    }
                    jsonObject1.put("sePuedeCancelar", sePuedeCancelar);
                } else {
                    return ResponseEntity.status(500).body("Recoleccion no encontrada");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al validar la recoleccion");
        }
        return ResponseEntity.ok(jsonObject1.toString());
    }


    @PutMapping("/Recoleccion/ModificarSAT")
    public ResponseEntity<?> modificarRecoleccionSAT(@RequestBody RecoleccionRequest recoRequest,
                                                     @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        int esRecoleccionDiferenteDomicilio;
        if (recoRequest.isM_bRecoleccionDiferenteDomicilio()) {
            esRecoleccionDiferenteDomicilio = 1;
        } else {
            esRecoleccionDiferenteDomicilio = 0;
        }

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProRecoleccionModificarSATPQ " +
                    recoRequest.getM_nIdTipoDeCobro() + ", "
                    + recoRequest.getM_nIdCiudadOrigen() + ", "
                    + recoRequest.getM_nIdCPDetalleRecoleccion() + ", '"
                    + recoRequest.getM_sDomicilioDetalleRecoleccion() + "', '"
                    + recoRequest.getM_sRecogerEnDetalleRecoleccion() + "', '"
                    + recoRequest.getM_sDatosAdicionalesDetalleRecoleccion() + "', "
                    + recoRequest.getM_nIdRecoleccion() + ", "
                    + recoRequest.getM_nIdCliente() + ", "
                    + esRecoleccionDiferenteDomicilio + ","
                    + recoRequest.getM_nIdZonaOperativa() + ", "
                    + recoRequest.getM_nIdRemitente() + ", "
                    + recoRequest.getM_nIdZonaOperativa() + ", '"
                    + recoRequest.getM_nIdEstadoRecoleccion() + "', '"
                    + recoRequest.getM_sCodigoMunicipioRecoleccion() + "', "
                    + recoRequest.getValorDeclarado() + ", "
                    + recoRequest.getM_nIdTipoSeguro() + ", "
                    + recoRequest.getM_xPorcentajeSeguro() + ", '"
                    + recoRequest.getM_sObservaciones() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs >= 0) {
                    if (recoRequest.getM_parrPaquetes().size() > 0) {
                        paquetesService.modificarPaquetesRecoleccion(recoRequest, statement);
                    }
                    if (recoRequest.getM_arrClsComplementoSAT().size() > 0) {
                        complementosService.modificarComplementosRecoleccion(recoRequest, jdbcConnection);
                    }
                    if (recoRequest.getM_arrConceptos().size() > 0) {
                        conceptosService.agregarConceptosARecoleccion(recoRequest, statement);
                    }

                    return ResponseEntity.ok("Se guardó la recolección correctamente");
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al guardar la recolección");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar la recolección");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
