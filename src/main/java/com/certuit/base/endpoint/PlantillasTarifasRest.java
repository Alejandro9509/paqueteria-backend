package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.PlantillaRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static com.certuit.base.util.UtilFuctions.*;

@RestController
@RequestMapping(value = "/api")
public class PlantillasTarifasRest {
    @Autowired
    DBConection dbConection;

    @GetMapping("/PlantillasImportacionTarifas/GetListado")
    public ResponseEntity<?> consultarPlantillas(@RequestHeader("RFC") String rfc) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "Select idPlantilla=IdPlantilla" +
                        "\n, archivoNombre=ArchivoNombre" +
                        "\n, idCliente=plantilla.IdCliente" +
                        "\n, nombreCliente=cliente.NombreFiscal" +
                        "\n, nombreCliente=cliente.NombreFiscal" +
                        "\n, idTipoPlantilla=plantilla.IdTipoPlantilla" +
                        "\n, tipoPlantilla=tipoPlantilla.Descripcion" +
                        "\nfrom CatPlantillasImportacionPQ plantilla" +
                        "\ninner Join CatClientes cliente on cliente.IdCliente = plantilla.IdCliente" +
                        "\ninner Join CatTiposPlantillaImportacionPQ tipoPlantilla on tipoPlantilla.IdTipoPlantillaImportacion = plantilla.IdTipoPlantilla";
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                jsonResponse.put("data", UtilFuctions.convertArray(rs));
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al consultar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al consultar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }
    }

    @GetMapping("/PlantillasImportacionTarifas/GetById/{id}")
    public ResponseEntity<?> consultarPlantillaPorId(@RequestHeader("RFC") String rfc, @PathVariable("id") int id) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "Select idPlantilla=IdPlantilla \n" +
                        ", archivoBase64=ArchivoBase64 \n" +
                        ", archivoNombre=ArchivoNombre \n" +
                        ", hojaEmbarques=HojaEmbarques \n" +
                        ", hojaPaquetes=HojaPaquetes \n" +
                        ", hojaComplementos=HojaComplementos \n" +
                        ", numeroEmbarque=NumeroEmbarque \n" +
                        ", moneda=Moneda \n" +
                        ", tipoCambio=TipoCambio \n" +
                        ", tipoCobro=TipoCobro \n" +
                        ", idCliente=IdCliente \n" +
                        ", tipoSeguro=TipoSeguro \n" +
                        ", porcentajeSeguro=PorcentajeSeguro \n" +
                        ", valorDeclarado=ValorDeclarado \n" +
                        ", observacionesEmbarque=ObservacionesEmbarque \n" +
                        ", validarTimbradoFactura=ValidarTimbradoFactura \n" +
                        ", tipoServicio=TipoServicio \n" +
                        ", numeroRemitente=NumeroRemitente \n" +
                        ", correoRemitente=CorreoRemitente \n" +
                        ", telefonoRemitente=TelefonoRemitente \n" +
                        ", contactoRemitente=ContactoRemitente \n" +
                        ", numeroDestinatario=NumeroDestinatario \n" +
                        ", correoDestinatario=CorreoDestinatario \n" +
                        ", telefonoDestinatario=TelefonoDestinatario \n" +
                        ", contactoDestinatario=ContactoDestinatario \n" +
                        ", entregaEnSucursal=EntregaEnSucursal \n" +
                        ", sucursalEntrega=SucursalEntrega \n" +
                        ", entregaDiferenteDomicilio=EntregaDiferenteDomicilio \n" +
                        ", codigoPostalDiferenteDomicilio=CodigoPostalDiferenteDomicilio \n" +
                        ", coloniaDiferenteDomicilio=ColoniaDiferenteDomicilio \n" +
                        ", calleNumeroDiferenteDomicilio=CalleNumeroDiferenteDomicilio \n" +
                        ", entregarEn=EntregarEn \n" +
                        ", datosAdicionales=DatosAdicionales \n" +
                        ", recoleccionDiferenteDomicilio=RecoleccionDiferenteDomicilio \n" +
                        ", codigoPostalDiferenteDomicilioRecoleccion=CodigoPostalDiferenteDomicilioRecoleccion \n" +
                        ", coloniaDiferenteDomicilioRecoleccion=ColoniaDiferenteDomicilioRecoleccion \n" +
                        ", calleNumeroDiferenteDomicilioRecoleccion=CalleNumeroDiferenteDomicilioRecoleccion \n" +
                        ", recogerEn=RecogerEn \n" +
                        ", datosAdicionalesRecoleccion=DatosAdicionalesRecoleccion \n" +
                        ", latitud=Latitud \n" +
                        ", longitud=Longitud \n" +
                        ", conCita=ConCita \n" +
                        ", citaPendiente=CitaPendiente \n" +
                        ", fechaCita=FechaCita \n" +
                        ", horaMinimaCita=HoraMinimaCita \n" +
                        ", horaMaximaCita=HoraMaximaCita \n" +
                        ", cantidadPaquete=CantidadPaquete \n" +
                        ", numeroProducto=NumeroProducto \n" +
                        ", embalajePaquete=EmbalajePaquete \n" +
                        ", largo=Largo \n" +
                        ", alto=Alto \n" +
                        ", ancho=Ancho \n" +
                        ", pesoPaquete=PesoPaquete \n" +
                        ", observacionesPaquete=ObservacionesPaquete \n" +
                        ", descripcionPaquete=DescripcionPaquete \n" +
                        ", cantidadComplemento=CantidadComplemento \n" +
                        ", pesoComplemento=PesoComplemento \n" +
                        ", claveProductoServicio=ClaveProductoServicio \n" +
                        ", claveUnidadMedida=ClaveUnidadMedida \n" +
                        ", esMaterialPeligroso=EsMaterialPeligroso \n" +
                        ", claveMaterialPeligroso=ClaveMaterialPeligroso \n" +
                        ", claveEmbalaje=ClaveEmbalaje \n" +
                        ", descripcionEmbalajeComplemento=DescripcionEmbalajeComplemento \n" +
                        ", claveFraccionArancelaria=ClaveFraccionArancelaria \n" +
                        ", idTipoPlantilla=IdTipoPlantilla \n" +
                        ", usarNumeroEquivalenciaDestinatario=UsarNumeroEquivalenciaDestinatario \n" +
                        ", usarNumeroEquivalenciaResponsablePago=UsarNumeroEquivalenciaResponsablePago \n" +
                        ", responsablePago=ResponsablePago \n" +
                        ", referencia=Referencia \n" +
                        ", esFarmaco=EsFarmaco \n" +
                        ", claveSectorCofepris=ClaveSectorCofepris \n" +
                        ", nombreIngredienteActivo=NombreIngredienteActivo \n" +
                        ", nombreQuimico=NombreQuimico \n" +
                        ", denominacionGenericaProd=DenominacionGenericaProd \n" +
                        ", denominacionDistintivaProd=DenominacionDistintivaProd \n" +
                        ", fabricante=Fabricante \n" +
                        ", fechaCaducidad=FechaCaducidad \n" +
                        ", loteMedicamento=LoteMedicamento \n" +
                        ", formaFarmaceutica=FormaFarmaceutica \n" +
                        ", condicionesEspTransp=CondicionesEspTransp \n" +
                        ", registroSanitarioFolioAutorizacion=RegistroSanitarioFolioAutorizacion \n" +
                        ", numeroCAS=NumeroCAS \n" +
                        ", numRegSanPlagCOFEPRIS=NumRegSanPlagCOFEPRIS \n" +
                        ", datosFabricante=DatosFabricante \n" +
                        ", datosFormulador=DatosFormulador \n" +
                        ", datosMaquilador=DatosMaquilador \n" +
                        ", usoAutorizado=UsoAutorizado \n" +
                        "from CatPlantillasImportacionPQ where IdPlantilla = " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject queryResponse = convertObject(rs);
                if (queryResponse != null) {
                    query = "Select " +
                            " idCliente=IdCliente" +
                            "\n, numeroCliente=NumeroCliente" +
                            "\n, nombreFiscal=NombreFiscal" +
                            "\nfrom CatClientes where IdCliente = " + queryResponse.getInt("idCliente");
                    statement = jdbcConnection.createStatement();
                    rs = statement.executeQuery(query);
                    queryResponse.put("cliente", convertObject(rs));
                }
                jsonResponse.put("data", queryResponse);
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al consultar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al consultar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }
    }

    /**
     * Devuelve los nombres personalizados de las datos necesarios en plantilla para importar embarques.
     * La idea es regresar un JSON tal cual se usará en la vista
     */
    @GetMapping("/PlantillasImportacionTarifas/GetByIdCliente/{id}")
    public ResponseEntity<?> consultarCamposPlantillaPorIdCliente(@RequestHeader("RFC") String rfc,
                                                                  @PathVariable("id") int id) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "Select " +
                        "\nidTipoPlantilla=IdTipoPlantilla" +
                        "\n, hojaEmbarques=HojaEmbarques" +
                        "\n, hojaPaquetes=HojaPaquetes" +
                        "\n, hojaComplementos=HojaComplementos" +
                        "\n, numeroEmbarque=NumeroEmbarque" +
                        "\n, moneda=Moneda" +
                        "\n, tipoCambio=TipoCambio" +
                        "\n, tipoCobro=TipoCobro" +
                        "\n, idCliente=IdCliente" +
                        "\n, tipoSeguro=TipoSeguro" +
                        "\n, porcentajeSeguro=PorcentajeSeguro" +
                        "\n, valorDeclarado=ValorDeclarado" +
                        "\n, observacionesEmbarque=ObservacionesEmbarque" +
                        "\n, validarTimbradoFactura=ValidarTimbradoFactura" +
                        "\n, tipoServicio=TipoServicio" +
                        "\n, numeroRemitente=NumeroRemitente" +
                        "\n, correoRemitente=CorreoRemitente" +
                        "\n, telefonoRemitente=TelefonoRemitente" +
                        "\n, contactoRemitente=ContactoRemitente" +
                        "\n, numeroDestinatario=NumeroDestinatario" +
                        "\n, correoDestinatario=CorreoDestinatario" +
                        "\n, telefonoDestinatario=TelefonoDestinatario" +
                        "\n, contactoDestinatario=ContactoDestinatario" +
                        "\n, entregaEnSucursal=EntregaEnSucursal" +
                        "\n, sucursalEntrega=SucursalEntrega" +
                        "\n, entregaDiferenteDomicilio=EntregaDiferenteDomicilio" +
                        "\n, codigoPostalDiferenteDomicilio=CodigoPostalDiferenteDomicilio" +
                        "\n, coloniaDiferenteDomicilio=ColoniaDiferenteDomicilio" +
                        "\n, calleNumeroDiferenteDomicilio=CalleNumeroDiferenteDomicilio" +
                        "\n, entregarEn=EntregarEn" +
                        "\n, datosAdicionales=DatosAdicionales" +
                        "\n, recoleccionDiferenteDomicilio=RecoleccionDiferenteDomicilio " +
                        "\n, codigoPostalDiferenteDomicilioRecoleccion=CodigoPostalDiferenteDomicilioRecoleccion " +
                        "\n, coloniaDiferenteDomicilioRecoleccion=ColoniaDiferenteDomicilioRecoleccion" +
                        "\n, calleNumeroDiferenteDomicilioRecoleccion=CalleNumeroDiferenteDomicilioRecoleccion" +
                        "\n, recogerEn=RecogerEn" +
                        "\n, datosAdicionalesRecoleccion=DatosAdicionalesRecoleccion" +
                        "\n, latitud=Latitud" +
                        "\n, longitud=Longitud" +
                        "\n, conCita=ConCita" +
                        "\n, citaPendiente=CitaPendiente" +
                        "\n, fechaCita=FechaCita" +
                        "\n, horaMinimaCita=HoraMinimaCita" +
                        "\n, horaMaximaCita=HoraMaximaCita" +
                        "\n, idTipoPlantilla=IdTipoPlantilla" +
                        "\n, responsablePago=ResponsablePago" +
                        "\n, referencia=Referencia" +
                        "\nfrom CatPlantillasImportacionPQ where IdCliente = " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject jsonQueryResponse = UtilFuctions.convertObject(rs);
                if (jsonQueryResponse == null) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "El cliente seleccionado no ha definido plantilla de importación. Definala en el catálogo Plantillas de importacción.");
                    jsonResponse.put("data", new JSONObject());
                    

                    return ResponseEntity.status(500).body(jsonResponse.toString());
                }
                query = "Select " +
                        "\n  cantidadPaquete=CantidadPaquete" +
                        "\n, numeroProducto=NumeroProducto" +
                        "\n, embalajePaquete=EmbalajePaquete" +
                        "\n, largo=Largo" +
                        "\n, alto=Alto" +
                        "\n, ancho=Ancho" +
                        "\n, pesoPaquete=PesoPaquete" +
                        "\n, observacionesPaquete=ObservacionesPaquete" +
                        "\n, descripcionPaquete=DescripcionPaquete" +
                        "\nfrom CatPlantillasImportacionPQ where IdCliente = " + id;
                rs = statement.executeQuery(query);
                jsonQueryResponse.put("paquetes", UtilFuctions.convertObject(rs));
                query = "Select " +
                        "  cantidadComplemento=CantidadComplemento \n" +
                        ", pesoComplemento=PesoComplemento \n" +
                        ", claveProductoServicio=ClaveProductoServicio \n" +
                        ", claveUnidadMedida=ClaveUnidadMedida \n" +
                        ", esMaterialPeligroso=EsMaterialPeligroso \n" +
                        ", claveMaterialPeligroso=ClaveMaterialPeligroso \n" +
                        ", claveEmbalaje=ClaveEmbalaje \n" +
                        ", descripcionEmbalajeComplemento=DescripcionEmbalajeComplemento \n" +
                        ", claveFraccionArancelaria=ClaveFraccionArancelaria \n" +
                        "\n, esFarmaco=EsFarmaco" +
                        "\n, claveSectorCofepris=ClaveSectorCofepris" +
                        "\n, nombreIngredienteActivo=NombreIngredienteActivo" +
                        "\n, nombreQuimico=NombreQuimico" +
                        "\n, denominacionGenericaProd=DenominacionGenericaProd" +
                        "\n, denominacionDistintivaProd=DenominacionDistintivaProd" +
                        "\n, fabricante=Fabricante" +
                        "\n, fechaCaducidad=FechaCaducidad" +
                        "\n, loteMedicamento=LoteMedicamento" +
                        "\n, formaFarmaceutica=FormaFarmaceutica" +
                        "\n, condicionesEspTransp=CondicionesEspTransp" +
                        "\n, registroSanitarioFolioAutorizacion=RegistroSanitarioFolioAutorizacion" +
                        "\n, numeroCAS=NumeroCAS" +
                        "\n, numRegSanPlagCOFEPRIS=NumRegSanPlagCOFEPRIS" +
                        "\n, datosFabricante=DatosFabricante" +
                        "\n, datosFormulador=DatosFormulador" +
                        "\n, datosMaquilador=DatosMaquilador" +
                        "\n, usoAutorizado=UsoAutorizado" +
                        "\nfrom CatPlantillasImportacionPQ where IdCliente = " + id;
                rs = statement.executeQuery(query);
                jsonQueryResponse.put("complementosSat", UtilFuctions.convertObject(rs));
                jsonResponse.put("data", jsonQueryResponse);
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al consultar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al consultar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }
    }

    @PostMapping("/PlantillasImportacionTarifas/Agregar")
    public ResponseEntity<?> agregarPlantillas(@RequestHeader("RFC") String rfc, @RequestBody PlantillaRequest
            request) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                Statement statement = jdbcConnection.createStatement();
                String query = "";
                ResultSet rs;
                JSONObject jsonAux;
                query = "Select IdPlantilla from CatPlantillasImportacionPQ where IdCliente = " + request.getIdCliente();
                jsonAux = convertObject(statement.executeQuery(query));
                if (jsonAux != null) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Ya existe una plantilla asignada al cliente seleccionado.");
                    jsonResponse.put("data", new JSONObject());
                    

                    return ResponseEntity.status(500).body(jsonResponse.toString());
                }
                JSONObject estatusDatosPlantilla = validarCamposPlantilla(request);
                if (!estatusDatosPlantilla.getBoolean("success")) {
                    jsonResponse = estatusDatosPlantilla;
                    

                    return ResponseEntity.status(500).body(jsonResponse.toString());
                }
                query = "insert into CatPlantillasImportacionPQ " +
                        "(ArchivoBase64, ArchivoNombre, IdCliente,HojaEmbarques,HojaPaquetes,HojaComplementos,NumeroEmbarque" +
                        ",Moneda,TipoCambio,TipoCobro,TipoSeguro,PorcentajeSeguro,ValorDeclarado,ObservacionesEmbarque" +
                        ",ValidarTimbradoFactura,TipoServicio,NumeroRemitente,CorreoRemitente,TelefonoRemitente,ContactoRemitente" +
                        ",NumeroDestinatario,CorreoDestinatario,TelefonoDestinatario,ContactoDestinatario,EntregaEnSucursal" +
                        ",SucursalEntrega,EntregaDiferenteDomicilio,CodigoPostalDiferenteDomicilio,ColoniaDiferenteDomicilio" +
                        ",CalleNumeroDiferenteDomicilio,EntregarEn,DatosAdicionales,Latitud,Longitud,ConCita,CitaPendiente" +
                        ",FechaCita,HoraMinimaCita,HoraMaximaCita,CantidadPaquete,NumeroProducto,EmbalajePaquete,Largo,Alto" +
                        ",Ancho,PesoPaquete,ObservacionesPaquete,DescripcionPaquete,CantidadComplemento,PesoComplemento" +
                        ",ClaveProductoServicio,ClaveUnidadMedida,EsMaterialPeligroso,ClaveMaterialPeligroso,ClaveEmbalaje" +
                        ",DescripcionEmbalajeComplemento,ClaveFraccionArancelaria,RecoleccionDiferenteDomicilio" +
                        ",CodigoPostalDiferenteDomicilioRecoleccion,ColoniaDiferenteDomicilioRecoleccion" +
                        ",CalleNumeroDiferenteDomicilioRecoleccion,RecogerEn,DatosAdicionalesRecoleccion, IdTipoPlantilla" +
                        ",UsarNumeroEquivalenciaDestinatario,ResponsablePago,UsarNumeroEquivalenciaResponsablePago" +
                        ",Referencia, EsFarmaco,ClaveSectorCofepris,NombreIngredienteActivo,NombreQuimico,DenominacionGenericaProd," +
                        "    DenominacionDistintivaProd,Fabricante,FechaCaducidad,LoteMedicamento, FormaFarmaceutica,CondicionesEspTransp," +
                        "    RegistroSanitarioFolioAutorizacion,NumeroCAS,NumRegSanPlagCOFEPRIS,DatosFabricante,DatosFormulador," +
                        "    DatosMaquilador,UsoAutorizado) " +
                        "values ('" + request.getArchivoBase64() + "'"
                        + addString(request.getArchivoNombre())
                        + addInt(request.getIdCliente())
                        + addString(request.getHojaEmbarques())
                        + addString(request.getHojaPaquetes())
                        + addString(request.getHojaComplementos())
                        + addString(request.getNumeroEmbarque())
                        + addString(request.getMoneda()) + addString(request.getTipoCambio())
                        + addString(request.getTipoCobro())
                        + addString(request.getTipoSeguro())
                        + addString(request.getPorcentajeSeguro())
                        + addString(request.getValorDeclarado())
                        + addString(request.getObservacionesEmbarque())
                        + addString(request.getValidarTimbradoFactura())
                        + addString(request.getTipoServicio())
                        + addString(request.getNumeroRemitente())
                        + addString(request.getCorreoRemitente())
                        + addString(request.getTelefonoRemitente())
                        + addString(request.getContactoRemitente())
                        + addString(request.getNumeroDestinatario()) + addString(request.getCorreoDestinatario())
                        + addString(request.getTelefonoDestinatario()) + addString(request.getContactoDestinatario())
                        + addString(request.getEntregaEnSucursal()) + addString(request.getSucursalEntrega())
                        + addString(request.getEntregaDiferenteDomicilio()) + addString(request.getCodigoPostalDiferenteDomicilio())
                        + addString(request.getColoniaDiferenteDomicilio()) + addString(request.getCalleNumeroDiferenteDomicilio())
                        + addString(request.getEntregarEn()) + addString(request.getDatosAdicionales())
                        + addString(request.getLatitud()) + addString(request.getLongitud())
                        + addString(request.getConCita()) + addString(request.getCitaPendiente())
                        + addString(request.getFechaCita()) + addString(request.getHoraMinimaCita())
                        + addString(request.getHoraMaximaCita()) + addString(request.getCantidadPaquete())
                        + addString(request.getNumeroProducto()) + addString(request.getEmbalajePaquete())
                        + addString(request.getLargo()) + addString(request.getAlto()) + addString(request.getAncho())
                        + addString(request.getPesoPaquete()) + addString(request.getObservacionesPaquete())
                        + addString(request.getDescripcionPaquete()) + addString(request.getCantidadComplemento())
                        + addString(request.getPesoComplemento()) + addString(request.getClaveProductoServicio())
                        + addString(request.getClaveUnidadMedida()) + addString(request.getEsMaterialPeligroso())
                        + addString(request.getClaveMaterialPeligroso()) + addString(request.getClaveEmbalaje())
                        + addString(request.getDescripcionEmbalajeComplemento()) + addString(request.getClaveFraccionArancelaria())
                        + addString(request.getRecoleccionDiferenteDomicilio()) + addString(request.getCodigoPostalDiferenteDomicilioRecoleccion())
                        + addString(request.getColoniaDiferenteDomicilioRecoleccion()) + addString(request.getCalleNumeroDiferenteDomicilioRecoleccion())
                        + addString(request.getRecogerEn()) + addString(request.getDatosAdicionalesRecoleccion()) + addInt(request.getIdTipoPlantilla())
                        + addBoolean(request.isUsarNumeroEquivalenciaDestinatario()) + addString(request.getResponsablePago())
                        + addBoolean(request.isUsarNumeroEquivalenciaResponsablePago()) + addString(request.getReferencia())
                        + addString(request.getEsFarmaco()) + addString(request.getClaveSectorCofepris())
                        + addString(request.getNombreIngredienteActivo()) + addString(request.getNombreQuimico())
                        + addString(request.getDenominacionGenericaProd()) + addString(request.getDenominacionDistintivaProd())
                        + addString(request.getFabricante()) + addString(request.getFechaCaducidad())
                        + addString(request.getLoteMedicamento()) + addString(request.getFormaFarmaceutica())
                        + addString(request.getCondicionesEspTransp()) + addString(request.getRegistroSanitarioFolioAutorizacion())
                        + addString(request.getNumeroCAS()) + addString(request.getNumRegSanPlagCOFEPRIS())
                        + addString(request.getDatosFabricante()) + addString(request.getDatosFormulador())
                        + addString(request.getDatosMaquilador()) + addString(request.getUsoAutorizado())
                        + ");";

                statement.executeUpdate(query);
                jsonResponse.put("message", "Información guardada con éxito.");
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al guardar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al guardar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }
    }

    @PutMapping("/PlantillasImportacionTarifas/Modificar/{idPlantilla}")
    public ResponseEntity<?> ModificarPlantillas(@RequestHeader("RFC") String rfc,
                                                 @RequestBody PlantillaRequest request,
                                                 @PathVariable("idPlantilla") int idPlantilla) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                JSONObject estatusDatosPlantilla = validarCamposPlantilla(request);
                if (!estatusDatosPlantilla.getBoolean("success")) {
                    jsonResponse = estatusDatosPlantilla;
                    

                    return ResponseEntity.status(500).body(jsonResponse.toString());
                }
                String query = "update CatPlantillasImportacionPQ\n" +
                        "set "
                        + "\nIdCliente = " + request.getIdCliente()
                        + "\n,ArchivoBase64 = " + concatString(request.getArchivoBase64())
                        + "\n,ArchivoNombre = " + concatString(request.getArchivoNombre())
                        + "\n,HojaEmbarques = " + concatString(request.getHojaEmbarques())
                        + "\n,HojaPaquetes = " + concatString(request.getHojaPaquetes())
                        + "\n,HojaComplementos = " + concatString(request.getHojaComplementos())
                        + "\n,NumeroEmbarque = " + concatString(request.getNumeroEmbarque())
                        + "\n,Moneda = " + concatString(request.getMoneda())
                        + "\n,TipoCambio = " + concatString(request.getTipoCambio())
                        + "\n,TipoCobro = " + concatString(request.getTipoCobro())
                        + "\n,TipoSeguro = " + concatString(request.getTipoSeguro())
                        + "\n,PorcentajeSeguro = " + concatString(request.getPorcentajeSeguro())
                        + "\n,ValorDeclarado = " + concatString(request.getValorDeclarado())
                        + "\n,ObservacionesEmbarque = " + concatString(request.getObservacionesEmbarque())
                        + "\n,ValidarTimbradoFactura = " + concatString(request.getValidarTimbradoFactura())
                        + "\n,TipoServicio = " + concatString(request.getTipoServicio())
                        + "\n,NumeroRemitente = " + concatString(request.getNumeroRemitente())
                        + "\n,CorreoRemitente = " + concatString(request.getCorreoRemitente())
                        + "\n,TelefonoRemitente = " + concatString(request.getTelefonoRemitente())
                        + "\n,ContactoRemitente = " + concatString(request.getContactoRemitente())
                        + "\n,NumeroDestinatario = " + concatString(request.getNumeroDestinatario())
                        + "\n,CorreoDestinatario = " + concatString(request.getCorreoDestinatario())
                        + "\n,TelefonoDestinatario = " + concatString(request.getTelefonoDestinatario())
                        + "\n,ContactoDestinatario = " + concatString(request.getContactoDestinatario())
                        + "\n,EntregaEnSucursal = " + concatString(request.getEntregaEnSucursal())
                        + "\n,SucursalEntrega = " + concatString(request.getSucursalEntrega())
                        + "\n,EntregaDiferenteDomicilio = " + concatString(request.getEntregaDiferenteDomicilio())
                        + "\n,CodigoPostalDiferenteDomicilio = " + concatString(request.getCodigoPostalDiferenteDomicilio())
                        + "\n,ColoniaDiferenteDomicilio = " + concatString(request.getColoniaDiferenteDomicilio())
                        + "\n,CalleNumeroDiferenteDomicilio = " + concatString(request.getCalleNumeroDiferenteDomicilio())
                        + "\n,EntregarEn = " + concatString(request.getEntregarEn())
                        + "\n,DatosAdicionales = " + concatString(request.getDatosAdicionales())
                        + "\n,RecoleccionDiferenteDomicilio = " + concatString(request.getRecoleccionDiferenteDomicilio())
                        + "\n,CodigoPostalDiferenteDomicilioRecoleccion = " + concatString(request.getCodigoPostalDiferenteDomicilioRecoleccion())
                        + "\n,ColoniaDiferenteDomicilioRecoleccion = " + concatString(request.getColoniaDiferenteDomicilioRecoleccion())
                        + "\n,CalleNumeroDiferenteDomicilioRecoleccion = " + concatString(request.getCalleNumeroDiferenteDomicilioRecoleccion())
                        + "\n,RecogerEn = " + concatString(request.getRecogerEn())
                        + "\n,DatosAdicionalesRecoleccion = " + concatString(request.getDatosAdicionalesRecoleccion())
                        + "\n,Latitud = " + concatString(request.getLatitud())
                        + "\n,Longitud = " + concatString(request.getLongitud())
                        + "\n,ConCita = " + concatString(request.getConCita())
                        + "\n,CitaPendiente = " + concatString(request.getCitaPendiente())
                        + "\n,FechaCita = " + concatString(request.getFechaCita())
                        + "\n,HoraMinimaCita = " + concatString(request.getHoraMinimaCita())
                        + "\n,HoraMaximaCita = " + concatString(request.getHoraMaximaCita())
                        + "\n,CantidadPaquete = " + concatString(request.getCantidadPaquete())
                        + "\n,NumeroProducto = " + concatString(request.getNumeroProducto())
                        + "\n,EmbalajePaquete = " + concatString(request.getEmbalajePaquete())
                        + "\n,Largo = " + concatString(request.getLargo())
                        + "\n,Alto = " + concatString(request.getAlto())
                        + "\n,Ancho = " + concatString(request.getAncho())
                        + "\n,PesoPaquete = " + concatString(request.getPesoPaquete())
                        + "\n,ObservacionesPaquete = " + concatString(request.getObservacionesPaquete())
                        + "\n,DescripcionPaquete = " + concatString(request.getDescripcionPaquete())
                        + "\n,CantidadComplemento = " + concatString(request.getCantidadComplemento())
                        + "\n,PesoComplemento = " + concatString(request.getPesoComplemento())
                        + "\n,ClaveProductoServicio = " + concatString(request.getClaveProductoServicio())
                        + "\n,ClaveUnidadMedida = " + concatString(request.getClaveUnidadMedida())
                        + "\n,EsMaterialPeligroso = " + concatString(request.getEsMaterialPeligroso())
                        + "\n,ClaveMaterialPeligroso = " + concatString(request.getClaveMaterialPeligroso())
                        + "\n,ClaveEmbalaje = " + concatString(request.getClaveEmbalaje())
                        + "\n,DescripcionEmbalajeComplemento = " + concatString(request.getDescripcionEmbalajeComplemento())
                        + "\n,ClaveFraccionArancelaria = " + concatString(request.getClaveFraccionArancelaria())
                        + "\n,IdTipoPlantilla = " + request.getIdTipoPlantilla()
                        + "\n,UsarNumeroEquivalenciaDestinatario = " + (request.isUsarNumeroEquivalenciaDestinatario() ? 1 : 0)
                        + "\n,UsarNumeroEquivalenciaResponsablePago = " + (request.isUsarNumeroEquivalenciaResponsablePago() ? 1 : 0)
                        + "\n,ResponsablePago = " + concatString(request.getResponsablePago())
                        + "\n,Referencia = " + concatString(request.getReferencia())
                        + "\n,EsFarmaco = " + concatString(request.getEsFarmaco())
                        + "\n,ClaveSectorCofepris = " + concatString(request.getClaveSectorCofepris())
                        + "\n,NombreIngredienteActivo = " + concatString(request.getNombreIngredienteActivo())
                        + "\n,NombreQuimico = " + concatString(request.getNombreQuimico())
                        + "\n,DenominacionGenericaProd = " + concatString(request.getDenominacionGenericaProd())
                        + "\n,DenominacionDistintivaProd = " + concatString(request.getDenominacionDistintivaProd())
                        + "\n,Fabricante = " + concatString(request.getFabricante())
                        + "\n,FechaCaducidad = " + concatString(request.getFechaCaducidad())
                        + "\n,LoteMedicamento = " + concatString(request.getLoteMedicamento())
                        + "\n,FormaFarmaceutica = " + concatString(request.getFormaFarmaceutica())
                        + "\n,CondicionesEspTransp = " + concatString(request.getCondicionesEspTransp())
                        + "\n,RegistroSanitarioFolioAutorizacion = " + concatString(request.getRegistroSanitarioFolioAutorizacion())
                        + "\n,NumeroCAS = " + concatString(request.getNumeroCAS())
                        + "\n,NumRegSanPlagCOFEPRIS = " + concatString(request.getNumRegSanPlagCOFEPRIS())
                        + "\n,DatosFabricante = " + concatString(request.getDatosFabricante())
                        + "\n,DatosFormulador = " + concatString(request.getDatosFormulador())
                        + "\n,DatosMaquilador = " + concatString(request.getDatosMaquilador())
                        + "\n,UsoAutorizado = " + concatString(request.getUsoAutorizado())
                        + "\nwhere IdPlantilla = " + idPlantilla;
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
                jsonResponse.put("message", "Información guardada con éxito.");
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al guardar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al guardar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }


    }

    @DeleteMapping("/PlantillasImportacionTarifas/Eliminar/{idPlantilla}")
    public ResponseEntity<?> EliminarPlantillas(@RequestHeader("RFC") String rfc,
                                                @PathVariable("idPlantilla") int idPlantilla) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "delete from CatPlantillasImportacionPQ where IdPlantilla = " + idPlantilla;
                Statement statement = jdbcConnection.createStatement();
                statement.executeUpdate(query);
                jsonResponse.put("message", "Se guardaron los cambios.");
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al guardar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al guardar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }
    }

    @GetMapping("/PlantillasImportacionTarifas/GetNombrePlantilla/{idCliente}")
    public ResponseEntity<?> consultarNombrePlantillaPorIdCliente(@RequestHeader("RFC") String rfc,
                                                                  @PathVariable("idCliente") int id) throws Exception {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "");
        jsonResponse.put("data", new JSONObject());
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "Select idPlantilla=IdPlantilla \n" +
                        ", archivoBase64=ArchivoBase64 \n" +
                        ", archivoNombre=ArchivoNombre \n" +
                        ", idCliente=IdCliente \n" +
                        "from CatPlantillasImportacionPQ where IdCliente = " + id;
                Statement statement = jdbcConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                JSONObject queryResponse = convertObject(rs);
                if (queryResponse != null) {
                    query = "Select " +
                            " idCliente=IdCliente" +
                            "\n, numeroCliente=NumeroCliente" +
                            "\n, nombreFiscal=NombreFiscal" +
                            "\nfrom CatClientes where IdCliente = " + queryResponse.getInt("idCliente");
                    statement = jdbcConnection.createStatement();
                    rs = statement.executeQuery(query);
                    queryResponse.put("cliente", convertObject(rs));
                }
                jsonResponse.put("data", queryResponse);
                

                return ResponseEntity.ok(jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Hubo un error al consultar la información.");
                jsonResponse.put("data", new JSONObject());
                return ResponseEntity.status(500).body(jsonResponse.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Hubo un error al consultar la información.");
            jsonResponse.put("data", new JSONObject());
            return ResponseEntity.status(500).body(jsonResponse.toString());
        }
    }

    private String concatString(String value) {
        return "'" + value + "'";
    }

    private JSONObject validarCamposPlantilla(PlantillaRequest plantilla) {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("message", "");
        response.put("data", new JSONObject());
        if (plantilla.getIdCliente() == 0) {
            response.put("success", false);
            response.put("message", "El cliente es un campo obligatorio.");
            return response;
        }
        if (plantilla.getIdTipoPlantilla() == 0) {
            response.put("success", false);
            response.put("message", "El tipo de plantilla es obligatorio.");
            return response;
        }
        if (plantilla.getIdTipoPlantilla() == 1) {
            if (Objects.equals(plantilla.getHojaEmbarques(), "")) {
                response.put("success", false);
                response.put("message", "El nombre de la hoja de embarques es obligatorio.");
                return response;
            }
            if (Objects.equals(plantilla.getHojaPaquetes(), "")) {
                response.put("success", false);
                response.put("message", "El nombre de la hoja de paquetes es obligatorio.");
                return response;
            }
            if (Objects.equals(plantilla.getHojaComplementos(), "")) {
                response.put("success", false);
                response.put("message", "El nombre de la hoja de complementos SAT es obligatorio.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroEmbarque(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número de embarque es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getMoneda(), "")) {
                response.put("success", false);
                response.put("message", "La columna de moneda es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getTipoCambio(), "")) {
                response.put("success", false);
                response.put("message", "La columna de tipo de cambio es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getTipoCobro(), "")) {
                response.put("success", false);
                response.put("message", "La columna de tipo de cobro es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getTipoSeguro(), "")) {
                response.put("success", false);
                response.put("message", "La columna de tipo de seguro es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getValidarTimbradoFactura(), "")) {
                response.put("success", false);
                response.put("message", "La columna validar timbrado de factura es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getTipoServicio(), "")) {
                response.put("success", false);
                response.put("message", "La columna de tipo de servicio es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroRemitente(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número del remitente es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getCorreoRemitente(), "")) {
                response.put("success", false);
                response.put("message", "La columna de correo de remitente es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroDestinatario(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número del destinatario es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getCorreoDestinatario(), "")) {
                response.put("success", false);
                response.put("message", "La columna de correo de destinatario es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getLatitud(), "")) {
                response.put("success", false);
                response.put("message", "La columna de latitud es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getLongitud(), "")) {
                response.put("success", false);
                response.put("message", "La columna de longitud es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getCantidadPaquete(), "")) {
                response.put("success", false);
                response.put("message", "La columna de cantidad de paquetes es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroProducto(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número de paquetes es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getDescripcionPaquete(), "")) {
                response.put("success", false);
                response.put("message", "La columna de descripción de paquetes es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getLargo(), "")) {
                response.put("success", false);
                response.put("message", "La columna de largo de paquete es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getAlto(), "")) {
                response.put("success", false);
                response.put("message", "La columna de alto de paquete es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getAncho(), "")) {
                response.put("success", false);
                response.put("message", "La columna de ancho de paquete es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getEmbalajePaquete(), "")) {
                response.put("success", false);
                response.put("message", "La columna de embalaje de paquete es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getPesoPaquete(), "")) {
                response.put("success", false);
                response.put("message", "La columna de peso de paquete es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getCantidadComplemento(), "")) {
                response.put("success", false);
                response.put("message", "La columna de cantidad de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getPesoComplemento(), "")) {
                response.put("success", false);
                response.put("message", "La columna de peso de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getClaveProductoServicio(), "")) {
                response.put("success", false);
                response.put("message", "La columna de clave de producto/servicio de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getClaveUnidadMedida(), "")) {
                response.put("success", false);
                response.put("message", "La columna de clave de unidad de medida de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
        }
        if (plantilla.getIdTipoPlantilla() == 2) {
            if (Objects.equals(plantilla.getHojaEmbarques(), "")) {
                response.put("success", false);
                response.put("message", "El nombre de la hoja de embarques es obligatorio.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroEmbarque(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número de embarque es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getResponsablePago(), "")) {
                response.put("success", false);
                response.put("message", "La columna de responsable de pago es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroRemitente(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número del remitente es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroDestinatario(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número del destinatario es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getCantidadPaquete(), "")) {
                response.put("success", false);
                response.put("message", "La columna de cantidad de paquetes es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getNumeroProducto(), "")) {
                response.put("success", false);
                response.put("message", "La columna de número de paquetes es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getCantidadComplemento(), "")) {
                response.put("success", false);
                response.put("message", "La columna de cantidad de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getClaveProductoServicio(), "")) {
                response.put("success", false);
                response.put("message", "La columna de clave de producto/servicio de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
            if (Objects.equals(plantilla.getClaveUnidadMedida(), "")) {
                response.put("success", false);
                response.put("message", "La columna de clave de unidad de medida de complemento SAT es obligatoria en la plantilla.");
                return response;
            }
        }


        return response;
    }
}
