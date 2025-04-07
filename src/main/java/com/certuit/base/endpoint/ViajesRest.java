package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.InformesService;
import com.certuit.base.service.base.UltimaMillaService;
import com.certuit.base.service.base.ViajesService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.apache.commons.lang.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

@RestController
@RequestMapping(value = "/api")
public class ViajesRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    ViajesService viajesService;
    @Autowired
    InformesService informesService;
    @Autowired
    EmbarquesRest embarqueRest;
    @Autowired
    UltimaMillaService ultimaMillaService;

    @GetMapping("/Viajes/GetByFiltro/{sFechaInicial}/{sFechaFinal}/{sEstatus}/{folio}/{origen}/{destino}/{operador}")
    public ResponseEntity<?> getViajesByFiltro(@PathVariable("sFechaInicial") String fechaInicial,
                                               @PathVariable("sFechaFinal") String fechaFinal,
                                               @PathVariable("sEstatus") String estatus,
                                               @PathVariable("folio") String folio,
                                               @PathVariable("origen") int origen,
                                               @PathVariable("destino") int destino,
                                               @PathVariable("operador") int idOperador,
                                               @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            fechaInicial = !fechaInicial.equalsIgnoreCase("0") ? "'" + fechaInicial + "'" : null;
            fechaFinal = !fechaFinal.equalsIgnoreCase("0") ? "'" + fechaFinal + "'" : null;
            folio = !folio.equalsIgnoreCase("0") ? folio : null;
            if (folio != null) {
                folio = folio.toUpperCase();
            }
            String query = "SELECT  \n" +
                    "               FORMAT(v.FechaRegistro, 'dd-MM-yyyy') + ' ' + convert(char(5)," +
                    "convert(time(0),v.HoraRegistro)) as m_sFechaHora\n" +
                    "                ,v.IdViaje as m_nIdViaje \n" +
                    "                ,v.IdSucursal as m_nIdSucursal \n" +
                    "                ,s.Sucursal as m_sSucursal \n" +
                    "                ,v.FolioViaje as m_sFolioViaje \n" +
                    "                ,v.NumViajeCliente as m_sNumViajeCliente \n" +
                    "                ,v.IdEstatusViaje as m_nIdEstatusViaje \n" +
                    "                ,ev.Estatus as m_sEstatus \n" +
                    "                ,ev.Color as m_sColorEstatus \n" +
                    "                ,ev.Abreviacion as m_sAbreviacion \n" +
                    "                ,v.CandadoOficial as m_sCandadoOficial \n" +
                    "                ,v.Identificador as m_sIdentificador \n" +
                    "                ,v.FechaRegistro as m_dFechaRegistro \n" +
                    "                ,v.FechaSalida as m_dFechaSalida \n" +
                    "                ,v.FechaLLegada as m_dFechaLlegada \n" +
                    "                ,v.HoraRegistro as m_tHoraRegistro \n" +
                    "                ,v.IdOrigen as m_nIdOrigen \n" +
                    "                ,v.IdDestino as m_nIdDestino \n" +
                    "                ,co.EsPermisionario as m_bEsPermisionario \n" +
                    "                ,(select OrigenDestino from CatOrigenesDestinos cc " +
                    "where v.IdOrigen = cc.IdOrigenDestino) as m_sOringen \n" +
                    "                ,(select OrigenDestino from CatOrigenesDestinos cc " +
                    "where v.IdDestino = cc.IdOrigenDestino) as m_sDestino \n" +
                    "                , (co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno) " +
                    "as m_sOperador\n" +
                    "                ,(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdUnidad) AS m_sUnidad\n" +
                    "                ,(select cu.Placas from CatUnidades cu where cu.IdUnidad = v.IdUnidad) " +
                    "AS m_sPlacasUnidad\n" +

                    "                ,(select cu.EsUnidadPermisionario from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdUnidad) AS m_bUnidadPermisionario \n" +
                    "                ,(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdRemolque1) AS m_sRemolque1\n" +
                    "                ,(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdRemolque2) AS m_sRemolque2\n" +
                    "                ,(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdDolly) AS m_sDolly\n" +
                    "                 ,case when v.IdEstatusViaje = 8 and " +
                    "(select IsNull(SUM(CAST(i.Timbrado AS INT)),0) from ProInformePQ i " +
                    "where i.IdViaje = v.IdViaje) = 0 then 1 else 0 end  as m_bSePuedeCancelar \n" +
                    "\n" +
                    "                 ,(select IdEstatuUnidad from CatEstatusUnidades where IdEstatuUnidad =" +
                    " (select IdEstatuUnidad from ProInventarioUnidades x where x.IdUnidad = v.IdUnidad)) " +
                    "as m_sEstatusUnidad \n" +
                    "                ,(select top 1 (select cs.Sucursal from CatSucursales cs " +
                    "where cs.IdSucursal = pi.IdSucursalReceptora) from ProInformePQ pi " +
                    "where pi.IdViaje = v.IdViaje) as m_sSucursalReceptora\n" +
                    "                ,FORMAT(v.FechaCancelacion, 'dd/MM/yyyy hh:mm tt') as FechaCancelacion\n" +
                    "                ,u.Nombre as 'UsuarioCancelacion'\n" +
                    "                ,v.MotivoCancelacion\n" +
                    "                ,CASE\n" +
                    "                   WHEN PV.Viaje IS NULL AND v.Viaje IS NULL THEN 'No encontrado'\n" +
                    "                   WHEN PV.Viaje IS NULL THEN CONCAT(s.Abreviacion, '-',v.Viaje)\n" +
                    "                   ELSE CONCAT(s.Abreviacion, '-',PV.Viaje)\n" +
                    "                END AS FolioERP\n" +
                    "                FROM ProViajesPQ v\n" +
                    "                left join CatSucursales s on v.IdSucursal = s.IdSucursal\n" +
                    "                left join CatOperadores co on co.IdOperador = v.IdOperador\n" +
                    "                left join CatUsuarios u on u.IdUsuario = v.IdUsuarioCancelacion\n" +
                    "                left join CatEstatusViajePQ ev on v.IdEstatusViaje= ev.IdEstatusViaje" +
                    "                left join ProViajes PV on v.IdViajeCompuesto = PV.IdViaje" +
                    " WHERE  (v.FolioViaje LIKE '%" + folio + "%')" +
                    " OR ((v.IdEstatusViaje = " + estatus + " OR " + estatus + " = 0)" +
                    " AND ((v.FechaRegistro >= " + fechaInicial + " OR " + fechaInicial + " IS NULL))" +
                    " AND (v.FechaRegistro <= " + fechaFinal + " OR " + fechaFinal + " IS NULL)" +
                    " AND (v.IdOperador = " + idOperador + " OR " + idOperador + " = 0)" +
                    " AND (v.IdOrigen = " + origen + " OR " + origen + " = 0)" +
                    " AND (v.IdDestino = " + destino + " OR " + destino + " = 0) and" + "'" + folio + "'"
                    + " = 'null' )" +
                    " ORDER BY v.FechaRegistro DESC, v.HoraRegistro DESC";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonArray = viajesService.convertViaje(rs, jdbcConnection);
//        JSONArray array = new JSONArray();
//        JSONObject json;
        /*while(rs.next()){
            json = new JSONObject();
            json.put("m_nIdViaje",rs.getInt("IdViaje"));
            json.put("m_nIdSucursal",rs.getInt("IdSucursal"));
            json.put("m_sSucursal",rs.getString("Sucursal"));
            json.put("m_sFolioViaje",rs.getString("FolioViaje"));
            json.put("m_sNumViajeCliente",rs.getString("NumViajeCliente"));
            json.put("m_dFecha",rs.getDate("Fecha"));
            json.put("m_tHora",rs.getTime("Hora"));
            json.put("m_nIdEstatusViaje",rs.getInt("IdEstatusViaje"));
            json.put("m_sCandadoOficial",rs.getString("CandadoOficial"));
            json.put("m_sIdentificador",rs.getString("Identificador"));
            json.put("m_sSucursalReceptora",rs.getString("SucursalReceptora"));
            json.put("m_dFechaRegistro",rs.getDate("FechaRegistro"));
            json.put("m_tHoraRegistro",rs.getTime("HoraRegistro"));
            json.put("m_sEstatus",rs.getString("Estatus"));
            json.put("m_sDescripcion",rs.getString("Color"));
            json.put("m_sAbreviacion",rs.getString("Abreviacion"));
            json.put("m_sFechaHora", rs.getDate("Fecha") +" "+rs.getTime("Hora"));
            json.put("m_arrInformes",viajesService.getListadoByViajeId(rs.getInt("IdViaje"),jdbcConnection));
            array.put(json);
        }*/

            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Viajes/GetListado")
    public ResponseEntity<?> getAllViajes(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "v.IdViaje\n" +
                    ",v.IdSucursal\n" +
                    ",s.Sucursal\n" +
                    ",v.FolioViaje\n" +
                    ",v.NumViajeCliente\n" +
                    ",v.Fecha\n" +
                    ",v.Hora\n" +
                    ",v.IdEstatusViaje\n" +
                    ",ev.Estatus\n" +
                    ",ev.Color\n" +
                    ",ev.Abreviacion\n" +
                    ",v.CandadoOficial\n" +
                    ",v.Identificador\n" +
                    ",v.FechaRegistro\n" +
                    ",v.HoraRegistro\n" +
                    ",v.IdOrigen\n" +
                    ",v.IdDestino,\n" +
                    "(select OrigenDestino from CatOrigenesDestinos cc " +
                    "where v.IdOrigen = cc.IdOrigenDestino) as Origen,\n" +
                    "(select OrigenDestino from CatOrigenesDestinos cc " +
                    "where v.IdDestino = cc.IdOrigenDestino) as Destino\n" +
                    ",(select cc.Ciudad from CatCiudades cc WHERE cc.IdCiudad = v.IdOrigen) as Origen\n" +
                    ",(select cc.Ciudad from CatCiudades cc WHERE cc.IdCiudad = v.IdDestino) as Destino\n" +
                    ",(select co.Nombres + ' ' + co.ApellidoPaterno + ' ' + co.ApellidoMaterno " +
                    "from CatOperadores co WHERE co.IdOperador = v.IdOperador) as Operador\n" +
                    ",(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdUnidad) AS Unidad\n" +
                    ",(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdRemolque1) AS Remolque1\n" +
                    ",(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdRemolque2) AS Remolque2\n" +
                    ",(select (cu.Codigo + '-' + cu.Descripcion) from CatUnidades cu " +
                    "where cu.IdUnidad = v.IdDolly) AS Dolly\n" +
                    ",(select top 1 (select cs.Sucursal from CatSucursales cs " +
                    "where cs.IdSucursal = pi.IdSucursalReceptora) from ProInformePQ pi " +
                    "where pi.IdViaje = v.IdViaje) as SucursalReceptora\n" +
                    "\n" +
                    "FROM ProViajesPQ v inner join CatSucursales s on v.IdSucursal = s.IdSucursal " +
                    "inner join CatEstatusViajePQ ev on v.IdEstatusViaje= ev.IdEstatusViaje\t" +
                    "Order by FechaRegistro desc\n";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdViaje", rs.getInt("IdViaje"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sSucursal", rs.getString("Sucursal"));
                json.put("m_sFolioViaje", rs.getString("FolioViaje"));
                json.put("m_sNumViajeCliente", rs.getString("NumViajeCliente"));
                json.put("m_dFecha", rs.getDate("Fecha"));
                json.put("m_tHora", rs.getTime("Hora"));
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sCandadoOficial", rs.getString("CandadoOficial"));
                json.put("m_sIdentificador", rs.getInt("Identificador"));
                json.put("m_sSucursalReceptora", rs.getString("SucursalReceptora"));
                json.put("m_sFechaHora", rs.getString("Fecha") + " " + rs.getTime("Hora"));
                json.put("m_dFechaRegistro", rs.getDate("FechaRegistro"));
                json.put("m_tHoraRegistro", rs.getTime("HoraRegistro"));
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sEstatus", rs.getString("Estatus"));
                json.put("m_sDescripcion", rs.getString("Color"));
                json.put("m_sAbreviacion", rs.getString("Abreviacion"));
                json.put("m_sOperador", rs.getString("Operador"));
                json.put("m_sUnidad", rs.getString("Unidad"));
                json.put("m_sRemolque1", rs.getString("Remolque1"));
                json.put("m_sRemolque2", rs.getString("Remolque2"));
                json.put("m_sDolly", rs.getString("Dolly"));
                json.put("m_sOringen", rs.getString("Origen"));
                json.put("m_sDestino", rs.getString("Destino"));
                json.put("m_arrInformes",
                        viajesService.getListadoByViajeId(rs.getInt("IdViaje"), jdbcConnection));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Viajes/GetByFiltros/{FechaInicial}/{FechaFinal}/{Sucursal}/{Estatus}")
    public ResponseEntity<?> getViajesFiltrados(@PathVariable("FechaInicial") String fechaInicial,
                                                @PathVariable("FechaFinal") String fechaFinal,
                                                @PathVariable("Sucursal") String sucursal,
                                                @PathVariable("Estatus") String estatus,
                                                @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            if (fechaInicial.equalsIgnoreCase("0")) {
                fechaInicial = "NULL";
            } else {
                char comilla = (char) 34;
                fechaInicial = comilla + fechaInicial + comilla;
            }
            if (fechaFinal.equalsIgnoreCase("0")) {
                fechaFinal = "NULL";
            } else {
                char comilla = (char) 34;
                fechaFinal = comilla + fechaFinal + comilla;
            }
            if (sucursal.equalsIgnoreCase("0")) {
                sucursal = "NULL";
            }
            if (estatus.equalsIgnoreCase("0")) {
                estatus = "NULL";
            }

            String query = "EXEC usp_ProViajesGetListadoFiltroPQ " + sucursal + "," + estatus + ","
                    + fechaInicial + ", " + fechaFinal + "";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdViaje", rs.getInt("IdViaje"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sSucursal", rs.getString("Sucursal"));
                json.put("m_sFolioViaje", rs.getString("FolioViaje"));
                json.put("m_sNumViajeCliente", rs.getString("NumViajeCliente"));
                json.put("m_dFecha", rs.getDate("Fecha"));
                json.put("m_tHora", rs.getTime("Hora"));
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sCandadoOficial", rs.getString("CandadoOficial"));
                json.put("m_sIdentificador", rs.getString("Identificador"));
                json.put("m_sFechaHora", rs.getString("Fecha") + " " + rs.getString("Hora"));
                json.put("m_dFechaRegistro", rs.getDate("FechaRegistro"));
                json.put("m_tHoraRegistro", rs.getTime("HoraRegistro"));
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sEstatus", rs.getString("Estatus"));
                json.put("m_sDescripcion", rs.getString("Descripcion"));
                json.put("m_sAbreviacion", rs.getString("Abreviacion"));
                json.put("m_arrInformes", viajesService.getListadoByViajeId(rs.getInt("IdViaje"),
                        jdbcConnection));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Viajes/GetById/{id}")
    public ResponseEntity<?> getViajeId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT \n" +
                    "                    IdViaje as m_nIdViaje \n" +
                    "                     ,g.IdSucursal as m_nIdSucursal \n" +
                    "                     ,FolioViaje as m_sFolioViaje \n" +
                    "                     ,NumViajeCliente as m_sNumViajeCliente \n" +
                    "                     ,Fecha as m_dFecha \n" +
                    "                     ,Hora as m_tHora \n" +
                    "                     ,FechaRegistro as m_dFechaRegistro \n" +
                    "                     ,HoraRegistro as m_tHoraRegistro \n" +
                    "                     ,IdRuta as m_nIdRuta \n" +
                    "                     ,g.IdEstatusViaje as m_nIdEstatusViaje \n" +
                    "                     ,CandadoOficial as m_sCandadoOficial \n" +
                    "                     ,Identificador as m_sIdentificador \n" +
                    "                     ,FechaCancelacion as m_dFechaCancelacion \n" +
                    "                     ,IdUsuarioCancelacion as m_nIdUsuarioCancelacion \n" +
                    "                     ,IdViajeCompuesto as m_nIdViajeCompuesto \n" +
                    "                     ,IdOrigen as m_nIdOrigen \n" +
                    "\t\t\t\t\t ,s.Sucursal as m_sSucursal\n" +
                    "\t\t\t\t\t ,g.MotivoCancelacion as m_sMotivoCancelacion\n" +
                    "\t\t\t\t\t ,u.Nombre as m_sUsuarioCancelacion\n" +
                    "\t\t\t\t\t ,ev.Estatus as m_sEstatusViaje\n" +
                    "                     ,IdDestino as m_nIdDestino \n" +
                    "                     ,(select cod.OrigenDestino from CatOrigenesDestinos cod " +
                    "where cod.IdOrigenDestino = g.IdOrigen) as m_sOrigen \n" +
                    "                     ,(select cod.OrigenDestino from CatOrigenesDestinos cod " +
                    "where cod.IdOrigenDestino = g.IdDestino) as m_sDestino \n" +
                    "                 ,IdRemolque1 as m_nIdRemolque1 \n" +
                    "                 ,(select Codigo from CatUnidades x " +
                    "where x.IdUnidad = IdRemolque1) as m_sCodigoRemolque1 \n" +
                    "                 ,(select Descripcion from CatUnidades x " +
                    "where x.IdUnidad = IdRemolque1) as m_sDescripcionRemolque1 \n" +
                    "                 ,(select Placas from CatUnidades x " +
                    "where x.IdUnidad = IdRemolque1) as m_sPlacasRemolque1 \n" +
                    "                 ,(select Estatus from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = IdRemolque1)) as m_sEstatusRemolque1 \n" +
                    "                 ,(select Color from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = IdRemolque1)) as m_sColorRemolque1 \n" +
                    "                 ,(select ColorLetra from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = IdRemolque1)) as m_sColorLetrasRemolque1 \n" +
                    "                 ,IdRemolque2 as m_nIdRemolque2 \n" +
                    "                 ,(select Codigo from CatUnidades x " +
                    "where x.IdUnidad = IdRemolque2) as m_sCodigoRemolque2 \n" +
                    "                 ,(select Descripcion from CatUnidades x " +
                    "where x.IdUnidad = IdRemolque2) as m_sDescripcionRemolque2 \n" +
                    "                 ,(select Placas from CatUnidades x " +
                    "where x.IdUnidad = IdRemolque2) as m_sPlacasRemolque2 \n" +
                    "                 ,(select Estatus from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = IdRemolque2)) as m_sEstatusRemolque2 \n" +
                    "                 ,(select Color from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = IdRemolque2)) as m_sColorRemolque2 \n" +
                    "                 ,(select ColorLetra from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = IdRemolque2)) as m_sColorLetrasRemolque2 \n" +
                    "                 ,IdDolly as m_nIdDolly \n" +
                    "                 ,(select Codigo from CatUnidades x " +
                    "where x.IdUnidad = IdDolly) as m_sCodigoDolly \n" +
                    "                 ,(select Descripcion from CatUnidades x " +
                    "where x.IdUnidad = IdDolly) as m_sDescripcionDolly \n" +
                    "                 ,(select Placas from CatUnidades x " +
                    "where x.IdUnidad = IdDolly) as m_sPlacasDolly \n" +
                    "                 ,IdOperador as m_nIdOperador  \n" +
                    "                 ,(select Nombre from CatOperadores x " +
                    "where x.IdOperador = g.IdOperador) as m_sNombreOperador  \n" +
                    "                 ,IdUnidad as m_nIdUnidad  \n" +
                    "                 ,case when g.IdEstatusViaje = 8 and " +
                    "(select IsNull(SUM(CAST(i.Timbrado AS INT)),0) from ProInformePQ i " +
                    "where i.IdViaje = g.IdViaje) = 0 then 1 else 0 end  as m_bSePuedeCancelar" +
                    "                 ,(select Codigo from CatUnidades x " +
                    "where x.IdUnidad = g.IdUnidad) as m_sCodigoUnidad \n" +
                    "                 ,(select Descripcion from CatUnidades x " +
                    "where x.IdUnidad = g.IdUnidad) as m_sDescripcionUnidad \n" +
                    "                 ,(select Placas from CatUnidades x " +
                    "where x.IdUnidad = g.IdUnidad) as m_sPlacasUnidad \n" +
                    "                 ,(select Estatus from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = g.IdUnidad)) as m_sEstatusUnidad \n" +
                    "                 ,(select Color from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = g.IdUnidad)) as m_sColorUnidad \n" +
                    "                 ,(select ColorLetra from CatEstatusUnidades " +
                    "where IdEstatuUnidad = (select IdEstatuUnidad from ProInventarioUnidades x " +
                    "where x.IdUnidad = g.IdUnidad)) as m_sColorLetrasUnidad \n" +
                    "                 ,g.EsOperadorPermisionario \n" +
                    "                 ,g.LicenciaPermisionario \n" +
                    "                 ,g.NombrePermisionario \n" +
                    "                 ,g.FechaVigenciaPermisionario \n" +
                    "                 FROM ProViajesPQ g inner join CatSucursales s on s.IdSucursal = g.IdSucursal " +
                    "left join CatUsuarios u on u.IdUsuario = g.IdUsuarioCancelacion" +
                    " inner join CatEstatusViajePQ ev on ev.IdEstatusViaje = g.IdEstatusViaje" +
                    " WHERE  g.IdViaje = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
//        jsonObject.put("m_arrConceptosFacturacion", viajesService.getConceptosFacturacionByIdViaje(id, 2, jdbcConnection));
            jsonObject.put("m_arrTrayectos", viajesService.getTrayectosViaje(id, jdbcConnection));
            jsonObject.put("m_arrInformes", viajesService.getInformesAsignadosByIdViaje(id, jdbcConnection));
            jsonObject.put("m_arrIdRutas", viajesService.getIdRutasByIdViaje(id, jdbcConnection));
//        jsonObject.put("m_arrInformesDesasignar", viajesService.getInformesDesasignarByIdViaje(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Viajes/paradasTimbradas/{idViajeTrayecto}")
    public ResponseEntity<?> getViajeTimbrado(@PathVariable("idViajeTrayecto") int id,
                                              @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
//          CUANDO ES OPERADOR PERMISIONARIO NO SE VALIDA QUE ESTE TIMBRADO
                String query = "SELECT IIF(viaje.EsOperadorPermisionario = 1,CAST(1 as BIT)," +
                        "ISNULL(pq.Timbrado, 0)) AS Timbrado, FolioInforme FROM ProViajeDetalleParadasPQ pq " +
                        "INNER JOIN ProInformePQ info ON info.IdInforme = pq.IdInforme " +
                        "INNER JOIN ProViajesPQ viaje on viaje.IdViaje = pq.IdViaje WHERE pq.IdViaje = " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONArray array = UtilFuctions.convertArray(rs);
                query = "Select IIF((IdEstatusViaje = 6 OR IdEstatusViaje = 10), CAST(0 AS BIT), " +
                        "CAST(1 AS BIT)) as salidaValida from ProViajesPQ where IdViaje = " + id;
                rs = statement.executeQuery(query);
                JSONObject jsonRs = UtilFuctions.convertObject(rs);
                if (!jsonRs.getBoolean("salidaValida")) {
                    return ResponseEntity.status(500).body("No se puede dar salida a un viaje cancelado o terminado.");
                }

                return ResponseEntity.ok(array.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception exception) {
            return ResponseEntity.status(500).body("Hubo un error al validar la salida.");
        }
    }

    @PostMapping("/Viajes/cancelarTrayecto/{idViajeTrayecto}")
    public ResponseEntity<?> cancelarTrayecto(@PathVariable("idViajeTrayecto") int id,
                                              @RequestBody CancelarRequest request,
                                              @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "exec usp_ProViajeCancelarSalidaLLegadaoPQ " + id + "," + request.getTipo() + ",'"
                    + request.getFecha() + "','" + request.getHora() + "'," + request.getUsuarioId() + ",'"
                    + request.getMotivo() + "'";
            Statement statement = jdbcConnection.createStatement();
            statement.execute(query);
            query = "SELECT GPQ.IdGuia as Medio, CASE WHEN TPQ.IdOrigen=IPQ.IdCiudadOrigen THEN 'true' " +
                    "ELSE 'false' end as Boo FROM ProViajesPQ as VPQ " +
                    "inner join ProInformePQ as IPQ on VPQ.IdViaje=IPQ.IdViaje " +
                    "inner join ProGuiaPQ as GPQ on GPQ.IdInforme=IPQ.IdInforme " +
                    "inner join ProViajeTrayectosPQ as TPQ on VPQ.IdViaje=TPQ.IdViaje " +
                    "where TPQ.IdViajeTrayecto= " + id + " ";
            ResultSet rs = statement.executeQuery(query);
            int gid = 0;
            boolean mandarCorreo = false;
            while (rs.next()) {
                gid = rs.getInt("Medio");
                mandarCorreo = rs.getBoolean("Boo");
            }
            if (mandarCorreo) {
                query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
                statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);
                try {
                    viajesService.enviarCorreoStatus(jdbcConnection, statement, gid, null,
                            true, rfc,
                            "Se ha presentado una situación y su orden ha regresado a la sucursal de origen");
                } catch (Exception e) {
                    return ResponseEntity.ok("Cancelado exitosamente, pero no se logró mandar el correo " +
                            "electrónico al cliente.");
                }
            }

            return ResponseEntity.ok("Se canceló el trayecto correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Viajes/GetListadoEstatus/{idEstatus}")
    public ResponseEntity<?> getListadoEstatusViajes(@PathVariable("idEstatus") int idEstatus,
                                                     @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\t\tv.IdViaje\n" +
                    "      ,v.IdSucursal\n" +
                    "\t  ,s.Sucursal\n" +
                    "      ,v.FolioViaje\n" +
                    "      ,v.NumViajeCliente\n" +
                    "      ,v.Fecha\n" +
                    "      ,v.Hora\n" +
                    "      ,v.IdEstatusViaje\n" +
                    "\t  ,ev.Estatus\n" +
                    "\t  ,ev.Color\n" +
                    "\t  ,ev.Abreviacion\n" +
                    "      ,v.CandadoOficial\n" +
                    "      ,v.Identificador\n" +
                    "      , CAST( v.FechaRegistro as VARCHAR(50)) as FechaRegistro\n" +
                    "      ,v.HoraRegistro \n" +
                    "\t  ,(select top 1 (select cs.Sucursal from CatSucursales cs " +
                    "where cs.IdSucursal = pi.IdSucursalReceptora)" +
                    " from ProInformePQ pi where pi.IdViaje = v.IdViaje) as SucursalReceptora\n" +
                    "     \n" +
                    "\t\tFROM ProViajesPQ v inner join CatSucursales s on v.IdSucursal = s.IdSucursal " +
                    "inner join CatEstatusViajePQ ev on v.IdEstatusViaje= ev.IdEstatusViaje where v.IdEstatusViaje = "
                    + idEstatus + " ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            int idViaje;
            Date fecha;
            Time hora;
            while (rs.next()) {
                idViaje = 0;
                json = new JSONObject();
                idViaje = rs.getInt("IdViaje");
                fecha = rs.getDate("Fecha");
                hora = rs.getTime("Hora");
                json.put("m_nIdViaje", idViaje);
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sSucursal", rs.getString("Sucursal"));
                json.put("m_sFolioViaje", rs.getString("FolioViaje"));
                json.put("m_sNumViajeCliente", rs.getString("NumViajeCliente"));
                json.put("m_dFecha", fecha);
                json.put("m_tHora", hora);
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sCandadoOficial", rs.getString("CandadoOficial"));
                json.put("m_sIdentificador", rs.getString("Identificador"));
                json.put("m_sSucursalReceptora", rs.getString("SucursalReceptora"));
                json.put("m_sFechaHora", DateFormatUtils.format(fecha, "yyyy-MM-dd") + " " +
                        DateFormatUtils.format(hora, "HH:mm:ss"));
                json.put("m_dFechaRegistro", rs.getDate("FechaRegistro"));
                json.put("m_tHoraRegistro", rs.getTime("HoraRegistro"));
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sEstatus", rs.getString("Estatus"));
                json.put("m_sDescripcion", rs.getString("Color"));
                json.put("m_sAbreviacion", rs.getString("Abreviacion"));
                json.put("m_arrInformes", viajesService.getListadoByViajeId(idViaje, jdbcConnection));
                array.put(json);
            }
            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Viajes/Eliminar/{idViaje}/{idEstatus}")
    public ResponseEntity<?> getListadoEstatusViajes(@PathVariable("idViaje") int idViaje,
                                                     @PathVariable("idEstatus") int idEstatus,
                                                     @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProViajeEliminarPQ " + idViaje + "";
            if (idEstatus != 10) {
                return ResponseEntity.status(500).body("Para eliminar debe estar cancelado");
            }
            Statement statement = jdbcConnection.createStatement();
            int r = statement.executeUpdate(query);

            return ResponseEntity.ok("Registro eliminado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Viajes/Agregar")
    public ResponseEntity<?> agregarViaje(@RequestBody ViajeRequest viaje, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                viaje.setLicenciaPermisionario((viaje.getLicenciaPermisionario() == null ? null : "'"
                        + viaje.getLicenciaPermisionario() + "'"));
                viaje.setNombrePermisionario((viaje.getNombrePermisionario() == null ? null : "'"
                        + viaje.getNombrePermisionario() + "'"));
                viaje.setFechaVigenciaPermisionario((viaje.getFechaVigenciaPermisionario() == null ? null : "'"
                        + viaje.getFechaVigenciaPermisionario() + "'"));
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(viaje);
                String query = "EXEC usp_ProViajeTriggerAgregarPQ '" + json + "'";
                statement.executeUpdate(query);

                return ResponseEntity.ok("Registro agregado con éxito");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al agregar el registro. Intente más tarde.");
        }
    }

    @PutMapping("/Viajes/Modificar")
    public ResponseEntity<?> modificarViaje(@RequestBody ViajeRequest viaje, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                viaje.setLicenciaPermisionario((viaje.getLicenciaPermisionario() == null ? null : "'"
                        + viaje.getLicenciaPermisionario() + "'"));
                viaje.setNombrePermisionario((viaje.getNombrePermisionario() == null ? null : "'"
                        + viaje.getNombrePermisionario() + "'"));
                viaje.setFechaVigenciaPermisionario((viaje.getFechaVigenciaPermisionario() == null ? null : "'"
                        + viaje.getFechaVigenciaPermisionario() + "'"));
                String query = "EXEC usp_ProViajeModificarPQ " + viaje.getM_nIdViaje() + "," + viaje.getM_nIdSucursal()
                        + ", '" + viaje.getM_sNumViajeCliente() + "'" + ",null,null, '" + viaje.getM_sFecha() + "','"
                        + viaje.getM_sHora() + "'"
                        + "," + viaje.getM_nIdEstatusViaje() + ",'" + viaje.getM_sCandadoOficial() + "','"
                        + viaje.getM_sIdentificador() + "'"
                        + "," + viaje.getM_nIdUsuarioCancelacion() + "," + viaje.getIdUnidad() + ","
                        + viaje.getM_nIdOrigen()
                        + "," + viaje.getM_nIdDestino() + "," + viaje.getIdRemolque1() + ", " + viaje.getIdRemolque2()
                        + ", " + viaje.getIdDolly() + "," + viaje.getM_nIdRuta() + ","
                        + (viaje.isEsOperadorPermisionario() ? 1 : 0)
                        + ", " + viaje.getLicenciaPermisionario() + "," + viaje.getNombrePermisionario() + ","
                        + viaje.getFechaVigenciaPermisionario() + "," + viaje.getIdOperador();
                statement.executeUpdate(query);
                for (InformeRequest informe : viaje.getM_arrInformes()) {
                    informe.setM_nIdViaje(viaje.getM_nIdViaje());
                    informe.setM_nIdUnidad(viaje.getIdUnidad());
                    informe.setM_nIdOperador(viaje.getIdOperador());
                    informe.setM_nIdRemolque1(viaje.getIdRemolque1());
                    informe.setM_nIdRemolque2(viaje.getIdRemolque2());
                    informe.setM_nIdDolly(viaje.getIdDolly());

                    query = "SELECT IdCiudadOrigen, IdCiudadDestino FROM ProInformePQ WHERE IdInforme ="
                            + informe.getM_nIdInforme();
                    ResultSet rs = statement.executeQuery(query);
                    JSONObject json = UtilFuctions.convertObject(rs);
                    informe.setM_nIdCiudadOrigen(json.getInt("IdCiudadOrigen"));
                    informe.setM_nIdCiudadDestino(json.getInt("IdCiudadDestino"));
                    query = "UPDATE ProInformePQ SET IdViaje=" + informe.getM_nIdViaje() + ", IdUnidad = "
                            + informe.getM_nIdUnidad() + ", IdOperador = " + informe.getM_nIdOperador()
                            + ",IdRemolque1 = " + informe.getM_nIdRemolque1() + ", IdRemolque2 = "
                            + informe.getM_nIdRemolque2() + ", IdDolly = " + informe.getM_nIdDolly()
                            + ",IdEstatusInforme=case when IdEstatusInforme = 5 then 6 else IdEstatusInforme end " +
                            "WHERE IdInforme=" + informe.getM_nIdInforme();
                    statement.executeUpdate(query);
                    query = "EXEC usp_ProViajeDetalleParadasAgregarPQ "
                            + viaje.getM_nIdViaje()
                            + "," + informe.getM_nIdInforme() + "," + viaje.getIdOperador()
                            + "," + viaje.getIdUnidad()
                            + ", " + viaje.getCvr1() + ", " + viaje.getCvr2()
                            + ",'" + viaje.getReferencia()
                            + "'," + informe.getM_nDestinoSeleccionado();
                    statement.executeUpdate(query);
                }
                query = "EXEC usp_ProViajeBorrarInformesPQ " + viaje.getM_nIdViaje();
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se guardaron los cambios con éxito");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios. Intente más tarde.");
        }
    }

    @PostMapping("/Viajes/AgregarSalida")
    public ResponseEntity<?> agregarSalidaViaje(@RequestBody ViajeSalidaRequest salida,
                                                @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "SELECT IdViajeCompuesto FROM ProViajesPQ WHERE IdViaje = " + salida.getM_nIdViaje();
                ResultSet rs = statement.executeQuery(query);
                JSONObject viajeJson = UtilFuctions.convertObject(rs);

                //Se agrega a la tabla 'ProViajeSalidaPQ' el registro de una salida y se actualiza el estatus de la unidad
                query = "EXEC usp_ProViajeSalidaAgregarPQ " + salida.getM_nIdViaje() + "," + salida.getM_nCV1Km() + ","
                        + salida.getM_nCV2Km() + "," + salida.getM_nCV1Millas() + "," + salida.getM_nCV2Millas() + ","
                        + (salida.isM_bCV1Estatus() ? 1 : 0) + "," + (salida.isM_bCV2Estatus() ? 1 : 0) + ",'"
                        + salida.getM_dFechaSalida() + "','" + salida.getM_tHoraSalida() + "'" + ","
                        + salida.getM_nIdEstatusSalida() + "," + salida.getM_nKmViaje() + ","
                        + salida.getM_nMillasViaje() + ",'" + salida.getM_sMotivoRetraso() + "'";
                statement.executeUpdate(query);
                //después se obtiene y se guarda el id del registro de salida, más adelante se utilizará
                query = "SELECT IDENT_CURRENT( 'ProViajeSalidaPQ' ) as id";
                rs = statement.executeQuery(query);
                JSONObject genericJson = UtilFuctions.convertObject(rs);
                salida.setM_nIdViajeSalida(genericJson.getInt("id"));

                //registro en bitácora de seguimiento
                query = "insert into BitacoraSeguimientoPQ(IdRegistro, Fecha, Hora, Descripcion, Proceso)\n" +
                        "    (select IdGuia,PIP.FechaSalida,PIP.HoraSalida,'El paquete se encuentra de camino a ' +\n" +
                        "            (SELECT OrigenDestino FROM CatOrigenesDestinos \n" +
                        "            where IdOrigenDestino = PVDPP.IdUbicacionDestino) + ' por el operador ' + (" +
                        "Select o.Nombres from CatOperadores o where o.IdOperador = PIP.IdOperador), 3 \n" +
                        "    from ProGuiaPQ g\n" +
                        "              inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                        "              inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme\n" +
                        "     where g.IdInforme in (select pi.IdInforme from ProInformePQ pi where PVDPP.IdViaje = " +
                        salida.getM_nIdViaje() + " and pi.IdUbicacionActual = " + salida.getM_nIdCiudadOrigen() +
                        " and IdEstatusInforme in (5,6) ))";
                statement.executeUpdate(query);

                //obtenemos las guias del viaje y embarques para enviar correos de notificación
                query = "select IdGuia, IdEmbarque\n" +
                        "from ProGuiaPQ g\n" +
                        "         inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                        "         inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme and " +
                        "PVDPP.IdViaje = PIP.IdViaje\n where g.IdInforme in (select pi.IdInforme " +
                        "from ProInformePQ pi where  pi.IdViaje = " + salida.getM_nIdViaje()
                        + " and pi.IdUbicacionActual = " + salida.getM_nIdCiudadOrigen()
                        + " and IdEstatusInforme in (5,6) )";
                rs = statement.executeQuery(query);
                EnvioCorreosRequest correosRequest = new EnvioCorreosRequest();
                correosRequest.setMensaje("¡Tu paquete se encuentra en camino!");
                while (rs.next()) {
                    embarqueRest.enviarNotificacion(rfc, rs.getInt("IdEmbarque"), correosRequest);
                }

                //actualiza estatus de informe y fecha de salida
                query = "UPDATE ProInformePQ SET FechaSalida = '" + salida.getM_dFechaSalida() + "', HoraSalida = '"
                        + salida.getM_tHoraSalida() + "'"
                        + ", IdEstatusInforme = 7 where IdViaje = " + salida.getM_nIdViaje()
                        + " and IdUbicacionActual = " + salida.getM_nIdCiudadOrigen()
                        + " and IdEstatusInforme in (5,6,7) ";
                statement.executeUpdate(query);

                query = "UPDATE ProViajeDetalleParadasPQ SET IdSalida = " + salida.getM_nIdViajeSalida()
                        + " where IdViaje=" + salida.getM_nIdViaje()
                        + " and IdInforme in (select pi.IdInforme from ProInformePQ pi where  pi.IdViaje = "
                        + salida.getM_nIdViaje() + " and pi.IdUbicacionActual = " + salida.getM_nIdCiudadOrigen()
                        + " and IdEstatusInforme in (5,6,7) )";
                statement.executeUpdate(query);

                //se actualiza estatus de la guia
                query = "UPDATE ProGuiaPQ  SET IdEstatusGuia = 6 where IdInforme in " +
                        "(select pi.IdInforme from ProInformePQ pi where  pi.IdViaje = " + salida.getM_nIdViaje()
                        + " and pi.IdUbicacionActual = " + salida.getM_nIdCiudadOrigen()
                        + " and IdEstatusInforme in (5,6,7) )";
                statement.executeUpdate(query);

                query = "UPDATE ProViajeTrayectosPQ SET IdSalida = " + salida.getM_nIdViajeSalida()
                        + " where IdViajeTrayecto =" + salida.getM_nIdRuta();
                statement.executeUpdate(query);

                //se actualiza estatus de viaje
                query = "UPDATE ProViajesPQ SET IdEstatusViaje = 5 where IdViaje = " + salida.getM_nIdViaje();
                statement.executeUpdate(query);

                //se actualiza estatus de embarque
                query = "UPDATE ProEmbarquePQ SET IdEstatusEmbarque = 19, FechaSalida = '" + salida.getM_dFechaSalida()
                        + "' where IdEmbarque in "
                        + "(SELECT IdEmbarque  FROM ProGuiaPQ pg WHERE pg.IdInforme in " +
                        "(select pi.IdInforme from ProInformePQ pi where pi.IdViaje = " + salida.getM_nIdViaje()
                        + " and pi.IdUbicacionActual = " + salida.getM_nIdCiudadOrigen()
                        + " and IdEstatusInforme in (5,6,7)))";
                statement.executeUpdate(query);

                query = "select case when count(*) = (select count(*) from ProViajeTrayectosPQ vt where vt.IdViaje = "
                        + salida.getM_nIdViaje() + ") then 1 else 0 end as terminarViaje from ProViajeTrayectosPQ i " +
                        "where i.IdViaje = " + salida.getM_nIdViaje() + " and ISNULL(i.IdSalida,0) != 0";
                rs = statement.executeQuery(query);
                genericJson = UtilFuctions.convertObject(rs);
                if (genericJson.getInt("terminarViaje") == 1) {
                    query = "UPDATE ProViajeDetalleParadasPQ SET IdSalida = " + salida.getM_nIdViajeSalida()
                            + " where IdViaje = " + salida.getM_nIdViaje()
                            + " and IdInforme in (select pi.IdInforme from ProInformePQ pi where  pi.IdViaje = "
                            + salida.getM_nIdViaje() + " and pi.IdEstatusInforme = 6)";
                    statement.executeUpdate(query);

                    query = "UPDATE ProGuiaPQ  SET IdEstatusGuia = 6 where IdInforme in " +
                            "(select pi.IdInforme from ProInformePQ pi where pi.IdViaje = " + salida.getM_nIdViaje() +
                            "  and pi.IdEstatusInforme = 6)";
                    statement.executeUpdate(query);

                    query = "insert into BitacoraSeguimientoPQ(IdRegistro,Fecha,Hora,Descripcion,Proceso) (select " +
                            "IdGuia, PIP.FechaSalida, PIP.HoraSalida, 'El paquete se encuentra de camino a ' +\n" +
                            "(SELECT OrigenDestino FROM CatOrigenesDestinos where IdOrigenDestino = " +
                            "PVDPP.IdUbicacionDestino) + ' por el operador '\n + (Select o.Nombres " +
                            "from CatOperadores o where o.IdOperador = PIP.IdOperador ),3\n from ProGuiaPQ g\n" +
                            "inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                            "inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme and " +
                            "PVDPP.IdViaje = PIP.IdViaje\n where g.IdInforme in  " +
                            "(select pi.IdInforme from ProInformePQ pi where pi.IdViaje = " + salida.getM_nIdViaje()
                            + "  and pi.IdEstatusInforme = 6))";
                    statement.executeUpdate(query);

                    query = "select IdGuia, IdEmbarque\n" +
                            "from ProGuiaPQ g\n" +
                            "inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                            "inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme " +
                            "and PVDPP.IdViaje = PIP.IdViaje\n" +
                            "where g.IdInforme in  (select pi.IdInforme from ProInformePQ pi where pi.IdViaje = "
                            + salida.getM_nIdViaje() + "  and pi.IdEstatusInforme = 6)";
                    rs = statement.executeQuery(query);
                    correosRequest.setMensaje("¡Tu paquete se encuentra en camino!");
                    while (rs.next()) {
                        embarqueRest.enviarNotificacion(rfc, rs.getInt("IdEmbarque"), correosRequest);
                    }

                    query = "UPDATE ProInformePQ SET FechaSalida = '" + salida.getM_dFechaSalida() + "', HoraSalida = '"
                            + salida.getM_tHoraSalida() + "', IdEstatusInforme = 7 where IdInforme in " +
                            "(select pi.IdInforme from ProInformePQ pi where pi.IdViaje = " + salida.getM_nIdViaje()
                            + "  and pi.IdEstatusInforme = 6)";
                    statement.executeUpdate(query);
                }

                //actualización de viaje en ERP
                query = "EXEC usp_ProViajeCompuestoSalidaPQ " + viajeJson.getInt("IdViajeCompuesto") + ","
                        + salida.getM_nIdCiudadOrigen() + ","+ salida.getM_nIdCiudadDestino() +" , '"
                        + salida.getM_dFechaSalida() + " " + salida.getM_tHoraSalida() + "'";
                statement.executeUpdate(query);

                //envío de correo por guías
                query = "SELECT GPQ.IdGuia as Medio, TPQ.IdViajeTrayecto, CASE WHEN TPQ.IdOrigen=IPQ.IdCiudadOrigen" +
                        " THEN 'true' ELSE 'false' end as Boo FROM ProViajesPQ as VPQ inner join ProInformePQ as IPQ" +
                        " on VPQ.IdViaje=IPQ.IdViaje inner join ProGuiaPQ as GPQ on GPQ.IdInforme=IPQ.IdInforme " +
                        "inner join ProViajeTrayectosPQ as TPQ on VPQ.IdViaje=TPQ.IdViaje where TPQ.IdSalida= " +
                        salida.getM_nIdViajeSalida() + " ";
                ResultSet obtenerIdGuia = statement.executeQuery(query);
                int sl = salida.getM_nIdViajeSalida();
                int gid = 0;
                boolean mandarCorreo = false;
                while (obtenerIdGuia.next()) {
                    gid = obtenerIdGuia.getInt("Medio");
                    mandarCorreo = obtenerIdGuia.getBoolean("Boo");
                }
                if (mandarCorreo) {
                    query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
                    statement = jdbcConnection.createStatement();
                    rs = statement.executeQuery(query);
                    try {
                        viajesService.enviarCorreoStatus(jdbcConnection, statement, gid, null, true,
                                rfc, "¡Su orden ha partido!");
                    } catch (Exception e) {
                        return ResponseEntity.ok("Se actualizó la información con éxito, pero no se logró " +
                                "mandar el correo electrónico al cliente.");
                    }
                }

                return ResponseEntity.ok(salida); //"Se actualizó la información con éxito"
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios. Intente más tarde.");
        }
    }

    @PostMapping("/Viajes/AgregarLlegada")
    public ResponseEntity<?> agregarLlegadaViaje(@RequestBody ViajeLlegadaRequest llegada,
                                                 @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "SELECT IdOrigen, IdDestino, IdViajeCompuesto FROM ProViajesPQ WHERE IdViaje = " + llegada.getM_nIdViaje();
                ResultSet rs = statement.executeQuery(query);
                JSONObject viajeJson = UtilFuctions.convertObject(rs);
                ViajeRequest viaje = new ViajeRequest();
                viaje.setM_nIdOrigen(viajeJson.getInt("IdOrigen"));
                viaje.setM_nIdDestino(viajeJson.getInt("IdDestino"));
                viaje.setIdViajeCompuesto(viajeJson.getInt("IdViajeCompuesto"));

                query = "EXEC usp_ProViajeLlegadaAgregarPQ " + llegada.getM_nIdViaje() + ","
                        + llegada.getM_nCV1Km() + ","
                        + llegada.getM_nCV2Km() + ","
                        + llegada.getM_nCV1Millas() + ","
                        + llegada.getM_nCV2Millas() + ",'"
                        + llegada.getM_dFechaSalida() + "','"
                        + llegada.getM_tHoraSalida() + "'" + ",'"
                        + llegada.getM_dFechaLlegada() + "','"
                        + llegada.getM_tHoraLlegada() + "',"
                        + llegada.getM_nKmViaje() + ","
                        + llegada.getM_nMillasViaje() + ","
                        + llegada.getM_nLiquidacion() + ","
                        + llegada.getM_nIdEstatusUnidad() + "," +
                        llegada.getM_nIdTipoCambio() + ","
                        + llegada.getM_nIdEstatusLlegada() + ",'"
                        + llegada.getM_sMotivoRetraso() + "'" + ","
                        + llegada.getM_nPesoCarga() + ","
                        + llegada.getM_nIdRuta();
                statement.executeUpdate(query);

                query = "SELECT IDENT_CURRENT( 'ProViajeLlegadaPQ' ) as id";
                rs = statement.executeQuery(query);
                JSONObject genericJson = UtilFuctions.convertObject(rs);
                llegada.setM_nIdViajeLlegada(genericJson.getInt("id"));

                query = "UPDATE ProInformePQ\n" +
                        "SET FechaLLegada      = '" + llegada.getM_dFechaLlegada() + "',\n" +
                        "    HoraLlegada       = '" + llegada.getM_tHoraLlegada() + "',\n" +
                        "    IdUbicacionActual = " + llegada.getM_nIdCiudadDestino() +"\n" +
                        "WHERE IdInforme IN (SELECT pi.IdInforme \n" +
                        "                    FROM ProInformePQ pi INNER JOIN ProViajeDetalleParadasPQ pv ON pv.IdInforme = pi.IdInforme\n" +
                        "                    WHERE pi.IdViaje = " + llegada.getM_nIdViaje() + " AND pv.IdUbicaciónActualInforme = " + llegada.getM_nIdCiudadOrigen() + ")";
                statement.executeUpdate(query);

                query = "UPDATE ProGuiaPQ\n" +
                        "SET IdEstatusGuia =\n" +
                        "        case\n" +
                        "            when (pe.EntregaEnSucursal = 1 and pe.IdCiudadDestino = " + llegada.getM_nIdCiudadDestino() + ") then 7\n" +
                        "            WHEN (pe.IdCiudadDestino = " + llegada.getM_nIdCiudadDestino() + " and pe.EntregaEnSucursal = 0) THEN 14\n" +
                        "            else 6\n" +
                        "            end\n" +
                        "from ProGuiaPQ pg\n" +
                        "     inner join ProEmbarquePQ pe on pe.IdEmbarque = pg.IdEmbarque\n" +
                        "where pg.IdInforme in (select pi.IdInforme from ProInformePQ pi\n" +
                        "                            inner join ProViajeDetalleParadasPQ pv " +
                        "on pv.IdInforme = pi.IdInforme\n" +
                        "                       where pv.IdViaje = " + llegada.getM_nIdViaje() +
                        " and pv.IdUbicacionDestino = " + llegada.getM_nIdCiudadDestino() + ")";
                statement.executeUpdate(query);

                query = "insert into BitacoraSeguimientoPQ(IdRegistro, Fecha, Hora, Descripcion, Proceso)\n" +
                        "    (select IdGuia,\n" +
                        "            PIP.FechaSalida, PIP.HoraSalida,\n" +
                        "            'El paquete llegó a la sucursal de ' +\n" +
                        "            (SELECT OrigenDestino FROM CatOrigenesDestinos where IdOrigenDestino = " +
                        "PVDPP.IdUbicacionDestino) +\n" +
                        "            '.', 3\n" +
                        "     from ProGuiaPQ g\n" +
                        "          inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                        "          inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme\n" +
                        "     where g.IdInforme in\n" +
                        "           (select pi.IdInforme from ProInformePQ pi\n" +
                        "             inner join ProViajeDetalleParadasPQ pv on pv.IdInforme = pi.IdInforme\n" +
                        "            where PVDPP.IdViaje = " + llegada.getM_nIdViaje()
                        + " and pv.IdUbicacionDestino = " + llegada.getM_nIdCiudadDestino() + "))";
                statement.executeUpdate(query);

                query = "select IdGuia, IdEmbarque\n" +
                        "from ProGuiaPQ g\n" +
                        "         inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                        "         inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme and" +
                        " PVDPP.IdViaje = PIP.IdViaje\n" +
                        "where g.IdInforme in (select pi.IdInforme from ProInformePQ pi where  pi.IdViaje = "
                        + llegada.getM_nIdViaje() + " and pi.IdCiudadDestino = " + llegada.getM_nIdCiudadDestino() + ")";
                rs = statement.executeQuery(query);
                EnvioCorreosRequest correosRequest = new EnvioCorreosRequest();
                correosRequest.setMensaje("¡Tu paquete llegó a su destino!");
                while (rs.next()) {
                    embarqueRest.enviarNotificacion(rfc, rs.getInt("IdEmbarque"), correosRequest);
                }

                query = "UPDATE ProEmbarqueTrayectosPQ\n" +
                        "set Estatus = 0\n" +
                        "where Estatus = 1\n" +
                        "  and IdOrigen = " + llegada.getM_nIdCiudadOrigen() + "\n" +
                        "  AND IdDestino = " + llegada.getM_nIdCiudadDestino() + "\n" +
                        "  and IdEmbarque in (SELECT IdEmbarque\n" +
                        "                     FROM ProGuiaPQ pg\n" +
                        "                     WHERE pg.IdInforme in (select pi.IdInforme from ProInformePQ pi " +
                        "where pi.IdViaje = " + llegada.getM_nIdViaje() + "))";
                statement.executeUpdate(query);

                query = "UPDATE ProViajeTrayectosPQ SET IdLlegada = " + llegada.getM_nIdViajeLlegada() +
                        " where IdViajeTrayecto =" + llegada.getM_nIdRuta();
                statement.executeUpdate(query);

                query = "UPDATE ProEmbarquePQ\n" +
                        "SET IdEstatusEmbarque = 20,\n" +
                        "    FechaLlegada      = '" + llegada.getM_dFechaLlegada() + "'\n" +
                        "where IdEmbarque in (SELECT IdEmbarque FROM ProGuiaPQ pg WHERE pg.IdInforme in \n" +
                        "                               (select pi.IdInforme from ProInformePQ pi\n" +
                        "                                     inner join ProViajeDetalleParadasPQ pv on " +
                        "pv.IdInforme = pi.IdInforme\n" +
                        "                                where pv.IdViaje = " + llegada.getM_nIdViaje() +
                        " and pv.IdUbicacionDestino = " + llegada.getM_nIdCiudadDestino() + "))";
                statement.executeUpdate(query);

                query = "UPDATE ProInformePQ\n" +
                        "SET IdEstatusInforme = case when IdCiudadDestino = " + llegada.getM_nIdCiudadDestino()
                        + " then 8 else 7 end,\n" +
                        "    IdViaje          = case when IdCiudadDestino = " + llegada.getM_nIdCiudadDestino()
                        + " then 0 else IdViaje end,\n" +
                        "    IdUbicacionActual=" + llegada.getM_nIdCiudadDestino() + ", FechaLLegada = '"
                        + llegada.getM_dFechaLlegada() + "'\n" +
                        "where IdInforme in \n" +
                        "    (select pi.IdInforme from ProInformePQ pi \n" +
                        "        inner join ProViajeDetalleParadasPQ pv on pv.IdInforme = pi.IdInforme \n" +
                        "    where pv.IdViaje = " + llegada.getM_nIdViaje() + " and pv.IdUbicacionDestino = "
                        + llegada.getM_nIdCiudadDestino() + ")";
                statement.executeUpdate(query);

                query = "select case when count(*) = (select count(*) from ProViajeTrayectosPQ vt where vt.IdViaje = "
                        + llegada.getM_nIdViaje() + ") then 1 else 0 end as terminarViaje " +
                        "from ProViajeTrayectosPQ i where i.IdViaje = " + llegada.getM_nIdViaje()
                        + " and ISNULL(i.IdLlegada,0) != 0";
                rs = statement.executeQuery(query);
                genericJson = UtilFuctions.convertObject(rs);

//                usp_ProViajeCompuestoLlegadaPQ SE ENCARGA DE REGISTRAR LA FECHA DE LLEGADA EN LOS TRAYECTOS DE VIAJE COMPUESTO CORRESPONDIENTES
//                TAMBIEN MARCA COMO TERMINADO EL VIAJE DEL ERP (NO COMPUESTO) EN CASO DE APLICAR
                query = "EXEC usp_ProViajeCompuestoLlegadaPQ " + viaje.getIdViajeCompuesto() + ","
                        + llegada.getM_nIdCiudadOrigen() + ","+ llegada.getM_nIdCiudadDestino()  + ", '"
                        + llegada.getM_dFechaLlegada() + " " + llegada.getM_tHoraLlegada() + "'";
                statement.executeUpdate(query);

                if (genericJson.getInt("terminarViaje") == 1) {
                    query = "UPDATE ProViajesPQ SET IdEstatusViaje = 6 where IdViaje = " + llegada.getM_nIdViaje();
                    statement.executeUpdate(query);

                    query = "UPDATE ProViajeDetalleParadasPQ SET IdLlegada = " + llegada.getM_nIdViajeLlegada()
                            + " where IdViaje=" + llegada.getM_nIdViaje() + " and IdInforme in " +
                            "(select pi.IdInforme from ProInformePQ pi where  pi.IdViaje = " + llegada.getM_nIdViaje()
                            + " and pi.IdEstatusInforme = 7)";
                    statement.executeUpdate(query);

//                    SE ACTUALIZA EL ESTATUS DEL VIAJE COMPUESTO (DEL ERP)
                    query = "update ProViajes set IdEstatuViaje=(select IdEstatuViaje from CatEstatusViaje where " +
                            "Estatus like '%TERMINADO%') where IdViaje= " + viaje.getIdViajeCompuesto();
                    statement.executeUpdate(query);

                    query = "insert into BitacoraSeguimientoPQ(IdRegistro, Fecha, Hora, Descripcion, Proceso)\n" +
                            "    (select IdGuia,\n" +
                            "            PIP.FechaSalida, PIP.HoraSalida,\n" +
                            "            'El paquete llegó a la sucursal de ' +\n" +
                            "            (SELECT OrigenDestino FROM CatOrigenesDestinos " +
                            "where IdOrigenDestino = PVDPP.IdUbicacionDestino) + '.',\n" +
                            "            3\n" +
                            "     from ProGuiaPQ g\n" +
                            "          inner join ProInformePQ PIP on g.IdInforme = PIP.IdInforme\n" +
                            "          inner join ProViajeDetalleParadasPQ PVDPP on g.IdInforme = PVDPP.IdInforme\n" +
                            "     where g.IdInforme in\n" +
                            "           (select pi.IdInforme from ProInformePQ pi where PVDPP.IdViaje = "
                            + llegada.getM_nIdViaje() + " and pi.IdEstatusInforme = 7))";
                    statement.executeUpdate(query);

                    query = "UPDATE ProInformePQ SET IdViaje = 0, IdEstatusInforme = 5, IdUbicacionActual=" + llegada.getM_nIdCiudadDestino()
                            + " where IdInforme in (select pi.IdInforme from ProInformePQ pi where pi.IdViaje = "
                            + llegada.getM_nIdViaje() + " and pi.IdEstatusInforme = 7)";
                    statement.executeUpdate(query);

                    query = "SELECT GPQ.IdGuia as Medio, CASE WHEN TPQ.IdDestino=IPQ.IdCiudadDestino " +
                            "THEN 'true' ELSE 'false' end as Boo " +
                            "FROM ProViajesPQ as VPQ " +
                            "inner join ProInformePQ as IPQ on VPQ.IdViaje=IPQ.IdViaje " +
                            "inner join ProGuiaPQ as GPQ on GPQ.IdInforme=IPQ.IdInforme " +
                            "inner join ProViajeTrayectosPQ as TPQ on VPQ.IdViaje=TPQ.IdViaje " +
                            "where TPQ.IdLlegada= " + llegada.getM_nIdViajeLlegada() + " ";
                    ResultSet obtenerDatosCorreo = statement.executeQuery(query);
                    int gid = 0;
                    boolean mandarCorreo = false;
                    while (obtenerDatosCorreo.next()) {
                        gid = obtenerDatosCorreo.getInt("Medio");
                        mandarCorreo = obtenerDatosCorreo.getBoolean("Boo");
                    }
                    if (mandarCorreo) {
                        query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
                        statement = jdbcConnection.createStatement();
                        rs = statement.executeQuery(query);
                        try {
                            viajesService.enviarCorreoStatus(jdbcConnection, statement, gid, null,
                                    true, rfc, "Su orden ha llegado a la sucursal de destino");
                        } catch (Exception e) {
                            return ResponseEntity.ok("Se actualizó la información con éxito, " +
                                    "pero no se logró mandar el correo electrónico al cliente.");
                        }
                    }

                }

                return ResponseEntity.ok(llegada); //ResponseEntity.ok("Se actualizó la información con éxito");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios. Intente más tarde.");
        }
    }

    @PostMapping("/Viajes/CancelarViaje/{idViaje}")
    public ResponseEntity<?> cancelarViaje(@PathVariable("idViaje") int idViaje,
                                           @RequestBody ViajeRequest request,
                                           @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                int result = viajesService.getCantidadAnticiposViaje(idViaje, jdbcConnection);
                if (result == -1) {
                    throw new Exception("Error al ejecutar query para validar anticipos.");
                }
                if (result > 0) {
                    throw new Exception("No se puede cancelar el viaje porque tiene con anticipos.");
                }

                String query = "EXEC usp_ProViajeCancelarPQ ?, ?, ?, ?";
                PreparedStatement preparedStatement = jdbcConnection.prepareStatement(query);
                preparedStatement.setInt(1, idViaje);
                preparedStatement.setInt(2, request.getM_nIdUsuarioCancelacion());
                preparedStatement.setString(3, request.getFechaCancelacion());
                preparedStatement.setString(4, request.getMotivoCancelacion());
                preparedStatement.executeUpdate();
                return ResponseEntity.ok("Viaje cancelado");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
