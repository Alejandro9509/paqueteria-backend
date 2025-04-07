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
public class EstadoRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Estados/ByPais/{id}")
    public ResponseEntity<?> getEstadoByPais(@RequestHeader("RFC") String rfc, @PathVariable("id") int id) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdEstado as m_nIdEstado, " +
                    "IdEstado as m_sCodigo, " +
                    "Estado as m_sEstado, " +
                    "Abreviacion as m_sAbreviacion," +
                    "IdPais as m_nIdPais," +
                    "CreadoEl as m_dtCreadoEl," +
                    "CreadoPor as m_nCreadoPor," +
                    "ModificadoEl as m_dtModificadoEl," +
                    "ModificadoPor as m_nModificadoPor," +
                    "IdEstado as m_nIdentificador " +
                    "FROM CatEstados " +
                    "WHERE IdPais=" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }

    }

    @GetMapping("/Estados/GetListado")
    public ResponseEntity<?> getEstadoListado(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdEstado          as m_nIdEstado,\n" +
                    "       IdEstado          as m_sCodigo,\n" +
                    "       Estado            as m_sEstado,\n" +
                    "       Abreviacion       as m_sAbreviacion,\n" +
                    "       CatEstados.IdPais as m_nIdPais,\n" +
                    "       CP.Pais           as m_sPais\n" +
                    "FROM CatEstados\n" +
                    "         LEFT JOIN CatPaises CP on CatEstados.IdPais = CP.IdPais";
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
