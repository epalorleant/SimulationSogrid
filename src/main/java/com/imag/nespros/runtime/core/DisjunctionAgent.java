/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.core;


import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.event.EventComparator2;
import com.imag.nespros.runtime.logging.MyLogger;
import com.imag.nespros.runtime.qosmonitor.QoSTuner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 *
 * @author epaln
 */
public class DisjunctionAgent extends EPUnit {

    IOTerminal inputTerminalL;
    IOTerminal inputTerminalR;
    IOTerminal outputTerminal;

    public DisjunctionAgent(String info, String IDinputTerminalL, String IDinputTerminalR, String IDoutputTerminal) {
        super(info);        
        this._info = info;
        this._type = "Disjunction";
        this._receivers[0] = new TopicReceiver(this, (short) 0);
        this._receivers[1] = new TopicReceiver(this, (short) 1);
        inputTerminalL = new IOTerminal(IDinputTerminalL, "input channel " + _type, _receivers[0], this);
        inputTerminalR = new IOTerminal(IDinputTerminalR, "input channel " + _type, _receivers[1], this);
        outputTerminal = new IOTerminal(IDoutputTerminal, "output channel " + _type, this);
        _outputNotifier = new OQNotifier(this, QoSTuner.NOTIFICATION_PRIORITY);
        logger = new MyLogger("DisjunctionMeasures");
        logger.log("Operator, isProduced, Processing Time, InputQ Size, OutputQ Size ");
    }

    @Override
    public Collection<IOTerminal> getInputTerminals() {
        ArrayList<IOTerminal> inputs = new ArrayList<IOTerminal>();
        inputs.add(inputTerminalL);
        inputs.add(inputTerminalR);
        return inputs;
    }

    @Override
    public IOTerminal getOutputTerminal() {
        return outputTerminal;
    }

    @Override
    public void process() {
        EventBean[] Values = new EventBean[1];
        ArrayList<EventBean> lValues, rValues;

        // statistics: #events processed, processing time
        long time = System.currentTimeMillis();
        long ntime = System.nanoTime(); // to compute the processing time for that cycle
        EventBean select = _selectedEvents.poll();

        if (select != null) {
            if (select.getHeader().getTypeIdentifier().equals("Window")) {
                Values = (EventBean[]) select.getValue("window");
            } else {
                Values = new EventBean[1];
                Values[0] = select;
            }
        }
        
        lValues = new ArrayList<>();
        rValues = new ArrayList<>();

        for (EventBean e : Values) {
            if (e.getValue("flag").toString().equals("0")) {
                lValues.add(e);
            } else {
                rValues.add(e);
            }
            e.payload.remove("flag");
        }
        EventBean[] lV, rV;
            lV = (EventBean[]) lValues.toArray(new EventBean[0]);
            rV = (EventBean[]) rValues.toArray(new EventBean[0]);
        if (!lValues.isEmpty() && !rValues.isEmpty()) {
            
            // many event instances can match... the selection mode should clearly define the event to select
            switch (selectionMode) {
                case SelectionMode.MODE_CONTINUOUS: {
                    for (EventBean l : lValues) {
                        for (EventBean r : rValues) {
                            // long ntime = System.nanoTime();
                            EventBean ec = new EventBean();
                            ec.getHeader().setDetectionTime(Math.min(r.getHeader().getDetectionTime(), l.getHeader().getDetectionTime()));
                            ec.getHeader().setPriority((short) Math.max(l.getHeader().getPriority(), r.getHeader().getPriority()));
                            ec.getHeader().setIsComposite(true);
                            ec.getHeader().setProductionTime(System.currentTimeMillis());
                            ec.getHeader().setProducerID(this.getName());
                            ec.getHeader().setTypeIdentifier("Disjunction");
                            ec.payload.put("l", l);
                            ec.payload.put("r", r);
                            ec.payload.put("ttl", TTL);
                            ec.payload.put("processTime", ntime);
                            _outputQueue.put(ec);
                            numEventProduced++;
                            getExecutorService().execute(getOutputNotifier());
                        }
                    }
                }
                break;
                case SelectionMode.MODE_CHRONOLOGIC: {
                    //long ntime = System.nanoTime();

                    Arrays.sort(lV, 0, lV.length - 1, new EventComparator2());
                    Arrays.sort(rV, 0, rV.length - 1, new EventComparator2());
                    EventBean ec = new EventBean();
                    ec.getHeader().setDetectionTime(Math.min(rV[0].getHeader().getDetectionTime(), lV[0].getHeader().getDetectionTime()));
                    ec.getHeader().setPriority((short) Math.max(lV[0].getHeader().getPriority(), rV[0].getHeader().getPriority()));
                    ec.getHeader().setIsComposite(true);
                    ec.getHeader().setProductionTime(System.currentTimeMillis());
                    ec.getHeader().setProducerID(this.getName());
                    ec.getHeader().setTypeIdentifier("Disjunction");
                    ec.payload.put("l", lV[0]);
                    ec.payload.put("r", rV[0]);
                    ec.payload.put("ttl", TTL);
                    ec.payload.put("processTime", ntime);
                    _outputQueue.put(ec);
                    numEventProduced++;
                    getExecutorService().execute(getOutputNotifier());

                }
                break;

                case SelectionMode.MODE_PRIORITY: {
                    //long ntime = System.nanoTime();
                    EventBean ec = new EventBean();
                    ec.getHeader().setDetectionTime(Math.min(rV[0].getHeader().getDetectionTime(), lV[0].getHeader().getDetectionTime()));
                    ec.getHeader().setPriority((short) Math.max(lV[0].getHeader().getPriority(), rV[0].getHeader().getPriority()));
                    ec.getHeader().setIsComposite(true);
                    ec.getHeader().setProductionTime(System.currentTimeMillis());
                    ec.getHeader().setProducerID(this.getName());
                    ec.getHeader().setTypeIdentifier("Disjunction");
                    ec.payload.put("l", lV[0]);
                    ec.payload.put("r", rV[0]);
                    ec.payload.put("ttl", TTL);
                    ec.payload.put("processTime", ntime);
                    _outputQueue.put(ec);
                    numEventProduced++;
                    getExecutorService().execute(getOutputNotifier());
                }
                break;

                default: { /// mode recent
                    //long ntime = System.nanoTime();
                    Arrays.sort(lV, 0, lV.length - 1, new EventComparator2());
                    Arrays.sort(rV, 0, rV.length - 1, new EventComparator2());
                    EventBean ec = new EventBean();
                    ec.getHeader().setDetectionTime(Math.min(rV[rV.length - 1].getHeader().getDetectionTime(), lV[lV.length - 1].getHeader().getDetectionTime()));
                    ec.getHeader().setPriority((short) Math.max(lV[lV.length - 1].getHeader().getPriority(), rV[rV.length - 1].getHeader().getPriority()));
                    ec.getHeader().setIsComposite(true);
                    ec.getHeader().setProductionTime(System.currentTimeMillis());
                    ec.getHeader().setProducerID(this.getName());
                    ec.getHeader().setTypeIdentifier("Disjunction");
                    ec.payload.put("l", lV[lV.length - 1]);
                    ec.payload.put("r", rV[rV.length - 1]);
                    ec.payload.put("ttl", TTL);
                    ec.payload.put("processTime", ntime);
                    _outputQueue.put(ec);
                    numEventProduced++;
                    getExecutorService().execute(getOutputNotifier());
                }
                break;
            }
          
        } else {
            if (!lValues.isEmpty()) {
                // many event instances can match... the selection mode should clearly define the event to select
                switch (selectionMode) {
                    case SelectionMode.MODE_CONTINUOUS: {
                        for (EventBean l : lValues) {

                            //EventBean ec = new EventBean();
                            //ec.getHeader().setDetectionTime(l.getHeader().getDetectionTime());
                            //ec.getHeader().setPriority(l.getHeader().getPriority());
                            l.getHeader().setIsComposite(true);
                            l.getHeader().setProductionTime(System.currentTimeMillis());
                            //ec.getHeader().setProducerID(this.getName());
                            l.getHeader().setTypeIdentifier("Disjunction");
                            //ec.payload.put("l", l);
                            l.payload.put("ttl", TTL);
                            l.payload.put("processTime", ntime);
                            _outputQueue.put(l);
                            numEventProduced++;
                            getExecutorService().execute(getOutputNotifier());
                        }
                    }
                    break;
                    case SelectionMode.MODE_CHRONOLOGIC: {

                        Arrays.sort(lV, 0, lV.length - 1, new EventComparator2());
                        //EventBean ec = new EventBean();
                        //ec.getHeader().setDetectionTime(lV[0].getHeader().getDetectionTime());
                        //ec.getHeader().setPriority(lV[0].getHeader().getPriority());
                        lV[0].getHeader().setIsComposite(true);
                        lV[0].getHeader().setProductionTime(System.currentTimeMillis());
                        //ec.getHeader().setProducerID(this.getName());
                        lV[0].getHeader().setTypeIdentifier("Disjunction");
                        //ec.payload.put("l", lV[0]);
                        lV[0].payload.put("ttl", TTL);
                        lV[0].payload.put("processTime", ntime);
                        _outputQueue.put(lV[0]);
                        numEventProduced++;
                        getExecutorService().execute(getOutputNotifier());
                    }
                    break;

                    case SelectionMode.MODE_PRIORITY: {

                        //EventBean ec = new EventBean();
                        //ec.getHeader().setDetectionTime(lV[0].getHeader().getDetectionTime());
                        //ec.getHeader().setPriority(lV[0].getHeader().getPriority());
                        lV[0].getHeader().setIsComposite(true);
                        lV[0].getHeader().setProductionTime(System.currentTimeMillis());
                        //ec.getHeader().setProducerID(this.getName());
                        lV[0].getHeader().setTypeIdentifier("Disjunction");
                        //ec.payload.put("l", lV[0]);
                        lV[0].payload.put("ttl", TTL);
                        lV[0].payload.put("processTime", ntime);
                        _outputQueue.put(lV[0]);
                        numEventProduced++;
                        getExecutorService().execute(getOutputNotifier());
                    }
                    break;

                    default: { /// mode recent

                        Arrays.sort(lV, 0, lV.length - 1, new EventComparator2());
                        //EventBean ec = new EventBean();
                        //ec.getHeader().setDetectionTime(lV[lV.length - 1].getHeader().getDetectionTime());
                        //ec.getHeader().setPriority(lV[lV.length - 1].getHeader().getPriority());
                        lV[lV.length - 1].getHeader().setIsComposite(true);
                        lV[lV.length - 1].getHeader().setProductionTime(System.currentTimeMillis());
                        //ec.getHeader().setProducerID(this.getName());
                        lV[lV.length - 1].getHeader().setTypeIdentifier("Disjunction");
                        //lV[lV.length - 1].payload.put("l", lV[lV.length - 1]);
                        lV[lV.length - 1].payload.put("ttl", TTL);
                        lV[lV.length - 1].payload.put("processTime", ntime);
                        _outputQueue.put(lV[lV.length - 1]);
                        numEventProduced++;
                        getExecutorService().execute(getOutputNotifier());
                    }
                    break;
                }
                // removes the processed events from the selected events
            }

            if (!rValues.isEmpty()) {
                // many event instances can match... the selection mode should clearly define the event to select

                switch (selectionMode) {
                    case SelectionMode.MODE_CONTINUOUS: {
                        for (EventBean r : rValues) {

                            //EventBean ec = new EventBean();
                            //ec.getHeader().setDetectionTime(r.getHeader().getDetectionTime());
                            //ec.getHeader().setPriority(r.getHeader().getPriority());
                            r.getHeader().setIsComposite(true);
                            r.getHeader().setProductionTime(System.currentTimeMillis());
                            //ec.getHeader().setProducerID(this.getName());
                            r.getHeader().setTypeIdentifier("Disjunction");
                            //ec.payload.put("r", r);
                            r.payload.put("ttl", TTL);
                            r.payload.put("processTime", ntime);
                            _outputQueue.put(r);
                            numEventProduced++;
                            getExecutorService().execute(getOutputNotifier());
                        }
                    }
                    break;
                    case SelectionMode.MODE_CHRONOLOGIC: {

                        Arrays.sort(rV, 0, rV.length - 1, new EventComparator2());
                        //EventBean ec = new EventBean();
                        //ec.getHeader().setDetectionTime(rV[0].getHeader().getDetectionTime());
                        //ec.getHeader().setPriority(rV[0].getHeader().getPriority());
                        rV[0].getHeader().setIsComposite(true);
                        rV[0].getHeader().setProductionTime(System.currentTimeMillis());
                        //ec.getHeader().setProducerID(this.getName());
                        rV[0].getHeader().setTypeIdentifier("Disjunction");
                        //ec.payload.put("r", rV[0]);
                        rV[0].payload.put("ttl", TTL);
                        rV[0].payload.put("processTime", ntime);
                        _outputQueue.put(rV[0]);
                        numEventProduced++;
                        getExecutorService().execute(getOutputNotifier());
                    }
                    break;

                    case SelectionMode.MODE_PRIORITY: {

                        //EventBean ec = new EventBean();
                        //ec.getHeader().setDetectionTime(rV[0].getHeader().getDetectionTime());
                        //ec.getHeader().setPriority(rV[0].getHeader().getPriority());
                        rV[0].getHeader().setIsComposite(true);
                        rV[0].getHeader().setProductionTime(System.currentTimeMillis());
                        //ec.getHeader().setProducerID(this.getName());
                        rV[0].getHeader().setTypeIdentifier("Disjunction");
                        //ec.payload.put("r", rV[0]);
                        rV[0].payload.put("ttl", TTL);
                        rV[0].payload.put("processTime", ntime);
                        _outputQueue.put(rV[0]);
                        numEventProduced++;
                        getExecutorService().execute(getOutputNotifier());
                    }
                    break;

                    default: { /// mode recent

                        Arrays.sort(rV, 0, rV.length - 1, new EventComparator2());
                        //EventBean ec = new EventBean();
                        //ec.getHeader().setDetectionTime(rV[rV.length - 1].getHeader().getDetectionTime());
                        //ec.getHeader().setPriority(rV[rV.length - 1].getHeader().getPriority());
                        rV[rV.length - 1].getHeader().setIsComposite(true);
                        rV[rV.length - 1].getHeader().setProductionTime(System.currentTimeMillis());
                        //ec.getHeader().setProducerID(this.getName());
                        rV[rV.length - 1].getHeader().setTypeIdentifier("Disjunction");
                        //ec.payload.put("r", rV[rV.length - 1]);
                        rV[rV.length - 1].payload.put("ttl", TTL);
                        rV[rV.length - 1].payload.put("processTime", ntime);
                        _outputQueue.put(rV[rV.length - 1]);
                        numEventProduced++;
                        getExecutorService().execute(getOutputNotifier());
                    }
                    break;
                }
            }
        }
        // update statistics: processing time, number event processed
        time = System.currentTimeMillis() - time;
        numEventProcessed += (Values.length);
        processingTime += time;
        // clear the processed events
    }

}
