/**
 * Created by yazid on 10-Feb-17.
 * Groups operations needed for byte transformation in order to read the sensor data
 */
public class MonkeysByteManager {
    /**
     * Reads bytes into an integer
     * @param bytes Bytes representing the integer value
     * @return Integer value of bytes
     */
    public static int BytesToInt(byte[] bytes){
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result = result | (bytes[i] & 0xFF);
            if (i != bytes.length - 1)
            {
                result = result << 8;
            }
        }
        return result;
    }

    /**
     * Reads bytes into a long
     * @param bytes Bytes representing the long value
     * @return Long value of bytes
     */
    public static long BytesToLong(byte[] bytes){
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result = result | (bytes[i] & 0xFF);
            if (i != bytes.length - 1)
            {
                result = result << 8;
            }
        }
        return result;
    }

    /**
     * Transforms an integer to a byte array
     * @param input Integer to transform
     * @return Output bytes
     */
    public static byte[] IntToBytes(int input) {
        final int length = 4;
        byte tm_stp[] = new byte[length];
        for (int i = 0; i < length; i++) {
            tm_stp[i] = (byte) (input >> 8 * (length - 1 - i));
        }
        return tm_stp;
    }

    /**
     * Transforms an long to a byte array
     * @param input Long to transform
     * @return Output bytes
     */
    public static byte[] LongToBytes(long input) {
        final int length = 8;
        byte tm_stp[] = new byte[length];
        for(int i = 0; i < length; i++){
            tm_stp[i] = (byte) (input >> 8 * (length - 1 - i));
        }
        return tm_stp;
    }

    /**
     * Test method to verify if int transformations work
     * @param input Integer input
     */
    public static void testInt(int input) {
        if(input == BytesToInt(IntToBytes(input))) {
            System.out.println("Test passed: int type");
        }
    }

    /**
     * Test method to verify if long transformations work
     * @param input Long input
     */
    public static void testLong(long input) {
        if(input == BytesToLong(LongToBytes(input))) {
            System.out.println("Test passed: long type");
        }
    }
}
