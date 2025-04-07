package com.certuit.base.service.base;

import com.certuit.base.config.APIConstants;
import com.certuit.base.util.HtmlEmail;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class EmbarqueService {
    @Autowired
    SeguimientoService seguimientoService;

    public JSONArray getPaquetesEmbarque(int id, int tipo, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT " +
                "    e.IdEmbarqueDetalle AS m_nIdEmbarqueDetalle " +
                "     , e.IdEmbarque AS m_nIdEmbarque " +
                "     , e.ValorDeclarado as m_cValorDeclarado " +
                "     , e.Tipo as m_nTipo " +
                "     , e.Descripcion as m_sDescripcion " +
                "     , e.Peso as m_xPeso " +
                "     , e.Largo as m_xLargo " +
                "     , e.Ancho as m_xAncho " +
                "     , e.Alto as m_xAlto " +
                "     , e.Volumen as m_xVolumen " +
                "     , e.IdTipoEmpaque as m_nIdTIpoEmpaque " +
                "     ,e.Observaciones as m_sObservaciones " +
                "     , e.ctd as ctd " +
                "     ,e.IdProducto AS m_nIdProducto " +
                "     ,e.ClaveSATProducto as m_nClaveSATProducto " +
                "     , e.ClaveSATUnidad as m_nClaveSATUnidad " +
                "     , e.ClaveEmbalaje as m_sClaveEmbalaje " +
                "     ,(select Nombre from CatEmbalajesPQ where IdEmbalaje = e.IdTipoEmpaque) as TipoEmbalaje " +
                "     ,(select Descripcion from CatProductosPQ p WHERE p.IdProducto = e.IdProducto ) as m_sProducto " +
                "     ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveSATProducto as varchar) and CatalogoSAT = 'c_ClaveProdServ') as m_sProductoSAT " +
                "     ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveSATUnidad as varchar) and CatalogoSAT = 'c_ClaveUnidad') as m_sUnidadSAT " +
                "     ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveEmbalaje as varchar) and CatalogoSAT = 'c_TipoEmbalaje') as m_sEmbalajeSAT " +
                "FROM ProEmbarqueDetallePQ e " +
                "WHERE IdEmbarque = " + id + " and Tipo = " + tipo;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getConceptosSATEmbarque(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT IdEmbarqueComplementoSAT as m_nIdComplementoSAT, " +
                "       IdEmbarque as m_nIdEmbarque, " +
                "       Cantidad as m_nCantidad, " +
                "       ClaveProductoServicio as m_sClaveProductoServicio, " +
                "       ClaveUnidad as m_sClaveUnidad, " +
                "       ClaveFraccionArancelaria as m_sClaveFraccionArancelaria, " +
                "       UUIDComercioExterior as m_sUUIDComercioExterior, " +
                "       EsMaterialPeligroso as m_bEsMaterialPeligroso, " +
                "       ClaveMaterialPeligroso as m_sClaveMaterialPeligroso, " +
                "       ClaveEmbalaje as m_sClaveEmbalaje, " +
                "       DescripcionEmbalaje as m_sDescripcionEmbalaje, " +
                "       Peso as m_xPeso, " +
                "       ClaveSectorCofepris," +
                "       NombreIngredienteActivo," +
                "       NombreQuimico," +
                "       DenominacionGenericaProd," +
                "       DenominacionDistintivaProd," +
                "       Fabricante," +
                "       FechaCaducidad," +
                "       LoteMedicamento," +
                "       FormaFarmaceutica," +
                "       CondicionesEspTransp," +
                "       RegistroSanitarioFolioAutorizacion," +
                "       NumeroCAS," +
                "       NumRegSanPlagCOFEPRIS," +
                "       DatosFabricante," +
                "       DatosFormulador," +
                "       DatosMaquilador," +
                "       UsoAutorizado" +
                "        ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveProductoServicio as varchar) and CatalogoSAT = 'c_ClaveProdServ') as m_sProductoServicio " +
                "        ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveUnidad as varchar) and CatalogoSAT = 'c_ClaveUnidad') as m_sUnidad " +
                "        ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveEmbalaje as varchar) and CatalogoSAT = 'c_TipoEmbalaje') as m_sTipoEmbalaje " +
                "        ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveMaterialPeligroso as varchar) and CatalogoSAT = 'c_MaterialPeligroso') as m_sMaterialPeligroso " +
                "        ,(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = " +
                "CAST(e.ClaveFraccionArancelaria as varchar) and CatalogoSAT = 'c_FraccionArancelaria') as m_sFraccionArancelaria " +
                "FROM ProEmbarqueComplementosSATPQ e " +
                "WHERE IdEmbarque = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getConceptosCotizacionEmbarque(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT IdCotizadorConcepto as m_nIdCotizadorConcepto, " +
                "       IdCotizador as m_nIdCotizador, " +
                "       IdConceptoFacturacion as m_nIdConceptoFacturacion, " +
                "       (Select ConceptoFacturacion from CatConceptosFacturacion where IdConceptoFacturacion = " +
                "cc.IdConceptoFacturacion) as m_sConcepto, " +
                "       Importe as m_cImporte," +
                "       IdImpuestoTraslada as m_nIdImpuestoTraslada," +
                "       ImporteIva as m_cImporteIva, " +
                "       IdImpuestoRetiene as m_nIdImpuestoRetiene, " +
                "       ImporteRetiene as m_cImporteRetiene, " +
                "       Descuento as m_c_Descuento " +
                "FROM CatCotizacionConceptosPQ cc " +
                "join CatCotizacion c on c.IdCotizacion = cc.IdCotizador " +
                "WHERE c.IdEmbarque = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);


    }

    public JSONArray getListadoByIdEmabrque(int id, Connection jdbcConnection) throws SQLException,Exception{
        String query = "SELECT\n" +
                " pe.IdEmbarqueDetalle " +
                "      ,pe.IdEmbarque,\n" +
                "\t  (select Nombre from CatEmbalajesPQ ce where pe.IdTipoEmpaque = ce.IdEmbalaje ) as Embalaje\n" +
                "      ,Tipo\n" +
                "      ,Descripcion\n" +
                "      ,Peso\n" +
                "      ,Largo\n" +
                "      ,Ancho\n" +
                "      ,Alto\n" +
                "      ,Volumen\n" +
                "      ,IdTipoEmpaque\n" +
                "      ,ValorDeclarado\n" +
                "      ,Observaciones\n" +
                "      ,Activo\n" +
                "      ,ctd\n" +
                "      ,IdProducto\n" +
                "      ,(select Descripcion from CatProductosPQ p WHERE p.IdProducto = pe.IdProducto ) as Producto\n" +
                "    FROM ProEmbarqueDetallePQ pe\n" +
                "\tWHERE IdEmbarque = "+id+"";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        while(rs.next()){
            json = new JSONObject();
            json.put("m_nIdEmbarqueDetalle",rs.getInt("IdEmbarqueDetalle"));
            json.put("m_nIdEmbarque",rs.getInt("IdEmbarque"));
            json.put("m_cValorDeclarado",rs.getFloat("ValorDeclarado"));
            json.put("m_nTipo",rs.getInt("Tipo"));
            json.put("m_sDescripcion",rs.getString("Descripcion"));
            json.put("m_xPeso",rs.getDouble("Peso"));
            json.put("m_xLargo",rs.getDouble("Largo"));
            json.put("m_xAncho",rs.getDouble("Ancho"));
            json.put("m_xAlto",rs.getDouble("Alto"));
            json.put("m_xVolumen",rs.getDouble("Volumen"));
            json.put("m_nIdTIpoEmpaque",rs.getInt("IdTipoEmpaque"));
            json.put("m_sObservaciones",rs.getString("Observaciones"));
            json.put("m_bActivo",rs.getBoolean("Activo"));
            json.put("ctd",rs.getInt("ctd"));
            json.put("m_sEmbalaje",rs.getString("Embalaje"));
            json.put("m_nIdProducto",rs.getInt("IdProducto"));
            json.put("m_sProducto",rs.getString("Producto"));
            array.put(json);
        }
        return array;
    }

    public void enviarCorreoEmbarque(int id, Connection jdbcConnection, String[] correos, String rfc,String mensaje) {
        try {
            String emailTemplate = HtmlEmail.htmlBody;
            String query = "select  CC.NombreCorto AS 'ClientePaga', Tracking, cco.OrigenDestino as 'Origen', " +
                    "ccd.OrigenDestino as 'Destino' from ProEmbarquePQ PIP inner join CatOrigenesDestinos cco " +
                    "on cco.IdOrigenDestino = PIP.IdCiudadOrigen inner join CatOrigenesDestinos ccd " +
                    "on ccd.IdOrigenDestino = PIP.IdCiudadOrigen inner join CatClientes CC " +
                    "on PIP.IdCliente = CC.IdCliente  where PIP.IdEmbarque = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = UtilFuctions.convertObject(rs);

            EmailService emailService = new EmailService(jdbcConnection);

            JSONArray jonArrayPaquetes = seguimientoService.getPaquetesSeguimiento(id, 2, jdbcConnection);
            String paquetesHtml = "";

            for (int i = 0; i < jonArrayPaquetes.length(); i++) {
                String template = HtmlEmail.htmlPaquetes;
                JSONObject jsonObject1 = jonArrayPaquetes.getJSONObject(i);
                template = template.replaceAll("#OBSERVACIONES",jsonObject1.getString("m_sObservaciones") );
                template = template.replaceAll("#CTD", String.valueOf(jsonObject1.getInt("m_nCantidad")));
                template = template.replaceAll("#DESCRIPCION",jsonObject1.getString("m_sDescripcion") );
                template = template.replaceAll("#ALTO", String.valueOf(jsonObject1.getDouble("m_rAlto")));
                template = template.replaceAll("#ANCHO", String.valueOf(jsonObject1.getDouble("m_rAncho")));
                template = template.replaceAll("#LARGO", String.valueOf(jsonObject1.getDouble("m_rLargo")));
                template = template.replaceAll("#PESO", String.valueOf(jsonObject1.getDouble("m_rPeso")));
                template = template.replaceAll("#NUM_PAQUETE", String.valueOf(i + 1));
                paquetesHtml += template;
            }

            emailTemplate = emailTemplate.replaceAll("#CLIENTE", jsonObject.getString("ClientePaga"));
            emailTemplate = emailTemplate.replaceAll("#TRACKING", jsonObject.getString("Tracking"));
            emailTemplate = emailTemplate.replaceAll("#MENSAJE",mensaje );

            emailTemplate = emailTemplate.replaceAll("#ORIGEN", jsonObject.getString("Origen"));
            emailTemplate = emailTemplate.replaceAll("#DESTINO", jsonObject.getString("Destino"));
            emailTemplate = emailTemplate.replaceAll("#PAQUETES", paquetesHtml);
            emailTemplate = emailTemplate.replaceAll("#LINK_TRACKING", APIConstants.PUBLIC_URL + "/app/applications/" +rfc + "/tracking/" + jsonObject.getString("Tracking"));

            emailService.sendMail(emailTemplate, "Estatus de seguimiento de paquete ", correos, new byte[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
