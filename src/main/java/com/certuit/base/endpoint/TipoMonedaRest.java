package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/api")
public class TipoMonedaRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Moneda/GetListado")
    public ResponseEntity<?> getOrigenDestino(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdMoneda as m_nIdMoneda," +
                    "Moneda as m_sMoneda," +
                    "Codigo as m_sCodigo," +
                    "Simbolo as m_sSimbolo," +
                    "'' as m_sCreadoEL," +
                    "'' AS m_sCreadoPor," +
                    "'' as m_sModificadoEl," +
                    "'' AS m_sModificadoPor," +
                    "Abreviacion as m_sAbreviacion " +
                    "FROM CatMonedasPQ AS MONEDA";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Moneda/GetById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "IdMoneda as m_nIdMoneda,\n" +
                    "Moneda as m_sMoneda,\n" +
                    "Codigo as m_sCodigo,\n" +
                    "Simbolo as m_sSimbolo,\n" +
                    "'' as m_sCreadoEL,\n" +
                    "'' AS m_sCreadoPor,\n" +
                    "'' as m_sModificadoEl,\n" +
                    "'' AS m_sModificadoPor,\n" +
                    "Abreviacion as m_sAbreviacion\t\n" +
                    "FROM CatMonedasPQ AS MONEDA where IdMoneda=" + id + "";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/Moneda/Eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatMonedaEliminarPQ " + id + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al eliminar moneda");
            }

            return ResponseEntity.ok("Eliminado exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Moneda/Agregar")
    public ResponseEntity<?> postMoneda(@RequestBody TarifasRequest request, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {

        if (request.getM_sMoneda() == "") {
            return ResponseEntity.status(500).body("La moneda es un campo requerido");
        }
        if (request.getM_sCodigo() == "") {
            return ResponseEntity.status(500).body("El codigo es un campo requerido");
        }
        if (request.getM_sSimbolo() == "") {
            return ResponseEntity.status(500).body("El simbolo es un campo requerido");
        }
        if (request.getM_sAbreviacion() == "") {
            return ResponseEntity.status(500).body("La abreviacion es un campo requerido");
        }

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());
            String query = "EXEC usp_CatMonedaAgregarPQ  '"
                    + request.getM_sMoneda() + "', '"
                    + request.getM_sCodigo() + "', '"
                    + request.getM_sSimbolo() + "', '" + date + "', "
                    + request.getM_nCreadoPor() + ", '"
                    + request.getM_sAbreviacion() + "'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            statement.executeUpdate(query);
            return ResponseEntity.ok("Agregado Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Moneda/Modificar/{id}")
    public ResponseEntity<?> putMoneda(@RequestBody TarifasRequest request, @PathVariable("id") int id,
                                       @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        if (request.getM_sMoneda() == "") {
            return ResponseEntity.status(500).body("La moneda es un campo requerido");
        }
        if (request.getM_sCodigo() == "") {
            return ResponseEntity.status(500).body("El codigo es un campo requerido");
        }
        if (request.getM_sSimbolo() == "") {
            return ResponseEntity.status(500).body("El simbolo es un campo requerido");
        }
        if (request.getM_sAbreviacion() == "") {
            return ResponseEntity.status(500).body("La abreviacion es un campo requerido");
        }
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatMonedaModificarPQ '" + request.getM_sMoneda() + "', '"
                    + request.getM_sCodigo() + "', '" + request.getM_sSimbolo() + "', '"
                    + request.getM_sAbreviacion() + "', " + id + "";
            try {
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al tratar de modificar");
            }
            return ResponseEntity.ok("Modificado Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
