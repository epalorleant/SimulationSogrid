/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.core;


import com.google.common.collect.Queues;
import com.imag.nespros.network.devices.Device;
import com.imag.nespros.runtime.base.BoundedPriorityBlockingQueue;
import com.imag.nespros.runtime.event.EventBean;
import com.imag.nespros.runtime.logging.MyLogger;
import com.imag.nespros.runtime.qosmonitor.QoSConstraint;

import hu.akarnokd.reactive4java.base.Subject;
import hu.akarnokd.reactive4java.util.DefaultObservable;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//import log.MyLogger;

/**
 *
 * @author epaln
 */
public abstract class EPUnit extends Thread {

    protected String _type;
    protected TopicReceiver[] _receivers;
    protected String _info;
    protected Queue<EventBean> _selectedEvents;
    protected BoundedPriorityBlockingQueue _outputQueue;
    protected BoundedPriorityBlockingQueue _inputQueue;
    public int TTL; // ttl value, which respect to the starvation problem...
    protected OQNotifier _outputNotifier;
    private QoSConstraint qosConstraint;
    public volatile float sumLatencies = 0;
    public volatile float sumNetworkLatencies = 0;
    public volatile int numEventNotifiedNetwork = 0; // number of events notified over the network via Relayer.callPublish(...)
    public volatile long numEventNotified = 0;       // number of events notified either by Relayer.callPublish(...) or by pub/sub
    public volatile int numAchievedNotifications = 0; // number of calls to Relayer.callPublish(...)
    public volatile int numEventProcessed = 0;
    public volatile int numEventProduced = 0;
    public volatile long processingTime = 0;
    protected short selectionMode = SelectionMode.MODE_PRIORITY;
    protected MyLogger logger;
    protected ExecutorService executorService;
    WindowHandler _handler;
    private Subject<EventBean, EventBean> _sourceStream;
    // the mapped device
    private Device device;
    // operators needs in terms of ressources
    private int executionTime;
    private int usedMemory;
    // operator mapoped?
    private boolean mapped;
    public static final short MAX= 0;
    public static final short MIN= 1;
    public static final short AVG= 2;
    public static final short SUM= 3;
    
    protected short priorityFunction = 0;
    
    public EPUnit(String info) {
        super(info);
        this._info = info;
        _selectedEvents = Queues.newArrayDeque();
        _receivers = new TopicReceiver[2];
        _outputQueue = new BoundedPriorityBlockingQueue(this);
         _inputQueue = new BoundedPriorityBlockingQueue(null);
        TTL = _outputQueue.getCapacity() * 2; // avoiding starvation after two time the capacity of the output queue
        qosConstraint = new QoSConstraint();
        executorService = Executors.newCachedThreadPool();
        _handler = new BatchNWindow(1);
        _sourceStream = new DefaultObservable<>();
        mapped = false;
    }

    public short getPriorityFunction() {
        return priorityFunction;
    }

    public void setPriorityFunction(short priorityFunction) {
        this.priorityFunction = priorityFunction;
    }

    
    public BoundedPriorityBlockingQueue getOutputQueue() {
        return _outputQueue;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int getNumAchievedNotifications() {
        return numAchievedNotifications;
    }

    public void setNumAchievedNotifications(int numAchievedNotifications) {
        this.numAchievedNotifications = numAchievedNotifications;
    }

   // public abstract Collection<IOTerminal> getInputTerminals();
 public abstract Collection<IOTerminal> getInputTerminals();
 public abstract IOTerminal getOutputTerminal();

    public String[] getInputTopics() {
        String[] topics = new String[getInputTerminals().size()];
        short i = 0;
        for (IOTerminal t : getInputTerminals()) {
            topics[i] = t.getTopic();
        }
        return topics;
    }

    public MyLogger getLogger() {
        return logger;
    }

    public String getOutputTopic() {
        return getOutputTerminal().getTopic();
    }

    public abstract void process();

    public boolean fetch() {
        try {           
              _selectedEvents.add((EventBean) this.getInputQueue().take());                        
        } catch (InterruptedException ex) {
            Logger.getLogger(EPUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return !_selectedEvents.isEmpty();
    }

    public boolean openIOchannels() {

        for (IOTerminal input : getInputTerminals()) {
            input.open();
        }

       // IOTerminal outputTerm = getOutputTerminal();
       // if(outputTerm != null){
        //    outputTerm.open();
       // }
        return true;
    }

    @Override
    public void run() {
        registerWindowHandler();
        while (true) {
            if (fetch()) {
                process();
            }
        }
    }

    public int getTTL() {
        return TTL;
    }

    public OQNotifier getOutputNotifier() {
        return _outputNotifier;
    }

    public QoSConstraint getQosConstraint() {
        return qosConstraint;
    }

    public void setQosConstraint(QoSConstraint qosConstraint) {
        this.qosConstraint = qosConstraint;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public String getInfo() {
        return _info;
    }

    public void setInfo(String _info) {
        this._info = _info;
    }

    @Override
    public String toString() {
        return getName();
    }

    public TopicReceiver[] getReceivers() {
        return _receivers;
    }

    public void setReceivers(TopicReceiver[] _receivers) {
        this._receivers = _receivers;
    }

//    public void setWindow(List<WindowHandler> handlers) {
//        for (int i = 0; i < getInputTerminals().size(); i++) {
//            _receivers[i].setWindowHandler(handlers.get(i));
//            //_receivers[i].registerWindowHandler();
//        }
//    }

//    private void registerWindowHandler() {
//        for (int i = 0; i < getInputTerminals().size(); i++) {
//            _receivers[i].registerWindowHandler();
//        }
//    }

        public BoundedPriorityBlockingQueue getInputQueue() {
        return _inputQueue;
    }

    public void registerWindowHandler() {
        _handler.register(this);
    }

    public void setWindowHandler(WindowHandler handler) {
        this._handler = handler;
    }

    public Subject<EventBean, EventBean> getSourceStream() {
        return _sourceStream;
    }

    public short getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(short selectionMode) {
        this.selectionMode = selectionMode;
    }

    public void shutdonwn() {
        getExecutorService().shutdown();
        this.interrupt();
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(int usedMemory) {
        this.usedMemory = usedMemory;
    }

    public boolean isMapped() {
        return mapped;
    }

    public void setMapped(boolean mapped) {
        this.mapped = mapped;
    }
     
}
