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
public class UtilityDevice extends Device {

    public UtilityDevice(String name) {
        super(name);
        this.setCpuSpeed(500);
        this.setTotalMemory(10);
        this.setDeviceType(DeviceType.UTILITY);
        this.setDeviceName(name);
        icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"utility.jpg").getImage());
    }
    public UtilityDevice(String name, double cpuSpeed, int totalMemory) {
        super(name, cpuSpeed, totalMemory, DeviceType.UTILITY);
        icon= new MyLayeredIcon(new ImageIcon("icons"+File.separator+"utility.jpg").getImage());
    }
}
