package com.certuit.base.domain.enums;

import lombok.Getter;
import lombok.Setter;

public enum PushNotificationMessages {
    NEW_REPORT("","Se ha creado una nueva solicitud con el folio %s del área %s. " +
            "Por favor ingresa para iniciar el seguimiento" ),
    NEW_ANSWER_REPORT("", "%s ha generado un comentario para la solicitud %s. Esperamos tu respuesta."),
    NEW_ANSWER_CLIENT_REPORT("", "%s ha generado un comentario para la solicitud %s. Por favor ingresa" +
            " y proporciona retroalimentación para cumplir con nuestra promesa de servicio."),

    CLOSE_REPORT("", "%s ha concluido tu solicitud %s. Si estás satisfecho con la solución ayúdanos " +
            "contestando una encuesta, en caso contrario, genera un nuevo comentario para reabrir la solicitud."),
    REASSIGNED_REPORT("","La solicitud %s enviada el %s se asignó a %s con la prioridad %s, te daremos" +
            " respuesta dentro de las próximas %d horas" ),
    REASSIGNED_REPORT_ASIGNED("","Tienes una nueva solicitud %s con prioridad %s. Por favor ingresa y " +
            "proporciona retroalimentación para cumplir con nuestra promesa de servicio." ),
    OPEN_REPORT("", "Del tipo ");

    @Getter
    @Setter
    String title;
    @Getter
    @Setter
    String content;

    PushNotificationMessages(String titulo, String contenido) {
        this.title = titulo;
        this.content = contenido;
    }






}
