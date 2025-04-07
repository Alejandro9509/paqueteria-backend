package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.RemitenteDestinatarioRequest;
import com.certuit.base.util.Constants;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.certuit.base.util.UtilFuctions.*;
import static com.certuit.base.util.UtilFuctions.addInt;

@RestController
@RequestMapping(value = "/api")
public class RemitenteDestinatarioRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/RemitentesDestinatarios/GetById/{id}")
    public ResponseEntity<?> getRemitenteDestinatarioId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = " Select *,\n" +
                    "  (select IdPais from CatEstados where ctrd.IdEstado = CatEstados.IdEstado ) as IdPais ,\n" +
                    "  (select top 1 cp.IdCodigoPostal from CatCodigosPostales cp " +
                    "where cp.CodigoPostal = ctrd.CodigoPostal and cp.IdEstado = ctrd.IdEstado) as IdCP\n" +
                    "  from CatRemitentesDestinatarios ctrd\n" +
                    "where IdRemitenteDestinatario =" + id + "";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            while (rs.next()) {
                json.put("m_nIdRemitenteDestinatario", rs.getInt("IdRemitenteDestinatario"));
                json.put("m_nIdCliente", rs.getInt("IdCliente"));
                json.put("m_nNumero", rs.getInt("Numero"));
                json.put("m_sNombre", rs.getString("Nombre"));
                json.put("m_sRFC", rs.getString("RFC"));
                json.put("m_bActivo", rs.getBoolean("Activo"));
                json.put("m_sCalle", rs.getString("Calle"));
                json.put("m_sNoExterior", rs.getString("NoExterior"));
                json.put("m_sNoInterior", rs.getString("NoInterior"));
                json.put("m_sColonia", rs.getString("Colonia"));
                json.put("m_sLocalidad", rs.getString("Localidad"));
                json.put("m_sMunicipio", rs.getString("Municipio"));
                json.put("m_nIdEstado", rs.getString("IdEstado"));
                json.put("m_nIdCP", rs.getInt("IdCP"));
                json.put("m_nCreadoPor", rs.getInt("CreadoPor"));
                json.put("m_dtCreadoEl", rs.getDate("CreadoEl"));
                json.put("m_nModificadoPor", rs.getInt("ModificadoPor"));
                json.put("m_dtModificadoEl", rs.getString("ModificadoEl"));
                json.put("m_sContacto", rs.getString("Contacto"));
                json.put("m_sCorreoElectronico", rs.getString("CorreoElectronico"));
                json.put("m_sTelefono", rs.getString("Telefono"));
                json.put("m_nIdPais", rs.getInt("IdPais"));
                json.put("m_sAlias", rs.getString("Alias"));
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/RemitentesDestinatarios/GetByName/{nombre}")
    public ResponseEntity<?> getRemitenteDestinatarioByName(@PathVariable("nombre") String nombre,
                                                            @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT COUNT(*) AS total FROM CatRemitentesDestinatarios WHERE Nombre = '" + nombre + "'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", UtilFuctions.convertObject(rs).getInt("total"));
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/RemitentesDestinatarios/GetListado")
    public ResponseEntity<?> getlistadoRemitentesDestinatarios(@RequestHeader("RFC") String rfc)
            throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "crd.IdRemitenteDestinatario as m_nIdRemitenteDestinatario,\n" +
                    "crd.Numero as m_nNumero,\n" +
                    "crd.RFC as m_sRFC,\n" +
                    "crd.Nombre as m_sNombre,\n" +
                    "crd.CorreoElectronico as m_sCorreoElectronico,\n" +
                    "crd.Telefono as m_sTelefono,\n" +
                    "crd.Contacto as m_sContacto,\n" +
                    "(select top 1 cp.IdCodigoPostal from CatCodigosPostales cp " +
                    "where cp.CodigoPostal = crd.CodigoPostal and cp.IdEstado = crd.IdEstado) as m_nIdCP,\n" +
                    "(SELECT DISTINCT top 1  CodigoMunicipio FROM CatCodigosPostales " +
                    "WHERE IdEstado = crd.IdEstado and (UPPER(Municipio) = crd.Municipio " +
                    "COLLATE Latin1_general_CI_AI or cast( replace((replace(UPPER(Municipio) " +
                    "collate Latin1_General_CS_AS, 'Œ' collate Latin1_General_CS_AS, 'OE' " +
                    "collate Latin1_General_CS_AS)     ) collate Latin1_General_CS_AS, 'œ' " +
                    "collate Latin1_General_CS_AS, 'oe' collate Latin1_General_CS_AS) as varchar(max)) " +
                    "collate SQL_Latin1_General_Cp1251_CS_AS = cast(    replace((        replace(crd.Municipio " +
                    "collate Latin1_General_CS_AS, 'Œ' collate Latin1_General_CS_AS, 'OE' " +
                    "collate Latin1_General_CS_AS)     ) collate Latin1_General_CS_AS, 'œ' " +
                    "collate Latin1_General_CS_AS, 'oe' collate Latin1_General_CS_AS) as varchar(max)) " +
                    "collate SQL_Latin1_General_Cp1251_CS_AS )) AS m_nIdMunicipio,\n" +
                    "crd.Municipio as m_sMunicipio,\n" +
                    "crd.Localidad as m_sLocalidad,\n" +
                    "crd.Colonia as m_sColonia,\n" +
                    "crd.Calle as m_sCalle,\n" +
                    "crd.NoExterior as m_sNoExterior,\n" +
                    "crd.NoInterior as m_sNoInterior,\n" +
                    "cc.NumeroCliente as m_nNumeroCliente,\n" +
                    "cc.NombreFiscal as m_sNombreFiscal,\n" +
                    "crd.Activo as m_bActivo,\n" +
                    "crd.IdEstado as m_nIdEstado,\n" +
                    "COALESCE(crd.Alias, '') as m_sAlias,\n" +
                    "ce.Estado as m_sEstado,\n" +
                    "COALESCE(crd.Latitud, '') as m_sLatitud,\n" +
                    "COALESCE(crd.Longitud, '') as m_sLongitud,\n" +
                    "crd.CodigoPostal as m_sCodigoPostal,\n" +
                    "(crd.Calle + '' + crd.NoExterior + ', ' + crd.Colonia) as m_sDomicilio\n" +
                    "FROM CatRemitentesDestinatarios crd left join CatClientes cc on crd.IdCliente= cc.IdCliente inner join CatEstados ce on ce.IdEstado= crd.IdEstado\n";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/RemitentesDestinatarios/GetListadoById/{id}")
    public ResponseEntity<?> getListadoIdCliente(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "crd.IdRemitenteDestinatario as m_nIdRemitenteDestinatario,\n" +
                    "crd.Numero as m_nNumero,\n" +
                    "crd.RFC as m_sRFC,\n" +
                    "crd.Nombre as m_sNombre,\n" +
                    "crd.CorreoElectronico as m_sCorreoElectronico,\n" +
                    "crd.Telefono as m_sTelefono,\n" +
                    "crd.Contacto as m_sContacto,\n" +
                    "(select top 1 cp.IdCodigoPostal from CatCodigosPostales cp " +
                    "where cp.CodigoPostal = crd.CodigoPostal and cp.IdEstado = crd.IdEstado) as m_nIdCP,\n" +
                    "(SELECT top 1 CodigoMunicipio FROM CatCodigosPostales " +
                    "where UPPER(Municipio) = crd.Municipio COLLATE Latin1_general_CI_AI) AS m_nIdMunicipio,\n" +
                    "crd.Municipio as m_sMunicipio,\n" +
                    "crd.Localidad as m_sLocalidad,\n" +
                    "crd.Colonia as m_sColonia,\n" +
                    "crd.Calle as m_sCalle,\n" +
                    "crd.NoExterior as m_sNoExterior,\n" +
                    "crd.NoInterior as m_sNoInterior,\n" +
                    "cc.NumeroCliente as m_nNumeroCliente,\n" +
                    "cc.NombreFiscal as m_sNombreFiscal,\n" +
                    "crd.Activo as m_bActivo,\n" +
                    "crd.IdEstado as m_nIdEstado,\n" +
                    "COALESCE(crd.Alias, '') as m_sAlias,\n" +
                    "ce.Estado as m_sEstado,\n" +
                    "COALESCE(crd.Latitud, '') as m_sLatitud,\n" +
                    "COALESCE(crd.Longitud, '') as m_sLongitud,\n" +
                    "crd.CodigoPostal as m_sCodigoPostal,\n" +
                    "(crd.Calle + '' + crd.NoExterior + ', ' + crd.Colonia) as m_sDomicilio\n" +
                    "FROM CatRemitentesDestinatarios crd left join CatClientes cc on crd.IdCliente= cc.IdCliente " +
                    "inner join CatEstados ce on ce.IdEstado= crd.IdEstado\n" +
                    "WHERE (crd.IdCliente = '" + id + "')";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/RemitentesDestinatarios/Agregar")
    public ResponseEntity<?> remitenteDestinatarioAgregar(@RequestBody RemitenteDestinatarioRequest request,
                                                          @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            Connection jdbcConnection = dbConection.getconnection(rfc);
            String query;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            int nextNumero = 0;

            query = "EXEC usp_CatRemitentesDestinatariosGetNextNumeroPQ";
            rs = statement.executeQuery(query);
            while (rs.next()) {
                nextNumero = rs.getInt("NextNumero");
            }
            String queryAgregar = "";
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            if (request.getIdCliente() == 0) {
                queryAgregar = "EXEC usp_CatRemitentesDestinatariosAgregar NULL" + addInt(nextNumero) +
                        addString(request.getNombre()) + addString(request.getRfc()) + addBoolean(request.isActivo()) +
                        addString(request.getCalle()) + addString(request.getNoExterior()) +
                        addString(request.getNoInterior()) + addString(request.getColonia()) +
                        addString(request.getLocalidad()) + addString(request.getMunicipio()) +
                        addString(request.getIdEstado()) + addString(request.getCodigoPostal()) +
                        addInt(request.getIdSucursal()) + addString(request.getCreadoPor()) +
                        addString(currentDateTime) + addString(request.getIdPeticion()) +
                        addString(request.getContacto()) + addString(request.getCorreoElectronico()) +
                        addString(request.getTelefono()) + addString(request.getNoRegistroIdentidadFiscal())+
                        addString(request.getAlias()) + ",''";
            } else {
                queryAgregar = "EXEC usp_CatRemitentesDestinatariosAgregar " + request.getIdCliente() + "" +
                        addInt(nextNumero) + addString(request.getNombre()) + addString(request.getRfc()) +
                        addBoolean(request.isActivo()) + addString(request.getCalle()) +
                        addString(request.getNoExterior()) + addString(request.getNoInterior()) +
                        addString(request.getColonia()) + addString(request.getLocalidad()) +
                        addString(request.getMunicipio()) + addString(request.getIdEstado()) +
                        addString(request.getCodigoPostal()) + addInt(request.getIdSucursal()) +
                        addString(request.getCreadoPor()) + addString(currentDateTime) +
                        addString(request.getIdPeticion()) + addString(request.getContacto()) +
                        addString(request.getCorreoElectronico()) + addString(request.getTelefono()) +
                        addString(request.getNoRegistroIdentidadFiscal())+addString(request.getAlias()) + ",''";
            }
            statement.executeUpdate(queryAgregar);
            return ResponseEntity.ok(nextNumero);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(Constants.MESSAGE_ERROR_POST);
        }

    }

    @PostMapping("/RemitentesDestinatarios/Agregar/v2")
    public ResponseEntity<?> remitenteDestinatarioAgregarv2(@RequestBody RemitenteDestinatarioRequest request,
                                                            @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            int nextNumero = 0;

            query = "EXEC usp_CatRemitentesDestinatariosGetNextNumeroPQ";
            rs = statement.executeQuery(query);
            while (rs.next()) {
                nextNumero = rs.getInt("NextNumero");
            }

            String queryAgregar = "";
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            if (request.getIdCliente() == 0) {
                queryAgregar = "EXEC usp_CatRemitentesDestinatariosAgregar " +
                        "NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ''";
                PreparedStatement preparedStatement = jdbcConnection.prepareStatement(queryAgregar);
                preparedStatement.setInt(1, nextNumero);
                preparedStatement.setString(2, request.getNombre());
                preparedStatement.setString(3, request.getRfc());
                preparedStatement.setBoolean(4, request.isActivo());
                preparedStatement.setString(5, request.getCalle());
                preparedStatement.setString(6, request.getNoExterior());
                preparedStatement.setString(7, request.getNoInterior());
                preparedStatement.setString(8, request.getColonia());
                preparedStatement.setString(9, request.getLocalidad());
                preparedStatement.setString(10, request.getMunicipio());
                preparedStatement.setString(11, request.getIdEstado());
                preparedStatement.setString(12, request.getCodigoPostal());
                preparedStatement.setInt(13, request.getIdSucursal());
                preparedStatement.setString(14, request.getCreadoPor());
                preparedStatement.setString(15, currentDateTime);
                preparedStatement.setString(16, request.getIdPeticion());
                preparedStatement.setString(17, request.getContacto());
                preparedStatement.setString(18, request.getCorreoElectronico());
                preparedStatement.setString(19, request.getTelefono());
                preparedStatement.setString(20, request.getNoRegistroIdentidadFiscal());
                preparedStatement.setString(21, request.getAlias());

                preparedStatement.executeUpdate();
            } else {
                queryAgregar = "EXEC usp_CatRemitentesDestinatariosAgregar " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ''";
                PreparedStatement preparedStatement = jdbcConnection.prepareStatement(queryAgregar);

                preparedStatement.setInt(1, request.getIdCliente());
                preparedStatement.setInt(2, nextNumero);
                preparedStatement.setString(3, request.getNombre());
                preparedStatement.setString(4, request.getRfc());
                preparedStatement.setBoolean(5, request.isActivo());
                preparedStatement.setString(6, request.getCalle());
                preparedStatement.setString(7, request.getNoExterior());
                preparedStatement.setString(8, request.getNoInterior());
                preparedStatement.setString(9, request.getColonia());
                preparedStatement.setString(10, request.getLocalidad());
                preparedStatement.setString(11, request.getMunicipio());
                preparedStatement.setString(12, request.getIdEstado());
                preparedStatement.setString(13, request.getCodigoPostal());
                preparedStatement.setInt(14, request.getIdSucursal());
                preparedStatement.setString(15, request.getCreadoPor());
                preparedStatement.setString(16, currentDateTime);
                preparedStatement.setString(17, request.getIdPeticion());
                preparedStatement.setString(18, request.getContacto());
                preparedStatement.setString(19, request.getCorreoElectronico());
                preparedStatement.setString(20, request.getTelefono());
                preparedStatement.setString(21, request.getNoRegistroIdentidadFiscal());
                preparedStatement.setString(22, request.getAlias());
                preparedStatement.executeUpdate();
            }
//            statement.executeUpdate(queryAgregar);
            return ResponseEntity.ok(nextNumero);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Constants.MESSAGE_ERROR_POST);
        }
    }

    @PutMapping("/RemitentesDestinatarios/Modificar/{id}")
    public ResponseEntity<?> remitenteDestinatarioModificar(@PathVariable("id") int id,
                                                            @RequestBody RemitenteDestinatarioRequest request,
                                                            @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatRemitentesDestinatariosGetNextNumeroPQ";
                Statement statement = jdbcConnection.createStatement();
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormatter.format(new Date());
                String queryModificar = "EXEC usp_CatRemitentesDestinatariosModificar " + id + ""
                        + addInt(request.getIdCliente())
                        + addInt(request.getNumero())
                        + addString(request.getNombre())
                        + addString(request.getRfc())
                        + addBoolean(request.isActivo())
                        + addString(request.getCalle())
                        + addString(request.getNoExterior())
                        + addString(request.getNoInterior())
                        + addString(request.getColonia())
                        + addString(request.getLocalidad())
                        + addString(request.getMunicipio())
                        + addString(request.getIdEstado())
                        + addString(request.getCodigoPostal())
                        + addInt(request.getIdSucursal())
                        + addString(request.getModificadoPor())
                        + addString(currentDateTime)
                        + addString(currentDateTime)
                        + addString(request.getIdPeticion())
                        + addString(request.getContacto())
                        + addString(request.getCorreoElectronico())
                        + addString(request.getTelefono())
                        + addString(request.getNoRegistroIdentidadFiscal()) + ",''"
                        + addString(request.getAlias());
                statement.executeUpdate(queryModificar);
                return ResponseEntity.ok(Constants.MESSAGE_SUCCESS_POST);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Constants.MESSAGE_ERROR_POST);
        }
    }

    @PutMapping("/RemitentesDestinatarios/ConfirmarCoordenadas")
    public ResponseEntity<?> remitenteDestinatarioConfirmarCoordenadas(@RequestBody RemitenteDestinatarioRequest request,
                                                                       @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
//            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String currentDateTime = dateFormatter.format(new Date());

                String queryBuscaRemiDest = "SELECT IdRemitenteDestinatario FROM CatRemitentesDestinatarios WHERE " +
                        "Nombre LIKE '" + request.getNombre() + "' AND RFC LIKE '" + request.getRfc() + "'";
                String queryBuscaRecoleccion = "SELECT IdRecoleccion FROM ProRecoleccionPQ WHERE IdRecoleccion = "
                        + request.getNumero();
                int idRecoleccion = 0;

                ResultSet recolecciones = statement.executeQuery(queryBuscaRecoleccion);
                if (recolecciones.next()) {
                    idRecoleccion = recolecciones.getInt("IdRecoleccion");
                }

                ResultSet rs = statement.executeQuery(queryBuscaRemiDest);
                if (rs.next()) {
                    int idRemitenteDestinatario = rs.getInt("IdRemitenteDestinatario");
                    String queryModificar = "EXEC usp_CatRemitentesDestinatariosConfirmarCoord "
                            + idRemitenteDestinatario
                            + addString(request.getLatitud()) + addString(request.getLongitud()) + "," +
                            idRecoleccion + "";
                    statement.executeUpdate(queryModificar);
                }
                return ResponseEntity.ok(Constants.MESSAGE_SUCCESS_POST);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Constants.MESSAGE_ERROR_POST);
        }
    }

    @DeleteMapping("/RemitentesDestinatarios/Eliminar/{id}/{idEliminadoPor}")
    public ResponseEntity<?> eliminarRemitenteDestinatario(@PathVariable("id") int id,
                                                           @PathVariable("idEliminadoPor") int idEliminadoPor,
                                                           @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatRemitentesDestinatariosEliminarPQ " + id + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
                return ResponseEntity.ok(Constants.MESSAGE_SUCCESS_DELETE);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Constants.MESSAGE_ERROR_DELETE);
        }
    }

}
