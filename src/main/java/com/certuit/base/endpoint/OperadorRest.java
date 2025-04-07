package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.LoginApp2Request;
import com.certuit.base.domain.request.base.LoginAppRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

@RestController
@RequestMapping(value = "/api")
public class OperadorRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Operadores/GetListado")
    public ResponseEntity<?> getEmbalaje(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "Select \n" +
                    "           co.IdOperador as m_nIdOperador, \n" +
                    "                co.NumeroOperador as m_nNumeroOperador, \n" +
                    "                (co.Nombre) as m_sNombreCompleto,\n" +
                    "                co.ApellidoPaterno as m_sApellidoPaterno, \n" +
                    "                co.ApellidoMaterno as m_sApellidoMaterno, \n" +
                    "                cu.Sucursal as m_sSucursal, \n" +
                    "                cu.IdSucursal as idSucursal,\n" +
                    "                co.EsPermisionario as m_bEsPermisionario, \n" +
                    "                co.Activo as m_bActivo, \n" +
                    "                co.Licencia as m_sLicencia, \n" +
                    "                co.LicenciaVencimiento as m_dLicenciaVencimiento, \n" +
                    "(Select IIF(COUNT(*) = 0, CAST(1 as BIT), CAST(0 as BIT)) from ParadaUltimaMillaPQ ruta " +
                    "inner join CatOperadores op on op.IdOperador = ruta.m_nIdOperador where ruta.Completada = 0 " +
                    "and Activa = 1 and op.IdOperador = co.IdOperador)  as operadorDisponible\n" +
                    "                FROM CatOperadores co " +
                    "inner JOIN CatSucursales cu on co.IdSucursal=  cu.IdSucursal where co.Activo = 1";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }

    }

    @PutMapping("/ReasignarOperador/{idParada}/{idOperdor}")
    public ResponseEntity<?> reasignarGuia(@PathVariable("idParada") int idParada,
                                           @PathVariable("idOperdor") int idOperador,
                                           @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "update ParadaUltimaMillaPQ\n" +
                        "set m_nIdOperador=" + idOperador + "\n " +
                        "where m_nIdProParadaUltimaMilla=" + idParada;
                statement.executeUpdate(query);
                

                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios.");
        }
    }

    @PostMapping("/Operador/ValidarLoginPaqueteria")
    public ResponseEntity<?> loginOperador(@RequestBody LoginApp2Request loginAppRequest, @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            Connection jdbcConnection = dbConection.getconnection(rfc);
            Statement statement = jdbcConnection.createStatement();
            String query = "SELECT top 1 o.IdOperador FROM CatOperadores o WHERE o.NumeroOperador = " + loginAppRequest.getUsername();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            if (jsonObject == null) {
                return ResponseEntity.status(401).body("No se encontró el operador. Verifique la información.");
            }
            query = "update CatOperadores set DeviceId = '" + loginAppRequest.getDeviceId() + "' where IdOperador = " + jsonObject.getInt("IdOperador");

            int result = statement.executeUpdate(query);
            if (result == 1) {
                query = "SELECT top 1 o.IdOperador                                                    as m_nIdOperador,\n" +
                        "             o.Activo                                                        as m_bActivo,\n" +
                        "             o.LicenciaVencimiento                                           as m_dtLicenciaVencimiento,\n" +
                        "             o.NumeroOperador                                                AS m_nNumeroOperador,\n" +
                        "             o.CorreoOperador                                                as m_sCorreoOperador,\n" +
                        "             ''                                                              as m_sFotoOperador,\n" +
                        "             o.Licencia                                                      as m_sLicencia,\n" +
                        "             (o.Nombres + ' ' + o.ApellidoPaterno + ' ' + o.ApellidoMaterno) as m_sNombreCompleto,\n" +
                        "             cu.Sucursal                                                     as m_sSucursal,\n" +
                        "             CAST(1 as bit)                                                  AS m_bAppPaqueteria,\n" +
                        "             o.DeviceId                                                      as m_sDeviceId\n" +
                        "FROM CatOperadores o\n" +
                        "         LEFT JOIN CatSucursales cu on o.IdSucursal = cu.IdSucursal\n" +
                        "WHERE o.IdOperador = " + jsonObject.getInt("IdOperador");
                rs = statement.executeQuery(query);
                jsonObject = UtilFuctions.convertObject(rs);
                query = "SELECT top 1 (case when TipoModulo = 2 then CAST(1 AS BIT) else CAST(0 AS BIT) end) as tipo  from SisParametros";

                ResultSet rs2 = statement.executeQuery(query);
                JSONObject jsonObject2 = UtilFuctions.convertObject(rs2);
                if (jsonObject2 == null) {
                    return ResponseEntity.status(401).body("No puede acceder a este módulo.");
                }
                jsonObject.put("Almacen", jsonObject2.getBoolean("tipo"));
                return ResponseEntity.ok(jsonObject.toString());
            }
            else {
                return ResponseEntity.status(500).body("No se encontró el operador. Verifique la información.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    /** Esta es la v2 del servicio de login para la app y escaner.
     * En la version anterior se retornaba la bandera Almacen para indicar si el operador pertenece al sistema de almacen.
     * En esta version se retorna el id del sistema al que pertenece el operador.
     * */
    @PostMapping("/v2/Operador/ValidarLoginPaqueteria")
    public ResponseEntity<?> loginOperadorv2(@RequestBody LoginAppRequest
                                                   loginAppRequest, @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try {
            Connection jdbcConnection = dbConection.getconnection(rfc);
            String query = "SELECT top 1 o.IdOperador FROM CatOperadores o WHERE o.NumeroOperador = ?";
            PreparedStatement preparedStatement = jdbcConnection.prepareStatement(query);
            preparedStatement.setString(1, loginAppRequest.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            if (jsonObject == null) {
                return ResponseEntity.status(401).body("No se encontró el operador. Verifique la información.");
            }
            query = "update CatOperadores set DeviceId = ? where IdOperador = ?";
            preparedStatement = jdbcConnection.prepareStatement(query);
            preparedStatement.setString(1, loginAppRequest.getDeviceId());
            preparedStatement.setInt(2, jsonObject.getInt("IdOperador"));
            int result = preparedStatement.executeUpdate();

            if (result != 1) {
                return ResponseEntity.status(500).body("Ocurrió un problema al iniciar sesión");
            }

            query = "SELECT top 1 o.IdOperador                                                    as m_nIdOperador,\n" +
                    "             o.Activo                                                        as m_bActivo,\n" +
                    "             o.LicenciaVencimiento                                           as m_dtLicenciaVencimiento,\n" +
                    "             o.NumeroOperador                                                AS m_nNumeroOperador,\n" +
                    "             o.CorreoOperador                                                as m_sCorreoOperador,\n" +
                    "             ''                                                              as m_sFotoOperador,\n" +
                    "             o.Licencia                                                      as m_sLicencia,\n" +
                    "             (o.Nombres + ' ' + o.ApellidoPaterno + ' ' + o.ApellidoMaterno) as m_sNombreCompleto,\n" +
                    "             cu.Sucursal                                                     as m_sSucursal,\n" +
                    "             o.DeviceId                                                      as m_sDeviceId\n" +
                    "FROM CatOperadores o\n" +
                    "         LEFT JOIN CatSucursales cu on o.IdSucursal = cu.IdSucursal\n" +
                    "WHERE o.IdOperador = ?";
            preparedStatement = jdbcConnection.prepareStatement(query);
            preparedStatement.setInt(1, jsonObject.getInt("IdOperador"));
            rs = preparedStatement.executeQuery();
            jsonObject = UtilFuctions.convertObject(rs);
            query = "SELECT top 1 TipoModulo as idSistema  from SisParametros";
            preparedStatement = jdbcConnection.prepareStatement(query);
            ResultSet rs2 = preparedStatement.executeQuery();
            JSONObject jsonObject2 = UtilFuctions.convertObject(rs2);
            if (jsonObject2 == null) {
                return ResponseEntity.status(401).body("No se encontró el operador. Verifique la información.");
            }
            jsonObject.put("idSistema", jsonObject2.getInt("idSistema"));
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al iniciar sesión");
        }

    }

    @PutMapping("/Operador/LogoutPaqueteria")
    public ResponseEntity<?> logoutOperador(@RequestBody LoginAppRequest loginAppRequest,
                                            @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                Statement statement = jdbcConnection.createStatement();
                String query = "update CatOperadores set DeviceId = '' where IdOperador = " + loginAppRequest.getId();
                int result = statement.executeUpdate(query);

                if (result == 1) {
                    return ResponseEntity.ok("Sesión terminada");
                } else {
                    return ResponseEntity.status(500).body("Ocurrió un problema al cerrar la sesión");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al cerrar la sesión");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Operadores/GetListado/PorSucursal/{idSucursal}")
    public ResponseEntity<?> getOperadoresPorSucursal(@RequestHeader("RFC") String rfc,
                                                      @PathVariable("idSucursal") int idSucursal)
            throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "Select \n" +
                    "           co.IdOperador as m_nIdOperador, \n" +
                    "                co.NumeroOperador as m_nNumeroOperador, \n" +
                    "                (co.Nombre) as m_sNombreCompleto,\n" +
                    "                co.ApellidoPaterno as m_sApellidoPaterno, \n" +
                    "                co.ApellidoMaterno as m_sApellidoMaterno, \n" +
                    "                cu.Sucursal as m_sSucursal, \n" +
                    "                co.EsPermisionario as m_bEsPermisionario, \n" +
                    "                co.Activo as m_bActivo, \n" +
                    "                co.Licencia as m_sLicencia, \n" +
                    "                co.LicenciaVencimiento as m_dLicenciaVencimiento, \n" +
                    "                (select case when (select count(*) from ParadaUltimaMillaPQ\n" +
                    "                   where m_nIdOperador=co.IdOperador and Completada=0 and Activa=1) >0 then CAST(1 AS BIT)\n" +
                    "                   else CAST(0 AS BIT) end) ocupado,\n" +
                    "               (select TOP 1 IIF(UM.m_dFecha IS NOT NULL, FORMAT(UM.m_dFecha, 'yyyy/MM/dd'),'') from ParadaUltimaMillaPQ PUM\n" +
                    "                   left join ProUltimaMillaPQ UM on PUM.m_nIdUltimaMilla=UM.m_nIdUltimaMilla\n" +
                                        "where PUM.m_nIdOperador=co.IdOperador and PUM.Completada=0 and Activa=1 order by UM.m_dFecha DESC) as fechaUltimaRuta"+
                    "                FROM CatOperadores co " +
                    "                inner JOIN CatSucursales cu on co.IdSucursal=  cu.IdSucursal " +
                    "                where co.Activo = 1 and co.IdSucursal = " + idSucursal;
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
