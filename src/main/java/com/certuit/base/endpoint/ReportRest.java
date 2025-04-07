package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.EtiquetaRequest;
import com.certuit.base.service.base.ReportServices;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ReportRest {

    @Autowired
    DBConection dbConection;

    @Autowired
    ReportServices reportServices;

    @GetMapping("/GenerarReporte/Guia/{id}")
    public ResponseEntity<?> generarReporteGuia(@PathVariable("id") int id,
                                                @RequestHeader("RFC") String rfc) throws JRException {
        return ResponseEntity.ok(reportServices.generateReportGuia(1, id , rfc));
    }

    @GetMapping("/GenerarReporte/Informe/{id}")
    public ResponseEntity<?> generarReporteInforme(@PathVariable("id") int id,
                                                   @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReportInforme(1, id, rfc));
    }

    @GetMapping("/GenerarReporte/CorteCaja/{id}")
    public ResponseEntity<?> generarReporteCorteCaja(@PathVariable("id") int id,
                                                     @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReportCorteCaja(1, id, rfc));
    }

    @GetMapping("/GenerarReporte/CorteCaja/General/{fecha}")
    public ResponseEntity<?> generarReporteCorteCajaGeneral(@PathVariable("fecha") String fecha,
                                                            @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReportCorteCajaGeneral(1, fecha, rfc));
    }

    @GetMapping("/GenerarReporte/UltimaMilla/{id}")
    public ResponseEntity<?> generarReporetUltimaMilla(@PathVariable("id") int id,
                                                       @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReportUltimaMilla(1, id,  rfc));
    }

    @GetMapping("/GenerarReporte/Recoleccion/{id}")
    public ResponseEntity<?> generarReporetRecoleccion(@PathVariable("id") int id,
                                                       @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReportRecoleccion(1, id,  rfc));
    }

    @GetMapping("/GenerarReporte/Embarque/{id}")
    public ResponseEntity<?> generarReporetEmbarque(@PathVariable("id") int id,
                                                    @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReporEmbarque(1, id,  rfc));
    }

    @GetMapping("/GenerarReporte/CFDI/{id}")
    public ResponseEntity<?> generarReporetCFDI(@PathVariable("id") int id,
                                                @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReporCFDI(1, id,  rfc));
    }

    @GetMapping("/GenerarReporte/CFDIViaje/{id}/{idInforme}")
    public ResponseEntity<?> generarReporetCFDIViaje(@PathVariable("id") int id,
                                                     @PathVariable("idInforme") int idInforme,
                                                     @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReporCFDIViaje(1, id, idInforme,  rfc));
    }

    @GetMapping("/GenerarReporte/CFDIGuia/{id}")
    public ResponseEntity<?> generarReporetCFDIGuia(@PathVariable("id") int id, @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReporCFDIGuia(1, id,  rfc));
    }

    @GetMapping("/GenerarReporte/CFDIRecoleccion/{id}")
    public ResponseEntity<?> generarReporetCFDIRecoleccion(@PathVariable("id") int id, @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReporCFDIRecoleccion(1, id,  rfc));
    }

    @GetMapping("/GenerarReporte/EtiquetasGuia/{id}")
    public ResponseEntity<?> generarReporetEtiquetasGuia(@PathVariable("id") int id, @RequestHeader("RFC") String rfc){
        return ResponseEntity.ok(reportServices.generateReporEtiquetasGuia(1, id,  rfc));
    }

    @PostMapping("/GenerarReporte/ValidarRangosEtiqueta")
    public ResponseEntity<?> validarRangosEtiqueta(@RequestHeader("RFC") String rfc,
                                                   @RequestBody List<EtiquetaRequest> objetos){
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "";
            query = "select (ISNULL(max(IdImpresion), 0) + 1) as IdImpresion from ProGuiaImpresionesEtiquetaPQ";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            int idImpresion = jsonObject.getInt("IdImpresion");
            boolean hayError = false;
            for (EtiquetaRequest etiqueta : objetos) {
                if (idImpresion == 0) {
                    hayError = true;
                    break;
                }
                if (etiqueta.getIdGuia() == 0) {
                    hayError = true;
                    break;
                }
                if (etiqueta.getRangoInicio() == 0) {
                    hayError = true;
                    break;
                }
                if (etiqueta.getRangoFin() == 0) {
                    hayError = true;
                    break;
                }
                if (etiqueta.getIdPaquete() == 0) {
                    hayError = true;
                    break;
                }
            }
            if (hayError){
                return ResponseEntity.status(500).body("Hubo un error al procesar los rangos dados.");
            }
            // Recorrer la lista de objetos
            for (EtiquetaRequest etiqueta : objetos) {
                etiqueta.setIdImpresion(idImpresion);
                query = "insert into ProGuiaImpresionesEtiquetaPQ(IdImpresion, IdPaquete, RangoInicio, RangoFin, IdGuia) " +
                        "VALUES (" + idImpresion +
                        ", " + etiqueta.getIdPaquete() +
                        ", " + etiqueta.getRangoInicio() +
                        ", " + etiqueta.getRangoFin() +
                        ", " + etiqueta.getIdGuia() +
                        ")";
                statement.executeUpdate(query);
            }
            EtiquetaRequest etiquetaResponse = new EtiquetaRequest();
            etiquetaResponse.setIdImpresion(idImpresion);
            return ResponseEntity.ok(etiquetaResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al validar los rangos.");
        }
    }

    @GetMapping("/GenerarReporte/EtiquetasGuiaRangos/{id}")
    public ResponseEntity<?> generarReporetEtiquetasGuiaRangos(@PathVariable("id") int idImpresion,
                                                               @RequestHeader("RFC") String rfc){
        try {
            return ResponseEntity.ok(reportServices.generateReporEtiquetasParcialesGuia(1, idImpresion,  rfc));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al generar el archivo.");
        }
    }
}
