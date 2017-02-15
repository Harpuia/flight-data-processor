/**
 * Filter that detects wild points and passes them on to the output
 */
public class MonkeysWildDetector extends MonkeysWildpointFramework {
    /**
     * Runs the filter
     */
    @Override
    public void run() {
        //Three frames to be read
        MonkeysFrame frame1 = null;
        MonkeysFrame frame2 = null;
        MonkeysFrame frame3 = null;
        //Initialization
        int counter = 1;
        while (true) {
            try {
                if (counter == 1) {
                    //Reading frames
                    frame1 = ReadFrame();
                    frame2 = ReadFrame();
                    frame3 = ReadFrame();
                    //Writing frame 2 if it is correct
                    if(VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.FIRST){
                        WriteFrame(frame1);
                        frame1.pressure = frame2.pressure;
                    }
                } else {
                    //Advancing
                    frame1 = frame2;
                    frame2 = frame3;
                    frame3 = ReadFrame();
                }
                //Extrapolating value to check whether next is wildpoint or not
                if (this.VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND) {
                    WriteFrame(frame2);
                    frame2.pressure = (frame1.pressure + frame3.pressure) / 2;
                }
                counter++;
            } catch (EndOfStreamException e) {
                if(VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND){
                    WriteFrame(frame2);
                }
                ClosePorts();
                break;
            }
        }
    }
}
