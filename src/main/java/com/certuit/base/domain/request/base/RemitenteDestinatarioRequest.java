package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RemitenteDestinatarioRequest {
   int idRemitenteDestinatario;
   int idCliente;
   int numero;
   String nombre;
   String rfc;
   boolean activo;
   String calle;
   String noExterior;
   String noInterior;
   String colonia;
   String localidad;
   String idMunicipio;
   String municipio;
   String idEstado;
   String creadoPor;
   String creadoEl;
   String idCP;
   String modificadoPor;
   String modificadoEl;
   String codigoPostal;
   int idSucursal;
   String idPeticion;
   String contacto;
   String correoElectronico;
   String telefono;
   String noRegistroIdentidadFiscal;
   String alias;
   String latitud;
   String longitud;
   String cliente;
   String equivalencia;
}
