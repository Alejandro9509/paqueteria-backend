package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.EmbalajeModificarRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
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

import static com.certuit.base.util.UtilFuctions.convertObject;

@RestController
@RequestMapping(value = "/api")
public class EmbalajesRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Embalajes/GetListado")
    public ResponseEntity<?> getEmbalaje(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdEmbalaje as m_nIdEmbalaje," +
                    "Codigo as m_sCodigo," +
                    "Nombre as m_sNombre," +
                    "Descripcion as m_sDescripcion," +
                    "(SELECT Usuario FROM CatUsuarios WHERE IdUsuario = EMBALAJE.CreadoPor) AS m_sCreadoPor," +
                    "CreadoEl as m_sCreadoEl," +
                    "(SELECT Usuario FROM CatUsuarios WHERE IdUsuario = EMBALAJE.ModificadoPor) AS m_sModificadoPor," +
                    "ModificadoEl as m_sModificadoEl " +
                    "FROM CatEmbalajesPQ AS EMBALAJE";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Embalajes/GetById/{id}")
    public ResponseEntity<?> getEmbalajesById(@RequestHeader("RFC") String rfc, @PathVariable("id") int id)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdEmbalaje as m_nIdEmbalaje, " +
                    "Codigo as m_sCodigo, " +
                    "Nombre as m_sNombre, " +
                    "Descripcion as m_sDescripcion " +
                    "FROM CatEmbalajesPQ " +
                    "WHERE IdEmbalaje =" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject = UtilFuctions.convertObject(rs).toString();
            return ResponseEntity.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }

    }

    @GetMapping("/Embalajes/ValidarEliminar/{id}")
    public ResponseEntity<?> getEmbalajeEliminar(@RequestHeader("RFC") String rfc, @PathVariable("id") int id)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = " select RegistroDeSistema from CatEmbalajesPQ where IdEmbalaje = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);
            jsonObject.put("RegistroDeSistema", rs.getBoolean("RegistroDeSistema"));
            if (jsonObject.getBoolean("RegistroDeSistema")) {
                return ResponseEntity.ok("No se puede eliminar el Embalaje, porque es registro del sistema.");
            } else {
                query = "IF EXISTS (select IdTipoEmbalaje from ProRecoleccionPaquetePQ where IdTipoEmbalaje = " + id
                        + ") and\n" +
                        "EXISTS (select IdTipoEmpaque from ProEmbarqueDetallePQ where IdTipoEmpaque = " + id + ")\n" +
                        "BEGIN\n" +
                        "   SELECT 0 as sePuedeEliminar\n" +
                        "END\n" +
                        "ELSE\n" +
                        "BEGIN\n" +
                        "    SELECT 1 as sePuedeEliminar\n" +
                        "END";
                statement = jdbcConnection.createStatement();
                rs = statement.executeQuery(query);
                String jsonObject2 = UtilFuctions.convertObject(rs).toString();
                return ResponseEntity.ok(jsonObject2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Embalaje/Modificar/{idEmbalaje}")
    public ResponseEntity<?> modificarEmbalaje(@PathVariable("idEmbalaje") int idEmbalaje,
                                               @RequestBody EmbalajeModificarRequest embalaje,
                                               @RequestHeader("RFC") String rfc) throws Exception {

        try {
            if (embalaje.getM_sNombre().equalsIgnoreCase("")) {
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
            }

            if (embalaje.getM_sCodigo().equalsIgnoreCase("")) {
                return ResponseEntity.status(500).body("El Código es un campo requerido");
            }

            if (embalaje.getM_sDescripcion().equalsIgnoreCase("")) {
                return ResponseEntity.status(500).body("La descripción es un campo requerido");
            }

            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String pattern = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                String query = "EXEC usp_CatEmbalajesModificarPQ " + idEmbalaje + ",'" + embalaje.getM_sCodigo()
                        + "','" + embalaje.getM_sNombre() + "','" + embalaje.getM_sDescripcion() + "',"
                        + Integer.parseInt(embalaje.getM_nModificadoPor()) + ",'" + date + "'";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al tratar de modificar");
        }
        return ResponseEntity.ok("Registro modificado");
    }

    @PostMapping("/Embalaje/Agregar")
    public ResponseEntity<?> agregarEmbalaje(@RequestBody EmbalajeModificarRequest embalaje,
                                             @RequestHeader("RFC") String rfc) throws Exception {
        try {
            if (embalaje.getM_sNombre().equalsIgnoreCase("")) {
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
            }

            if (embalaje.getM_sCodigo().equalsIgnoreCase("")) {
                return ResponseEntity.status(500).body("El Código es un campo requerido");
            }

            if (embalaje.getM_sDescripcion().equalsIgnoreCase("")) {
                return ResponseEntity.status(500).body("La descripción es un campo requerido");
            }

            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String pattern = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());
                String query = "EXEC usp_CatEmbalajesAgregarPQ '" + embalaje.getM_sCodigo() + "','"
                        + embalaje.getM_sNombre() + "','" + embalaje.getM_sDescripcion() + "',"
                        + embalaje.getM_sCreadoPor() + ",'" + date + "',"
                        + embalaje.getM_nModificadoPor() + ",'" + date + "'";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al agregar");
        }

        return ResponseEntity.ok("Agregado Exitosamente");
    }

    @DeleteMapping("/Embalaje/Eliminar/{nIdEmbalaje}/{idUsuario}")
    public ResponseEntity<?> eliminarEmbalaje(@PathVariable("nIdEmbalaje") int nIdEmbalaje,
                                              @PathVariable("idUsuario") int idUsuario,
                                              @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_CatEmbalajesEliminarPQ " + nIdEmbalaje;
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar");
        }
        return ResponseEntity.ok("Eliminado exitoso");
    }

}
