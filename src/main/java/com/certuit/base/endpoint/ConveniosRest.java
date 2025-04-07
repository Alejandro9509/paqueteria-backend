package com.certuit.base.endpoint;

import com.certuit.base.service.base.ConvenioService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class ConveniosRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    ConvenioService convenioService;

    @GetMapping("/Convenios/GetListado")
    public ResponseEntity<?> getListadoConvenios(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdConvenio as m_nIdConvenio," +
                    "IdCliente as m_nIdCliente," +
                    "(SELECT NombreFiscal FROM CatClientes WHERE IdCliente = t.IdCliente) as m_sNombreFiscal," +
                    "(SELECT NumeroCliente FROM CatClientes WHERE IdCliente = t.IdCliente) as m_nNumeroCliente," +
                    "Vigencia as m_sVigencia," +
                    "activo as m_bActivo," +
                    "CuotaMensual as m_xCuotaMensual " +
                    "FROM CatConveniosPQ t";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Convenios/GetById/{id}")
    public ResponseEntity<?> getConvenioId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdConvenio,IdCliente, Vigencia, activo, CuotaMensual FROM CatConveniosPQ " +
                    "WHERE IdConvenio =" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            while (rs.next()) {
                json.put("m_nIdCliente", rs.getInt("IdCliente"));
                json.put("m_xCuotaMensual", rs.getFloat("CuotaMensual"));
                json.put("m_sVigencia", rs.getString("Vigencia"));
                json.put("m_nIdConvenio", rs.getInt("IdConvenio"));
                json.put("m_bActivo", rs.getBoolean("activo"));
                json.put("m_arrArTarifas",
                        convenioService.getTarifasConvenioPorIdConvenio(rs.getInt("IdConvenio"),
                                jdbcConnection));
                json.put("m_arrArZonas",
                        convenioService.getZonasConvenioPorIdConvenio(rs.getInt("IdConvenio"),
                        jdbcConnection));
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Convenios/GetByIdCliente/{idCliente}")
    public ResponseEntity<?> getConvenioByIdCliente(@PathVariable("idCliente") int idCliente,
                                                    @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdConvenio,IdCliente, Vigencia, activo, CuotaMensual FROM CatConveniosPQ " +
                    "WHERE IdCliente =" + idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            while (rs.next()) {
                json.put("m_nIdCliente", rs.getInt("IdCliente"));
                json.put("m_xCuotaMensual", rs.getFloat("CuotaMensual"));
                json.put("m_sVigencia", rs.getString("Vigencia"));
                json.put("m_nIdConvenio", rs.getInt("IdConvenio"));
                json.put("m_bActivo", rs.getBoolean("activo"));
                json.put("m_arrArTarifas",
                        convenioService.getTarifasConvenioPorIdConvenio(rs.getInt("IdConvenio"),
                                jdbcConnection));
                json.put("m_arrArZonas",
                        convenioService.getZonasConvenioPorIdConvenio(rs.getInt("IdConvenio"),
                                jdbcConnection));
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
