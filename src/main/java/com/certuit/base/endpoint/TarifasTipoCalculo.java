package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
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
public class TarifasTipoCalculo {
    @Autowired
    DBConection dbConection;

    @GetMapping("/TipoCalculo/GetListado")
    public ResponseEntity<?> getListado(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdTarifaTipoCalculo,\n" +
                    "\tTarifaTipoCalculo\n" +
                    "\tFROM CatTarifaTipoCalculoPQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdTarifaTipoCalculo", rs.getInt("IdTarifaTipoCalculo"));
                json.put("m_sTarifaTipoCalculo", rs.getString("TarifaTipoCalculo"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}
