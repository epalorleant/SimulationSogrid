/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros.network.devices;

import com.imag.nespros.gui.plugin.MyLayeredIcon;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author epaln
 */
public class DCDevice extends Device {

    public DCDevice(String name) {
        super(name);
        this.setCpuSpeed(100);
        this.setTotalMemory(700);
        this.setDeviceType(DeviceType.DC);
        this.setDeviceName(name);
         
        try {
            byte[] imageInByte;            
            imageInByte = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("image/dc.jpeg"));            
            icon = new MyLayeredIcon(new ImageIcon(imageInByte).getImage());
        } catch (IOException ex) {
            Logger.getLogger(AMIDevice.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }

    public DCDevice(String name, double cpuSpeed, int totalMemory) {
        super(name, cpuSpeed, totalMemory, DeviceType.DC);
        try {
            byte[] imageInByte;            
            imageInByte = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("image/dc.jpeg"));            
            icon = new MyLayeredIcon(new ImageIcon(imageInByte).getImage());
        } catch (IOException ex) {
            Logger.getLogger(AMIDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
}
