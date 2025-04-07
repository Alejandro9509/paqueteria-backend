package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.*;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.certuit.base.util.UtilFuctions.convertArray;

@Service
public class PaquetesService {
    public void agregarPaquetesARecoleccion(RecoleccionRequest recoRequest, Statement statement) throws SQLException, Exception {
        String query = "";
        for (PaqueteRequest paquete : recoRequest.getM_parrPaquetes()) {
            paquete.setM_nIdRecoleccion(recoRequest.getM_nIdRecoleccion());
            query = "EXEC usp_ProRecoleccionAgregarPaquetePQ "+paquete.getM_nIdRecoleccion() +", "
                    +paquete.getM_nCantidad()+", '"+paquete.getM_sDescripcion()+"', "+paquete.getM_rPeso()
                    +"," + paquete.getM_rLargo() +", "+ paquete.getM_rAncho() +", "+paquete.getM_rAlto() +", "
                    +paquete.getM_rVolumen()+", "+paquete.getM_nIdTipoEmbalaje()+", "+paquete.getM_cyValorDeclarado()
                    +", '"+paquete.getM_sObservaciones()+"', "+paquete.getM_nIdProducto() +", "+paquete.getM_nIdTipo()
                    +", '"+paquete.getM_sClaveSATProducto() +"','"+paquete.getM_sClaveSATUnidad() +"', '"
                    +paquete.getM_sEmbalajeSAT()+"'";
            statement.executeUpdate(query);
        }
    }

    public void modificarPaquetesRecoleccion(RecoleccionRequest recoRequest, Statement statement)
            throws SQLException, Exception {
        String query = "";
        query = "EXEC usp_ProRecoleccionEliminarPaquetesPQ " + recoRequest.getM_nIdRecoleccion();
        statement.executeUpdate(query);
        for (PaqueteRequest paquete : recoRequest.getM_parrPaquetes()) {
            paquete.setM_nIdRecoleccion(recoRequest.getM_nIdRecoleccion());
            query = "EXEC usp_ProRecoleccionAgregarPaquetePQ "+paquete.getM_nIdRecoleccion() +", "
                    +paquete.getM_nCantidad()+", '"+paquete.getM_sDescripcion()+"', "+paquete.getM_rPeso()
                    +"," + paquete.getM_rLargo() +", "+ paquete.getM_rAncho() +", "+paquete.getM_rAlto() +", "
                    +paquete.getM_rVolumen()+", "+paquete.getM_nIdTipoEmbalaje()+", "+paquete.getM_cyValorDeclarado()
                    +", '"+paquete.getM_sObservaciones()+"', "+paquete.getM_nIdProducto() +", "+paquete.getM_nIdTipo()
                    +", '"+paquete.getM_sClaveSATProducto() +"','"+paquete.getM_sClaveSATUnidad() +"', '"
                    +paquete.getM_sEmbalajeSAT()+"'";
            statement.executeUpdate(query);
        }
    }

    public void agregarPaquetesAEmbarque(EmbarqueRequest embRequest, Statement statement)
            throws SQLException, Exception {
        String query = "";
        for (PaqueteRequest paquete : embRequest.getM_arrClsDetalle()) {
            paquete.setM_nIdEmbarque(embRequest.getM_nIdEmbarque());
            query = "EXEC usp_ProEmbarqueDetalleAgregarPQ "+paquete.getM_nIdEmbarque() +", "+paquete.getM_nTipo()+", '"
                    +paquete.getM_sDescripcion()+"', "+paquete.getM_xPeso()
                    +"," + paquete.getM_xLargo() +", "+ paquete.getM_xAncho() +", "+paquete.getM_xAlto() +", "
                    +paquete.getM_xVolumen()+", "+paquete.getM_nIdTIpoEmpaque()
                    +", "+paquete.getM_cValorDeclarado()+", '"+paquete.getM_sObservaciones()+"', 0, "+paquete.getCtd()
                    +", "+paquete.getM_nIdProducto() +",'"+paquete.getClaveSATProducto() +"', '"
                    +paquete.getClaveSATUnidad()+"', '"+paquete.getClaveEmbalaje()+"'";
            statement.executeUpdate(query);
        }
    }

    /**Retorna listado de paquetes que se mandaron en entrega parcial.*/
    public JSONArray getPaquetesParcialesByGuia(int idParadaGuia, Connection jdbcConnection)
            throws SQLException,Exception{
        Statement statement = jdbcConnection.createStatement();
        String query = "Select PUMGPPU.IdParciales         as idParciales,\n" +
                "       PUMGPPU.IdPaquete           as idPaquete,\n" +
                "       PGP.Descripcion             as descripcion,\n" +
                "       PUMGPPU.[index]             as noIndex,\n" +
                "       PGP.ctd as cantidad\n" +
                "from ProUltimaMillaGuiasPaquetesParcialesPQ PUMGPPU\n" +
                "         left join ProEmbarqueDetallePQ PGP on PGP.IdEmbarqueDetalle = PUMGPPU.IdPaquete\n" +
                "where PUMGPPU.IdParadaGuia = "+idParadaGuia;
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = UtilFuctions.convertArray(rs);
        return array;
    }

    //Servicio obtener paquetes por parada
    /**Retorna los paquetes de la guia con los indices de los paquetes que ya fueron o estan siendo entregadas de
     * forma parcial pero no cancelados*/
    public JSONArray getPaquetesDisponiblesPorParada(int idGuia, Connection jdbcConnection)
            throws SQLException,Exception{
        Statement statement = jdbcConnection.createStatement();
        String query = " SELECT\n" +
                "pe.IdEmbarqueDetalle as idPaquete\n" +
                ",guia.IdGuia as idGuia\n" +
                ",pe.Descripcion as descripcion \n" +
                ",ctd as cantidad\n" +
                "FROM ProEmbarqueDetallePQ pe \n" +
                "inner join ProGuiaPQ guia on guia.IdEmbarque = pe.IdEmbarque\n" +
                "WHERE guia.IdGuia = " + idGuia;
        ResultSet rss = statement.executeQuery(query);
        JSONArray jsonArrayResult = new JSONArray();
        while (rss.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idPaquete", rss.getInt("idPaquete"));
            jsonObject.put("idGuia", rss.getInt("idGuia"));
            jsonObject.put("descripcion", rss.getString("descripcion"));
            jsonObject.put("cantidad", rss.getInt("cantidad"));
            jsonObject.put("indexEnParciales",getIndexPaquetesEnParciales(rss.getInt("idGuia"),
                    rss.getInt("idPaquete"), jdbcConnection));
            jsonArrayResult.put(jsonObject);
        }
        return jsonArrayResult;
    }

    /**Retorna el listado de index de los paquetes que están en entrega parcial pero no canceladas*/
    public JSONArray getIndexPaquetesEnParciales(int idGuia,int idPaquete, Connection connection)
            throws SQLException,Exception{
        String query = "Select pe.[index] as noIndex \n" +
                "FROM ProUltimaMillaGuiasPaquetesParcialesPQ pe \n" +
                "INNER join ProParadaUltimaMillaGuiasPQ guia on guia.IdParadaGuia = pe.IdParadaGuia \n" +
                "where guia.EsRecoleccion = 0 \n" +
                "and guia.EsEntregaParcial = 1 \n" +
                "and guia.IdEstatusUltimaMilla != 4 \n" +
                "and guia.m_nIdGuia = "+idGuia+"\n" +
                "and pe.IdPaquete = "+idPaquete+"\n" +
                "order by pe.[index]";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return convertArray(rs);
    }

}
