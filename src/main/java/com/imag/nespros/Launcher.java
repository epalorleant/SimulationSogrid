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
import com.imag.nespros.runtime.core.EPUnit;
import com.imag.nespros.runtime.core.FilterAgent;
import com.imag.nespros.runtime.core.GreatherOrEqualFilter;
import com.imag.nespros.runtime.core.TimeBatchWindow;
import com.imag.nespros.samples.consumer.Consumer;
import com.imag.nespros.samples.event.MeterEvent;
import com.imag.nespros.samples.producer.MeterSimulator;
import java.io.File;
import java.net.URI;
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
    final static String path = "dataset";
    static int NUMBER = 3;
    static long delay = 5000;
    static int duration = 0;
    
            
    public static void main(String[] args) throws URISyntaxException{
        //Sim_system.initialise();
        ArrayList<EPUnit> operators = new ArrayList<>();
        if (args.length == 3) {
            NUMBER = Integer.parseInt(args[0]);
            delay = Long.parseLong(args[1]);
            duration = Integer.parseInt(args[2]);
        }
        
        EventConsumer utility = new EventConsumer("Utility","Aggregated", new Consumer());
        operators.add(utility);        
        FilterAgent filter = new FilterAgent("FilterA","MeterEvent", "Filtered");
        filter.addFilter(new GreatherOrEqualFilter("realPowerWatts", 1d));
        AggregatorAgent aggregate = new AggregatorAgent("AVG_PWR", "Filtered", "Aggregated");
        aggregate.setWindowHandler(new TimeBatchWindow(10, TimeUnit.SECONDS));
        aggregate.addAggregator(new Avg("realPowerWatts", "avgPwr"));
        filter.setExecutionTime(1);
        filter.setUsedMemory(10);
        aggregate.setExecutionTime(1000);
        aggregate.setUsedMemory(50);
        operators.add(aggregate);
         operators.add(filter);
         ResourceLoader r = new ResourceLoader();
        File folder = r.getRessource(path);
        //File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].isHidden() && i < NUMBER) {
                String file = path + Sep + listOfFiles[i].getName();
                MeterSimulator simulator = new MeterSimulator(listOfFiles[i].getName(),"MeterEvent", file, meterSchema, types, delay, MeterEvent.class)
                        .simulate("realPowerWatts");
                 operators.add(simulator);                
                //AMIDevice device = new AMIDevice("AMI_"+i);
                //device.setTotalMemory(0);                
                //device.addEPUnit(simulator);
                simulator.setMapped(false);
                //Topology.getInstance().getGraph().addVertex(device);                
                //DeviceFactory.setAmiCount(i+1);
            }
        }
        EPGraph.getInstance().AddEPGraphFromList(operators);        
        g = GraphEditor.getInstance();
    }
}
