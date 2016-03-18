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
import com.imag.nespros.runtime.core.SlidingWindow;
import com.imag.nespros.runtime.core.TimeBatchWindow;
import com.imag.nespros.samples.consumer.Consumer;
import com.imag.nespros.samples.event.MeterEvent;
import com.imag.nespros.samples.producer.MeterSimulator;
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
        //Sim_system.initialise();
        ArrayList<EPUnit> operators = new ArrayList<>();
        if (args.length == 3) {
            NUMBER = Integer.parseInt(args[0]);
            delay = Long.parseLong(args[1]);
            duration = Integer.parseInt(args[2]);
        }
        
        Simulation s = new Simulation();      
        EventConsumer utility = new EventConsumer("Utility","Aggregated", new Consumer());
        //operators.add(utility);   
        
        
        FilterAgent filter = new FilterAgent("FilterA","MeterEvent", "Filtered");
        filter.addFilter(new GreatherOrEqualFilter("realPowerWatts", 1d));
        filter.setExecutionTime(1);
        filter.setUsedMemory(10);
        utility.getEPUList().add(filter);
                
        AggregatorAgent aggregate = new AggregatorAgent("AVG_PWR", "Filtered", "Aggregated");
        aggregate.setWindowHandler(new TimeBatchWindow(10, TimeUnit.SECONDS));
        aggregate.addAggregator(new Avg("realPowerWatts", "avgPwr"));        
        aggregate.setExecutionTime(1000);
        aggregate.setUsedMemory(50);
        utility.getEPUList().add(aggregate);
        /*
        FilterAgent filterB = new FilterAgent("FilterB","Filtered", "FilteredB");
        filterB.addFilter(new GreatherOrEqualFilter("realPowerWatts", 2d));
        filterB.setExecutionTime(1);
        filterB.setUsedMemory(10);
        utility.getEPUList().add(filterB);
        
        String[] inputs = {"FilteredB", "Aggregated"};
        DisjunctionAgent orAgent = new DisjunctionAgent("OR", inputs, "Result");
        orAgent.setExecutionTime(1);
        orAgent.setUsedMemory(15);
        utility.getEPUList().add(orAgent);
        //orAgent.setWindowHandler(new TimeBatchWindow(5, TimeUnit.SECONDS));
        */
        
         //operators.add(orAgent);
         //operators.add(filterB);        
        //operators.add(aggregate);
        //operators.add(filter);
       
       
         ResourceLoader r = new ResourceLoader();
        //File folder = new File(Thread.currentThread().getContextClassLoader().getResource(path).getFile());//r.getRessource(path);
        String[] listOfFiles = r.listFiles(path); 
       String[] inputOr = new String[NUMBER];
        for (int i = 0; i < listOfFiles.length; i++) {
            if ( i < NUMBER) {                                              
                MeterSimulator simulator = new MeterSimulator(listOfFiles[i].substring(path.length()+1),"MeterEvent", 
                        r.getRessource(listOfFiles[i]), meterSchema, types, delay, MeterEvent.class)
                        .simulate("realPowerWatts");
                 //operators.add(simulator);                                
                simulator.setMapped(false); 
                //utility.getEPUList().add(simulator);
                s.addProducer(simulator);
                inputOr[i]= simulator.getOutputTopic();
            }
            else{
                break;
            }
        }
         ConjunctionAgent or = new ConjunctionAgent("AND", inputOr, "MeterEvent");
         or.setUsedMemory(20);
         or.setExecutionTime(20);
         or.setWindowHandler(new SlidingWindow(6000, 1000, TimeUnit.MILLISECONDS));
         utility.getEPUList().add(or);
         s.addConsumer(utility);
         s.run();
        //EPGraph.getInstance().AddEPGraphFromList(operators);        
        //g = GraphEditor.getInstance();
    }
}
