/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imag.netah.runtime.core;

/**
 *
 * @author epaln
 */
public abstract class WindowHandler {

    public EPUnit _agent;

    /**
     * register an observer to the source stream (observable) of the given
     * Window agent. The registered observer start a notification thread
     * (notifier) each time it receive a new window
     *
     * @param agent
     */
    public abstract void register(EPUnit agent);
}
