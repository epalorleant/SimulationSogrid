/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros.network.devices;

import com.imag.nespros.gui.plugin.MyLayeredIcon;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author epaln
 */
public class AMIDevice extends Device {
    

    public AMIDevice(String name, double cpuSpeed, int totalMemory) {
        super(name, cpuSpeed, totalMemory, DeviceType.AMI);
        icon = new MyLayeredIcon(new ImageIcon("icons"+File.separator+"meter.jpeg").getImage());
    }

    public AMIDevice(String name) {
        super(name);
        this.setCpuSpeed(10);
        this.setTotalMemory(1000);
        this.setDeviceType(DeviceType.AMI);
        this.setDeviceName(name); 
        icon = new MyLayeredIcon(new ImageIcon("icons"+File.separator+"meter.jpeg").getImage());
    }
    
    
    
}
