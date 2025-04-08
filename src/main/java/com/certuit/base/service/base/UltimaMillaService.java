package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.GuiaUltimaMilla;
import com.certuit.base.domain.request.base.RutaRequest;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class UltimaMillaService {

    public void eliminarPaquetesIdParada(int idParada, Statement statement) throws Exception {
        try{
            String query =  "EXEC usp_ProUltimaMillaEliminarParadasPQ " + idParada;
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void cambiarOrdenRuta (RutaRequest ruta, Statement statement) throws Exception {
        try{
            String query = "";
            for (GuiaUltimaMilla guia : ruta.getGuias()) {
                if (guia.getIdEstatus() == 1) {
                    query =
                            "DELETE FROM ProParadaUltimaMillaGuiasPQ " +
                                    "WHERE IdEstatusUltimaMilla = 1 " +
                                    " AND IdParadaGuia = " + guia.getIdParadaGuia();
                    statement.executeUpdate(query);
                }
            }
            agregarGuiasParada(ruta,statement);
            descartarGuiasDeRuta(ruta,statement);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void descartarGuiasDeRuta (RutaRequest ruta, Statement statement) throws Exception {
        try{
            String query = "";
            for (GuiaUltimaMilla guia : ruta.getGuiasDescartadasDeRuta()) {
                if (guia.isEsRecoleccion()){
                    query = "UPDATE ProRecoleccionPQ SET IdEstatusRecoleccion = 1 WHERE IdRecoleccion = "
                            +guia.getIdGuia();
                } else {
                    query = "UPDATE ProGuiaPQ SET IdEstatusGuia = 14 WHERE IdGuia = " + guia.getIdGuia();
                }
                statement.executeUpdate(query);
                query =
                        "DELETE FROM ProParadaUltimaMillaGuiasPQ " +
                                "WHERE IdEstatusUltimaMilla = 1 " +
                                " AND IdParadaGuia = " + guia.getIdParadaGuia();
                statement.executeUpdate(query);

                query= "SELECT Count(*) as GuiasPendientes\n" +
                        "          FROM ParadaUltimaMillaPQ\n" +
                        "                   inner join ProParadaUltimaMillaGuiasPQ " +
                        "on m_nIdProParadaUltimaMilla = m_nIdParadaUltimaMilla\n" +
                        "          where m_nIdProParadaUltimaMilla = "+ruta.getM_nIdParadaUltimaMilla()+"\n" +
                        "            and IdEstatusUltimaMilla < 3";
                ResultSet rs=statement.executeQuery(query);
                rs.next();
                if(rs.getInt("GuiasPendientes")==0)
                {
                    query="UPDATE ParadaUltimaMillaPQ\n" +
                            "SET Completada=1\n" +
                            "WHERE m_nIdProParadaUltimaMilla="+ruta.getM_nIdParadaUltimaMilla();
                    statement.executeUpdate(query);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**Agrega las guias a la ruta de ultima milla especificada.*/
    public void agregarGuiasParadaEspecificado(RutaRequest ruta, Statement statement,String fecha,String hora)
            throws Exception {
        try{
            String query = "";
            ResultSet rs;

            for (GuiaUltimaMilla guia : ruta.getGuias()){
                if (guia.isEsRecoleccion()){
                    query = "UPDATE ProRecoleccionPQ SET IdEstatusRecoleccion = 3 WHERE IdRecoleccion = "
                            +guia.getIdGuia();
                } else {
                    query = "UPDATE ProGuiaPQ SET IdEstatusGuia = 17 WHERE IdGuia = " + guia.getIdGuia();
                }
                statement.executeUpdate(query);

                query = "insert into ProParadaUltimaMillaGuiasPQ (m_nIdParadaUltimaMilla, m_nIdGuia,EsRecoleccion," +
                        "EsEntregaParcial,IdEstatusUltimaMilla, OrdenUltimaMilla) " +
                        "values ("+ruta.getM_nIdParadaUltimaMilla()+", "+guia.getIdGuia()+", "
                        +(guia.isEsRecoleccion() ? 1 : 0)+",0,1,"+guia.getOrden()+")";
                statement.executeUpdate(query);
                query = "SELECT IDENT_CURRENT( 'ProParadaUltimaMillaGuiasPQ' ) as id";
                rs = statement.executeQuery(query);
                int idParadaGuia = 0;
                while(rs.next()){
                    idParadaGuia = rs.getInt("id");
                }

                //ruta.setIdParadaGuia(rs.getInt("id"));

                query = " insert into BitacoraSeguimientoPQ (IdRegistro, Descripcion, Fecha, Hora, Proceso)\n" +
                        "             values (" + idParadaGuia+ ", 'Paquete en reparto última milla' ,'"+fecha+"','"
                        +hora+"', 9)";
                statement.executeUpdate(query);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    /**Agrega las guias a la ruta de ultima milla especificada.*/
    public void agregarGuiasParada(RutaRequest ruta, Statement statement) throws Exception {
        try{
            String query = "";
            ResultSet rs;
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();

            for (GuiaUltimaMilla guia : ruta.getGuias()){
                if (guia.getIdEstatus() == 1) {
                    if (guia.isEsRecoleccion()) {
                        query = "UPDATE ProRecoleccionPQ SET IdEstatusRecoleccion = 3 WHERE IdRecoleccion = "
                                + guia.getIdGuia();
                    } else {
                        query = "UPDATE ProGuiaPQ SET IdEstatusGuia = 17 WHERE IdGuia = " + guia.getIdGuia();
                    }
                    statement.executeUpdate(query);

                    query = "insert into ProParadaUltimaMillaGuiasPQ (m_nIdParadaUltimaMilla, m_nIdGuia," +
                            "EsRecoleccion,EsEntregaParcial,IdEstatusUltimaMilla, OrdenUltimaMilla) " +
                            "values (" + ruta.getM_nIdParadaUltimaMilla() + ", " + guia.getIdGuia() + ", "
                            + (guia.isEsRecoleccion() ? 1 : 0) + ",0,1," + guia.getOrden() + ")";
                    statement.executeUpdate(query);
                    query = "SELECT IDENT_CURRENT( 'ProParadaUltimaMillaGuiasPQ' ) as id";
                    rs = statement.executeQuery(query);
                    int idParadaGuia = 0;
                    while (rs.next()) {
                        idParadaGuia = rs.getInt("id");
                    }

                    //ruta.setIdParadaGuia(rs.getInt("id"));
                    query = " insert into BitacoraSeguimientoPQ (IdRegistro, Descripcion, Fecha, Hora, Proceso)\n" +
                            "             values (" + idParadaGuia + ", 'Paquete en reparto última milla' ,'" + date
                            + "','" + time + "', 9)";
                    statement.executeUpdate(query);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public JSONArray getZonasByUltimaMilla(Statement statement, int idUltimaMilla) throws Exception {
        try{
            String query = "SELECT\n" +
                    "zonas.IdZona as m_nIdZona,\n" +
                    "zonas.Folio as m_nFolio,\n" +
                    "zonas.Descripcion as m_sDescripcion,\n" +
                    "zonas.IdSucursal as m_nIdSucursal,\n" +
                    "sucursal.Sucursal as m_sSucursal,\n" +
                    "zonas.CreadoEl as m_dtCreadoEl,\n" +
                    "zonas.CreadoPor as m_nCreadoPor,\n" +
                    "zonas.ModificadoEl as m_dtModificadoEl,\n" +
                    "zonas.ModificadoPor as m_nModificadoPor,\n" +
                    "zonas.CostoEntrega as m_cyCostoEntrega,\n" +
                    "zonas.CostoRecolectar as m_cyCostoRecolectar\n" +
                    "  FROM CatZonasPQ AS zonas\n" +
                    "  INNER JOIN CatSucursales AS sucursal\n" +
                    "  ON zonas.IdSucursal = sucursal.IdSucursal\n" +
                    "  WHERE zonas.IdZona = (select m_nIdZonas from ProUltimaMillaZonasPQ as temp " +
                    "where temp.m_nIdZonas = zonas.IdZona and temp.m_nIdUltimaMilla = "+idUltimaMilla+" )\n";
            ResultSet rs = statement.executeQuery(query);
            return UtilFuctions.convertArray(rs);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public JSONArray getParadasByIdUltimaMilla(Statement statement, int idUltimaMilla)
            throws Exception {
        try{
            String query = "SELECT\n" +
                    "    parada.m_nIdProParadaUltimaMilla as m_nIdParadaUltimaMilla,\n" +
                    "    parada.m_nIdUltimaMilla as m_nIdUltimaMilla,\n" +
                    "    ultimaMilla.m_dFecha as m_dFecha,\n" +
                    "    ultimaMilla.m_nCreadoPor as creadoPor,\n" +
                    "    (cast(operador.NumeroOperador as varchar(max)) + '-' +  operador.Nombres + ' ' + " +
                    "operador.ApellidoPaterno + ' ' + operador.ApellidoMaterno) as m_snNombreOperador,\n" +
                    "    operador.IdOperador as m_nIdOperador,\n" +
                    "    unidad.IdUnidad as m_nIdUnidad,\n" +
                    "    unidad.Codigo+ '-' + unidad.Descripcion as m_sPlacasUnidad,\n" +
                    "    ISNULL((select TOP 1 lat from CoordenadasOperadorPQ where idCoordenadasOperador in " +
                    "(select idCoordenadaOperador from ParadaUltimaMillaCoordenadasOperadorPQ pc " +
                    "where pc.idParadaUltimaMilla = parada.m_nIdProParadaUltimaMilla) " +
                    "order by fecha desc),0) as m_xlat,\n" +
                    "    ISNULL((select TOP 1 lng from CoordenadasOperadorPQ where idCoordenadasOperador in " +
                    "(select idCoordenadaOperador from ParadaUltimaMillaCoordenadasOperadorPQ pc " +
                    "where pc.idParadaUltimaMilla = parada.m_nIdProParadaUltimaMilla) " +
                    "order by fecha desc),0) as m_xlng,\n" +
                    "    unidad.EsUnidadPermisionario as m_bEsUnidadPermisionario,\n" +
                    "    operador.EsPermisionario as m_bEsPermisionario,\n" +
                    "    parada.Activa as m_bActiva,\n" +
                    "    remolque1.IdUnidad as m_nIdRemolque1,\n" +
                    "    remolque2.IdUnidad as m_nIdRemolque2,\n" +
                    "    remolque1.Codigo + '-' + remolque1.Descripcion  as m_sRemolque1,\n" +
                    "    remolque2.Codigo + '-' + remolque2.Descripcion as m_sRemolque2,\n" +
                    "    dolly.IdUnidad as m_nIdDolly,\n" +
                    "    dolly.Codigo + '-' + dolly.Descripcion as Dolly,\n" +
                    "    ISNULL(parada.EsEntregaParcial, 0) as m_bEsEntregaParcial\n" +
                    "FROM ParadaUltimaMillaPQ as parada\n" +
                    "         inner join CatUnidades as unidad\n" +
                    "                    on parada.m_nIdUnidad = unidad.IdUnidad\n" +
                    "         inner join CatOperadores as operador\n" +
                    "                    on parada.m_nIdOperador = operador.IdOperador\n" +
                    "         inner join ProUltimaMillaPQ as ultimaMilla\n" +
                    "                    on parada.m_nIdUltimaMilla = ultimaMilla.m_nIdUltimaMilla\n" +
                    "         left join CatUnidades as remolque1\n" +
                    "                   on parada.IdRemolque1 = remolque1.IdUnidad\n" +
                    "         left join CatUnidades as remolque2\n" +
                    "                   on parada.IdRemolque2 = remolque2.IdUnidad\n" +
                    "         left join CatUnidades as dolly\n" +
                    "                   on parada.IdDolly = dolly.IdUnidad\n" +
                    "WHERE parada.m_nIdUltimaMilla = "+idUltimaMilla;
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonResponse = UtilFuctions.convertArray(rs);
            for (int i= 0 ;  i < jsonResponse.length(); i++) {
                JSONObject jsonTemp = jsonResponse.getJSONObject(i);
                jsonTemp.put("m_arrClsProGuia", getPaquetesByIdParada(statement,jsonTemp.getInt("m_nIdParadaUltimaMilla")));
            }
            return jsonResponse;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public JSONArray getPaquetesByIdParada(Statement statement, int idParadaUltimaMilla)
            throws Exception {
        try{
            String query = "EXEC usp_ProIdUltimaMillaGetPaquetesPQ "+idParadaUltimaMilla;
            ResultSet rs = statement.executeQuery(query);
            JSONArray jsonResponse = UtilFuctions.convertArray(rs);
            for (int i= 0 ;  i < jsonResponse.length(); i++){
                JSONObject jsonTemp = jsonResponse.getJSONObject(i);
                jsonTemp.put("m_nId", jsonTemp.getInt("m_nIdPaquete"));
                if (jsonTemp.getBoolean("m_bEsRecoleccion")){
                    jsonTemp.put("m_parrPaquetes", getListadoPaquetesByIdRecoleccion(statement,jsonTemp.getInt("m_nIdPaquete")));
                }else{
                    jsonTemp.put("m_arrPaquetes", getListadoPaquetesByIdGuia(statement,jsonTemp.getInt("m_nIdPaquete")));
                }
                jsonTemp.put("m_arrImagenes", new ArrayList<>());
            }
            return jsonResponse;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public JSONArray getListadoPaquetesByIdRecoleccion(Statement statement, int idRecoleccion)
            throws Exception {
        try{
            String query = "SELECT\n" +
                    "IdPaquete as m_nIdPaquete\n" +
                    ",Peso as m_rPeso\n" +
                    ",Largo as m_rLargo\n" +
                    ",Ancho as m_rAncho\n" +
                    ",Alto as m_rAlto\n" +
                    ",Volumen as m_rVolumen\n" +
                    ",IdTipoEmbalaje as m_nIdTipoEmbalaje\n" +
                    ",ValorDeclarado as m_cyValorDeclarado\n" +
                    ",Descripcion as m_sDescripcion\n" +
                    ",Cantidad as m_nCantidad\n" +
                    ",Observaciones as m_sObservaciones\n" +
                    ",IdRecoleccion as m_nIdRecoleccion\n" +
                    ",IdProducto as m_nIdProducto\n" +
                    ",IdTipo as m_nIdTipo\n" +
                    ",ClaveSATProducto as m_sClaveSATProducto\n" +
                    ",ClaveSATUnidad as m_sClaveSATUnidad\n" +
                    ",ClaveEmbalaje as m_sClaveEmbalaje\n" +
                    ",(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                    "CAST(e.ClaveSATProducto as varchar) and CatalogoSAT = 'c_ClaveProdServ') as m_nProductoSAT\n" +
                    ",(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                    "CAST(e.ClaveSATUnidad as varchar) and CatalogoSAT = 'c_ClaveUnidad') as m_nUnidadSAT\n" +
                    ",(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                    "CAST(e.ClaveEmbalaje as varchar) and CatalogoSAT = 'c_TipoEmbalaje') as m_sEmbalajeSAT\n" +
                    "FROM ProRecoleccionPaquetePQ e\n" +
                    "WHERE IdRecoleccion = "+idRecoleccion;
            ResultSet rs = statement.executeQuery(query);
            return UtilFuctions.convertArray(rs);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public JSONArray getListadoPaquetesByIdGuia(Statement statement, int idGuia) throws Exception {
        try{
            String query = "SELECT\n" +
                    "    e.IdEmbarqueDetalle as m_nIdEmbarqueDetalle\n" +
                    "     ,e.IdEmbarque as m_nIdEmbarque\n" +
                    "     ,e.Tipo as m_nTipo\n" +
                    "     ,e.Descripcion as m_sDescripcion\n" +
                    "     ,e.Peso as m_xPeso\n" +
                    "     ,e.Largo as m_xLargo\n" +
                    "     ,e.Ancho as m_xAncho\n" +
                    "     ,e.Alto as m_xAlto\n" +
                    "     ,e.Volumen as m_xVolumen\n" +
                    "     ,e.IdTipoEmpaque as m_nIdTIpoEmpaque\n" +
                    "     ,(select Nombre from CatEmbalajesPQ where IdEmbalaje = e.IdTipoEmpaque) as m_sEmbalaje\n" +
                    "     ,e.ValorDeclarado as m_cValorDeclarado\n" +
                    "     ,e.Observaciones as m_sObservaciones\n" +
                    "     ,e.Activo as m_bActivo\n" +
                    "     ,e.ctd\n" +
                    "     ,e.IdProducto as m_nIdProducto\n" +
                    "     ,(select Descripcion from CatProductosPQ p where p.IdProducto = e.IdProducto ) as m_sProducto\n" +
                    "FROM ProEmbarqueDetallePQ e\n" +
                    "\twhere IdEmbarque = (select pg.IdEmbarque from ProGuiaPQ pg where pg.IdGuia = "
                    +idGuia+") AND Tipo = 2";
            ResultSet rs = statement.executeQuery(query);
            return UtilFuctions.convertArray(rs);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public JSONArray getImagenPaquete(Statement statement, int idGuia, boolean esRecoleccion)
            throws Exception {
        try{
            String query = "SELECT * FROM CatParadasImagenes where IdGuia = "+idGuia
                    +" AND EsRecoleccion = "+esRecoleccion;
            ResultSet rs = statement.executeQuery(query);
            return UtilFuctions.convertArray(rs);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**Actualiza los datos de ProGuiaPQ y ProRecoleccionPQ con los datos de ultima milla*/
    public void guardarRuta(RutaRequest ruta, Statement statement) throws Exception {
        try{
            String query = "";
            for (GuiaUltimaMilla guia : ruta.getGuias()) {
                query = "EXEC usp_ProGuiaGenerarRutaPQ "+guia.getIdGuia()+", '"+guia.getLng()+"', '"+guia.getLat()+"'"
                        +", "+guia.getOrden()+", "+(guia.isEsRecoleccion() ? 1 : 0)+", '"+guia.getHoraEstimada()+"',"
                        +guia.getKilometros();
                statement.executeUpdate(query);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
