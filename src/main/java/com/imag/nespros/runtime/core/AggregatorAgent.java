/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.core;


import com.imag.nespros.runtime.base.NameValuePair;
import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.logging.MyLogger;
import com.imag.nespros.runtime.qosmonitor.QoSTuner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 *
 * @author epaln
 */
public class AggregatorAgent extends EPUnit {

    IOTerminal inputTerminal;
    IOTerminal outputTerminal;
    List<Aggregate> aggregators = new ArrayList<>();

    public AggregatorAgent(String info, String IDinputTerminal, String IDoutputTerminal) {
        super(info);
        //this._filter = filter;        
        this._info = info;
        this._type = "Aggregator";
        this._receivers[0] = new TopicReceiver(this);
        inputTerminal = new IOTerminal(IDinputTerminal, "input channel " + _type, _receivers[0], this);
        outputTerminal = new IOTerminal(IDoutputTerminal, "output channel " + _type, this);
        _outputNotifier = new OQNotifier(this, QoSTuner.NOTIFICATION_PRIORITY);
        logger = new MyLogger("AggregatorMeasures");
        logger.log("Operator, isProduced, Processing Time, InputQ Size, OutputQ Size ");
    }

    public void addAggregator(Aggregate aggregator) {
        this.aggregators.add(aggregator);
    }

    public List<Aggregate> getAggregators() {
        return aggregators;
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

    @Override
    public void process() {
        //EventBean evt = aggregator.aggregate(_selectedEvents.toArray(new EventBean[0]));
        EventBean[] operands;
        //start processing time
        long time = System.currentTimeMillis();
        long ntime = System.nanoTime();
        EventBean evt = _selectedEvents.poll();
        if (evt.getHeader().getTypeIdentifier().equals("Window")) {
            operands = (EventBean[]) evt.getValue("window");
        } else {
            operands = new EventBean[1];
            operands[0] = evt;
        }
        // update the number of event processed by this EPU
        numEventProcessed += operands.length;
        short maxPriority = 0;
        for (EventBean e : operands) {
            if (e.getHeader().getPriority() > maxPriority) {
                maxPriority = e.getHeader().getPriority();
            }
        }
        EventBean ec = new EventBean();
        ec.getHeader().setDetectionTime(operands[0].getHeader().getDetectionTime());
        ec.getHeader().setIsComposite(true);
        ec.getHeader().setProducerID(this.getName());
        ec.getHeader().setProductionTime(System.currentTimeMillis());
        ec.getHeader().setTypeIdentifier("Aggregate");
        ec.payload.put("processTime", ntime);
        ec.getHeader().setPriority(maxPriority);
        for (Aggregate aggregator : aggregators) {
            NameValuePair res = aggregator.aggregate(operands);
            ec.payload.put(res.getAttribute(), (Serializable) res.getValue());
        }

        ec.payload.put("ttl", TTL);
        _outputQueue.put(ec);
        numEventProduced++;
        // update the processing time of this EPU
        time = System.currentTimeMillis() - time;
        processingTime += time;
        getExecutorService().execute(getOutputNotifier());
    }
}
