package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class ImpuestosRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Impuestos/GetListado")
    public ResponseEntity<?> getImpuestos(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdImpuesto as m_nIdImpuesto," +
                    "Impuesto as m_sImpuesto," +
                    "Porcentaje as m_nPorcentaje," +
                    "EsImpuestoLocal as m_bImpuestolocal," +
                    "CASE WHEN TipoCalculo = 1 THEN 1 ELSE 0 END as m_nTIpoImpuesto," +
                    "TipoCalculo as m_nTIpoCalculo," +
                    "Activo as m_bActivo " +
                    "FROM CatImpuestos where TipoCalculo in (1,2)";
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
