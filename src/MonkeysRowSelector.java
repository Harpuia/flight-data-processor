/**
 * Selects rows according to a comparison to a value applied to one column of the input stream
 */
public class MonkeysRowSelector extends MonkeysFilterFramework {
    //Attributes
    private int columnIndex;
    private Utils.ComparisonOperator operator;
    private double value;

    /**
     * Constructor of the MonkeysRowSelector class
     * @param columnIndex index of the column to filter
     * @param operator comparison operator
     * @param value comparison value
     */
    public MonkeysRowSelector(int columnIndex, Utils.ComparisonOperator operator, double value) {
        this.columnIndex = columnIndex;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Runs the filter
     */
    @Override
    public void run() {
        MonkeysFrame frame = null;
        while (true) {
            try {
                //Reading frame
                frame = ReadFrame();
                //Selecting column index, then operator for each case
                switch (this.columnIndex) {
                    case 1:
                        switch(operator){
                            case LessOrEqual:
                                if (frame.velocity <= value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Less:
                                if (frame.velocity < value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Equal:
                                if (frame.velocity == value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Larger:
                                if (frame.velocity > value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case LargerOrEqual:
                                if (frame.velocity >= value) {
                                    WriteFrame(frame);
                                }
                                break;
                        }
                        break;
                    case 2:
                        switch(operator){
                            case LessOrEqual:
                                if (frame.altitude <= value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Less:
                                if (frame.altitude < value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Equal:
                                if (frame.altitude == value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Larger:
                                if (frame.altitude > value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case LargerOrEqual:
                                if (frame.altitude >= value) {
                                    WriteFrame(frame);
                                }
                                break;
                        }
                        break;
                    case 3:
                        switch(operator){
                            case LessOrEqual:
                                if (frame.pressure <= value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Less:
                                if (frame.pressure < value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Equal:
                                if (frame.pressure == value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Larger:
                                if (frame.pressure > value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case LargerOrEqual:
                                if (frame.pressure >= value) {
                                    WriteFrame(frame);
                                }
                                break;
                        }
                        break;
                    case 4:
                        switch(operator){
                            case LessOrEqual:
                                if (frame.temperature <= value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Less:
                                if (frame.temperature < value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Equal:
                                if (frame.temperature == value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Larger:
                                if (frame.temperature > value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case LargerOrEqual:
                                if (frame.temperature >= value) {
                                    WriteFrame(frame);
                                }
                                break;
                        }
                        break;
                    case 5:
                        switch(operator){
                            case LessOrEqual:
                                if (frame.attitude <= value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Less:
                                if (frame.attitude < value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Equal:
                                if (frame.attitude == value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case Larger:
                                if (frame.attitude > value) {
                                    WriteFrame(frame);
                                }
                                break;
                            case LargerOrEqual:
                                if (frame.attitude >= value) {
                                    WriteFrame(frame);
                                }
                                break;
                        }
                        break;
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                break;
            }
        }
    }
}
