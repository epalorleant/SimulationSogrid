/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.samples.consumer;


import com.imag.nespros.runtime.client.AnEventHandler;
import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.logging.MyLogger;
import java.util.Calendar;



/**
 *
 * @author epaln
 */
public class Consumer implements AnEventHandler {

    private boolean isQoS = false;
    MyLogger logger;

    public Consumer() {
        logger = new MyLogger("latencies");
    }

    @Override
    public void notify(EventBean[] evts) {
        System.out.println("Received(" + evts.length + " evts): ");

        for (EventBean evt : evts) {
            //long timeSinceProd = evt.getHeader().getReceptionTime()- evt.getHeader().getProductionTime();
            //long notificationTime = evt.getHeader().getReceptionTime()- evt.getHeader().getNotificationTime();
            //logger.log(timeSinceProd + ", "+ notificationTime);
            //System.out.println(MemoryMeasurer.measureBytes(evt)+" bytes");
            // System.out.println(evt.getValue("avgPwr")+", " +evt.getValue("meterID"));//+", "+evt.getHeader().getPriority());
            if(evt.payload.contains("avgPwr")){
             System.out.println("Average Pwr: "+ evt.getValue("avgPwr"));//+", " +evt.getValue("meterID"));
            }
            else{
                System.out.println(evt.payload);
            }
        }
    }

private String getHour(long timestamp) {
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(timestamp);
        return d.get(Calendar.HOUR_OF_DAY) + ":" + d.get(Calendar.MINUTE) + ":" + d.get(Calendar.SECOND);
    }
}