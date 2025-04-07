package com.certuit.base.endpoint;

import com.certuit.base.service.base.SeguimientoService;
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
public class SeguimientoRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    SeguimientoService seguimientoService;

    @GetMapping("/Seguimeinto/folio/{folio}/tipo/{tipo}")
    public ResponseEntity<?> getByIdTipo(@PathVariable("folio") String folio,
                                         @PathVariable("tipo") int tipo,
                                         @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            if (tipo == 4) {
                folio = "%" + folio;
            }
            String query = "exec usp_ProSeguimientoIdTipoPQ_1338 '" + folio + "'," + tipo;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            if (jsonObject != null) {
                jsonObject.put("paquetes",
                        seguimientoService.getPaquetesSeguimiento(jsonObject.getInt("m_nId"),
                                jsonObject.getInt("m_nTipo"), jdbcConnection));
                jsonObject.put("conceptos",
                        seguimientoService.getConceptosSeguimiento(jsonObject.getInt("m_nId"),
                                jsonObject.getInt("m_nTipo"), jdbcConnection));
                jsonObject.put("bitacora",
                        seguimientoService.getBitacoraSeguimiento(jsonObject.getInt("m_nId"),
                                jsonObject.getInt("m_nTipo"), jdbcConnection));
                jsonObject.put("Estatus", true);
            } else {
                return ResponseEntity.ok("No se pudo obtener la infomación.");
            }

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Seguimiento/folios")
    public ResponseEntity<?> getByIdTipo(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("recolecciones", seguimientoService.getFoliosRecoleccion(jdbcConnection));
            jsonObject.put("embarques", seguimientoService.getFoliosEmbarques(jdbcConnection));
            jsonObject.put("guias", seguimientoService.getFoliosGuias(jdbcConnection));
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
