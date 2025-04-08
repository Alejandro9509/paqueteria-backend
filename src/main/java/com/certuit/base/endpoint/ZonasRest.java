package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CodigoPostalRequest;
import com.certuit.base.domain.request.base.ZonaOperativaRequest;
import com.certuit.base.service.base.ZonasService;
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

@RestController
@RequestMapping(value = "/api")
public class ZonasRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    ZonasService zonasService;

    @GetMapping("/ZonaOperativa/GetByIdCodigoPostal/{id}")
    public ResponseEntity<?> getZonaOperativaByIdCodigoPostal(@PathVariable("id") int codigo,
                                                              @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT DISTINCT z.IdZona as m_nIdZona ," +
                    "CodigoZona as m_sCodigoZona," +
                    "IdSucursal as m_nIdSucursal," +
                    "z.IdEstado as m_sIdEstado," +
                    "Estado as m_sEstado," +
                    "z.CodigoMunicipio as m_sCodMunicipio," +
                    "z.Municipio as m_sMunicipio," +
                    "z.IdOrigenDestino as m_nIdOrigenDestino," +
                    "od.OrigenDestino as m_sOrigenDestino, " +
                    "z.AplicaEntrega as m_bAplicaEntrega " +
                    "FROM CatZonasOperativasCPPQ zcp " +
                    "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                    "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                    "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino " +
                    "WHERE cp.IdCodigoPostal =  " + codigo;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaOperativa/GetByCodigoPostal/{cp}")
    public ResponseEntity<?> getZonaOperativaByCodigoPostal(@PathVariable("cp") String codigo,
                                                            @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT DISTINCT z.IdZona as m_nIdZona ," +
                    "CodigoZona as m_sCodigoZona," +
                    "IdSucursal as m_nIdSucursal," +
                    "z.IdEstado as m_sIdEstado," +
                    "Estado as m_sEstado," +
                    "z.CodigoMunicipio as m_sCodMunicipio," +
                    "z.Municipio as m_sMunicipio," +
                    "z.IdOrigenDestino as m_nIdOrigenDestino," +
                    "od.OrigenDestino as m_sOrigenDestino, " +
                    "z.AplicaEntrega as m_bAplicaEntrega " +
                    "FROM CatZonasOperativasCPPQ zcp " +
                    "join CatZonasOperativasPQ z on zcp.IdZona = z.IdZona " +
                    "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                    "left join CatOrigenesDestinos od on od.IdOrigenDestino = z.IdOrigenDestino " +
                    "WHERE cp.CodigoPostal =  '" + codigo + "'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
    @GetMapping("/ZonaOperativa/GetColoniasCPs")
    public ResponseEntity<?> getColoniasCPs(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        String query = "SELECT DISTINCT MAX(IdCodigoPostal) IdCodigoPostal,MAX(z.IdZona) IdZona,CodigoPostal," +
                "Colonia,MAX(IdSucursal) IdSucursal,MAX(IdOrigenDestino) IdOrigenDestino FROM CatZonasOperativasPQ z\n" +
                "                         inner join CatZonasOperativasCPPQ zcp on zcp.IdZona=z.IdZona \n" +
                "                         inner join CatCodigosPostales cp on cp.IdCodigoPostal=zcp.IdCP\n" +
                "                where Colonia !=''\n" +
                "                GROUP BY CodigoPostal, Colonia\n" +
                "                order by CodigoPostal desc";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaOperativa/GetDatosUbicacionDestinatario/{id}")
    public ResponseEntity<?> getParametrosDestino(@PathVariable("id") int idGuia, @RequestHeader("RFC") String rfc)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "WITH params as (select c.CodigoMunicipio,c.IdCodigoPostal,c.IdEstado,p.IdPais," +
                    "e.CalleDestinatario,e.NoExtDestinatario " +
                    "from ProEmbarquePQ e " +
                    "inner join CatCodigosPostales c on e.IdCodigoPostalDestinatario=c.IdCodigoPostal\n" +
                    "    inner join CatEstados s on c.IdEstado = s.IdEstado " +
                    "inner join CatPaises p on s.IdPais = p.IdPais where e.IdGuia=" + idGuia + ")\n" +
                    "select distinct d.CodigoMunicipio,d.IdEstado,d.IdCodigoPostal,c.CodigoPostal,c.Colonia," +
                    "c.Localidad,d.IdPais,z.CodigoZona,CONCAT(d.CalleDestinatario,' ',d.NoExtDestinatario) " +
                    "domicilio,z.IdZona,z.Estado,c.Municipio,\n" +
                    "                ISNULL(z.AplicaEntrega,1) as m_bNoAplicaEntrega,CAST(CASE\n" +
                    "                WHEN  c.IdCodigoPostal in (select cz.IdCP from CatZonasOperativasCPPQ cz " +
                    "JOIN CatCodigosPostales cp on cz.IdCP = cp.IdCodigoPostal WHERE cz.IdZona = z.IdZona) THEN 0\n" +
                    "                ELSE 1\n" +
                    "    END AS BIT) AS codigoFueraDeZonaOperativa from params d\n" +
                    "    inner join CatCodigosPostales c on c.IdCodigoPostal=d.IdCodigoPostal\n" +
                    "    left join CatZonasOperativasCPPQ zcp on zcp.IdCP = c.IdCodigoPostal\n" +
                    "    left join CatZonasOperativasPQ z on z.IdZona = zcp.IdZona";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaOperativa/GetByIdOrigenDestino/{id}")
    public ResponseEntity<?> getZonaOperativaByOrigenDestino(@PathVariable("id") String codigo,
                                                             @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT zona.IdZona          as m_nIdZona,\n" +
                    "       zona.CodigoZona      as m_sCodigoZona,\n" +
                    "       zona.IdSucursal      as m_nIdSucursal,\n" +
                    "       sucursal.Sucursal    as m_sSucursal,\n" +
                    "       zona.IdEstado        as m_sIdEstado,\n" +
                    "       zona.Estado          as m_sEstado,\n" +
                    "       zona.CodigoMunicipio as m_sCodMunicipio,\n" +
                    "       zona.Municipio       as m_sMunicipio,\n" +
                    "       zona.IdOrigenDestino as m_nIdOrigenDestino,\n" +
                    "       COD.OrigenDestino    as m_sOrigenDestino\n" +
                    "FROM CatZonasOperativasPQ zona\n" +
                    "         left join CatOrigenesDestinos COD on zona.IdOrigenDestino = COD.IdOrigenDestino\n" +
                    "         left join CatSucursales sucursal on zona.IdSucursal = sucursal.IdSucursal\n" +
                    "WHERE zona.IdOrigenDestino =" + codigo;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaOperativa/GetListadoBySucursal/{id}")
    public ResponseEntity<?> getZonaOperativaBySucursal(@PathVariable("id") String codigo,
                                                        @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT zona.IdZona          as m_nIdZona,\n" +
                    "       zona.CodigoZona      as m_sCodigoZona,\n" +
                    "       zona.IdSucursal      as m_nIdSucursal,\n" +
                    "       sucursal.Sucursal    as m_sSucursal,\n" +
                    "       zona.IdEstado        as m_sIdEstado,\n" +
                    "       zona.Estado          as m_sEstado,\n" +
                    "       zona.CodigoMunicipio as m_sCodMunicipio,\n" +
                    "       zona.Municipio       as m_sMunicipio,\n" +
                    "       zona.IdOrigenDestino as m_nIdOrigenDestino,\n" +
                    "       COD.OrigenDestino    as m_sOrigenDestino\n" +
                    "FROM CatZonasOperativasPQ zona\n" +
                    "         left join CatOrigenesDestinos COD on zona.IdOrigenDestino = COD.IdOrigenDestino\n" +
                    "         left join CatSucursales sucursal on zona.IdSucursal = sucursal.IdSucursal\n" +
                    "WHERE zona.IdSucursal =" + codigo;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaTarifa/GetByIdCodigoPostal/{id}")
    public ResponseEntity<?> getZonaTarifa(@PathVariable("id") String codigo, @RequestHeader("RFC") String rfc)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT DISTINCT z.IdZona as m_nIdZona ,CodigoZona as m_sCodigoZona," +
                    "z.IdSucursal as m_nIdSucursal,s.OrigenDestino as m_sSucursal,z.IdEstado as m_sIdEstado," +
                    "Estado as m_sEstado,z.CodigoMunicipio as m_sCodMunicipio,z.Municipio as m_sMunicipio " +
                    "FROM CatZonasTarifasCPPQ zcp " +
                    "join CatZonasTarifasPQ z on zcp.IdZona = z.IdZona " +
                    "join CatCodigosPostales cp on cp.IdCodigoPostal = zcp.IdCP " +
                    "join CatOrigenesDestinos s on s.IdOrigenDestino = z.IdSucursal " +
                    "WHERE cp.CodigoPostal = '" + codigo + "'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaTarifa/GetById/{id}")
    public ResponseEntity<?> getZonaTarifa(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT  " +
                    " IdZona as m_nIdZona " +
                    " ,CodigoZona as m_sCodigoZona " +
                    " ,IdSucursal as m_nIdSucursal " +
                    " ,IdEstado as m_sIdEstado  " +
                    " ,Estado as m_sEstado " +
                    " , CodigoMunicipio as m_sCodMunicipio " +
                    " , Municipio as m_sMunicipio  " +
                    " FROM CatZonasTarifasPQ WHERE IdZona = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            jsonObject.put("m_arrCPs", zonasService.getCodigoPostalZonaTarifa(id, jdbcConnection));
            jsonObject.put("m_arrArConceptos", zonasService.getConceptoZonaTarifa(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaTarifa/GetByIdSinCP/{id}")
    public ResponseEntity<?> getZonaTarifaSinCP(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT  " +
                    " IdZona as m_nIdZona " +
                    " ,CodigoZona as m_sCodigoZona " +
                    " ,IdSucursal as m_nIdSucursal " +
                    " ,IdEstado as m_sIdEstado  " +
                    " ,Estado as m_sEstado " +
                    " , CodigoMunicipio as m_sCodMunicipio " +
                    " , Municipio as m_sMunicipio  " +
                    " FROM CatZonasTarifasPQ WHERE IdZona = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            jsonObject.put("m_arrArConceptos", zonasService.getConceptoZonaTarifa(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ZonaOperativa/GetById/{id}")
    public ResponseEntity<?> getZonaOperativaId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatZonasOperativasGetByIdPQ " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            int idZona = 0;
            while (rs.next()) {
                idZona = rs.getInt("IdZona");
                json.put("m_nIdZona", idZona);
                json.put("m_sCodigoZona", rs.getString("CodigoZona"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sIdEstado", rs.getString("IdEstado"));
                json.put("m_sEstado", rs.getString("Estado"));
                json.put("m_sCodMunicipio", rs.getString("CodigoMunicipio"));
                json.put("m_sMunicipio", rs.getString("Municipio"));
                json.put("m_bAplicaEntrega", rs.getBoolean("AplicaEntrega"));
                json.put("m_nIdPais", rs.getString("IdPais"));
                json.put("m_nIdOrigenDestino", rs.getInt("IdOrigenDestino"));
                json.put("m_sIdEstado", rs.getString("IdEstado"));
                json.put("m_arrCPs", zonasService.getCPsByIdZonaOperativa(idZona, jdbcConnection));
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/ZonaOperativa/Agregar")
    public ResponseEntity<?> postZonaOperativa(@RequestBody ZonaOperativaRequest request,
                                               @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                String query = " EXEC usp_CatZonasOperativasAgregarPQ "
                        + "'" + request.getM_sCodigoZona() + "'"
                        + "," + request.getM_nIdSucursal()
                        + ",'" + request.getM_sIdEstado() + "'"
                        + ",'" + request.getM_sEstado() + "'"
                        + ",'" + request.getM_sCodMunicipio() + "'"
                        + ",'" + request.getM_sMunicipio() + "'"
                        + "," + request.getM_nIdOrigenDestino()
                        + "," + request.getM_nIdPais()
                        + "," + (request.isM_bAplicaEntrega() ? 1 : 0);
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs;
                statement.executeUpdate(query);

                query = "select @@identity as id";
                rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                for (CodigoPostalRequest cp : request.getM_arrCP()) {
                    cp.setM_nIdZona(jsonObject.getInt("id"));
                    query = " EXEC usp_CatZonasOperativasCPAgregarPQ "
                            + cp.getM_nIdZona()
                            + "," + cp.getM_nIdCP();
                    statement.executeUpdate(query);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar la zona operativa");
            }
            JSONObject jsonFin = new JSONObject();
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/ZonaOperativa/Modificar/{m_nIdZona}")
    public ResponseEntity<?> putZonaOperativa(@PathVariable("m_nIdZona") int idZona,
                                              @RequestBody ZonaOperativaRequest request,
                                              @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                String query = " EXEC usp_CatZonasOperativasModificarPQ "
                        + request.getM_nIdZona()
                        + ",'" + request.getM_sCodigoZona() + "'"
                        + ",'" + request.getM_sIdEstado() + "'"
                        + ",'" + request.getM_sEstado() + "'"
                        + ",'" + request.getM_sCodMunicipio() + "'"
                        + ",'" + request.getM_sMunicipio() + "'"
                        + "," + request.getM_nIdSucursal()
                        + "," + request.getM_nIdOrigenDestino()
                        + "," + request.getM_nIdPais()
                        + "," + (request.isM_bAplicaEntrega() ? 1 : 0);
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs;
                statement.executeUpdate(query);
                for (CodigoPostalRequest cp : request.getM_arrCP()) {
                    cp.setM_nIdZona(idZona);
                    query = " EXEC usp_CatZonasOperativasCPAgregarPQ "
                            + cp.getM_nIdZona()
                            + "," + cp.getM_nIdCP();
                    statement.executeUpdate(query);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar la zona operativa");
            }
            JSONObject jsonFin = new JSONObject();
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/ZonaOperativa/Eliminar/{id}")
    public ResponseEntity<?> deleteZonaOperativa(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatZonasOperativasEliminarPQ " + id;
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al eliminar");
        }
        return ResponseEntity.ok("Eliminado exitoso");
    }

    @GetMapping("/ZonaOperativa/GetListado")
    public ResponseEntity<?> getZonaOperativaListado(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatZonasOperativasGetListadoPQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdZona", rs.getInt("IdZona"));
                json.put("m_sCodigoZona", rs.getString("CodigoZona"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sIdEstado", rs.getString("IdEstado"));
                json.put("m_sEstado", rs.getString("Estado"));
                json.put("m_sCodMunicipio", rs.getString("CodigoMunicipio"));
                json.put("m_sMunicipio", rs.getString("Municipio") == null ? "" :
                        rs.getString("Municipio"));
                json.put("m_nIdPais", rs.getInt("IdPais"));
                json.put("m_sPais", "");
                json.put("m_sOrigenDestino", "");
                json.put("m_nIdOrigenDestino", 0);
                json.put("m_arrCPs", new ArrayList<>());
                json.put("m_bAplicaEntrega", rs.getBoolean("AplicaEntrega"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/ZonaOperativa/Rollback")
    public ResponseEntity<?> rollbackZonaOpeartiva(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                jdbcConnection.setAutoCommit(false);
                String query = "EXEC usp_CatZonasAgregarPQ 1,'agricola',7,'2020-11-11 23:35:12.000'," +
                        "13,'2020-11-11 23:35:12.000',0,580,580";
                Statement statement = jdbcConnection.createStatement();

                String query2 = "EXEC usp_CatZonasAgregarPQ 1,'aeroespacial',7,'2020-11-11 23:35:12.000'," +
                        "13,'2020-11-11 23:35:12.000',0,580,580";
                Statement statement2 = jdbcConnection.createStatement();

                statement.executeUpdate(query);
                statement2.executeUpdate(query2);
                jdbcConnection.commit();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    jdbcConnection.rollback();
                } catch (SQLException excep) {

                }
                return ResponseEntity.status(500).body("error");
            }
            jdbcConnection.setAutoCommit(true);

            return ResponseEntity.ok("okok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
