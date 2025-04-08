package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CubicarRequest;
import com.certuit.base.domain.response.CubicajeResponse;
import com.certuit.base.service.base.CubicarService;
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
public class UnidadesRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    CubicarService cubicarService;

    @GetMapping("/InventarioUnidades/GetListado")
    public ResponseEntity<?> getInventarioUnidades(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT pih.IdInventarioUnidad as m_nIdInventarioUnidad, ctu.TipoUnidad as m_sTipoUnidad," +
                    " pih.IdUnidad as m_nIdUnidad," +
                    " cu.Codigo as m_sCodigoUnidad," +
                    "ceu.Abreviacion,pih.IdEstatuUnidad as m_nIdEstatusUnidad,ceu.Color as m_sColor," +
                    "ceu.ColorLetra as m_sColorLetra,pih.Desde,cod.OrigenDestino AS m_sUbicacion," +
                    "ceu.Estatus as m_sEstatus,pih.IdViaje as m_nIdViaje, pih.IdCliente as m_nIdCliente, " +
                    "cu.Descripcion as m_sUnidad " +
                    "FROM ProInventarioUnidades AS pih " +
                    "LEFT JOIN CatUnidades AS cu ON pih.IdUnidad = cu.IdUnidad " +
                    "INNER JOIN CatTiposUnidades AS ctu ON cu.IdTipoUnidad = ctu.IdTipoUnidad " +
                    "INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad " +
                    "left JOIN CatOrigenesDestinos AS cod ON pih.IdUbicacion = cod.IdOrigenDestino " +
                    "WHERE cu.Activa=1 " +
                    "ORDER BY ctu.TipoUnidad,CodigoUnidad";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Unidades/GetListadoUltimaMilla/{id}")
    public ResponseEntity<?> getUnidadesUltimaMilla(@PathVariable("id") String id,
                                                    @RequestBody CubicarRequest request,
                                                    @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select  \n" +
                    "           cu.IdUnidad as m_nIdUnidad,  \n" +
                    "                ctu.TipoUnidad as m_sTipoUnidad,  \n" +
                    "                cu.Codigo as m_sCodigo,  \n" +
                    "                cu.Descripcion as m_sDescripcion,  \n" +
                    "                cu.Activa as m_bActivo,  \n" +
                    "                isnull(co.NumeroOperador,0) as m_nNumeroOperador,  \n" +
                    "                isnull(co.Nombres,'-') as m_sNombreOperador,  \n" +
                    "                cu.Placas as m_sPlacas,  \n" +
                    "                isnull(cu.IdOperador,0) as m_nIdOperador,\n" +
                    "       (case when ctu.Identificador = 1 or ctu.Identificador = 4 " +
                    "then 0 else 1 end) m_bAplicaRemolques,\n" +
                    "       (select case when (select count(*) from ParadaUltimaMillaPQ " +
                    "where m_nIdOperador=co.IdOperador and Completada=0 and Activa=1) >0 " +
                    "then CAST(1 AS BIT) else CAST(0 AS BIT) end) ocupado \n" +
                    "from CatUnidades cu " +
                    "   inner join  CatTiposUnidades ctu on cu.IdTipoUnidad =ctu.IdTipoUnidad\n" +
                    "   left join  CatOperadores co on cu.IdOperador = co.IdOperador " +
                    "where ctu.Identificador in (1,2) " +
                    "                 and cu.Activa = 1 " +
                    "                 and (co.Activo = 1 OR co.Activo is null ) " +
                    "                 and cu.IdSucursal = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonArray = UtilFuctions.convertArray(rs);
            try {
                if (!request.getGuias().isEmpty()) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getInt("m_bAplicaRemolques") == 0) {
                            CubicajeResponse cubicajeResponse = cubicarService.cubicarGuia(jdbcConnection,
                                    jsonObject.getInt("m_nIdUnidad"), -1, request.getGuias());
                            if (cubicajeResponse.isSuccess()) {
                                jsonObject.put("utilizacion", cubicajeResponse.getUtilizacion());
                            } else {
                                jsonObject.put("utilizacion", 0);
                            }
                        } else {
                            jsonObject.put("utilizacion", 0);
                        }
                        jsonArray.put(i, jsonObject);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return ResponseEntity.ok(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
    @GetMapping("/Unidades/GetListadoUltimaMilla/{id}")
    public ResponseEntity<?> getUnidadesUltimaMilla(@PathVariable("id") String id,
                                                    @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select  \n" +
                    "           cu.IdUnidad as m_nIdUnidad,  \n" +
                    "                ctu.TipoUnidad as m_sTipoUnidad,  \n" +
                    "                cu.Codigo as m_sCodigo,  \n" +
                    "                cu.Descripcion as m_sDescripcion,  \n" +
                    "                cu.Activa as m_bActivo,  \n" +
                    "                isnull(co.NumeroOperador,0) as m_nNumeroOperador,  \n" +
                    "                isnull(co.Nombres,'-') as m_sNombreOperador,  \n" +
                    "                cu.Placas as m_sPlacas,  \n" +
                    "                isnull(cu.IdOperador,0) as m_nIdOperador,\n" +
                    "       (case when ctu.Identificador = 1 or ctu.Identificador = 4 " +
                    "then 0 else 1 end) m_bAplicaRemolques, \n" +
                    "       (select case when (select count(*) from ParadaUltimaMillaPQ " +
                    "where m_nIdOperador=co.IdOperador and Completada=0 and Activa=1) >0 then CAST(1 AS BIT) " +
                    "else CAST(0 AS BIT) end) ocupado \n" +
                    "from CatUnidades cu " +
                    "   inner join  CatTiposUnidades ctu on cu.IdTipoUnidad =ctu.IdTipoUnidad\n" +
                    "   left join  CatOperadores co on cu.IdOperador = co.IdOperador " +
                    "where ctu.Identificador in (1,2) " +
                    "                 and cu.Activa = 1 " +
                    "                 and (co.Activo = 1 OR co.Activo is null ) " +
                    "                 and cu.IdSucursal = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
    @GetMapping("/Unidades/GetListadoInformes")
    public ResponseEntity<?> getUnidadesInforme(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select " +
                    "cu.IdUnidad as m_nIdUnidad, " +
                    "ctu.TipoUnidad as m_sTipoUnidad, " +
                    "cu.Codigo as m_sCodigo, " +
                    "cu.Descripcion as m_sDescripcion, " +
                    "cu.Activa as m_bActivo, " +
                    "cu.Placas as m_sPlacas, " +
                    "ctu.Identificador as m_nIdentificador " +
                    "from CatUnidades cu inner join  CatTiposUnidades ctu on cu.IdTipoUnidad =ctu.IdTipoUnidad " +
                    "where cu.IdTipoUnidad in (1,12,30)";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Unidades/GetListado")
    public ResponseEntity<?> obtenerUnidades(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select cu.IdUnidad                  as m_nIdUnidad,\n" +
                    "                       cu.Placas                    as m_sPlacas,\n" +
                    "                       ctu.Identificador            as m_nIdentificador,\n" +
                    "                       cu.IdTipoUnidad              as m_nIdTipoUnidad,\n" +
                    "                       ctu.TipoUnidad               as m_sTipoUnidad,\n" +
                    "                       cu.Codigo                    as m_sCodigo,\n" +
                    "                       cu.Descripcion               as m_sDescripcion,\n" +
                    "                       pih.IdEstatuUnidad           as IdEstatusUnidad,\n" +
                    "                       ceu.Estatus                  as EstatusUnidad,\n" +
                    "                       ceu.Color                    as ColorEstatus,\n" +
                    "                       ceu.ColorLetra               as ColorEstatusLetra,\n" +
                    "                       cu.EsUnidadPermisionario     as EsUnidadPermisionario,\n" +
                    "                       cu.IdentificadorSatelital    as m_sIdentificadorSatelital,\n" +
                    "                       cu.Activa                    as m_bActivo,\n" +
                    "                       co.NumeroOperador            as m_nNumeroOperador,\n" +
                    "                       co.Nombres                   as m_sNombreOperador,\n" +
                    "                       cu.PlacasVencimiento         as m_sPlacasVencimiento,\n" +
                    "                       cu.IdOperador                as m_nIdOperador,\n" +
                    "                       cu.HorasTrabajadasMotorNoGPS as m_nHorasTrabajadasMotorNoGPS,\n" +
                    "                       cu.Odometro                  as m_nOdometro,\n" +
                    "                       ctu.ClaveAutotransporteFederal                 as m_ClaveSAT,\n" +
                    "                       case when ctu.Identificador = 2 then 1 else 0 end      as m_bAplicaRemolque\n" +
                    "                from CatUnidades cu\n" +
                    "                         inner join CatTiposUnidades ctu on cu.IdTipoUnidad = ctu.IdTipoUnidad\n" +
                    "                         left join CatOperadores co on cu.IdOperador = co.IdOperador\n" +
                    "                         inner join ProInventarioUnidades pih on pih.IdUnidad = cu.IdUnidad\n" +
                    "                         INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Unidades/GetListadoRemolques")
    public ResponseEntity<?> obtenerRemolques(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select cu.IdUnidad                  as m_nIdUnidad,\n" +
                    "       cu.Placas                    as m_sPlacas,\n" +
                    "       cu.IdTipoUnidad              as m_nIdTipoUnidad,\n" +
                    "       ctu.TipoUnidad               as m_sTipoUnidad,\n" +
                    "       cu.Codigo                    as m_sCodigo,\n" +
                    "       cu.Descripcion               as m_sDescripcion,\n" +
                    "       pih.IdEstatuUnidad           as IdEstatusUnidad,\n" +
                    "       ceu.Estatus                  as EstatusUnidad,\n" +
                    "       ceu.Color                    as ColorEstatus,\n" +
                    "       ceu.ColorLetra               as ColorEstatusLetra,\n" +
                    "       cu.EsUnidadPermisionario     as EsUnidadPermisionario,\n" +
                    "       cu.IdentificadorSatelital    as m_sIdentificadorSatelital,\n" +
                    "       cu.Activa                    as m_bActivo,\n" +
                    "       co.NumeroOperador            as m_nNumeroOperador,\n" +
                    "       co.Nombres                   as m_sNombreOperador,\n" +
                    "       cu.PlacasVencimiento         as m_sPlacasVencimiento,\n" +
                    "       cu.IdOperador                as m_nIdOperador,\n" +
                    "       cu.HorasTrabajadasMotorNoGPS as m_nHorasTrabajadasMotorNoGPS,\n" +
                    "       cu.Odometro                  as m_nOdometro\n" +
                    "from CatUnidades cu\n" +
                    "         inner join CatTiposUnidades ctu on cu.IdTipoUnidad = ctu.IdTipoUnidad\n" +
                    "         left join CatOperadores co on cu.IdOperador = co.IdOperador\n" +
                    "         inner join ProInventarioUnidades pih on pih.IdUnidad = cu.IdUnidad\n" +
                    "         INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad " +
                    "where ctu.Identificador = 4 or ctu.Identificador = 3 or ctu.Identificador = 5";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/InventarioUnidades/GetByIdUnidad/{idUnidad}")
    public ResponseEntity<?> obtenerUnidadById(@PathVariable("idUnidad") int idUnidad, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT pih.IdInventarioUnidad as m_nIdInventarioUnidad, " +
                    "       ctu.TipoUnidad as m_sTipoUnidad,\n" +
                    "       pih.IdUnidad as m_nIdUnidad,\n" +
                    "       cu.Codigo as m_sCodigoUnidad,\n" +
                    "       ceu.Abreviacion," +
                    "       pih.IdEstatuUnidad as m_nIdEstatusUnidad," +
                    "       ceu.Color as m_sColor," +
                    "       ceu.ColorLetra as m_sColorLetra," +
                    "       pih.Desde as m_dDesde," +
                    "       cod.Descripcion AS m_sUbicacion,\n" +
                    "       pih.IdUnidad as m_nIdUnidad," +
                    "       ceu.Estatus as m_sEstatus," +
                    "       pih.IdViaje as m_nIdViaje," +
                    "       pih.IdCliente as m_nIdCliente\n" +
                    "FROM ProInventarioUnidades AS pih\n" +
                    "         LEFT JOIN CatUnidades AS cu ON pih.IdUnidad = cu.IdUnidad\n" +
                    "         INNER JOIN CatTiposUnidades AS ctu ON cu.IdTipoUnidad = ctu.IdTipoUnidad\n" +
                    "         INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad\n" +
                    "         left JOIN CatDetalleRutasPQ AS cod ON pih.IdUbicacion = cod.IdDetalleRuta where cu.IdUnidad =  " + idUnidad;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Unidades/ByTipoUnidad/{idTipoUnidad}")
    public ResponseEntity<?> obtenerUnidadesByTipoUnidad(@PathVariable("idTipoUnidad") int idTipoUnidad,
                                                         @RequestHeader("RFC") String rfc)
            throws Exception {
        ResultSet rs;
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "select" +
                        " cu.IdUnidad as m_nIdUnidad, " +
                        "ctu.TipoUnidad as m_sTipoUnidad," +
                        "cu.Codigo as m_sCodigo," +
                        "cu.Descripcion as m_sDescripcion," +
                        "cu.Activa as m_bActivo," +
                        "cu.Rentada as m_bRentada," +
                        "cu.Largo," +
                        "cu.Ancho," +
                        "cu.Alto," +
                        "cu.Capacidad as m_nCapacidad," +
                        " cu.Placas as m_sPlacas\n" +
                        "\t from CatUnidades cu inner join CatTiposUnidades ctu on cu.IdTipoUnidad =ctu.IdTipoUnidad " +
                        "where cu.IdTipoUnidad= " + idTipoUnidad;
                Statement statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Ocurrió un problema al listar las unidades");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Unidades/ByOperador/{idOperador}")
    public ResponseEntity<?> obtenerUnidadesByOperador(@PathVariable("idOperador") int idOperador,
                                                       @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select cu.IdUnidad                  as m_nIdUnidad,\n" +
                    "                       cu.Placas                    as m_sPlacas,\n" +
                    "                       ctu.Identificador            as m_nIdentificador,\n" +
                    "                       cu.IdTipoUnidad              as m_nIdTipoUnidad,\n" +
                    "                       ctu.TipoUnidad               as m_sTipoUnidad,\n" +
                    "                       cu.Codigo                    as m_sCodigo,\n" +
                    "                       cu.Descripcion               as m_sDescripcion,\n" +
                    "                       pih.IdEstatuUnidad           as IdEstatusUnidad,\n" +
                    "                       ceu.Estatus                  as EstatusUnidad,\n" +
                    "                       ceu.Color                    as ColorEstatus,\n" +
                    "                       ceu.ColorLetra               as ColorEstatusLetra,\n" +
                    "                       cu.EsUnidadPermisionario     as EsUnidadPermisionario,\n" +
                    "                       cu.IdentificadorSatelital    as m_sIdentificadorSatelital,\n" +
                    "                       cu.Activa                    as m_bActivo,\n" +
                    "                       co.NumeroOperador            as m_nNumeroOperador,\n" +
                    "                       co.Nombres                   as m_sNombreOperador,\n" +
                    "                       cu.PlacasVencimiento         as m_sPlacasVencimiento,\n" +
                    "                       cu.IdentificadorConvoy       as IdentificadorConvoy,\n" +
                    "                       cu.IdOperador                as m_nIdOperador,\n" +
                    "                       cu.HorasTrabajadasMotorNoGPS as m_nHorasTrabajadasMotorNoGPS,\n" +
                    "                       cu.Odometro                  as m_nOdometro,\n" +
                    "                       ctu.ClaveAutotransporteFederal                 as m_ClaveSAT,\n" +
                    "                       case when ctu.Identificador = 2 then 1 else 0 end as m_bAplicaRemolque,\n" +
                    "                       case when ceu.Estatus != 'DISPONIBLE' " +
                    "then CAST(1 AS BIT) else CAST(0 AS BIT) end as m_bDeshabilitado\n" +
                    "                from CatUnidades cu\n" +
                    "                         inner join CatTiposUnidades ctu on cu.IdTipoUnidad = ctu.IdTipoUnidad\n" +
                    "                         left join CatOperadores co on cu.IdOperador = co.IdOperador\n" +
                    "                         inner join ProInventarioUnidades pih on pih.IdUnidad = cu.IdUnidad\n" +
                    "                         INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad" +
                    " WHERE cu.IdOperador = " + idOperador + " AND cu.Activa = 1 AND ceu.IdEstatuUnidad = 1 ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /**
     * Esta funcion solo trae los remolques que estan disponibles y se buscan segun el identificadr del convoy que se seleccino en la unidad
     */
    @GetMapping("/Unidades/ByConvoy/{idConvoy}")
    public ResponseEntity<?> obtenerRemolquesByConvoy(@PathVariable("idConvoy") String idConvoy,
                                                      @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select cu.IdUnidad                  as m_nIdUnidad,\n" +
                    "       cu.Placas                    as m_sPlacas,\n" +
                    "       cu.IdTipoUnidad              as m_nIdTipoUnidad,\n" +
                    "       ctu.TipoUnidad               as m_sTipoUnidad,\n" +
                    "       cu.Codigo                    as m_sCodigo,\n" +
                    "       cu.Descripcion               as m_sDescripcion,\n" +
                    "       pih.IdEstatuUnidad           as IdEstatusUnidad,\n" +
                    "       ceu.Estatus                  as EstatusUnidad,\n" +
                    "       ceu.Color                    as ColorEstatus,\n" +
                    "       ceu.ColorLetra               as ColorEstatusLetra,\n" +
                    "       cu.EsUnidadPermisionario     as EsUnidadPermisionario,\n" +
                    "       cu.IdentificadorSatelital    as m_sIdentificadorSatelital,\n" +
                    "       cu.Activa                    as m_bActivo,\n" +
                    "       co.NumeroOperador            as m_nNumeroOperador,\n" +
                    "       co.Nombres                   as m_sNombreOperador,\n" +
                    "       cu.PlacasVencimiento         as m_sPlacasVencimiento,\n" +
                    "       cu.IdOperador                as m_nIdOperador,\n" +
                    "       cu.HorasTrabajadasMotorNoGPS as m_nHorasTrabajadasMotorNoGPS,\n" +
                    "       cu.Odometro                  as m_nOdometro,\n" +
                    "       case when ceu.Estatus != 'DISPONIBLE' then CAST(1 AS BIT) else CAST(0 AS BIT) end " +
                    "as m_bDeshabilitado\n" +
                    "from CatUnidades cu\n" +
                    "         inner join CatTiposUnidades ctu on cu.IdTipoUnidad = ctu.IdTipoUnidad\n" +
                    "         left join CatOperadores co on cu.IdOperador = co.IdOperador\n" +
                    "         inner join ProInventarioUnidades pih on pih.IdUnidad = cu.IdUnidad\n" +
                    "         INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad " +
                    "where (ctu.Identificador = 4 or ctu.Identificador = 3 or ctu.Identificador = 5) " +
                    "AND cu.IdentificadorConvoy = '" + idConvoy + "' AND cu.Activa = 1 AND ceu.IdEstatuUnidad = 1 ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Unidades/AsignarOperador/{idUnidad}/{idOperador}")
    public ResponseEntity<?> asignarOperador(@PathVariable("idUnidad") int idUnidad,
                                             @PathVariable("idOperador") int idOperador,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatAsignarOperadorUnidadPQ " + idUnidad + ", " + idOperador;
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);
            return ResponseEntity.ok("Asignado(a) Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al asignar el operador. Intente más tarde.");
        }
    }

/** Esta funcion es la misma que la de arriba, solo que trae todos los remolques sin importar si su estatus es disponible o no */
//    @GetMapping("/Unidades/ByConvoy/{idConvoy}")
//    public ResponseEntity<?> obtenerRemolquesByConvoy(@PathVariable("idConvoy") String idConvoy,@RequestHeader("RFC") String rfc) throws SQLException, Exception {
//
//        try (HikariDataSource datasorce = dbConection.getconnection(rfc)) {
           // Connection jdbcConnection = datasorce.getConnection();
//        String query = "select cu.IdUnidad                  as m_nIdUnidad,\n" +
//                "       cu.Placas                    as m_sPlacas,\n" +
//                "       cu.IdTipoUnidad              as m_nIdTipoUnidad,\n" +
//                "       ctu.TipoUnidad               as m_sTipoUnidad,\n" +
//                "       cu.Codigo                    as m_sCodigo,\n" +
//                "       cu.Descripcion               as m_sDescripcion,\n" +
//                "       pih.IdEstatuUnidad           as IdEstatusUnidad,\n" +
//                "       ceu.Estatus                  as EstatusUnidad,\n" +
//                "       ceu.Color                    as ColorEstatus,\n" +
//                "       ceu.ColorLetra               as ColorEstatusLetra,\n" +
//                "       cu.EsUnidadPermisionario     as EsUnidadPermisionario,\n" +
//                "       cu.IdentificadorSatelital    as m_sIdentificadorSatelital,\n" +
//                "       cu.Activa                    as m_bActivo,\n" +
//                "       co.NumeroOperador            as m_nNumeroOperador,\n" +
//                "       co.Nombres                   as m_sNombreOperador,\n" +
//                "       cu.PlacasVencimiento         as m_sPlacasVencimiento,\n" +
//                "       cu.IdOperador                as m_nIdOperador,\n" +
//                "       cu.HorasTrabajadasMotorNoGPS as m_nHorasTrabajadasMotorNoGPS,\n" +
//                "       cu.Odometro                  as m_nOdometro\n" +
//                "from CatUnidades cu\n" +
//                "         inner join CatTiposUnidades ctu on cu.IdTipoUnidad = ctu.IdTipoUnidad\n" +
//                "         left join CatOperadores co on cu.IdOperador = co.IdOperador\n" +
//                "         inner join ProInventarioUnidades pih on pih.IdUnidad = cu.IdUnidad\n" +
//                "         INNER JOIN CatEstatusUnidades AS ceu ON pih.IdEstatuUnidad = ceu.IdEstatuUnidad " +
//                "where (ctu.Identificador = 4 or ctu.Identificador = 3 or ctu.Identificador = 5) AND cu.IdentificadorConvoy = '" + idConvoy + "' AND cu.Activa = 1 ";
//
//        Statement statement = jdbcConnection.createStatement();
//        ResultSet rs = statement.executeQuery(query);
//
//        return ResponseEntity.ok(UtilFuctions.convertArray(rs).toString());
//
//    }

}
