package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.TipoServicioRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/api")
public class TipoServicioRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/TipoServicio/GetListado")
    public ResponseEntity<?> getTipoServicio(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdTipoServicio as m_nIdTipoServicio," +
                    "Descripcion as m_sDescripcion," +
                    "Costo as m_cCosto," +
                    "DiasHabiles as m_nDiashabiles," +
                    "Activo as m_bActivo," +
                    "CreadoEl as m_dtCreadoEl," +
                    "(SELECT Usuario FROM CatUsuarios WHERE IdUsuario = TIPOSERVICIO.CreadoPor) AS m_sCreadoPor," +
                    "ModificadoEl as m_dtModificadoEl," +
                    "(SELECT Usuario FROM CatUsuarios WHERE IdUsuario = TIPOSERVICIO.ModificadoPor) AS m_sModificadoPor " +
                    "FROM CatTipoServicioPQ AS TIPOSERVICIO";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/TipoServicio/GetById/{idTipoServicio}")
    public ResponseEntity<?> getByIdTipoServicio(@PathVariable("idTipoServicio") int idTipoServicio,
                                                 @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdTipoServicio as m_nIdTipoServicio,\n" +
                    "\tDescripcion as m_sDescripcion,\n" +
                    "\tCosto as m_cCosto,\n" +
                    "\tDiasHabiles as m_nDiashabiles,\n" +
                    "\tActivo as m_bActivo,\n" +
                    "\tCreadoEl as m_dtCreadoEl,\n" +
                    "\tCreadoPor as m_nCreadoPor,\n" +
                    "\tModificadoEl as m_dtModificadoEl,\n" +
                    "\tModificadoPor as m_nModificadoPor \n" +
                    "\tFROM CatTipoServicioPQ\n" +
                    "\tWHERE IdTipoServicio = " + idTipoServicio + "";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/TipoServicio/Eliminar/{idTipoServicio}/{idUsuario}")
    public ResponseEntity<?> deleteTipoServicio(@PathVariable("idTipoServicio") int idTipoServicio,
                                                @PathVariable("idUsuario") int idUsuario,
                                                @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatTipoServicioEliminarPQ " + idTipoServicio + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al eliminar");
            }

            return ResponseEntity.ok("Eliminado exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/TipoServicio/Agregar")
    public ResponseEntity<?> agregarTipoServicio(@RequestBody TipoServicioRequest ts,
                                                 @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            if (ts.getM_sDescripcion() == "") {
                return ResponseEntity.status(500).body("La Descripción es un campo requerido ");
            }
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String pattern = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());
                String query = "EXEC usp_CatTipoServicioAgregarPQ '" + ts.getM_sDescripcion() + "',"
                        + ts.getM_cCosto() + "," + ts.getM_nDiashabiles() + "," + (ts.isM_bActivo() ? 1 : 0) + ",'"
                        + date + "'," + ts.getM_nCreadoPor() + ",'" + date + "'," + ts.getM_nModificadoPor() + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al agregar");
            }
            return ResponseEntity.ok("Agregado Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/TipoServicio/Modificar/{idTipoServicio}")
    public ResponseEntity<?> modificarProducto(@RequestBody TipoServicioRequest ts,
                                               @PathVariable("idTipoServicio") int idTipoServicio,
                                               @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatTipoServicioModificarPQ  " + idTipoServicio + ",'"
                        + ts.getM_sDescripcion() + "'," + ts.getM_cCosto() + "," + ts.getM_nDiashabiles() + ","
                        + (ts.isM_bActivo() ? 1 : 0) + ",'" + date +
                        "'," + ts.getM_nModificadoPor() + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error al modificar");
            }
            return ResponseEntity.ok("Registro Modificado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
