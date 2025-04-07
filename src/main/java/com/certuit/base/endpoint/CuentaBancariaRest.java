package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class CuentaBancariaRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/CuentaBancaria/GetListado")
    public ResponseEntity<?> getListadoCuentasBancarias(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT CB.IdCuentaBancaria\n" +
                    "       , CB.CuentaBancaria\n" +
                    "       , CB.NumeroCuenta\n" +
                    "       , CB.IdMoneda\n" +
                    "       , CM.Moneda\n" +
                    "       , CB.IdSucursal\n" +
                    "       , CB.IdBanco\n" +
                    "FROM CatCuentasBancarias CB\n" +
                    "    LEFT JOIN CatMonedas CM ON CM.IdMoneda = CB.IdMoneda\n" +
                    "WHERE Activa = 1";
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