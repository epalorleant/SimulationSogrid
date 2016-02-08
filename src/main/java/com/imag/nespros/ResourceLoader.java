/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros;

import java.io.File;

/**
 *
 * @author epaln
 */
public class ResourceLoader {
    
    public File getRessource(String name){
        ClassLoader cl = this.getClass().getClassLoader();        
        return new File(cl.getResource(name).getFile());
    }
    
}
