import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by Yuchao on 10/02/2017.
 */
public class MonkeysMerger extends MonkeysFilterFramework
{
    private PipedInputStream InputReadPort1 = new PipedInputStream();
    private PipedInputStream InputReadPort2 = new PipedInputStream();
    private PipedOutputStream OutputWritePort = new PipedOutputStream();
    private final int MeasurementLength = 8;        // This is the length of all measurements (including time) in bytes
    private final int IdLength = 4;                // This is the length of IDs in the byte stream
    private final int frameCol = 6;
    private MonkeysFilterFramework InputFilter1;
    private MonkeysFilterFramework InputFilter2;


    private boolean EndOfInputStream(MonkeysFilterFramework InputFilter)
    {
        if (InputFilter.isAlive())
        {
            return false;

        } else {

            return true;

        } // if

    } // EndOfInputStream

    void Connect (MonkeysFilterFramework Filter1, MonkeysFilterFramework Filter2)
    {
        try
        {
            // Connect this filter's input to the upstream pipe's output stream

            InputReadPort1.connect(Filter1.OutputWritePort);
            InputReadPort2.connect(Filter2.OutputWritePort);
            InputFilter1 = Filter1;
            InputFilter2 = Filter2;
        } // try

        catch( Exception Error )
        {
            System.out.println( "\n" + this.getName() + " FilterFramework error connecting::"+ Error );

        } // catch

    } // Connect

    byte ReadFilterInputPort(MonkeysFilterFramework InputFilterFramework, PipedInputStream InputReadPort, int id) throws EndOfStreamException
    {
        byte datum = 0;

        /***********************************************************************
         * Since delays are possible on upstream filters, we first wait until
         * there is data available on the input port. We check,... if no data is
         * available on the input port we wait for a quarter of a second and check
         * again. Note there is no timeout enforced here at all and if upstream
         * filters are deadlocked, then this can result in infinite waits in this
         * loop. It is necessary to check to see if we are at the end of stream
         * in the wait loop because it is possible that the upstream filter completes
         * while we are waiting. If this happens and we do not check for the end of
         * stream, then we could wait forever on an upstream pipe that is long gone.
         * Unfortunately Java pipes do not throw exceptions when the input pipe is
         * broken.
         ***********************************************************************/

        try
        {
            while (InputReadPort.available()==0 )
            {
                if (EndOfInputStream(InputFilterFramework))
                {
                    throw new EndOfStreamException("Port " + id + ": End of input stream reached");

                } //if

                //sleep(250);

            } // while

        } // try

        catch( EndOfStreamException Error )
        {
            throw Error;

        } // catch

        catch( Exception Error )
        {
            System.out.println( "\n" + this.getName() + " Error in read port " + id + " wait loop::" + Error );

        } // catch

        /***********************************************************************
         * If at least one byte of data is available on the input
         * pipe we can read it. We read and write one byte to and from ports.
         ***********************************************************************/

        try
        {
            datum = (byte)InputReadPort.read();
            return datum;

        } // try

        catch( Exception Error )
        {
            System.out.println( "\n" + this.getName() + " Pipe read error::" + Error );
            return datum;

        } // catch

    } // ReadFilterPort


    private MonkeysFrame readOneFrame (MonkeysFilterFramework InputFilter, PipedInputStream port, int id) throws EndOfStreamException {
        long[] frameData = new long[frameCol];
        for(int i = 0; i < frameCol; i++) {
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

    private void writeOneFrame (MonkeysFrame frame) {
        long[] frameData = frame.toLongArray();
        for (int i = 0; i < frameCol; i++) {
            byte[] byteBuffer = MonkeysByteManager.IntToBytes(i);
            for (int j = 0; j < byteBuffer.length; j++) {
                WriteFilterOutputPort(byteBuffer[j]);
            }

            byteBuffer = MonkeysByteManager.LongToBytes(frameData[i]);
            for(int j = 0; j < byteBuffer.length; j++) {
                WriteFilterOutputPort(byteBuffer[j]);
            }
        }
    }

    void ClosePorts(PipedInputStream InputReadPort)
    {
        try
        {
            InputReadPort.close();
            OutputWritePort.close();

        }
        catch( Exception Error )
        {
            System.out.println( "\n" + this.getName() + " ClosePorts error::" + Error );

        } // catch

    } // ClosePorts

    public void run()
    {
        //Declarations
        boolean hasNext1 = true, hasNext2 = true;

        System.out.print( "\n" + this.getName() + "::Merging ");
        while (hasNext1 && hasNext2)
        {
            try
            {
                MonkeysFrame frameA = readOneFrame(InputFilter1, InputReadPort1, 1);
                MonkeysFrame frameB = readOneFrame(InputFilter2, InputReadPort2, 2);
                if(frameA.time <= frameB.time) {
                    writeOneFrame(frameA);
                    frameA = readOneFrame(InputFilter1, InputReadPort1, 1);
                } else {
                    frameB = readOneFrame(InputFilter2, InputReadPort2, 2);
                }
            }

            catch (EndOfStreamException e)
            {
                if(e.getMessage().startsWith("Port 1")) {
                    hasNext1 = false;
                } else if(e.getMessage().startsWith("Port 2")) {
                    hasNext2 = false;
                } else {
                    e.printStackTrace();
                    break;
                }
                //ClosePorts();
                //break;
            }

        } // while

        if (hasNext1) {
            while(true) {
                try {
                    byte databyte = ReadFilterInputPort(InputFilter1, InputReadPort1, 1);
                    WriteFilterOutputPort(databyte);
                } catch (EndOfStreamException e) {
                    ClosePorts(InputReadPort1);
                    break;
                }
            }
        } else if (hasNext2) {
            while(true) {
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
