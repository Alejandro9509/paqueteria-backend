package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.EnvioCorreosRequest;
import com.certuit.base.domain.request.base.satPaginadoRequest;
import com.certuit.base.service.base.GuiaService;
import com.certuit.base.service.base.InformesService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
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
public class SATRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    InformesService informesService;
    @Autowired
    GuiaService guiaService;

    @GetMapping("/SAT/ObtenerClavesCancelacion")
    public ResponseEntity<?> getFechaInicial(@RequestHeader("RFC") String rfc) throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "exec usp_CatMotivosCancelacionSATPQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/SAT/{idViaje}/Informe/{id}/EnviarCorreoFactura")
    public ResponseEntity<?> enviarCorreoInforme(@PathVariable("id") int id,
                                                 @PathVariable("idViaje") int idViaje,
                                                 @RequestHeader("RFC") String rfc,
                                                 @RequestBody EnvioCorreosRequest request) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select CorreoFacturaViaje from CatParametrosConfiguracion2PQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            String emailTemplate = jsonObject.getString("CorreoFacturaViaje");
            informesService.enviarCorreoFacturacion(emailTemplate, id, jdbcConnection,
                    request.getCorreos().toLowerCase().split(","), request.getCorreoDefault(), rfc, idViaje);
            return ResponseEntity.ok("Se envió el correo con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/SAT/UltimaMilla/{id}/{esRecoleccion}/EnviarCorreoFactura")
    public ResponseEntity<?> enviarCorreoUltimaMilla(@PathVariable("id") int id,
                                                     @PathVariable("esRecoleccion") boolean esRecoleccion,
                                                     @RequestHeader("RFC") String rfc,
                                                     @RequestBody EnvioCorreosRequest request) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select CorreoFacturaUltimaMilla from CatParametrosConfiguracion2PQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            String emailTemplate = jsonObject.getString("CorreoFacturaUltimaMilla");
            guiaService.enviarCorreoFacturacion(emailTemplate, id, jdbcConnection,
                    request.getCorreos().toLowerCase().split(","), request.getCorreoDefault(), rfc, esRecoleccion);
            return ResponseEntity.ok("Se envió el correo con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/SAT/GetListadoPaginado/{nRegistros}/{nPagina}")
    public ResponseEntity<?> getListadoPaginado(@PathVariable("nRegistros") int nRegistros,
                                                @PathVariable("nPagina") int nPagina,
                                                @RequestBody satPaginadoRequest sat,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "exec usp_CatCatalogoSATPaginadoPQ '" + sat.getBusqueda() + "', '"
                    + sat.getCatalogo() + "', " + nPagina + " , " + nRegistros;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject json;
            while (rs.next()) {
                json = new JSONObject();
                json.put("m_sClaveSAT", rs.getString("m_sClaveSAT"));
                json.put("m_sCatalogoSAT", rs.getString("m_sCatalogoSAT"));
                json.put("m_sDescripcion", rs.getString("m_sDescripcion"));
                json.put("m_bMaterialPeligroso", rs.getBoolean("m_bMaterialPeligroso"));
                json.put("m_bMaterialPeligrosoOpcional", rs.getBoolean("m_bMaterialPeligrosoOpcional"));
                array.put(json);
            }

            return ResponseEntity.ok(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/SAT/GetListado/{catalogo}/{busqueda}")
    public ResponseEntity<?> getListado(@PathVariable("catalogo") String catalogo,
                                        @PathVariable("busqueda") String busqueda,
                                        @RequestHeader("RFC") String rfc) throws Exception {
        JSONObject jsonAux = new JSONObject();
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "select ClaveSAT                                                as m_sClaveSAT,\n" +
                        "       Descripcion                                             as m_sDescripcion,\n" +
                        "       CatalogoSAT                                             as m_sCatalogoSAT,\n" +
                        "       (CASE WHEN MaterialPeligroso = '1' THEN 1 ELSE 0 END)   as m_bMaterialPeligroso,\n" +
                        "       (CASE WHEN MaterialPeligroso = '0,1' THEN 1 ELSE 0 END) as m_bMaterialPeligrosoOpcional\n" +
                        "from CatGeneralesSAT\n" +
                        "where CatalogoSAT =  '" + catalogo + "'" +
                        " and ( '" + busqueda + "' is NULL OR \n" +
                        "           (ClaveSAT LIKE '%' + UPPER( '" + busqueda + "') + '%' or " +
                        "Descripcion LIKE '%' + UPPER('" + busqueda + "') + '%'))\n" +
                        "    ORDER BY ClaveSAT";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                jsonAux = convertObject(rs);

                if (jsonAux == null) {
                    return ResponseEntity.ok("No se encontro la clave, verifique la clave.");
                } else {
                    jsonAux.put("Estatus", true);
                    return ResponseEntity.ok(jsonAux.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
        }
    }

    @GetMapping("/SAT/Busqueda/{catalogo}/{busqueda}")
    public ResponseEntity<?> getListadoBusqueda(@PathVariable("catalogo") String catalogo,
                                                @PathVariable("busqueda") String busqueda,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        JSONObject jsonAux = new JSONObject();
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "select ClaveSAT                                                as m_sClaveSAT,\n" +
                        "       Descripcion                                             as m_sDescripcion,\n" +
                        "       CatalogoSAT                                             as m_sCatalogoSAT,\n" +
                        "       (CASE WHEN MaterialPeligroso = '1' THEN 1 ELSE 0 END)   as m_bMaterialPeligroso,\n" +
                        "       (CASE WHEN MaterialPeligroso = '0,1' THEN 1 ELSE 0 END) as m_bMaterialPeligrosoOpcional\n" +
                        "from CatGeneralesSAT\n" +
                        "where CatalogoSAT =  '" + catalogo + "'" +
                        " and ClaveSAT = '" + busqueda + "' \n" +
                        "    ORDER BY ClaveSAT";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                jsonAux = convertObject(rs);

                if (jsonAux == null) {
                    return ResponseEntity.ok("No se encontro la clave, verifique la clave.");
                } else {
                    jsonAux.put("Estatus", true);
                    return ResponseEntity.ok(jsonAux.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar el embarque");
        }
    }

    @GetMapping("/SAT/ObtenerClavesByInforme/{idInforme}")
    public ResponseEntity<?> getFechaInicial(@RequestHeader("RFC") String rfc,
                                             @PathVariable("idInforme") int idInforme) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "select guia.FolioGuia as [Folio guia], " +
                    "complemento.ClaveProductoServicio AS [Clave SAT Producto o Servicio], " +
                    "complemento.ClaveUnidad AS [Clave SAT Unidad], " +
                    "complemento.Cantidad, complemento.Peso AS [Peso (kg)], " +
                    "IIF(complemento.EsMaterialPeligroso = 1, 'SI', 'NO') AS [Material Peligroso], " +
                    "complemento.ClaveMaterialPeligroso AS [Clave SAT Material Peligroso], " +
                    "complemento.ClaveEmbalaje AS [Clave SAT Embalaje], " +
                    "complemento.ClaveFraccionArancelaria AS [Clave SAT Fraccion Arancelaria] " +
                    "from ProEmbarqueComplementosSATPQ complemento " +
                    "left join CatGeneralesSAT satProducto on satProducto.ClaveSAT = complemento.ClaveProductoServicio " +
                    "and satProducto.CatalogoSAT = 'c_ClaveProdServCP' " +
                    "left join CatGeneralesSAT satUnidad on satUnidad.ClaveSAT = complemento.ClaveUnidad " +
                    "and satUnidad.CatalogoSAT = 'c_ClaveUnidad' " +
                    "left join CatGeneralesSAT satPeligroso on complemento.EsMaterialPeligroso = 1 " +
                    "and satPeligroso.ClaveSAT = complemento.ClaveMaterialPeligroso " +
                    "and satPeligroso.CatalogoSAT = 'c_MaterialPeligroso' " +
                    "left join CatGeneralesSAT satEmbalaje on complemento.EsMaterialPeligroso = 1 " +
                    "and satEmbalaje.ClaveSAT = complemento.ClaveEmbalaje " +
                    "and satEmbalaje.CatalogoSAT = 'c_TipoEmbalaje' " +
                    "left join CatGeneralesSAT satArancelaria on complemento.EsMaterialPeligroso = 1 " +
                    "and satArancelaria.ClaveSAT = complemento.ClaveFraccionArancelaria " +
                    "and satArancelaria.CatalogoSAT = 'c_FraccionArancelaria' " +
                    "inner join ProGuiaPQ guia on guia.IdEmbarque = complemento.IdEmbarque " +
                    "inner join ProInformeGuiaPQ inf on inf.IdGuia = guia.IdGuia where inf.IdInforme =" + idInforme;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/SAT/ValidarComplemento/{catalogoSat}/{claveSat}")
    public ResponseEntity<?> validarComplementos(@RequestHeader("RFC") String rfc,
                                                 @PathVariable("catalogoSat") String catalogoSat,
                                                 @PathVariable("claveSat") String claveSat) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = " SELECT ClaveSAT FROM CatGeneralesSAT " +
                        "WHERE UPPER(CatalogoSAT) = UPPER('" + catalogoSat + "') " +
                        "AND UPPER(ClaveSAT) = UPPER('" + claveSat + "')";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject json = UtilFuctions.convertObject(rs);

                JSONObject response = new JSONObject();
                if (json == null) {
                    response.put("success", false);
                    response.put("message", "No se encontró la clave: " + claveSat);
                    return ResponseEntity.ok(response.toString());
                }
                response.put("success", true);
                response.put("message", json.getString("ClaveSAT"));
                return ResponseEntity.ok(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al validar complementos");
        }
    }
}
