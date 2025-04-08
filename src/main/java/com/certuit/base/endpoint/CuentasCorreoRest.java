package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CuentaCorreoRequest;
import com.certuit.base.util.DBConection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class CuentasCorreoRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/CuentasCorreo/GetListado")
    public ResponseEntity<?> getListadoCuentasCorreo(@RequestHeader("RFC") String rfc) {
        List<Map<String, Object>> resultado = new ArrayList<>();

        try (Connection conn = dbConection.getconnection(rfc);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM SisCuentasCorreoPQ")) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("m_nIdCuentasCorreo", rs.getInt("IdCuentasCorreo"));
                fila.put("m_nTipoCuenta", rs.getInt("TipoCuenta"));
                fila.put("m_sServidor", rs.getString("Servidor"));
                fila.put("m_nPuerto", rs.getInt("Puerto"));
                fila.put("m_sUsuario", rs.getString("Usuario"));
                fila.put("m_sContrasenia", rs.getString("Contrasenia"));
                fila.put("m_nTipoCifrado", rs.getInt("TipoCifrado"));
                fila.put("m_dtCreadoEl", rs.getString("CreadoEl"));
                fila.put("m_dtModificadoEl", rs.getString("ModificadoEl"));
                fila.put("m_nCreadoPor", rs.getInt("CreadoPor"));
                fila.put("m_nModificadoPor", rs.getInt("ModificadoPor"));
                resultado.add(fila);
            }

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/CuentasCorreo/Agregar")
    public ResponseEntity<?> agregarCuentaCorreo(@RequestBody CuentaCorreoRequest request,
                                                 @RequestHeader("RFC") String rfc) {
        String procedure = """
        EXEC usp_SisCuentasCorreoAgregarPQ ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
        """;

        try (Connection conn = dbConection.getconnection(rfc);
             PreparedStatement stmt = conn.prepareStatement(procedure)) {

            var now = LocalDateTime.now();
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            var formattedNow = now.format(formatter);

            stmt.setInt(1, request.getM_nTipoCuenta());
            stmt.setString(2, request.getM_sServidor());
            stmt.setInt(3, request.getM_nPuerto());
            stmt.setString(4, request.getM_sUsuario());
            stmt.setString(5, request.getM_sContrasenia());
            stmt.setInt(6, request.getM_nTipoCifrado());
            stmt.setString(7, formattedNow); // CreadoEl
            stmt.setInt(8, Integer.parseInt(request.getM_sCreadoPor()));
            stmt.setString(9, formattedNow); // ModificadoEl
            stmt.setInt(10, Integer.parseInt(request.getM_sCreadoPor())); // ModificadoPor

            stmt.executeUpdate();
            return ResponseEntity.ok("Agregado exitosamente");

        } catch (SQLException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un problema al agregar cuenta correo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un problema al procesar la solicitud. Intente más tarde.");
        }
    }

    @PutMapping("/CuentasCorreo/Modificar/{id}")
    public ResponseEntity<?> modificarCuentaCorreo(@PathVariable("id") int id,
                                                   @RequestBody CuentaCorreoRequest request,
                                                   @RequestHeader("RFC") String rfc) {
        String procedure = """
        EXEC usp_SisCuentasCorreoModificarPQ ?, ?, ?, ?, ?, ?, ?, ?, ?
        """;

        try (Connection conn = dbConection.getconnection(rfc);
             PreparedStatement stmt = conn.prepareStatement(procedure)) {

            var now = LocalDateTime.now();
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            var formattedNow = now.format(formatter);

            stmt.setInt(1, id);
            stmt.setInt(2, request.getM_nTipoCuenta());
            stmt.setString(3, request.getM_sServidor());
            stmt.setInt(4, request.getM_nPuerto());
            stmt.setString(5, request.getM_sUsuario());
            stmt.setString(6, request.getM_sContrasenia());
            stmt.setInt(7, request.getM_nTipoCifrado());
            stmt.setString(8, formattedNow); // ModificadoEl
            stmt.setInt(9, Integer.parseInt(request.getM_sModificadoPor()));

            stmt.executeUpdate();
            return ResponseEntity.ok("Modificado exitosamente");

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un problema al modificar cuenta correo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un problema al procesar la solicitud. Intente más tarde.");
        }
    }

}
