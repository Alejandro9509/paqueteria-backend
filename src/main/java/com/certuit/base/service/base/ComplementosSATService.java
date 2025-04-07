package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.ComplementoSATRequest;
import com.certuit.base.domain.request.base.EmbarqueRequest;
import com.certuit.base.domain.request.base.RecoleccionRequest;
import com.certuit.base.util.UtilFuctions;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class ComplementosSATService {
    public void agregarComplementosARecoleccion(RecoleccionRequest recoRequest, Connection jdbcConnection) throws SQLException, Exception {
        String query = "";
        int esMaterialPeligroso;

        for (ComplementoSATRequest complemento : recoRequest.getM_arrClsComplementoSAT()) {
            complemento.setM_nIdRecoleccion(recoRequest.getM_nIdRecoleccion());
            if (complemento.isM_bEsMaterialPeligroso()){
                esMaterialPeligroso = 1;
            }else{
                esMaterialPeligroso = 0;
            }
            query= "EXEC usp_ProRecoleccionAgregarComplementoPQ ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ";
            PreparedStatement ps=jdbcConnection.prepareStatement(query);
            ps.setInt(1,complemento.getM_nIdRecoleccion());
            ps.setInt(2,complemento.getM_nCantidad());
            ps.setString(3,complemento.getM_sClaveProductoServicio());
            ps.setString(4,complemento.getM_sClaveUnidad());
            ps.setString(5,complemento.getM_sClaveFraccionArancelaria());
            ps.setString(6,complemento.getM_sUUIDComercioExterior());
            ps.setInt(7,esMaterialPeligroso);
            ps.setString(8,complemento.getM_sClaveMaterialPeligroso());
            ps.setString(9,complemento.getM_sClaveEmbalaje());
            ps.setString(10,complemento.getM_sTipoEmbalaje());
            ps.setString(11,complemento.getM_sDescripcionEmbalaje());
            ps.setFloat(12, UtilFuctions.round(complemento.getM_xPeso(),3));
            ps.setString(13,complemento.getSectorCOFEPRIS());
            ps.setString(14,complemento.getNombreIngredienteActivo());
            ps.setString(15,complemento.getNombreQuimico());
            ps.setString(16,complemento.getDenominacionGenerica());
            ps.setString(17,complemento.getDenominacionDistintiva());
            ps.setString(18,complemento.getFabricante());
            ps.setString(19,complemento.getFechaCaducidad());
            ps.setString(20,complemento.getLoteMedicamento());
            ps.setString(21,complemento.getClaveFormaFarmaceutica());
            ps.setString(22,complemento.getRegistroSanitario_folioAutorizacion());
            ps.setString(23,complemento.getNumeroCAS());
            ps.setString(24,complemento.getClaveCondicionEspecial());
            ps.setString(25,complemento.getNumRegSanPlagCOFEPRIS());
            ps.setString(26,complemento.getDatosFabricante());
            ps.setString(27,complemento.getDatosFormulador());
            ps.setString(28,complemento.getDatosMaquilador());
            ps.setString(29,complemento.getUsoAutorizado());

            ps.execute();
        }
    }

    public void modificarComplementosRecoleccion(RecoleccionRequest recoRequest, Connection jdbcConnection) throws SQLException, Exception {
        String query = "";
        int esMaterialPeligroso;
        query = "EXEC usp_ProRecoleccionEliminarComplementosSATPQ "+recoRequest.getM_nIdRecoleccion();
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate(query);
        for (ComplementoSATRequest complemento : recoRequest.getM_arrClsComplementoSAT()) {
            complemento.setM_nIdRecoleccion(recoRequest.getM_nIdRecoleccion());
            if (complemento.isM_bEsMaterialPeligroso()){
                esMaterialPeligroso = 1;
            }else{
                esMaterialPeligroso = 0;
            }

            query= "EXEC usp_ProRecoleccionAgregarComplementoPQ ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ";
            PreparedStatement ps=jdbcConnection.prepareStatement(query);
            ps.setInt(1,complemento.getM_nIdRecoleccion());
            ps.setInt(2,complemento.getM_nCantidad());
            ps.setString(3,complemento.getM_sClaveProductoServicio());
            ps.setString(4,complemento.getM_sClaveUnidad());
            ps.setString(5,complemento.getM_sClaveFraccionArancelaria());
            ps.setString(6,complemento.getM_sUUIDComercioExterior());
            ps.setInt(7,esMaterialPeligroso);
            ps.setString(8,complemento.getM_sClaveMaterialPeligroso());
            ps.setString(9,complemento.getM_sClaveEmbalaje());
            ps.setString(10,complemento.getM_sTipoEmbalaje());
            ps.setString(11,complemento.getM_sDescripcionEmbalaje());
            ps.setFloat(12, UtilFuctions.round(complemento.getM_xPeso(),3));
            ps.setString(13,complemento.getSectorCOFEPRIS());
            ps.setString(14,complemento.getNombreIngredienteActivo());
            ps.setString(15,complemento.getNombreQuimico());
            ps.setString(16,complemento.getDenominacionGenerica());
            ps.setString(17,complemento.getDenominacionDistintiva());
            ps.setString(18,complemento.getFabricante());
            ps.setString(19,complemento.getFechaCaducidad());
            ps.setString(20,complemento.getLoteMedicamento());
            ps.setString(21,complemento.getClaveFormaFarmaceutica());
            ps.setString(22,complemento.getRegistroSanitario_folioAutorizacion());
            ps.setString(23,complemento.getNumeroCAS());
            ps.setString(24,complemento.getClaveCondicionEspecial());
            ps.setString(25,complemento.getNumRegSanPlagCOFEPRIS());
            ps.setString(26,complemento.getDatosFabricante());
            ps.setString(27,complemento.getDatosFormulador());
            ps.setString(28,complemento.getDatosMaquilador());
            ps.setString(29,complemento.getUsoAutorizado());

            ps.execute();
        }
    }

    public void agregarComplementosAEmbarque(EmbarqueRequest embRequest, Connection jdbcConnection) throws SQLException, Exception {
        String query = "";
        int esMaterialPeligroso;
        for (ComplementoSATRequest complemento : embRequest.getM_arrClsComplementoSAT()) {
            complemento.setM_nIdEmbarque(embRequest.getM_nIdEmbarque());
            if (complemento.isM_bEsMaterialPeligroso()){
                esMaterialPeligroso = 1;
            }else{
                esMaterialPeligroso = 0;
            }

            query= "EXEC usp_ProEmbarqueDetalleAgregarComplementoPQ ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ";
            PreparedStatement ps=jdbcConnection.prepareStatement(query);
            ps.setInt(1,complemento.getM_nIdEmbarque());
            ps.setInt(2,complemento.getM_nCantidad());
            ps.setString(3,complemento.getM_sClaveProductoServicio());
            ps.setString(4,complemento.getM_sClaveUnidad());
            ps.setString(5,complemento.getM_sClaveFraccionArancelaria());
            ps.setString(6,complemento.getM_sUUIDComercioExterior());
            ps.setInt(7,esMaterialPeligroso);
            ps.setString(8,complemento.getM_sClaveMaterialPeligroso());
            ps.setString(9,complemento.getM_sClaveEmbalaje());
            ps.setString(10,complemento.getM_sTipoEmbalaje());
            ps.setString(11,complemento.getM_sDescripcionEmbalaje());
            ps.setFloat(12, complemento.getM_xPeso());
            ps.setString(13,complemento.getSectorCOFEPRIS());
            ps.setString(14,complemento.getNombreIngredienteActivo());
            ps.setString(15,complemento.getNombreQuimico());
            ps.setString(16,complemento.getDenominacionGenerica());
            ps.setString(17,complemento.getDenominacionDistintiva());
            ps.setString(18,complemento.getFabricante());
            ps.setString(19,complemento.getFechaCaducidad());
            ps.setString(20,complemento.getLoteMedicamento());
            ps.setString(21,complemento.getClaveFormaFarmaceutica());
            ps.setString(22,complemento.getRegistroSanitario_folioAutorizacion());
            ps.setString(23,complemento.getNumeroCAS());
            ps.setString(24,complemento.getClaveCondicionEspecial());
            ps.setString(25,complemento.getNumRegSanPlagCOFEPRIS());
            ps.setString(26,complemento.getDatosFabricante());
            ps.setString(27,complemento.getDatosFormulador());
            ps.setString(28,complemento.getDatosMaquilador());
            ps.setString(29,complemento.getUsoAutorizado());

            ps.execute();
        }
    }
}
