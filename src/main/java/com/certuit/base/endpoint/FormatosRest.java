package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.FormatoRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api")
public class FormatosRest {
    @Autowired
    DBConection dbConection;

    @PostMapping("/Formato/Agregar")
    public ResponseEntity<?> agregarFormato(@RequestHeader("RFC") String rfc,
                                            @RequestPart("request") FormatoRequest request,
                                            @RequestPart("file") MultipartFile file,
                                            @RequestPart(value = "image", required = false) MultipartFile image)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String encodedFileString = Base64.getEncoder().encodeToString(file.getBytes());
            String encodedImageString = null;
            if (image != null) {
                encodedImageString = "'<WINDEV_TABLE>\n" +
                        "  <LOOP_CatFormatosImagenes>\n" +
                        "    <ATT_Imagen>" + Base64.getEncoder().encodeToString(image.getBytes()) + "</ATT_Imagen>\n" +
                        "    <ATT_ImagenNombreArchivo>logo(2).png</ATT_ImagenNombreArchivo>\n" +
                        "    <ATT_IdFormatoImagen>0</ATT_IdFormatoImagen>\n" +
                        "  </LOOP_CatFormatosImagenes>\n" +
                        "</WINDEV_TABLE>'";
            }


            String query = "exec usp_CatFormatosAgregar '" + request.getFormato() + "'," + request.getTipoProceso()
                    + ",'" + encodedFileString + "','" + fileName + "'," + request.getIdUsuario() + ",'"
                    + request.getFecha() + "',1," + encodedImageString + ",null";

            Statement statement = jdbcConnection.createStatement();
            int rs = statement.executeUpdate(query);

            if (rs == 0) {
                return ResponseEntity.ok("Se registró el formato exitosamente");

            } else {
                return ResponseEntity.status(500).body("Ocurrio un problema al guardar la información");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Formato/Modificar/{id}")
    public ResponseEntity<?> modificarFormato(@PathVariable("id") int id,
                                              @RequestHeader("RFC") String rfc,
                                              @RequestPart("request") FormatoRequest request
            /*@RequestPart("file") MultipartFile file,*/ /*@RequestPart("image") MultipartFile image*/)
            throws Exception {

        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

                // String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                //String encodedFileString = Base64.getEncoder().encodeToString(file.getBytes());
       /* String encodedImageString = null;
        if (image != null){
            encodedImageString = "'<WINDEV_TABLE>\n" +
                    "  <LOOP_CatFormatosImagenes>\n" +
                    "    <ATT_Imagen>" + Base64.getEncoder().encodeToString(image.getBytes()) +  "</ATT_Imagen>\n" +
                    "    <ATT_ImagenNombreArchivo>logo(2).png</ATT_ImagenNombreArchivo>\n" +
                    "    <ATT_IdFormatoImagen>0</ATT_IdFormatoImagen>\n" +
                    "  </LOOP_CatFormatosImagenes>\n" +
                    "</WINDEV_TABLE>'" ;
        }*/

                String query = "exec usp_CatFormatosModificarPQ " + id + ",'"
                        /* + encodedFileString +"','" +fileName+ "',"*/
                        + request.getFormato() + "'," + request.getTipoProceso() + "," + request.getIdUsuario()
                        + ",'" + request.getFecha() + "',1"; /*+ encodedImageString+*/

                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se registró el formato exitosamente");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocurrió  un problema al guardar la información");
        }
    }

    @GetMapping("/Formato/Proceso/{id}")
    public ResponseEntity<?> obtenerFormatosProceso(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "SELECT\n" +
                    "\tIdFormato as m_nIdFormato,\n" +
                    "\tFormato as m_sFormato,\n" +
                    "\tTipoProceso as m_nTipoProceso,\n" +
                    "\tActivo as m_bActivo,\n" +
                    "\tNombreArchivo as m_sNombreArchivo\n" +
                    "\tFROM CatFormatos where TipoProceso = " + id + " and Activo = 1";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Formato/{id}")
    public ResponseEntity<?> obtenerFormatoId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "SELECT\n" +
                    "\tIdFormato as m_nIdFormato,\n" +
                    "\tFormato as m_sFormato,\n" +
                    "\tTipoProceso as m_nTipoProceso,\n" +
                    "\tNombreArchivo as m_sNombreArchivo,\n" +
                    "\tFormatoWDE as m_sFormatoWDE,\n" +
                    "\tActivo as m_bActivo,\n" +
                    "\tModificadoEl as m_sModificadoEl\n" +
                    "\tFROM CatFormatos where IdFormato = " + id + " and Activo = 1";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Formato/GetListado")
    public ResponseEntity<?> obtenerFormatos(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "    CF.IdFormato AS m_nIdFormato,\n" +
                    "    CF.Formato AS m_sFormato,\n" +
                    "    CF.TipoProceso AS m_nTipoProceso,\n" +
                    "    CASE\n" +
                    "        WHEN CF.TipoProceso = 42 THEN 'Viajes Paquetería'\n" +
                    "        WHEN  CF.TipoProceso = 43 THEN 'Viajes Cliente TXT'\n" +
                    "        WHEN  CF.TipoProceso = 44 THEN 'Viajes Cliente EXCEL'\n" +
                    "        WHEN  CF.TipoProceso = 201 THEN 'Citas'\n" +
                    "        WHEN  CF.TipoProceso = 202 THEN 'Recibo'\n" +
                    "        WHEN  CF.TipoProceso = 203 THEN 'Embarque'\n" +
                    "        WHEN  CF.TipoProceso = 204 THEN 'Embarque Logística de Almacén'\n" +
                    "        WHEN  CF.TipoProceso = 205 THEN 'Informe ÚltimaMilla'\n" +
                    "        WHEN  CF.TipoProceso = 206 THEN 'Informe CFDI'\n" +
                    "        WHEN  CF.TipoProceso = 207 THEN 'Movimientos Almacén'\n" +
                    "        WHEN  CF.TipoProceso = 208 THEN 'Reporte Kárdex Insumos'\n" +
                    "        WHEN  CF.TipoProceso = 209 THEN 'Reporte Existencias'\n" +
                    "        WHEN  CF.TipoProceso = 221 THEN 'Reporte Inventario Físico'\n" +
                    "        WHEN  CF.TipoProceso = 210 THEN 'Recolección'\n" +
                    "        WHEN  CF.TipoProceso = 211 THEN 'Embarque'\n" +
                    "        WHEN  CF.TipoProceso = 212 THEN 'Guía'\n" +
                    "        WHEN  CF.TipoProceso = 213 THEN 'Guía Etiqueta'\n" +
                    "        WHEN  CF.TipoProceso = 214 THEN 'Informe'\n" +
                    "        WHEN  CF.TipoProceso = 215 THEN 'Informe Última Milla'\n" +
                    "        WHEN  CF.TipoProceso = 216 THEN 'CFDI Primera Milla'\n" +
                    "        WHEN  CF.TipoProceso = 217 THEN 'CFDI Última Milla'\n" +
                    "        WHEN  CF.TipoProceso = 218 THEN 'CFDI Timbrado Viajes'\n" +
                    "        WHEN  CF.TipoProceso = 219 THEN 'Corte de Caja'\n" +
                    "        WHEN  CF.TipoProceso = 221 THEN 'Reporte Inventario Físico'\n" +
                    "        WHEN  CF.TipoProceso = 220 THEN 'Corte de Caja General'\n" +
                    "        WHEN  CF.TipoProceso = 222 THEN 'Informe'\n" +
                    "        WHEN  CF.TipoProceso = 223 THEN 'Guía Etiqueta Rangos'\n" +
                    "        ELSE 'Sin definir'\n" +
                    "        END AS m_sNombreTipoProceso,\n" +
                    "    CF.FormatoWDE AS m_sFormatoWDE,\n" +
                    "    CF.NombreArchivo AS m_sNombreArchivo,\n" +
                    "    ISNULL((SELECT TOP 1 CU.Nombre FROM CatUsuarios CU WHERE CU.IdUsuario = CF.CreadoPor),'') AS m_sCreadoPor,\n" +
                    "    CF.CreadoEl AS m_sCreadoEl,\n" +
                    "    ISNULL((SELECT TOP 1 CU.Nombre FROM CatUsuarios CU WHERE CU.IdUsuario = CF.ModificadoPor),'') AS m_sModificadoPor,\n" +
                    "    CF.ModificadoEl AS m_sModificadoEl,\n" +
                    "    CF.Activo AS m_bActivo\n" +
                    "FROM CatFormatos CF\n" +
                    "WHERE CF.TipoProceso >= 42 AND CF.Activo = 1;";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
