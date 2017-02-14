/**
 * Created by yazid on 13-Feb-17.
 */
public class MonkeysWildDetector extends MonkeysWildpointFramework {
    //We are assuming the first point is always valid (since we won't have any way of extrapolating if this is not the case)
    @Override
    public void run() {
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
                    if(VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.FIRST){
                        WriteFrame(frame1);
                        frame1.pressure = frame2.pressure;
                    }
                } else {
                    frame1 = frame2;
                    frame2 = frame3;
                    frame3 = ReadFrame();
                }

                if (this.VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND) {
                    WriteFrame(frame2);
                    frame2.pressure = (frame1.pressure + frame3.pressure) / 2; //Minus is used to communicate to the sink the fact that this was a wild point
                    //Write to the output
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
