package com.certuit.base.domain.request.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplementoSATRequest {
    int m_nIdComplementoSAT;
    int m_nIdRecoleccion;
    int m_nIdEmbarque;
    int m_nCantidad;
    String m_sClaveProductoServicio;
    String m_sProductoServicio;
    String m_sClaveUnidad;
    String m_sUnidad;
    String m_sClaveFraccionArancelaria;
    String m_sFraccionArancelaria;
    String m_sUUIDComercioExterior;
    String m_sClaveMaterialPeligroso;
    String m_sMaterialPeligroso;
    boolean m_bEsMaterialPeligroso;
    String m_sClaveEmbalaje;
    String m_sTipoEmbalaje;
    String m_sDescripcionEmbalaje;
    float m_xPeso;
    String sectorCOFEPRIS;
    String denominacionGenerica;
    String denominacionDistintiva;
    String fabricante;
    String fechaCaducidad;
    String loteMedicamento;
    String claveFormaFarmaceutica;
    String formaFarmaceutica;
    String claveCondicionEspecial;
    String condicionEspecial;
    String registroSanitario_folioAutorizacion;
    String nombreQuimico;
    String numeroCAS;
    String nombreIngredienteActivo;
    String numRegSanPlagCOFEPRIS;
    String datosFabricante;
    String datosFormulador;
    String datosMaquilador;
    String usoAutorizado;
    String m_sUltimoError; 
}
