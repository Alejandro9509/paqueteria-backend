package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.ParametrosConfiguracionRequest;
import com.certuit.base.domain.request.base.TipoDocumentoRequest;
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

@RestController
@RequestMapping(value = "/api")
public class ParametrosConfiguracionRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/ParametrosConfiguracion/GetListado")
    public ResponseEntity<?> consultarParametrosConfiguracion(@RequestHeader("RFC") String rfc)
            throws Exception {

        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "SELECT IdParametrosConfiguracion\n" +
                    "     , EstatusRecoleccion\n" +
                    "     , EstatusEmbarque\n" +
                    "     , MonedaEmbarque\n" +
                    "     , ISNULL((select top 1 IdTipoCambio from CatTiposCambio order by Fecha desc )," +
                    "TipoCambioEmbarque) as TipoCambioEmbarque\n" +
                    "     , EstatusGuia\n" +
                    "     , TipoTarifaTarifas\n" +
                    "     , CobroPorcentual\n" +
                    "     , esCobro\n" +
                    "     , CobroCitaTarifas\n" +
                    "     , DetectarTipoCobro\n" +
                    "     , LimpiarProducto\n" +
                    "     , TipoCobro\n" +
                    "     , TiposCobroActivos\n" +
                    "     , CorreoFacturaViaje\n" +
                    "     , CorreoFacturaUltimaMilla\n" +
                    "     , IdConceptoFlete\n" +
                    "     , IdConceptoCarga\n" +
                    "     , IdConceptoDescarga\n" +
                    "     , IdConceptoRecoleccion\n" +
                    "     , IdConceptoEntrega\n" +
                    "     , FoliosPorSucursal\n" +
                    "     , IdConceptoSeguro\n" +
                    "     , IdConceptoCita\n" +
                    "     , validarQR\n" +
                    "     , TimbradoPruebaGuia\n" +
                    "     , IdComplemento\n" +
                    "     , ValidarTimbrado\n" +
                    "     , ValidarTimbradoIngreso\n" +
                    "     , TipoTimbrado\n" +
                    "     , ModificarValorEmbarque\n" +
                    "     , FijarCapturaValorDeclarado\n" +
                    "     , FactorConversion\n" +
                    "     , CobrarConceptoCarga\n" +
                    "     , CobrarConceptoDescarga\n" +
                    "     , ImprimirEtiquetasIndividuales\n" +
                    "     , HorasLimiteEntregasUltimaMilla\n" +
                    "     , PorcentualSeguroDefecto\n" +
                    "     , CobrarConceptoManiobras\n" +
                    "     , IdConceptoManiobras\n" +
                    "FROM CatParametrosConfiguracion2PQ";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            try {
                query = "select sucursal.IdSucursal as idSucursal, sucursal.Sucursal as sucursal," +
                        "ISNULL(parametro.IdTipoDocumento, 0) as idTipoDocumento, ISNULL(CTD.Documento + '-' + " +
                        "ISNULL(CTDVT.Serie,'') + '('+ (case when CTDVT.Complemento = 1 " +
                        "then 'Traslado' when CTDVT.Complemento = 2 " +
                        "then 'Ingreso' ELSE 'Ninguno' end) + ')','SIN DEFINIR') as documento " +
                        "from CatSucursales sucursal " +
                        "left join CatParametrosConfiguracionDocumentosTimbradoPQ parametro " +
                        "on sucursal.IdSucursal = parametro.IdSucursal " +
                        "left join CatTiposDocumentosConfiguracionTimbrado CTDVT " +
                        "on CTDVT.IdTipoDocumentoConfiguracionTimbrado = parametro.IdTipoDocumento " +
                        "left join CatTiposDocumentos CTD on CTDVT.IdTipoDocumento = CTD.IdTipoDocumento";
                rs = statement.executeQuery(query);
                JSONArray jsonArrayDocumentos = UtilFuctions.convertArray(rs);
                jsonObject.put("documentos", jsonArrayDocumentos);
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.ok(jsonObject.toString());
            }

            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PutMapping("/ParametrosConfiguracion/Modificar")
    public ResponseEntity<?> modificarParametrosConfiguracion(@RequestBody ParametrosConfiguracionRequest request,
                                                              @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "EXEC usp_CatParametrosConfiguracionModificar_1284 1, " + request.getEstatusRecoleccion()
                        + ", " + request.getEstatusEmbarque() + ", " + request.getMonedaEmbarque() + ",\n"
                        + "    " + request.getTipoCambioEmbarque() + ", " + request.getEstatusGuia() + ", "
                        + request.getTipoTarifaTarifas() + ", " + request.getCostoCitaTarifas() + ", "
                        + (request.isCobrarCita() ? 1 : 0) + ",\n" + "    " + (request.isDetectarTipoCobro() ? 1 : 0)
                        + ", " + (request.isLimpiarProducto() ? 1 : 0) + ", " + request.getTipoCobro() + ", '"
                        + request.getTiposCobroActivos() + "', '" + request.getCorreoFacturacionViaje() + "',\n"
                        + "    '" + request.getCorreoFacturacionUltimaMilla() + "', " + request.getIdConceptoFlete()
                        + ", " + request.getIdConceptoCarga() + ", " + request.getIdConceptoDescarga() + ",\n" +
                        "    " + request.getIdConceptoRecoleccion() + ", " + request.getIdConceptoEntrega() + ", "
                        + request.getIdConceptoSeguro() + ", " + request.getIdConceptoCita() + ", "
                        + (request.isValidarInforme() ? 1 : 0) + "," + (request.isTimbradoPruebaGuia() ? 1 : 0) + ","
                        + request.getIdComplemento() + "," + (request.isValidarTimbrado() ? 1 : 0) + ","
                        + (request.isValidarTimbradoIngreso() ? 1 : 0) + "," + (request.getTipoTimbrado()) + ","
                        + (request.isModificarValorEmbarque() ? 1 : 0) + "," + (request.isCobrarConceptoCarga() ? 1 : 0)
                        + "," + (request.isCobrarConceptoDescarga() ? 1 : 0) + ","
                        + (request.isImprimirEtiquetasIndividuales() ? 1 : 0) + ","
                        + request.getHorasLimiteEntregasUltimaMilla() + "," + (request.getPorcentualSeguroDefecto())
                        + "," + request.isCobrarConceptoManiobras() +  "," +(request.getIdConceptoManiobras());
                statement.executeUpdate(query);
                try {
                    query = "Delete CatParametrosConfiguracionDocumentosTimbradoPQ;";
                    statement.executeUpdate(query);
                    for (TipoDocumentoRequest documento : request.getDocumentos()) {
                        if (documento.getIdTipoDocumento() == 0) {
                            continue;
                        }
                        agregarTipoDocumento(documento, rfc);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return ResponseEntity.ok("Se guardaron los cambios.Excepto los documentos por sucursal.");
                }

                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar todos los cambios");
        }
    }

    @PutMapping("/ParametrosConfiguracion/AgregarTipoDocumento")
    public ResponseEntity<?> agregarTipoDocumento(@RequestBody TipoDocumentoRequest documento,
                                                  @RequestHeader("RFC") String rfc) throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                if (documento.getIdTipoDocumento() == 0) {
                    return ResponseEntity.status(500).body("Documento inválido.");
                }
                String query = "declare @idSucursal int = " + documento.getIdSucursal() +
                        "    ,@idTipoDocumento int = " + documento.getIdTipoDocumento() +
                        "\ninsert into CatParametrosConfiguracionDocumentosTimbradoPQ(IdSucursal, IdTipoDocumento)\n" +
                        "values (@idSucursal,@idTipoDocumento)";
                statement.executeUpdate(query);

                return ResponseEntity.ok("Se guardaron los cambios.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar todos los cambios");
        }
    }

    @GetMapping("/ParametrosConfiguracion/ValidarRequiereDocumentoTimbrado/{idSucursal}")
    public ResponseEntity<?> validarRequiereDocumentoTimbrado(@PathVariable("idSucursal") int idSucursal,
                                                              @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "";
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            JSONObject jsonObject = new JSONObject();
            try {
                query = "exec usp_CatParametrosConfiguracionValidarDocumentoSucursalPQ " + idSucursal;
                rs = statement.executeQuery(query);
                jsonObject = UtilFuctions.convertObject(rs);

                return ResponseEntity.ok(jsonObject.toString());
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un error al válidar los documentos de la sucursal");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
