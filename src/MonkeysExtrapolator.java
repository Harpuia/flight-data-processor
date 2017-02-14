/**
 * Created by yazid on 13-Feb-17.
 */
public class MonkeysExtrapolator extends MonkeysWildpointFramework {
    //We are assuming there can't be two consecutive wildpoints
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
                        frame1.pressure = frame2.pressure;
                    }
                    WriteFrame(frame1);
                } else {
                    frame1 = frame2;
                    frame2 = frame3;
                    frame3 = ReadFrame();
                }

                //Testing frames
                if (this.VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND) {
                    frame2.pressure = -(frame1.pressure + frame3.pressure) / 2; //Minus is used to communicate to the sink the fact that this was a wild point
                    //Write to the output
                    WriteFrame(frame2);
                } else {
                    //Write to the output
                    WriteFrame(frame2);
                }
                    counter++;
            } catch (EndOfStreamException e) {
                if(VerifyWildpoint(frame1.pressure, frame2.pressure) == Wildpoint.SECOND){
                    frame3.pressure = frame1.pressure;
                }
                WriteFrame(frame3);
                ClosePorts();
                break;
            }
        }
    }
}
