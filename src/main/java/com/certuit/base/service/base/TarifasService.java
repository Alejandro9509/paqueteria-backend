package com.certuit.base.service.base;

import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class TarifasService {

    public JSONArray getDestinosByIdTarifa(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT\n" +
                "       IdTarifaDestino, \n" +
                "       IdTarifa, \n" +
                "       IdTarifaViaje,\n" +
                "       orides.IdOrigenDestino as m_nIdCiudad, \n" +
                "       OrigenDestino as m_sCiudad,\n" +
                "       IdOrigenDestino as m_nCodigo\n" +
                "FROM CatTarifaDestinosPQ destino\n" +
                "LEFT JOIN CatOrigenesDestinos orides on orides.IdOrigenDestino = destino.IdDestino\n" +
                "WHERE destino.IdTarifaViaje in (SELECT viaje.IdTarifaViaje FROM CatTarifaViajesPQ viaje " +
                "WHERE viaje.IdTarifa = " + id + " )";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getProductosByIdTarifa(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "Select \n" +
                "  pc.IdProductoTarifa as m_nIdTarifaProducto\n" +
                "  ,pc.IdTarifa as m_nIdTarifa\n" +
                "  ,pc.IdProducto as m_nIdProducto\n" +
                "  ,p.NoProducto as m_nNoProducto\n" +
                "  ,p.Descripcion as m_sDescripcion\n" +
                "  ,p.Embalaje as m_sEmbalaje\n" +
                "  ,p.IdEmbalaje as m_nIdEmbalaje\n" +
                "  ,p.Largo as m_xLargo\n" +
                "  ,p.Ancho as m_xAncho\n" +
                "  ,p.Alto as m_xAlto\n" +
                "  ,p.Peso as m_xPeso\n" +
                "  ,pc.Importe as m_cImporte\n" +
                "  ,pc.IdImpuestoTraslada as m_nIdImpuestoTraslada\n" +
                "  ,pc.ImporteIva as m_cImporteIva\n" +
                "  ,pc.IdImpuestoRetiene as m_nIdImpuestoRetiene\n" +
                "  ,pc.ImporteRetiene as m_cImporteRetiene\n" +
                "  ,p.Activo as m_bActivo\n" +
                "  ,pc.IdTarifaViaje\n" +
                "  FROM CatProductosTarifaPQ pc\n" +
                "  JOIN CatProductosPQ p ON p.IdProducto = pc.IdProducto\n" +
                "WHERE pc.IdTarifaViaje in (SELECT viaje.IdTarifaViaje FROM CatTarifaViajesPQ viaje WHERE viaje.IdTarifa = " + id + " )";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getViajesByIdTarifa(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT \n" +
                "       IdTarifaViaje, IdTarifa, IdOrigen, FleteMinimo, orides.OrigenDestino as Origen \n" +
                "FROM CatTarifaViajesPQ viaje " +
                "LEFT JOIN CatOrigenesDestinos orides on orides.IdOrigenDestino = viaje.IdOrigen\n" +
                "WHERE viaje.IdTarifa = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public void validarVigenciaTarifas(Connection jdbcConnection) throws SQLException, Exception {
        String query = "UPDATE CatTarifasRangosPQ SET Activo = IIF(Vigencia < (convert(DATE,GETDATE()))," +
                "CAST(0 AS BIT), CAST(1 AS BIT))";
        Statement statement = jdbcConnection.createStatement();
        statement.execute(query);
    }
}
