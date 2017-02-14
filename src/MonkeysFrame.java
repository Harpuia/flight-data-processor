/**
 * Created by Yuchao on 10/02/2017.
 * MonkeysFrame stores data of one sample.
 * It includes time, velocity, altitude, pressure, temperature and attitude.
 */
public class MonkeysFrame {
    long time;
    double velocity;
    double altitude;
    double pressure;
    double temperature;
    double attitude;

    /* This is a const variable storing how many types of data there are in one frame. */
    final int frameCol = 6;

    public MonkeysFrame(long time, double velocity, double altitude, double pressure, double temperature, double attitude) {
        this.time = time;
        this.velocity = velocity;
        this.altitude = altitude;
        this.pressure = pressure;
        this.temperature = temperature;
        this.attitude = attitude;
    }

    /* A customized constructor that takes a long typed array of data and store them in the object. */
    public MonkeysFrame(long[] dataFrame) {
        if(dataFrame.length < 6) {
            return;
        }

        this.time = dataFrame[0];
        this.velocity = Double.longBitsToDouble(dataFrame[1]);
        this.altitude = Double.longBitsToDouble(dataFrame[2]);
        this.pressure = Double.longBitsToDouble(dataFrame[3]);
        this.temperature = Double.longBitsToDouble(dataFrame[4]);
        this.attitude = Double.longBitsToDouble(dataFrame[5]);
    }

    /* This methods output all the data in a long typed array. */
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
