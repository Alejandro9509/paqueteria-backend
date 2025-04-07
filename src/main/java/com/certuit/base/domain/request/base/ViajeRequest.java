package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ViajeRequest {
    int m_nIdViaje;
    int m_nIdRutaTrayecto;
    int m_nIdCliente;
    int m_nIdSucursal;
    int m_nIdRemitente;
    int m_nIdDestinatario;
    int m_nIdMoneda;
    int m_nGeneradoDesde;
    int m_nIdEstatusViaje;
    int m_nSecuencia;
    int m_nOrigen;
    int m_nDestino;
    int m_nIdDestino;
    int m_nKilometros;
    String m_dFecha;
    String m_tHora;
    String m_sFecha; //esta es la buena
    String m_sHora; //esta es la buena

    String m_sCandadoOficial;
    String m_sFolioViaje;
    String m_sIdentificador;
    String m_sNumViajeCliente;
    int idRemolque1;
    int idRemolque2;
    int idDolly;
    int m_nErp;
    int idUnidad;
    int m_nTipoCobroPorViaje=1;
    float m_rKilometros;
    float m_xHoras;
    int m_nIdConcepto;
    float m_xImporte;
    int m_nIdOrigen;
    int m_nIdRuta;
    boolean esOperadorPermisionario = false;
    String licenciaPermisionario;
    String nombrePermisionario;
    int idOperador;
    String fechaVigenciaPermisionario;
    
    List<InformeRequest> m_arrInformes;
    int cvr1;
    int cvr2;
    String referencia;
    float kilometros;
    String horas;
    String fechaCarga;
    String fechaEntrega;
    String horaCarga;
    String horaEntrega;
    List<InformeRequest> m_arrInformesDesasignar;
    int m_nIdUsuarioCancelacion;

    int idViajeCompuesto;

    String motivoCancelacion;
    String fechaCancelacion;
}
