package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.GuiaRequest;
import com.certuit.base.domain.request.base.PaqueteRequest;
import com.certuit.base.domain.request.base.ProGuiaConceptosRequest;
import com.certuit.base.util.DBConection;
import com.certuit.base.util.UtilFuctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.certuit.base.util.UtilFuctions.convertArray;

@Service
public class GuiaService {
    @Autowired
    ReportServices reportServices;
    @Autowired
    ConceptosService conceptosService;
    @Autowired
    GuiaService guiaService;

    public JSONArray getGuiasPaquetes(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = "SELECT  " +
                "    pe.IdEmbarqueDetalle as m_nIdEmbarqueDetalle  " +
                "      ,pe.IdEmbarque as m_nIdEmbarque,  " +
                "    (select Nombre from CatEmbalajesPQ ce where pe.IdTipoEmpaque = ce.IdEmbalaje ) as m_sEmbalaje  " +
                "      ,Tipo as m_nTipo  " +
                "      ,Descripcion as m_sDescripcion  " +
                "      ,Peso as m_xPeso  " +
                "      ,Largo as m_xLargo  " +
                "      ,Ancho as m_xAncho  " +
                "      ,Alto as m_xAlto  " +
                "      ,Volumen as m_xVolumen  " +
                "      ,IdTipoEmpaque as m_nIdTIpoEmpaque  " +
                "      ,ValorDeclarado as m_cValorDeclarado  " +
                "      ,Observaciones as m_sObservaciones  " +
                "      ,Activo as m_bActivo  " +
                "      ,ctd as ctd  " +
                "      ,IdProducto as m_nIdProducto  " +
                "      ,(select Descripcion from CatProductosPQ p WHERE p.IdProducto = pe.IdProducto ) as m_sProducto  " +
                "    FROM ProEmbarqueDetallePQ pe  " +
                "  WHERE IdEmbarque = " + id;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return UtilFuctions.convertArray(rs);
    }

    public JSONArray convertGuia(ResultSet resultSet, Connection jdbcConnection) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {

            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));


            obj.put("m_arrClsDetalle",getGuiasPaquetes(obj.getInt("m_nIdEmbarque"), jdbcConnection) );
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public void enviarCorreoFacturacion(String emailTemplate, int id,Connection jdbcConnection,
                                        String[] correos,Boolean correoDefault,String rfc, boolean esRecoleccion)  {
        try {
            EmailService emailService = new EmailService(jdbcConnection);
            String query = "";
            if(esRecoleccion) {
                query = "select  CC.NombreFiscal AS 'ClientePaga', CC.CorreoElectronico,FORMAT(G.FechaTimbrado," +
                        "'dd/MMM/yyyy HH:mm') as 'FechaTimbrado', G.FolioRecoleccion as 'Folio'," +
                        "G.XMLTraslada as 'XMLTraslada',G.FolioFiscalUUID  from ProRecoleccionPQ G " +
                        "left join CatClientes CC on G.IdCliente = CC.IdCliente  where G.IdRecoleccion =  " + id;
            }else {
                query = "select  CC.NombreFiscal AS 'ClientePaga', CC.CorreoElectronico,FORMAT(G.FechaTimbrado," +
                        "'dd/MMM/yyyy HH:mm') as 'FechaTimbrado', G.FolioGuia as 'Folio'," +
                        "G.XMLTraslada as 'XMLTraslada',G.FolioFiscalUUID  " +
                        "from ProGuiaPQ G left join ProEmbarquePQ PEP on G.IdEmbarque = PEP.IdEmbarque " +
                        "left join CatClientes CC on PEP.IdCliente = CC.IdCliente  where G.IdGuia = " + id;
            }
            Statement statement = jdbcConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
                List<File> fileList = new ArrayList<>();
                JSONObject jsonObject = UtilFuctions.convertObject(rs);
                String emailFormat = UtilFuctions.converTemplate(jsonObject, emailTemplate);
                if (esRecoleccion){
                    fileList.add(reportServices.generateReporCFDIRecoleccion(1, id,rfc,
                            jsonObject.getString("FolioFiscalUUID")));
                }else {
                    fileList.add(reportServices.generateReporCFDIGuia(1, id,rfc,
                            jsonObject.getString("FolioFiscalUUID")));
                }
                fileList.add(UtilFuctions.convertXMLToFile(jsonObject.getString("XMLTraslada"),
                        jsonObject.getString("FolioFiscalUUID") ));

                if (correoDefault){
                    emailService.sendMail(emailFormat, "CFDI " + jsonObject.getString("FolioFiscalUUID"),
                            jsonObject.getString("CorreoElectronico"), fileList);
                    Thread.sleep(30000);

                }
                for (String correo: correos) {
                    emailService.sendMail(emailFormat, "CFDI " + jsonObject.getString("FolioFiscalUUID"),
                            correo, fileList);
                    Thread.sleep(30000);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONArray getGuiasUltimaMilla(Connection jdbcConnection, int idParadaUltimaMilla)
            throws SQLException, Exception {
        String query = "EXEC usp_ProIdUltimaMillaGetPaquetesPQ " + idParadaUltimaMilla;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray jsonArray = anidarArray(rs,jdbcConnection);
        return jsonArray;
            /*
            * guia is ClsPaquetesUltimaMilla
		    guia.m_nId = dsConsulta.m_nIdPaquete

		    guia.m_sFechaRecoleccionCita = DateToString(dsConsulta.m_sFechaRecoleccionCita, "DD-MM-YYYY")
			guia.m_sHoraCitarRecoleccionMinima = TimeToString(dsConsulta.m_sHoraCitarRecoleccionMinima, "HH:MM AP")
			guia.m_sHoraCitaRecoleccionMaxima =  TimeToString(dsConsulta.m_sHoraCitaRecoleccionMaxima, "HH:MM AP")

		    guia.m_sFechaEmbarqueCita = DateToString(dsConsulta.m_sFechaEmbarqueCita, "DD-MM-YYYY")
			guia.m_sHoraEmbarqueCitaMinima = TimeToString(dsConsulta.m_sHoraEmbarqueCitaMinima, "HH:MM AP")
			guia.m_sHoraEmbarqueCitaMaxima = TimeToString(dsConsulta.m_sHoraEmbarqueCitaMaxima, "HH:MM AP")


			guia.m_parrPaquetes = ClsProRecoleccionPaquete::GetListadoPaquetesByIdRecoleccion(guia.m_nId)
			guia.m_parrSobres =  ClsProRecoleccionSobre::GetListadoSobresByIdRecoleccion(guia.m_nId)
		    guia.m_arrPaquetes = ClsProEmbarqueDetalle::GetListadoByIdGuiaTipo(guia.m_nId,2)
			guia.m_arrSobres = ClsProEmbarqueDetalle::GetListadoByIdGuiaTipo(guia.m_nId,1)
			guia.m_arrImagenes = ClsImagenes::GetImagenPaquete(guia.m_nId,guia.m_bEsRecoleccion )*/
    }

    public JSONArray getPaquetesRecoleccionUltimaMilla(int idRecoleccion,Connection jdbcConnection)
            throws SQLException, Exception {
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
                ",(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = CAST(e.ClaveSATProducto as varchar) " +
                "and CatalogoSAT = 'c_ClaveProdServ') as m_nProductoSAT\n" +
                ",(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = CAST(e.ClaveSATUnidad as varchar) " +
                "and CatalogoSAT = 'c_ClaveUnidad') as m_nUnidadSAT\n" +
                ",(select gs.Descripcion from CatGeneralesSAT gs where ClaveSAT = CAST(e.ClaveEmbalaje as varchar) " +
                "and CatalogoSAT = 'c_TipoEmbalaje') as m_sEmbalajeSAT\n" +
                "\n" +
                "FROM ProRecoleccionPaquetePQ e\n" +
                "WHERE IdRecoleccion = " + idRecoleccion;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);

            /*
            * guia is ClsPaquetesUltimaMilla
		    guia.m_nId = dsConsulta.m_nIdPaquete

		    guia.m_sFechaRecoleccionCita = DateToString(dsConsulta.m_sFechaRecoleccionCita, "DD-MM-YYYY")
			guia.m_sHoraCitarRecoleccionMinima = TimeToString(dsConsulta.m_sHoraCitarRecoleccionMinima, "HH:MM AP")
			guia.m_sHoraCitaRecoleccionMaxima =  TimeToString(dsConsulta.m_sHoraCitaRecoleccionMaxima, "HH:MM AP")

		    guia.m_sFechaEmbarqueCita = DateToString(dsConsulta.m_sFechaEmbarqueCita, "DD-MM-YYYY")
			guia.m_sHoraEmbarqueCitaMinima = TimeToString(dsConsulta.m_sHoraEmbarqueCitaMinima, "HH:MM AP")
			guia.m_sHoraEmbarqueCitaMaxima = TimeToString(dsConsulta.m_sHoraEmbarqueCitaMaxima, "HH:MM AP")


			guia.m_parrPaquetes = ClsProRecoleccionPaquete::GetListadoPaquetesByIdRecoleccion(guia.m_nId)
			guia.m_parrSobres =  ClsProRecoleccionSobre::GetListadoSobresByIdRecoleccion(guia.m_nId)
		    guia.m_arrPaquetes = ClsProEmbarqueDetalle::GetListadoByIdGuiaTipo(guia.m_nId,2)
			guia.m_arrSobres = ClsProEmbarqueDetalle::GetListadoByIdGuiaTipo(guia.m_nId,1)
			guia.m_arrImagenes = ClsImagenes::GetImagenPaquete(guia.m_nId,guia.m_bEsRecoleccion )*/
    }

    public JSONArray getPaquetesGuiaUltimaMilla(int idParadaGuia, Connection jdbcConnection) throws SQLException,
            Exception {
        String query = "declare @EsEntregaParcial bit = 0, @idGuia int = 0, @idParadaGuia int ="+idParadaGuia+"\n" +
                "select @EsEntregaParcial = ISNULL(EsEntregaParcial,0), @idGuia = m_nIdGuia " +
                "from ProParadaUltimaMillaGuiasPQ where IdParadaGuia = @idParadaGuia\n" +
                "if @EsEntregaParcial = 0 begin \n" +
                "    SELECT \n" +
                "        e.IdEmbarqueDetalle as m_nIdEmbarqueDetalle\n" +
                "         ,e.IdEmbarque as m_nIdEmbarque\n" +
                "         ,g.IdGuia as m_nIdGuia\n" +
                "         --,e.Tipo as m_nTipo \n" +
                "         ,e.Descripcion as m_sDescripcion \n" +
                "         ,e.Observaciones as m_sObservaciones \n" +
                "         ,Cantidad = e.ctd\n" +
                "    FROM ProEmbarqueDetallePQ e\n" +
                "    Inner join ProGuiaPQ g on g.IdEmbarque = e.IdEmbarque\n" +
                "    where g.IdGuia = @idGuia\n" +
                "end \n" +
                "if @EsEntregaParcial = 1 begin \n" +
                "    select paqueteParcial.IdPaquete as m_nIdEmbarqueDetalle, \n" +
                "           paqueteGuia.IdEmbarque as m_nIdEmbarque,\n" +
                "           embarque.IdGuia as m_nIdGuia,\n" +
                "           paqueteGuia.Descripcion as m_sDescripcion, \n" +
                "           paqueteGuia.Observaciones as m_sObservaciones, \n" +
                "           (select count(*) \n" +
                "           from ProUltimaMillaGuiasPaquetesParcialesPQ paqueteParcial2\n" +
                "                    inner Join ProEmbarqueDetallePQ paqueteGuia2 " +
                "on paqueteGuia2.IdEmbarqueDetalle = paqueteParcial2.IdPaquete\n" +
                "           where  paqueteParcial2.IdParadaGuia = @idParadaGuia) as Cantidad \n" +
                "    from ProUltimaMillaGuiasPaquetesParcialesPQ paqueteParcial\n" +
                "             inner Join ProEmbarqueDetallePQ paqueteGuia " +
                "on paqueteGuia.IdEmbarqueDetalle = paqueteParcial.IdPaquete\n" +
                "             inner Join ProEmbarquePQ embarque on embarque.IdEmbarque = paqueteGuia.IdEmbarque\n" +
                "    where  paqueteParcial.IdParadaGuia = @idParadaGuia\n" +
                "    group by paqueteParcial.IdPaquete, embarque.IdGuia,paqueteGuia.IdEmbarque, " +
                "paqueteGuia.Descripcion, paqueteGuia.Observaciones, paqueteGuia.ctd\n" +
                "end";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
    }

    public JSONArray getImagenesGuiaUltimaMilla(int idGuia, boolean esRecoleccion,Connection jdbcConnection) throws SQLException, Exception {
        int esRec;
        if(esRecoleccion) {
            esRec = 1;
        } else {
            esRec = 0;
        }
        String query = "SELECT ImagenNombreArchivo as m_sImagen, Descripcion as m_sDescripcion, Tipo as m_nTipoArchivo " +
                "FROM CatParadasImagenes where IdGuia = "+idGuia+" AND EsRecoleccion = " + esRec;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return UtilFuctions.convertArray(rs);
            /*
            * guia is ClsPaquetesUltimaMilla
		    guia.m_nId = dsConsulta.m_nIdPaquete

		    guia.m_sFechaRecoleccionCita = DateToString(dsConsulta.m_sFechaRecoleccionCita, "DD-MM-YYYY")
			guia.m_sHoraCitarRecoleccionMinima = TimeToString(dsConsulta.m_sHoraCitarRecoleccionMinima, "HH:MM AP")
			guia.m_sHoraCitaRecoleccionMaxima =  TimeToString(dsConsulta.m_sHoraCitaRecoleccionMaxima, "HH:MM AP")

		    guia.m_sFechaEmbarqueCita = DateToString(dsConsulta.m_sFechaEmbarqueCita, "DD-MM-YYYY")
			guia.m_sHoraEmbarqueCitaMinima = TimeToString(dsConsulta.m_sHoraEmbarqueCitaMinima, "HH:MM AP")
			guia.m_sHoraEmbarqueCitaMaxima = TimeToString(dsConsulta.m_sHoraEmbarqueCitaMaxima, "HH:MM AP")


			guia.m_parrPaquetes = ClsProRecoleccionPaquete::GetListadoPaquetesByIdRecoleccion(guia.m_nId)
			guia.m_parrSobres =  ClsProRecoleccionSobre::GetListadoSobresByIdRecoleccion(guia.m_nId)
		    guia.m_arrPaquetes = ClsProEmbarqueDetalle::GetListadoByIdGuiaTipo(guia.m_nId,2)
			guia.m_arrSobres = ClsProEmbarqueDetalle::GetListadoByIdGuiaTipo(guia.m_nId,1)
			guia.m_arrImagenes = ClsImagenes::GetImagenPaquete(guia.m_nId,guia.m_bEsRecoleccion )*/
    }

    public  JSONArray anidarArray(ResultSet resultSet, Connection jdbcConnection) throws Exception {
        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < columns; i++)
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

            obj.put("m_nId", obj.getInt("m_nIdPaquete"));
            if (obj.getBoolean("m_bEsRecoleccion")){
                obj.put("m_parrPaquetes",getPaquetesRecoleccionUltimaMilla(obj.getInt("m_nId"), jdbcConnection) );
            }else{
                obj.put("m_arrPaquetes",getPaquetesGuiaUltimaMilla(obj.getInt("m_nIdParadaGuia"), jdbcConnection) );
            }
            obj.put("m_arrImagenes",getImagenesGuiaUltimaMilla(obj.getInt("m_nId"),
                    obj.getBoolean("m_bEsRecoleccion"), jdbcConnection) );
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public JSONArray getListadoByGuiaId(int id, Connection jdbcConnection) throws SQLException, Exception {
        String query = " select *\n" +
                "      ,(select ConceptoFacturacion from  CatConceptosFacturacion cf where cf.IdConceptoFacturacion " +
                "= pg.IdConceptoFacturacion) as Concepto\n" +
                "\t  ,(select IncluirCalculoIngresosLiquidacion from  CatConceptosFacturacion cf " +
                "where cf.IdConceptoFacturacion = pg.IdConceptoFacturacion) as Liquidacion\n" +
                "      ,(select IncluirCalculoLiquidacionPorcentajeSobreImpFlete from  CatConceptosFacturacion cf " +
                "where cf.IdConceptoFacturacion = pg.IdConceptoFacturacion) as Flete\n" +
                "\t  ,(select Porcentaje from  CatImpuestos c " +
                "where c.IdImpuesto = pg.IdImpuestoRetiene) as PorcentajeRetiene\n" +
                "      ,(select Porcentaje from  CatImpuestos c " +
                "where c.IdImpuesto = pg.IdImpuestoTraslada) as PorcentajeTraslada\n" +
                "\t  from ProGuiaConceptoPQ pg\n" +
                "      \n" +
                "\tWHERE IdGuia = "+id+"";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = new JSONArray();
        JSONObject json;
        double porcentajeRetiene;
        double porcentajeTraslada;
        int idConceptoFacturacion;
        while(rs.next()) {
            json = new JSONObject();
            porcentajeRetiene = 0;
            porcentajeTraslada = 0;
            idConceptoFacturacion = 0;
            json.put("m_bActivo", rs.getBoolean("Activo"));
            json.put("m_c_Descuento", rs.getFloat("Descuento"));
            json.put("m_dtCreadoEl", rs.getTimestamp("CreadoEl"));
            json.put("m_dtModificadoEl", rs.getTimestamp("MOdificadoEl"));
            json.put("m_nCreadoPor", rs.getInt("CreadoPor"));
            json.put("m_cImporte", rs.getFloat("Importe"));
            json.put("m_cImporteIva", rs.getFloat("ImporteIva"));
            json.put("m_cImporteRetiene", rs.getFloat("ImporteRetiene"));
            idConceptoFacturacion = rs.getInt("IdConceptoFacturacion");
            json.put("m_nIdConceptoFacturacion",idConceptoFacturacion);
            json.put("m_nIdImpuestoRetiene", rs.getInt("IdImpuestoRetiene"));
            json.put("m_nIdImpuestoTraslada", rs.getInt("IdImpuestoTraslada"));
            json.put("m_nIdGuiaConcepto", rs.getInt("IdGuiaConcepto"));
            json.put("m_nIdGuia", rs.getInt("IdGuia"));
            json.put("m_nModificadoPor", rs.getInt("ModificadoPor"));
            json.put("m_cTotal", rs.getFloat("Total"));
            json.put("m_sConcepto", rs.getString("Concepto"));
            json.put("m_bIncluirFlete", rs.getBoolean("Flete"));
            json.put("m_bIncluirLiquidacion", rs.getBoolean("Liquidacion"));
            json.put("conceptoFacturacion",
                    conceptosService.conceptosFacturacion(idConceptoFacturacion,jdbcConnection));
            porcentajeRetiene = rs.getDouble("PorcentajeRetiene");
            porcentajeTraslada = rs.getDouble("PorcentajeTraslada");
            if(porcentajeRetiene>0){
                json.put("m_cPorcentajeRetiene", porcentajeRetiene/100);
            }else{
                json.put("m_cPorcentajeRetiene", porcentajeRetiene);
            }
            if(porcentajeTraslada>0){
                json.put("m_cPorcentajeTraslada", porcentajeTraslada/100);
            }else{
                json.put("m_cPorcentajeTraslada", porcentajeTraslada);
            }

            array.put(json);
        }
        return array;
    }

    public JSONArray getGuiasByIdCorte(int idCorte,Connection jdbcConnection) throws SQLException, Exception {
        String query = "EXEC usp_ProCorteCajaGuiasGetByIdCortePQ_762 "+idCorte;
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONArray array = convertArray(rs);
        return array;
    }

    public Boolean agregarConceptosGuia(ProGuiaConceptosRequest conceptos, Connection jdbcConnection)
            throws SQLException, Exception {
        String query = "EXEC usp_ProGuiaConceptosAgregarPQ "+conceptos.getM_nIdGuia()+","
                +conceptos.getM_nIdConceptosFacturacion()+"," +
                ""+conceptos.getM_cImporte()+","+conceptos.getM_nIdImpuestoTraslada()+","
                +conceptos.getM_cImporteIva()+"," +
                ""+ conceptos.getM_nIdImpuestoRetiene()+","+conceptos.getM_cImporteRetiene()
                +",null,"+conceptos.getM_nCreadoPor()+"," +
                "null,"+conceptos.getM_nModificadoPor()+","+(conceptos.getM_bActivo()?1:0)
                +","+conceptos.getM_cDescuento()+"";
        Boolean resultado = false;
        Statement statement = jdbcConnection.createStatement();
        try {
            statement.executeUpdate(query);
            resultado = true;
        }catch (Exception e){
            resultado = false;
            throw e;
        }
        return resultado;
    }

    public Boolean eliminarGuia(int idGuia,Connection jdbcConnection)throws SQLException, Exception {
        String query = "EXEC usp_ProGuiaEliminarPQ "+idGuia+"";

        Statement statement = jdbcConnection.createStatement();
        Boolean resultado = false;
        try {
            statement.executeUpdate(query);
            resultado = true;
        }catch (Exception e){
            resultado = false;
            throw e;
        }

        return resultado;
    }

    public void enviarCorreoTracking(Connection jdbcConnection, Statement statement, int nIdGuia, String sRFC)
            throws Exception {
        try{
            GuiaRequest guiaRequest = new GuiaRequest();
            guiaRequest.setM_nIdGuia(nIdGuia);
            String query = "SELECT Tracking,FolioGuia FROM ProGuiaPQ WHERE IdGuia = "+ guiaRequest.getM_nIdGuia();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                guiaRequest.setM_nTracking(rs.getString("Tracking"));
                guiaRequest.setM_sFolioGuia(rs.getString("FolioGuia"));
            }
            query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ false, '"+guiaRequest.getM_nTracking()+"'";
            rs = statement.executeQuery(query);
            JSONObject jsonGuia = UtilFuctions.convertObject(rs);
            String tipoServicio="";
            if (jsonGuia != null){
                guiaRequest.setM_sCorreoDestinatario(jsonGuia.getString("m_sCorreoDestinatario"));
                guiaRequest.setM_sDomicilioDestinatario(
                        jsonGuia.getBoolean("m_bEntregaDiferenteDomicilio") ?
                                jsonGuia.getString("m_sDomicilioDetalleEntrega") :
                                rs.getString("m_sDomicilioDestinatario"));
                guiaRequest.setM_dFecha(jsonGuia.getString("m_dFechaRegistro"));
                guiaRequest.setM_nidTipoServicio(jsonGuia.getInt("m_nIdTipoServicio"));
                if(guiaRequest.getM_nidTipoServicio()==1)
                {
                    tipoServicio="Consolidado";
                }
                else{
                    tipoServicio="Paquetería";
                }                guiaRequest.setM_arrPaquetes(getPaquetesByGuia(statement, guiaRequest.getM_nIdGuia()));
            }

            query = "SELECT NombreFiscal,Logo FROM SisParametros";
            rs = statement.executeQuery(query);
            jsonGuia = UtilFuctions.convertObject(rs);
            guiaRequest.setM_sNombreCliente(jsonGuia.getString("NombreFiscal"));
            String logo=jsonGuia.getString("Logo").replace("\r\n","");
            logo=logo.replace("=","");
            query = "SELECT top 1 FormatoCorreo FROM SisParametrosPQ";
            rs = statement.executeQuery(query);

            String plantillaCorreo = "";
            while(rs.next()){
                plantillaCorreo = rs.getString("FormatoCorreo");
            }

            plantillaCorreo = plantillaCorreo.replace("#LINK_TRACKING", DBConection.getEnviromentURL()
                    +"app/applications/"+sRFC+"/"+guiaRequest.getM_nTracking()+"/tracking");
            plantillaCorreo = plantillaCorreo.replace("#TRACKING", guiaRequest.getM_nTracking());
            plantillaCorreo = plantillaCorreo.replace("#IMAGEN", "cid:logoImage");
            plantillaCorreo = plantillaCorreo.replace("#CLIENTE", guiaRequest.getM_sNombreCliente());
            plantillaCorreo = plantillaCorreo.replace( "#DIRECCION_ENTREGA",  guiaRequest.getM_sDomicilioDestinatario() );
            plantillaCorreo = plantillaCorreo.replace( "#FOLIO",  guiaRequest.getM_sFolioGuia() );
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            plantillaCorreo = plantillaCorreo.replace( "#FECHA",  guiaRequest.getM_dFecha());
            plantillaCorreo = plantillaCorreo.replace( "#TIPO_SERVICIO", tipoServicio );

            int contador = 1;
            String paquetesConcat = "";
            for (PaqueteRequest paquete : guiaRequest.getM_arrPaquetes()) {
                String sPaquete = UtilFuctions.getPlantillaPaquetesCorreo();
                sPaquete = sPaquete.replace("#NUM_PAQUETE", String.valueOf(contador));
                sPaquete = sPaquete.replace("#PESO", String.valueOf(paquete.getM_xPeso()));
                sPaquete = sPaquete.replace("#LARGO", String.valueOf(paquete.getM_xLargo()));
                sPaquete = sPaquete.replace("#ANCHO", String.valueOf(paquete.getM_xAncho()));
                sPaquete = sPaquete.replace("#ALTO", String.valueOf(paquete.getM_xAlto()));
                sPaquete = sPaquete.replace("#DESCRIPCION", String.valueOf(paquete.getM_sDescripcion()));
                sPaquete = sPaquete.replace("#CTD", String.valueOf(paquete.getCtd()));
                sPaquete = sPaquete.replace("#OBSERVACIONES", paquete.getM_sObservaciones());
                paquetesConcat = paquetesConcat.concat(sPaquete);
                contador = contador + 1;
            }
            plantillaCorreo = plantillaCorreo.replace( "#PAQUETES", paquetesConcat);
            EmailService emailService = new EmailService(jdbcConnection);
            String[] correo = new String[1];
            correo[0] = guiaRequest.getM_sCorreoDestinatario();
            emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(),
                    correo, Base64.getDecoder().decode(logo));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Hubo un error al tratar de mandar el correo electrónico al destinatario.");
        }
    }

    public void reenviarCorreoTracking(Connection jdbcConnection, Statement statement, int nIdGuia,
                                       String[] correos,Boolean correoDefault,String sRFC) throws Exception {
        try{
            GuiaRequest guiaRequest = new GuiaRequest();
            guiaRequest.setM_nIdGuia(nIdGuia);
            String query = "SELECT Tracking,FolioGuia FROM ProGuiaPQ WHERE IdGuia = "+ guiaRequest.getM_nIdGuia();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                guiaRequest.setM_nTracking(rs.getString("Tracking"));
                guiaRequest.setM_sFolioGuia(rs.getString("FolioGuia"));
            }
            query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ false, '"+guiaRequest.getM_nTracking()+"'";
            rs = statement.executeQuery(query);
            JSONObject jsonGuia = UtilFuctions.convertObject(rs);
            String nombreCliente="";
            String tipoServicio="";
            if (jsonGuia != null){
                guiaRequest.setM_sCorreoDestinatario(jsonGuia.getString("m_sCorreoDestinatario"));
                guiaRequest.setM_sDomicilioDestinatario(
                        jsonGuia.getBoolean("m_bEntregaDiferenteDomicilio") ?
                                jsonGuia.getString("m_sDomicilioDetalleEntrega") :
                                rs.getString("m_sDomicilioDestinatario"));
                guiaRequest.setM_dFecha(jsonGuia.getString("m_dFechaRegistro"));
                guiaRequest.setM_nidTipoServicio(jsonGuia.getInt("m_nIdTipoServicio"));
                if(guiaRequest.getM_nidTipoServicio()==1)
                {
                    tipoServicio="Consolidado";
                }
                else{
                    tipoServicio="Paquetería";
                }
                guiaRequest.setM_arrPaquetes(getPaquetesByGuia(statement, guiaRequest.getM_nIdGuia()));
                nombreCliente=jsonGuia.getString("m_sNombreDestinatario");
            }
            query = "SELECT NombreFiscal,Logo FROM SisParametros";
            rs = statement.executeQuery(query);
            jsonGuia = UtilFuctions.convertObject(rs);
            String logo=jsonGuia.getString("Logo").replace("\r\n","");
            logo=logo.replace("=","");
            guiaRequest.setM_sNombreCliente(jsonGuia.getString("NombreFiscal"));
            query = "SELECT top 1 FormatoCorreo FROM SisParametrosPQ";
            rs = statement.executeQuery(query);
            String plantillaCorreo = "";
            while(rs.next()){
                plantillaCorreo = rs.getString("FormatoCorreo");
            }
            plantillaCorreo = plantillaCorreo.replace("#LINK_TRACKING",
                    DBConection.getEnviromentURL() +"app/applications/"+sRFC+"/"
                            +guiaRequest.getM_nTracking()+"/tracking");
            plantillaCorreo = plantillaCorreo.replace("#TRACKING", guiaRequest.getM_nTracking());
            plantillaCorreo = plantillaCorreo.replace("#IMAGEN", "cid:logoImage");
            plantillaCorreo = plantillaCorreo.replace("#CLIENTE", guiaRequest.getM_sNombreCliente());
            plantillaCorreo = plantillaCorreo.replace( "#DIRECCION_ENTREGA",
                    guiaRequest.getM_sDomicilioDestinatario() );
            plantillaCorreo = plantillaCorreo.replace( "#FOLIO",  guiaRequest.getM_sFolioGuia() );
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            plantillaCorreo = plantillaCorreo.replace( "#FECHA",  guiaRequest.getM_dFecha());
            plantillaCorreo = plantillaCorreo.replace( "#TIPO_SERVICIO", tipoServicio );

            int contador = 1;
            String paquetesConcat = "";
            for (PaqueteRequest paquete : guiaRequest.getM_arrPaquetes()) {

                String sPaquete = UtilFuctions.getPlantillaPaquetesCorreo();
                sPaquete = sPaquete.replace("#NUM_PAQUETE", String.valueOf(contador));
                sPaquete = sPaquete.replace("#PESO", String.valueOf(paquete.getM_xPeso()));
                sPaquete = sPaquete.replace("#LARGO", String.valueOf(paquete.getM_xLargo()));
                sPaquete = sPaquete.replace("#ANCHO", String.valueOf(paquete.getM_xAncho()));
                sPaquete = sPaquete.replace("#ALTO", String.valueOf(paquete.getM_xAlto()));
                sPaquete = sPaquete.replace("#DESCRIPCION", String.valueOf(paquete.getM_sDescripcion()));
                sPaquete = sPaquete.replace("#CTD", String.valueOf(paquete.getCtd()));
                sPaquete = sPaquete.replace("#OBSERVACIONES", paquete.getM_sObservaciones());
                paquetesConcat = paquetesConcat.concat(sPaquete);
                contador = contador + 1;
            }
            plantillaCorreo = plantillaCorreo.replace( "#PAQUETES", paquetesConcat);
            EmailService emailService = new EmailService(jdbcConnection);

            String[] todosLosCorreos=new String[correos.length+1];
            for(int i=0;i<correos.length;i++)
            {
                todosLosCorreos[i]=correos[i];
            }
            todosLosCorreos[todosLosCorreos.length-1]=guiaRequest.getM_sCorreoDestinatario();
            if(correos[0]==""){todosLosCorreos=new String[]{guiaRequest.getM_sCorreoDestinatario()};}
            emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " +
                    guiaRequest.getM_sFolioGuia(), todosLosCorreos, Base64.getDecoder().decode(logo));

            //emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(), correo);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Hubo un error al tratar de mandar el correo electrónico al destinatario.");
        }
    }
    public void enviarCorreoStatus(Connection jdbcConnection, Statement statement, int nIdGuia,
                                   String[] correos,Boolean correoDefault,String sRFC, String mensaje) throws Exception {
        try{
            GuiaRequest guiaRequest = new GuiaRequest();
            guiaRequest.setM_nIdGuia(nIdGuia);
            String query = "SELECT Tracking,FolioGuia FROM ProGuiaPQ WHERE IdGuia = "+ guiaRequest.getM_nIdGuia();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                guiaRequest.setM_nTracking(rs.getString("Tracking"));
                guiaRequest.setM_sFolioGuia(rs.getString("FolioGuia"));
            }
            query = "EXEC usp_ProIdUltimaMillaGetPaquetesEsRecoleccionPQ false, '"+guiaRequest.getM_nTracking()+"'";
            rs = statement.executeQuery(query);
            JSONObject jsonGuia = UtilFuctions.convertObject(rs);
            String tipoServicio="";
            if (jsonGuia != null){
                guiaRequest.setM_sCorreoDestinatario(jsonGuia.getString("m_sCorreoDestinatario"));
                guiaRequest.setM_sDomicilioDestinatario(
                        jsonGuia.getBoolean("m_bEntregaDiferenteDomicilio") ?
                                jsonGuia.getString("m_sDomicilioDetalleEntrega") :
                                rs.getString("m_sDomicilioDestinatario"));
                guiaRequest.setM_dFecha(jsonGuia.getString("m_dFechaRegistro"));
                guiaRequest.setM_nidTipoServicio(jsonGuia.getInt("m_nIdTipoServicio"));
                if(guiaRequest.getM_nidTipoServicio()==1)
                {
                    tipoServicio="Consolidado";
                }
                else{
                    tipoServicio="Paquetería";
                }                guiaRequest.setM_arrPaquetes(getPaquetesByGuia(statement, guiaRequest.getM_nIdGuia()));
            }
            query = "SELECT top 1 FormatoCorreo FROM SisParametrosPQ";
            rs = statement.executeQuery(query);
            String plantillaCorreo = "";
            while(rs.next()){
                plantillaCorreo = rs.getString("FormatoCorreo");
            }
            plantillaCorreo = plantillaCorreo.replace("#LINK_TRACKING",
                    DBConection.getEnviromentURL() +"app/applications/"+sRFC+"/"
                            +guiaRequest.getM_nTracking()+"/tracking");
            plantillaCorreo = plantillaCorreo.replace("#TRACKING", guiaRequest.getM_nTracking());
          //  plantillaCorreo = plantillaCorreo.replace("<p style=\"font-family: Ubuntu, Helvetica, Arial; font-size:
            //  11px; text-align: left;\">", "<p style=\"font-family: Ubuntu, Helvetica, Arial;
            //  font-size: 11px; text-align: left;\">");
            plantillaCorreo = plantillaCorreo.replace("<span style=\"font-size: 20px;\">" +
                    "¡Tu orden ha sido registrada! </span>", "<span style=\"font-size: " +
                    "20px;text-align:center\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    +mensaje+"</span>");
            plantillaCorreo = plantillaCorreo.replace( "#DIRECCION_ENTREGA",  guiaRequest.getM_sDomicilioDestinatario() );
            plantillaCorreo = plantillaCorreo.replace( "#FOLIO",  guiaRequest.getM_sFolioGuia() );
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            plantillaCorreo = plantillaCorreo.replace( "#FECHA",  guiaRequest.getM_dFecha());
            plantillaCorreo = plantillaCorreo.replace( "#TIPO_SERVICIO", tipoServicio );

            int contador = 1;
            String paquetesConcat = "";
            for (PaqueteRequest paquete : guiaRequest.getM_arrPaquetes()) {
                String sPaquete = UtilFuctions.getPlantillaPaquetesCorreo();
                sPaquete = sPaquete.replace("#NUM_PAQUETE", String.valueOf(contador));
                sPaquete = sPaquete.replace("#PESO", String.valueOf(paquete.getM_xPeso()));
                sPaquete = sPaquete.replace("#LARGO", String.valueOf(paquete.getM_xLargo()));
                sPaquete = sPaquete.replace("#ANCHO", String.valueOf(paquete.getM_xAncho()));
                sPaquete = sPaquete.replace("#ALTO", String.valueOf(paquete.getM_xAlto()));
                sPaquete = sPaquete.replace("#DESCRIPCION", String.valueOf(paquete.getM_sDescripcion()));
                sPaquete = sPaquete.replace("#CTD", String.valueOf(paquete.getCtd()));
                sPaquete = sPaquete.replace("#OBSERVACIONES", paquete.getM_sObservaciones());
                paquetesConcat = paquetesConcat.concat(sPaquete);
                contador = contador + 1;
            }
            plantillaCorreo = plantillaCorreo.replace( "#PAQUETES", paquetesConcat);
            EmailService emailService = new EmailService(jdbcConnection);
            String[] correo = new String[1];
            correo[0] = guiaRequest.getM_sCorreoDestinatario();

            if(correoDefault){
                emailService.sendMail(plantillaCorreo, "Seguimiento de la guia "
                        + guiaRequest.getM_sFolioGuia(),correo[0], correo);
            }else{
                emailService.sendMail(plantillaCorreo, "Seguimiento de la guia "
                        + guiaRequest.getM_sFolioGuia(),null, correos);
            }
            //emailService.sendMail(plantillaCorreo, "Seguimiento de la guia " + guiaRequest.getM_sFolioGuia(), correo);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Hubo un error al tratar de mandar el correo electrónico al destinatario.");
        }
    }

    public ArrayList<PaqueteRequest> getPaquetesByGuia(Statement statement,int nIdGuia) throws SQLException {
        String query = "SELECT\n" +
                "\t\tpe.IdEmbarqueDetalle\n" +
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
                "\tWHERE IdEmbarque = (select pg.IdEmbarque from ProGuiaPQ pg where pg.IdGuia = "+nIdGuia+") AND Tipo=2";

        ResultSet rs = statement.executeQuery(query);
        ArrayList<PaqueteRequest> listPaquetes = new ArrayList<>();
        while(rs.next()){
            PaqueteRequest paqueteRequest = new PaqueteRequest();
            paqueteRequest.setM_xPeso(rs.getFloat("Peso"));
            paqueteRequest.setM_xLargo(rs.getInt("Largo"));
            paqueteRequest.setM_xAncho(rs.getInt("Ancho"));
            paqueteRequest.setM_xAlto(rs.getInt("Alto"));
            paqueteRequest.setCtd(rs.getInt("ctd"));
            paqueteRequest.setM_sDescripcion(rs.getString("Descripcion"));
            paqueteRequest.setM_sObservaciones(rs.getString("Observaciones"));

            listPaquetes.add(paqueteRequest);
        }
        return listPaquetes;

    }

    public JSONObject validarFoliosDisponiblesViajesERP(Connection jdbcConnection, int idSucursal){
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("success", true);
        jsonResult.put("message", "");
        jsonResult.put("errorCode", 0);
        try {
            Statement statement = jdbcConnection.createStatement();
            String query = "";
            JSONObject jsonRs;
            query = "select top 1 ISNULL(IdTipoDocumento,0) as idTipoDocumento " +
                    "from CatParametrosConfiguracionDocumentosTimbradoPQ where IdSucursal = " + idSucursal;
            jsonRs = UtilFuctions.convertObject(statement.executeQuery(query));
            if (jsonRs == null || jsonRs.getInt("idTipoDocumento") == 0) {
                jsonResult.put("success", false);
                jsonResult.put("message", "No hay un documento asignado a la sucursal. " +
                        "Asígnelo en parámetros de configuración");
                jsonResult.put("errorCode", 2);
                return jsonResult;
            }
            query = "select ISNULL(IdTipoDocumento, 0) as idTipoDocumento, ISNULL(Serie,'') as serie\n" +
                    "from CatTiposDocumentosConfiguracionTimbrado\n" +
                    "where IdTipoDocumentoConfiguracionTimbrado = (select top 1 IdTipoDocumento\n" +
                    "                                              from CatParametrosConfiguracionDocumentosTimbradoPQ\n" +
                    "                                              where IdSucursal = " + idSucursal + ");";
            jsonRs = UtilFuctions.convertObject(statement.executeQuery(query));
            if (jsonRs == null || jsonRs.getInt("idTipoDocumento") == 0) {
                jsonResult.put("success", false);
                jsonResult.put("message", "No se encontró ningún tipo de documento para timbrado asignado a la " +
                        "sucursal en el ERP. Verifique en el ERP.");
                jsonResult.put("errorCode", 3);
                return jsonResult;
            }

            query = "select ISNULL(MIN(IdFolio),0) as idFolio from CatFolios where Estatus = 1 and IdTipoDocumento = "
                    + jsonRs.getInt("idTipoDocumento") + " and Serie = '" + jsonRs.getString("serie") + "'";
            jsonRs = UtilFuctions.convertObject(statement.executeQuery(query));
            if (jsonRs == null || jsonRs.getInt("idFolio") == 0) {
                jsonResult.put("success", false);
                jsonResult.put("message", "La sucursal no cuenta con folios disponibles para el documento y la " +
                        "serie asignadas a la sucursal en parámetros de configuración. Asigne en el catálogo de " +
                        "folios en el ERP.");
                jsonResult.put("errorCode", 4);
                return jsonResult;
            }
            jsonResult.put("message", "Folio disponible.");
            return jsonResult;
        }catch (Throwable exception) {
            jsonResult.put("success", false);
            jsonResult.put("message", exception.toString());
            jsonResult.put("errorCode", 1);
            return jsonResult;
        }
    }

//    public JSONObject validarCartaPorteRelacionada(Statement statement, int idGuia) {
//        JSONObject jsonResponse = null;
//        try {
//            String query = "SELECT TOP 1 IdGuia, viaje.NoViajeCliente, viaje.IdViaje, viaje.IdEstatuViaje, estatus.Estatus,CONCAT(cs.Abreviacion, '-', viaje.Viaje) folioErp FROM ProViajes viaje INNER JOIN CatEstatusViaje AS estatus ON estatus.IdEstatuViaje = viaje.IdEstatuViaje INNER JOIN CatSucursales AS cs ON viaje.IdSucursal = cs.IdSucursal WHERE IdGuia = " + idGuia;
//            ResultSet rs;
//            rs = statement.executeQuery(query);
//            jsonResponse = convertObject(rs);
//            if (jsonResponse.has("IdViaje")) {
//                String folioGuia = jsonResponse.getString("NoViajeCliente");
//                String estatusViaje = jsonResponse.getString("Estatus");
//                String folioViaje = jsonResponse.getString("folioErp");
//                jsonResponse.put("message", "La guía " + folioGuia + " aún está relacionada al viaje " + folioViaje + " con estatus " + estatusViaje + " en el ERP. Elimine la relación desde el ERP para continuar.");
//                jsonResponse.put("tieneViajeRelacionado", true);
//            } else {
//                jsonResponse.put("tieneViajeRelacionado", false);
//            }
//            return jsonResponse;
//        }catch (Throwable exception) {
//            exception.printStackTrace();
//            return jsonResponse;
//        }
//    }
}
