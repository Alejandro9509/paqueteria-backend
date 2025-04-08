package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.GuiaService;
import com.certuit.base.service.base.UltimaMillaService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.certuit.base.util.ForHuaweiCloudFunctions.*;

@RestController
@RequestMapping(value = "/api")
public class UltimaMillaAppRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    GuiaService guiaService;

    @GetMapping("/GetParadasOperador/{idOperador}/{fecha}")
    public ResponseEntity<?> obtenerParadasOperador(@PathVariable("idOperador") int idOperador,
                                                    @PathVariable("fecha") String fecha,
                                                    @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT TOP 1\n" +
                    "parada.m_nIdProParadaUltimaMilla as m_nIdParadaUltimaMilla,\n" +
                    "parada.m_nIdUltimaMilla as m_nIdUltimaMilla,\n" +
                    "FORMAT(ultimaMilla.m_dFecha,'dd/MM/yyyy') as m_dFecha,\n" +
                    "ultimaMilla.m_nCreadoPor as creadoPor,\n" +
                    "CONCAT(operador.Nombres,' ',operador.ApellidoPaterno,' '," +
                    "operador.ApellidoPaterno) as m_snNombreOperador,\n" +
                    "operador.IdOperador as m_nIdOperador,\n" +
                    "unidad.IdUnidad as m_nIdUnidad,\n" +
                    "ultimaMilla.TipoSistema as tipoSistema,\n" +
                    "unidad.Placas as m_sPlacasUnidad\n" +
                    "  FROM ParadaUltimaMillaPQ as parada\n" +
                    "  inner join CatUnidades as unidad\n" +
                    "  on parada.m_nIdUnidad = unidad.IdUnidad\n" +
                    "  inner join CatOperadores as operador\n" +
                    "  on parada.m_nIdOperador = operador.IdOperador\n" +
                    "  inner join ProUltimaMillaPQ as ultimaMilla\n" +
                    "  on parada.m_nIdUltimaMilla = ultimaMilla.m_nIdUltimaMilla\n" +
                    "WHERE parada.m_nIdOperador = " + idOperador + " and parada.Completada = 0 and parada.Activa = 1";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                jsonObject.put("creadoPor", 0);
                jsonObject.put("m_nIdOperador", idOperador);
                jsonObject.put("m_sPlacasUnidad", "");
                jsonObject.put("m_nIdUltimaMilla", 0);
                jsonObject.put("m_dFecha", fecha);
                jsonObject.put("m_nIdUnidad", 0);
                jsonObject.put("m_nIdParadaUltimaMilla", 0);
                jsonObject.put("m_snNombreOperador", "");
                jsonObject.put("m_arrClsProGuia", new ArrayList<>());
            } else {
                jsonObject.put("m_arrClsProGuia",
                        guiaService.getGuiasUltimaMilla(jdbcConnection,
                                jsonObject.getInt("m_nIdParadaUltimaMilla")));
            }
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /*CAMBIA DE ESTATUS CADA PARADA DE LA RUTA*/
    @PutMapping("/UltimaMilla/CambiarEstatusParada/{idEstatus}/{idParadaGuia}")
    public ResponseEntity<?> carbiarEstatus(@RequestHeader("RFC") String rfc,
                                            @PathVariable("idEstatus") int idEstatus,
                                            @PathVariable("idParadaGuia") int idParadaGuia,
                                            @RequestBody TerminarParadaUltimaMillaRequest request)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_UltimaMillaCambiarEstatusParadaPQ "
                        + idParadaGuia
                        + ", " + idEstatus
                        + ",'" + request.getM_sMotivo()
                        + "'," + request.getM_nMontoRecibido()
                        + "," + (request.isM_bQRValidado() ? 1 : 0)
                        + ",'" + request.getM_nIdTipoPago()
                        + "','" + request.getM_sReceptor() + "'"
                        + ",'" + request.getHoraEntrega() + "'";

                statement.executeUpdate(query);

                return ResponseEntity.ok("Se actualizó la información correctamente");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar la información.");
        }

    }

    /*MARCA COMO COMPLETADA LA RUTA*/
    @PutMapping("/UltimaMilla/TerminarRuta/{IdDetalleParada}")
    public ResponseEntity<?> terminarRutaUltimaMilla(@RequestHeader("RFC") String rfc,
                                                     @PathVariable("IdDetalleParada") int id)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "UPDATE ParadaUltimaMillaPQ set Completada = 1 where m_nIdProParadaUltimaMilla =" + id;
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);
            query = "exec usp_ProUltimaMillaCambiarEstatusUnidadPQ 1," + id + ",'La unidad regresó de su ruta'";
            statement.executeUpdate(query);

            return ResponseEntity.ok("Se actualizó la información correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Guia/SubirImagen")
    public ResponseEntity<?> subirImagenPaquete(@RequestHeader("RFC") String rfc,
                                                @RequestHeader("ImagenNombreArchivo") String nombreArchivo,
                                                @RequestHeader("Descripcion") String descripcion,
                                                @RequestHeader("IdGuia") int idGuia,
                                                @RequestHeader("EsRecoleccion") int esRec,
                                                @RequestHeader("TipoArchivo") int tipo,
                                                @RequestBody byte[] byteArray) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                if (idGuia <= 0) {
                    

                    return ResponseEntity.status(403).body("El identificador es un campo requerido.");
                }
                String sCarpeta = "GMTERPV8PQ/";

                String bufContent = nombreArchivo;
                String bufContentSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(byteArray);
                nombreArchivo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "_"
                        + nombreArchivo; //ESTA LINEA ES PORQUE CUANDO EL nombreArchivo ES EL MISMO QUE EL DE OTRA IMAGEN SE TOMA COMO LA MISMA Y ESO NO DEBE PASAR
                String baseName = FilenameUtils.getBaseName(nombreArchivo);
                String extension = FilenameUtils.getExtension(nombreArchivo);

                String sPrefijoKey = "AWS4";
                String sSecretAccessKey = "oaLMImmaTrq4FqTevwD5A3k5C90IM6v6Wo8Qw1WT";
                String sAccessKeyId = "6ROWJGWMNFPXA19YOETK";

                //es la region donde esta contratdo el servicio en este caso mexico
                String sAWSRegion = "na-mexico-1";
                String sAWSServicio = "s3";
                String sAWSRequest = "aws4_request";
                String sContentType = "text/plain";
                String mg_sBucket_and_EndPoint = "obs-gmterp-produccion.obs.na-mexico-1.myhuaweicloud.com";
                String sContentSHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
                String sSignedHeaders = "content-type;host;x-amz-content-sha256;x-amz-date";

                sCarpeta += org.apache.commons.codec.digest.DigestUtils.sha256Hex(baseName) + "." + extension;
                String dtFechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
                String dtFechaHoraSQL = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String dtFechaHoraUTC = LocalDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
                String fecha = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T";
                String hora = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("HHmmss")) + "Z";
                String sFechaHoraISO = fecha + hora;

                String sCanonicalHeader = "content-type" + ":" + sContentType + "\n" + "host" + ":"
                        + mg_sBucket_and_EndPoint + "\n" + "x-amz-content-sha256" + ":" + bufContentSHA256
                        + "\n" + "x-amz-date" + ":" + sFechaHoraISO + "\n";
                //String sCanonicalRequest = "GET" + "\n" + sFileName + "\n" + "" + "\n" + sCanonicalHeader + "\n" + sSignedHeaders + "\n" + sContentSHA256;

                //  String stringCanonical = org.apache.commons.codec.digest.DigestUtils.sha256Hex(sCanonicalRequest);
                String sCanonicalRequest = "PUT" + "\n" + "/" + sCarpeta + "\n\n" + sCanonicalHeader + "\n"
                        + sSignedHeaders + "\n" + bufContentSHA256;
                String stringCanonical = org.apache.commons.codec.digest.DigestUtils.sha256Hex(sCanonicalRequest);

                String sStringToSign = "AWS4-HMAC-SHA256" + "\n" + sFechaHoraISO + "\n"
                        + dtFechaHoraUTC.substring(0, 8) + "/" + sAWSRegion + "/" + sAWSServicio + "/" + sAWSRequest
                        + "\n" + stringCanonical;

                String bufClave = sPrefijoKey + sSecretAccessKey;
                String sFecha = dtFechaHoraUTC.substring(0, 8);

                byte[] keydate = hacerHMAC256(bufClave.getBytes(), sFecha);
                byte[] keyregion = hacerHMAC256(keydate, sAWSRegion);
                byte[] keyservice = hacerHMAC256(keyregion, sAWSServicio);
                byte[] keyrequest = hacerHMAC256(keyservice, sAWSRequest);
                byte[] keySignature = hacerHMAC256(keyrequest, sStringToSign);
                String signatureFinal = bytestoHex(keySignature);

                String sAutorization = "AWS4-HMAC-SHA256 Credential=" + sAccessKeyId + "/"
                        + dtFechaHoraUTC.substring(0, 8) + "/" + sAWSRegion + "/" + sAWSServicio + "/"
                        + sAWSRequest + ", SignedHeaders=" + sSignedHeaders + ", Signature=" + signatureFinal;
                //  List<Sucursal> sucursalList = apiService.getImage(sFileName,sContentSHA256,sFechaHoraISO, sAutorization);
                //String sContentSHA256=org.apache.commons.codec.digest.DigestUtils.sha256Hex(bufContent);
                String clen = Integer.toString(byteArray.length);

                URL url = new URL("https://obs-gmterp-produccion.obs.na-mexico-1.myhuaweicloud.com"
                        + "/" + sCarpeta);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("x-amz-content-sha256", bufContentSHA256);
                con.setRequestProperty("x-amz-date", sFechaHoraISO);
                con.setRequestProperty("Content-Length", clen);
                con.setRequestProperty("Authorization", sAutorization);
                con.setRequestProperty("Content-Type", "text/plain");
                con.setRequestProperty("User-Agent", "gmterpv8");
                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    os.write(byteArray, 0, byteArray.length);
                    os.flush();
                    os.close();
                }
                //con.connect();
                int status = con.getResponseCode();
                //POR SI SE QUIEREN DEBUGGEAR ERRORES
                Reader streamReader = null;

                if (status > 299) {
                    streamReader = new InputStreamReader(con.getErrorStream());
                /*
                String aa;
                BufferedReader in = new BufferedReader(
                        streamReader);
                StringBuilder sb = new StringBuilder();
                while ((aa = in.readLine()) != null) {
                    sb.append(aa);
                }*/
                    return ResponseEntity.status(500).body("Hubo un error al guardar la información. Intente más tarde.");
                } else {
                    String query = "EXEC usp_ProTerminarParadaImagenesAgregar " + idGuia + ", " + esRec + ", ?"
                            + ", ?" + ", ?" + ", " + 1 + ", ?, ?" + ", " + 1 + ", " + tipo;
                    PreparedStatement ps = jdbcConnection.prepareStatement(query);
                    ps.setString(1, "");
                    ps.setString(2, "/" + sCarpeta);
                    ps.setString(3, descripcion);
                    ps.setString(4, dtFechaHoraSQL);
                    ps.setString(5, null);
                    //ps.setString(3,descripcion);
                    ps.execute();
                    String whoknows = "";

                    return ResponseEntity.ok("Agregado Exitoso");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar la información. Intente más tarde.");
        }
    }


}


