/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.client;

import com.imag.nespros.runtime.event.EventBean;



/**
 *
 * @author epaln
 */
public interface AnEventHandler {

    public void notify(EventBean[] evts);
}
