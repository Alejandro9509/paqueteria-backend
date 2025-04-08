package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.CotizacionRequest;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class CotizadorService {
    public int agregarCotizacionEmbarque(CotizacionRequest cotRequest, Statement statement) throws Exception {
        int esEntregaSucursal;
        if (  cotRequest.getEntregaEnSucursal()){
            esEntregaSucursal = 1;
        }else{
            esEntregaSucursal = 0;
        }
        int aplicaSeguro;
        if (  cotRequest.getAplicaSeguro()){
            aplicaSeguro = 1;
        }else{
            aplicaSeguro = 0;
        }
        int recoleccionConCita;
        if (  cotRequest.getRecoleccionConCita()){
            recoleccionConCita = 1;
        }else{
            recoleccionConCita = 0;
        }
        int embarqueConCita;
        if ( cotRequest.getEmbarqueConCita()){
            embarqueConCita = 1;
        }else{
            embarqueConCita = 0;
        }
        int hayRecoleccion;
        if (  cotRequest.getAplicaRecoleccion()){
            hayRecoleccion = 1;
        }else{
            hayRecoleccion = 0;
        }
        String query = "";
        query = "EXEC usp_CatCotizacionAgregarPQ "+cotRequest.getIdOrigen()+", "+ cotRequest.getIdDestino()+", "
                + cotRequest.getIdZonaEntrega()+","+ cotRequest.getIdCliente()+", "+esEntregaSucursal
                +","+ cotRequest.getValorDeclarado()+", "+hayRecoleccion+", "+ cotRequest.getIdSeguro()+", "
                +aplicaSeguro+", "+ cotRequest.getPorcentajeSeguro()+", "+ cotRequest.getIdEmbarque()
                +","+ cotRequest.getIdRecoleccion()+", "+ cotRequest.getIdZonaRecoleccion()+", "+recoleccionConCita
                +", "+embarqueConCita;
        statement.executeUpdate(query);
        query = "SELECT IDENT_CURRENT( 'CatCotizacion' ) as id";
        ResultSet resultSet = statement.executeQuery(query);
        JSONObject jsonResult = UtilFuctions.convertObject(resultSet);
        cotRequest.setIdCotizacion(jsonResult.getInt("id"));
        return cotRequest.getIdCotizacion();
    }
}
