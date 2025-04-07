package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class UsuarioRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Usuarios/GetListado")
    public ResponseEntity<?> getUsers(@RequestHeader("RFC") String rfc) throws SQLException, Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select IdUsuario as idUsuario, Usuario as usuario, Nombre as nombre, " +
                    "userr.IdSucursal as idSucursal, suc.Sucursal as sucursal " +
                    "from CatUsuarios userr " +
                    "left join CatSucursales suc on suc.IdSucursal = userr.IdSucursal where Activo = 1;";
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
