/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.imag.nespros;

import com.imag.nespros.gui.plugin.GraphEditor;
import com.imag.nespros.network.routing.EPGraph;
import com.imag.nespros.runtime.client.EventConsumer;
import com.imag.nespros.runtime.core.AggregatorAgent;
import com.imag.nespros.runtime.core.Avg;
import com.imag.nespros.runtime.core.BatchNWindow;
import com.imag.nespros.runtime.core.ConjunctionAgent;
import com.imag.nespros.runtime.core.DisjunctionAgent;
import com.imag.nespros.runtime.core.EPUnit;
import com.imag.nespros.runtime.core.FilterAgent;
import com.imag.nespros.runtime.core.GreatherOrEqualFilter;
import com.imag.nespros.runtime.core.TimeBatchWindow;
import com.imag.nespros.samples.consumer.Consumer;
import com.imag.nespros.samples.event.MeterEvent;
import com.imag.nespros.samples.producer.MeterSimulator;
import com.imag.nespros.samples.producer.NTPClient;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;



/**
 *
 * @author epaln
 */
public class Launcher {
    static GraphEditor g;
    
    static String[] meterSchema = {"timestampUTC", "realPowerWatts", "meterID"};
    static String[] types = {"long", "double", "String"};
    static char Sep = File.separatorChar;
    final static String path = "microgrid";
    static int NUMBER = 3;
    static long delay = 5000;
    static int duration = 0;
    
            
    public static void main(String[] args) throws URISyntaxException{
        ArrayList<EPUnit> operators = new ArrayList<>();
        if (args.length == 3) {
            NUMBER = Integer.parseInt(args[0]);
            delay = Long.parseLong(args[1]);
            duration = Integer.parseInt(args[2]);
        }
        
        EventConsumer utility = new EventConsumer("Utility","NTP_Event");
        
        Consumer c =new Consumer(utility);
        utility.setHAndler(c);
        operators.add(utility);                
       
        for (int i = 0; i < NUMBER; i++) {
                                                        
                NTPClient client = new NTPClient("NTP_Client_"+i, "NTP_Event", delay);
                operators.add(client);
                client.setMapped(false);                 
        }
        EPGraph.getInstance().AddEPGraphFromList(operators);        
        g = GraphEditor.getInstance();
    }
}
