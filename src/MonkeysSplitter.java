import java.io.PipedOutputStream;

/**
 *  MonkeysSplitter
 *  An filter class that own one additional PipedOutputStream.
 *  You shall not do any data manipulating in this class.
 *
 */
public class MonkeysSplitter extends MonkeysFilterFramework {

    private static final int MAX_CONNECTED = 2;

    /** Member Variables **/
    private PipedOutputStream splitOutputWritePort = new PipedOutputStream();
    private int connectedNum = 0;   // the number of connected output ports
    private FilterFramework inputFilter;
    private FilterFramework outputFilter1;
    private FilterFramework outputFilter2;

    /** Constructor **/
    public MonkeysSplitter(){

    }


    /** Methods **/

    /*
     * Add one on the counter
     */
    public void AddOneConnected(){
        connectedNum += 1;
    }

    /***
     * Return available output port for binding, maxium 2
     * @return the PipedOutputStream
     * @throws Exception
     */
    public PipedOutputStream getSplitOutputPort() throws Exception {
        if(connectedNum == 0){
            return super.OutputWritePort;
        }else if(connectedNum == 1){
            return splitOutputWritePort;
        }else{
            throw new Exception("No accessible output port in splitter ");
        }
    }

    @Override
    public void run()
    {

        int bytesread = 0;					// Number of bytes read from the input file.
        int byteswritten = 0;				// Number of bytes written to the stream.
        byte databyte = 0;					// The byte of data read from the file

        // Next we write a message to the terminal to let the world know we are alive...

        System.out.print( "\n" + this.getName() + "::The Splitter Reading ");

        while (true)
        {
            /*************************************************************
             *	Here we read a byte and write a byte
             *************************************************************/

            try
            {
                databyte = ReadFilterInputPort();
                bytesread++;
                this.WriteFilterOutputPort(databyte);
                byteswritten++;

            } // try

            catch (EndOfStreamException e)
            {
                ClosePorts();
                System.out.print( "\n" + this.getName() + ":: The Splitter Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;

            } // catch

        } // while

    } // run
    @Override
    void WriteFilterOutputPort(byte datum){
        try
        {
            if(connectedNum == 1){
                OutputWritePort.write((int) datum );
            }else if(connectedNum == 2){
                OutputWritePort.write((int) datum );
                OutputWritePort.flush();
                splitOutputWritePort.write((int) datum);
                splitOutputWritePort.flush();
            }else{
                System.out.println("No output now...");
            }

        } // try

        catch( Exception Error )
        {
            System.out.println("\n" + this.getName() + " Splitter Pipe write error::" + Error );

        } // catch

        return;
    }

    /**
     * Override the FilterFramework method to close the additional outputWritePort
     */
    @Override
    public void ClosePorts()
    {
        super.ClosePorts();
        try{
            splitOutputWritePort.close();
        }catch( Exception Error )
        {
            System.out.println( "\n" + this.getName() + " ClosePorts error::" + Error );

        }
    }

}
