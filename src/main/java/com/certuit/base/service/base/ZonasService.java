package com.certuit.base.service.base;

import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ZonasService {
    public JSONArray getCodigoPostalZonaTarifa(int id,  Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT  " +
                "  cz.IdCPZona as m_nIdCPZona " +
                "  , cz.IdZona AS m_nIdZona " +
                "  ,cz.IdCP as m_nIdCP " +
                "  ,c.CodigoPostal AS m_sCP " +
                "  ,c.Colonia as m_sColonia " +
                "  ,c.Localidad as m_sLocalidad " +
                "  ,c.Municipio as m_sMunicipio  " +
                "  ,c.CodigoMunicipio as m_sCodigoMunicipio " +
                "  ,c.IdEstado as m_sIdEstado " +
                "  , c.ClaveColonia as m_sClaveColonia " +
                "  , c.ClaveLocalidad as m_sClaveLocalidad " +
                "  FROM CatZonasTarifasCPPQ cz " +
                "  JOIN CatCodigosPostales c on cz.IdCP = c.IdCodigoPostal " +
                "  WHERE IdZona = "+id+"";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getConceptoZonaTarifa(int id,  Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "SELECT " +
                "    IdZonaConceptos as m_nIdZonaConceptos " +
                "     ,IdZona as m_nIdZona " +
                "     ,Importe as m_cImporte " +
                "     ,IdImpuestoTraslada as m_nIdImpuestoTraslada " +
                "     ,ImporteIva as m_cImporteIva " +
                "     ,IdImpuestoRetiene as m_nIdImpuestoRetiene " +
                "     ,ImporteRetiene as m_cImporteRetiene " +
                "     ,TF.IdConceptoFacturacion as m_nIdConceptosFacturacion " +
                "     ,IdTipoCalculo as m_nIdTipoCalculo " +
                "     ,RangoMinimo as m_xnRangoMinimo " +
                "     ,RangoMaximo as m_xnRangoMaximo " +
                "     ,IdAgregadoDesde as m_nIdAgregadoDesde " +
                "     ,IdTipoMedida as m_nIdTipoMedida " +
                "     ,CF.ConceptoFacturacion AS m_sConcepto " +
                "FROM CatZonasTarifasConceptosPQ TF INNER JOIN CatConceptosFacturacion CF " +
                "ON TF.IdConceptoFacturacion = CF.IdConceptoFacturacion " +
                "WHERE IdZona = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getCPsByIdZonaOperativa(int id,  Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "EXEC usp_CatZonasOperativasCPGetByIdZonaPQ "+id+"";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("m_nIdCPZona", rs.getInt("IdCPZona"));
            json.put("m_nIdZona", rs.getInt("IdZona"));
            json.put("m_nIdCP", rs.getInt("IdCodigoPostal"));
            json.put("m_sCP", rs.getString("CodigoPostal"));
            json.put("m_sColonia", rs.getString("Colonia"));
            json.put("m_sLocalidad", rs.getString("Localidad"));
            json.put("m_sMunicipio", rs.getString("Municipio"));
            json.put("m_sCodigoMunicipio", rs.getString("CodigoMunicipio"));
            json.put("m_sIdEstado", rs.getString("IdEstado"));
            json.put("m_sClaveColonia", rs.getString("ClaveColonia"));
            json.put("m_sClaveLocalidad", rs.getString("ClaveLocalidad"));
            array.put(json);
        }
        return array;
    }
}
