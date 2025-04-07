package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class ConceptosCobranzaRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/ConceptosCobranza/GetListado")
    public ResponseEntity<?> getListadoConceptosCobranza(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT \n" +
                    "    IdConceptoCobranza\n" +
                    "     , Codigo\n" +
                    "     , ConceptoCobranza\n" +
                    "     , TipoConcepto\n" +
                    "FROM dbo.CatConceptosCobranza WHERE Activo = 1;";
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