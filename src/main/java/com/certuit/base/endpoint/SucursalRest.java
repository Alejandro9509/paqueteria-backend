package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class SucursalRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/Sucursales/GetListado")
    public ResponseEntity<?> getSucursal(@RequestHeader("RFC") String rfc) throws SQLException, Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT " +
                    "s.IdSucursal as m_nIdSucursal, " +
                    "s.Sucursal as m_sSucursal, " +
                    "s.Abreviacion as m_sAbreviacion, " +
                    "s.Calle as m_sCalle, " +
                    "s.NoExterior as m_sNoExterior, " +
                    "s.NoInterior as m_sNoInterior, " +
                    "s.Colonia as m_sColonia, " +
                    "s.Localidad as m_sLocalidad, " +
                    "s.Municipio as m_sMunicipio, " +
                    "s.IdEstado as m_nIdEstado, " +
                    "s.Activa as m_bActiva, " +
                    "s.CodigoPostal as m_sCodigoPostal ," +
                    "(SELECT TOP 1 cp.IdCodigoPostal FROM CatCodigosPostales cp WHERE " +
                    "(cp.CodigoPostal = s.CodigoPostal AND s.IdEstado = s.IdEstado " +
                    "AND UPPER(cp.Municipio) = UPPER(s.Municipio))) AS m_nIdCodigoPostal " +
                    "FROM CatSucursales s ";
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
