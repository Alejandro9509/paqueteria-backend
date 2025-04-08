package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.service.base.TarifasService;
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
public class TarifasRest {
    @Autowired
    DBConection dbConection;

    @Autowired
    TarifasService tarifasService;

    @GetMapping("/Tarifas/GetByTipo/{idTipoTarifa}")
    public ResponseEntity<?> getListadoTarifasByTipo(@PathVariable("idTipoTarifa") int idTipotarifa,
                                                     @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "";
            switch (idTipotarifa) {
                case 1:
                    query = "SELECT \n" +
                            "        t.IdTarifa as m_nIdTarifa, \n" +
                            "        t.IdSucursal as m_nIdSucursal, \n" +
                            "        (select Sucursal from CatSucursales where IdSucursal=t.IdSucursal )as m_sSucursal, \n" +
                            "        (SELECT SUBSTRING( \n" +
                            "                        ( \n" +
                            "                            SELECT ','+   (select od.OrigenDestino " +
                            "from CatOrigenesDestinos od where od.IdOrigenDestino = td.IdDestino) AS 'data()'\n" +
                            "                            FROM CatTarifaDestinosPQ td \n" +
                            "                            WHERE td.IdTarifa = t.IdTarifa \n" +
                            "                            FOR XML PATH('') \n" +
                            "                        ), 2 , 9999))as m_sDestino, \n" +
                            "        (select OrigenDestino from CatOrigenesDestinos " +
                            "where IdOrigenDestino=t.IdOrigen )as m_sOrigen, \n" +
                            "        t.IdDestino as m_nIdDestino, \n" +
                            "        t.IdOrigen as m_nIdOrigen, \n" +
                            "        t.PrecioM3 as m_cPrecioM3, \n" +
                            "        t.PrecioKilo as m_cPrecioKilo, \n" +
                            "        t.FleteMinimo as m_cFleteMinimo, \n" +
                            "        t.MontoMinimo as m_cMontoMinimo, \n" +
                            "        t.PorPesoVolumen as m_bPorPesoVolumen, \n" +
                            "        t.PorRango as m_bPorRango, \n" +
                            "        t.PorRegion as m_bPorRegion, \n" +
                            "        t.FactorConversion as m_nFactorConversion, \n" +
                            "        t.activo as m_bActivo, \n" +
                            "        t.CreadoEl as m_dtCreadoEl, \n" +
                            "        t.CreadoPor, \n" +
                            "        t.ModificadoEl as m_dtModificadoEl, \n" +
                            "        t.ModificadoPor as m_nModificadoPor, \n" +
                            "        t.Codigo as m_sCodigo, \n" +
                            "        t.IdCliente as m_nIdCliente, \n" +
                            "    cliente.NombreFiscal as Cliente \n" +
                            "    FROM CatTarifaPQ t \n" +
                            "    LEFT JOIN CatOrigenesDestinos origen ON t.IdOrigen = origen.IdOrigenDestino \n" +
                            "    left join CatClientes cliente on cliente.IdCliente=t.IdCliente \n" +
                            "    WHERE t.PorPesoVolumen = 1";
                    break;
                case 3:
                    query = "SELECT \n" +
                            "        t.IdTarifa as m_nIdTarifa, \n" +
                            "        t.IdSucursal as m_nIdSucursal, \n" +
                            "        (select Sucursal from CatSucursales where IdSucursal=t.IdSucursal )as m_sSucursal, \n" +
                            "        (SELECT SUBSTRING( \n" +
                            "                        ( \n" +
                            "                            SELECT ','+   (select od.OrigenDestino " +
                            "from CatOrigenesDestinos od where od.IdOrigenDestino = td.IdDestino) AS 'data()'\n" +
                            "                            FROM CatTarifaDestinosPQ td \n" +
                            "                            WHERE td.IdTarifa = t.IdTarifa \n" +
                            "                            FOR XML PATH('') \n" +
                            "                        ), 2 , 9999))as m_sDestino, \n" +
                            "        (select OrigenDestino from CatOrigenesDestinos " +
                            "where IdOrigenDestino=t.IdOrigen )as m_sOrigen, \n" +
                            "        t.IdDestino as m_nIdDestino, \n" +
                            "        t.IdOrigen as m_nIdOrigen, \n" +
                            "        t.PrecioM3 as m_cPrecioM3, \n" +
                            "        t.PrecioKilo as m_cPrecioKilo, \n" +
                            "        t.FleteMinimo as m_cFleteMinimo, \n" +
                            "        t.MontoMinimo as m_cMontoMinimo, \n" +
                            "        t.PorPesoVolumen as m_bPorPesoVolumen, \n" +
                            "        t.PorRango as m_bPorRango, \n" +
                            "        t.PorRegion as m_bPorRegion, \n" +
                            "        t.FactorConversion as m_nFactorConversion, \n" +
                            "        t.activo as m_bActivo, \n" +
                            "        t.CreadoEl as m_dtCreadoEl, \n" +
                            "        t.CreadoPor, \n" +
                            "        t.ModificadoEl as m_dtModificadoEl, \n" +
                            "        t.ModificadoPor as m_nModificadoPor, \n" +
                            "        t.Codigo as m_sCodigo, \n" +
                            "        t.IdCliente as m_nIdCliente, \n" +
                            "    cliente.NombreFiscal as Cliente \n" +
                            "    FROM CatTarifaPQ t \n" +
                            "    LEFT JOIN CatOrigenesDestinos origen ON t.IdOrigen = origen.IdOrigenDestino \n" +
                            "    left join CatClientes cliente on cliente.IdCliente=t.IdCliente \n" +
                            "    WHERE t.PorRegion = 1";
                    break;
                default:
                    query = "SELECT \n" +
                            "        t.IdTarifa as m_nIdTarifa, \n" +
                            "        t.IdSucursal as m_nIdSucursal, \n" +
                            "        (select Sucursal from CatSucursales where IdSucursal=t.IdSucursal )as m_sSucursal, \n" +
                            "        (SELECT SUBSTRING( \n" +
                            "                        ( \n" +
                            "                            SELECT ','+   (select od.OrigenDestino " +
                            "from CatOrigenesDestinos od where od.IdOrigenDestino = td.IdDestino) AS 'data()'\n" +
                            "                            FROM CatTarifaDestinosPQ td \n" +
                            "                            WHERE td.IdTarifa = t.IdTarifa \n" +
                            "                            FOR XML PATH('') \n" +
                            "                        ), 2 , 9999))as m_sDestino, \n" +
                            "        (select OrigenDestino from CatOrigenesDestinos " +
                            "where IdOrigenDestino=t.IdOrigen )as m_sOrigen, \n" +
                            "        t.IdDestino as m_nIdDestino, \n" +
                            "        t.IdOrigen as m_nIdOrigen, \n" +
                            "        t.PrecioM3 as m_cPrecioM3, \n" +
                            "        t.PrecioKilo as m_cPrecioKilo, \n" +
                            "        t.FleteMinimo as m_cFleteMinimo, \n" +
                            "        t.MontoMinimo as m_cMontoMinimo, \n" +
                            "        t.PorPesoVolumen as m_bPorPesoVolumen, \n" +
                            "        t.PorRango as m_bPorRango, \n" +
                            "        t.PorRegion as m_bPorRegion, \n" +
                            "        t.FactorConversion as m_nFactorConversion, \n" +
                            "        t.activo as m_bActivo, \n" +
                            "        t.CreadoEl as m_dtCreadoEl, \n" +
                            "        t.CreadoPor, \n" +
                            "        t.ModificadoEl as m_dtModificadoEl, \n" +
                            "        t.ModificadoPor as m_nModificadoPor, \n" +
                            "        t.Codigo as m_sCodigo, \n" +
                            "        t.IdCliente as m_nIdCliente, \n" +
                            "    cliente.NombreFiscal as Cliente \n" +
                            "    FROM CatTarifaPQ t \n" +
                            "    LEFT JOIN CatOrigenesDestinos origen ON t.IdOrigen = origen.IdOrigenDestino \n" +
                            "    left join CatClientes cliente on cliente.IdCliente=t.IdCliente \n";
                    break;
            }

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Tarifas/GetById/{id}")
    public ResponseEntity<?> getTarifaById(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT \n" +
                    "    ctpq.IdTarifa as m_nIdTarifa, \n" +
                    "    ctpq.IdOrigen as m_nIdOrigen, \n" +
                    "    cto.OrigenDestino as m_sOrigen, \n" +
                    "    ctpq.FleteMinimo as m_cFleteMinimo, \n" +
                    "    ctpq.PorPesoVolumen as m_bPorPesoVolumen, \n" +
                    "    ctpq.PorRango as m_bPorRango, \n" +
                    "    ctpq.PorRegion as m_bPorRegion, \n" +
                    "    ctpq.activo as m_bActivo, \n" +
                    "    ctpq.Codigo as m_sCodigo, \n" +
                    "    ctpq.IdCliente as m_nIdCliente, \n" +
                    "    cliente.NombreFiscal as m_sCliente \n" +
                    "    FROM CatTarifaPQ ctpq \n" +
                    "        left join CatOrigenesDestinos cto on cto.IdOrigenDestino=ctpq.IdOrigen \n" +
                    "        left join CatClientes cliente on cliente.IdCliente=ctpq.IdCliente \n" +
                    "    WHERE ctpq.IdTarifa = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            jsonObject.put("m_arrArViajes", tarifasService.getViajesByIdTarifa(id, jdbcConnection));
            jsonObject.put("m_arrArDestinos", tarifasService.getDestinosByIdTarifa(id, jdbcConnection));
            jsonObject.put("m_arrArProductos", tarifasService.getProductosByIdTarifa(id, jdbcConnection));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Tarifas/Agregar")
    public ResponseEntity<?> postTarifa(@RequestBody TarifaRequest request, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String query = " EXEC usp_CatTarifaAgregarPQ "
                    + request.getIdCliente()
                    + "," + request.getTipo();
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            statement.execute(query);

            query = "select @@identity as id";
            rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            int idTarifa = jsonObject.getInt("id");
            for (TarifaViajeRequest viajeRequest : request.getViajes()) {
                viajeRequest.setIdTarifa(idTarifa);
                query = "EXEC usp_CatTarifaViajesAgregarPQ " + viajeRequest.getIdTarifa() + ","
                        + viajeRequest.getIdOrigen() + "," + viajeRequest.getFleteMinimo();
                statement.execute(query);
                query = "select @@identity as id";
                rs = statement.executeQuery(query);
                JSONObject jsonObject2 = UtilFuctions.convertObject(rs);
                int idViaje = jsonObject2.getInt("id");
                for (OrigenDestinoRequest destino : viajeRequest.getDestinos()) {
                    destino.setIdTarifaViaje(idViaje);
                    query = "EXEC usp_CatTarifaDestinosAgregarPQ " + destino.getIdTarifaViaje() + ","
                            + destino.getM_nIdCiudad();
                    statement.execute(query);
                }
                for (ProductoRequest producto : viajeRequest.getProductos()) {
                    producto.setIdTarifaViaje(idViaje);
                    query = "EXEC usp_CatTarifaProductosAgregarPQ " + producto.getIdTarifaViaje() + ","
                            + producto.getM_nIdProducto() + "," + producto.getM_cImporte();
                    statement.execute(query);
                }
            }

            JSONObject jsonFin = new JSONObject();
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Tarifas/Modificar/{idTarifa}")
    public ResponseEntity<?> putTarifa(@PathVariable("idTarifa") int idTarifa, @RequestBody TarifaRequest request,
                                       @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            ResultSet rs;
            String query = " EXEC usp_CatTarifaModificarPQ "
                    + idTarifa
                    + "," + request.getIdCliente();
            Statement statement = jdbcConnection.createStatement();
            statement.execute(query);
            for (TarifaViajeRequest viajeRequest : request.getViajes()) {
                viajeRequest.setIdTarifa(idTarifa);
                query = "EXEC usp_CatTarifaViajesAgregarPQ " + viajeRequest.getIdTarifa() + ","
                        + viajeRequest.getIdOrigen() + "," + viajeRequest.getFleteMinimo();
                statement.execute(query);
                query = "select @@identity as id";
                rs = statement.executeQuery(query);
                JSONObject jsonObject2 = UtilFuctions.convertObject(rs);
                int idViaje = jsonObject2.getInt("id");
                for (OrigenDestinoRequest destino : viajeRequest.getDestinos()) {
                    destino.setIdTarifaViaje(idViaje);
                    query = "EXEC usp_CatTarifaDestinosAgregarPQ " + destino.getIdTarifaViaje() + ","
                            + destino.getM_nIdCiudad();
                    statement.execute(query);
                }
                for (ProductoRequest producto : viajeRequest.getProductos()) {
                    producto.setIdTarifaViaje(idViaje);
                    query = "EXEC usp_CatTarifaProductosAgregarPQ " + producto.getIdTarifaViaje() + ","
                            + producto.getM_nIdProducto() + "," + producto.getM_cImporte();
                    statement.execute(query);
                }
            }

            JSONObject jsonFin = new JSONObject();
            jsonFin.put("Estatus", true);

            return ResponseEntity.ok(jsonFin.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/Tarifas/Eliminar/{idTarifa}/{idUsuario}")
    public ResponseEntity<?> deleteTarifa(@PathVariable("idTarifa") int idTarifa, @RequestHeader("RFC") String rfc) {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_CatTarifaEliminarPQ " + idTarifa;
                statement.executeUpdate(query);

                return ResponseEntity.ok("Registro eliminado correctamente");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al eliminar el registro. Intente más tarde.");
        }
    }
}
