/**
 * Created by Yuchao on 10/02/2017.
 */
public class MonkeysFrame {
    long time;
    double velocity;
    double altitude;
    double pressure;
    double temperature;
    double attitude;

    final int frameCol = 6;

    public MonkeysFrame(long time, double velocity, double altitude, double pressure, double temperature, double attitude) {
        this.time = time;
        this.velocity = velocity;
        this.altitude = altitude;
        this.pressure = pressure;
        this.temperature = temperature;
        this.attitude = attitude;
    }

    public MonkeysFrame(long[] dataFrame) {
        if(dataFrame.length < 6) {
            return;
        }

        this.time = dataFrame[0];
        this.velocity = dataFrame[1];
        this.altitude = dataFrame[2];
        this.pressure = dataFrame[3];
        this.temperature = dataFrame[4];
        this.attitude = dataFrame[5];
    }

    public long[] toLongArray() {
        long[] frameData = new long[frameCol];
        frameData[0] = time;
        frameData[1] = Double.doubleToLongBits(velocity);
        frameData[2] = Double.doubleToLongBits(altitude);
        frameData[3] = Double.doubleToLongBits(pressure);
        frameData[4] = Double.doubleToLongBits(temperature);
        frameData[5] = Double.doubleToLongBits(attitude);
        return frameData;
    }
}
