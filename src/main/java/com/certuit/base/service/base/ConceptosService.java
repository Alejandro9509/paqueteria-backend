package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.ConceptoFacturacionRequest;
import com.certuit.base.domain.request.base.EmbarqueRequest;
import com.certuit.base.domain.request.base.RecoleccionRequest;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ConceptosService {
    public JSONArray getDestinosTarifaConvenio(int idConvenio, Connection jdbcConnection) throws Exception{
        String query = "SELECT " +
                " IdOrigenDestino as m_nIdCiudad, " +
                " OrigenDestino as m_sCiudad, " +
                " IdOrigenDestino as m_nCodigo " +
                " From CatOrigenesDestinos " +
                " WHERE IdOrigenDestino in (select ctd.IdDestino from CatTarifaDestinosPQ ctd where ctd.IdTarifa = " + idConvenio+ ")" ;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }
    public JSONArray getProductosTarifaConvenio(int idConvenio, Connection jdbcConnection) throws Exception{
        String query = "Select  " +
                "  m_nIdTarifaConvenioProducto = IdProductoTarifaConvenio, " +
                " m_nIdTarifaConvenio = IdTarifaConvenio, " +
                " m_nNoProducto = NoProducto, " +
                " m_sDescripcion = Descripcion, " +
                " m_nIdProducto = pc.IdProducto, " +
                " m_nIdEmbalaje = IdEmbalaje, " +
                " m_sEmbalaje = Embalaje, " +
                " m_xLargo = Largo, " +
                " m_xAlto = Alto, " +
                " m_xAncho = Ancho, " +
                " m_xPeso = Peso, " +
                " m_cImporte = Importe, " +
                " m_cImporteIva = ImporteIva, " +
                " m_cImporteRetiene = ImporteRetiene, " +
                " m_nIdImpuestoRetiene = IdImpuestoRetiene, " +
                " m_nIdImpuestoTraslada = IdImpuestoTraslada, " +
                " m_bActivo = Activo " +
                "  FROM CatProductosTarifaConvenioPQ pc " +
                "  JOIN CatProductosPQ p ON p.IdProducto = pc.IdProducto " +
                " WHERE IdTarifaConvenio = " + idConvenio;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getConceptosZonaTarifaConvenio(int idConvenio, Connection jdbcConnection) throws Exception{
        String query = " select  " +
                " m_cImporte = Importe, " +
                "  m_cImporteIva = ImporteIva, " +
                "  m_cImporteRetiene = ImporteRetiene, " +
                "  m_nIdConceptosFacturacion = IdConceptoFacturacion, " +
                "  m_nIdImpuestoRetiene = IdImpuestoRetiene, " +
                "  m_nIdImpuestoTraslada = IdImpuestoTraslada, " +
                "  m_nIdZonaConceptos = IdZonaConvenioConceptos,   " +
                "  m_nIdZona = IdZonaConvenio, " +
                "  m_xnRangoMinimo = RangoMinimo, " +
                "  m_xnRangoMaximo = RangoMaximo, " +
                "  m_nIdTipoCalculo = IdTipoCalculo, " +
                "  m_nIdAgregadoDesde = IdAgregadoDesde, " +
                "  m_nIdTipoMedida = IdTipoMedida, " +
                " (select ConceptoFacturacion from CatConceptosFacturacion cf where cf.IdConceptoFacturacion = ztc.IdConceptoFacturacion) as m_sConcepto  " +
                " from CatZonasTarifasConvenioConceptosPQ ztc where IdZonaConvenio =  " + idConvenio;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return convertirConcepto(rs, jdbcConnection);
    }
    public JSONArray getConceptosTarifaConvenio(int idConvenio, Connection jdbcConnection) throws Exception{
        String query = "SELECT " +
                " IdTarifaConvenioConcepto as m_nIdTarifaConvenioConcepto " +
                "    ,IdTarifaConvenio as m_nIdTarifaConvenio " +
                "    ,Importe as m_cImporte       " +
                "    ,IdImpuestoTraslada as m_nIdImpuestoTraslada " +
                "    ,ImporteIva as m_cImporteIva " +
                "    ,IdImpuestoRetiene as m_nIdImpuestoRetiene " +
                "    ,ImporteRetiene as m_cImporteRetiene " +
                "    ,TF.IdConceptoFacturacion as m_nIdConceptosFacturacion " +
                "    ,IdTipoCalculo as m_nIdTipoCalculo " +
                " ,RangoMinimo as m_xnRangoMinimo " +
                " ,RangoMaximo as m_xnRangoMaximo " +
                " ,IdAgregadoDesde as m_nIdAgregadoDesde " +
                " ,IdTipoMedida as m_nIdTipoMedida " +
                " ,CF.ConceptoFacturacion AS m_sConcepto " +
                "FROM CatTarifaConvenioConceptosPQ TF INNER JOIN CatConceptosFacturacion CF ON TF.IdConceptoFacturacion = CF.IdConceptoFacturacion " +
                "WHERE IdTarifaConvenio =" + idConvenio;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return convertirConcepto(rs, jdbcConnection);
    }

    public  JSONArray convertirConcepto(ResultSet resultSet, Connection jdbcConnection) throws Exception {

        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            obj.put("arClsDetalle",obtenerConceptosTarifa(obj.getInt("m_nIdConceptosFacturacion"), jdbcConnection) );
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public  JSONArray obtenerConceptosTarifa(int idConcepto, Connection jdbcConnection) throws Exception{
        String query = "SELECT Detalle.IdConceptoFacturacionImpuesto as IdConceptosFacturacionDetalle " +
                "      ,Detalle.IdConceptoFacturacion as IdConceptosFacturacion " +
                "      ,IdImpuesto " +
                "      ,(SELECT CASE WHEN TipoCalculo = 1 THEN 1 ELSE 0 END  from CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Trasladado " +
                "   ,(SELECT Impuesto FROM CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Impuesto " +
                "   ,(SELECT Porcentaje FROM CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Porcentaje " +
                "      ,Detalle.Predeterminado " +
                "    FROM CatConceptosFacturacionImpuestos AS Detalle " +
                " WHERE IdConceptoFacturacion = " + idConcepto;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }

    public JSONObject conceptosFacturacion(int id, Connection jdbcConnection) throws Exception {
        String query = "SELECT\n" +
                "\tIdConceptoFacturacion as IdConceptosFacturacion\n" +
                "      ,Codigo\n" +
                "      ,ConceptoFacturacion as Concepto\n" +
                "      ,Activo\n" +
                "      ,IncluirCalculoIngresosLiquidacion as CalculoIngresos\n" +
                "      ,IncluirCalculoLiquidacionPorcentajeSobreImpFlete as CalculoFlete\n" +
                "      ,(select IdCatUnidadMedidaSAT from CatUnidadMedidaSATPQ cs where cs.ClaveUnidad = ClaveSATUnidad COLLATE Modern_Spanish_CS_AS) as IdUnidadMedidaSat\n" +
                "      , ClaveSATProducto as IdProdServSAT\n" +
                "      ,UnidadMedida\n" +
                "      ,CreadoEl\n" +
                "      ,ModificadoEl\n" +
                "      ,ModificadoPor\n" +
                "      ,CreadoPor\n" +
                "    FROM CatConceptosFacturacion\n" +
                "\n" +
                "\tWHERE IdConceptoFacturacion = "+id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONObject json = new JSONObject();
        int idConceptoFacturacion;
        while(rs.next()) {
            json = new JSONObject();
            idConceptoFacturacion =  rs.getInt("IdConceptosFacturacion");
            json.put("m_nIdConceptosFacturacion", idConceptoFacturacion);
            json.put("m_sCodigo", rs.getString("Codigo"));
//            json.put("m_sClase", rs.getString("Clase"));
            json.put("m_bCalculoIngreso", rs.getBoolean("CalculoIngresos"));
            json.put("m_bActivo", rs.getBoolean("Activo"));
            json.put("m_bCalculoFlete", rs.getBoolean("CalculoFlete"));
            json.put("m_sConcepto", rs.getString("Concepto"));
            if(idConceptoFacturacion > 0) {
                json.put("arClsDetalle", obtenerListadoIdConcepto(idConceptoFacturacion,jdbcConnection));
            }
        }
        return json;
    }

    public  JSONArray obtenerListadoIdConcepto(int idConcepto, Connection jdbcConnection) throws Exception{
        String query = "SELECT Detalle.IdConceptoFacturacionImpuesto as IdConceptosFacturacionDetalle\n" +
                "      ,Detalle.IdConceptoFacturacion as IdConceptosFacturacion\n" +
                "                ,IdImpuesto\n" +
                "                ,(SELECT CASE WHEN TipoCalculo = 1 THEN 1 ELSE 0 END  from CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Trasladado\n" +
                "\t  ,(SELECT Impuesto FROM CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Impuesto\n" +
                "\t  ,(SELECT Porcentaje FROM CatImpuestos WHERE IdImpuesto = Detalle.IdImpuesto) AS Porcentaje\n" +
                "      ,Detalle.Predeterminado\n" +
                "        FROM CatConceptosFacturacionImpuestos AS Detalle\n" +
                "        WHERE IdConceptoFacturacion = "+idConcepto;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray jsonArray = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("m_bPredeterminado", rs.getBoolean("Predeterminado"));
            json.put("m_bTrasladado", rs.getBoolean("Trasladado"));
            json.put("m_nIdConceptosFacturacion", rs.getInt("IdConceptosFacturacion"));
            json.put("m_nIdConceptosFacturacionDetalle", rs.getInt("IdConceptosFacturacionDetalle"));
            json.put("m_nIdImpuesto", rs.getInt("IdImpuesto"));
            json.put("m_xPorcentaje", rs.getFloat("Porcentaje"));
            json.put("m_sImpuesto", rs.getString("Impuesto"));
            jsonArray.put(json);
        }
        return jsonArray;
    }

    public void agregarConceptosARecoleccion(RecoleccionRequest recoRequest, Statement statement) throws Exception {
        String query = "";
        query = "update CatCotizacion set IdRecoleccion = "+recoRequest.getM_nIdRecoleccion()+",Activa=1 where IdCotizacion = "+recoRequest.getM_nIdCotizacion();
        statement.executeUpdate(query);
        for (ConceptoFacturacionRequest concepto : recoRequest.getM_arrConceptos()) {
            concepto.setM_nIdCotizacion(recoRequest.getM_nIdCotizacion());
            query = "insert into CatCotizacionConceptosPQ(IdCotizador, IdConceptoFacturacion, Importe, IdImpuestoTraslada, ImporteIva,\n" +
                    "                                     IdImpuestoRetiene, ImporteRetiene, Descuento)\n" +
                    "values("+concepto.getM_nIdCotizacion()+","+concepto.getM_nIdConceptoFacturacion()+","+concepto.getM_cImporte()+","+concepto.getM_nIdImpuestoTraslada()+","+concepto.getM_cImporteIva()+","+concepto.getM_nIdImpuestoRetiene()+","+concepto.getM_cImporteRetiene()+","+concepto.getM_c_Descuento()+");";
            statement.executeUpdate(query);
        }
    }

    public void agregarConceptosAEmbarque(EmbarqueRequest embRequest, Statement statement) throws Exception {
        String query = "";
        query = "update CatCotizacion set IdEmbarque = "+embRequest.getM_nIdEmbarque()+",Activa=1 where IdCotizacion = "+embRequest.getM_nIdCotizacion();
        statement.executeUpdate(query);
        for (ConceptoFacturacionRequest concepto : embRequest.getM_arrConceptos()) {
            concepto.setM_nIdCotizacion(embRequest.getM_nIdCotizacion());
            query = "insert into CatCotizacionConceptosPQ(IdCotizador, IdConceptoFacturacion, Importe, IdImpuestoTraslada, ImporteIva,\n" +
                    "                                     IdImpuestoRetiene, ImporteRetiene, Descuento)\n" +
                    "values("+concepto.getM_nIdCotizacion()+","+concepto.getM_nIdConceptoFacturacion()+","+concepto.getM_cImporte()+","+concepto.getM_nIdImpuestoTraslada()+","+concepto.getM_cImporteIva()+","+concepto.getM_nIdImpuestoRetiene()+","+concepto.getM_cImporteRetiene()+","+concepto.getM_c_Descuento()+");";
            statement.executeUpdate(query);
        }
    }
}
