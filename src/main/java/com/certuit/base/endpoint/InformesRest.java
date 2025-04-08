package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.InformesService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.apache.commons.lang.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class InformesRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    InformesService informesService;

    @GetMapping("/Informes/GetByFiltro/{sFechaInicial}/{sFechaFinal}/{folio}/{sSucursalEmisora}/{sSucursalReceptora}")
    public ResponseEntity<?> getInformesFiltro(@PathVariable("sFechaInicial") String fechaInicial,
                                               @PathVariable("sFechaFinal") String fechaFinal,
                                               @PathVariable("folio") String folio,
                                               @PathVariable("sSucursalEmisora") int origen,
                                               @PathVariable("sSucursalReceptora") int destino,
                                               @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            fechaInicial = !fechaInicial.equalsIgnoreCase("0") ? "'" + fechaInicial + "'" : null;
            fechaFinal = !fechaFinal.equalsIgnoreCase("0") ? "'" + fechaFinal + "'" : null;
            folio = !folio.equalsIgnoreCase("0") ? folio : null;
            if (folio != null) {
                folio = folio.toUpperCase();
            }
            String query = "SELECT " +
                    "    cin.IdInforme as m_nIdInforme, " +
                    "    cin.FolioInforme as m_sFolioInforme, " +
                    "    FORMAT(cin.Fecha, 'dd-MM-yyyy') + ' ' +  " +
                    "convert(char(5),convert(time(0),cin.Hora)) as m_sFechaHora," +
                    "    cin.Fecha as m_dFecha, " +
                    "    cin.Hora as m_tHora, " +
                    "    cin.IdEstatusInforme as m_nIdEstatusInforme, " +
                    "    ev.Estatus as m_sEstatusInforme, " +
                    "    cin.IdViaje as m_nIdViaje, " +
                    "    cin.IdRuta as m_nIdRuta, " +
                    "    (select FolioViaje from ProViajesPQ v " +
                    "where cin.IdViaje = v.IdViaje) as m_sFolioViaje, " +
                    "    cin.IdSucursalEmisora as m_nIdSucursalEmisora , " +
                    "    (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalEmisora = cs.IdSucursal) as m_sSucursalEmisora, " +
                    "    cin.IdSucursalReceptora as m_nIdSucursalReceptora, " +
                    " " +
                    "    (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalReceptora = cs.IdSucursal) as m_sSucursalReceptora, " +
                    "    cin.IdOperador as m_nIdOperador, " +
                    " " +
                    "    (select (co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno) as NombreCompleto " +
                    "from CatOperadores co where cin.IdOperador = co.IdOperador) as m_sNombreCompleto, " +
                    "    (select NumeroOperador from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador) as m_sNumeroOperador, " +
                    "    (select Descripcion from CatUnidades cu " +
                    "where cin.IdDolly = cu.IdUnidad) as Dolly, " +
                    "    (select Codigo from CatUnidades cu " +
                    "where cin.IdRemolque1 = cu.IdUnidad) as m_nIdentificador, " +
                    "    cin.IdRemolque1 as m_nIdRemolque1, " +
                    "    cin.IdRemolque2 as m_nIdRemolque2, " +
                    "    cin.IdDolly as m_nIdDolly, " +
                    " " +
                    "    (select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_sRemolque1, " +
                    "    (select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as m_sRemolque2, " +
                    "    cin.PlacasRemolque1 as m_sPlacasRemolque1, " +
                    "    cin.PlacasRemolque2 as m_sPlacasRemolque2, " +
                    "    cin.PlacasDolly as m_sDolly, " +
                    " " +
                    "    (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadOrigen = cc.IdOrigenDestino) as m_sCiudadOrigen, " +
                    "    (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadDestino = cc.IdOrigenDestino) as m_sCiudadDestino, " +
                    "    ISNULL((select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdUbicacionActual = cc.IdOrigenDestino),'') as m_sUbicacionActual, " +
                    "    (select Descripcion from CatDetalleRutasPQ cr " +
                    "where cin.IdRuta = cr.IdDetalleRuta) as m_sRuta, " +
                    "    cin.IdCiudadDestino as m_nIdCiudadDestino, " +
                    "    cin.IdCiudadOrigen as m_nIdCiudadOrigen, " +
                    "    cin.FechaCancelacion as m_dtFechaCancelacion, " +
                    "    cin.UsuarioCancelacion as m_nIdUsuarioCancelacion, " +
                    "    cus.Nombre as m_sNombre, " +
                    "    ev.Color as m_sColorEstatus " +
                    "FROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario  " +
                    "inner join CatEstatusInformePQ ev on cin.IdEstatusInforme= ev.IdEstatusInforme " +
                    "where  (FolioInforme LIKE '%" + folio + "%') OR " +
                    "  ((Fecha >= " + fechaInicial + " OR " + fechaInicial + " IS NULL) " +
                    "  AND (Fecha <= " + fechaFinal + " OR " + fechaFinal + " IS NULL) " +
                    "  AND (cin.IdSucursalEmisora = " + origen + " OR " + origen + " = 0) " +
                    "  AND (cin.IdSucursalReceptora = " + destino + " OR " + destino + " = 0) AND " + "'" + folio +
                    "'" + " = 'null' )" + "ORDER BY Fecha DESC, Hora DESC";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetListadoEscaner")
    public ResponseEntity<?> getInformesEscaner(
            @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "    cin.IdInforme as m_nIdInforme, " +
                    "    cin.FolioInforme as m_sFolioInforme, " +
                    "    cin.Fecha as m_dFecha, " +
                    "    cin.Hora as m_tHora, " +
                    "    cin.IdEstatusInforme as m_nIdEstatusInforme, " +
                    "    cin.IdViaje as m_nIdViaje, " +
                    "    cin.IdRuta as m_nIdRuta, " +
                    "    (select FolioViaje from ProViajesPQ v where cin.IdViaje = v.IdViaje) as m_sFolioViaje, " +
                    "    cin.IdSucursalEmisora as m_nIdSucursalEmisora , " +
                    "    (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalEmisora = cs.IdSucursal) as m_sSucursalEmisora, " +
                    "    cin.IdSucursalReceptora as m_nIdSucursalReceptora, " +
                    " " +
                    "    (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalReceptora = cs.IdSucursal) as m_sSucursalReceptora, " +
                    "    cin.IdOperador as m_nIdOperador, " +
                    " " +
                    "    (select (co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno) as NombreCompleto" +
                    " from CatOperadores co where cin.IdOperador = co.IdOperador) as m_sNombreCompleto, " +
                    "    (select NumeroOperador from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador) as m_sNumeroOperador, " +
                    "    (select Descripcion from CatUnidades cu " +
                    "where cin.IdDolly = cu.IdUnidad) as Dolly, " +
                    "    (select Codigo from CatUnidades cu " +
                    "where cin.IdRemolque1 = cu.IdUnidad) as m_nIdentificador, " +
                    "    cin.IdRemolque1 as m_nIdRemolque1, " +
                    "    cin.IdRemolque2 as m_nIdRemolque2, " +
                    "    cin.IdDolly as m_nIdDolly, " +
                    " " +
                    "    (select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_sRemolque1, " +
                    "    (select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as m_sRemolque2, " +
                    "    cin.PlacasRemolque1 as m_sPlacasRemolque1, " +
                    "    cin.PlacasRemolque2 as m_sPlacasRemolque2, " +
                    "    cin.PlacasDolly as m_sDolly, " +
                    " " +
                    "    (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadOrigen = cc.IdOrigenDestino) as m_sCiudadOrigen, " +
                    "    (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadDestino = cc.IdOrigenDestino) as m_sCiudadDestino, " +
                    "    (select Descripcion from CatDetalleRutasPQ cr " +
                    "where cin.IdRuta = cr.IdDetalleRuta) as m_sRuta, " +
                    "    cin.IdCiudadDestino as m_nIdCiudadDestino, " +
                    "    cin.IdCiudadOrigen as m_nIdCiudadOrigen, " +
                    "    cin.FechaCancelacion as m_dtFechaCancelacion, " +
                    "    cin.UsuarioCancelacion as m_nIdUsuarioCancelacion, " +
                    "    cus.Nombre as m_sNombre " +
                    "FROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario " +
                    "where cin.IdEstatusInforme = 5 order by  cin.Fecha desc";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonArray = informesService.convertInformes(rs, jdbcConnection);

            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetListadoEscaner/ByOperador/{idOperador}")
    public ResponseEntity<?> getInformesEscanerByOperador(@RequestHeader("RFC") String rfc,
                                                          @PathVariable("idOperador") int idOperador)
            throws Exception
    {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT  cin.IdInforme AS m_nIdInforme,\n" +
                    "        cin.FolioInforme AS m_sFolioInforme,\n" +
                    "        cin.Fecha AS m_dFecha,\n" +
                    "        cin.Hora AS m_tHora,\n" +
                    "        cin.IdEstatusInforme AS m_nIdEstatusInforme,\n" +
                    "        cin.IdViaje AS m_nIdViaje,\n" +
                    "        cin.IdRuta AS m_nIdRuta,\n" +
                    "        (SELECT FolioViaje from ProViajesPQ v " +
                    "WHERE cin.IdViaje = v.IdViaje) AS m_sFolioViaje,\n" +
                    "        cin.IdSucursalEmisora AS m_nIdSucursalEmisora,\n" +
                    "        (SELECT Sucursal from CatSucursales cs " +
                    "WHERE cin.IdSucursalEmisora = cs.IdSucursal) AS m_sSucursalEmisora,\n" +
                    "        cin.IdSucursalReceptora AS m_nIdSucursalReceptora,\n" +
                    "        (SELECT Sucursal from CatSucursales cs " +
                    "WHERE cin.IdSucursalReceptora = cs.IdSucursal) AS m_sSucursalReceptora,\n" +
                    "        cin.IdOperador AS m_nIdOperador,\n" +
                    "        CONCAT(co.Nombres,' ',co.ApellidoPaterno,' ',co.ApellidoMaterno) AS m_sNombreCompleto,\n" +
                    "        co.NumeroOperador AS m_sNumeroOperador,\n" +
                    "        (SELECT Descripcion from CatUnidades cu " +
                    "WHERE cin.IdDolly = cu.IdUnidad) AS Dolly,\n" +
                    "        (SELECT Descripcion from CatUnidades cu " +
                    "WHERE cin.IdRemolque1 = cu.IdUnidad) AS m_sRemolque1,\n" +
                    "        (SELECT Descripcion from CatUnidades cu " +
                    "WHERE cin.IdRemolque2 = cu.IdUnidad) AS m_sRemolque2,\n" +
                    "        (SELECT Codigo from CatUnidades cu " +
                    "WHERE cin.IdRemolque1 = cu.IdUnidad) AS m_nIdentificador,\n" +
                    "        cin.IdRemolque1 AS m_nIdRemolque1,\n" +
                    "        cin.IdRemolque2 AS m_nIdRemolque2,\n" +
                    "        cin.IdDolly AS m_nIdDolly,\n" +
                    "        cin.PlacasRemolque1 AS m_sPlacasRemolque1,\n" +
                    "        cin.PlacasRemolque2 AS m_sPlacasRemolque2,\n" +
                    "        cin.PlacasDolly AS m_sDolly,\n" +
                    "        (SELECT OrigenDestino from CatOrigenesDestinos cc " +
                    "WHERE cin.IdCiudadOrigen = cc.IdOrigenDestino) AS m_sCiudadOrigen,\n" +
                    "        (SELECT OrigenDestino from CatOrigenesDestinos cc " +
                    "WHERE cin.IdCiudadDestino = cc.IdOrigenDestino) AS m_sCiudadDestino,\n" +
                    "        (SELECT Descripcion from CatDetalleRutasPQ cr " +
                    "WHERE cin.IdRuta = cr.IdDetalleRuta) AS m_sRuta,\n" +
                    "        cin.IdCiudadDestino AS m_nIdCiudadDestino,\n" +
                    "        cin.IdCiudadOrigen AS m_nIdCiudadOrigen,\n" +
                    "        cin.FechaCancelacion AS m_dtFechaCancelacion,\n" +
                    "        cin.UsuarioCancelacion AS m_nIdUsuarioCancelacion,\n" +
                    "        cus.Nombre AS m_sNombre\n" +
                    "FROM ProInformePQ cin\n" +
                    "    LEFT JOIN CatUsuarios cus ON cin.UsuarioCancelacion = cus.IdUsuario\n" +
                    "    LEFT JOIN CatOperadores co ON cin.IdOperador = co.IdOperador\n" +
                    "WHERE cin.IdEstatusInforme = 5 \n" +//AND (co.NumeroOperador = " + idOperador + ")\n" + //OR cin.IdOperador = 0
                    "ORDER BY cin.FolioInforme DESC";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonArray = informesService.convertInformes(rs, jdbcConnection);
            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetListadoSinViajes")
    public ResponseEntity<?> getInformesFiltro(
            @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "    cin.IdInforme as m_nIdInforme, " +
                    "    cin.FolioInforme as m_sFolioInforme, " +
                    "    cin.Fecha as m_dFecha, " +
                    "    cin.Hora as m_tHora, " +
                    "    cin.IdEstatusInforme as m_nIdEstatusInforme, " +
                    "    cin.IdViaje as m_nIdViaje, " +
                    "    cin.IdRuta as m_nIdRuta, " +
                    "    (select FolioViaje from ProViajesPQ v " +
                    "where cin.IdViaje = v.IdViaje) as m_sFolioViaje, " +
                    "    cin.IdSucursalEmisora as m_nIdSucursalEmisora , " +
                    "    (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalEmisora = cs.IdSucursal) as m_sSucursalEmisora, " +
                    "    cin.IdSucursalReceptora as m_nIdSucursalReceptora, " +
                    " " +
                    "    (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalReceptora = cs.IdSucursal) as m_sSucursalReceptora, " +
                    "    cin.IdOperador as m_nIdOperador, " +
                    " " +
                    "    (select (co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno) as NombreCompleto" +
                    " from CatOperadores co where cin.IdOperador = co.IdOperador) as m_sNombreCompleto, " +
                    "    (select NumeroOperador from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador) as m_sNumeroOperador, " +
                    "    (select Descripcion from CatUnidades cu where cin.IdDolly = cu.IdUnidad) as Dolly, " +
                    "    (select Codigo from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_nIdentificador, " +
                    "    cin.IdRemolque1 as m_nIdRemolque1, " +
                    "    cin.IdRemolque2 as m_nIdRemolque2, " +
                    "    cin.IdDolly as m_nIdDolly, " +
                    " " +
                    "    (select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_sRemolque1, " +
                    "    (select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as m_sRemolque2, " +
                    "    cin.PlacasRemolque1 as m_sPlacasRemolque1, " +
                    "    cin.PlacasRemolque2 as m_sPlacasRemolque2, " +
                    "    cin.PlacasDolly as m_sDolly, " +
                    " " +
                    "    (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadOrigen = cc.IdOrigenDestino) as m_sCiudadOrigen, " +
                    "    (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadDestino = cc.IdOrigenDestino) as m_sCiudadDestino, " +
                    "    (select Descripcion from CatDetalleRutasPQ cr " +
                    "where cin.IdRuta = cr.IdDetalleRuta) as m_sRuta, " +
                    "    cin.IdCiudadDestino as m_nIdCiudadDestino, " +
                    "    cin.IdCiudadOrigen as m_nIdCiudadOrigen, " +
                    "    cin.FechaCancelacion as m_dtFechaCancelacion, " +
                    "    cin.UsuarioCancelacion as m_nIdUsuarioCancelacion, " +
                    "    cus.Nombre as m_sNombre " +
                    "FROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario  ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonArray = informesService.convertInformes(rs, jdbcConnection);

            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Informes/GetListadoDisponiblesViaje")
    public ResponseEntity<?> getInformesViajes(
            @RequestHeader("RFC") String rfc, @RequestBody @Valid InformesRequets requets) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    " cin.IdInforme as m_nIdInforme,\n" +
                    " cin.FolioInforme as m_sFolioInforme,\n" +
                    " cin.Fecha as m_dFecha,\n" +
                    " FORMAT(cin.Fecha, 'dd-MM-yyyy') + ' ' +  " +
                    "convert(char(5),convert(time(0),cin.Hora)) as m_sFechaHora,\n" +
                    " cin.Hora as m_tHora,\n" +
                    " cin.IdEstatusInforme as m_nIdEstatusInforme,\n" +
                    " cin.IdViaje as m_nIdViaje,\n" +
                    " cin.IdCiudadDestino as m_nIdDestino,\n" +
                    " cin.IdRuta as m_nIdRuta,\n" +
                    " (select FolioViaje from ProViajesPQ v where cin.IdViaje = v.IdViaje) as m_sFolioViaje,\n" +
                    "  cin.IdSucursalEmisora as m_nIdSucursalEmisora,\n" +
                    " (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalEmisora = cs.IdSucursal) as m_sSucursalEmisora,\n" +
                    " cin.IdSucursalReceptora as m_nIdSucursalReceptora,\n" +
                    "\n" +
                    " (select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalReceptora = cs.IdSucursal) as m_sSucursalReceptora,\n" +
                    "  cin.IdOperador as m_nIdOperador,\n" +
                    "\n" +
                    " (select (co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno) as NombreCompleto " +
                    "from CatOperadores co where cin.IdOperador = co.IdOperador) as m_sNombreCompleto,\n" +
                    " (select NumeroOperador from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador) as m_sNumeroOperador,\n" +
                    " (select Descripcion from CatUnidades cu where cin.IdDolly = cu.IdUnidad) as m_sDolly,\n" +
                    " (select Codigo from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_nIdentificador,\n" +
                    "  cin.IdRemolque1 as m_nIdRemolque1,\n" +
                    "  cin.IdRemolque2 as m_nIdRemolque2,\n" +
                    "  cin.IdDolly as m_nIdDolly,\n" +
                    " (select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as m_sRemolque1,\n" +
                    " (select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as m_sRemolque2,\n" +
                    " cin.PlacasRemolque1 as m_sPlacasRemolque1,\n" +
                    " cin.PlacasRemolque2 as m_sPlacasRemolque2,\n" +
                    " cin.PlacasDolly as m_sPlacasDolly,\n" +
                    " (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdUbicacionActual = cc.IdOrigenDestino) as m_sCiudadOrigen,\n" +
                    " (select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadDestino = cc.IdOrigenDestino) as m_sCiudadDestino,\n" +
                    " (select Descripcion from CatDetalleRutasPQ cr where cin.IdRuta = cr.IdDetalleRuta) as m_sRuta,\n" +
                    " cin.FechaCancelacion as m_dtFechaCancelacion,\n" +
                    " cin.UsuarioCancelacion as m_nIdUsuarioCancelacion, \n" +
                    " cus.Nombre as m_sNombre,\n" +
                    " ((select CONVERT(varchar(10), NumeroOperador) from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador) + ' ' + (select (co.Nombres + ' ' " +
                    "+ co.ApellidoPaterno + ' ' + co.ApellidoMaterno) as NombreCompleto from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador)) as m_sNumeroNombreOperador,\n" +
                    " ((select Codigo from CatUnidades cu " +
                    "where cin.IdRemolque1 = cu.IdUnidad) + ' ' + (select Descripcion from CatUnidades cu " +
                    "where cin.IdDolly = cu.IdUnidad)) as m_sUnidadIdentificador\n" +
                    " FROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario \n" +
                    "WHERE cin.IdUbicacionActual = " + requets.getIdOrigen() + " and cin.IdCiudadDestino = "
                    + requets.getIdDestino() +
                    " and cin.IdViaje <= 0 and (cin.IdEstatusInforme = 5 or cin.IdEstatusInforme = 7)  and " +
                    "(SELECT COUNT(*) FROM ProGuiaPQ guia WHERE guia.IdInforme= cin.IdInforme) > 0";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonArray = informesService.convertInformes(rs, jdbcConnection);

            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetById/{id}")
    public ResponseEntity<?> getInformesId(@PathVariable("id") int id,
                                           @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                JSONObject jsonObject = informesService.getInformeId(id, jdbcConnection);
                return ResponseEntity.ok(jsonObject.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ResponseEntity.status(500).body("");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetById/Escaner/{id}")
    public ResponseEntity<?> getInformesIdEscaner(@PathVariable("id") int id,
                                                  @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            JSONObject jsonObject = informesService.getInformeIdEscaner(id, jdbcConnection);
            if (jsonObject != null) {
                return ResponseEntity.ok(jsonObject.toString());
            } else {
                return ResponseEntity.status(500).body("No se encontro la información, asegúrese que el informe se encuentre en ruta");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Informes/AgregarLlegada/{id}")
    public ResponseEntity<?> agregarLlegada(@PathVariable("id") int id, @RequestBody ViajeLlegadaRequest llegada,
                                            @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "UPDATE ProInformePQ SET FechaLLegada = '" + llegada.getM_dFechaLlegada()
                        + "', HoraLlegada = '" + llegada.getM_tHoraLlegada()
                        + "', IdEstatusInforme = 8 where IdInforme =" + id;
                Statement statement = jdbcConnection.createStatement();
                int estado = statement.executeUpdate(query);

                query = "UPDATE ProGuiaPQ  SET IdEstatusGuia = (case when (select pe.EntregaEnSucursal " +
                        "from ProEmbarquePQ pe where pe.IdEmbarque = pg.IdEmbarque) = 1 then 7 else 14 end) " +
                        "from ProGuiaPQ pg where IdInforme = " + id;
                statement = jdbcConnection.createStatement();
                estado = statement.executeUpdate(query);

                query = "UPDATE ProEmbarquePQ SET IdEstatusEmbarque = 20, FechaLlegada = '"
                        + llegada.getM_dFechaLlegada() + "' where IdInforme =" + id;
                statement = jdbcConnection.createStatement();
                estado = statement.executeUpdate(query);
                

                return ResponseEntity.ok("Guardado Exitosamente");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("No se encontro la información, " +
                        "asegúrese que el informe se encuentre en ruta");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                    "Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetById/EscanerValidar/{id}")
    public ResponseEntity<?> getInformesIdEscanerValidar(@PathVariable("id") int id,
                                                         @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            JSONObject jsonObject = informesService.getInformeIdEscanerValidar(id, jdbcConnection);

            if (jsonObject != null) {
                return ResponseEntity.ok(jsonObject.toString());
            } else {
                return ResponseEntity.status(500).body("No se encontro la información, asegúrese que el" +
                        " informe se encuentre en ruta");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetPardasIdViaje/{id}")
    public ResponseEntity<?> getInformesParadasViaje(@PathVariable("id") int id,
                                                     @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select i.IdInforme                                                                                         as m_nIdInforme,\n" +
                    "       i.FolioInforme                                                                                      as m_sFolioInforme,\n" +
                    "       (select OrigenDestino\n" +
                    "        from CatOrigenesDestinos cc\n" +
                    "        where i.IdCiudadOrigen = cc.IdOrigenDestino)                                                       as m_sCiudadOrigen,\n" +
                    "       (select OrigenDestino\n" +
                    "        from CatOrigenesDestinos cc\n" +
                    "        where cc.IdOrigenDestino = py.IdUbicacionDestino)                                                  as m_sCiudadDestino,\n" +
                    "       CONCAT(operador.Nombres, ' ', operador.ApellidoPaterno, ' ',\n" +
                    "              operador.ApellidoMaterno)                                                                    as m_sNombreCompleto\n" +
                    "        ,\n" +
                    "       (Select cu.Codigo + ' ' + cu.Descripcion from CatUnidades cu where cu.IdUnidad = i.IdUnidad)        as m_sCamion,\n" +
                    "       i.IdRuta                                                                                            as m_nIdRuta,\n" +
                    "       (Select (Nombres + ' ' + ApellidoPaterno + ' ' + ApellidoMaterno) as NombreCompleto\n" +
                    "        from CatOperadores\n" +
                    "        where IdOperador = i.IdOperador)                                                                   as m_sOperador,\n" +
                    "       i.IdInforme                                                                                         as m_nIdInforme,\n" +
                    "       (select OrigenDestino from CatOrigenesDestinos c where c.IdOrigenDestino = i.IdCiudadOrigen)        as m_sOrigen,\n" +
                    "       i.IdCiudadOrigen                                                                                    as m_nIdOrigen,\n" +
                    "       (select OrigenDestino\n" +
                    "        from CatOrigenesDestinos c\n" +
                    "        where c.IdOrigenDestino = py.IdUbicacionDestino)                                                   as m_sDestino,\n" +
                    "       py.IdUbicacionDestino                                                                               as m_nIdDestino,\n" +
                    "       FORMAT(i.FechaSalida, 'yyyy-MM-dd')                                                                 as m_dFechaSalida,\n" +
                    "       FORMAT(i.FechaSalida, 'dd-MM-yyyy')                                                                 as m_sFechaSalidaFormat,\n" +
                    "       i.HoraSalida                                                                                        as m_tHoraSalida,\n" +
                    "       FORMAT(i.FechaLLegada, 'yyyy-MM-dd')                                                                as m_dFechaLlegada,\n" +
                    "       FORMAT(i.FechaLLegada, 'dd-MM-yyyy')                                                                as m_sFechaLlegadaFormat,\n" +
                    "       i.HoraLlegada                                                                                       as m_tHoraLlegada,\n" +
                    "       i.IdViaje                                                                                           as m_nIdViaje,\n" +
                    "       ''                                                                                                  as m_sRuta,\n" +
                    "       ISNULL(py.FolioFiscalUUID, '')                                                                       as m_sFolioFiscalUUID,\n" +
                    "       ISNULL(py.Timbrado, 0)                                                                               as m_bTimbrado,\n" +
                    "       ISNULL(py.XMLTraslada, '')                                                                           as m_sXMLTraslada,\n" +
                    "       ISNULL(py.FolioFiscalUUIDSustituido, '')                                                             as m_sFolioFiscalUUIDSustituido,\n" +
                    "       ISNULL(py.UltimoFolioFiscalUUID, '')                                                                 as m_sUltimoFolioFiscalUUIDSustituido,\n" +
                    "       ISNULL(py.MotivoCancelacionTimbrado, '')                                                             as m_sMotivoCancelacionTimbrado,\n" +
                    "       ISNULL(py.MotivoCancelacionSAT, '')                                                                  as m_sMotivoCancelacionSAT,\n" +
                    "       py.IdDetalleParada                                                                  as m_nIdParada\n" +
                    "from ProInformePQ i\n" +
                    "         inner join ProViajeDetalleParadasPQ py on py.IdInforme = i.IdInforme\n" +
                    "         left join CatOperadores operador on operador.IdOperador = i.IdOperador\n" +
                    "where i.IdInforme in (select dp.IdInforme from ProViajeDetalleParadasPQ dp where py.IdViaje =" + id + ") ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            JSONArray jsonArray = informesService.convertInformes(rs, jdbcConnection);

            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Informes/ValidarQR/{id}")
    public ResponseEntity<?> ValidarQR(@PathVariable("id") int id,
                                       @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "update ProInformePQ \n" +
                    "set Escaneado = 1\n" +
                    "where IdInforme = " + id;

            Statement statement = jdbcConnection.createStatement();
            boolean estado = statement.execute(query);

            return ResponseEntity.ok("Se valido correctamente el informe.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Informes/Agregar")
    public ResponseEntity<?> agregarInforme(@RequestBody InformeRequest informe,
                                            @RequestHeader("RFC") String rfc) throws Exception {

        if (informe.getM_dFecha().isEmpty()) {
            return ResponseEntity.status(500).body("La fecha  es un campo requerido");
        }
        if (informe.getM_tHora().isEmpty()) {
            return ResponseEntity.status(500).body("La hora es un campo requerido");
        }

        int id = 0;
        String folio = "";
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProInformeAgregarPQ 0,'" + informe.getM_dFecha() + "','"
                        + informe.getM_tHora() + "'," + informe.getM_nIdEstatusInforme() + "," + informe.getM_nIdViaje() + ","
                        + informe.getM_nIdSucursalEmisora() + "," + informe.getM_nIdSucursalReceptora() + "," + informe.getM_nIdOperador() + ","
                        + informe.getM_nIdDolly() + "," + informe.getM_nIdRemolque1() + "," + informe.getM_nIdRemolque2() + ",'" + informe.getM_sPlacasRemolque1() + "','"
                        + informe.getM_sPlacasRemolque2() + "','" + informe.getM_sPlacasDolly() + "'," + informe.getM_nIdCiudadOrigen() + "," + informe.getM_nIdCiudadDestino() + "," + informe.getM_nIdRuta() + ",''," + informe.getM_nTotalxCDestinatario() + ","
                        + informe.getM_nTotalxCCobrarRemitente() + "," + informe.getM_nTotalPagoMostrador() + ","
                        + informe.getM_nTotalUnidadCompleta() + "," + informe.getM_nTotalGeneral() + ","
                        + informe.getM_nIdUnidad() + "," + informe.getM_nTipoTimbrado();

                Statement statement = jdbcConnection.createStatement();
                int estado = statement.executeUpdate(query);

                String query2 = "SELECT IdInforme AS id FROM ProInformeDetallePQ " +
                        "WHERE IdInformeDetalle = (SELECT @@identity as id)";
                Statement statement2 = jdbcConnection.createStatement();
                ResultSet rs = statement2.executeQuery(query2);
                while (rs.next()) {
                    id = rs.getInt("id");
                }

                String query3 = "select FolioInforme from ProInformePQ where IdInforme = " + id;
                Statement statement3 = jdbcConnection.createStatement();
                ResultSet rs3 = statement3.executeQuery(query3);
                while (rs3.next()) {
                    folio = rs3.getString("FolioInforme");
                }
                for (InformeGuiaRequest i : informe.getM_arrClsProInformeGuia()) {
                    i.setM_nIdInforme(id);
                    informesService.agregarInformeGuia(i, id, informe.getM_nCreadoPor(), jdbcConnection);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al agregar");
        }
        return ResponseEntity.ok("Agregado Exitosamente,Folio: " + folio);
    }

    @GetMapping("/Informes/GetByIdViaje/{id}")
    public ResponseEntity<?> getInformesIdViaje(@PathVariable("id") int id,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "SELECT\n" +
                    "\t\tcin.IdInforme,\n" +
                    "\t\tcin.FolioInforme,\n" +
                    "\t\tcin.Fecha,\n" +
                    "\t\tcin.Hora,\n" +
                    "\t\tcin.IdEstatusInforme,\n" +
                    "\t\tcin.IdViaje,\n" +
                    "\t\tcin.FechaLLegada,\n" +
                    "\t\tcin.FechaSalida,\n" +
                    "\t\tcin.IdRuta,\n" +
                    "\t\t(select FolioViaje from ProViajesPQ v where cin.IdViaje = v.IdViaje) as FolioViaje,\n" +
                    "\t\t\t\tcin.IdSucursalEmisora,\n" +
                    "\t\t(select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalEmisora = cs.IdSucursal) as SucursalEmisora,\n" +
                    "\t\tcin.IdSucursalReceptora,\n" +
                    "\n" +
                    "\t\t(select Sucursal from CatSucursales cs " +
                    "where cin.IdSucursalReceptora = cs.IdSucursal) as SucursalReceptora,\n" +
                    "\t\t\t\tcin.IdOperador,\n" +
                    "\n" +
                    "\t\t(select (co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno) as NombreCompleto" +
                    " from CatOperadores co where cin.IdOperador = co.IdOperador) as NombreOperador,\n" +
                    "\t\t(select NumeroOperador from CatOperadores co " +
                    "where cin.IdOperador = co.IdOperador) as NumeroOperador,\n" +
                    "\t\t(select Descripcion from CatUnidades cu where cin.IdDolly = cu.IdUnidad) as Dolly,\n" +
                    "\t\t(select Codigo from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as Identificador,\n" +
                    "\t\t\t\tcin.IdRemolque1,\n" +
                    "\t\t\t\tcin.IdRemolque2,\n" +
                    "\t\t\t\tcin.IdDolly,\n" +
                    "\n" +
                    "\t\t(select Descripcion from CatUnidades cu where cin.IdRemolque1 = cu.IdUnidad) as Remolque1,\n" +
                    "\t\t(select Descripcion from CatUnidades cu where cin.IdRemolque2 = cu.IdUnidad) as Remolque2,\n" +
                    "\t\tcin.PlacasRemolque1,\n" +
                    "\t\tcin.PlacasRemolque2,\n" +
                    "\t\tcin.PlacasDolly,\n" +
                    "\n" +
                    "\t\t\n" +
                    "\t\t(select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadOrigen = cc.IdOrigenDestino) as CiudadOrigen,\n" +
                    "\t\t(select OrigenDestino from CatOrigenesDestinos cc " +
                    "where cin.IdCiudadDestino = cc.IdOrigenDestino) as CiudadDestino,\n" +
                    "\t\t(select Descripcion from CatDetalleRutasPQ cr " +
                    "where cin.IdRuta = cr.IdDetalleRuta) as Ruta,\n" +
                    "\t\tcin.FechaCancelacion,\n" +
                    "\t\tcin.UsuarioCancelacion, \n" +
                    "\t\tcus.Nombre\n" +
                    "\t\tFROM ProInformePQ cin left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario " +
                    "where cin.IdViaje= " + id;

            JSONArray array = new JSONArray();
            try {
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject json;
                String numeroOperador;
                String nombreCompleto;
                String fecha;
                String hora;
                String nombre;
                String apellidoP;
                String apellidoM;
                while (rs.next()) {
                    nombreCompleto = "";
                    numeroOperador = "";
                    fecha = "";
                    hora = "";
                    json = new JSONObject();
                    fecha = DateFormatUtils.format(rs.getDate("Fecha"), "yyyy-MM-dd");
                    json.put("m_dFecha", fecha);
                    json.put("m_dFechaSalida", rs.getTimestamp("FechaSalida"));
                    json.put("m_dFechaLlegada", rs.getTimestamp("FechaLlegada"));
                    json.put("m_dtFechaCancelacion",
                            DateFormatUtils.format(rs.getTimestamp("FechaCancelacion"),
                                    "yyyy-MM-dd HH:mm:ss"));
                    json.put("m_sFolioInforme", rs.getString("FolioInforme"));
                    json.put("m_sCiudadDestino", rs.getString("CiudadDestino"));
                    json.put("m_sCiudadOrigen", rs.getString("CiudadOrigen"));
                    json.put("m_nIdEstatusInforme", rs.getInt("IdEstatusInforme"));
                    json.put("m_nIdInforme", rs.getInt("IdInforme"));
                    json.put("m_nIdOperador", rs.getInt("IdOperador"));
                    numeroOperador = rs.getString("NumeroOperador");
                    nombreCompleto = rs.getString("NombreOperador");
                    json.put("m_sNombreCompleto", nombreCompleto);
                    json.put("m_sNumeroOperador", numeroOperador);
                    json.put("m_sNumeroNombreOperador", numeroOperador + " " + nombreCompleto);
                    json.put("m_nIdRemolque1", rs.getInt("IdRemolque1"));
                    json.put("m_nIdRemolque2", rs.getInt("IdRemolque2"));
                    json.put("m_sRemolque1", rs.getString("Remolque1"));
                    json.put("m_sRemolque2", rs.getString("Remolque2"));
                    json.put("m_sPlacasRemolque1", rs.getString("PlacasRemolque1"));
                    json.put("m_sPlacasRemolque2", rs.getString("PlacasRemolque2"));
                    json.put("m_sPlacasDolly", rs.getString("PlacasDolly"));
                    json.put("m_sRuta", rs.getString("Ruta"));
                    json.put("m_nIdRuta", rs.getString("IdRuta"));
                    json.put("m_sSucursalReceptora", rs.getString("SucursalReceptora"));
                    json.put("m_sSucursalEmisora", rs.getString("SucursalEmisora"));
                    json.put("m_nIdSucursalReceptora", rs.getInt("IdSucursalReceptora"));
                    json.put("m_nIdSucursalEmisora", rs.getInt("IdSucursalEmisora"));
                    json.put("m_nIdDolly", rs.getInt("IdDolly"));
                    json.put("m_sDolly", rs.getString("Dolly"));
                    json.put("m_nIdViaje", rs.getInt("IdViaje"));
                    json.put("m_sFolioViaje", rs.getString("FolioViaje"));
                    hora = DateFormatUtils.format(rs.getTime("Hora"), "HH:mm:ss");
                    json.put("m_tHora", hora);
                    json.put("m_sFechaSalida", rs.getTimestamp("FechaSalida"));
                    json.put("m_sFechaLlegada ", rs.getTimestamp("FechaLlegada"));
                    json.put("m_sFechayHora", fecha + " " + hora);
                    json.put("m_nIdUsuarioCancelacion", rs.getInt("UsuarioCancelacion"));

                    nombre = rs.getString("Nombre");
                    json.put("m_sNombre", nombre);
                    json.put("m_sNombreUsuario", nombre);
                    json.put("m_sNombreCompleto", rs.getString("NombreOperador"));
                    json.put("m_sSucursalEmisora", rs.getString("SucursalEmisora"));
                    json.put("m_sSucursalReceptora", rs.getString("SucursalReceptora"));
                    json.put("m_arrClsProGuia", informesService.getListadoPorInformes(id, jdbcConnection));
                    array.put(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ResponseEntity.status(500).body("Error al listar informe por viaje");
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/Informes/Eliminar/{idInforme}/{idUsuario}")
    public ResponseEntity<?> eliminarInforme(@PathVariable("idInforme") int idInforme,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "Select IIF(IdEstatusInforme = 9, CAST(1 as BIT), CAST(0 as BIT)) as sePuedeEliminar " +
                        "FROM ProInformePQ WHERE IdInforme = " + idInforme;
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                if (!jsonObject.getBoolean("sePuedeEliminar")) {
                    return ResponseEntity.status(500).body("Debe cancelar el informe para poder eliminarlo");
                }
                query = "EXEC usp_ProInformeEliminarPQ " + idInforme;
                statement.executeUpdate(query);

                return ResponseEntity.ok("Registro Eliminado");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al eliminar el informe");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Informes/Cancelar/{idInforme}")
    public ResponseEntity<?> cancelarInforme(@RequestBody InformesCancelarRequest icr,
                                             @PathVariable("idInforme") int idInforme,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProInformeCancelarPQ  " + idInforme + ", " + icr.getUsuarioCancelacion()
                        + ", '" + icr.getFechaCancelacion() + "', '" + icr.getMotivoCancelacion() + "'";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al cancelar el informe");
            }
            return ResponseEntity.ok("Cancelado exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Informes/Modificar")
    public ResponseEntity<?> modificarInforme(@RequestBody InformesModificarRequest imr,
                                              @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProInformeModificarPQ '" + imr.getM_dFecha() + "','" + imr.getM_tHora() + "',"
                        + imr.getM_nIdEstatusInforme() + "," + imr.getM_nIdSucursalEmisora() + ","
                        + imr.getM_nIdSucursalReceptora() + "," + imr.getM_nIdDolly() + ","
                        + imr.getM_nIdRemolque1() + "," + imr.getM_nIdRemolque2() + ","
                        + imr.getM_nIdCiudadOrigen() + "," + imr.getM_nIdCiudadDestino() + ","
                        + imr.getM_nIdRuta() + ",''," + imr.getM_nIdInforme();
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
                String query2 = "EXEC usp_ProInformesEliminarGuiasPQ " + imr.getM_nIdInforme();
                Statement statement2 = jdbcConnection.createStatement();
                statement2.executeUpdate(query2);
                for (InformeGuiaRequest i : imr.getM_arrClsProInformeGuia()) {
                    informesService.agregarInformeGuia(i, imr.getM_nIdInforme(), imr.getM_nCreadoPor(), jdbcConnection);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al modificar");
            }
            return ResponseEntity.ok("Modificacion exitosa");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Informes/ModificarEscaner")
    public ResponseEntity<?> modificarInformeEscaner(@RequestBody InformesModificarRequest imr,
                                                     @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

                String query2 = "EXEC usp_ProInformesEliminarGuiasPQ " + imr.getM_nIdInforme();
                Statement statement2 = jdbcConnection.createStatement();
                statement2.executeUpdate(query2);
                for (InformeGuiaRequest i : imr.getM_arrClsProInformeGuia()) {
                    informesService.agregarInformeGuia(i, imr.getM_nIdInforme(), imr.getM_nCreadoPor(), jdbcConnection);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al modificar");

            }
            return ResponseEntity.ok("Modificacion exitosa");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Informes/cargaAgregarListadoBorrador/{idInforme}")
    public ResponseEntity<?> cargaAgregarListadoBorrador(
            @PathVariable("idInforme") int idInforme, @RequestBody List<String> listaIgr,
            @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                //SE ELIMINAN LOS REGISTROS EN CASO DE TENER UN BORRADOR ANTERIOR, ASI NO SE SOBREESCRIBEN LOS DATOS
                String queryDelete = "DELETE FROM ProInformeGuiaPaquetesEscanerBorradorPQ WHERE idInforme = ? AND isDescarga = ?";
                PreparedStatement pstmtDelete = jdbcConnection.prepareStatement(queryDelete);
                pstmtDelete.setInt(1, idInforme);
                pstmtDelete.setBoolean(2, false);
                pstmtDelete.executeUpdate();

                //SE RECORRE TODOS LOS ELEMENTOS DE LA LISTA QUE LLEGO Y SE AÑADEN A LA TABLA
                String query = "insert into ProInformeGuiaPaquetesEscanerBorradorPQ (idInforme, qr, idGuia, " +
                        "idEmbarque, m_iIndex, isDescarga) values (?, ?, ?, ?, ?, ?)";

                for (String igr : listaIgr) {
                    String[] valores = igr.split("-");
                    int idGuia = Integer.parseInt(valores[0]);
                    int idEmbarque = Integer.parseInt(valores[1]);
                    int index = Integer.parseInt(valores[2]);
                    PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
                    pstmt.setInt(1, idInforme);
                    pstmt.setString(2, igr);
                    pstmt.setInt(3, idGuia);
                    pstmt.setInt(4, idEmbarque);
                    pstmt.setInt(5, index);
                    pstmt.setBoolean(6, false);
                    pstmt.executeUpdate();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar el borrador");
        }
        return ResponseEntity.ok("Borrador guardado exitosamente");
    }

    //OBTENER TODOS LOS LISTADOS DE LA TABLA SEGUN EL ID DE INFORME QUE LLEGUE
    @GetMapping("/Informes/cargaObtenerListadoBorrador/{idInforme}")
    public ResponseEntity<?> cargaObtenerListadoBorrador(
            @PathVariable("idInforme") int idInforme, @RequestHeader("RFC") String rfc) throws
            Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "SELECT * FROM ProInformeGuiaPaquetesEscanerBorradorPQ WHERE idInforme = ?";

                PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
                pstmt.setInt(1, idInforme);

                ResultSet rs = pstmt.executeQuery();
                List<Map<String, Object>> records = new ArrayList<>();

                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    // Inserting key-value pairs into the json object
                    record.put("idInforme", rs.getInt("idInforme"));
                    record.put("qr", rs.getString("qr"));
                    record.put("idGuia", rs.getInt("idGuia"));
                    record.put("idEmbarque", rs.getInt("idEmbarque"));
                    record.put("m_iIndex", rs.getInt("m_iIndex"));
                    records.add(record);
                }

                return ResponseEntity.ok(records);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al obtener el borrador");
        }
    }

    @PostMapping("/Informes/descargaAgregarListadoBorrador/{idInforme}")
    public ResponseEntity<?> descargaAgregarListadoBorrador(
            @PathVariable("idInforme") int idInforme,
            @RequestBody List<InformeDescargaBorradorRequest> borrador,
            @RequestHeader("RFC") String rfc) throws Exception {

        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                //SE ELIMINAN LOS REGISTROS EN CASO DE TENER UN BORRADOR ANTERIOR, ASI NO SE SOBREESCRIBEN LOS DATOS
                String queryDelete = "DELETE FROM ProInformeGuiaPaquetesEscanerBorradorPQ WHERE idInforme = ? AND isDescarga = ?";
                PreparedStatement pstmtDelete = jdbcConnection.prepareStatement(queryDelete);
                pstmtDelete.setInt(1, idInforme);
                pstmtDelete.setBoolean(2, true);
                pstmtDelete.executeUpdate();

                //SE RECORRE TODOS LOS ELEMENTOS DE LA LISTA QUE LLEGO Y SE AÑADEN A LA TABLA
                String query = "insert into ProInformeGuiaPaquetesEscanerBorradorPQ " +
                        "(idInforme, qr, idGuia, idEmbarque, m_iIndex, isDescarga) values (?, ?, ?, ?, ?, ?)";
                for (InformeDescargaBorradorRequest detalle : borrador) {
                    int idGuia = detalle.getM_nIdGuia();
                    int idEmbarqueDetalle = detalle.getM_nIdEmbarqueDetalle();
                    int index = detalle.getIndex();
                    String qr = idGuia + "-" + idEmbarqueDetalle + "-" + index;

                    PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
                    pstmt.setInt(1, idInforme);
                    pstmt.setString(2, qr);
                    pstmt.setInt(3, idGuia);
                    pstmt.setInt(4, idEmbarqueDetalle);
                    pstmt.setInt(5, index);
                    pstmt.setBoolean(6, true);
                    pstmt.executeUpdate();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar el borrador");
        }
        return ResponseEntity.ok("Borrador guardado exitosamente");
    }

    @GetMapping("/Informes/descargaObtenerListadoBorrador/{idInforme}")
    public ResponseEntity<?> descargaObtenerListadoBorrador(
            @PathVariable("idInforme") int idInforme,
            @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "SELECT * FROM ProInformeGuiaPaquetesEscanerBorradorPQ WHERE idInforme = ? AND isDescarga = ?";

                PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
                pstmt.setInt(1, idInforme);
                pstmt.setBoolean(2, true);


                ResultSet rs = pstmt.executeQuery();
                List<Map<String, Object>> records = new ArrayList<>();

                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("idInforme", rs.getInt("idInforme"));
                    record.put("m_nIdGuia", rs.getString("idGuia"));
                    record.put("qr", rs.getString("qr"));
                    record.put("m_nIdEmbarqueDetalle", rs.getString("idEmbarque"));
                    record.put("index", rs.getString("m_iIndex"));
                    records.add(record);
                }

                return ResponseEntity.ok(records);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al obtener el borrador");
        }
    }

    @GetMapping("/SisEstatus/getEstatusDefaultInformes")
    public ResponseEntity<?> obtenerEstatusInformes(@RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdEstatusInforme as m_nIdEstatusInforme\n" +
                    "     , Abreviacion as m_sAbreviacion\n" +
                    "     , Estatus as m_sEstatus\n" +
                    "     , Descripcion as m_sDescripcion\n" +
                    "     , Color as m_sColor\n" +
                    "FROM CatEstatusInformePQ WHERE Estatus = 'Pendiente'";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}