package com.certuit.base.endpoint;

import com.certuit.base.domain.request.base.CubicarRequest;
import com.certuit.base.domain.request.base.InformeGuiaRequest;
import com.certuit.base.domain.request.base.PaqueteCubicar;
import com.certuit.base.domain.response.CubicajeResponse;
import com.certuit.base.service.base.CubicarService;
import com.certuit.base.util.DBConection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xf.xflp.XFLP;
import xf.xflp.opt.XFLPOptType;
import xf.xflp.report.ContainerReport;
import xf.xflp.report.LPReport;
import java.sql.Connection;

@RestController
@RequestMapping(value = "/api")
public class CubicarRest {
    @Autowired
    DBConection dbConection;

    @Autowired
    CubicarService cubicarService;

    @PostMapping("/Cubicar")
    public ResponseEntity<?> cubicar(@RequestBody CubicarRequest request, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                XFLP xflp = new XFLP();
                if (request.getIdRemolque2() == 0) {
                    xflp.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_OPTIMIZER);
                } else {
                    xflp.setTypeOfOptimization(XFLPOptType.BEST_FIXED_CONTAINER_PACKER);
                }
                JSONObject jsonRemolque = cubicarService.obtenerDatosContenedor(jdbcConnection,
                        request.getIdRemolque1());
                if (jsonRemolque == null) {
                    return ResponseEntity.status(500).body("No hay remolque.");
                }
                if (jsonRemolque.getInt("largo") == 0 || jsonRemolque.getInt("ancho") == 0 ||
                        jsonRemolque.getInt("alto") == 0 || jsonRemolque.getInt("capacidad") == 0) {
                    return ResponseEntity.status(500).body("El remolque " + jsonRemolque.getString("Codigo")
                            + " no cuenta con las dimensiones registradas. Favor de ingresar al catálogo de unidades " +
                            "y agregar la información para poder realizar el cúbicaje.");
                }

                xflp.addContainer()
                        .setLength(jsonRemolque.getInt("largo"))
                        .setWidth(jsonRemolque.getInt("ancho"))
                        .setHeight(jsonRemolque.getInt("alto"))
                        .setMaxWeight(jsonRemolque.getInt("capacidad"))
                        .setContainerType("" + request.getIdRemolque1());
                JSONObject jsonRemolque2 = cubicarService.obtenerDatosContenedor(jdbcConnection,
                        request.getIdRemolque2());
                if (jsonRemolque2 != null) {
                    if (jsonRemolque2.getInt("largo") == 0 || jsonRemolque2.getInt("ancho") == 0 ||
                            jsonRemolque2.getInt("alto") == 0 || jsonRemolque2.getInt("capacidad") == 0) {
                        return ResponseEntity.status(500).body("El remolque " + jsonRemolque2.getString("Codigo")
                                + " no cuenta con las dimensiones registradas. Favor de ingresar al catálogo de " +
                                "unidades y agregar la información para poder realizar el cúbicaje.");
                    }

                    xflp.addContainer()
                            .setLength(jsonRemolque2.getInt("largo"))
                            .setWidth(jsonRemolque2.getInt("ancho"))
                            .setHeight(jsonRemolque2.getInt("alto"))
                            .setMaxWeight(jsonRemolque2.getInt("capacidad"))
                            .setContainerType("" + request.getIdRemolque2());
                }

//            AGREGANDO PRODUCTOS
//            CADA PRODUCTO TIENE N CANTIDAD DE PAQUETES. SE OCUPA SACAR EL TOTAL DE PAQUETES POR CADA PRODUCTO.
                for (PaqueteCubicar paquete : request.getPaquetes()) {
                    for (int i = 0; i < paquete.getCantidad(); i++) {
                        xflp.addItem().setLength(paquete.getLargo()).setWidth(paquete.getAncho())
                                .setHeight(paquete.getAlto()).setWeight(paquete.getPeso());
                    }
                }
                float utilization = 0;
                CubicajeResponse cubicajeResponse = new CubicajeResponse();

                if (request.getPaquetes().isEmpty()) {
                    cubicajeResponse.setUtilizacion(utilization);
                    return ResponseEntity.ok(cubicajeResponse);
                }
                xflp.executeLoadPlanning();
                LPReport report = xflp.getReport();

                if (report.getUnplannedPackages().size() > 0) {
                    cubicajeResponse.setMensaje("Capacidad máxima superada");
                    cubicajeResponse.setUtilizacion(101);
                } else {
                    if (jsonRemolque2 != null && report.getContainerReports().size() == 1) {
                        utilization =
                                jsonRemolque.getInt("capacidad") * utilization / (jsonRemolque.getInt("capacidad")
                                        + jsonRemolque2.getInt("capacidad"));
                    }
                    cubicajeResponse.setUtilizacion(utilization);
                }
                return ResponseEntity.ok(cubicajeResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios. Intente más tarde.");
        }
    }

    @PostMapping("/Cubicar/Informe")
    public ResponseEntity<?> cubicarInforme(@RequestBody CubicarRequest request, @RequestHeader("RFC") String rfc)
            throws Exception {
        try {
            try (Connection jdbcConnection = dbConection.getconnection(rfc)) {
                String query = "";
                XFLP xflp = new XFLP();
                if (request.getIdRemolque2() == 0) {
                    xflp.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_OPTIMIZER);
                } else {
                    xflp.setTypeOfOptimization(XFLPOptType.BEST_FIXED_CONTAINER_PACKER);
                }
                JSONObject jsonRemolque = cubicarService.obtenerDatosContenedor(jdbcConnection, request.getIdRemolque1());
                if (jsonRemolque == null) {
                    return ResponseEntity.status(500).body("No hay remolque.");
                }
                if (jsonRemolque.getInt("largo") == 0 || jsonRemolque.getInt("ancho") == 0
                        || jsonRemolque.getInt("alto") == 0 || jsonRemolque.getInt("capacidad") == 0) {
                    return ResponseEntity.status(500).body("El remolque " + jsonRemolque.getString("Codigo")
                            + " no cuenta con las dimensiones registradas. Favor de ingresar al catálogo de unidades " +
                            "y agregar la información para poder realizar el cúbicaje.");
                }
                xflp.addContainer()
                        .setLength(jsonRemolque.getInt("largo"))
                        .setWidth(jsonRemolque.getInt("ancho"))
                        .setHeight(jsonRemolque.getInt("alto"))
                        .setMaxWeight(jsonRemolque.getInt("capacidad"))
                        .setContainerType("" + request.getIdRemolque1());
                JSONObject jsonRemolque2 = cubicarService.obtenerDatosContenedor(jdbcConnection,
                        request.getIdRemolque2());
                if (jsonRemolque2 != null) {
                    if (jsonRemolque2.getInt("largo") == 0 || jsonRemolque2.getInt("ancho") == 0 ||
                            jsonRemolque2.getInt("alto") == 0 || jsonRemolque2.getInt("capacidad") == 0) {
                        return ResponseEntity.status(500).body("El remolque " + jsonRemolque2.getString("Codigo")
                                + " no cuenta con las dimensiones registradas. Favor de ingresar al catálogo de " +
                                "unidades y agregar la información para poder realizar el cúbicaje.");
                    }
                    //xflp.setTypeOfOptimization(XFLPOptType.BEST_FIXED_CONTAINER_PACKER);
                    xflp.addContainer()
                            .setLength(jsonRemolque2.getInt("largo"))
                            .setWidth(jsonRemolque2.getInt("ancho"))
                            .setHeight(jsonRemolque2.getInt("alto"))
                            .setMaxWeight(jsonRemolque2.getInt("capacidad"))
                            .setContainerType("" + request.getIdRemolque2());
                }

                if (request.getGuias().isEmpty()) {
                    CubicajeResponse cubicajeResponse = new CubicajeResponse();
                    cubicajeResponse.setUtilizacion(0);
                    return ResponseEntity.ok(cubicajeResponse);
                }

//            AGREGANDO PRODUCTOS
//            CADA PRODUCTO TIENE N CANTIDAD DE PAQUETES. SE OCUPA SACAR EL TOTAL DE PAQUETES POR CADA PRODUCTO.
                float pesoTotalPaquetes = 0;
                float volumenTotalPaquetes = 0;
                JSONArray paquetes = cubicarService.obtenerPaquetesGuias(jdbcConnection, request.getGuias().stream().mapToInt(InformeGuiaRequest::getM_nIdGuia).toArray());
                for (int i = 0; i < paquetes.length(); i++) {
                    JSONObject paquete = paquetes.getJSONObject(i);
                    pesoTotalPaquetes += paquete.getInt("peso");
                    volumenTotalPaquetes +=
                            paquete.getInt("largo") * paquete.getInt("ancho") * paquete.getInt("alto");
                    xflp.addItem().setLength(paquete.getInt("largo")).setWidth(paquete.getInt("ancho"))
                            .setHeight(paquete.getInt("alto")).setWeight(paquete.getInt("peso"));
                }

                xflp.executeLoadPlanning();
                LPReport report = xflp.getReport();
                if (report.getContainerReports().size() == 0) {
                    return ResponseEntity.status(500).body("No cupo ningun paquete");
                }

//            System.out.printf("%.0f", report.getContainerReports().get(0).getSummary().getMaxUsedVolume());
//            System.out.printf("%.0f", report.getContainerReports().get(0).getSummary().getMaxVolume());
//            System.out.println();
//            System.out.println(report.getContainerReports().get(0).getSummary().getUtilization());
//            System.out.println(report.getUnplannedPackages().size());
                float utilization = report.getSummary().getUtilization() * 100;
                if (jsonRemolque2 != null && report.getContainerReports().size() == 1) {
                    utilization = jsonRemolque.getInt("capacidad") * utilization /
                            (jsonRemolque.getInt("capacidad") + jsonRemolque2.getInt("capacidad"));
                }
                CubicajeResponse cubicajeResponse = new CubicajeResponse();
                if (report.getUnplannedPackages().size() > 0) {
                    int pesoMaximo = 0;
                    int volumenMaximo = 0;
                    for (ContainerReport contenedor : report.getContainerReports()) {
                        pesoMaximo += contenedor.getContainer().getMaxWeight();
                        volumenMaximo += contenedor.getContainer().getLength() * contenedor.getContainer().getHeight()
                                * contenedor.getContainer().getWidth();
                    }
                    if (pesoTotalPaquetes > pesoMaximo)
                        cubicajeResponse.setMensaje("Capacidad de peso máximo superada");
                    else if (volumenTotalPaquetes > volumenMaximo)
                        cubicajeResponse.setMensaje("Capacidad de volumen máximo superada");
                    else
                        cubicajeResponse.setMensaje("No se encontró la forma de acomodar la mercancía dadas sus dimensiones");
                    cubicajeResponse.setUtilizacion(101);
                } else {
                    cubicajeResponse.setUtilizacion(utilization);
                }
                return ResponseEntity.ok(cubicajeResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Hubo un problema al consultar la información. Intente más tarde.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hubo un error al guardar los cambios. Intente más tarde.");
        }
    }
}
