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
}
