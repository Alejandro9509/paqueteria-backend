package com.certuit.base.endpoint;

import com.certuit.base.domain.dto.DataNotification;
import com.certuit.base.domain.dto.Notification;
import com.certuit.base.domain.dto.Push;
import com.certuit.base.domain.enums.ClickAction;
import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.*;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.PaqueteParcial;
import com.certuit.base.util.UtilFuctions;
import org.apache.commons.lang.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.certuit.base.util.ForHuaweiCloudFunctions.*;
import static com.certuit.base.util.UtilFuctions.convertObject;

@RestController
@RequestMapping(value = "/api")
public class UltimaMillaRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    InformesService informesService;
    @Autowired
    GuiaService guiaService;
    @Autowired
    UltimaMillaService ultimaMillaService;
    @Autowired
    PaquetesService paquetesService;
    @Autowired
    RecoleccionesRest recoleccionesRest;
    @Autowired
    GuiasRest guiasRest;

    @PostMapping("/UltimaMilla/ValidarUnidades")
    public ResponseEntity<?> getUnidadesValidacion(@RequestBody Integer[] unidades, @RequestHeader("RFC") String rfc)
            throws Exception {
        StringBuilder sbf = new StringBuilder();
        sbf.append(Arrays.toString(unidades).replace("[", "")  //remove the right bracket
                .replace("]", ""));
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "IF EXISTS (select m_nIdUnidad from ParadaUltimaMillaPQ where m_nIdUnidad in (" + sbf + "))\n" +
                    "                BEGIN\n" +
                    "                   SELECT 0 as sePuedeSeleccionar\n" +
                    "                END\n" +
                    "                ELSE\n" +
                    "                BEGIN\n" +
                    "                    SELECT 1 as sePuedeSeleccionar\n" +
                    "                END";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/UltimaMilla/ValidarUnidad/{idUnidad}/{fecha}/{idSucursal}")
    public ResponseEntity<?> validarUnidadDisponible(@PathVariable("idUnidad") int idUnidad,
                                                     @PathVariable("fecha") String fecha,
                                                     @PathVariable("idSucursal") int idSucursal,
                                                     @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            fecha = "'" + fecha + "'";
            String query = "DECLARE @UnidadDisponible BIT = 1;\n" +
                    "SELECT @UnidadDisponible = (IIF(parada.m_nIdProParadaUltimaMilla IS NOT NULL, 0, 1))\n" +
                    "FROM ProUltimaMillaPQ milla\n" +
                    "LEFT JOIN ParadaUltimaMillaPQ parada on milla.m_nIdUltimaMilla = parada.m_nIdUltimaMilla\n" +
                    "WHERE milla.m_dFecha = " + fecha +
                    " AND parada.Activa = 1\n" +
                    " AND milla.m_nIdSucursalReceptora = " + idSucursal +
                    " AND parada.m_nIdUnidad = " + idUnidad +
                    " AND parada.Completada = 0\n" +
                    "SELECT @UnidadDisponible AS UnidadDisponible;";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }


    @PostMapping("/UltimaMilla/Chat")
    public ResponseEntity<?> agregarChat(@RequestHeader("RFC") String rfc, @RequestBody ChatRequest request)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String query = "EXEC usp_ProChatEnLineaEnviarMensajePQ " + (request.isEsOperador() ? 1 : 0) + ","
                    + request.getIdOperador() + "," + request.getIdUsuario() + ",'" + request.getMensaje() + "','"
                    + request.getFechaHora() + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs == 1) {
                    if (!request.isEsOperador()) {
                        try {
                            query = "select DeviceId from CatOperadores where IdOperador = " + request.getIdOperador();
                            ResultSet resultSet = statement.executeQuery(query);
                            JSONObject jsonObject = convertObject(resultSet);
                            PushNotificationService pushNotificationService = new PushNotificationService();
                            pushNotificationService.sendNotification(new Push(
                                    jsonObject.getString("DeviceId"),
                                    "high",
                                    new Notification("default", "Mensaje", request.getMensaje(),
                                            ClickAction.OPEN_CHAT_ACTIVITY.getDescripcion()),
                                    new DataNotification(1L)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return ResponseEntity.ok("Se envió el mensaje correctamente");
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al enviar el mensaje");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al enviar el mensaje");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/UltimaMilla/Chat/Operador")
    public ResponseEntity<?> obtenerChat(@RequestHeader("RFC") String rfc,
                                         @RequestParam("fecha") String fecha,
                                         @RequestParam("idOperador") int idOperador) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String fechaInicial = fecha + " 01:00:00";
            String fechaFinal = fecha + " 23:59:59";
            String query = "select pc.IdOperador as m_nIdOperador, " +
                    "pc.IdUsuario m_sIdUsuario, pc.Operador as m_bEsOperador, pc.FechaHora as m_sFechaHora,\n" +
                    "      CU.Nombre as m_sNombreUsuario,  CO.Nombre AS m_sNombreOperador,pc.Mensaje as m_sMensaje\n" +
                    "from ProChatEnLineaMensajePQ pc\n" +
                    "          left join CatUsuarios CU on pc.IdUsuario = CU.IdUsuario\n" +
                    "    inner join CatOperadores CO on pc.IdOperador = CO.IdOperador where pc.IdOperador = "
                    + idOperador + " and FechaHora >= '" + fechaInicial + "' and FechaHora <= '" + fechaFinal + "'";
            try {
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs != null) {
                    String jsonArray = UtilFuctions.convertArray(rs).toString();
                    return ResponseEntity.ok(jsonArray);
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al recuperar mensajes");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al recuperar mensajes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Informes/GetListadoEstatus/{estatus}")
    public ResponseEntity<?> obtenerListadoEstatusInformes(@PathVariable("estatus") int estatus,
                                                           @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT cin.IdInforme,\n" +
                    "           cin.FolioInforme,\n" +
                    "           cin.Fecha,\n" +
                    "           cin.Hora,\n" +
                    "           cin.IdEstatusInforme,\n" +
                    "           cin.IdViaje,\n" +
                    "           cin.IdRuta,\n" +
                    "           pv.FolioViaje as FolioViaje,\n" +
                    "           cin.IdSucursalEmisora,\n" +
                    "           cs.Sucursal as SucursalEmisora,\n" +
                    "           cin.IdSucursalReceptora,\n" +
                    "           csr.Sucursal as SucursalReceptora,\n" +
                    "           cin.IdOperador,\n" +
                    "           co.Nombres as NombreOperador,\n" +
                    "           co.NumeroOperador as NumeroOperador,\n" +
                    "           cu.Descripcion as Dolly,\n" +
                    "           cur.Codigo as Identificador,\n" +
                    "          cin.IdRemolque1,\n" +
                    "          cin.IdRemolque2,\n" +
                    "          cin.IdDolly,\n" +
                    "\n" +
                    "          cur.Descripcion as Remolque1,\n" +
                    "          curr.Descripcion as Remolque2,\n" +
                    "            cin.PlacasRemolque1,\n" +
                    "            cin.PlacasRemolque2,\n" +
                    "            cin.PlacasDolly,\n" +
                    "            cc.OrigenDestino as CiudadOrigen,\n" +
                    "            ccd.OrigenDestino as CiudadDestino,\n" +
                    "            cr.Descripcion as Ruta,\n" +
                    "               cin.IdCiudadDestino,\n" +
                    "               cin.IdCiudadOrigen,\n" +
                    "               cin.FechaCancelacion,\n" +
                    "               cin.UsuarioCancelacion,\n" +
                    "               cus.Nombre\n" +
                    "FROM ProInformePQ cin\n" +
                    "left JOIN CatUsuarios cus on cin.UsuarioCancelacion= cus.IdUsuario\n" +
                    "left join ProViajesPQ pv on cin.IdViaje = pv.IdViaje\n" +
                    "left join CatSucursales cs on cin.IdSucursalEmisora = cs.IdSucursal\n" +
                    "left join CatSucursales csr on cin.IdSucursalReceptora = cs.IdSucursal\n" +
                    "left join CatOperadores co on co.IdOperador = cin.IdOperador\n" +
                    "left join CatUnidades cu on cin.IdDolly = cu.IdUnidad\n" +
                    "left join CatUnidades cur on cin.IdRemolque1 = cu.IdUnidad\n" +
                    "left join CatUnidades curr on cin.IdRemolque2 = cu.IdUnidad\n" +
                    "left join CatOrigenesDestinos cc on cc.IdOrigenDestino = cin.IdCiudadOrigen\n" +
                    "left join CatOrigenesDestinos ccd on ccd.IdOrigenDestino = cin.IdCiudadDestino\n" +
                    "left join CatDetalleRutasPQ cr on cin.IdRuta = cr.IdDetalleRuta\n" +
                    "where cin.IdEstatusInforme = " + estatus + " ORDER BY Fecha DESC, Hora DESC\n";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            String nombreUsuario;
            String nombre;
            String numeroOperador;
            Time hora;
            String identificador;
            String nombreOperador;
            int idInforme;
            String dolly;
            while (rs.next()) {
                json = new JSONObject();
                nombreUsuario = null;
                nombre = null;
                identificador = "";
                dolly = null;
                numeroOperador = null;
                idInforme = 0;
                nombre = rs.getString("Nombre");
                nombreUsuario = nombre;
                numeroOperador = rs.getString("NumeroOperador");
                dolly = rs.getString("Dolly");
                identificador = rs.getString("Identificador");
                idInforme = rs.getInt("IdInforme");
                hora = rs.getTime("Hora");
                json.put("m_dFecha", rs.getDate("Fecha"));
                json.put("m_dtFechaCancelacion", rs.getTimestamp("FechaCancelacion"));
                json.put("m_sFolioInforme", rs.getString("FolioInforme"));
                json.put("m_sCiudadDestino", rs.getString("CiudadDestino"));
                json.put("m_sCiudadOrigen", rs.getString("CiudadOrigen"));
                json.put("m_nIdEstatusInforme", rs.getInt("IdEstatusInforme"));
                json.put("m_nIdInforme", rs.getInt("IdInforme"));
                json.put("m_nIdOperador", rs.getInt("IdOperador"));
                nombreOperador = rs.getString("NombreOperador");
                json.put("m_sNombreCompleto", nombreOperador);
                json.put("m_sNumeroOperador", numeroOperador);
                json.put("m_sNumeroNombreOperador", numeroOperador + " " + nombreOperador);
                json.put("m_nIdRemolque1", rs.getInt("IdRemolque1"));
                json.put("m_nIdRemolque2", rs.getInt("IdRemolque2"));
                json.put("m_sRemolque1", rs.getString("Remolque1"));
                json.put("m_sRemolque2", rs.getString("Remolque2"));
                json.put("m_sPlacasRemolque1", rs.getString("PlacasRemolque1"));
                json.put("m_sPlacasRemolque2", rs.getString("PlacasRemolque2"));
                json.put("m_sPlacasDolly", rs.getString("PlacasDolly"));
                json.put("m_sRuta", rs.getString("Ruta"));
                json.put("m_nIdRuta", rs.getInt("IdRuta"));
                json.put("m_sSucursalReceptora", rs.getString("SucursalReceptora"));
                json.put("m_sSucursalEmisora", rs.getString("SucursalEmisora"));
                json.put("m_nIdSucursalReceptora", rs.getInt("IdSucursalReceptora"));
                json.put("m_nIdSucursalEmisora", rs.getInt("IdSucursalEmisora"));
                json.put("m_nIdDolly", rs.getInt("IdDolly"));
                json.put("m_sDolly", dolly);
                json.put("m_nIdentificador", identificador);
                json.put("m_sUnidadIdentificador", identificador + " " + dolly);
                json.put("m_nIdViaje", rs.getInt("IdViaje"));
                json.put("m_sFolioViaje", rs.getString("FolioViaje"));
                json.put("m_tHora", DateFormatUtils.format(hora, "HH:mm:ss"));
                json.put("m_sFechayHora", DateFormatUtils.format(rs.getDate("Fecha"), "yyyy-MM-dd")
                        + " " + DateFormatUtils.format(hora, "HH:mm:ss"));
                json.put("m_nIdUsuarioCancelacion", rs.getInt("UsuarioCancelacion"));
                json.put("m_sNombre", nombre);
                json.put("m_sNombreUsuario", nombreUsuario);
                json.put("m_sNombreCompleto", rs.getString("NombreOperador"));
                json.put("m_sSucursalEmisora", rs.getString("SucursalEmisora"));
                json.put("m_sSucursalReceptora", rs.getString("SucursalReceptora"));
                json.put("m_arrClsProGuia", informesService.getListadoPorInformes(idInforme, jdbcConnection));
                array.put(json);
            }
            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/UltimaMilla/EliminarParadaOperador")
    public ResponseEntity<?> eliminarParadas(@RequestBody EliminarParadaRequest request,
                                             @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = "exec usp_ProUltimaMillaParadaEliminarPQ "+request.getIdParada()+","+request.getIdGuia()+","
                +(request.getEsRecoleccion()?1:0);
            try {
                Statement statement = jdbcConnection.createStatement();
                int rs = statement.executeUpdate(query);
                if (rs == 1) {
                    return ResponseEntity.ok("Se eliminó la parada correctamente");
                }else {
                    return ResponseEntity.status(500).body("Ocurrio un problema al guardar la información");
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrio un problema al guardar la información");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/GuardarUltimaMilla")
    public ResponseEntity<?> guardarUltimaMilla(@RequestBody UltimaMillaRequest request,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "";
                ResultSet rs;
//            SI NO HAY ULTIMA MILLA CREADA PARA LA FECHA Y SUCURSAL DADA
                if (request.getIdUltimaMilla() == 0) {
                    query = "select m_nIdUltimaMilla as id from ProUltimaMillaPQ where TipoSistema = 1 and m_dFecha = '" + request.getFecha() + "' and m_nIdSucursalReceptora = " + request.getIdSucursal();
                    rs = statement.executeQuery(query);
                    JSONObject ultimaMilla = UtilFuctions.convertObject(rs);
                    if (ultimaMilla == null) {
                        query = "insert into ProUltimaMillaPQ (m_dFecha, m_nIdSucursalReceptora, m_nCreadoPor) " +
                                "values ('" + request.getFecha() + "', " + request.getIdSucursal() + ", "
                                + request.getCreadoPor() + ")";
                        statement.executeUpdate(query);
                        query = "SELECT IDENT_CURRENT('ProUltimaMillaPQ') as id";
                        rs = statement.executeQuery(query);
                        while (rs.next()) {
                            request.setIdUltimaMilla(rs.getInt("id"));
                        }
                        for (ZonaOperativaRequest zona : request.getArrZonas()) {
                            query = "insert into ProUltimaMillaZonasPQ (m_nIdUltimaMilla, m_nIdZonas) " +
                                    "values (" + request.getIdUltimaMilla() + ", " + zona.getM_nIdZona() + ")";
                            statement.executeUpdate(query);
                        }
                    } else {
                        request.setIdUltimaMilla(ultimaMilla.getInt("id"));
                        for (ZonaOperativaRequest zona : request.getArrZonas()) {
                            query = "insert into ProUltimaMillaZonasPQ (m_nIdUltimaMilla, m_nIdZonas) " +
                                    "values (" + request.getIdUltimaMilla() + ", " + zona.getM_nIdZona() + ")";
                            statement.executeUpdate(query);
                        }
                    }

                }
                for (RutaRequest ruta : request.getRutas()) {
                /*for (GuiaUltimaMilla guia : ruta.getGuias()) {
                    query = "EXEC usp_ProGuiaGenerarRutaPQ "+guia.getIdGuia()+", '"+guia.getLng()+"', '"+guia.getLat()+"'"
                            +", "+guia.getOrden()+", "+(guia.isEsRecoleccion() ? 1 : 0)+", '"+guia.getHoraEstimada()+"',"+guia.getKilometros();
                    statement.executeUpdate(query);
                }*/
//                CREA LA RUTA
                    query = "insert into ParadaUltimaMillaPQ " +
                            "(m_nIdUnidad, m_nIdOperador, m_nIdUltimaMilla,IdRemolque1, IdRemolque2, IdDolly,Completada, Activa) " +
                            "values (" + ruta.getIdUnidad() + ", " + ruta.getIdOperador() + ", "
                            + request.getIdUltimaMilla() + ", " + ruta.getIdRemolque1() + ","
                            + ruta.getIdRemolque2() + "," + ruta.getIdDolly() + ", 0, 1)";
                    statement.executeUpdate(query);
                    query = "SELECT IDENT_CURRENT( 'ParadaUltimaMillaPQ' ) as id";
                    rs = statement.executeQuery(query);
                    while (rs.next()) {
                        ruta.setM_nIdParadaUltimaMilla(rs.getInt("id"));
                    }

//                ACTUALIZA LOS ESTATUS DE LA UNIDAD
                    query = "exec usp_ProUltimaMillaCambiarEstatusUnidadPQ 2," + ruta.getM_nIdParadaUltimaMilla()
                            + ",'La se asignó a última milla'";
                    statement.executeUpdate(query);
                /*for (GuiaUltimaMilla guia : ruta.getGuias()){
                    query = "insert into ProParadaUltimaMillaGuiasPQ (m_nIdParadaUltimaMilla, m_nIdGuia,EsRecoleccion) " +
                            "values ("+ruta.getM_nIdParadaUltimaMilla()+", "+guia.getIdGuia()+", "+(guia.isEsRecoleccion() ? 1 : 0)+")";
                    statement.executeUpdate(query);
                }*/
//                ACTUALIZA LOS DATOS DE LA GUIA CON LOS DATOS DE ULTIMA MILLA
//                ultimaMillaService.guardarRuta(ruta, statement);
//                AGREGA LAS GUIAS A LA RUTA DE ULTIMA MILLA ESPECIFICADA
                    //ultimaMillaService.agregarGuiasParada(ruta, statement);
                    ultimaMillaService.agregarGuiasParadaEspecificado(ruta, statement, request.getFecha(), request.getHora());
                }
                return ResponseEntity.ok("Se guardó la información");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar la informacion");
        }
    }

    @PutMapping("/UltimaMilla/OrdenarParada")
    public ResponseEntity<?> ordenarParadas(@RequestBody RutaRequest ruta,
                                            @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                ultimaMillaService.cambiarOrdenRuta(ruta, statement);
                return ResponseEntity.ok("Informacion actualizada con éxito");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al actualizar la información. Intente más tarde.");
        }
    }

    @PostMapping("/GetUltimaMillaFecha/{fecha}/{idSucursal}")
    public ResponseEntity<?> getUltimaMillaFecha(@PathVariable("fecha") String fecha,
                                                 @PathVariable("idSucursal") int idSucursal,
                                                 @RequestBody UltimaMillaRequest request,
                                                 @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "SELECT top 1 ultimaMilla.m_nIdUltimaMilla,\n" +
                        "       ultimaMilla.m_dFecha,\n" +
                        "       ultimaMilla.m_nCreadoPor,\n" +
                        "       ultimaMilla.m_nIdSucursalReceptora,\n" +
                        "       sucursal.Sucursal as m_sNombreSucursal\n" +
                        "FROM ProUltimaMillaPQ as ultimaMilla\n" +
                        "         inner join CatSucursales as sucursal\n" +
                        "                    on ultimaMilla.m_nIdSucursalReceptora = sucursal.IdSucursal\n" +
                        "    left join ProUltimaMillaZonasPQ pum on pum.m_nIdUltimaMilla = ultimaMilla.m_nIdUltimaMilla\n" +
                        "WHERE (ultimaMilla.TipoSistema IS NULL or ultimaMilla.TipoSistema = 1)\n" + //VALIDA QUE LA UM SEA DE PAQUETERIA
                        "  and ultimaMilla.m_dFecha = '" + fecha + "'\n" +
                        "  and ultimaMilla.m_nIdSucursalReceptora = " + idSucursal + "\n" +
                        "  and (pum.m_nIdZonas in (" + request.getZonas() + "))";
                ResultSet rs = statement.executeQuery(query);
                JSONObject json = new JSONObject();
                while (rs.next()) {
                    int idUltimaMilla = rs.getInt("m_nIdUltimaMilla");
                    json.put("m_nIdUltimaMilla", idUltimaMilla);
                    json.put("m_dFecha", rs.getString("m_dFecha"));
                    json.put("m_nCreadoPor", rs.getInt("m_nCreadoPor"));
                    json.put("m_nIdSucursalReceptora", rs.getInt("m_nIdSucursalReceptora"));
                    json.put("m_sNombreSucursal", rs.getString("m_sNombreSucursal"));
//                json.put("m_arrClsZonas",ultimaMillaService.getZonasByUltimaMilla(statement,json.getInt("m_nIdUltimaMilla")));
                    json.put("m_arrClsZonas", new ArrayList<>());
                    json.put("m_arrClsParadaUltimaMilla", ultimaMillaService.getParadasByIdUltimaMilla(statement,
                            json.getInt("m_nIdUltimaMilla")));
                    return ResponseEntity.ok(json.toString());
                }
                json.put("m_nIdUltimaMilla", 0);
                json.put("m_dFecha", fecha);
                json.put("m_nCreadoPor", 0);
                json.put("m_nIdSucursalReceptora", 0);
                json.put("m_sNombreSucursal", "");
                json.put("m_arrClsZonas", new ArrayList<>());
                json.put("m_arrClsParadaUltimaMilla", new ArrayList<>());


                return ResponseEntity.ok(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al actualizar la información. Intente más tarde.");
        }
    }

    @PostMapping("/UltimaMilla/GetListadoPaquetesByInforme/{idInforme}")
    public ResponseEntity<?> getPaquetesByInforme(@PathVariable("idInforme") int idInforme,
                                                  @RequestBody UltimaMillaRequest request,
                                                  @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_ProUltimaMillaGetPaquetesListadoInformePQ '" + idInforme + "', '"
                        + request.getZonas() + "'";
                ResultSet rs = statement.executeQuery(query);
                JSONArray jsonResponse = UtilFuctions.convertArray(rs);
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject jsonTemp = jsonResponse.getJSONObject(i);
                    if (jsonTemp.getBoolean("m_bEsRecoleccion")) {
                        jsonTemp.put("m_parrPaquetes", ultimaMillaService.getListadoPaquetesByIdRecoleccion(statement,
                                jsonTemp.getInt("m_nId")));
                    } else {
                        jsonTemp.put("m_arrPaquetes", ultimaMillaService.getListadoPaquetesByIdGuia(statement,
                                jsonTemp.getInt("m_nId")));
                    }
                }
                return ResponseEntity.ok(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }

    @PostMapping("/UltimaMilla/GetListadoPaquetesByViaje/{idViaje}")
    public ResponseEntity<?> getPaquetesByViaje(@PathVariable("idViaje") int idViaje,
                                                @RequestBody UltimaMillaRequest request,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_ProUltimaMillaGetPaquetesListadoViajePQ '" + idViaje + "', '"
                        + request.getZonas() + "'";
                ResultSet rs = statement.executeQuery(query);
                JSONArray jsonResponse = UtilFuctions.convertArray(rs);
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject jsonTemp = jsonResponse.getJSONObject(i);
                    if (jsonTemp.getBoolean("m_bEsRecoleccion")) {
                        jsonTemp.put("m_parrPaquetes", ultimaMillaService.getListadoPaquetesByIdRecoleccion(statement,
                                jsonTemp.getInt("m_nId")));
                    } else {
                        jsonTemp.put("m_arrPaquetes", ultimaMillaService.getListadoPaquetesByIdGuia(statement,
                                jsonTemp.getInt("m_nId")));
                    }
                }
                return ResponseEntity.ok(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }

    @PostMapping("/UltimaMilla/GetListadoPaquetesByUnidadOperador/{idUnidad}/{idOperador}")
    public ResponseEntity<?> getPaquetesByViaje(@PathVariable("idUnidad") int idUnidad,
                                                @PathVariable("idOperador") int idOperador,
                                                @RequestBody UltimaMillaRequest request,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_ProUltimaMillaGetPaquetesListadoOperadorUnidadPQ '" + idUnidad + "', '"
                        + idOperador + "', '" + request.getZonas() + "'";
                ResultSet rs = statement.executeQuery(query);
                JSONArray jsonResponse = UtilFuctions.convertArray(rs);
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject jsonTemp = jsonResponse.getJSONObject(i);
                    if (jsonTemp.getBoolean("m_bEsRecoleccion")) {
                        jsonTemp.put("m_parrPaquetes", ultimaMillaService.getListadoPaquetesByIdRecoleccion(statement,
                                jsonTemp.getInt("m_nId")));
                    } else {
                        jsonTemp.put("m_arrPaquetes", ultimaMillaService.getListadoPaquetesByIdGuia(statement,
                                jsonTemp.getInt("m_nId")));
                    }
                }
                return ResponseEntity.ok(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }

    @PutMapping("/UltimaMilla/RemplazarParada/{idParada}/{idPaqueteViejo}")
    public ResponseEntity<?> remplazarParada(@PathVariable("idParada") int idParada,
                                             @PathVariable("idPaqueteViejo") int idPaqueteViejo,
                                             @RequestBody GuiaUltimaMilla request,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_UltimaMillaRemplazarPaquetePQ " + idParada + ", " + idPaqueteViejo + ", "
                        + (request.isEsRecoleccion() ? 1 : 0) + ", " + request.getIdNuevaGuia() + ", "
                        + (request.isNuevoEsRecoleccion() ? 1 : 0) + ", '" + request.getLat() + "', '"
                        + request.getLng() + "'";
                statement.executeUpdate(query);
                return ResponseEntity.ok("Se actualizó la información.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/UltimaMilla/EliminarRuta/{idParada}")
    public ResponseEntity<?> eliminarRuta(@PathVariable("idParada") int idParada, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_ProUltimaMillaEliminarParadaPQ " + idParada;
                statement.executeUpdate(query);
                query = "EXEC usp_ProUltimaMillaCambiarEstatusUnidadPQ 1," + idParada
                        + ", 'Se canceló la ruta de la unidad'";
                statement.executeUpdate(query);
                return ResponseEntity.ok("Se actualizó la información.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }

    @GetMapping("/UltimaMilla/GetParadasEsRecoleccion/{esRecoleccion}/{tracking}")
    public ResponseEntity<?> getParadasRecoleccion(@PathVariable("esRecoleccion") boolean esRecoleccion,
                                                   @PathVariable("tracking") String tracking,
                                                   @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

                String query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ " + (esRecoleccion ? 1 : 0) + UtilFuctions.addString(tracking);
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                String pattern = "yyyy-MM-dd";
                String patten2 = "hh:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(patten2);

                // String date = simpleDateFormat.format("request.getFechaHora()");
                JSONObject json = new JSONObject();
                boolean recoleccionConCita = false;
                boolean embarqueConCita = false;
                int idGuia = 0;
                while (rs.next()) {
                    idGuia = rs.getInt("m_nIdPaquete");
                    json.put("m_nId", idGuia);
                    json.put("m_bEsRecoleccion", esRecoleccion);
                    json.put("m_nEstatusUlimaMilla", rs.getInt("m_nEstatusUlimaMilla"));
                    json.put("m_sEstatusUltimaMilla", rs.getString("m_sEstatusUltimaMilla"));
                    json.put("m_sMotivoCancelacion", rs.getString("m_sMotivoCancelacion"));
                    json.put("m_dFechaRegistro", simpleDateFormat.format(rs.getDate("m_dFechaRegistro")));
                    json.put("m_tHoraRegistro", simpleDateFormat2.format(rs.getTimestamp("m_tHoraRegistro")));
                    json.put("m_nIdTipoDeCobro", rs.getInt("m_nIdTipoDeCobro"));
                    json.put("m_sTipoCobro", rs.getString("m_sTipoCobro"));
                    json.put("m_sFolio", rs.getString("m_sFolio"));
                    json.put("m_sCiudadDestino", rs.getString("m_sCiudadDestino"));
                    json.put("m_sCiudadOrigen", rs.getString("m_sCiudadOrigen"));
                    json.put("m_sLatitud", rs.getString("m_sLatitud"));
                    json.put("m_sLongitud", rs.getString("m_sLongitud"));
                    json.put("m_nMontoACobrar", rs.getInt("m_nMontoACobrar"));
                    json.put("m_sZona", rs.getString("m_sZona"));
                    json.put("m_nIdTipoPago", rs.getInt("m_nIdTipoPago"));
                    json.put("m_sTipoPago", rs.getString("m_sTipoPago"));
                    json.put("m_nImporteFlete", rs.getInt("m_nImporteFlete"));
                    recoleccionConCita = rs.getBoolean("m_bRecoleccionConCita");
                    json.put("m_bRecoleccionConCita", recoleccionConCita);
                    if (recoleccionConCita) {
                        json.put("m_sFechaRecoleccionCita", simpleDateFormat.format(
                                rs.getString("m_sFechaRecoleccionCita")));
                        json.put("m_sHoraCitarRecoleccionMinima", simpleDateFormat2.format(
                                rs.getString("m_sHoraCitarRecoleccionMinima")));
                        json.put("m_sHoraCitaRecoleccionMaxima", simpleDateFormat2.format(
                                rs.getString("m_sHoraCitaRecoleccionMaxima")));
                    }
                    json.put("m_nIdEstatusRecoleccion", rs.getInt("m_nIdEstatusRecoleccion"));
                    json.put("m_sEstatusRecoleccion", rs.getString("m_sEstatusRecoleccion"));
                    //Embarque
                    json.put("m_bEmbarqueConCita", rs.getBoolean("m_bEmbarqueConCita"));
                    embarqueConCita = rs.getBoolean("m_bEmbarqueConCita");
                    if (embarqueConCita) {
                        json.put("m_sFechaEmbarqueCita", simpleDateFormat.format(
                                rs.getString("m_sFechaEmbarqueCita")));
                        json.put("m_sHoraEmbarqueCitaMinima", simpleDateFormat2.format(
                                rs.getString("m_sHoraEmbarqueCitaMinima")));
                        json.put("m_sHoraEmbarqueCitaMaxima", simpleDateFormat2.format(
                                rs.getString("m_sHoraEmbarqueCitaMaxima")));
                    }
                    json.put("m_nIdEstatusGuia", rs.getInt("m_nIdEstatusGuia"));
                    json.put("m_sEstatusGuia", rs.getString("m_sEstatusGuia"));

                    //recoleccion diferente domicilio
                    json.put("m_bRecoleccionDiferenteDomicilio",
                            rs.getBoolean("m_bRecoleccionDiferenteDomicilio"));
                    json.put("m_sDomicilioDetalleRecoleccion",
                            rs.getString("m_sDomicilioDetalleRecoleccion"));
                    json.put("m_sRecogerEnDetalleRecoleccion",
                            rs.getString("m_sRecogerEnDetalleRecoleccion"));
                    json.put("m_sDatosAdicionalesDetalleRecoleccion",
                            rs.getString("m_sDatosAdicionalesDetalleRecoleccion"));

                    //Entrega diferente domicilio

                    json.put("m_bEntregaDiferenteDomicilio", rs.getBoolean("m_bEntregaDiferenteDomicilio"));
                    json.put("m_sDomicilioDetalleEntrega", rs.getString("m_sDomicilioDetalleEntrega"));
                    json.put("m_sEntregarEnDetalleEntrega", rs.getString("m_sEntregarEnDetalleEntrega"));
                    json.put("m_sDatosAdicionalesDetalleEntrega",
                            rs.getString("m_sDatosAdicionalesDetalleEntrega"));

                    //DatosRemitente
                    json.put("m_sNombreRemitente", rs.getString("m_sNombreRemitente"));
                    json.put("m_sCalleRemitente", rs.getString("m_sCalleRemitente"));
                    json.put("m_sNoIntRemitente", rs.getString("m_sNoIntRemitente"));
                    json.put("m_sNoExtRemitente", rs.getString("m_sNoExtRemitente"));
                    json.put("m_sColoniaRemitente", rs.getString("m_sColoniaRemitente"));
                    json.put("m_sRFCRemitente", rs.getString("m_sRFCRemitente"));
                    json.put("m_sDomicilioRemitente", rs.getString("m_sDomicilioRemitente"));
                    json.put("m_sCorreoRemitente", rs.getString("m_sCorreoRemitente"));
                    json.put("m_sCorreoDestinatario", rs.getString("m_sCorreoDestinatario"));
                    json.put("m_sTelefonoRemitente", rs.getString("m_sTelefonoRemitente"));
                    json.put("m_sContactoRemitente", rs.getString("m_sContactoRemitente"));

                    //Datos Destinatario
                    json.put("m_sNombreDestinatario", rs.getString("m_sNombreDestinatario"));
                    json.put("m_sCalleDestinatario", rs.getString("m_sCalleDestinatario"));
                    json.put("m_sNoIntDestinatario", rs.getString("m_sNoIntDestinatario"));
                    json.put("m_sNoExtDestinatario", rs.getString("m_sNoExtDestinatario"));
                    json.put("m_sColoniaDestinatario", rs.getString("m_sColoniaDestinatario"));
                    json.put("m_sContactoDestinatario", rs.getString("m_sContactoDestinatario"));
                    json.put("m_sTelefonoDestinatario", rs.getString("m_sTelefonoDestinatario"));
                    json.put("m_sDomicilioDestinatario", rs.getString("m_sDomicilioDestinatario"));
                    json.put("m_sRFCDestinatario", rs.getString("m_sRFCDestinatario"));
                    json.put("m_sCodigoPostalRemitente", rs.getString("m_sCodigoPostalRemitente"));
                    json.put("m_sCodigoPostalDestinatario", rs.getString("m_sCodigoPostalDestinatario"));
                    json.put("m_nIdTipoServicio", rs.getInt("m_nIdTipoServicio"));
                    json.put("m_sTipoServicio", rs.getString("m_sTipoServicio"));
                    json.put("m_sNombreResponsablePago", rs.getString("m_sNombreResponsablePago"));

                    json.put("m_dFechaRegistroInforme", rs.getDate("m_dFechaInforme"));
                    json.put("m_dFechaRegistroViaje", rs.getDate("m_dFechaRegistroViaje"));
                    json.put("m_dFechaSalidaViaje", rs.getDate("m_dFechaSalidaViaje"));
                    json.put("m_dFechaLlegadaViaje", rs.getDate("m_dFechaLlegadaViaje"));
                    json.put("m_dFechaOcurre", rs.getDate("m_dFechaOcurre"));
                    json.put("m_dFechaUltimaMilla", rs.getDate("m_dFechaUltimaMilla"));
                    json.put("m_dFechaCancelacion", rs.getDate("m_dFechaCancelacion"));
                    json.put("m_dFechaCancelacionInforme", rs.getDate("m_dFechaCancelacionInforme"));
                    json.put("m_dHoraInforme", rs.getDate("m_dHoraInforme"));
                    json.put("m_dHoraRegistroViaje", rs.getDate("m_dHoraRegistroViaje"));
                    json.put("m_dHoraSalidaViaje", rs.getDate("m_dHoraSalidaViaje"));
                    json.put("m_dHoraLlegadaViaje", rs.getDate("m_dHoraLlegadaViaje"));
                    statement = jdbcConnection.createStatement();
                    if (esRecoleccion) {
                        json.put("m_parrPaquetes",
                                ultimaMillaService.getListadoPaquetesByIdRecoleccion(statement, idGuia));
                    } else {
                        json.put("m_parrPaquetes", ultimaMillaService.getListadoPaquetesByIdGuia(statement, 2));
                    }
                }
                if (json.toString().equalsIgnoreCase("{}")) {
                    return ResponseEntity.status(500).body("Numero de guia no encontrado");
                }
                return ResponseEntity.ok(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }

    @PostMapping("/UltimaMilla/paquetes-parciales/agregar/{idParadaUltimaMilla}/{idGuia}")
    public ResponseEntity<?> agregarPaquetesParcialesUM(
            @PathVariable("idParadaUltimaMilla") int idParadaUltimaMilla,
            @PathVariable("idGuia") int idGuia,
            @RequestBody PaqueteParcial[] paquetes,
            @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "";
                Statement statement = jdbcConnection.createStatement();

                query = "SELECT IdParadaGuia,EsEntregaParcial FROM ProParadaUltimaMillaGuiasPQ " +
                        "WHERE EsRecoleccion = 0 and m_nIdParadaUltimaMilla=" + idParadaUltimaMilla
                        + " AND m_nIdGuia=" + idGuia;
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                int idParadaGuia = jsonObject.getInt("IdParadaGuia");
                if (jsonObject.getBoolean("EsEntregaParcial")) {

                    query = "SELECT IdPaquete from ProUltimaMillaGuiasPaquetesParcialesPQ WHERE IdParadaGuia = "
                            + idParadaGuia;
                    rs = statement.executeQuery(query);
                    JSONArray jsonArray = UtilFuctions.convertArray(rs);

                    if (jsonArray.length() > 0) {

                        query = "DELETE from ProUltimaMillaGuiasPaquetesParcialesPQ " +
                                "WHERE IdParadaGuia=" + idParadaGuia;
                        statement.executeUpdate(query);
                    }
                }
                for (PaqueteParcial paquete : paquetes) {
                    query = "EXEC usp_ProUltimaMillaAgregarPaquetesParcialesPQ " + paquete.getIdPaquete() + ","
                            + idParadaGuia + "," + paquete.getNoIndex();
                    statement.executeUpdate(query);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar la información.");
        }
        return ResponseEntity.ok("Se guardó la información correctamente");
    }

    /**
     * Retorna listado de paquetes que se mandaron en entrega parcial.
     */
    @GetMapping("/UltimaMilla/paquetes-parciales/consultar/{idParadaUltimaMilla}/{idGuia}")
    public ResponseEntity<?> obtenerPaquetesParcialesUM(@PathVariable("idParadaUltimaMilla") int idParadaUltimaMilla,
                                                        @PathVariable("idGuia") int idGuia,
                                                        @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();

                String query = "SELECT IdParadaGuia FROM ProParadaUltimaMillaGuiasPQ " +
                        "WHERE m_nIdParadaUltimaMilla=" + idParadaUltimaMilla + " AND m_nIdGuia=" + idGuia
                        + " AND EsRecoleccion=0";
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                int idParadaGuia = jsonObject.getInt("IdParadaGuia");
                JSONArray jsonArrayParadas = paquetesService.getPaquetesParcialesByGuia(idParadaGuia, jdbcConnection);
                return ResponseEntity.ok(jsonArrayParadas.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /**
     * Retorna listado paquetes restantes de una guia que pueden ser agregados a una entrega parcial.
     */
    @GetMapping("/UltimaMilla/paquetes-por-parada/consultar/{idGuia}")
    public ResponseEntity<?> obtenerPaquetesPorParadaUM(@PathVariable("idGuia") int idGuia,
                                                        @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JSONArray jsonArrayParadas = paquetesService.getPaquetesDisponiblesPorParada(idGuia, jdbcConnection);
                return ResponseEntity.ok(jsonArrayParadas.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/UltimaMilla/GetGuiaRecoleccionPorFolio/{folio}")
    public ResponseEntity<?> getGuiaRecoleccionPorFolio(@PathVariable("folio") String folio,
                                                        @RequestHeader("RFC") String rfc) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs;
                String query = "";
                JSONObject jsonAux;
                if (folio.startsWith("RE")) {
                    query = "select IdRecoleccion from ProRecoleccionPQ where FolioRecoleccion = '" + folio + "'";
                    rs = statement.executeQuery(query);
                    jsonAux = convertObject(rs);
                    if (jsonAux == null) {
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "No se encontró la recoleccion.");
                        jsonResponse.put("data", new JSONObject());
                        return ResponseEntity.status(500).body(jsonResponse.toString());
                    } else {
                        jsonAux = new JSONObject(recoleccionesRest.getRecoleccionId(
                                jsonAux.getInt("IdRecoleccion"), rfc).getBody().toString());
                        jsonResponse.put("data", jsonAux);
                    }
                }
                if (folio.startsWith("FG")) {
                    query = "select IdGuia from ProGuiaPQ where FolioGuia = '" + folio + "'";
                    rs = statement.executeQuery(query);
                    jsonAux = convertObject(rs);
                    if (jsonAux == null) {
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "No se encontró la guia.");
                        jsonResponse.put("data", new JSONObject());
                        return ResponseEntity.status(500).body(jsonResponse.toString());
                    } else {
                        jsonAux = new JSONObject(guiasRest.obtenerGuiaById(jsonAux.getInt("IdGuia"), rfc)
                                .getBody().toString());
                        jsonResponse.put("data", jsonAux);
                    }
                }
                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al consultar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al consultar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }

    }

    @GetMapping("/UltimaMilla/GetImagenEvidencia/{idGuia}/{esRecoleccion}")
    public ResponseEntity<?> getImagenEvidencia(@PathVariable("idGuia") int idGuia,
                                                @PathVariable("esRecoleccion") boolean esRecoleccion,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                int idImagen = 0;
                JSONArray response = new JSONArray();
                int esRec = 0;
                if (esRecoleccion) {
                    esRec = 1;
                }
                //OBTENCION DE RUTAS DE IMAGENES
                String query = "select * from CatParadasImagenes where IdGuia=" + idGuia + " and EsRecoleccion=" + esRec;
                Statement statement = jdbcConnection.createStatement();
                String ruta = "";
                String descripcion = "";
                int tipo = 0;
                ResultSet rs = statement.executeQuery(query);

                while (rs.next()) {
                    //POR CADA RUTA DE IMAGEN ENCONTRADA HACE UNA PETICIÓN AL API DE HUAWEI
                    ruta = rs.getString("ImagenNombreArchivo");
                    descripcion = rs.getString("Descripcion");
                    tipo = rs.getInt("Tipo");

                    //OBTENER FECHA
                    String dtFechaHoraUTC = LocalDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
                    String fecha = LocalDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T";
                    String hora = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("HHmmss")) + "Z";
                    String sFechaHoraISO = fecha + hora;

                    //OBTENER STRING TO SIGN
                    String sFileName = ruta;
                    String sCanonicalHeader = "content-type" + ":" + CONTENT_TYPE + "\n" + "host" + ":"
                            + BUCKET_AND_ENDPOINT + "\n" + "x-amz-content-sha256" + ":" + CONTENT_SHA256 + "\n"
                            + "x-amz-date" + ":" + sFechaHoraISO + "\n";
                    String sCanonicalRequest = "GET" + "\n" + sFileName + "\n" + "\n" + sCanonicalHeader + "\n"
                            + SIGNED_HEADERS + "\n" + CONTENT_SHA256;

                    String stringCanonical = org.apache.commons.codec.digest.DigestUtils.sha256Hex(sCanonicalRequest);

                    String sStringToSign = "AWS4-HMAC-SHA256" + "\n" + sFechaHoraISO + "\n"
                            + dtFechaHoraUTC.substring(0, 8) + "/" + AWS_REGION + "/" + AWS_SERVICE + "/"
                            + AWS_REQUEST + "\n" + stringCanonical;

                    //OBTENER SIGNATURE
                    String bufClave = PREFIJO_KEY + SECRET_ACCESS_KEY;
                    String sFecha = dtFechaHoraUTC.substring(0, 8);
                    byte[] keydate = hacerHMAC256(bufClave.getBytes(), sFecha);
                    byte[] keyregion = hacerHMAC256(keydate, AWS_REGION);
                    byte[] keyservice = hacerHMAC256(keyregion, AWS_SERVICE);
                    byte[] keyrequest = hacerHMAC256(keyservice, AWS_REQUEST);
                    byte[] keySignature = hacerHMAC256(keyrequest, sStringToSign);
                    String signatureFinal = bytestoHex(keySignature);

                    String sAutorization = "AWS4-HMAC-SHA256 Credential=" + ACCESS_KEY_ID + "/"
                            + dtFechaHoraUTC.substring(0, 8) + "/" + AWS_REGION + "/" + AWS_SERVICE + "/"
                            + AWS_REQUEST + ", SignedHeaders=" + SIGNED_HEADERS + ", Signature=" + signatureFinal;

                    //CONSTRUCCIÓN DE LA PETICIÓN HTTPS
                    URL url = new URL("https://obs-gmterp-produccion.obs.na-mexico-1.myhuaweicloud.com" + ruta);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("x-amz-content-sha256", CONTENT_SHA256);
                    con.setRequestProperty("x-amz-date", sFechaHoraISO);
                    String auth = "AWS4-HMAC-SHA256 " +
                            "Credential=6ROWJGWMNFPXA19YOETK/20231124/na-mexico-1/s3/aws4_request, " +
                            "SignedHeaders=content-type;host;x-amz-content-sha256;x-amz-date, " +
                            "Signature=0a3b2d95a6a9a568cc77a6b5a513b76ee92f960a0c8d7ea63c23c665a2c886c4";
                    con.setRequestProperty("Authorization", sAutorization);
                    con.setRequestProperty("Content-Type", "text/plain");
                    con.setRequestProperty("User-Agent", "gmterpv8");

                    int status = con.getResponseCode();
                    Reader streamReader = null;
                    //POR SI SE QUIEREN CONOCER LOS HEADERS QUE SE REGRESAN
                    Map<String, List<String>> map = con.getHeaderFields();
                    //POR SI SE QUIEREN DEBUGGEAR ERRORES
                /*    if (status > 299) {
                    streamReader = new InputStreamReader(con.getErrorStream());
                } else {
                    streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.ISO_8859_1);
                }
                String aa;
                BufferedReader in = new BufferedReader(
                        streamReader);
                StringBuilder sb = new StringBuilder();
                while ((aa = in.readLine()) != null) {
                    sb.append(aa);
                }
                in.close();
                */
                    //OBTENCIÓN DEL CONTENIDO DEL REQUEST
                    String stringSinCodificar = readFullyAsString(con.getInputStream(), "ISO-8859-1");
                    String stringBase64 = Base64.getEncoder().encodeToString(
                            stringSinCodificar.getBytes(StandardCharsets.ISO_8859_1));
                    String imagenBase64 = stringBase64.replaceAll("(.{80})", "$1\n");
                    con.disconnect();
                    //SE VA CONSTRUYENDO EL JSON DE RETURN
                    JSONObject json = new JSONObject();
                    json.put("m_nIdImagen", idImagen);
                    json.put("m_sImagen", imagenBase64);
                    json.put("m_sDescripcion", descripcion);
                    json.put("m_nTipoArchivo", tipo);
                    response.put(json);
                    idImagen++;
                }
                return ResponseEntity.ok(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al recuperar la información. Intente más tarde.");
        }
    }
}


