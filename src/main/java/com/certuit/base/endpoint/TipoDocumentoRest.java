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
public class TipoDocumentoRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/TipoDocumento/GetListado/{id}")
    public ResponseEntity<?> getOrigenDestino(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select CTDVT.IdTipoDocumentoConfiguracionTimbrado as IdDocumento, " +
                    "CTDVT.IdSucursal, (CTD.Documento + '-' + ISNULL(CTDVT.Serie,'') + '('+ " +
                    "(case when CTDVT.Complemento = 1 then 'Traslado' when CTDVT.Complemento = 2 then 'Ingreso' " +
                    "ELSE 'Ninguno' end) + ')') as Documento, CTDVT.Complemento as IdComplemento " +
                    "from CatTiposDocumentosConfiguracionTimbrado CTDVT " +
                    "inner join CatTiposDocumentos CTD on CTDVT.IdTipoDocumento = CTD.IdTipoDocumento " +
                    "where CTDVT.Complemento = 2 and CTDVT.IdSucursal =" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();
            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/TipoDocumento/GetListado")
    public ResponseEntity<?> getTiposDocumento(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select CTDVT.IdTipoDocumentoConfiguracionTimbrado as idDocumento, " +
                    "(CTD.Documento + '-' + ISNULL(CTDVT.Serie,'') + '('+ (case when CTDVT.Complemento = 1 " +
                    "then 'Traslado' when CTDVT.Complemento = 2 then 'Ingreso' ELSE 'Ninguno' end) + ')') as documento, " +
                    "CTDVT.Complemento as idComplemento\n" +
                    "                                from CatTiposDocumentosConfiguracionTimbrado CTDVT\n" +
                    "                                         inner join CatTiposDocumentos CTD\n" +
                    "                                                    on CTDVT.IdTipoDocumento = CTD.IdTipoDocumento";
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
