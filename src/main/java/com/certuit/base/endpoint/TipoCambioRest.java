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
public class TipoCambioRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/TipoCambio/GetListado")
    public ResponseEntity<?> getOrigenDestino(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdTipoCambio as m_nIdTipoCambio," +
                    "Fecha as m_sFecha," +
                    "TipoCambio as m_cTipoCambio " +
                    "FROM CatTiposCambio ORDER BY Fecha Desc";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/TipoCambio/GetById/{id}")
    public ResponseEntity<?> getTipoCambio(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            ResultSet rs;
            try {
                String query = "SELECT\n" +
                        "\tIdTipoCambio as m_nIdTipoCambio,\n" +
                        "\tFecha as m_dtFecha,\n" +
                        "\tTipoCambio as m_cTipoCambio,\n" +
                        "\tCreadoEl as m_dtCreadoEl,\n" +
                        "\tCreadoPor as m_nCreadoPor,\n" +
                        "\tModificadoEl as m_dtModificadoEl,\n" +
                        "\tModificadoPor as m_nModificadoPor\n" +
                        "\tFROM CatTiposCambioPQ\n" +
                        "\tWHERE IdTipoCambio =" + id;
                Statement statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Tipo Servicio no encontrada");
            }
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();
            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /*@DeleteMapping("/TipoCambio/Eliminar/{idTipoCambio}/{idUsuario}")
    public ResponseEntity<?> deleteTipoCobro(@PathVariable("idTipoCambio") int idTipoCambio,@PathVariable("idUsuario") int idUsuario, @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        try {
            String query = "EXEC usp_CatTiposCambioEliminarPQ "+idTipoCambio+"";

            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body( "Eliminado fracaso");

        }
        return ResponseEntity.ok("Eliminado exitoso");
    }*/
    /*@PostMapping("/TipoCambio/Agregar")
    public ResponseEntity<?> postTipoCambio(@RequestBody TipoCambioRequest tc, @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        try {
            String query = "EXEC usp_CatTiposCambioAgregarPQ '"+tc.getM_dtFecha()+"' ,"+tc.getM_cTipoCambio()+",'"+tc.getM_dtFecha()+"',"+tc.getM_nCreadoPor()+",'',"+0+"";
            Statement statement = jdbcConnection.createStatement();
            int rs = statement.executeUpdate(query);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar");

        }
        return ResponseEntity.ok("Agregado Exitosamente");
    }
*/
    /*@PutMapping("/TipoCambio/Modificar/{idTipoCambio}")
    public ResponseEntity<?> putTipoCambio(@PathVariable("idTipoCambio") int idTipoCambio,@RequestBody TipoCambioRequest tc, @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        try {
            String query = "EXEC usp_CatTiposCambioModificarPQ "+idTipoCambio+",'"+ tc.getM_dtFecha()+"',"+tc.getM_cTipoCambio()+",'',"+0+"";
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body( "Error al tratar de cambiar");

        }
        return ResponseEntity.ok("Cambio Exitoso");
    }

*/
}
