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
 * Le SACOMUT est un boitier modulaire embarquant un processeur applicatif de type ARM9 (450MHz) 
 * @author epaln
 */
public class SacomutDevice extends Device {

    public SacomutDevice(String name) {
        super(name);
        this.setCpuSpeed(350);
        this.setTotalMemory(5000);
        this.setDeviceType(DeviceType.SACOMUT);
        this.setDeviceName(name);
        String imageURI = getClass().getClassLoader().getResource("image"+File.separator+"sacomut.jpg").getFile();
        icon= new MyLayeredIcon(new ImageIcon(imageURI).getImage());
        //icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"sacomut.jpg").getImage());
    }

    public SacomutDevice(String name, double cpuSpeed, int totalMemory) {
        super(name, cpuSpeed, totalMemory, DeviceType.SACOMUT);
         String imageURI = getClass().getClassLoader().getResource("image"+File.separator+"sacomut.jpg").getFile();
        icon= new MyLayeredIcon(new ImageIcon(imageURI).getImage());
    }

}
