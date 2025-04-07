package com.certuit.base.endpoint;

import com.certuit.base.service.base.ExcelExportService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/api")
public class UtilsRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Utilerias/GetFechaInicio")
    public ResponseEntity<?> getFechaInicial(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT FORMAT(SWITCHOFFSET(SYSDATETIMEOFFSET(),'-06:00'),'yyyy-MM-dd') as Fecha";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Utilerias/GetFechaFinal")
    public ResponseEntity<?> getFechaFinal(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT FORMAT(SWITCHOFFSET(SYSDATETIMEOFFSET(),'-06:00'),'yyyy-MM-dd') as Fecha";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Utilerias/ValidaDerechos/{idUsuario}/{idPrivilegio}/{idTipo}")
    public ResponseEntity<?> validaDerechos(@PathVariable("idUsuario") int idUsuario,
                                            @PathVariable("idPrivilegio") int idPrivilegio,
                                            @PathVariable("idTipo") int idTipo,
                                            @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String sConsulta = "SELECT 1 as existe\n" +
                    "FROM CatUsuariosDerechos cud Inner JOIN SisProcesos sp on cud.IdProceso = sp.IdProceso\n" +
                    " WHERE cud.IdUsuario= " + idUsuario + "";
            switch (idTipo) {
                case 1:
                    sConsulta += " and sp.IdModuloPaqueteria = " + idPrivilegio + " and sp.TipoPaqueteria = 1 ";
                    break;
                case 2:
                    sConsulta += " and sp.IdModuloPaqueteria =" + idPrivilegio + " and sp.TipoPaqueteria = 2 ";
                    break;
                case 3:
                    sConsulta += " and sp.IdModuloPaqueteria = " + idPrivilegio + " and sp.TipoPaqueteria = 3 ";
                    break;

            }
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(sConsulta);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Utilerias/descargar-plantilla-importacion-embarques/{idCliente}")
    public void generarPlantillaImportacion(HttpServletResponse response, @RequestHeader("RFC") String rfc,
                                            @PathVariable("idCliente") int idCliente) throws IOException {
        //Seteando headers para crear excel
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String headerValue2 = "application/octet-stream";
            response.setContentType(headerValue2);
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=\"" + currentDateTime + "importarGuias.xlsx\"";
            response.setHeader(headerKey, headerValue);

            ExcelExportService excel = new ExcelExportService(jdbcConnection, idCliente);
            excel.export(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
