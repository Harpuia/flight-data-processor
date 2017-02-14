/**
 * Created by yazid on 13-Feb-17.
 */
public class MonkeysExtrapolator extends MonkeysWildpointFramework {
    /**
     * Constants
     **/
    private final int FrameSize = 6;
    private final int MeasurementLength = 8;
    private final int IdLength = 4;

    //TODO: place this in MonkeysFilter Framework

    /**
     * Reads a frame from input
     *
     * @return MonkeysFrame object read from input
     * @throws EndOfStreamException
     */
    private MonkeysFrame ReadFrame() throws EndOfStreamException {
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
    private void WriteFrame(MonkeysFrame frame) {
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

    //We are assuming the first point is always valid (since we won't have any way of extrapolating if this is not the case)
    @Override
    public void run() {
        //TODO: handle case where first point or last point are wild points
        MonkeysFrame frame1 = null;
        MonkeysFrame frame2 = null;
        MonkeysFrame frame3 = null;
        int counter = 1;
        while (true) {
            try {
                if (counter == 1) {
                    //Reading frames
                    frame1 = ReadFrame();
                    frame2 = ReadFrame();
                    frame3 = ReadFrame();
                    WriteFrame(frame1);
                } else {
                    frame1 = frame2;
                    frame2 = frame3;
                    frame3 = ReadFrame();
                }

                //Testing frames
                if (this.VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.NO) {
                    //Write to the output
                    WriteFrame(frame2);
                } else if (this.VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND) {
                    frame2.pressure = (frame1.pressure + frame3.pressure) / 2;
                    //Write to the output
                    WriteFrame(frame2);
                }
                counter++;
            } catch (EndOfStreamException e) {
                WriteFrame(frame3);
                ClosePorts();
                break;
            }
        }
    }
}
