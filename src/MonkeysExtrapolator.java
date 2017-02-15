/**
 * MonkeysExtrapolator.java
 * A filter that is inherited from class MonkeysWildpointFramework
 * This filter could extrapolate the wildpoint data( invalid pressure data) by replacing them with extrapolated valid data
 */
public class MonkeysExtrapolator extends MonkeysWildpointFramework {
    /** Methods **/
    //We are assuming there can't be two consecutive wildpoints
    @Override
    public void run() {
        MonkeysFrame frame1 = null;
        MonkeysFrame frame2 = null;
        MonkeysFrame frame3 = null;
        int counter = 1;
        // The extrapolator will buffer three frames of data at first, then verify second one then write then read next one
        while (true) {
            try {
                // at the very beginning, we need read 3 frame data( as reading windows), one frame contains all five measurements
                if (counter == 1) {
                    //Reading frames
                    frame1 = ReadFrame();
                    frame2 = ReadFrame();
                    frame3 = ReadFrame();
                    if (VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.FIRST) {
                        frame1.pressure = frame2.pressure;
                    }
                    WriteFrame(frame1);
                } else {
                    // right shift the reading windows, and read a new frame to frame 3
                    frame1 = frame2;
                    frame2 = frame3;
                    frame3 = ReadFrame();
                }

                //Testing frames, when second frame is invalid, we will extrapolate it and write it into pipe
                if (this.VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND) {
                    frame2.pressure = -(frame1.pressure + frame3.pressure) / 2; //Minus is used to communicate to the sink the fact that this was a wild point
                    //Write to the output
                    WriteFrame(frame2);
                } else {
                    //The second frame is valid, directly write it to the output
                    WriteFrame(frame2);
                }
                counter++;
            } catch (EndOfStreamException e) { // write the buffered data and handle the exception
                // when reach the end of input stream, there will still one data buffered in frame2, we will check whether it is valid and write it into output
                if (VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND) {
                    frame3.pressure = frame1.pressure;
                }
                WriteFrame(frame3);
                ClosePorts();
                break;
            }
        }
    }
}
