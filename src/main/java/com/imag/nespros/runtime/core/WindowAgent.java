/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.core;

import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.logging.MyLogger;
import com.imag.nespros.runtime.qosmonitor.QoSTuner;
import hu.akarnokd.reactive4java.base.Subject;
import hu.akarnokd.reactive4java.util.DefaultObservable;
import java.util.ArrayList;
import java.util.Collection;



/**
 *
 * @author epaln
 */
public class WindowAgent extends EPUnit {

    IOTerminal inputTerminal;
    IOTerminal outputTerminal;
    Subject<EventBean, EventBean> _sourceStream;
    WindowHandler _handler;

    public WindowAgent(String info, String IDinputTerminal, String IDoutputTerminal) {
        super(info);
        this.setName(this.getName() + "@" + getDevice().getDeviceName());
        _sourceStream = new DefaultObservable<EventBean>();
        this._info = info;
        this._type = "Window";
        this._receivers[0] = new TopicReceiver(this);
        inputTerminal = new IOTerminal(IDinputTerminal, "input channel " + _type, _receivers[0], this);
        outputTerminal = new IOTerminal(IDoutputTerminal, "output channel " + _type, this);
        _outputNotifier = new OQNotifier(this, QoSTuner.NOTIFICATION_PRIORITY);
        //Queue<EventBean> selected1 = Queues.newArrayDeque();
        //_selectedEvents[0] = selected1;
        logger = new MyLogger("WindowsMeasures");
        logger.log("Operator, isProduced, Processing Time, InputQ Size, OutputQ Size ");
    }

    @Override
    public Collection<IOTerminal> getInputTerminals() {
        ArrayList<IOTerminal> inputs = new ArrayList<IOTerminal>();
        inputs.add(inputTerminal);
        return inputs;
    }

    @Override
    public IOTerminal getOutputTerminal() {
        return outputTerminal;
    }

    public void setWindowHandler(WindowHandler handler) {
        this._handler = handler;
        //_handler.register(this);
    }

    @Override
    public void process() {
        while (!_selectedEvents.isEmpty()) {
            EventBean evt = _selectedEvents.poll();
            long ntime = System.nanoTime();

            evt.payload.put("#time#", System.currentTimeMillis()); // start processing this evt at #time#
            evt.payload.put("processTime", ntime);

            _sourceStream.next(evt);
            numEventProcessed++;
        }
    }

//    @Override
//    public void process(EventBean[] evts) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
