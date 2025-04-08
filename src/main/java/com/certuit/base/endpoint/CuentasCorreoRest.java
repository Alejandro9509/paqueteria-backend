package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CuentaCorreoRequest;
import com.certuit.base.util.DBConection;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class CuentasCorreoRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/CuentasCorreo/GetListado")
    public ResponseEntity<?> getlistadocuentascorreo(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT * FROM SisCuentasCorreoPQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdCuentasCorreo", rs.getInt("IdCuentasCorreo"));
                json.put("m_nTipoCuenta", rs.getInt("TipoCuenta"));
                json.put("m_sServidor", rs.getString("Servidor"));
                json.put("m_nPuerto", rs.getInt("Puerto"));
                json.put("m_sUsuario", rs.getString("Usuario"));
                json.put("m_sContrasenia", rs.getString("Contrasenia"));
                json.put("m_nTipoCifrado", rs.getInt("TipoCifrado"));
                json.put("m_dtCreadoEl", rs.getString("CreadoEl"));
                json.put("m_dtModificadoEl", rs.getString("ModificadoEl"));
                json.put("m_nCreadoPor", rs.getInt("CreadoPor"));
                json.put("m_nModificadoPor", rs.getInt("ModificadoPor"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/CuentasCorreo/Agregar")
    public ResponseEntity<?> agregarCuentaCorreo(@RequestBody CuentaCorreoRequest request,
                                                 @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            try {
                String date = simpleDateFormat.format(new Date());
                String query = "EXEC usp_SisCuentasCorreoAgregarPQ  " + request.getM_nTipoCuenta() + ", '"
                        + request.getM_sServidor() + "'," + request.getM_nPuerto() + ",'" + request.getM_sUsuario()
                        + "','" + request.getM_sContrasenia() + "'," + request.getM_nTipoCifrado() + ",'" + date
                        + "'," + request.getM_sCreadoPor() + ", '" + date + "'," + request.getM_sCreadoPor();
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al agregar cuenta correo");
            }
            return ResponseEntity.ok("Agregado Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/CuentasCorreo/Modificar/{id}")
    public ResponseEntity<?> modificarCuentaCorreo(@PathVariable("id") int id, @RequestBody CuentaCorreoRequest request,
                                                   @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            try {
                String date = simpleDateFormat.format(new Date());
                String query = "EXEC usp_SisCuentasCorreoModificarPQ  " + id + "," + request.getM_nTipoCuenta() + ",'"
                        + request.getM_sServidor() + "',"
                        + request.getM_nPuerto() + ",'"
                        + request.getM_sUsuario() + "','" + request.getM_sContrasenia() + "',"
                        + request.getM_nTipoCifrado() + ", '" + date + "'," + request.getM_sModificadoPor();
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al modificar cuenta correo");
            }
            return ResponseEntity.ok("Agregado Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}
