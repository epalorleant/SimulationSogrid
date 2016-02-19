/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.samples.consumer;


import com.imag.nespros.network.devices.Device;
import com.imag.nespros.network.routing.EventPacket;
import com.imag.nespros.runtime.client.AnEventHandler;
import com.imag.nespros.runtime.client.EventConsumer;
import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.logging.MyLogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author epaln
 */
public class Consumer implements AnEventHandler {

    private boolean isQoS = false;
    MyLogger logger;
    long processingTime = 20;
    EventConsumer c;

    public Consumer(EventConsumer c) {
        logger = new MyLogger("latencies");
        this.c = c;
    }

    @Override
    public void notify(EventBean[] evts) {
        
        System.out.println("Received(" + evts.length + " evts): ");

        for (EventBean evt : evts) {            
            try {
                Device sender = (Device) evt.payload.get("sender");
                long tt = (long)evt.getValue("TT");
                evt.payload.put("RT", evt.getHeader().getReceptionTime());
                evt.payload.put("OT", tt);
                Thread.sleep(processingTime);
                evt.payload.put("TT", System.currentTimeMillis());
                evt.payload.remove("realPowerWatts");
                evt.payload.remove("timestampUTC");
                evt.payload.remove("meterID");
                System.out.println("Answer for: "+ sender.getDeviceName() + "with data: " +evt.payload);
                EventPacket p = new EventPacket(evt);
                ArrayList<Device> dest = new ArrayList<>();
                dest.add(sender);
                c.getDevice().sendPacket(p, dest);
                /*if(evt.payload.contains("avgPwr")){
                    System.out.println("Average Pwr: "+ evt.getValue("avgPwr"));//+", " +evt.getValue("meterID"));
                }
                else{
                    System.out.println(evt.payload);
                }
                    */
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

private String getHour(long timestamp) {
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(timestamp);
        return d.get(Calendar.HOUR_OF_DAY) + ":" + d.get(Calendar.MINUTE) + ":" + d.get(Calendar.SECOND);
    }
}