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
public class RutasRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Rutas/GetRutasByIdCliente/{idCliente}")
    public ResponseEntity<?> getRutasByIdCliente(@PathVariable("idCliente") int idCliente,
                                                 @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select ct.IdRuta,ct.Kilometros,ct.Horas,ct.DescripcionRuta from CatRutas ct " +
                    "where ct.IdCliente=" + idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Rutas/GetByIdClienteOrigenDestino/{idCliente}/{idOrigen}/{idDestino}")
    public ResponseEntity<?> getRutasByIdClienteOrigenDestino(@PathVariable("idCliente") int idCliente,
                                                              @PathVariable("idOrigen") int idOrigen,
                                                              @PathVariable("idDestino") int idDestino,
                                                              @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select " +
                    "ct.IdRuta," +
                    "ct.Kilometros," +
                    "ct.Horas," +
                    "ct.DescripcionRuta " +
                    "from CatRutas ct  " +
                    "where ct.Activa=1 and ct.IdOrigen=" + idOrigen +
                    " and ct.IdDestino=" + idDestino +
                    " and ct.IdCliente=" + idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Rutas/GetByOrigenDestino/{idOrigen}/{idDestino}")
    public ResponseEntity<?> getRutasByOrigenDestino(@PathVariable("idOrigen") int idOrigen,
                                                     @PathVariable("idDestino") int idDestino,
                                                     @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select " +
                    "ct.IdRuta," +
                    "ct.Kilometros," +
                    "ct.Horas," +
                    "ct.DescripcionRuta " +
                    "from CatRutas ct  " +
                    "where ct.IdOrigen=" + idOrigen +
                    " and ct.IdDestino=" + idDestino +
                    " and ct.IdCliente=(select IdCliente from CatClientes where NombreFiscal " +
                    "LIKE '%PUBLICO EN GENERAL%')";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Rutas/GetTrayectosRuta/{idRuta}")
    public ResponseEntity<?> getRutas(@PathVariable("idRuta") int idRuta,
                                      @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select " +
                    "IdRutaTrayecto," +
                    "IdRuta," +
                    "Secuencia," +
                    "IdOrigen," +
                    "(select OrigenDestino from CatOrigenesDestinos where IdOrigenDestino = IdOrigen) Origen," +
                    "(select OrigenDestino from CatOrigenesDestinos where IdOrigenDestino = IdDestino) Destino," +
                    "IdDestino," +
                    "Kilometros," +
                    "Horas " +
                    "from CatRutasTrayectos " +
                    "where IdRuta=" + idRuta;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Rutas/GetById/{idRuta}")
    public ResponseEntity<?> getById(@PathVariable("idRuta") int idRuta, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdRuta, DescripcionRuta, IdOrigen, IdDestino, IdCliente\n" +
                    "from CatRutas\n" +
                    "where IdRuta = " + idRuta;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
