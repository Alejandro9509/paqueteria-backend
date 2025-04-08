package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.TarifasRangosService;
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

@RestController
@RequestMapping(value = "/api")
public class TarifasRangosRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    TarifasRangosService tarifasRangosService;

    @GetMapping("/Tarifas/Rangos/GetListado")
    public ResponseEntity<?> getListadoTarifas(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdTarifa, tarifa.IdCliente, CONCAT(CC.NumeroCliente, '. ', CC.NombreFiscal) AS Cliente, Vigencia, CuotaMensual, tarifa.Activo\n" +
                    "FROM CatTarifasRangosPQ tarifa\n" +
                    "         LEFT JOIN CatClientes CC on tarifa.IdCliente = CC.IdCliente\n" +
                    "order by CC.NombreFiscal, tarifa.IdTarifa desc";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Tarifas/Rangos/GetByFiltro/{nombreCliente}")
    public ResponseEntity<?> getListadoEmbarquesFiltro(
            @PathVariable("nombreCliente") String nombreCliente,
            @RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            StringBuilder query = new StringBuilder();
            query.append("SELECT IdTarifa, tarifa.IdCliente, CONCAT(CC.NumeroCliente, '. ', " +
                            "CC.NombreFiscal) AS Cliente, Vigencia, CuotaMensual, tarifa.Activo ")
                    .append("FROM CatTarifasRangosPQ tarifa ")
                    .append("LEFT JOIN CatClientes CC on tarifa.IdCliente = CC.IdCliente ");

            if (nombreCliente != null && !nombreCliente.trim().isEmpty() && !"0".equals(nombreCliente)) {
                nombreCliente = nombreCliente.toUpperCase();
                query.append("WHERE CC.NombreFiscal LIKE '%" + nombreCliente + "%' ");
            }
            query.append("ORDER BY CC.NombreFiscal, tarifa.IdTarifa desc");
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query.toString());
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Tarifas/Rangos/GetGeneral")
    public ResponseEntity<?> getTarifaClienteGeneral(@RequestHeader("RFC") String rfc) throws Exception {
        Connection jdbcConnection = dbConection.getconnection(rfc);
        String query = "SELECT t.IdTarifa, t.IdCliente, t.Vigencia, t.CuotaMensual, c.NombreFiscal as Cliente\n" +
                "                FROM CatTarifasRangosPQ t\n" +
                "                LEFT JOIN CatClientes c on c.IdCliente = t.IdCliente\n" +
                "        Where c.NombreFiscal ='PUBLICO EN GENERAL' and t.Activo=1";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        int id=jsonObject.getInt("IdTarifa");

        jsonObject.put("ViajesLocales", tarifasRangosService.getViajesLocalesByIdTarifa(id, jdbcConnection));
        jsonObject.put("ViajesForaneos", tarifasRangosService.getViajesForaneosByIdTarifa(id, jdbcConnection));
        jsonObject.put("Grupos", tarifasRangosService.getGruposByIdTarifa(id, jdbcConnection));
        jsonObject.put("Conceptos", tarifasRangosService.getConceptosByIdTarifa(id, jdbcConnection));
        jsonObject.put("Productos", tarifasRangosService.getProductosByIdTarifa(id, jdbcConnection));
        jsonObject.put("Zonas", tarifasRangosService.getViajesZonasByIdTarifa(id, jdbcConnection));

        return ResponseEntity.ok(jsonObject.toString());
    }

    @GetMapping("/Tarifas/Rangos/GetById/{id}")
    public ResponseEntity<?> getTarifaById(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT t.IdTarifa, t.IdCliente, t.Vigencia, t.CuotaMensual, c.NombreFiscal as Cliente\n" +
                    "FROM CatTarifasRangosPQ t\n" +
                    "LEFT JOIN CatClientes c on c.IdCliente = t.IdCliente\n" +
                    "WHERE IdTarifa = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            jsonObject.put("ViajesLocales", tarifasRangosService.getViajesLocalesByIdTarifa(id, jdbcConnection));
            jsonObject.put("ViajesForaneos", tarifasRangosService.getViajesForaneosByIdTarifa(id, jdbcConnection));
            jsonObject.put("Grupos", tarifasRangosService.getGruposByIdTarifa(id, jdbcConnection));
            jsonObject.put("Conceptos", tarifasRangosService.getConceptosByIdTarifa(id, jdbcConnection));
            jsonObject.put("Productos", tarifasRangosService.getProductosByIdTarifa(id, jdbcConnection));
            jsonObject.put("Zonas", tarifasRangosService.getViajesZonasByIdTarifa(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Tarifas/Rangos/Agregar")
    public ResponseEntity<?> postTarifa(@RequestBody TarifaRangosRequest request,
                                        @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            JSONObject jsonValidacion = new JSONObject();
            JSONObject respuestaValidacion=tarifasRangosService.validarTarifaRango(request.getViajesForaneos(),jdbcConnection);
            if(!respuestaValidacion.getBoolean("success"))
            {
                jsonValidacion.put("Estatus", false);
                jsonValidacion.put("error",respuestaValidacion.getString("motivo"));
                return ResponseEntity.ok(jsonValidacion.toString());
            }
            String query = " EXEC usp_CatTarifasRangosAgregarPQ " + request.getIdCliente() + ", '"
                    + request.getVigencia() + "'," + request.getCuotaMensual();
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            statement.execute(query);
            query = "select @@identity as id";
            rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            agregarHijosTarifa(request, jsonObject.getInt("id"), query, statement);
            JSONObject jsonFin = new JSONObject();
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/Tarifas/Rangos/Eliminar/{id}")
    public ResponseEntity<?> deleteTarifa(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatTarifasRangosEliminarPQ " + id;
            Statement statement = jdbcConnection.createStatement();
            statement.execute(query);
            JSONObject jsonFin = new JSONObject();
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Tarifas/Rangos/Modificar/{id}")
    public ResponseEntity<?> updateTarifa(@PathVariable("id") int id,
                                          @RequestHeader("RFC") String rfc,
                                          @RequestBody TarifaRangosRequest request) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            JSONObject jsonFin = new JSONObject();
            JSONObject respuestaValidacion=tarifasRangosService.validarTarifaRango(request.getViajesForaneos(),
                    jdbcConnection);
            if(!respuestaValidacion.getBoolean("success"))
            {
                jsonFin.put("Estatus", false);
                jsonFin.put("error",respuestaValidacion.getString("motivo"));
                return ResponseEntity.ok(jsonFin.toString());
            }
            String query = "EXEC usp_CatTarifasRangosModificarPQ_1284 " + id + ", " + request.getIdCliente() + ", '" + request.getVigencia() + "'," + request.getCuotaMensual();
            Statement statement = jdbcConnection.createStatement();
            statement.execute(query);
            agregarHijosTarifa(request, id, query, statement);
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    public void agregarHijosTarifa(TarifaRangosRequest request, int idTarifa, String query, Statement statement)
            throws Exception {
        JSONObject jsonViajeLocal;
        JSONObject jsonViajeForaneo;
        JSONObject jsonViajeForaneoGrupo;
        ResultSet rs;
        for (ViajeLocalRequest viaje : request.getViajesLocales()) {
            viaje.setIdTarifa(idTarifa);
            query = " EXEC usp_CatTarifasRangosAgregarViajeLocalPQ " + viaje.getIdTarifa() + ", "
                    + viaje.getIdSucursal() + ", " + viaje.getIdConcepto() + ", " + viaje.getIdTipoMedida();
            statement.execute(query);

            query = "select @@identity as id";
            rs = statement.executeQuery(query);
            jsonViajeLocal = UtilFuctions.convertObject(rs);
            for (ViajeZonaRequest zona : viaje.getZonas()) {
                zona.setIdViajeLocal(jsonViajeLocal.getInt("id"));
                query = " EXEC usp_CatTarifasRangosAgregarViajeZonaPQ " + zona.getIdZonaOperativa() + ", "
                        + zona.getIdViajeLocal() + ",NULL";
                statement.execute(query);
            }
            for (TarifaConceptoRequest concepto : viaje.getConceptos()) {
                concepto.setIdViajeLocal(jsonViajeLocal.getInt("id"));
                query = " EXEC usp_CatTarifasRangosAgregarConceptoPQ "
                        + concepto.getIdConceptoFacturacion()
                        + ", " + concepto.getIdUnidadMedida()
                        + ", " + concepto.getMinimo()
                        + ", " + concepto.getMaximo()
                        + ", " + concepto.getImporte()
                        + ", " + concepto.getIdTipoCalculo()
                        + ", " + concepto.getIdViajeLocal()
                        + ", NULL,NULL"
                        + ", " + concepto.getPorcentaje();
                statement.execute(query);
            }
            for (TarifaProductoRequest producto : viaje.getProductos()) {
                producto.setIdViajeLocal(jsonViajeLocal.getInt("id"));
                query = " EXEC usp_CatTarifasRangosAgregarProductoPQ " + producto.getIdProducto() + ", "
                        + producto.getIdViajeLocal() + ", NULL ";
                statement.execute(query);
            }
        }

        for (TarifaConceptoRequest concepto : request.getManiobras()) {
            concepto.setIdTarifa(idTarifa);
            query = " EXEC usp_CatTarifasRangosAgregarConceptoPQ "
                    + concepto.getIdConceptoFacturacion()
                    + ", " + concepto.getIdUnidadMedida()
                    + ", " + concepto.getMinimo()
                    + ", " + concepto.getMaximo()
                    + ", " + concepto.getImporte()
                    + ", " + concepto.getIdTipoCalculo()
                    + ", NULL,NULL"
                    + ", " + concepto.getIdTarifa()
                    + ", " + concepto.getPorcentaje();
            statement.execute(query);

            if(concepto.getProductos() != null){
                for(TarifaConceptoProductoRequest producto : concepto.getProductos()) {
                    producto.setIdTarifa(idTarifa);
                    producto.setIdConceptoFacturacion(concepto.getIdConceptoFacturacion());
                    query = "EXEC usp_CatTarifasRangosConceptosAgregarProductoPQ " + producto.getIdTarifa() + ", "
                            + producto.getIdProducto() + ", " + producto.getIdConceptoFacturacion();
                    System.out.println(query);
                    statement.execute(query);
                }
            }
        }

        for (ViajeForaneoRequest viaje : request.getViajesForaneos()) {
            viaje.setIdTarifa(idTarifa);
            query = " EXEC usp_CatTarifasRangosAgregarViajeForaneoPQ "
                    + viaje.getIdOrigen()
                    + ", " + viaje.getIdDestino()
                    + ", " + viaje.getIdTipoMedida()
                    + ", " + viaje.getIdTarifa()
                    + ", " + viaje.getFleteMinimo();
            statement.execute(query);

            query = "select @@identity as id";
            rs = statement.executeQuery(query);
            jsonViajeForaneo = UtilFuctions.convertObject(rs);

            for (ViajeForaneoGrupo grupo : viaje.getGrupos()) {
                grupo.setIdViajeForaneo(jsonViajeForaneo.getInt("id"));
                query = " EXEC usp_CatTarifasRangosAgregarViajeForaneoGrupoPQ '"
                        + grupo.getNombre()
                        + "', " + grupo.getIdViajeForaneo();
                statement.execute(query);

                query = "select @@identity as id";
                rs = statement.executeQuery(query);
                jsonViajeForaneoGrupo = UtilFuctions.convertObject(rs);

                for (ViajeZonaRequest zona : grupo.getZonas()) {
                    zona.setIdViajeForaneoGrupo(jsonViajeForaneoGrupo.getInt("id"));
                    query = " EXEC usp_CatTarifasRangosAgregarViajeZonaPQ " + zona.getIdZonaOperativa() + ", NULL," + zona.getIdViajeForaneoGrupo();
                    statement.execute(query);
                }
                for (TarifaConceptoRequest concepto : grupo.getConceptos()) {
                    concepto.setIdViajeForaneoGrupo(jsonViajeForaneoGrupo.getInt("id"));
                    query = " EXEC usp_CatTarifasRangosAgregarConceptoPQ "
                            + concepto.getIdConceptoFacturacion()
                            + ", " + concepto.getIdUnidadMedida()
                            + ", " + concepto.getMinimo()
                            + ", " + concepto.getMaximo()
                            + ", " + concepto.getImporte()
                            + ", " + concepto.getIdTipoCalculo()
                            + ", NULL"
                            + ", " + concepto.getIdViajeForaneoGrupo()
                            + ", NULL"
                            + ", " + concepto.getPorcentaje();
                    statement.execute(query);
                }
                for (TarifaProductoRequest producto : grupo.getProductos()) {
                    producto.setIdViajeForaneoGrupo(jsonViajeForaneoGrupo.getInt("id"));
                    query = " EXEC usp_CatTarifasRangosAgregarProductoPQ " + producto.getIdProducto() + ", NULL," + producto.getIdViajeForaneoGrupo();
                    statement.execute(query);
                }
            }
        }
    }
}
