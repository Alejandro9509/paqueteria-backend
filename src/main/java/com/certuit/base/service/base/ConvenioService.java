package com.certuit.base.service.base;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ConvenioService {
    @Autowired
    ConceptosService conceptosService;
    public JSONArray getTarifasConvenioPorIdConvenio(int idConvenio, Connection jdbcConnection) throws Exception{
        String query = "SELECT  " +
                "    tc.IdTarifaConvenio as m_nIdTarifaConvenio,  " +
                "    t.IdTarifa as m_nIdTarifa,  " +
                "    IdSucursal,  " +
                "    IdOrigen,  " +
                "    IdDestino,  " +
                "    (SELECT SUBSTRING(  " +
                "                    (  " +
                "                        SELECT ',' +  (select od.OrigenDestino from CatOrigenesDestinos od where od.IdOrigenDestino = td.IdDestino) AS 'data()'  " +
                "                        FROM CatTarifaDestinosPQ td  " +
                "                        WHERE td.IdTarifa = t.IdTarifa  " +
                "                        FOR XML PATH('')  " +
                "                    ), 2 , 9999))as m_sDestino,  " +
                "    (select OrigenDestino from CatOrigenesDestinos where IdOrigenDestino=IdOrigen )as m_sOrigen,  " +
                "    PrecioM3,  " +
                "    PrecioKilo,  " +
                "    FleteMinimo,  " +
                "    MontoMinimo,  " +
                "    PorPesoVolumen as m_bPorPesoVolumen,  " +
                "    PorRango as m_bPorRango,  " +
                "    PorRegion as m_bPorRegion,  " +
                "    FactorConversion,  " +
                "    activo as Activo,  " +
                "    Codigo as m_sCodigo,  " +
                "    IdConvenio as m_nIdConvenio,  " +
                "    t.IdSucursal,  " +
                "    (select Sucursal from CatSucursales where IdSucursal=t.IdSucursal ) as Sucursal  " +
                "FROM CatTarifaConvenioPQ tc  " +
                "JOIN CatTarifaPQ t ON t.IdTarifa = tc.IdTarifa  " +
                "WHERE tc.IdConvenio = "+idConvenio;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return convertirConcepto(rs, jdbcConnection);
    }

    public JSONArray getZonasConvenioPorIdConvenio(int idConvenio, Connection jdbcConnection) throws Exception{
        String query = "select  " +
                "m_nIdZonaConvenio = IdZonaConvenio, " +
                "  m_nIdConvenio = IdConvenio, " +
                "  m_nIdZona = IdZonaTarifa, " +
                "  m_sEstado = Estado, " +
                "  m_sCodigoZona = CodigoZona " +
                "from CatZonasTarifasConvenioPQ ztc " +
                "join CatZonasTarifasPQ zt on zt.IdZona = ztc.IdZonaTarifa " +
                "where IdConvenio = " +idConvenio;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return convertirZona(rs, jdbcConnection);
    }

    public  JSONArray convertirConcepto(ResultSet resultSet, Connection jdbcConnection) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            obj.put("m_arrArConceptos",conceptosService.getConceptosTarifaConvenio(obj.getInt("m_nIdTarifaConvenio"), jdbcConnection) );
            obj.put("m_arrArProductos",conceptosService.getProductosTarifaConvenio(obj.getInt("m_nIdTarifaConvenio"), jdbcConnection) );
            obj.put("m_arrArDestinos",conceptosService.getDestinosTarifaConvenio(obj.getInt("m_nIdTarifa"), jdbcConnection) );

            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public  JSONArray convertirZona(ResultSet resultSet, Connection jdbcConnection) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            obj.put("m_arrArConceptos",conceptosService.getConceptosZonaTarifaConvenio(obj.getInt("m_nIdZonaConvenio"), jdbcConnection) );

            jsonArray.put(obj);
        }
        return jsonArray;
    }
}
