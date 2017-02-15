import java.util.List;

/**
 * A class that allows selecting certain columns from the input data
 */
public class MonkeysColSelector extends MonkeysFilterFramework {
    //Columns to keep
    private List<Integer> columns;

    /**
     * Constructor of the MonkeyColSelector class
     * @param columns A list of columns that will be selected and output
     */
    public MonkeysColSelector(List<Integer> columns) {
        columns.add(0);
        this.columns = columns;
    }

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

                //Filtering on column
                if (columns.contains(id)) {
                    //Writing the ID
                    byteBuffer = MonkeysByteManager.IntToBytes(id);
                    for (int i = 0; i < IdLength; i++)
                        WriteFilterOutputPort(byteBuffer[i]);

                    //Writing the measurement
                    byteBuffer = MonkeysByteManager.LongToBytes(measurement);
                    for (int i = 0; i < MeasurementLength; i++)
                        WriteFilterOutputPort(byteBuffer[i]);
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                break;
            }
        }
    }
}
