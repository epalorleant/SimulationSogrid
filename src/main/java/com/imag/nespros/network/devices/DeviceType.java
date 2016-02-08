/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros.network.devices;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *
 * @author epaln
 */
public enum DeviceType implements Serializable{
    AMI("Smart Meter"),
    HTA_COORD("HTA Coordinator"),
    DC("Data Concentrator"), 
    SACOMUT("SACOMUT"),
    UTILITY("Utility");
    
    DeviceType(String name){
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, name);
            fieldName.setAccessible(false);
        } catch (Exception e) {}
    }
    
}
