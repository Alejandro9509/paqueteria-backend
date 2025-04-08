package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CotizacionRequest;
import com.certuit.base.domain.request.base.PaqueteCotizadorRequest;
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

@RestController
@RequestMapping(value = "/api")
public class CotizacionRest {
    @Autowired
    DBConection dbConection;

    @PostMapping("/Cotizador/Agregar")
    public ResponseEntity<?> postCotizacion(@RequestBody CotizacionRequest request, @RequestHeader("RFC") String rfc)
            throws Exception {
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
        String query = "EXEC usp_CatCotizacionAgregarPQ " +
                request.getIdOrigen() +
                "," + request.getIdDestino() +
                "," + request.getIdZonaEntrega() +
                "," + request.getIdCliente() +
                "," + request.getEntregaEnSucursal() +
                "," + request.getValorDeclarado() +
                "," + request.getAplicaRecoleccion() +
                "," + request.getIdSeguro() +
                "," + request.getAplicaSeguro() +
                "," + request.getPorcentajeSeguro() +
                "," + request.getIdEmbarque() +
                "," + request.getIdRecoleccion() +
                "," + request.getIdZonaRecoleccion() +
                "," + request.getRecoleccionConCita() +
                "," + request.getEmbarqueConCita();
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs;
        statement.execute(query);
        query = "select @@identity as id";
        rs = statement.executeQuery(query);
        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        request.setIdCotizacion(jsonObject.getInt("id"));

        for (PaqueteCotizadorRequest paquete : request.getPaquetesCotizacion()){
            paquete.setIdCotizador(request.getIdCotizacion());
            query = "EXEC usp_CatPaquetesCotizadorAgregarPQ " +
                    paquete.getIdCotizador() +
                    "," + paquete.getTipo() +
                    ", '" + paquete.getDescripcion() +"'" +
                    "," + paquete.getPeso() +
                    "," + paquete.getLargo() +
                    "," + paquete.getAncho() +
                    "," + paquete.getAlto() +
                    "," + paquete.getVolumen() +
                    "," + paquete.getIdTipoEmpaque() +
                    "," + paquete.getValorDeclarado() +
                    ", '" + paquete.getObservaciones() + "'" +
                    "," + paquete.getActivo() +
                    "," + paquete.getCtd() +
                    "," + paquete.getIdProducto() +
                    ", '" + paquete.getClaveEmbalaje() + "'";
            statement.execute(query);
        }
        query = "select top 1 TipoTarifaTarifas from CatParametrosConfiguracion2PQ";
        rs = statement.executeQuery(query);
        JSONObject tarifa = UtilFuctions.convertObject(rs);
        if (tarifa.getInt("TipoTarifaTarifas") == 2){
            query = "EXEC usp_CatTarifasRangosGetByCotizacionPQ_1284 " + request.getIdCotizacion();
        }else{
            query = "EXEC usp_CatTarifasGetByOrigenDestinoTipoCotizacionPQ  " +
                    request.getIdCotizacion() +
                    "," + request.getIdTipoTarifa();
        }
        rs = statement.executeQuery(query);
        String jsonArray = UtilFuctions.convertArray(rs).toString();

            return ResponseEntity.ok(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }

    @PostMapping("/Cotizador/Busqueda")
    public ResponseEntity<?> postCotizacionBsuqueda(@RequestBody CotizacionRequest request,
                                                    @RequestHeader("RFC") String rfc) throws Exception{
        try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
            String query = "EXEC usp_CatCotizacionAgregarPQ " +
                    request.getIdOrigen() +
                    "," + request.getIdDestino() +
                    "," + request.getIdZonaEntrega() +
                    "," + request.getIdCliente() +
                    "," + request.getEntregaEnSucursal() +
                    "," + request.getValorDeclarado() +
                    "," + request.getAplicaRecoleccion() +
                    "," + request.getIdSeguro() +
                    "," + request.getAplicaSeguro() +
                    "," + request.getPorcentajeSeguro() +
                    "," + request.getIdEmbarque() +
                    "," + request.getIdRecoleccion() +
                    "," + request.getIdZonaRecoleccion() +
                    "," + request.getRecoleccionConCita() +
                    "," + request.getEmbarqueConCita();
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs;
            statement.execute(query);
            query = "select @@identity as id";
            rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);
            request.setIdCotizacion(jsonObject.getInt("id"));

            for (PaqueteCotizadorRequest paquete : request.getPaquetesCotizacion()){
                paquete.setIdCotizador(request.getIdCotizacion());
                query = "EXEC usp_CatPaquetesCotizadorAgregarPQ " +
                        paquete.getIdCotizador() +
                        "," + paquete.getTipo() +
                        ", '" + paquete.getDescripcion() +"'" +
                        "," + paquete.getPeso() +
                        "," + paquete.getLargo() +
                        "," + paquete.getAncho() +
                        "," + paquete.getAlto() +
                        "," + paquete.getVolumen() +
                        "," + paquete.getIdTipoEmpaque() +
                        "," + paquete.getValorDeclarado() +
                        ", '" + paquete.getObservaciones() + "'" +
                        "," + paquete.getActivo() +
                        "," + paquete.getCtd() +
                        "," + paquete.getIdProducto() +
                        ", '" + paquete.getClaveEmbalaje() + "'";
                statement.execute(query);
            }
            query = "select top 1 TipoTarifaTarifas from CatParametrosConfiguracion2PQ";
            rs = statement.executeQuery(query);
            JSONObject tarifa = UtilFuctions.convertObject(rs);
            if (tarifa.getInt("TipoTarifaTarifas") == 2){
                query = "EXEC usp_ProBuscadorCotizacionPQ  " + request.getIdCotizacion();
            }else{
                query = "EXEC usp_CatTarifasGetByOrigenDestinoTipoCotizacionPQ  " +
                        request.getIdCotizacion() +
                        "," + request.getIdTipoTarifa();
            }
            rs = statement.executeQuery(query);
            return ResponseEntity.ok(UtilFuctions.convertArray(rs).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
        }
    }
}
