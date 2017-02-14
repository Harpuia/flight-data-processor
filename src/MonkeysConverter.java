import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yazid on 10-Feb-17.
 */
public class MonkeysConverter extends MonkeysFilterFramework {
    @Override
    public void run() {
        //Declarations
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        int MeasurementLength = 8;        // This is the length of all measurements (including time) in bytes
        int IdLength = 4;                // This is the length of IDs in the byte stream

        long measurement;                // This is the word used to store all measurements - conversions are illustrated.
        int id;                            // This is the measurement id
        int i;                            // This is a loop counter
        double measurementValue;        //This is the measurement value converted appropriately when id!=0

        //Buffer
        byte[] byteBuffer;

        while (true) {
            try {
                //Converting ID
                byteBuffer = new byte[IdLength];
                for (i = 0; i < IdLength; i++) {
                    byteBuffer[i] = ReadFilterInputPort();
                } // for
                id = MonkeysByteManager.BytesToInt(byteBuffer);

                //Converting ID
                byteBuffer = new byte[MeasurementLength];
                for (i = 0; i < MeasurementLength; i++) {
                    byteBuffer[i] = ReadFilterInputPort();
                }
                measurement = MonkeysByteManager.BytesToLong(byteBuffer);

                //Setting timestamp
                if (id == 0) {
                    TimeStamp.setTimeInMillis(measurement);
                }

                //Conversion
                measurementValue = Double.longBitsToDouble(measurement);
                if (id == 2) {
                    measurementValue = measurementValue * 0.3048;
                } else if (id == 4) {
                    measurementValue = (measurementValue - 32) * 5 / 9;
                }
                if(id == 2 || id == 4)
                    measurement = Double.doubleToLongBits(measurementValue);

                //Copy id to output port
                byteBuffer = MonkeysByteManager.IntToBytes(id);
                for (i = 0; i < IdLength; i++)
                    WriteFilterOutputPort(byteBuffer[i]);

                //Copy measurement to output port
                byteBuffer = MonkeysByteManager.LongToBytes(measurement);
                for (i = 0; i < MeasurementLength; i++)
                    WriteFilterOutputPort(byteBuffer[i]);
            } catch (EndOfStreamException e) {
                ClosePorts();
                break;
            }
        }
    }
}