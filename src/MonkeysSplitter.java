import java.io.PipedOutputStream;

/**
 *  MonkeysSplitter
 *  A filter class that own one additional PipedOutputStream.
 *  You shall not do any data manipulating in this class.
 */
public class MonkeysSplitter extends MonkeysFilterFramework {
    /** Member Variables **/
    private PipedOutputStream splitOutputWritePort = new PipedOutputStream();
    private int connectedNum = 0;

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

    /**
     * Override of the filter run method
     */
    @Override
    public void run()
    {
        int bytesread = 0;
        int byteswritten = 0;
        byte databyte = 0;

        System.out.print( "\n" + this.getName() + "::The Splitter Reading ");

        while (true)
        {
            try
            {
                databyte = ReadFilterInputPort();
                bytesread++;
                this.WriteFilterOutputPort(databyte);
                byteswritten++;
            }
            catch (EndOfStreamException e)
            {
                ClosePorts();
                System.out.print( "\n" + this.getName() + ":: The Splitter Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;
            }
        }
    }

    /**
     * Writes a data byte to the output port
     * @param datum Byte to be written to the output
     */
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
        }
        catch( Exception Error )
        {
            System.out.println("\n" + this.getName() + " Splitter Pipe write error::" + Error );
        }
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
