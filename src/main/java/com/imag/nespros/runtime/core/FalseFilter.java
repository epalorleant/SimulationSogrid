/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.nespros.runtime.core;

import com.imag.nespros.runtime.base.Func1;
import com.imag.nespros.runtime.event.EventBean;



/**
 *
 * @author epaln
 */
public class FalseFilter implements Func1<EventBean, Boolean> {

    @Override
    public Boolean invoke(EventBean param1) {
        return false;
    }

}
