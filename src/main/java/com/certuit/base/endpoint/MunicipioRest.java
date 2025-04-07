package com.certuit.base.endpoint;

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

@RestController
@RequestMapping(value = "/api")
public class MunicipioRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Municipios/GetByIdEstado/{id}")
    public ResponseEntity<?> getMunicipioByIdEstado(@RequestHeader("RFC") String rfc, @PathVariable("id") String id)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JSONObject jsonResponse;
                String query = "SELECT IdPais FROM CatEstados WHERE IdEstado = '" + id + "'  ";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                jsonResponse = UtilFuctions.convertObject(rs);
                if (jsonResponse.getInt("IdPais") == 1) {
                    query = "SELECT DISTINCT CodigoMunicipio as m_sCodigoMunicipio, UPPER(Municipio) AS m_sMunicipio " +
                            "FROM CatCodigosPostales WHERE IdEstado = '" + id + "' ORDER BY m_sMunicipio";
                } else {
                    query = "SELECT DISTINCT Municipio as m_sCodigoMunicipio, UPPER(Municipio) AS m_sMunicipio " +
                            "FROM CatCodigosPostales WHERE IdEstado = '" + id + "' ORDER BY m_sMunicipio";
                }
                rs = statement.executeQuery(query);
                String jsonArray = UtilFuctions.convertArray(rs).toString();
                

                return ResponseEntity.ok(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al obtener el listado de municipios.");
        }
    }

}
