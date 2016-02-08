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
import java.util.concurrent.TimeUnit;


/**
 *
 * @author epaln
 */
public class TimeBatchWindow extends WindowHandler {

    //WindowAgent _wagent;
    Notifier notifier;
    TimeUnit _unit;
    long _timespan;

    public TimeBatchWindow(long timespan, TimeUnit timeUnit) {
        this._unit = timeUnit;
        this._timespan = timespan;
    }

    @Override
    public void register(EPUnit agent) {
          _agent = agent;
        //_receiver = agent;
        Observable<Observable<EventBean>> windows = Reactive.window(_agent.getSourceStream(), _timespan, _unit);

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
                            if (!_agent.getType().equals("Negation")) {
                                EventBean[] evts;
                                EventBean evt = new EventBean();
                                evts = res.toArray(new EventBean[0]);
                                res.clear();
                                evt.payload.put("window", evts);
                                evt.getHeader().setTypeIdentifier("Window");
                                _agent.getInputQueue().put(evt);
                            }
                        } else {
                            if (_agent.getType().equals("Negation")) {
                                EventBean evt = new EventBean();
                                long ntime = System.nanoTime();
                                evt.payload.put("#time#", System.currentTimeMillis()); // start processing this evt at #time#
                                evt.payload.put("processTime", ntime);
                                _agent.getInputQueue().put(evt);
                            }
                        }
                    }
                });
            }
        });
    }
}
