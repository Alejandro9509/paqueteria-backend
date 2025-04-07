package com.certuit.base.endpoint;

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
public class CodigoPostalRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/CodigoPostal/GetById/{id}")
    public ResponseEntity<?> getCodigoPostal(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "    CP.IdCodigoPostal as m_nIdCP, " +
                    "    CP.CodigoPostal as m_sCP, " +
                    "    CP.IdEstado as m_nIdEstado, " +
                    "    CP.IdCiudad as m_nIdCiudad, " +
                    "    Estado.Estado as m_sEstado, " +
                    "    Ciudad.Ciudad as m_sCiudad, " +
                    "    CP.Colonia as m_sColonia " +
                    "FROM CatCodigosPostales as CP " +
                    "         left join CatEstados as Estado " +
                    "                   on CP.IdEstado = Estado.Abreviacion " +
                    "         left join CatCiudades as Ciudad " +
                    "                   on CP.IdCiudad = Ciudad.IdCiudad " +
                    "WHERE IdCodigoPostal = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/CodigoPostal/GetByCode/{code}")
    public ResponseEntity<?> getCodigoPostal(@PathVariable("code") String code, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        Connection jdbcConnection = dbConection.getconnection(rfc);
        String query = "SELECT " +
                "    CP.IdCodigoPostal as m_nIdCP,\n" +
                "       CP.CodigoPostal as m_sCP,\n" +
                "       CP.CodigoMunicipio as m_nIdMunicipio,\n" +
                "       CP.Municipio as m_sMunicipio,\n" +
                "       CP.IdEstado as m_nIdEstado,\n" +
                "       CP.IdCiudad as m_nIdCiudad,\n" +
                "       CP.Colonia as m_sColonia\n" +
                "FROM CatCodigosPostales CP WHERE CP.CodigoPostal = '" + code + "'";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return ResponseEntity.ok(UtilFuctions.convertArray(rs).toString());
    }

    @GetMapping("/CodigoPostal/GetByEstadoMunicipio/{idestado}/{idmunicipio}")
    public ResponseEntity<?> getCodigoPostalByEstadoMunicipio(@PathVariable("idestado") String idEstado,
                                                              @PathVariable("idmunicipio") String idMunicipio,
                                                              @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        String query;
        Statement statement;
        ResultSet rs;
        JSONObject jsonResponse;
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            query = "SELECT IdPais FROM CatEstados WHERE IdEstado = '" + idEstado + "'  ";
            statement = jdbcConnection.createStatement();
            rs = statement.executeQuery(query);
            jsonResponse = UtilFuctions.convertObject(rs);
            if (jsonResponse.getInt("IdPais") == 1) {
                query = "SELECT cp.IdCodigoPostal as m_nIdCP, " +
                        "cp.CodigoPostal as m_sCP, cp.Colonia as m_sColonia," +
                        "cp.IdEstado as m_nIdEstado, " +
                        "cp.Localidad as m_sLocalidad, " +
                        "(SELECT DISTINCT UPPER(cp.Municipio) FROM CatCodigosPostales WHERE cp.IdEstado = '"
                        + idEstado + "' and cp.CodigoMunicipio = '" + idMunicipio + "') AS m_sMunicipio,  " +
                        "ISNULL(z.AplicaEntrega,1) as m_bNoAplicaEntrega, \n " +
                        " CAST(CASE" +
                        " WHEN  cp.IdCodigoPostal in (select cz.IdCP from CatZonasOperativasCPPQ cz " +
                        "JOIN CatCodigosPostales c on cz.IdCP = c.IdCodigoPostal WHERE cz.IdZona = z.IdZona) THEN 0\n" +
                        " ELSE 1\n" +
                        " END AS BIT) AS codigoFueraDeZonaOperativa" +
                        " FROM CatCodigosPostales cp \n  " +
                        " left join CatZonasOperativasCPPQ zcp on zcp.IdCP = cp.IdCodigoPostal \n" +
                        " left join CatZonasOperativasPQ z on z.IdZona = zcp.IdZona \n" +
                        " WHERE cp.IdEstado = '" + idEstado + "' and cp.CodigoMunicipio ='" + idMunicipio + "'  " +
                        " order by cp.CodigoPostal, cp.Colonia";
            } else {
                query = "SELECT  cp.IdCodigoPostal as m_nIdCP, " +
                        "cp.CodigoPostal as m_sCP, cp.Colonia as m_sColonia," +
                        "cp.IdEstado as m_nIdEstado, " +
                        "cp.Localidad as m_sLocalidad, " +
                        "UPPER(cp.Municipio) as m_sMunicipio  " +
                        "ISNULL(z.AplicaEntrega,1) as m_bNoAplicaEntrega, \n " +
                        " CAST(CASE" +
                        " WHEN cp.IdCodigoPostal in (select cz.IdCP from CatZonasOperativasCPPQ cz " +
                        "JOIN CatCodigosPostales c on cz.IdCP = c.IdCodigoPostal WHERE cz.IdZona = z.IdZona) THEN 0\n" +
                        " ELSE 1\n" +
                        " END AS BIT) AS codigoFueraDeZonaOperativa" +
                        " FROM CatCodigosPostales cp \n " +
                        " left join CatZonasOperativasCPPQ zcp on zcp.IdCP = cp.IdCodigoPostal \n" +
                        " left join CatZonasOperativasPQ z on z.IdZona = zcp.IdZona \n" +
                        " WHERE cp.IdEstado = '" + idEstado + "' and cp.Municipio ='" + idMunicipio + "'  " +
                        " order by cp.CodigoPostal, cp.Colonia";
            }

            rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/CodigoPostal/GetByEstadoMunicipioDisponible/{idestado}/{idmunicipio}")
    public ResponseEntity<?> getCodigoPostalByEstadoMunicipioDisponibles(@PathVariable("idestado") String idEstado,
                                                                         @PathVariable("idmunicipio") String idMunicipio,
                                                                         @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        String query;
        Statement statement;
        ResultSet rs;
        JSONObject jsonResponse;
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            query = "SELECT IdPais FROM CatEstados WHERE IdEstado = '" + idEstado + "'  ";
            statement = jdbcConnection.createStatement();
            rs = statement.executeQuery(query);
            jsonResponse = UtilFuctions.convertObject(rs);
            if (jsonResponse.getInt("IdPais") == 1) {
                query = "SELECT cp.IdCodigoPostal as m_nIdCP, " +
                        "cp.CodigoPostal as m_sCP, cp.Colonia as m_sColonia," +
                        "cp.IdEstado as m_nIdEstado, " +
                        "cp.Localidad as m_sLocalidad, " +
                        "(SELECT DISTINCT UPPER(cp.Municipio) FROM CatCodigosPostales WHERE cp.IdEstado = '" + idEstado
                        + "' and cp.CodigoMunicipio = '" + idMunicipio + "') AS m_sMunicipio,  " +
                        "ISNULL(z.AplicaEntrega,1) as m_bNoAplicaEntrega, \n " +
                        " CAST(CASE" +
                        " WHEN  cp.IdCodigoPostal in (select cz.IdCP from CatZonasOperativasCPPQ cz " +
                        "JOIN CatCodigosPostales c on cz.IdCP = c.IdCodigoPostal WHERE cz.IdZona = z.IdZona) THEN 0\n" +
                        " ELSE 1\n" +
                        " END AS BIT) AS codigoFueraDeZonaOperativa" +
                        " FROM CatCodigosPostales cp \n  " +
                        " left join CatZonasOperativasCPPQ zcp on zcp.IdCP = cp.IdCodigoPostal \n" +
                        " left join CatZonasOperativasPQ z on z.IdZona = zcp.IdZona \n" +
                        " WHERE cp.IdCodigoPostal NOT IN (select zonasCP.IdCP from CatZonasOperativasCPPQ zonasCP " +
                        "join CatCodigosPostales cp on zonasCP.IdCP = cp.IdCodigoPostal)\n" +
                        " and cp.IdEstado = '" + idEstado + "' and cp.CodigoMunicipio ='" + idMunicipio + "'  " +
                        " order by cp.CodigoPostal, cp.Colonia";
            } else {
                query = "SELECT  cp.IdCodigoPostal as m_nIdCP, " +
                        "cp.CodigoPostal as m_sCP, cp.Colonia as m_sColonia," +
                        "cp.IdEstado as m_nIdEstado, " +
                        "cp.Localidad as m_sLocalidad, " +
                        "UPPER(cp.Municipio) as m_sMunicipio  " +
                        "ISNULL(z.AplicaEntrega,1) as m_bNoAplicaEntrega, \n " +
                        " CAST(CASE" +
                        " WHEN cp.IdCodigoPostal in (select cz.IdCP from CatZonasOperativasCPPQ cz " +
                        "JOIN CatCodigosPostales c on cz.IdCP = c.IdCodigoPostal WHERE cz.IdZona = z.IdZona) THEN 0\n" +
                        " ELSE 1\n" +
                        " END AS BIT) AS codigoFueraDeZonaOperativa" +
                        " FROM CatCodigosPostales cp \n " +
                        " left join CatZonasOperativasCPPQ zcp on zcp.IdCP = cp.IdCodigoPostal \n" +
                        " left join CatZonasOperativasPQ z on z.IdZona = zcp.IdZona \n" +
                        " WHERE cp.IdCodigoPostal NOT IN (select zonasCP.IdCP from CatZonasOperativasCPPQ zonasCP " +
                        "join CatCodigosPostales cp on zonasCP.IdCP = cp.IdCodigoPostal)\n " +
                        " and cp.IdEstado = '" + idEstado + "' and cp.Municipio ='" + idMunicipio + "'  " +
                        " order by cp.CodigoPostal, cp.Colonia";
            }

            rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/CodigoPostal/GetListado")
    public ResponseEntity<?> getListadoCP(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT * FROM CatCodigosPostales order by CodigoPostal, Colonia";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdCP", rs.getInt("IdCodigoPostal"));
                json.put("m_sCP", rs.getString("CodigoPostal"));
                json.put("m_sColonia", rs.getString("Colonia"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}
