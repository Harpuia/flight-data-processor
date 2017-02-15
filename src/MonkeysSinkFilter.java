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
import java.util.HashMap;

public class MonkeysSinkFilter extends MonkeysFilterFramework {
    private final HashMap<Integer, String> headers = new HashMap<Integer, String>();
    private String fileName;
    private boolean displayWildPoints;
    private int[] columnsOrder;

    /**
     * Initialize the MonkeysSinkFilter class.
     *
     * @param fileName          the output file we should write data into.
     * @param columnsOrder      the order of the data order.
     * @param displayWildPoints the boolean type to show if the wild point should be dispalyed.
     */
    public MonkeysSinkFilter(String fileName, int[] columnsOrder, boolean displayWildPoints) {
        this.fileName = fileName;
        this.displayWildPoints = displayWildPoints;
        //Initializing the column order array.
        if (columnsOrder == null) {
            this.columnsOrder = new int[]{0, 1, 2, 3, 4, 5};
        } else {
            this.columnsOrder = columnsOrder;
        }
        //Initializing headers
        headers.put(0, "Time");
        headers.put(1, "Velocity");
        headers.put(2, "Altitude");
        headers.put(3, "Pressure");
        headers.put(4, "Temperature");
        headers.put(5, "Attitude");
    }

    /**
     * The main function to write data into the output file.
     */
    public void run() {
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        int MeasurementLength = 8;
        int IdLength = 4;

        byte dataByte = 0;
        int bytesRead = 0;

        long measurement;
        int id;

        File file = null;
        Writer writer = null;

        //Starting writing data into the output file.
        try {

            file = new File(fileName);

            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), "UTF-8"));
            //Printing header
            StringBuilder header = new StringBuilder();
            for (int i : columnsOrder) {
                header.append(headers.get(i) + "\t");
            }
            writer.write(header.toString());
            writer.flush();
        } catch (Exception e) {
        }

        System.out.print("\n" + this.getName() + "::Sink Reading ");

        //Creating a hash map to store the ID-measurement pair of the inputs.
        HashMap<Integer, Long> frame = new HashMap<Integer, Long>();
        int counter = 0;
        while (true) {
            try {
                id = 0;
                //Reading ID
                for (int i = 0; i < IdLength; i++) {
                    dataByte = ReadFilterInputPort();
                    id = id | (dataByte & 0xFF);
                    if (i != IdLength - 1) {
                        id = id << 8;
                    }
                    bytesRead++;
                }
                measurement = 0;
                //Reading measurement
                for (int i = 0; i < MeasurementLength; i++) {
                    dataByte = ReadFilterInputPort();
                    measurement = measurement | (dataByte & 0xFF);

                    if (i != MeasurementLength - 1) {
                        measurement = measurement << 8;
                    }
                    bytesRead++;
                }
                frame.put(id, measurement);
                counter++;
                if (counter == columnsOrder.length) {
                    writer.write("\n");
                    writer.flush();

                    //Write items in correct order
                    for (int i : columnsOrder) {
                        PrintMeasurement(i, frame.get(i), writer, TimeStamp, TimeStampFormat);
                    }
                    frame.clear();
                    counter = 0;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (EndOfStreamException e) {
                try {
                    writer.close();
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead);
                    break;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

    }

    /**
     * Print the measurement based on IDs.
     *
     * @param id              the int type ID of the input data.
     * @param measurement     the long type measurement of the input data.
     * @param writer          the writer object to write data into output file.
     * @param timeStamp       the timestamp to show the time measurement.
     * @param timeStampFormat the time stamp fomat of the time measurement.
     */
    private void PrintMeasurement(int id, long measurement, Writer writer, Calendar timeStamp, SimpleDateFormat timeStampFormat) throws IOException {
        //If the id equals 0, which means this is the time measurement, then we need to format it to output.
        if (id == 0) {
            timeStamp.setTimeInMillis(measurement);
            writer.write(timeStampFormat.format(timeStamp.getTime()) + "\t");
            writer.flush();
            //If the id is 3, displayWildPoints is true, and measurement <0, we need to format the output as below.
        } else if (id == 3 && displayWildPoints && measurement < 0) {
            writer.write(String.valueOf(new DecimalFormat("#0.00000").format(-Double.longBitsToDouble(measurement))) + "*\t");
            writer.flush();
            //Otherwise, format the output as below.
        } else {
            writer.write(String.valueOf(new DecimalFormat("#0.00000").format(Double.longBitsToDouble(measurement))) + "\t");
            writer.flush();
        }
    }
}