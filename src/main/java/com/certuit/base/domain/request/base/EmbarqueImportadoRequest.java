package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class EmbarqueImportadoRequest {
    //ESTOS DATOS SE USAN SOLO PARA RECIBIR LOS EMBARQUES QUE SE VAN A VALIDAR
    int numeroEmbarque = 0;
    boolean esRecoleccion = false;
    int idMoneda = 0;
    String moneda = "";
    int idTipoCambio = 0;
    String tipoCambio = "";
    int idTipoCobro = 0;
    String TipoCobro = "";
    int idCliente = 0;
    int idTipoSeguro = 0;
    String tipoSeguro = "";
    float porcentajeSeguro = 0;
    float valorDeclarado = 0;
    boolean validarTimbradoFactura = false;
    String observaciones = "";
    int idRemitente = 0;
    int numeroRemitente = 0;
    String correoRemitente = "";
    String telefonoRemitente = "";
    String contactoRemitente = "";
    int idDestinatario = 0;
    String numeroDestinatario = "0";
    String correoDestinatario = "";
    String telefonoDestinatario = "";
    String contactoDestinatario = "";
    boolean entregaEnSucursal = false;
    int idSucursalEntrega = 0;
    String sucursalEntrega = "";
    boolean entregaDiferenteDomicilio = false;
    String codigoPostalDiferenteDomicilio = "";
    String coloniaDiferenteDomicilio = "";
    boolean recoleccionDiferenteDomicilio = false;
    String codigoPostalDiferenteDomicilioRecoleccion = "";
    String coloniaDiferenteDomicilioRecoleccion = "";
    String latitud = "";
    String longitud = "";
    boolean conCita = false;
    boolean citaPendiente = false;
    String fechaCita = "";
    String horaCitaMinima = "";
    String horaCitaMaxima = "";
    int idTipoServicio = 0;
    String tipoServicio = "";
    int responsablePago = 0;
    String referencia = "";
    ArrayList<PaqueteImportadoRequest> paquetes = new ArrayList<>();
    ArrayList<ComplementoSATImportadoRequest> complementosSAT = new ArrayList<>();

    //ESTAS VARIABLES SE USAN PARA RECIBIR LOS EMBARQUES QUE SE VAN A AGREGAR JUNTO CON LOS QUE ESTAN ARRIBA
    String fechaRegistro = "";
    String horaRegistro = "";
    int idEstatus = 0;
    int idSucursalRegistro = 0;
    int idUsuarioRegistro = 0;
    String aliasRemitente = "";
    String nombreRemitente = "";
    String rfcRemitente = "";
    String domicilioRemitente = "";
    int idCodigoPostalRemitente = 0;
    String calleRemitente = "";
    String numeroInteriorRemitente = "";
    String numeroExteriorRemitente = "";
    String coloniaRemitente = "";
    String idEstadoRemitente = "";
    String idMunicipioRemitente = "";
    int idOrigen = 0;
    String aliasDestinatario = "";
    String nombreDestinatario = "";
    String rfcDestinatario = "";
    String domicilioDestinatario = "";
    int idCodigoPostalDestinatario = 0;
    String calleDestinatario = "";
    String numeroInteriorDestinatario = "";
    String numeroExteriorDestinatario = "";
    String coloniaDestinatario = "";
    String idEstadoDestinatario = "";
    String idMunicipioDestinatario = "";
    int idDestino = 0;
    int idZonaEntrega = 0;
    int idCodigoPostalDiferenteDomicilio = 0;
    String idEstadoDiferenteDomicilio = "";
    String idMunicipioDiferenteDomicilio = "";
    String calleNumeroDiferenteDomicilio = "";
    String entregarEn = "";
    String datosAdicionalesEntrega = "";
    int idZonaRecoleccion = 0;
    int idCodigoPostalDiferenteDomicilioRecoleccion = 0;
    String idEstadoDiferenteDomicilioRecoleccion = "";
    String idMunicipioDiferenteDomicilioRecoleccion = "";
    String calleNumeroDiferenteDomicilioRecoleccion = "";
    String recogerEn = "";
    String datosAdicionalesRecoleccion = "";
    boolean aplicaSeguro = false;
    int idRuta = 0;
    int idCotizacion = 0;
    ArrayList<ConceptoFacturacionImportadoRequest> conceptosFacturacion = new ArrayList<>();
}
