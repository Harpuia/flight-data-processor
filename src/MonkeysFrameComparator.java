import java.util.Comparator;

/**
 * This class is the comparator to define the compare operation of class MonkeysFrame.
 * We compare the MonkeysFrame by comaring the time.
 */
public class MonkeysFrameComparator implements Comparator<MonkeysFrame> {

    /**
     *
     * @param f1 the MonkeysFrame object passed in
     * @param f2 the MonkeysFrame object passed in
     * @return -1 if f1 smaller than f2, 0 if f1 equals to f2, and 1 if f1 larger than f2.
     */

    @Override
    public int compare (MonkeysFrame f1, MonkeysFrame f2) {
        if(f1.time == f2.time) {
            return 0;
        }

        /** If the time of f1 is smaller than that in f2, we say f1 is smaller than f2. **/
        return f1.time < f2.time ? -1 : 1;
    }
}
