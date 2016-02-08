/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.core;

import com.google.common.collect.Queues;
import com.imag.nespros.runtime.event.EventBean;

import hu.akarnokd.reactive4java.base.Observable;
import hu.akarnokd.reactive4java.reactive.Reactive;
import hu.akarnokd.reactive4java.util.ObserverAdapter;
import java.util.Queue;


/**
 *
 * @author epaln
 */
public class BatchNWindow extends WindowHandler {

    //EPAgent _wagent;
    int _size;
    Notifier notifier;

    public BatchNWindow(int size) {
        _size = size;
    }

    @Override
    public void register(EPUnit agent) {
        //  _wagent = agent;
        _agent = agent;
        Observable<Observable<EventBean>> windows = Reactive.window(_agent.getSourceStream(), _size);

        windows.register(new ObserverAdapter<Observable<EventBean>>() {
            @Override
            public void next(Observable<EventBean> aWindow) {

                aWindow.register(new ObserverAdapter<EventBean>() {
                   // PriorityQueue<EventBean> res = new PriorityQueue<>(1000, new EventComparator());
                    Queue<EventBean> res = Queues.newArrayDeque();
                    @Override
                    public void next(EventBean evt) {
                        res.add(evt);
                    }

                    @Override
                    public void finish() {
                        if (!res.isEmpty()) {
                            EventBean[] evts;
                            evts = res.toArray(new EventBean[0]);
                            //long ptime = System.currentTimeMillis() - (long) evts[0].getValue("#time#");
                            //_wagent.processingTime += ptime;
                            // for (EventBean e : evts) {
                            //     e.payload.remove("#time#");
                            // }
                            res.clear();
                            EventBean evt = new EventBean();
                            // long processTime = (long) evts[0].getValue("processTime");
                            // evt.payload.put("processTime", processTime);
                            // for(EventBean e:evts){
                            //    e.payload.remove("processTime");
                            // }
                            evt.payload.put("window", evts);
//                            evt.getHeader().setIsComposite(true);
//                            evt.getHeader().setProductionTime(System.currentTimeMillis());
//                            evt.getHeader().setDetectionTime(evts[0].getHeader().getDetectionTime());
                            evt.getHeader().setTypeIdentifier("Window");
//                            evt.getHeader().setProducerID(_wagent.getName());
                            evt.getHeader().setPriority((short)1);
//                            evt.payload.put("ttl", _wagent.TTL);
//                            _wagent.getOutputQueue().put(evt);
//                            _wagent.numEventProduced++;
//                           _wagent.getExecutorService().execute(_wagent.getOutputNotifier());
                            _agent.getInputQueue().put(evt);
                        }
                    }
                });
            }
        });
    }
}
