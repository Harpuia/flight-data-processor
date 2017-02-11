/**
 * Created by yazid on 10-Feb-17.
 */
public class MonkeysByteManager {

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

    public static byte[] IntToBytes(int input) {
        final int length = 4;
        byte tm_stp[] = new byte[length];
        for (int i = 0; i < length; i++) {
            tm_stp[i] = (byte) (input >> 8 * (length - 1 - i));
        }
        return tm_stp;
    }

    public static byte[] LongToBytes(long input) {
        final int length = 8;
        byte tm_stp[] = new byte[length];
        for(int i = 0; i < length; i++){
            tm_stp[i] = (byte) (input >> 8 * (length - 1 - i));
        }
        return tm_stp;
    }

    public static void testInt(int input) {
        if(input == BytesToInt(IntToBytes(input))) {
            System.out.println("Test passed: int type");
        }
    }

    public static void testLong(long input) {
        if(input == BytesToLong(LongToBytes(input))) {
            System.out.println("Test passed: long type");
        }
    }
}
