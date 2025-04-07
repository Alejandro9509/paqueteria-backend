package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.TipoCobroRequest;
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
public class TipoCobroRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/TipoCobro/GetListado")
    public ResponseEntity<?> getOrigenDestino(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT" +
                    "  IdTipoCobro as m_nIdTipoCobro," +
                    "  Codigo as m_nCodigo," +
                    "  Descripcion as m_sDescripcion," +
                    "  cobro.IdTipoPago as m_nIdTipoPago," +
                    "  pago.MetodoPago as m_sTipoPago," +
                    "  cobro.BloquearUltimaMilla as m_bBloquearUltimaMilla," +
                    "  (SELECT Usuario FROM CatUsuarios WHERE IdUsuario = cobro.CreadoPor) AS m_sCreadoPor," +
                    "  CreadoEl as m_sCreadoEl," +
                    "  (SELECT Usuario FROM CatUsuarios WHERE IdUsuario = cobro.ModificadoPor) AS m_sModificadoPor," +
                    "  ModificadoEl as m_sModificadoEl " +
                    " FROM CatTipoCobroPQ cobro " +
                    " LEFT JOIN CatMetodosPagos pago on pago.Clave = cobro.IdTipoPago";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/TipoCobro/GetTipoCobro/{id}")
    public ResponseEntity<?> getOrigenDestino(@PathVariable("id") int idTipoCobro, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\t  IdTipoCobro,\n" +
                    "\t  Codigo,\n" +
                    "\t  Descripcion,\n" +
                    "\t  IdTipoPago,\n" +
                    "\t  BloquearUltimaMilla,\n" +
                    "\t  CreadoPor,\n" +
                    "\t  CreadoEl,\n" +
                    "\t  ModificadoPor,\n" +
                    "\t  ModificadoEl\n" +
                    "\tFROM CatTipoCobroPQ\n" +
                    "\tWHERE IdTipoCobro = " + idTipoCobro;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/TipoCobro/Agregar")
    public ResponseEntity<?> postTipoCobro(@RequestBody TipoCobroRequest request, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatTipoCobroAgregarPQ  " +
                    +request.getCodigo() +
                    ", '" + request.getDescripcion() + "'" +
                    ", " + request.getCreadoPor() +
                    ", " + request.getModificadoPor() +
                    ", " + request.isBloqueaUltimaMilla() +
                    ", '" + request.getIdTipoPago() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);

                if (rs == 1) {
                    return ResponseEntity.ok("Se guardó correctamente.");

                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al guardar");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/TipoCobro/Modificar/{id}")
    public ResponseEntity<?> putTipoCobro(@PathVariable("id") int idTipoCobro,
                                          @RequestBody TipoCobroRequest request,
                                          @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatTipoCobroModificarPQ  " +
                    +idTipoCobro +
                    ", " + request.getCodigo() +
                    ", '" + request.getDescripcion() + "'" +
                    ", " + request.getModificadoPor() +
                    ", " + request.isBloqueaUltimaMilla() +
                    ", '" + request.getIdTipoPago() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs == 1) {
                    return ResponseEntity.ok("Se guardó correctamente.");

                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al guardar");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/TipoCobro/Eliminar/{id}")
    public ResponseEntity<?> deleteTipoCobro(@PathVariable("id") int idTipoCobro,
                                             @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "";
            Statement statement = jdbcConnection.createStatement();
            JSONObject jsonObject;
            try {
                query = "Select EstatusDeSistema From CatTipoCobroPQ where IdTipoCobro = " + idTipoCobro;
                jsonObject = UtilFuctions.convertObject(statement.executeQuery(query));
                if (jsonObject.getBoolean("EstatusDeSistema")) {
                    return ResponseEntity.status(500).body("Error al eliminar: No se puede borrar el registro porque es propio del sistema");
                }
                query = "EXEC usp_CatTipoCobroEliminarPQ  " + idTipoCobro;
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se eliminó correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
                String errorMessage = e.getMessage();  // Obtén el mensaje de error de la excepción SQL
                return ResponseEntity.status(500).body("Error al eliminar: " + errorMessage);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al eliminar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}

