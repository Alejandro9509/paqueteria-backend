package com.certuit.base.service.base;

import com.certuit.base.domain.request.base.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Statement;

@Service
public class CorteCajaService {

    public void agregarCorteCajaGuias(CorteCajaGuiaRequest c, Statement statement) throws SQLException, Exception {
        try {
            String query = "EXEC usp_ProCorteCajaGuiasAgregarPQ  " + c.getM_nIdCorte() + "," + c.getM_nIdGuia() + "," + c.getM_nTotal();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
