/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros.samples.producer;

import com.imag.nespros.runtime.client.EventProducer;
import com.imag.nespros.runtime.event.EventBean;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author epaln
 */
public class NTPClient extends EventProducer {
    Random random;

    public NTPClient(String name, String typeName, long delay) {
        super(name, typeName, null);
        this.delay = delay;
        this.definedDelay = delay;
        //this.file = file;          
        random = new Random();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt((int) delay));
            EventBean e = new EventBean();
            e.payload.put("sender", this.getDevice());
            e.payload.put("TT", System.currentTimeMillis());
            sendEvent(e);
        } catch (Exception ex) {
            Logger.getLogger(NTPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
