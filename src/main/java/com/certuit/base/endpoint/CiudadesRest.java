package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class CiudadesRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Ciudades/GetListadoCP/{idCiudad}")
    public ResponseEntity<?> getListadoCP(@PathVariable("idCiudad") String idCiudad, @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdCodigoPostal, \n" +
                    "\tCodigoPostal,\n" +
                    "\tColonia as Ciudad\n" +
                    "\tfrom CatCodigosPostales cp where cp.IdEstado = (select IdEstado from CatCiudades cc where cc.IdCiudad = " + idCiudad + ")";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdCP", rs.getInt("IdCodigoPostal"));
                json.put("m_sCP", rs.getString("CodigoPostal"));
                json.put("m_sCiudad", rs.getString("Ciudad"));
                array.put(json);
            }
            

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}




























