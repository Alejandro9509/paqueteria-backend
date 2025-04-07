package com.certuit.base.util;

import com.certuit.base.service.base.TarifasRangosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Cron {
    //Ejemplos
    // * "0 0 * * * *" = the top of every hour of every day.
    // * "*/10 * * * * *" = every ten seconds.
    // * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    // * "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
    // * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    // * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    // * "0 0 0 25 12 ?" = every Christmas Day at midnight
    // * "0 0 0 * * ?" Todos los dias a las 12am

  /*  @Autowired
    TicketService ticketService;*/
    @Autowired
    DBConection dbConection;
    @Autowired
    TarifasRangosService tarifasRangosService;

    public Cron() {

    }
    //Todos los dias a las 12am
    @Scheduled(cron = "0 0 0 * * ?")
    public void async() {
        try{
        //Connection jdbcConnection = dbConection.getconnection("ADI880815DA7");
        //tarifasRangosService.validarVigenciaTarifas(jdbcConnection);
        //jdbcConnection = dbConection.getconnection("SOPO110101PQ1");
        //tarifasRangosService.validarVigenciaTarifas(jdbcConnection);
        //jdbcConnection = dbConection.getconnection("PTR170523BI6");
        //tarifasRangosService.validarVigenciaTarifas(jdbcConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ticketService.checkTicketsForClosedInTime();
    }

    // cron = segundos, minutos, horas, dia del mes, mes, dia de la semana
    // * means match any
    // */X means "every X"
    // ? ("no specific value") - useful when you need to specify something in one of the two fields in which the character is allowed
}
