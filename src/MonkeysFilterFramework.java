import java.io.*;

public class MonkeysFilterFramework extends FilterFramework
{
    /**
     * Constants
     **/
    protected final int FrameSize = 6;
    protected final int MeasurementLength = 8;
    protected final int IdLength = 4;

    /**
     * Reads a frame from input
     *
     * @return MonkeysFrame object read from input
     * @throws EndOfStreamException
     */
    protected MonkeysFrame ReadFrame() throws EndOfStreamException {
        //Reading frame data
        long[] frameData = new long[FrameSize];
        for (int i = 0; i < FrameSize; i++) {
            byte[] byteBuffer = new byte[MeasurementLength];

            for (int j = 0; j < IdLength; j++) {
                ReadFilterInputPort();
            }

            for (int j = 0; j < MeasurementLength; j++) {
                byteBuffer[j] = ReadFilterInputPort();
            }
            frameData[i] = MonkeysByteManager.BytesToLong(byteBuffer);
        }
        return new MonkeysFrame(frameData);
    }

    /**
     * Writes frame to the output of the filter
     *
     * @param frame frame to write to the output
     */
    protected void WriteFrame(MonkeysFrame frame) {
        long[] frameData = frame.toLongArray();
        for (int i = 0; i < FrameSize; i++) {
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
     * Connects input port(s) of a filter to output port of another filter
     * @param Filter Filter to connect to
     */
	void Connect( MonkeysFilterFramework Filter )
	{
		PipedOutputStream tmpOuputPort;
		// check if the Filter is a Splitter
		if(Filter instanceof MonkeysSplitter){
			MonkeysSplitter splitter = (MonkeysSplitter) Filter;
			try{
				tmpOuputPort = splitter.getSplitOutputPort();
				InputReadPort.connect( tmpOuputPort );
				InputFilter = Filter;
				splitter.AddOneConnected();
			}catch( Exception err){
				err.printStackTrace();
				System.out.println( "\n" + this.getName() + " FilterFramework error connecting::"+ err );
			}
		}else{
			try
			{
				// Connect this filter's input to the upstream pipe's output stream
				tmpOuputPort = Filter.OutputWritePort;
				InputReadPort.connect( tmpOuputPort );
				InputFilter = Filter;
			} catch( Exception Error ) {
				System.out.println( "\n" + this.getName() + " FilterFramework error connecting::"+ Error );

			}
		}

	}
}
