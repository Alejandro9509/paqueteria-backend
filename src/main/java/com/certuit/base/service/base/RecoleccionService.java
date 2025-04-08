package com.certuit.base.service.base;

import com.certuit.base.config.APIConstants;
import com.certuit.base.endpoint.ProductoRest;
import com.certuit.base.util.HtmlEmail;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.certuit.base.util.UtilFuctions.convertArray;
import static com.certuit.base.util.UtilFuctions.convertObject;

@Service
public class RecoleccionService {

    @Autowired
    SeguimientoService seguimientoService;

    @Autowired
    ProductoRest productoRest;

    public void enviarCorreoRecoleccion(int id, Connection jdbcConnection, String[] correos,
                                        String rfc,String mensaje) {

        try {
            String emailTemplate = HtmlEmail.htmlBody;
            String query = "select  CC.NombreCorto AS 'ClientePaga', Tracking, cco.OrigenDestino as 'Origen', " +
                    "ccd.OrigenDestino as 'Destino' from ProRecoleccionPQ PIP " +
                    "inner join CatOrigenesDestinos cco on cco.IdOrigenDestino = PIP.IdCiudadOrigen " +
                    "inner join CatOrigenesDestinos ccd on ccd.IdOrigenDestino = PIP.IdCiudadDestino " +
                    "inner join CatClientes CC on PIP.IdCliente = CC.IdCliente  where PIP.IdRecoleccion = " + id;
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            JSONObject jsonObject = convertObject(rs);

            EmailService emailService = new EmailService(jdbcConnection);

            JSONArray jonArrayPaquetes = seguimientoService.getPaquetesSeguimiento(id, 1, jdbcConnection);
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
            emailTemplate = emailTemplate.replaceAll("#LINK_TRACKING", APIConstants.PUBLIC_URL
                    + "/app/applications/" +rfc + jsonObject.getString("Tracking") + "/tracking");

            emailService.sendMail(emailTemplate, "Estatus de seguimiento de paquete ", correos, new byte[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getPaquetesRecoleccion(int id, Connection jdbcConnection,String rfc)
            throws Exception {
        String query = "SELECT\n" +
                "e.IdPaquete as m_nIdPaquete\n" +
                ",e.Peso as m_rPeso\n" +
                ",e.Largo as m_rLargo\n" +
                ",e.Ancho as m_rAncho\n" +
                ",e.Alto as m_rAlto\n" +
                ",e.Volumen as m_rVolumen\n" +
                ",e.IdTipoEmbalaje as m_nIdTipoEmbalaje\n" +
                ",embalaje.Nombre as m_sTipoEmbalaje\n" +
                ",e.ValorDeclarado as m_cyValorDeclarado\n" +
                ",e.Descripcion as m_sDescripcion\n" +
                ",e.Cantidad as m_nCantidad\n" +
                ",e.Observaciones as m_sObservaciones\n" +
                ",IdRecoleccion as m_nIdRecoleccion\n" +
                ",e.IdProducto as m_nIdProducto\n" +
                ",prod.Descripcion as m_sProducto\n" +
                ",e.IdTipo as m_nIdTipo\n" +
                ",IIF(IdTipo=1,'Sobre', 'Paquete') as m_sTipo\n" +
                ",ClaveSATProducto as m_sClaveSATProducto\n" +
                ",ClaveSATUnidad as m_sClaveSATUnidad\n" +
                ",ClaveEmbalaje as m_sClaveEmbalaje\n" +
                ",gs1.Descripcion as m_nProductoSAT\n" +
                ",gs2.Descripcion as m_nUnidadSAT\n" +
                ",gs3.Descripcion as m_sEmbalajeSAT\n" +
                "\n" +
                "FROM ProRecoleccionPaquetePQ e\n" +
                "LEFT JOIN CatGeneralesSAT gs1 ON gs1.ClaveSAT = CAST(e.ClaveSATProducto as varchar) " +
                "and gs1.CatalogoSAT = 'c_ClaveProdServ'\n" +
                "LEFT JOIN CatGeneralesSAT gs2 ON gs2.ClaveSAT = CAST(e.ClaveSATProducto as varchar) " +
                "and gs2.CatalogoSAT = 'c_ClaveUnidad'\n" +
                "LEFT JOIN CatGeneralesSAT gs3 ON gs3.ClaveSAT = CAST(e.ClaveSATProducto as varchar) " +
                "and gs3.CatalogoSAT = 'c_TipoEmbalaje'\n" +
                "LEFT JOIN CatEmbalajesPQ embalaje ON embalaje.IdEmbalaje = e.IdTipoEmbalaje\n" +
                "LEFT JOIN CatProductosPQ prod ON prod.IdProducto = e.IdProducto\n" +
                "WHERE IdRecoleccion = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        JSONArray jsonArray = convertArray(rs);
        for (int i = 0; i < jsonArray.length(); i++){
            query = "SELECT " +
                    "IdProducto as m_nIdProducto, " +
                    "NoProducto as m_nNoProducto, " +
                    "Descripcion as m_sDescripcion " +
                    "FROM CatProductosPQ WHERE IdProducto="+jsonArray.getJSONObject(i).getInt("m_nIdProducto");
            statement = jdbcConnection.createStatement();
            rs = statement.executeQuery(query);
            jsonArray.getJSONObject(i).put("producto", convertObject(rs));
        }
        return jsonArray;
    }

    public JSONArray getConceptosSATRecoleccion(int id, Connection jdbcConnection)
            throws Exception {
        String query = "SELECT IdRecoleccionComplementoSAT as id,\n" +
                "       IdRecoleccion as m_nIdRecoleccion,\n" +
                "       Cantidad as cantidad,\n" +
                "       ClaveProductoServicio as claveProducto,\n" +
                "       ClaveUnidad as claveUnidad,\n" +
                "       ClaveFraccionArancelaria as claveFraccion,\n" +
                "       UUIDComercioExterior as comercioExterior,\n" +
                "       EsMaterialPeligroso as esPeligroso,\n" +
                "       ClaveMaterialPeligroso as claveMaterialPeligroso,\n" +
                "       ClaveEmbalaje as claveEmbalaje,\n" +
                "       DescripcionEmbalaje as descripcionEmbalajeSAT,\n" +
                "       Peso as peso\n" +
                "       ,ClaveSectorCofepris," +
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
                "       ,gs1.Descripcion as ProductoSAT\n" +
                "       ,gs2.Descripcion as UnidadSAT\n" +
                "       ,gs3.Descripcion as embalajeSAT\n" +
                "       ,gs4.Descripcion as materialPeligrosoSAT\n" +
                "       ,gs5.Descripcion as fraccionSAT\n" +
                "FROM ProRecoleccionComplementosSATPQ e\n" +
                "LEFT JOIN CatGeneralesSAT gs1 ON gs1.ClaveSAT = CAST(e.ClaveProductoServicio as varchar) " +
                "and gs1.CatalogoSAT = 'c_ClaveProdServ'\n" +
                "LEFT JOIN CatGeneralesSAT gs2 ON gs2.ClaveSAT = CAST(e.ClaveUnidad as varchar) " +
                "and gs2.CatalogoSAT = 'c_ClaveUnidad'\n" +
                "LEFT JOIN CatGeneralesSAT gs3 ON gs3.ClaveSAT = CAST(e.ClaveEmbalaje as varchar) " +
                "and gs3.CatalogoSAT = 'c_TipoEmbalaje'\n" +
                "LEFT JOIN CatGeneralesSAT gs4 ON gs4.ClaveSAT = CAST(e.ClaveMaterialPeligroso as varchar) " +
                "and gs4.CatalogoSAT = 'c_MaterialPeligroso'\n" +
                "LEFT JOIN CatGeneralesSAT gs5 ON gs5.ClaveSAT = CAST(e.ClaveFraccionArancelaria as varchar) " +
                "and gs5.CatalogoSAT = 'c_FraccionArancelaria'\n" +
                "WHERE IdRecoleccion = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return convertArray(rs);
    }

    public JSONArray getConceptosCotizacionRecoleccion(int id, Connection jdbcConnection)
            throws Exception {
        String query = "SELECT pg.IdCotizadorConcepto\n" +
                "     ,pg.IdCotizador as m_nIdCotizacion\n" +
                "     ,pg.IdConceptoFacturacion as m_nIdConceptoFacturacion\n" +
                "     ,pg.Importe as m_cImporte\n" +
                "     ,pg.IdImpuestoTraslada as m_nIdImpuestoTraslada\n" +
                "     ,pg.ImporteIva as m_cImporteIva\n" +
                "     ,pg.IdImpuestoRetiene as m_nIdImpuestoRetiene\n" +
                "     ,pg.ImporteRetiene as m_cImporteRetiene\n" +
                "     ,pg.Descuento as m_c_Descuento\n" +
                "     ,pg.Total as m_cTotal\n" +
                "     ,cf1.ConceptoFacturacion as m_sConcepto\n" +
                "     ,cf2.IncluirCalculoIngresosLiquidacion as Liquidacion\n" +
                "     ,cf3.IncluirCalculoLiquidacionPorcentajeSobreImpFlete as Flete\n" +
                "     ,c1.Porcentaje as PorcentajeRetiene\n" +
                "     ,c2.Porcentaje as PorcentajeTraslada\n" +
                "FROM CatCotizacionConceptosPQ pg\n" +
                "join CatCotizacion c on c.IdCotizacion = pg.IdCotizador \n" +
                "LEFT JOIN CatConceptosFacturacion cf1 ON cf1.IdConceptoFacturacion = pg.IdConceptoFacturacion\n" +
                "LEFT JOIN CatConceptosFacturacion cf2 ON cf2.IdConceptoFacturacion = pg.IdConceptoFacturacion\n" +
                "LEFT JOIN CatConceptosFacturacion cf3 ON cf3.IdConceptoFacturacion = pg.IdConceptoFacturacion\n" +
                "LEFT JOIN CatImpuestos c1 ON c1.IdImpuesto = pg.IdImpuestoRetiene\n" +
                "LEFT JOIN CatImpuestos c2 ON c2.IdImpuesto = pg.IdImpuestoTraslada\n" +
                "WHERE pg.IdCotizador = (select top 1 c.IdCotizacion from CatCotizacion c where c.IdRecoleccion = " +
                id +" and c.Activa = 1  order by IdCotizacion desc)";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return convertArray(rs);
    }
}
