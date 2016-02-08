/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros.network.routing;

import com.imag.nespros.network.devices.ComLink;
import com.imag.nespros.network.devices.Device;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author epaln
 */
public class EventPacket implements Serializable{
    private Device origin;
    private Device destination;
    private final ArrayList<ComLink> path = new ArrayList<>();
    private Object event;
    private String topic;
    private Color color = Color.BLUE;
    
    public EventPacket(Object event) {
        this.event = event;
    }

    public EventPacket(Device origin, Object event) {
        this.origin = origin;
        this.event = event;
    }

    public EventPacket(Device origin, Object event, String topic) {
        this.origin = origin;
        this.event = event;
        this.topic = topic;
    }

    public Device getOrigin() {
        return origin;
    }

    public void setOrigin(Device origin) {
        this.origin = origin;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    public ArrayList<ComLink> getPath() {
        return path;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public void setPathToDestination(){
        path.clear();
        path.addAll(Topology.getInstance().getPath(origin, destination));
    }
    
    public void setDestination(Device d){
        destination = d;
    }
    public Device getDestination(){
        return destination;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
}
