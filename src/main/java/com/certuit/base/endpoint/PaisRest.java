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
public class PaisRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Pais/GetListado")
    public ResponseEntity<?> getOrigenDestino(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdPais as m_nIdPais,\n" +
                    "\tCodigo as m_sCodigo,\n" +
                    "\tPais as m_sPais,\n" +
                    "\tIdMoneda as m_nIdMoneda,\n" +
                    "\tCreadoEl as m_dCreadoEl,\n" +
                    "\tCreadoPor as m_nCreadoPor,\n" +
                    "\tModificadoEl as m_dModificadoEl,\n" +
                    "\tModificadoPor as m_nModificadoPor\n" +
                    "\tFROM CatPaises";
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
