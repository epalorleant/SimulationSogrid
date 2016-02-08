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
public class DCDevice extends Device {

    public DCDevice(String name) {
        super(name);
        this.setCpuSpeed(100);
        this.setTotalMemory(700);
        this.setDeviceType(DeviceType.DC);
        this.setDeviceName(name);
         String imageURI = getClass().getClassLoader().getResource("image"+File.separator+"dc.jpeg").getFile();
        icon= new MyLayeredIcon(new ImageIcon(imageURI).getImage());
        //icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"dc.jpeg").getImage());
    }

    public DCDevice(String name, double cpuSpeed, int totalMemory) {
        super(name, cpuSpeed, totalMemory, DeviceType.DC);
        String imageURI = getClass().getClassLoader().getResource("image"+File.separator+"dc.jpeg").getFile();
        icon= new MyLayeredIcon(new ImageIcon(imageURI).getImage());
        //icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"dc.jpeg").getImage());
    }  
    
}
