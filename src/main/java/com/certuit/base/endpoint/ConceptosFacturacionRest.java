package com.certuit.base.endpoint;

import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping(value = "/api")
public class ConceptosFacturacionRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/ConceptosFacturacion/GetListado")
    public ResponseEntity<?> getListadoConceptosFacturacion(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "    IdConceptoFacturacion as m_nIdConceptosFacturacion\n" +
                    "     ,Codigo as m_sCodigo\n" +
                    "     ,ConceptoFacturacion as m_sConcepto\n" +
                    "     ,Activo as m_bActivo\n" +
                    "     ,IncluirCalculoIngresosLiquidacion as m_bCalculoIngreso\n" +
                    "     ,IncluirCalculoLiquidacionPorcentajeSobreImpFlete as m_bCalculoFlete\n" +
                    "     ,ClaveSATProducto as m_nIdProdServSAT\n" +
                    "     ,UnidadMedida as m_sUnidadMedida\n" +
                    "     ,CreadoEl as m_dtCreadoEl\n" +
                    "     ,ModificadoEl as m_dtModificadoEl\n" +
                    "     ,ModificadoPor as m_nModificadoPor\n" +
                    "     ,CreadoPor as m_nCreadoPor\n" +
                    "\tFROM CatConceptosFacturacion";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ConceptosFacturacion/GetImpuestosByIdConcepto/{id}")
    public ResponseEntity<?> getListado(@PathVariable("id") int id, @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT Detalle.IdConceptoFacturacionImpuesto as IdConceptosFacturacionDetalle\n" +
                    "      ,Detalle.IdConceptoFacturacion as IdConceptosFacturacion\n" +
                    "      ,IdImpuesto\n" +
                    "      ,(SELECT CASE WHEN TipoCalculo = 1 THEN 1 ELSE 0 END  from CatImpuestos " +
                    "WHERE IdImpuesto = Detalle.IdImpuesto) AS Trasladado\n" +
                    "\t  ,(SELECT Impuesto FROM CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Impuesto\n" +
                    "\t  ,(SELECT Porcentaje FROM CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Porcentaje\n" +
                    "      ,Detalle.Predeterminado\n" +
                    "    FROM CatConceptosFacturacionImpuestos AS Detalle\n" +
                    "\tWHERE IdConceptoFacturacion = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_bPredeterminado", rs.getBoolean("Predeterminado"));
                json.put("m_bTrasladado", rs.getBoolean("Trasladado"));
                json.put("m_nIdConceptosFacturacion", rs.getInt("IdConceptosFacturacion"));
                json.put("m_nIdConceptosFacturacionDetalle", rs.getInt("IdConceptosFacturacionDetalle"));
                json.put("m_nIdImpuesto", rs.getInt("IdImpuesto"));
                json.put("m_xPorcentaje", rs.getInt("Porcentaje"));
                json.put("m_sImpuesto", rs.getString("Impuesto"));
                array.put(json);
            }
            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/ConceptosFacturacion/GetListado/Maniobra")
    public ResponseEntity<?> getListadoManiobraCF(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT\n" +
                    "    IdConceptoFacturacion as m_nIdConceptosFacturacion\n" +
                    "     ,Codigo as m_sCodigo\n" +
                    "     ,ConceptoFacturacion as m_sConcepto\n" +
                    "     ,Activo as m_bActivo\n" +
                    "     ,IncluirCalculoIngresosLiquidacion as m_bCalculoIngreso\n" +
                    "     ,IncluirCalculoLiquidacionPorcentajeSobreImpFlete as m_bCalculoFlete\n" +
                    "     ,(select IdCatUnidadMedidaSAT from CatUnidadMedidaSATPQ cs " +
                    "where cs.ClaveUnidad = ClaveSATUnidad COLLATE Modern_Spanish_CS_AS) as m_nIdUnidadMedidaSAT\n" +
                    "     ,ClaveSATProducto as m_nIdProdServSAT\n" +
                    "     ,UnidadMedida as m_sUnidadMedida\n" +
                    "     ,CreadoEl as m_dtCreadoEl\n" +
                    "     ,ModificadoEl as m_dtModificadoEl\n" +
                    "     ,ModificadoPor as m_nModificadoPor\n" +
                    "     ,CreadoPor as m_nCreadoPor\n" +
                    "\tFROM CatConceptosFacturacion WHERE IdConceptoFacturacion in (16,17)";
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
