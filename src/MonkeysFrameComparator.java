import java.util.Comparator;

/**
 * Created by Yuchao on 12/02/2017.
 */
public class MonkeysFrameComparator implements Comparator<MonkeysFrame> {

    @Override
    public int compare (MonkeysFrame f1, MonkeysFrame f2) {
        if(f1.time == f2.time) {
            return 0;
        }

        return f1.time < f2.time ? -1 : 1;
    }
}
