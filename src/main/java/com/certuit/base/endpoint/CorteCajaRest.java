package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CorteCajaFiltroRequest;
import com.certuit.base.domain.request.base.CorteCajaGuiaRequest;
import com.certuit.base.domain.request.base.CorteCajaRequest;
import com.certuit.base.domain.request.base.PagosRequest;
import com.certuit.base.service.base.ConvenioService;
import com.certuit.base.service.base.CorteCajaService;
import com.certuit.base.service.base.EmbarqueService;
import com.certuit.base.service.base.GuiaService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;

import static com.certuit.base.util.UtilFuctions.*;

@RestController
@RequestMapping(value = "/api")
public class CorteCajaRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    ConvenioService convenioService;
    @Autowired
    GuiaService guiaService;
    @Autowired
    EmbarqueService embarqueService;
    @Autowired
    CorteCajaService corteCajaService;

    @GetMapping("/CorteCaja/GetListadoByFiltros/{fecha}/{idOperador}/{idUsuario}")
    public ResponseEntity<?> getListadoConvenios(@PathVariable("fecha") String fecha,
                                                 @PathVariable("idOperador") int idOperador,
                                                 @PathVariable("idUsuario") int idUsuario,
                                                 @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            if (fecha.equalsIgnoreCase("0")) {
                fecha = "null";
            } else {
                fecha = "'" + fecha + "'";
            }
            String query = "EXEC usp_ProCorteCajaGetListadoFiltrosPQ  " + fecha + "," + idOperador + "," + idUsuario;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray cortes = convertArray(rs);
            try {

                for (int i = 0; i < cortes.length(); i++) {
                    JSONObject objeto = cortes.getJSONObject(i);
                    objeto.put("guias", guiaService.getGuiasByIdCorte(objeto.getInt("idCorte"), jdbcConnection));
                }
            } catch (JSONException e) {
                

                e.printStackTrace();
            }

            return ResponseEntity.ok(cortes.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/CorteCaja/GetById/{id}")
    public ResponseEntity<?> getConvenioId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        JSONObject json;
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProCorteCajaGetByIdPQ " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                json = convertObject(rs);
                if (json.getInt("idOperador") > 0) {
                    query = "SELECT IdOperador as m_nIdOperador, Nombre as m_sNombreCompleto FROM CatOperadores " +
                            "WHERE IdOperador = " + json.getInt("idOperador");
                    rs = statement.executeQuery(query);
                    json.put("operador", convertObject(rs));
                }
                if (json.getInt("idUsuario") > 0) {
                    query = "SELECT IdUsuario as idUsuario, Nombre as nombre, Usuario as usuario FROM CatUsuarios " +
                            "WHERE IdUsuario = " + json.getInt("idUsuario");
                    rs = statement.executeQuery(query);
                    json.put("usuario", convertObject(rs));
                }
                json.put("guias", guiaService.getGuiasByIdCorte(json.getInt("idCorte"), jdbcConnection));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al listar el corte de caja");
        }
        return ResponseEntity.ok(json.toString());
    }

    @GetMapping("/CorteCaja/GetListado")
    public ResponseEntity<?> obtenerListadoCorteCaja(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProCorteCajaGetListadoPQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray cortes = convertArray(rs);
            try {
                for (int i = 0; i < cortes.length(); i++) {
                    JSONObject objeto = cortes.getJSONObject(i);
                    objeto.put("guias", guiaService.getGuiasByIdCorte(objeto.getInt("idCorte"), jdbcConnection));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok(cortes.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/CorteCaja/Eliminar/{id}")
    public ResponseEntity<?> eliminarCorte(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProCorteCajaEliminarPQ " + id;
                Statement statement = jdbcConnection.createStatement();
                int resultado = statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Ocurrió un problema al eliminar el corte de caja");
            }
            return ResponseEntity.ok("Registro Eliminado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/CorteCaja/Modificar/{id}")
    public ResponseEntity<?> modificarCorte(@RequestBody CorteCajaRequest corte, @PathVariable("id") int id,
                                            @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                corte.setM_nIdCorte(id);
                String query = "EXEC usp_ProCorteCajaModificarPQ " + corte.getM_nIdCorte() + ","
                        + corte.getM_nIdOperador() + "," + corte.getM_nIdUsuario() + ",'"
                        + corte.getM_sFechaRegistro() + " " + corte.getM_sHoraRegistro() + "'," + corte.getM_cTotal();
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
                for (CorteCajaGuiaRequest c : corte.getM_arrGuias()) {
                    c.setM_nIdCorte(corte.getM_nIdCorte());
                    corteCajaService.agregarCorteCajaGuias(c, statement);
                }
                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar la información");
        }
    }

    @PostMapping("/CorteCaja/Agregar")
    public ResponseEntity<?> agregarCorte(@RequestBody CorteCajaRequest
                                                  corte, @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "EXEC usp_ProCorteCajaAgregarPQ  " + corte.getM_cTotal() + ", " + corte.getM_nIdUsuario()
                        + " ," + corte.getM_nIdOperador() + ",'" + corte.getM_sFechaRegistro() + " "
                        + corte.getM_sHoraRegistro() + "'"
                        + " ," + corte.getM_nIdSucursal();
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
                query = "SELECT IDENT_CURRENT( 'ProCorteCajaPQ') as id";
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                for (CorteCajaGuiaRequest c : corte.getM_arrGuias()) {
                    c.setM_nIdCorte(jsonObject.getInt("id"));
                    corteCajaService.agregarCorteCajaGuias(c, statement);
                }
                return ResponseEntity.ok("Se guardó la información.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un problema al guardar la información");
        }
    }

    //filtro para obtener los datos que estan solicitando
    @PostMapping("/Guias/GetListadoFiltrosCorteCaja")
    public ResponseEntity<?> filtroCorte(@RequestBody CorteCajaFiltroRequest
                                                 filtro, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                if (filtro.getFecha() != null) {
                    filtro.setFecha("'" + filtro.getFecha() + "'");
                }
                String query = "EXEC usp_ProCorteCajaGuiasDisponiblesPQ " + (filtro.getBusquedaPorUsuario() ? 1 : 0) + ", "
                        + filtro.getIdOperador() + ", " + filtro.getIdUsuario() + ", " + filtro.getFecha();
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONArray jsonArray = convertArray(rs);

                return ResponseEntity.ok(jsonArray.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente de nuevo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/CorteCaja/Facturas/{idsFacturas}")
    public ResponseEntity<?> getListadoFacturas(@PathVariable("idsFacturas") String idsFacturas, @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            if (idsFacturas.equalsIgnoreCase("0")) {
                idsFacturas = null;
            } else {
                idsFacturas = "(" + idsFacturas + ")";
            }
            System.out.println(idsFacturas);
            /*String query = "EXEC usp_ProCorteCajaGetListadoFacturasPQ  " + idsFacturas;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONArray facturas = convertArray(rs);*/
            return ResponseEntity.ok(200);//facturas.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/CorteCaja/Parametros")
    public ResponseEntity<?> getListadoFacturas(@RequestHeader("RFC") String rfc) {
        String parametrosQuery = """
            SELECT Parametro, Valor
            FROM SisParametrosValores
            WHERE Parametro IN (
                'HabilitarGeneraciónComprobantesCFDI33',
                'HabilitarGeneraciónComplementosDePagoCFDI33',
                'FacturarConMasDeUnaRazonSocial'
            )
        """;

        String certificadosQuery = """
            SELECT DISTINCT
                RFC,
                IdCertificado,
                VigenteHasta
            FROM CatCertificados
            WHERE EstatusActivo = 1
                AND VigenteHasta > GETDATE()
                AND VigenteDesde <= GETDATE()
            ORDER BY IdCertificado ASC, VigenteHasta DESC
        """;

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            JSONArray parametros;
            boolean habilitado = true;

            try (PreparedStatement ps = jdbcConnection.prepareStatement(parametrosQuery);
                 ResultSet rs = ps.executeQuery()) {

                parametros = convertArray(rs);

                for (int i = 0; i < parametros.length(); i++) {
                    JSONObject obj = parametros.getJSONObject(i);
                    if (obj.optInt("Valor", 0) == 0) {
                        habilitado = false;
                        break;
                    }
                }
            }

            if (!habilitado) {
                return ResponseEntity.ok(new JSONArray().toString());
            }

            try (PreparedStatement ps = jdbcConnection.prepareStatement(certificadosQuery);
                 ResultSet rs = ps.executeQuery()) {

                JSONArray certificados = convertArray(rs);
                return ResponseEntity.ok(certificados.toString());
            }

        } catch (Exception e) {
            // Usa un logger si tienes
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/CorteCaja/CrearPago")
    public ResponseEntity<?> crearPago(@RequestBody PagosRequest request, @RequestHeader("RFC") String rfc)
            throws Exception {
        Connection jdbcConnection;
        try {
            jdbcConnection = dbConection.getconnection(rfc);
            String query = "EXEC usp_ProCobranzaRegistrarPagosAbonos_762 " + request.toString();
            System.out.println(query);
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate(query);
            return ResponseEntity.ok("Se guardó la información.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
