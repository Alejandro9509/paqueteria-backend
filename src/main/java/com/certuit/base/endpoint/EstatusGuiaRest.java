package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class EstatusGuiaRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/EstatusGuia/GetListado")
    public ResponseEntity<?> getEstatusGuia(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdEstatusGuias as m_nIdEstatusGuia" +
                    ",Abreviacion as m_sAbreviacion" +
                    ",Estatus as m_sEstatus" +
                    ",Descripcion as m_sDescripcion" +
                    ",Color as m_sColor " +
                    "FROM CatEstatusGuiasPQ";
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
