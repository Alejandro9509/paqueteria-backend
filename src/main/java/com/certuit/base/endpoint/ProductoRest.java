package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.ProductoAgregarRequest;
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

import static com.certuit.base.util.UtilFuctions.convertObject;

@RestController
@RequestMapping(value = "/api")
public class ProductoRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Productos/GetListado")
    public ResponseEntity<?> getProducto(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdProducto as m_nIdProducto, " +
                    "NoProducto as m_nNoProducto, " +
                    "Descripcion as m_sDescripcion, " +
                    "Embalaje as m_sEmbalaje, " +
                    "IdEmbalaje as m_nIdEmbalaje, " +
                    "Largo as m_xLargo, " +
                    "Ancho as m_xAncho, " +
                    "Alto as m_xAlto," +
                    "Predeterminado as m_bPredeterminado," +
                    "Peso as m_xPeso," +
                    "Activo as m_bActivo," +
                    "IdUnidadMedidaVolumen," +
                    "IdUnidadMedidaPeso " +
                    " FROM CatProductosPQ ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Productos/GetById/{id}")
    public ResponseEntity<?> getProductosById(@RequestHeader("RFC") String rfc, @PathVariable("id") int id)
            throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "IdProducto as m_nIdProducto, " +
                    "NoProducto as m_nNoProducto, " +
                    "Descripcion as m_sDescripcion, " +
                    "IdEmbalaje as m_nIdEmbalaje, " +
                    "Embalaje as m_sEmbalaje, " +
                    "Largo as m_xLargo, " +
                    "Alto as m_xAlto, " +
                    "Ancho as m_xAncho, " +
                    "Predeterminado as m_bPredeterminado," +
                    "Peso as m_xPeso, " +
                    "Activo as m_bActivo " +
                    "FROM " +
                    "CatProductosPQ WHERE IdProducto=" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject2 = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Productos/GetByConvenioCliente/{idCliente}")
    public ResponseEntity<?> getProductosByConvenioCliente(@RequestHeader("RFC") String rfc,
                                                           @PathVariable("idCliente") int idCliente)
            throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdProducto  as m_nIdProducto," +
                    "       NoProducto  as m_nNoProducto," +
                    "       Descripcion as m_sDescripcion," +
                    "       Embalaje    as m_sEmbalaje," +
                    "       IdEmbalaje  as m_nIdEmbalaje," +
                    "       Largo       as m_xLargo," +
                    "       Ancho       as m_xAncho," +
                    "       Alto        as m_xAlto," +
                    "       Peso        as m_xPeso," +
                    "       Activo      as m_bActivo," +
                    "       IdUnidadMedidaVolumen," +
                    "       IdUnidadMedidaPeso " +
                    "FROM CatProductosPQ " +
                    "where IdProducto in (" +
                    "    select producto.IdProducto " +
                    "    from CatProductosTarifaConvenioPQ producto " +
                    "    where IdTarifaConvenio in (" +
                    "        Select tarifa.IdTarifaConvenio " +
                    "        from CatTarifaConvenioPQ tarifa " +
                    "        where IdConvenio = (" +
                    "            Select convenio.IdConvenio " +
                    "            from CatConveniosPQ convenio " +
                    "            where IdCliente = " + idCliente +
                    "        )" +
                    "    )" +
                    "    group by IdProducto" +
                    ") or Predeterminado = 1";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Productos/Eliminar/{idProducto}")
    public ResponseEntity<?> eliminarProducto(@PathVariable("idProducto") int idProducto,
                                              @RequestHeader("RFC") String rfc)
            throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = " select RegistroDeSistema from CatProductosPQ where IdProducto = " + idProducto;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);
            jsonObject.put("RegistroDeSistema", rs.getBoolean("RegistroDeSistema"));

            if (jsonObject.getBoolean("RegistroDeSistema")) {
                return ResponseEntity.ok("No se puede eliminar el producto, porque es registro del sistema.");
            } else {
                query = "EXEC usp_CatProductosEliminarPQ " + idProducto + "";
                statement.executeUpdate(query);
                int resultado = statement.executeUpdate(query);
                return ResponseEntity.ok("Producto Eliminado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Productos/Agregar")
    public ResponseEntity<?> agregarProdutos(@RequestBody ProductoAgregarRequest p,
                                             @RequestHeader("RFC") String rfc) throws SQLException, Exception {

        if (p.getM_sDescripcion() == "") {
            return ResponseEntity.status(500).body("La descripción del Producto es un campo requerido");
        }

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_CatProductosAgregarPQ '" + p.getM_sDescripcion() + "','"
                        + p.getM_sEmbalaje() + "'," + p.getM_nIdEmbalaje() + "," + p.getM_xLargo() + ","
                        + p.getM_xAncho() + "," + p.getM_xAlto() + "," + p.getM_xPeso() + ","
                        + (p.isM_bActivo() ? 1 : 0) + "," + p.getM_nNoProducto() + ", "
                        + (p.isM_bPredeterminado() ? 1 : 0) + "";
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al agregar producto");
            }
            return ResponseEntity.ok("Agregado Exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Productos/Modificar/{idProducto}")
    public ResponseEntity<?> modificarProducto(@RequestBody ProductoAgregarRequest p,
                                               @PathVariable("idProducto") int idProducto,
                                               @RequestHeader("RFC") String rfc) throws SQLException, Exception {
        if (p.getM_sDescripcion() == "") {
            return ResponseEntity.status(500).body("La descripción del Producto es un campo requerido");
        }

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            try {
                String query = "EXEC usp_CatProductosModificarPQ " + idProducto + ",'" + p.getM_sDescripcion() + "','"
                        + p.getM_sEmbalaje() + "'," + p.getM_nIdEmbalaje() + "," + p.getM_xLargo() + ","
                        + p.getM_xAncho() + "," + p.getM_xAlto() + "," + p.getM_xPeso() + ","
                        + (p.isM_bActivo() ? 1 : 0) + "," + p.getM_nNoProducto() + ", "
                        + (p.isM_bPredeterminado() ? 1 : 0) + "";
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al modificar producto");
            }
            return ResponseEntity.ok("Cambio Exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}
