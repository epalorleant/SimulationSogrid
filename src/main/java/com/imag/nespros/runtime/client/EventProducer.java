/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.client;


import com.imag.nespros.runtime.core.EPUnit;
import com.imag.nespros.runtime.core.IOTerminal;
import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.event.EventTypeRepository;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;


/**
 *
 * @author epaln
 */
public class EventProducer extends EPUnit {

    private final Class _clazz;
    private final String _topicName;
    private final IOTerminal output;
    protected long delay;
    
    // for graphic control
    protected long definedDelay;

    public EventProducer(String name, String typeName, Class clazz) {
        super(name);
        _clazz = clazz;
        _topicName = typeName;
        if (clazz != null) {
            declareEventType(typeName, clazz);
        }
        _type = "Producer";
        output = new IOTerminal(_topicName, _topicName, null, this);
        setUsedMemory(0);
        setExecutionTime(0);
    }

    //private boolean declareEventType(String typeName, Class clazz) throws EventTypeException {
    private boolean declareEventType(String typeName, Class clazz) {
        if (!EventTypeRepository.getInstance().addEventType(typeName, clazz)) {
            //throw new EventTypeException("The type name " + typeName + " has already been defined");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Send the event to the corresponding event channel
     *
     * @param evt
     * @throws Exception
     */
    public void sendEvent(EventBean evt) throws Exception {
        if (evt != null) {
            EventBean[] evts = {evt};
            output.send(evts);
        }
        else 
            throw new NullPointerException("event is null");
    }

    /**
     * construct an event bean using the given event object
     *
     * @param o the event carrying the data to be send
     * @return true if the event all happens correctly or false otherwise
     */
    public EventBean createEventWithPayload(Object o) {
        try {
            ///String type = EventTypeRepository.getInstance().findByValue(o.getClass());
            if (_clazz != null) {
                EventBean evt = new EventBean();
                evt.getHeader().setDetectionTime(System.currentTimeMillis());
                evt.getHeader().setTypeIdentifier(_type);
                evt.getHeader().setPriority((short) 0);
                evt.getHeader().setProductionTime(System.currentTimeMillis());
                evt.getHeader().setProducerID(this.getName());
                BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());

                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

                for (int i = 0; i < pds.length; i++) {
                    String attribute = pds[i].getName();
                    if (attribute.equals("class")) {
                        continue;
                    }

                    Method getter = MethodUtils.getAccessibleMethod(o.getClass(), pds[i].getReadMethod());
                    if (getter != null) {
                        Serializable value = (Serializable) getter.invoke(o, null);
                        //Object value=BeanUtils.getProperty(o, attribute);
                        evt.payload.put(attribute, value);
                    }
                }
                return evt;
                // System.out.println(evt.sizeOf());         
            } else {
                throw new Exception("The underlying event type has not been registered");
            }

        } catch (Exception ex) {
            Logger.getLogger(EventProducer.class.getName()).log(Level.SEVERE, null, ex);

        }
        return null;
    }

    @Override
    public void run() {
    }

    @Override
    public Collection<IOTerminal> getInputTerminals() {
        return new ArrayList<>();
    }

    @Override
    public IOTerminal getOutputTerminal() {
        return output;
    }

    @Override
    public void process() {
    }

    @Override
    public boolean fetch() {
        return false;
    }

    
//    @Override
//    public void process(EventBean[] evts) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

//    @Override
//    public boolean openIOchannels() {
//        
//        return true;
//    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDefinedDelay() {
        return definedDelay;
    }

    public void setDefinedDelay(long definedDelay) {
        this.definedDelay = definedDelay;
    }
    
    
}
