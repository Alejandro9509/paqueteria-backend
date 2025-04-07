package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.EstatusViajeRequest;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(value = "/api")
public class EstatusViajeRest {
    @Autowired
    DBConection dbConection;

    @PostMapping("/EstatusViajes/Agregar")
    public ResponseEntity<?> agregarEstatusViaje(@RequestBody EstatusViajeRequest params, @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String fecha = df.format(today);
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatEstatusViajeAgregarPQ '" + params.getM_sAbreviacion() + "','"
                        + params.getM_sColor() + "'," + params.getM_sColorLetra() + ",'"
                        + params.getM_sCreadoPor() + "'," + "'"
                        + params.getM_sEstatus() + "','" + params.getM_sModificadoPor() + "',"
                        + params.getM_sTipoEstatus() + "," + (params.getM_barchivo() ? 1 : 0) + ","
                        + (params.getM_bcarga() ? 1 : 0) + "," + (params.getM_bnoSeguimiento() ? 1 : 0) + ",'"
                        + fecha + "'";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity.status(500).body("Ocurrió un problema al guardar la información.");
        }
        return ResponseEntity.ok("Agregado Exitosamente");
    }

    @GetMapping("/SisEstatus/getListadoViajes")
    public ResponseEntity<?> sisEstatusViaje(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "\tIdEstatusViaje,\n" +
                    "\tAbreviacion,\n" +
                    "\tEstatus,\n" +
                    "\tColor,\n" +
                    "\tNoEnviarCorreo,\n" +
                    "Carga,\n" +
                    "ArchivoEDI,\n" +
                    "(SELECT Usuario FROM CatUsuarios WHERE IdUsuario = ESTATUSVIAJE.CreadoPor) AS CreadoPor,\n" +
                    "CreadoEl,\n" +
                    "(SELECT Usuario FROM CatUsuarios WHERE IdUsuario = ESTATUSVIAJE.ModificadoPor) AS ModificadoPor,\n" +
                    "ModificadoEl\n" +
                    "\tFROM CatEstatusViajePQ AS ESTATUSVIAJE";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_nIdEstatusViaje", rs.getInt("IdEstatusViaje"));
                json.put("m_sAbreviacion", rs.getString("Abreviacion"));
                json.put("m_sEstatus", rs.getString("Estatus"));
                json.put("m_sColor", rs.getString("Color"));
                json.put("m_bNoEnviarCorreo", rs.getBoolean("NoEnviarCorreo"));
                json.put("m_bCarga", rs.getBoolean("Carga"));
                json.put("m_bArchivoEDI", rs.getBoolean("ArchivoEDI"));
                json.put("m_sCreadoPor", rs.getString("CreadoPor"));
                json.put("m_sCreadoEl", rs.getString("CreadoEl"));
                json.put("m_sModificadoPor", rs.getString("ModificadoPor"));
                json.put("m_sModificadoEl", rs.getString("ModificadoEl"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }

    }
}
