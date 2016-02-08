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
 * un serveur HP ProLiant DL360  Gen 9  (rackable) avec  processeur 1.6GHz 6-core 1P  et  16GB  de RAM.
 * @author epaln
 */
public class HTACoordDevice extends Device {
    
    public HTACoordDevice(String name) {
         super(name);
        this.setCpuSpeed(500);
        this.setTotalMemory(10000);
        this.setDeviceType(DeviceType.HTA_COORD);
        this.setDeviceName(name);
        String imageURI = getClass().getClassLoader().getResource("image"+File.separator+"htaCoord.jpg").getFile();
        //icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"htaCoord.jpg").getImage());
        icon= new MyLayeredIcon(new ImageIcon(imageURI).getImage());
    }

    public HTACoordDevice(String name, double cpuSpeed, int totalMemory) {
        super(name, cpuSpeed, totalMemory, DeviceType.HTA_COORD);
        String imageURI = getClass().getClassLoader().getResource("image"+File.separator+"htaCoord.jpg").getFile();
        //icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"htaCoord.jpg").getImage());
        icon= new MyLayeredIcon(new ImageIcon(imageURI).getImage());
    }
    
    
}
