/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.client;

import com.imag.nespros.network.routing.Subscriber;
import com.imag.nespros.network.routing.Topic2Device;
import com.imag.nespros.runtime.core.EPUnit;
import com.imag.nespros.runtime.core.IOTerminal;
import com.imag.nespros.runtime.event.EventBean;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @author epaln
 */
public class EventConsumer extends EPUnit implements Subscriber {

    String _info, _input;
    AnEventHandler _handler;

    public EventConsumer(String info, String IDinputTerminal, AnEventHandler handler) {
        super(info);
        _handler = handler;
        this.setExecutionTime(0);
        //try {
            _info = info;
            _input = IDinputTerminal;
            _type = "Consumer";
//            if (handler != null && this.getDevice() != null) {
//                this.getDevice().getPubSubService().subscribe(this, _input);
//
//            }
      //  } catch (Exception ex) {
        //    Logger.getLogger(EventConsumer.class.getName()).log(Level.SEVERE, null, ex);
      //  }
    }

    @Override
    public synchronized void notify(Object event) {
        EventBean[] evts = (EventBean[]) event;
        for (EventBean evt : evts) {
            evt.getHeader().setReceptionTime(System.currentTimeMillis());
            if(evt.payload.contains("#time#")) evt.payload.remove("#time#");
            if(evt.payload.contains("processTime")) evt.payload.remove("processTime");
        }
        _handler.notify(evts);
    }

    @Override
    public Collection<IOTerminal> getInputTerminals() {
        ArrayList<IOTerminal> inputs = new ArrayList<IOTerminal>();
        inputs.add(new IOTerminal(_input, null, this));
        return inputs;
    }

    @Override
    public IOTerminal getOutputTerminal() {
        return null;
    }

    @Override
    public void process() {
    }

    @Override
    public boolean fetch() {
        return false;
    }

    @Override
    public void run() {
    }
  
    @Override
    public boolean openIOchannels() {        
        Topic2Device.getInstance().AddMapping(_input, getDevice());
        this.getDevice().getPubSubService().subscribe(this, _input);
        return true;
    }
    
    
}
