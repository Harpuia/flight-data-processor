import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * MonkeysMerger is a filter that takes input from two filters and merge the results sorted increasingly based on time.
 */
public class MonkeysMerger extends MonkeysFilterFramework {
    /**
     * Input ports, input filter instances and output port.
     * MeasurementLength is the length of all measurements (including time) in bytes
     * IdLength is the length of IDs in the byte stream
     * frameCol refers to how many measurements we have in one frame.
     **/
    private PipedInputStream InputReadPort1 = new PipedInputStream();
    private PipedInputStream InputReadPort2 = new PipedInputStream();
    private PipedOutputStream OutputWritePort = new PipedOutputStream();
    private final int MeasurementLength = 8;
    private final int IdLength = 4;
    private final int frameCol = 6;
    private MonkeysFilterFramework InputFilter1;
    private MonkeysFilterFramework InputFilter2;

    /**
     * This function identifies if the stream reaches to the end of the input.
     * @param InputFilter specifies which input filter we read from.
     * @return true if the filter reaches the end of the input.
     */
    private boolean EndOfInputStream(MonkeysFilterFramework InputFilter) {
        if (InputFilter.isAlive()) {
            return false;

        } else {

            return true;

        }

    }

    /**
     * This is the overloaded function from MonkeysFilterFramework
     * @param Filter1 is the first filter it reads from
     * @param Filter2 is the second filter it reads from
     */
    void Connect(MonkeysFilterFramework Filter1, MonkeysFilterFramework Filter2) {
        /** The merger will connect to the port of both filter1 and filter2. **/
        try {
            InputReadPort1.connect(Filter1.OutputWritePort);
            InputReadPort2.connect(Filter2.OutputWritePort);
            InputFilter1 = Filter1;
            InputFilter2 = Filter2;
        }

        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);

        }

    }

    /**
     * This is the overloaded function from MonkeysFilterFramework
     * @param InputFilterFramework specifies the input filter it reads from.
     * @param InputReadPort specifies the port it reads from.
     * @param id is 1 if we read from filter1, 2 if we read from filter2.
     * @return a byte of information we read
     * @throws EndOfStreamException
     */
    byte ReadFilterInputPort(MonkeysFilterFramework InputFilterFramework, PipedInputStream InputReadPort, int id) throws EndOfStreamException {
        byte datum = 0;

        try {
            while (InputReadPort.available() == 0) {
                if (EndOfInputStream(InputFilterFramework)) {
                    throw new EndOfStreamException("Port " + id + ": End of input stream reached");

                }

                sleep(250);

            }

        }

        catch (EndOfStreamException Error) {
            throw Error;

        }

        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Error in read port " + id + " wait loop::" + Error);

        }

        try {
            datum = (byte) InputReadPort.read();
            return datum;

        }

        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
            return datum;

        }

    }


    /**
     * This function reads one frame of data from a specified input filter, and returns as a MonkeysFrame format
     * @param InputFilter
     * @param port
     * @param id
     * @return
     * @throws EndOfStreamException
     */
    private MonkeysFrame readOneFrame(MonkeysFilterFramework InputFilter, PipedInputStream port, int id) throws EndOfStreamException {
        long[] frameData = new long[frameCol];
        for (int i = 0; i < frameCol; i++) {
            byte[] byteBuffer = new byte[MeasurementLength];

            for (int j = 0; j < IdLength; j++) {
                ReadFilterInputPort(InputFilter, port, id);
            }

            for (int j = 0; j < MeasurementLength; j++) {
                byteBuffer[j] = ReadFilterInputPort(InputFilter, port, id);
            }
            frameData[i] = MonkeysByteManager.BytesToLong(byteBuffer);
        }
        return new MonkeysFrame(frameData);
    }

    /**
     * This function writes one frame each time.
     * @param frame is the MonkeysFrame object we want to write.
     */
    private void writeOneFrame(MonkeysFrame frame) {
        long[] frameData = frame.toLongArray();
        for (int i = 0; i < frameCol; i++) {
            byte[] byteBuffer = MonkeysByteManager.IntToBytes(i);
            for (int j = 0; j < byteBuffer.length; j++) {
                WriteFilterOutputPort(byteBuffer[j]);
            }

            byteBuffer = MonkeysByteManager.LongToBytes(frameData[i]);
            for (int j = 0; j < byteBuffer.length; j++) {
                WriteFilterOutputPort(byteBuffer[j]);
            }
        }
    }

    /**
     * This is the overloaded the function from MonkeysFilterFramework.
     * @param InputReadPort specifies which input port we want to close.
     */
    void ClosePorts(PipedInputStream InputReadPort) {
        try {
            InputReadPort.close();
            OutputWritePort.close();

        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);

        }

    }

    public void run() {
        /** boolean variables we need to use. **/
        boolean hasNext1 = true, hasNext2 = true, firstTime = true;

        /** frameA is the frame read from filter1. frameB is the frame read from filter2. **/
        MonkeysFrame frameA = null;
        MonkeysFrame frameB = null;
        System.out.print("\n" + this.getName() + "::Merging ");

        /**
         * Here we do the merging by buffering one frame from each filter.
         * Then we compare these two frames, and always write the smaller frame.
         * */
        while (hasNext1 && hasNext2) {
            try {
                /** In the first time, we read frames from the ports. **/
                if (firstTime) {
                    frameA = readOneFrame(InputFilter1, InputReadPort1, 1);
                    frameB = readOneFrame(InputFilter2, InputReadPort2, 2);
                    firstTime = false;
                }

                if (frameA.time <= frameB.time) {
                    writeOneFrame(frameA);
                    frameA = readOneFrame(InputFilter1, InputReadPort1, 1);
                } else {
                    writeOneFrame(frameB);
                    frameB = readOneFrame(InputFilter2, InputReadPort2, 2);
                }
            } catch (EndOfStreamException e) {
                if (e.getMessage().startsWith("Port 1")) {
                    hasNext1 = false;
                } else if (e.getMessage().startsWith("Port 2")) {
                    hasNext2 = false;
                } else {
                    e.printStackTrace();
                }
            }
        } // while

        /** If the first port still has remaining data, we write them all. **/
        if (hasNext1) {
            writeOneFrame(frameA);
            while (true) {
                try {
                    byte databyte = ReadFilterInputPort(InputFilter1, InputReadPort1, 1);
                    WriteFilterOutputPort(databyte);
                } catch (EndOfStreamException e) {
                    ClosePorts(InputReadPort1);
                    break;
                }
            }
        }

        /** Otherwise we write data from the second port. **/
        else if (hasNext2) {
            writeOneFrame(frameB);
            while (true) {
                try {
                    byte databyte = ReadFilterInputPort(InputFilter2, InputReadPort2, 2);
                    WriteFilterOutputPort(databyte);
                } catch (EndOfStreamException e) {
                    ClosePorts(InputReadPort2);
                    break;
                }
            }
        }
    } // run

} // MiddleFilter
