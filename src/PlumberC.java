import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/******************************************************************************************************************
 * File:Plumber.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example to illstrate how to use the PlumberTemplate to create a main thread that
 * instantiates and connects a set of filters. This example consists of three filters: a source, a middle filter
 * that acts as a pass-through filter (it does nothing to the data), and a sink filter which illustrates all kinds
 * of useful things that you can do with the input stream of data.
 *
 * Parameters: 		None
 *
 * Internal Methods:	None
 *
 ******************************************************************************************************************/
public class PlumberC {
    public static void main(String argv[]) {
        /****************************************************************************
         * Here we instantiate three filters.
         ****************************************************************************/
        Date timer = new Date();
        long startTime = System.currentTimeMillis();
        List<Integer> columns = new ArrayList<>();
        columns.add(3);

        MonkeysSourceFilter Source1 = new MonkeysSourceFilter("SubSetA.dat");
        MonkeysSourceFilter Source2 = new MonkeysSourceFilter("SubSetB.dat");
        MonkeysMerger Merger = new MonkeysMerger();
        //MonkeysSplitter Splitter = new MonkeysSplitter();
        //MonkeysRowSelector Selector = new MonkeysRowSelector();
        //MonkeysWildDetector Detector = new MonkeysWildDetector();
        MonkeysSinkFilter Sink1 = new MonkeysSinkFilter("MergerOutPut.txt", false);
        //MonkeysSinkFilter Sink2 = new MonkeysSinkFilter("PressureWildPoints.txt", false);

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (Filter 1) which
         * we connect to Filter2 the middle filter. Then we connect Filter2 to the
         * source filter (Filter3).
         ****************************************************************************/

        try {
            /*
            Sink2.Connect(Detector);
            Sink1.Connect(Selector);
            Detector.Connect(Splitter);
            Selector.Connect(Splitter);
            Splitter.Connect(Merger);
            Merger.Connect(Source1, Source2);
            */
            Sink1.Connect(Merger);
            Merger.Connect(Source1, Source2);
            Source1.start();
            Source2.start();
            Merger.start();
            /*
            Splitter.start();
            Selector.start();
            Detector.start();
            */
            Sink1.start();
            //Sink2.start();
            Sink1.join();
            //Sink2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println("Duration: " + interval + " milliseconds.");
    } // main

} // Plumber