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
public class LastNWindow extends WindowHandler {

    //WindowAgent _wagent;
    int _size;
    Notifier notifier;

    public LastNWindow(int _size) {
        this._size = _size;
    }

    @Override
    public void register(EPUnit agent) {
        //  _wagent = agent;
        _agent = agent;
        Observable<Observable<EventBean>> windows = Reactive.window(_agent.getSourceStream(), _size, 1);

        windows.register(new ObserverAdapter<Observable<EventBean>>() {
            @Override
            public void next(Observable<EventBean> aWindow) {

                aWindow.register(new ObserverAdapter<EventBean>() {
                    //PriorityQueue<EventBean> res = new PriorityQueue<>(1000, new EventComparator());
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
                            res.clear();
                            EventBean evt = new EventBean();
                            evt.payload.put("window", evts);
//                            evt.getHeader().setIsComposite(true);
//                            evt.getHeader().setProductionTime(System.currentTimeMillis());
//                            evt.getHeader().setDetectionTime(evts[0].getHeader().getDetectionTime());
                            evt.getHeader().setTypeIdentifier("Window");
//                            evt.getHeader().setProducerID(_wagent.getName());
//                            evt.getHeader().setPriority((short)1);
//                            evt.payload.put("ttl", _wagent.TTL);
//                            _wagent.getOutputQueue().put(evt);
//                            _wagent.numEventProduced++;
//                            _wagent.getOutputNotifier().run();
//                            
                            //notifier = new Notifier(evts, _wagent.outputTerminal);
                            //notifier.start();
                            _agent.getInputQueue().put(evt);
                        }
                    }
                });
            }
        });
    }
}
