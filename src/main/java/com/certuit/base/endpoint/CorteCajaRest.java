package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CorteCajaFiltroRequest;
import com.certuit.base.domain.request.base.CorteCajaGuiaRequest;
import com.certuit.base.domain.request.base.CorteCajaRequest;
import com.certuit.base.domain.request.base.PagosRequest;
import com.certuit.base.service.base.CorteCajaService;
import com.certuit.base.service.base.GuiaService;
import com.certuit.base.util.DBConection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.sql.*;
import static com.certuit.base.util.UtilFuctions.*;

@RestController
@RequestMapping(value = "/api")
public class CorteCajaRest {
    @Autowired
    DBConection dbConection;
    @Autowired
    GuiaService guiaService;
    @Autowired
    CorteCajaService corteCajaService;
    private static final Logger log = LoggerFactory.getLogger(CorteCajaRest.class);

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
                log.error("Error: ", e);
            }

            return ResponseEntity.ok(cortes.toString());
        } catch (Exception e) {
            log.error("Error: ", e);
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
                assert json != null;
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
                log.error("Error: ", e);
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. " +
                        "Intente más tarde.");
            }
        } catch (Exception e) {
            log.error("Error: ", e);
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
                log.error("Error: ", e);
            }

            return ResponseEntity.ok(cortes.toString());
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @DeleteMapping("/CorteCaja/Eliminar/{id}")
    public ResponseEntity<?> eliminarCorte(@PathVariable("id") int id, @RequestHeader("RFC") String rfc) {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProCorteCajaEliminarPQ ?";

            try (PreparedStatement ps = jdbcConnection.prepareStatement(query)) {
                ps.setInt(1, id);  // Establecer el parámetro para el procedimiento almacenado

                int resultado = ps.executeUpdate();

                if (resultado > 0) {
                    return ResponseEntity.ok("Registro Eliminado");
                } else {
                    log.warn("No se encontró el corte de caja con el id: {}", id);
                    return ResponseEntity.status(404).body("No se encontró el registro a eliminar");
                }
            } catch (SQLException e) {
                log.error("Error al ejecutar el procedimiento almacenado para eliminar el corte de caja: ", e);
                return ResponseEntity.status(500).body("Ocurrió un problema al eliminar el corte de caja");
            }
        } catch (SQLException | FileNotFoundException e) {
            log.error("Error al establecer la conexión a la base de datos para eliminar el corte de caja: ", e);
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/CorteCaja/Modificar/{id}")
    public ResponseEntity<?> modificarCorte(@RequestBody CorteCajaRequest corte, @PathVariable("id") int id,
                                            @RequestHeader("RFC") String rfc) {
        // Asignar ID del corte recibido en la URL al objeto CorteCajaRequest
        corte.setM_nIdCorte(id);

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_ProCorteCajaModificarPQ ?, ?, ?, ?, ?";

            // Usar PreparedStatement para evitar inyección SQL
            try (PreparedStatement ps = jdbcConnection.prepareStatement(query)) {
                ps.setInt(1, corte.getM_nIdCorte());
                ps.setInt(2, corte.getM_nIdOperador());
                ps.setInt(3, corte.getM_nIdUsuario());
                ps.setString(4, corte.getM_sFechaRegistro() + " " + corte.getM_sHoraRegistro());
                ps.setDouble(5, corte.getM_cTotal());

                int resultado = ps.executeUpdate();

                // Verificar si la actualización se realizó correctamente
                if (resultado > 0) {
                    // Actualizar las guías de corte
                    for (CorteCajaGuiaRequest guia : corte.getM_arrGuias()) {
                        guia.setM_nIdCorte(corte.getM_nIdCorte());
                        corteCajaService.agregarCorteCajaGuias(guia, (Statement) jdbcConnection);
                    }
                    return ResponseEntity.ok("Se guardaron los cambios.");
                } else {
                    log.warn("No se encontró el corte de caja con el id: {}", id);
                    return ResponseEntity.status(404).body("No se encontró el corte de caja para modificar.");
                }
            } catch (SQLException e) {
                log.error("Error al ejecutar el procedimiento almacenado para modificar el corte de caja: ", e);
                return ResponseEntity.status(500).body("Ocurrió un problema al modificar el corte de caja");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | FileNotFoundException e) {
            log.error("Error al establecer la conexión a la base de datos para modificar el corte de caja: ", e);
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/CorteCaja/Agregar")
    public ResponseEntity<?> agregarCorte(@RequestBody CorteCajaRequest corte, @RequestHeader("RFC") String rfc) {
        // Validación básica para el request
        if (corte == null) {
            return ResponseEntity.status(400).body("Los datos del corte no pueden estar vacíos.");
        }

        // Intentar realizar la operación de base de datos
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            // Preparar el query con parámetros para evitar inyección SQL
            String query = "EXEC usp_ProCorteCajaAgregarPQ ?, ?, ?, ?, ?";

            // Usar PreparedStatement para evitar inyección SQL
            try (PreparedStatement ps = jdbcConnection.prepareStatement(query)) {
                ps.setDouble(1, corte.getM_cTotal());
                ps.setInt(2, corte.getM_nIdUsuario());
                ps.setInt(3, corte.getM_nIdOperador());
                ps.setString(4, corte.getM_sFechaRegistro() + " " + corte.getM_sHoraRegistro());
                ps.setInt(5, corte.getM_nIdSucursal());

                int resultado = ps.executeUpdate();

                if (resultado > 0) {
                    // Obtener el ID del último registro insertado
                    query = "SELECT IDENT_CURRENT('ProCorteCajaPQ') AS id";
                    try (Statement statement = jdbcConnection.createStatement();
                         ResultSet rs = statement.executeQuery(query)) {
                        if (rs.next()) {
                            int corteId = rs.getInt("id");
                            // Actualizar las guías asociadas con el corte
                            for (CorteCajaGuiaRequest guia : corte.getM_arrGuias()) {
                                guia.setM_nIdCorte(corteId);
                                corteCajaService.agregarCorteCajaGuias(guia, (Statement) jdbcConnection);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return ResponseEntity.ok("Se guardó la información.");
                } else {
                    log.warn("No se pudo agregar el corte de caja.");
                    return ResponseEntity.status(400).body("No se pudo agregar el corte de caja.");
                }

            } catch (SQLException e) {
                log.error("Error al ejecutar el procedimiento almacenado para agregar el corte de caja: ", e);
                return ResponseEntity.status(500).body("Ocurrió un problema al guardar la información.");
            }

        } catch (SQLException | FileNotFoundException e) {
            log.error("Error al establecer la conexión a la base de datos para agregar el corte de caja: ", e);
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    //filtro para obtener los datos que estan solicitando
    @PostMapping("/Guias/GetListadoFiltrosCorteCaja")
    public ResponseEntity<?> filtroCorte(@RequestBody CorteCajaFiltroRequest
                                                 filtro, @RequestHeader("RFC") String rfc)  throws Exception {
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
                log.error("Error: ", e);
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente de nuevo.");
            }
        } catch (Exception e) {
            log.error("Error: ", e);
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
            log.error("Error: ", e);
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

            log.error("Error: ", e);
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
            log.error("Error: ", e);
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
