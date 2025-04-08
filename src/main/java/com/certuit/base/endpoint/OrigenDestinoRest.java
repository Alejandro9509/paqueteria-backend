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
public class OrigenDestinoRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Ciudades/GetListado")
    public ResponseEntity<?> getOrigenDestino(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select distinct  ctod.IdOrigenDestino  as m_nIdCiudad,ctod.OrigenDestino as m_sCiudad, " +
                    "ctod.IdOrigenDestino  as m_nIdCiudad2 from CatRutas cr " +
                    "inner join CatOrigenesDestinos ctod on ctod.IdOrigenDestino=cr.IdOrigen " +
                    "where cr.IdCliente in " +
                    "(select IdCliente from CatClientes where NombreFiscal like '%PUBLICO EN GENERAL%') " +
                    "and cr.IdTipoViaje in " +
                    "(select IdTipoViaje from CatTiposViajes where TipoViaje like '%CONSOLIDADO%')" +
                    "union " +
                    "select distinct  ctod.IdOrigenDestino  as m_nIdCiudad,ctod.OrigenDestino as m_sCiudad, " +
                    "ctod.IdOrigenDestino  as m_nIdCiudad2 from CatRutas cr " +
                    "inner join CatOrigenesDestinos ctod on ctod.IdOrigenDestino=cr.IdDestino " +
                    "where cr.IdCliente in (select IdCliente from CatClientes " +
                    "where NombreFiscal like '%PUBLICO EN GENERAL%') " +
                    "and cr.IdTipoViaje in (select IdTipoViaje from CatTiposViajes " +
                    "where TipoViaje like '%CONSOLIDADO%')";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Ciudades/GetById/{id}")
    public ResponseEntity<?> getOrigenDestinoById(@RequestHeader("RFC") String rfc, @PathVariable("id") int id)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdOrigenDestino as m_nIdCiudad, " +
                    "OrigenDestino as m_sCiudad, " +
                    "IdOrigenDestino as m_sCodigo " +
                    "From CatOrigenesDestinos " +
                    "WHERE IdOrigenDestino =" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}
