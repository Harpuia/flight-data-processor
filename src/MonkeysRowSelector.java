import java.util.List;

/**
 * Created by rachel on 2/12/17.
 */
public class MonkeysRowSelector extends MonkeysFilterFramework {
    //Columns to keep

    public void run() {
        int id;
        long measurement;
        int IdLength = 4;
        int MeasurementLength = 8;
        byte[] byteBuffer;


        while (true) {
            try {
                //Reading id
                byteBuffer = new byte[IdLength];
                for (int i = 0; i < IdLength; i++) {
                    byteBuffer[i] = ReadFilterInputPort();
                }
                id = MonkeysByteManager.BytesToInt(byteBuffer);

                //Reading measurement
                byteBuffer = new byte[MeasurementLength];
                for (int i = 0; i < MeasurementLength; i++) {
                    byteBuffer[i] = ReadFilterInputPort();
                }
                measurement = MonkeysByteManager.BytesToLong(byteBuffer);
                //Filtering on row
                if(id == 0) {
                    byteBuffer = MonkeysByteManager.IntToBytes(id);
                    for(int i=0;i<IdLength;i++)
                        WriteFilterOutputPort(byteBuffer[i]);
                    //Writing the measurement
                    byteBuffer = MonkeysByteManager.LongToBytes(measurement);
                    for (int i = 0; i < MeasurementLength; i++)
                        WriteFilterOutputPort(byteBuffer[i]);
                }

                if(id == 2)
                {
                   if(Double.longBitsToDouble(measurement) < 10000)

                   {
                       //Writing the ID
                     byteBuffer = MonkeysByteManager.IntToBytes(id);
                       for(int i=0;i<IdLength;i++)
                           WriteFilterOutputPort(byteBuffer[i]);
                       //Writing the measurement
                       byteBuffer = MonkeysByteManager.LongToBytes(measurement);
                       for (int i = 0; i < MeasurementLength; i++)
                           WriteFilterOutputPort(byteBuffer[i]);
                   }
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                break;
            }
        }
    }
}