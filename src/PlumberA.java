import java.util.ArrayList;
import java.util.List;

/**
 * System A pipe-and -filter
 */
public class PlumberA {
    public static void main(String argv[]) {
        //Preparing columns to be selected
        List<Integer> columns = new ArrayList<Integer>();
        columns.add(2);
        columns.add(4);

        //Source filter
        MonkeysSourceFilter Source = new MonkeysSourceFilter("FlightData.dat");

        //Data processing
        MonkeysConverter Converter = new MonkeysConverter();
        MonkeysColSelector Selector = new MonkeysColSelector(columns);
        MonkeysSinkFilter Sink = new MonkeysSinkFilter("outputA.txt", new int[]{0, 4, 2},false);

        try {
            //Connecting filters
            Sink.Connect(Selector);
            Selector.Connect(Converter);
            Converter.Connect(Source);

            //Starting filters
            Source.start();
            Converter.start();
            Selector.start();
            Sink.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}