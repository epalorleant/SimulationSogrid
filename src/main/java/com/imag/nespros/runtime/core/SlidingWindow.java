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
public class SlidingWindow extends WindowHandler {

    // WindowAgent _wagent;
    Notifier notifier;
    TimeUnit _unit;
    long _timespan;
    long _timeshift;
    //int sumPriority =0;

    public SlidingWindow(long timespan, long timeshift, TimeUnit timeUnit) {
        this._unit = timeUnit;
        this._timespan = timespan;
        this._timeshift = timeshift;
    }

    @Override
    public void register(EPUnit agent) {
        //  _wagent = agent;
        _agent = agent;

        Observable<Observable<EventBean>> windows = Reactive.window(_agent.getSourceStream(), _timespan, _timeshift, _unit);

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
                                evts = res.toArray(new EventBean[0]);
                                res.clear();
                                EventBean evt = new EventBean();
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
