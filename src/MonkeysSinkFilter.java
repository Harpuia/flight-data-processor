/******************************************************************************************************************
 * File:SinkFilter.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This particular
 * filter reads some input from the filter's input port and does the following:
 *
 *	1) It parses the input stream and "decommutates" the measurement ID
 *	2) It parses the input steam for measurments and "decommutates" measurements, storing the bits in a long word.
 *
 * This filter illustrates how to convert the byte stream data from the upstream filterinto useable data found in
 * the stream: namely time (long type) and measurements (double type).
 *
 *
 * Parameters: 	None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MonkeysSinkFilter extends MonkeysFilterFramework {
    private String fileName;
    private boolean displayWildPoints;
    public MonkeysSinkFilter(String fileName, boolean displayWildPoints) {
        this.fileName = fileName;
        this.displayWildPoints=displayWildPoints;
    }

    public void run() {
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        byte[] byteArray;

        int MeasurementLength = 8;
        int IdLength = 4;

        byte databyte = 0;
        int bytesread = 0;

        long measurement;
        int id;
        int i;

        File file=null;
        Writer writer=null;

        try {

            file = new File(fileName);

            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), "UTF-8"));

        }
        catch (Exception e)
        {

        }
        System.out.print("\n" + this.getName() + "::Sink Reading ");

        while (true) {

            try {
                id = 0;

                for (i = 0; i < IdLength; i++) {
                    databyte = ReadFilterInputPort();

                    id = id | (databyte & 0xFF);

                    if (i != IdLength - 1)
                    {
                        id = id << 8;

                    }

                    bytesread++;

                }
                measurement = 0;

                for (i = 0; i < MeasurementLength; i++) {
                    databyte = ReadFilterInputPort();
                    measurement = measurement | (databyte & 0xFF);

                    if (i != MeasurementLength - 1)
                    {
                        measurement = measurement << 8;
                    }

                    bytesread++;

                }
                if (id == 0) {
                    TimeStamp.setTimeInMillis(measurement);


                }

                else if(id==3 && displayWildPoints && measurement < 0) {
                    writer.write(TimeStampFormat.format(TimeStamp.getTime()) + "  " +String.valueOf(new DecimalFormat("#0.00000").format(-Double.longBitsToDouble(measurement))) + "* \n");
                    writer.flush();
                }

                else {
                    writer.write(TimeStampFormat.format(TimeStamp.getTime()) + "  " +String.valueOf(new DecimalFormat("#0.00000").format(Double.longBitsToDouble(measurement))) + "\n");
                    writer.flush();
                }

            }
            catch(IOException ioe)
            {

            }
            catch(EndOfStreamException e)
            {
                try {
                    writer.close();
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread);
                    break;
                } catch (IOException ioe) {

                }
            }
        }
    }

}