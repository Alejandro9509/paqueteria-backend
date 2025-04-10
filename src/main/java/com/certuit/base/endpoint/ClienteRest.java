package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.LoginRequest;
import com.certuit.base.domain.request.base.SearchRequest;
import com.certuit.base.domain.request.base.SeguroRequest;
import com.certuit.base.service.base.PermisosService;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;

@RestController
@RequestMapping(value = "/api")
public class ClienteRest {

    @Autowired
    DBConection dbConection;
    @Autowired
    PermisosService permisosService;

    @GetMapping("/Clientes/GetListado")
    public ResponseEntity<?> getClientesListado(@RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = """
                SELECT 
                    cl.IdCliente AS m_nIdCliente,
                    cl.NumeroCliente AS m_nNumeroCliente,
                    cl.TipoCliente AS m_nTipoCliente,
                    cl.RFC AS m_sRFC,
                    cl.NombreFiscal AS m_sNombreFiscal,
                    cl.NombreCorto AS m_sNombreCorto,
                    cl.Activo AS m_bActivo,
                    cl.CodigoPostal AS m_nIdCP,
                    cl.CodigoPostal AS m_sCodigoPostal,
                    cl.IdEstado AS m_nIdEstado,
                    cl.Municipio AS m_sMunicipio,
                    cl.Localidad AS m_sLocalidad,
                    cl.Colonia AS m_sColonia,
                    cl.Calle AS m_sCalle,
                    cl.NoExterior AS m_sNoExterior,
                    cl.NoInterior AS m_sNoInterior,
                    cl.Telefono AS m_sTelefono,
                    cl.Celular AS m_sCelular,
                    cl.Celular AS m_sTelefonoCelular,
                    cl.Nextel AS m_sNextel,
                    cl.CorreoElectronico AS m_sCorreoElectronico,
                    CASE 
                        WHEN cl.TieneSeguro IS NULL OR cl.TieneSeguro = 0 THEN CAST(0 AS BIT) 
                        ELSE CAST(1 AS BIT)
                    END AS m_bTieneSeguro,
                    ISNULL(cl.IdTipoSeguro, 0) AS m_nIdTipoSeguro,
                    ISNULL(cl.PorcentajeSeguro, 0) AS m_cPorcentajeSeguro,
                    cl.IdSucursal AS m_nIdSucursal,
                    cl.IdImpuestoTransladado AS m_nIdImpuestoTransladado,
                    CASE WHEN cl.SaldoCredito > 0 OR cl.DiasCredito <= 0 THEN 1 ELSE 0 END AS m_bCreditoVencido,
                    CASE WHEN cl.SaldoCredito > 0 AND cl.DiasCredito > 0 THEN 1 ELSE 0 END AS m_bCreditoDisponible,
                    CASE WHEN cl.SaldoCredito = 0 AND cl.DiasCredito = 0 THEN 1 ELSE 0 END AS m_bSinCredito,
                    cl.SaldoCredito AS m_nSaldoCliente,
                    cl.SaldoCreditoDLLS AS m_nSaldoDLLSCliente,
                    cl.DiasCredito AS m_nDiasCredito,
                    (SELECT Sucursal FROM CatSucursales s WHERE s.IdSucursal = cl.IdSucursal) AS m_sNombreSucursal,
                    (SELECT IdPais FROM CatEstados e WHERE e.IdEstado = cl.IdEstado) AS m_nIdPais
                FROM CatClientes cl
                """;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Client/GetListadoPaginado/{pagina}/{resgistros}")
    public ResponseEntity<?> getClient(@PathVariable("pagina") int pagina, @PathVariable("resgistros") int resgistros,
                                       @RequestHeader("RFC") String rfc, @RequestBody SearchRequest request)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatClientesPaginado_762 " + pagina + "," + resgistros + ",'" + request.getBusqueda() + "'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", UtilFuctions.convertArray(rs));
            query = "select COUNT(*) as total from CatClientes where Activo = 1";
            statement = jdbcConnection.createStatement();
            rs = statement.executeQuery(query);
            jsonObject.put("total", UtilFuctions.convertObject(rs).getInt("total"));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Remitentes/GetListadoPaginado/{pagina}/{resgistros}")
    public ResponseEntity<?> getRemitentesDestinatarios(@PathVariable("pagina") int pagina, @PathVariable("resgistros")
    int resgistros, @RequestHeader("RFC") String rfc, @RequestBody SearchRequest request)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatRemitentesDestinatariosPaginadoPQ " + pagina + "," + resgistros + ",'"
                    + request.getBusqueda() + "'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/Remitentes/GetListadoPaginado/{pagina}/{resgistros}/{idCliente}")
    public ResponseEntity<?> getRemitentesDestinatarios(@PathVariable("pagina") int pagina,
                                                        @PathVariable("resgistros") int resgistros,
                                                        @PathVariable("idCliente") int idCliente,
                                                        @RequestHeader("RFC") String rfc,
                                                        @RequestBody SearchRequest request)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatRemitentesDestinatariosPaginadoClientePQ " + pagina + "," + resgistros + ",'"
                    + request.getBusqueda() + "'," + idCliente;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", UtilFuctions.convertArray(rs));
            if (Objects.equals(request.getBusqueda(), "")) {
                request.setBusqueda("null");
            } else {
                request.setBusqueda("'" + request.getBusqueda() + "'");
            }
            query = "SELECT count(*) as total FROM CatRemitentesDestinatarios crd\n" +
                    "    where (" + request.getBusqueda() + " is NULL OR\n" +
                    "           (crd.Nombre LIKE '%' + UPPER(" + request.getBusqueda() + ") + '%' or crd.RFC LIKE " +
                    "'%' + UPPER(" + request.getBusqueda() + ") + '%' or\n" +
                    "            crd.Numero like '%' + " + request.getBusqueda() + " + '%')) and crd.IdCliente = "
                    + idCliente;
            statement = jdbcConnection.createStatement();
            rs = statement.executeQuery(query);
            jsonObject.put("count", UtilFuctions.convertObject(rs).getInt("total"));

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Clientes/GetById/{id}")
    public ResponseEntity<?> getClienteId(@PathVariable("id") int id, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdCliente,\n" +
                    "\tNumeroCliente,\n" +
                    "\tTipoCliente,\n" +
                    "\tRFC,\n" +
                    "\tNombreFiscal,\n" +
                    "\tNombreCorto,\n" +
                    "\tIdSucursal,\n" +
                    "\tIdMoneda,\n" +
                    "\tIdImpuestoTransladado,\n" +
                    "\tActivo,\n" +
                    "\tAplicarDetalleMaterialesEnCadaViajeEnXML as AplicarDetalleMaterialesCadaViajeXML,\n" +
                    "\tOperadorLogistico,\n" +
                    "\tCalle,\n" +
                    "\tNoExterior,\n" +
                    "\tNoInterior,\n" +
                    "\tColonia,\n" +
                    "\tLocalidad,\n" +
                    "\tMunicipio,\n" +
                    "\tIdEstado,\n" +
                    "\tCodigoPostal,\n" +
                    "\tTelefono,\n" +
                    "\tCelular,\n" +
                    "\tNextel,\n" +
                    "\tCorreoElectronico,\n" +
                    "\tMetodoPago,\n" +
                    "\tNroCuentaPago,\n" +
                    "\tCredito,\n" +
                    "\tCreditoDLLS,\n" +
                    "\tSaldoCredito,\n" +
                    "\tSaldoCreditoDLLS,\n" +
                    "\tDiasCredito,\n" +
                    "\tCreadoEl,\n" +
                    "\tCreadoPor,\n" +
                    "\tModificadoEl,\n" +
                    "\tModificadoPor,\n" +
                    "\tPendFacturar,\n" +
                    "\tPendFacturarDLLS,\n" +
                    "\tEquivalenciaCuentaContable1,\n" +
                    "\tEquivalenciaCuentaContable2,\n" +
                    "\tEquivalenciaCuentaContable3,\n" +
                    "\tEquivalenciaCuentaContable4,\n" +
                    "\tEquivalenciaCuentaContable5,\n" +
                    "\tEquivalenciaCuentaContable6,\n" +
                    "\tEquivalenciaCuentaContable7,\n" +
                    "\tEquivalenciaCuentaContable8,\n" +
                    "\tEquivalenciaCuentaContable9,\n" +
                    "\tEquivalenciaCuentaContable10,\n" +
                    "\tEquivalenciaCuentaContable11,\n" +
                    "\tEquivalenciaCuentaContable12,\n" +
                    "\tEquivalenciaCuentaContable13,\n" +
                    "\tEquivalenciaCuentaContable14,\n" +
                    "\tEquivalenciaCuentaContable15,\n" +
                    "\tIdGrupoCliente,\n" +
                    "\tPermitirParoDeMotor,\n" +
                    "\tFacturacionPorViajeDetallado,\n" +
                    "\tPermitirHistorialDeDia as PermitirHistoriaDeDia,\n" +
                    "\tIdUsoCFDI,\n" +
                    "\tNoRegistroIdentidadFiscal,\n" +
                    "\tClaveTAR,\n" +
                    "\tDistanciaDistribuidorCliente,\n" +
                    "\tLatitud,\n" +
                    "\tLongitud,\n" +
                    "\tNumeroProveedor,\n" +
                    "\tPermitirAgruparCantidadPorConcepto,\n" +
                    "\tEnvioCorreoDias,\n" +
                    "\tFechaEnvioCorreoApartir,\n" +
                    "\tInformacionRetransmisionGPS,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesActivar,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesFechaHoraInicio,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesFrecuencia,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesUltimoEnvio,\n" +
                    "\tAjustarImportes2DecimalesXML,\n" +
                    "\tBancoOrdenante,\n" +
                    "\tRFCBancoOrdenante,\n" +
                    "\tNoCuentaBancoOrdenante,\n" +
                    "\tExcluirNodoCondicionesPagoXML,\n" +
                    "\tEstatusViajeEDI,\n" +
                    "\tCompaniaSeguros1,\n" +
                    "\tTelefonosCompaniaSeguros1,\n" +
                    "\tNumeroSeguro1,\n" +
                    "\tVencimientoSeguro1,\n" +
                    "\tTipoCoberturaSeguro1,\n" +
                    "\tCompaniaSeguros,\n" +
                    "\tTelefonosCompaniaSeguros,\n" +
                    "\tNumeroSeguro,\n" +
                    "\tVencimientoSeguro,\n" +
                    "\tTipoCoberturaSeguro,\n" +
                    "\t(case when IdTipoSeguro = 1 then CAST(0 AS BIT) else CAST(1 AS BIT) end) as m_bTieneSeguro,\n" +
                    "\t(case when IdTipoSeguro is null then 0 else IdTipoSeguro end) as m_nIdTipoSeguro,\n" +
                    "\t(case when PorcentajeSeguro is null then 0 else PorcentajeSeguro end) as m_cPorcentajeSeguro\n" +
                    "\tFROM CatClientes\n" +
                    "\tWHERE IdCliente =" + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            while (rs.next()) {
                json.put("m_cPorcentajeSeguro", rs.getFloat("m_cPorcentajeSeguro"));
                json.put("m_nIdCliente", rs.getInt("IdCliente"));
                json.put("m_nNumeroCliente", rs.getInt("NumeroCliente"));
                json.put("m_nTipoCliente", rs.getInt("TipoCliente"));
                json.put("m_sRFC", rs.getString("RFC"));
                json.put("m_sNombreFiscal", rs.getString("NombreFiscal"));
                json.put("m_sNombreCorto", rs.getString("NombreCorto"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_nIdMoneda", rs.getInt("IdMoneda"));
                json.put("m_nIdImpuestoTransladado", rs.getInt("IdImpuestoTransladado"));
                json.put("m_bActivo", rs.getBoolean("Activo"));
                json.put("m_bAplicarDetalleMaterialesCadaViajeXML", rs.getBoolean("AplicarDetalleMaterialesCadaViajeXML"));
                json.put("m_bOperadorLogistico", rs.getBoolean("OperadorLogistico"));
                json.put("m_sCalle", rs.getString("Calle"));
                json.put("m_sNoExterior", rs.getString("NoExterior"));
                json.put("m_sNoInterior", rs.getString("NoInterior"));
                json.put("m_sColonia", rs.getString("Colonia"));
                json.put("m_sLocalidad", rs.getString("Localidad"));
                json.put("m_sMunicipio", rs.getString("Municipio"));
                json.put("m_nIdEstado", rs.getString("IdEstado"));
                json.put("m_sCodigoPostal", rs.getString("CodigoPostal"));
                json.put("m_sTelefono", rs.getString("Telefono"));
                json.put("m_sCelular", rs.getString("Celular"));
                json.put("m_sNextel", rs.getString("Nextel"));
                json.put("m_sCorreoElectronico", rs.getString("CorreoElectronico"));
                json.put("m_sMetodoPago", rs.getString("MetodoPago"));
                json.put("m_sNroCuentaPago", rs.getString("NroCuentaPago"));
                json.put("m_cyCredito", rs.getFloat("Credito"));
                json.put("m_cyCreditoDLLS", rs.getFloat("CreditoDLLS"));
                json.put("m_cySaldoCredito", rs.getFloat("SaldoCredito"));
                json.put("m_cySaldoCreditoDLLS", rs.getFloat("SaldoCreditoDLLS"));
                json.put("m_nDiasCredito", rs.getInt("DiasCredito"));
                json.put("m_sCreadoEl", rs.getDate("CreadoEl"));
                json.put("m_nCreadoPor", rs.getInt("CreadoPor"));
                json.put("m_sModificadoEl", rs.getDate("ModificadoEl"));
                json.put("m_nModificadoPor", rs.getInt("ModificadoPor"));
                json.put("m_cyPendFacturar", rs.getFloat("PendFacturar"));
                json.put("m_cyPendFacturarDLLS", rs.getFloat("PendFacturarDLLS"));
                //faltan equivalencia contable 1-15
                json.put("m_nIdGrupoCliente", rs.getInt("IdGrupoCliente"));
                json.put("m_bPermitirParoDeMotor", rs.getBoolean("PermitirParoDeMotor"));
                json.put("m_bFacturacionPorViajeDetallado", rs.getBoolean("FacturacionPorViajeDetallado"));
                json.put("m_bPermitirHistoriaDeDia", rs.getBoolean("PermitirHistoriaDeDia"));
                json.put("m_sIdUsoCFDI", rs.getString("IdUsoCFDI"));
                json.put("m_sNoRegistroIdentidadFiscal", rs.getString("NoRegistroIdentidadFiscal"));
                json.put("m_sClaveTAR", rs.getString("ClaveTAR"));
                json.put("m_nDistanciaDistribuidorCliente", rs.getInt("DistanciaDistribuidorCliente"));
                json.put("m_xLatitud", rs.getInt("Latitud"));
                json.put("m_xLongitud", rs.getInt("Longitud"));

                json.put("m_sNumeroProveedor", rs.getString("NumeroProveedor"));
                json.put("m_bPermitirAgruparCantidadPorConcepto",
                        rs.getBoolean("PermitirAgruparCantidadPorConcepto"));
                json.put("m_nEnvioCorreoDias", rs.getInt("EnvioCorreoDias"));
                json.put("m_sFechaEnvioCorreoApartir", rs.getDate("FechaEnvioCorreoApartir"));
                json.put("m_sInformacionRetransmisionGPS", rs.getString("InformacionRetransmisionGPS"));
                json.put("m_bEnvioAutomaticoSeguimientoViajesActivar",
                        rs.getBoolean("EnvioAutomaticoSeguimientoViajesActivar"));
                json.put("m_dtEnvioAutomaticoSeguimientoViajesFechaHoraInicio",
                        rs.getDate("EnvioAutomaticoSeguimientoViajesFechaHoraInicio"));
                json.put("m_nEnvioAutomaticoSeguimientoViajesFrecuencia",
                        rs.getInt("EnvioAutomaticoSeguimientoViajesFrecuencia"));
                json.put("m_dtEnvioAutomaticoSeguimientoViajesUltimoEnvio",
                        rs.getDate("EnvioAutomaticoSeguimientoViajesUltimoEnvio"));
                json.put("m_bAjustarImportes2DecimalesXML", rs.getBoolean("AjustarImportes2DecimalesXML"));
                json.put("m_sBancoOrdenante", rs.getString("BancoOrdenante"));

                json.put("m_sRFCBancoOrdenante", rs.getString("RFCBancoOrdenante"));
                json.put("m_sNoCuentaBancoOrdenante", rs.getString("NoCuentaBancoOrdenante"));
                json.put("m_bExcluirNodoCondicionesPagoXML", rs.getBoolean("ExcluirNodoCondicionesPagoXML"));
                json.put("m_sEstatusViajeEdi", rs.getString("EstatusViajeEDI"));
                json.put("m_sCompaniaSeguros1", rs.getString("CompaniaSeguros1"));
                json.put("m_sTelefonosCompaniaSeguros1", rs.getString("TelefonosCompaniaSeguros1"));
                json.put("m_sNumeroSeguro1", rs.getString("NumeroSeguro"));
                json.put("m_dtVencimientoSeguro1", rs.getString("VencimientoSeguro"));
                json.put("m_nTipoCoberturaSeguro1", rs.getInt("TipoCoberturaSeguro"));
                json.put("m_sCompaniaSeguros", rs.getString("CompaniaSeguros"));
                json.put("m_sTelefonosCompaniaSeguros", rs.getString("TelefonosCompaniaSeguros"));
                json.put("m_sNumeroSeguro", rs.getString("NumeroSeguro"));
                json.put("m_dtVencimientoSeguro", rs.getString("VencimientoSeguro"));
                json.put("m_nTipoCoberturaSeguro", rs.getInt("TipoCoberturaSeguro"));
                json.put("m_bTieneSeguro", rs.getBoolean("m_bTieneSeguro"));
                json.put("m_nIdTipoSeguro", rs.getInt("m_nIdTipoSeguro"));
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Clientes/GetPublicoGeneral")
    public ResponseEntity<?> getClienteGenerico(@RequestHeader("RFC") String rfc) throws
            Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT TOP 1 IdCliente,\n" +
                    "\tNumeroCliente,\n" +
                    "\tTipoCliente,\n" +
                    "\tRFC,\n" +
                    "\tNombreFiscal,\n" +
                    "\tNombreCorto,\n" +
                    "\tIdSucursal,\n" +
                    "\tIdMoneda,\n" +
                    "\tIdImpuestoTransladado,\n" +
                    "\tActivo,\n" +
                    "\tAplicarDetalleMaterialesEnCadaViajeEnXML as AplicarDetalleMaterialesCadaViajeXML,\n" +
                    "\tOperadorLogistico,\n" +
                    "\tCalle,\n" +
                    "\tNoExterior,\n" +
                    "\tNoInterior,\n" +
                    "\tColonia,\n" +
                    "\tLocalidad,\n" +
                    "\tMunicipio,\n" +
                    "\tIdEstado,\n" +
                    "\tCodigoPostal,\n" +
                    "\tTelefono,\n" +
                    "\tCelular,\n" +
                    "\tNextel,\n" +
                    "\tCorreoElectronico,\n" +
                    "\tMetodoPago,\n" +
                    "\tNroCuentaPago,\n" +
                    "\tCredito,\n" +
                    "\tCreditoDLLS,\n" +
                    "\tSaldoCredito,\n" +
                    "\tSaldoCreditoDLLS,\n" +
                    "\tDiasCredito,\n" +
                    "\tCreadoEl,\n" +
                    "\tCreadoPor,\n" +
                    "\tModificadoEl,\n" +
                    "\tModificadoPor,\n" +
                    "\tPendFacturar,\n" +
                    "\tPendFacturarDLLS,\n" +
                    "\tEquivalenciaCuentaContable1,\n" +
                    "\tEquivalenciaCuentaContable2,\n" +
                    "\tEquivalenciaCuentaContable3,\n" +
                    "\tEquivalenciaCuentaContable4,\n" +
                    "\tEquivalenciaCuentaContable5,\n" +
                    "\tEquivalenciaCuentaContable6,\n" +
                    "\tEquivalenciaCuentaContable7,\n" +
                    "\tEquivalenciaCuentaContable8,\n" +
                    "\tEquivalenciaCuentaContable9,\n" +
                    "\tEquivalenciaCuentaContable10,\n" +
                    "\tEquivalenciaCuentaContable11,\n" +
                    "\tEquivalenciaCuentaContable12,\n" +
                    "\tEquivalenciaCuentaContable13,\n" +
                    "\tEquivalenciaCuentaContable14,\n" +
                    "\tEquivalenciaCuentaContable15,\n" +
                    "\tIdGrupoCliente,\n" +
                    "\tPermitirParoDeMotor,\n" +
                    "\tFacturacionPorViajeDetallado,\n" +
                    "\tPermitirHistorialDeDia as PermitirHistoriaDeDia,\n" +
                    "\tIdUsoCFDI,\n" +
                    "\tNoRegistroIdentidadFiscal,\n" +
                    "\tClaveTAR,\n" +
                    "\tDistanciaDistribuidorCliente,\n" +
                    "\tLatitud,\n" +
                    "\tLongitud,\n" +
                    "\tNumeroProveedor,\n" +
                    "\tPermitirAgruparCantidadPorConcepto,\n" +
                    "\tEnvioCorreoDias,\n" +
                    "\tFechaEnvioCorreoApartir,\n" +
                    "\tInformacionRetransmisionGPS,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesActivar,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesFechaHoraInicio,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesFrecuencia,\n" +
                    "\tEnvioAutomaticoSeguimientoViajesUltimoEnvio,\n" +
                    "\tAjustarImportes2DecimalesXML,\n" +
                    "\tBancoOrdenante,\n" +
                    "\tRFCBancoOrdenante,\n" +
                    "\tNoCuentaBancoOrdenante,\n" +
                    "\tExcluirNodoCondicionesPagoXML,\n" +
                    "\tEstatusViajeEDI,\n" +
                    "\tCompaniaSeguros1,\n" +
                    "\tTelefonosCompaniaSeguros1,\n" +
                    "\tNumeroSeguro1,\n" +
                    "\tVencimientoSeguro1,\n" +
                    "\tTipoCoberturaSeguro1,\n" +
                    "\tCompaniaSeguros,\n" +
                    "\tTelefonosCompaniaSeguros,\n" +
                    "\tNumeroSeguro,\n" +
                    "\tVencimientoSeguro,\n" +
                    "\tTipoCoberturaSeguro,\n" +
                    "\tTieneSeguro,\n" +
                    "\tIdTipoSeguro,\n" +
                    "\tPorcentajeSeguro\n" +
                    "\tFROM CatClientes\n" +
                    "\tWHERE NombreFiscal LIKE '%PUBLICO EN GENERAL%'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            while (rs.next()) {
                json.put("m_cPorcentajeSeguro", rs.getInt("PorcentajeSeguro"));
                json.put("m_nIdCliente", rs.getInt("IdCliente"));
                json.put("m_nNumeroCliente", rs.getInt("NumeroCliente"));
                json.put("m_nTipoCliente", rs.getInt("TipoCliente"));
                json.put("m_sRFC", rs.getString("RFC"));
                json.put("m_sNombreFiscal", rs.getString("NombreFiscal"));
                json.put("m_sNombreCorto", rs.getString("NombreCorto"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_nIdMoneda", rs.getInt("IdMoneda"));
                json.put("m_nIdImpuestoTransladado", rs.getInt("IdImpuestoTransladado"));
                json.put("m_bActivo", rs.getBoolean("Activo"));
                json.put("m_bAplicarDetalleMaterialesCadaViajeXML",
                        rs.getBoolean("AplicarDetalleMaterialesCadaViajeXML"));
                json.put("m_bOperadorLogistico", rs.getBoolean("OperadorLogistico"));
                json.put("m_sCalle", rs.getString("Calle"));
                json.put("m_sNoExterior", rs.getString("NoExterior"));
                json.put("m_sNoInterior", rs.getString("NoInterior"));
                json.put("m_sColonia", rs.getString("Colonia"));
                json.put("m_sLocalidad", rs.getString("Localidad"));
                json.put("m_sMunicipio", rs.getString("Municipio"));
                json.put("m_nIdEstado", rs.getString("IdEstado"));
                json.put("m_sCodigoPostal", rs.getString("CodigoPostal"));
                json.put("m_sTelefono", rs.getString("Telefono"));
                json.put("m_sCelular", rs.getString("Celular"));
                json.put("m_sNextel", rs.getString("Nextel"));
                json.put("m_sCorreoElectronico", rs.getString("CorreoElectronico"));
                json.put("m_sMetodoPago", rs.getString("MetodoPago"));
                json.put("m_sNroCuentaPago", rs.getString("NroCuentaPago"));
                json.put("m_cyCredito", rs.getFloat("Credito"));
                json.put("m_cyCreditoDLLS", rs.getFloat("CreditoDLLS"));
                json.put("m_cySaldoCredito", rs.getFloat("SaldoCredito"));
                json.put("m_cySaldoCreditoDLLS", rs.getFloat("SaldoCreditoDLLS"));
                json.put("m_nDiasCredito", rs.getInt("DiasCredito"));
                json.put("m_sCreadoEl", rs.getDate("CreadoEl"));
                json.put("m_nCreadoPor", rs.getInt("CreadoPor"));
                json.put("m_sModificadoEl", rs.getDate("ModificadoEl"));
                json.put("m_nModificadoPor", rs.getInt("ModificadoPor"));
                json.put("m_cyPendFacturar", rs.getFloat("PendFacturar"));
                json.put("m_cyPendFacturarDLLS", rs.getFloat("PendFacturarDLLS"));
                //faltan equivalencia contable 1-15
                json.put("m_nIdGrupoCliente", rs.getInt("IdGrupoCliente"));
                json.put("m_bPermitirParoDeMotor", rs.getBoolean("PermitirParoDeMotor"));
                json.put("m_bFacturacionPorViajeDetallado", rs.getBoolean("FacturacionPorViajeDetallado"));
                json.put("m_bPermitirHistoriaDeDia", rs.getBoolean("PermitirHistoriaDeDia"));
                json.put("m_sIdUsoCFDI", rs.getString("IdUsoCFDI"));
                json.put("m_sNoRegistroIdentidadFiscal", rs.getString("NoRegistroIdentidadFiscal"));
                json.put("m_sClaveTAR", rs.getString("ClaveTAR"));
                json.put("m_nDistanciaDistribuidorCliente", rs.getInt("DistanciaDistribuidorCliente"));
                json.put("m_xLatitud", rs.getInt("Latitud"));
                json.put("m_xLongitud", rs.getInt("Longitud"));

                json.put("m_sNumeroProveedor", rs.getString("NumeroProveedor"));
                json.put("m_bPermitirAgruparCantidadPorConcepto",
                        rs.getBoolean("PermitirAgruparCantidadPorConcepto"));
                json.put("m_nEnvioCorreoDias", rs.getInt("EnvioCorreoDias"));
                json.put("m_sFechaEnvioCorreoApartir", rs.getDate("FechaEnvioCorreoApartir"));
                json.put("m_sInformacionRetransmisionGPS", rs.getString("InformacionRetransmisionGPS"));
                json.put("m_bEnvioAutomaticoSeguimientoViajesActivar",
                        rs.getBoolean("EnvioAutomaticoSeguimientoViajesActivar"));
                json.put("m_dtEnvioAutomaticoSeguimientoViajesFechaHoraInicio",
                        rs.getDate("EnvioAutomaticoSeguimientoViajesFechaHoraInicio"));
                json.put("m_nEnvioAutomaticoSeguimientoViajesFrecuencia",
                        rs.getInt("EnvioAutomaticoSeguimientoViajesFrecuencia"));
                json.put("m_dtEnvioAutomaticoSeguimientoViajesUltimoEnvio",
                        rs.getDate("EnvioAutomaticoSeguimientoViajesUltimoEnvio"));
                json.put("m_bAjustarImportes2DecimalesXML", rs.getBoolean("AjustarImportes2DecimalesXML"));
                json.put("m_sBancoOrdenante", rs.getString("BancoOrdenante"));

                json.put("m_sRFCBancoOrdenante", rs.getString("RFCBancoOrdenante"));
                json.put("m_sNoCuentaBancoOrdenante", rs.getString("NoCuentaBancoOrdenante"));
                json.put("m_bExcluirNodoCondicionesPagoXML", rs.getBoolean("ExcluirNodoCondicionesPagoXML"));
                json.put("m_sEstatusViajeEdi", rs.getString("EstatusViajeEDI"));
                json.put("m_sCompaniaSeguros1", rs.getString("CompaniaSeguros1"));
                json.put("m_sTelefonosCompaniaSeguros1", rs.getString("TelefonosCompaniaSeguros1"));
                json.put("m_sNumeroSeguro1", rs.getString("NumeroSeguro"));
                json.put("m_dtVencimientoSeguro1", rs.getString("VencimientoSeguro"));
                json.put("m_nTipoCoberturaSeguro1", rs.getInt("TipoCoberturaSeguro"));
                json.put("m_sCompaniaSeguros", rs.getString("CompaniaSeguros"));
                json.put("m_sTelefonosCompaniaSeguros", rs.getString("TelefonosCompaniaSeguros"));
                json.put("m_sNumeroSeguro", rs.getString("NumeroSeguro"));
                json.put("m_dtVencimientoSeguro", rs.getString("VencimientoSeguro"));
                json.put("m_nTipoCoberturaSeguro", rs.getInt("TipoCoberturaSeguro"));
                json.put("m_bTieneSeguro", rs.getBoolean("TieneSeguro"));
                json.put("m_nIdTipoSeguro", rs.getInt("IdTipoSeguro"));
            }

            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/GetLogo")
    public ResponseEntity<?> getLogo(@RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "SELECT Logo from SisParametros";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject json = new JSONObject();
                while (rs.next()) {
                    json.put("Logo", rs.getString(1));

                }

                return ResponseEntity.ok(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500)
                        .body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception x) {
            JSONObject json = new JSONObject();
            json.put("Logo", "");
            return ResponseEntity.ok(json.toString());

        }
    }

    public static String getHash2(String input, int limit){
        int key = 1;
        for (int position = 0; position < input.length(); position++) {
            int ascValue = input.charAt(position);
            key = key * ascValue;
            key = Math.abs(key ^ 0xAAAAAAAA);
            key = floorMod(key, limit); // Using floorMod for consistent negative number handling
        }
        // Safely convert to string with bounds checking
        if (key < 0 || key > Integer.MAX_VALUE) {
            throw new ArithmeticException("Hash value out of bounds");
        }
        return String.valueOf(key);
    }

    private String getMasterPasswor(){
        long nNumeroHash;
        String sFecha = new SimpleDateFormat("YYYYMMDD").format(new Date());
        nNumeroHash = Integer.parseInt(getHash2(("ENTROPIA7" + sFecha.substring(0,6)),99999999));
        String sCadena1 = String.format("%08d", abs(nNumeroHash));//NumToString(nNumeroHash,"08d");

        nNumeroHash = Integer.parseInt(getHash2(("ENTROPIA8"+ sFecha.substring(0,6)),99999999));
        String sCadena2 = String.format("%08d", abs(nNumeroHash));

        nNumeroHash = Integer.parseInt(getHash2(("ENTROPIA9"+ sFecha.substring(0,6)),99999999));
        StringBuilder sCadenaFinal = getStringBuilder(nNumeroHash, sCadena1, sCadena2);

        return sCadenaFinal.toString();
    }

    @NotNull
    private static StringBuilder getStringBuilder(long nNumeroHash, String sCadena1, String sCadena2) {
        String sCadena3 = String.format("%08d", abs(nNumeroHash));

        StringBuilder sCadenaFinal = new StringBuilder("MKM");
        String sCadenaCombinada = sCadena1 + sCadena2 +sCadena3;
        int nLongitud = sCadenaCombinada.length();
        int nPosicion;
        int nExtraer;
        String sValorReferencia ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%*()_-+=:;?.,/<>||\"'&{}[]1234567890";
        for(nPosicion = 0; nPosicion < nLongitud; nPosicion += 2){
            String extract = sCadenaCombinada.substring(nPosicion, nPosicion+2);
            nExtraer = Integer.parseInt(extract);
            if(nExtraer == 0){
                nExtraer = 1;
            }

            sCadenaFinal.append(sValorReferencia.charAt(nExtraer - 1));
        }
        return sCadenaFinal;
    }

    private String cargarClaveEncriptacion() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("/opt/apache-tomcat-8.5.40/webapps/rfc.json"));
        return jsonNode.get("encriptacion").asText();
    }
    @PostMapping("/ValidarLogin/V2/{Usuario}/{Password}")
    public ResponseEntity<?> validarLoginV2(@RequestBody LoginRequest request, @RequestHeader("RFC") String rfc) {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {

            String queryTipo = "SELECT DATA_TYPE AS Tipo FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'CatUsuarios' AND COLUMN_NAME = 'Contrasena'";
            String tipoContrasena;

            try (Statement statement = jdbcConnection.createStatement();
                 ResultSet rsTipo = statement.executeQuery(queryTipo)) {

                if (!rsTipo.next()) {
                    return ResponseEntity.status(500).body("No se pudo determinar el tipo de la columna Contrasena.");
                }
                tipoContrasena = rsTipo.getString("Tipo");
            }

            StringBuilder query = new StringBuilder();
            query.append("SELECT u.IdUsuario, u.Usuario, u.Nombre, u.Nombre AS APaterno, u.Nombre AS AMaterno, ")
                    .append("u.TipoUsuario, u.CorreoElectronico, u.IdSucursal, u.Activo, u.FiltrarAcceso, ")
                    .append("FiltrarAccesoPorLaIP as FiltrarAccesoPorIP, u.FiltrarAccesoPorHora, ")
                    .append("u.Hora0, u.Hora1, u.Hora2, u.Hora3, u.Hora4, u.Hora5, u.Hora6, u.Hora7, u.Hora8, u.Hora9, ")
                    .append("u.Hora10, u.Hora11, u.Hora12, u.Hora13, u.Hora14, u.Hora15, u.Hora16, u.Hora17, ")
                    .append("u.Hora18, u.Hora19, u.Hora20, u.Hora21, u.Hora22, u.Hora23, ")
                    .append("u.Lunes, u.Martes, u.Miercoles, u.Jueves, u.Viernes, u.Sabado, u.Domingo, p.RFC, ")
                    .append("(SELECT Sucursal FROM CatSucursales s WHERE s.IdSucursal = u.IdSucursal) AS Sucursal ")
                    .append("FROM CatUsuarios u, SisParametros p WHERE u.Usuario = '")
                    .append(request.getEmail().replace("'", "''")).append("'");

            if ("GM".equals(request.getEmail())) {
                String master = getMasterPasswor();
                query.append(" AND ('").append(request.getPassword().replace("'", "''")).append("' = '")
                        .append(master.replace("'", "''")).append("')");
            } else {
                if ("varchar".equalsIgnoreCase(tipoContrasena)) {
                    query.append(" AND u.Contrasena = '").append(request.getPassword().replace("'", "''")).append("'");
                } else {
                    String encriptacion = cargarClaveEncriptacion();
                    query.append(" AND CONVERT(VARCHAR(100), DECRYPTBYPASSPHRASE('")
                            .append(encriptacion.replace("'", "''")).append("', u.Contrasena)) = '")
                            .append(request.getPassword().replace("'", "''")).append("'");
                }
            }

            try (Statement statement = jdbcConnection.createStatement();
                 ResultSet rs = statement.executeQuery(query.toString())) {

                if (!rs.isBeforeFirst()) {
                    return ResponseEntity.status(201).body("No se encontró el usuario o la contraseña es incorrecta.");
                }

                JSONObject json = new JSONObject();

                while (rs.next()) {
                    boolean activo = rs.getBoolean("Activo");
                    if (!activo) {
                        return ResponseEntity.status(201).body("El usuario no se encuentra activo");
                    }

                    String filtrarAccesoIp = rs.getString("FiltrarAccesoPorIP");
                    boolean filtrarAcceso = rs.getBoolean("FiltrarAcceso");
                    if (filtrarAcceso && (filtrarAccesoIp == null || filtrarAccesoIp.trim().isEmpty())) {
                        return ResponseEntity.status(201).body("La IP por la que se está accesando no está permitida");
                    }

                    boolean filtrarAccesoHora = rs.getBoolean("FiltrarAccesoPorHora");
                    int idUsuario = rs.getInt("IdUsuario");

                    json.put("m_nIdUsuario", idUsuario);
                    json.put("m_sUsuario", rs.getString("Usuario"));
                    json.put("m_sNombre", rs.getString("Nombre"));
                    json.put("m_sAPaterno", rs.getString("APaterno"));
                    json.put("m_sAMaterno", rs.getString("AMaterno"));
                    json.put("m_nTipoUsuario", rs.getInt("TipoUsuario"));
                    json.put("m_sCorreoElectronico", rs.getString("CorreoElectronico"));
                    json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                    json.put("m_sSucursal", rs.getString("Sucursal"));
                    json.put("m_bActivo", activo);
                    json.put("m_bFiltrarAcceso", filtrarAcceso);
                    json.put("m_sFiltrarAccesoIP", filtrarAccesoIp);
                    json.put("m_bFiltrarAccesoHora", filtrarAccesoHora);

                    for (int i = 0; i < 24; i++) {
                        json.put("m_bHora" + i, rs.getBoolean("Hora" + i));
                    }

                    json.put("m_bLunes", rs.getBoolean("Lunes"));
                    json.put("m_bMartes", rs.getBoolean("Martes"));
                    json.put("m_bMiercoles", rs.getBoolean("Miercoles"));
                    json.put("m_bJueves", rs.getBoolean("Jueves"));
                    json.put("m_bViernes", rs.getBoolean("Viernes"));
                    json.put("m_bSabado", rs.getBoolean("Sabado"));
                    json.put("m_bDomingo", rs.getBoolean("Domingo"));
                    json.put("m_arrayPermisos", permisosService.getPermisosUsuario(idUsuario, jdbcConnection));
                }

                return ResponseEntity.ok(json.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información.");
        }
    }


    @GetMapping("/ValidarLogin/{Usuario}/{Password}")
    public ResponseEntity<?> validarLogin(@PathVariable("Usuario") String Usuario,
                                          @PathVariable("Password") String Password,
                                          @RequestHeader("RFC") String rfc) throws
            Exception
    {
        Connection jdbcConnection = null;
        try  {
            jdbcConnection = dbConection.getconnection(rfc);
            String query = "SELECT DATA_TYPE AS Tipo FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'CatUsuarios' AND COLUMN_NAME = 'Contrasena'";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
                query = "SELECT\n" +
                    "\tu.IdUsuario,\n" +
                    "\tu.Usuario,\n" +
                    "\tu.Nombre,\n" +
                    "\tNombre as APaterno,\n" +
                    "\tNombre as AMaterno,\n" +
                    "\tu.TipoUsuario,\n" +
                    "\tu.CorreoElectronico,\n" +
                    "\tu.IdSucursal,\n" +
                    "\tu.Activo,\n" +
                    "\tu.FiltrarAcceso,\n" +
                    "\tFiltrarAccesoPorLaIP as FiltrarAccesoPorIP,\n" +
                    "\tu.FiltrarAccesoPorHora,\n" +
                    "\tu.Hora0,\n" +
                    "\tu.Hora1,\n" +
                    "\tu.Hora2,\n" +
                    "\tu.Hora3,\n" +
                    "\tu.Hora4,\n" +
                    "\tu.Hora5,\n" +
                    "\tu.Hora6,\n" +
                    "\tu.Hora7,\n" +
                    "\tu.Hora8,\n" +
                    "\tu.Hora9,\n" +
                    "\tu.Hora10,\n" +
                    "\tu.Hora11,\n" +
                    "\tu.Hora12,\n" +
                    "\tu.Hora13,\n" +
                    "\tu.Hora14,\n" +
                    "\tu.Hora15,\n" +
                    "\tu.Hora16,\n" +
                    "\tu.Hora17,\n" +
                    "\tu.Hora18,\n" +
                    "\tu.Hora19,\n" +
                    "\tu.Hora20,\n" +
                    "\tu.Hora21,\n" +
                    "\tu.Hora22,\n" +
                    "\tu.Hora23,\n" +
                    "\tu.Lunes,\n" +
                    "\tu.Martes,\n" +
                    "\tu.Miercoles,\n" +
                    "\tu.Jueves,\n" +
                    "\tu.Viernes,\n" +
                    "\tu.Sabado,\n" +
                    "\tu.Domingo,p.RFC\n," +
                    "(select Sucursal from CatSucursales s where s.IdSucursal = u.IdSucursal) as Sucursal \n" +
                    "\tFROM CatUsuarios u,SisParametros p\n" +
                    "\tWHERE u.Usuario = " + Usuario;
            rs.next();
            if(rs.getString("Tipo").equals("varchar")){
                query = query +  " AND u.Contrasena = " + Password;
            }else{
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(new File("/opt/apache-tomcat-8.5.40/webapps/rfc.json"));
                String encriptacion = jsonNode.get("encriptacion").asText();
                query = query + " AND CONVERT(VARCHAR(100),DECRYPTBYPASSPHRASE('"+ encriptacion + "',u.Contrasena)) = " + Password;
                //"(SELECT CONVERT(VARBINARY(max)," + Password + ",1))";
            }
            rs = statement.executeQuery(query);
            JSONObject json = new JSONObject();
            Boolean activo = true;
            Boolean filtrarAcceso = true;
            String filtrarAccesoIp = "";
            Boolean filtrarAccesoHora = true;
            int idUsuario;
            if (!rs.isBeforeFirst()){
                return ResponseEntity.status(201).body("No se encontró el usuario o la contraseña es incorrecta.");
            }
            while (rs.next()) {
                activo = rs.getBoolean("Activo");
                if (!activo) {
                    return ResponseEntity.status(201).body("El usuario no se encuentra activo");
                }
                filtrarAcceso = rs.getBoolean("FiltrarAcceso");
                if (filtrarAcceso) {
                    if (filtrarAccesoIp.equalsIgnoreCase("")) {
                        return ResponseEntity.status(201).body("La IP por la que se esta accesando no esta permitida");
                    }
                }
                filtrarAccesoIp = rs.getString("FiltrarAccesoPorIP");
                filtrarAccesoHora = rs.getBoolean("FiltrarAccesoPorHora");

                idUsuario = rs.getInt("IdUsuario");
                json.put("m_nIdUsuario", idUsuario);
                json.put("m_sUsuario", rs.getString("Usuario"));
                json.put("m_sNombre", rs.getString("Nombre"));
                json.put("m_sAPaterno", rs.getString("APaterno"));
                json.put("m_sAMaterno", rs.getString("AMaterno"));
                json.put("m_nTipoUsuario", rs.getInt("TipoUsuario"));
                json.put("m_sCorreoElectronico", rs.getString("CorreoElectronico"));
                json.put("m_nIdSucursal", rs.getInt("IdSucursal"));
                json.put("m_sSucursal", rs.getString("Sucursal"));
                json.put("m_bActivo", activo);
                json.put("m_bFiltrarAcceso", filtrarAcceso);
                json.put("m_sFiltrarAccesoIP", filtrarAccesoIp);
                json.put("m_bFiltrarAccesoHora", filtrarAccesoHora);
                json.put("m_bHora0", rs.getBoolean("Hora0"));
                json.put("m_bHora1", rs.getBoolean("Hora1"));
                json.put("m_bHora2", rs.getBoolean("Hora2"));
                json.put("m_bHora3", rs.getBoolean("Hora3"));
                json.put("m_bHora4", rs.getBoolean("Hora4"));
                json.put("m_bHora5", rs.getBoolean("Hora5"));
                json.put("m_bHora6", rs.getBoolean("Hora6"));
                json.put("m_bHora7", rs.getBoolean("Hora7"));
                json.put("m_bHora8", rs.getBoolean("Hora8"));
                json.put("m_bHora9", rs.getBoolean("Hora9"));
                json.put("m_bHora10", rs.getBoolean("Hora10"));
                json.put("m_bHora11", rs.getBoolean("Hora11"));
                json.put("m_bHora12", rs.getBoolean("Hora12"));
                json.put("m_bHora13", rs.getBoolean("Hora13"));
                json.put("m_bHora14", rs.getBoolean("Hora14"));
                json.put("m_bHora15", rs.getBoolean("Hora15"));
                json.put("m_bHora16", rs.getBoolean("Hora16"));
                json.put("m_bHora17", rs.getBoolean("Hora17"));
                json.put("m_bHora18", rs.getBoolean("Hora18"));
                json.put("m_bHora19", rs.getBoolean("Hora19"));
                json.put("m_bHora20", rs.getBoolean("Hora20"));
                json.put("m_bHora21", rs.getBoolean("Hora21"));
                json.put("m_bHora22", rs.getBoolean("Hora22"));
                json.put("m_bHora23", rs.getBoolean("Hora23"));
                json.put("m_bLunes", rs.getBoolean("Lunes"));
                json.put("m_bMartes", rs.getBoolean("Martes"));
                json.put("m_bMiercoles", rs.getBoolean("Miercoles"));
                json.put("m_bJueves", rs.getBoolean("Jueves"));
                json.put("m_bViernes", rs.getBoolean("Viernes"));
                json.put("m_bSabado", rs.getBoolean("Sabado"));
                json.put("m_bDomingo", rs.getBoolean("Domingo"));
                json.put("m_arrayPermisos", permisosService.getPermisosUsuario(idUsuario, jdbcConnection));
            }
            return ResponseEntity.ok(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información.");
        } finally {
            if (jdbcConnection != null) {
                try{
                    jdbcConnection.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/Clientes/ValidarTieneConvenio/{idCliente}/{idTipoTarifa}")
    public ResponseEntity<?> getIfHaveConvenio(@PathVariable("idCliente") int idCliente,
                                               @PathVariable("idTipoTarifa") int idTipoTarifa,
                                               @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "";
            if (idTipoTarifa == 2) {
                query = "select (IIF(COUNT(IdTarifa) > 0, CAST(1 as BIT), CAST(0 as BIT))) as value\n" +
                        "from CatTarifasRangosPQ\n" +
                        "where IdCliente = " + idCliente + " " +
                        "and Activo = 1";
            } else {
                query = "Select CAST(0 as bit) as value";
            }
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String jsonObject = UtilFuctions.convertObject(rs).toString();

            return ResponseEntity.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Clientes/ModificarSeguro")
    public ResponseEntity<?> modificarSeguro(@RequestBody SeguroRequest
                                                     request, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "UPDATE CatClientes " +
                    "SET IdTipoSeguro =" + request.getIdTipoSeguro() + "," +
                    "SeguroObligatorio =" + (request.isAplicaSeguro() ? 1 : 0) + "," +
                    "PorcentajeSeguro =" + request.getPorcentajeSeguro() + ", " +
                    "Aseguradora ='" + request.getAseguradora() + "', " +
                    "Poliza ='" + request.getPoliza() + "' " +
                    "WHERE IdCliente = " + request.getIdCliente() + " ";

            Statement statement = jdbcConnection.createStatement();
            int rs = statement.executeUpdate(query);

            return ResponseEntity.ok(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @GetMapping("/Clientes/ValidarLogin/{correo}/{rfc}")
    public ResponseEntity<?> validarLoginPortal(@PathVariable("correo") String correo,
                                                @PathVariable("rfc") String rfcCliente,
                                                @RequestHeader("RFC") String rfc) throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT [IdCliente] as m_nIdCliente\n" +
                    "      ,[NumeroCliente] as m_nNumeroCliente\n" +
                    "      ,[TipoCliente] as m_nTipoCliente\n" +
                    "      ,[RFC] m_sRFC\n" +
                    "      ,[NombreFiscal] as m_sNombreFiscal\n" +
                    "      ,[IdSucursal] m_nIdSucursal\n" +
                    "  FROM CatClientes\n" +
                    "  WHERE (CorreoElectronico = '" + correo + "') \n" +
                    "  AND (RFC = '" + rfcCliente + "')";

            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject obj = UtilFuctions.convertObject(rs);
            if (obj == null) {
                obj = new JSONObject();
                obj.put("Aprobado", "No");

                return ResponseEntity.ok(obj);
            }

            return ResponseEntity.ok(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

}
