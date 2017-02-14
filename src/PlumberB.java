import java.util.ArrayList;
import java.util.List;

/**
 * System B pipe-and -filter
 */
public class PlumberB {
    public static void main(String argv[]) {
        //Extrapolated columns to display
        List<Integer> columnsExtrapolated = new ArrayList<Integer>();
        columnsExtrapolated.add(2);
        columnsExtrapolated.add(3);
        columnsExtrapolated.add(4);

        //Wild points columns to display
        List<Integer> columnsWildpoints = new ArrayList<Integer>();
        columnsWildpoints.add(3);

        //Source
        MonkeysSourceFilter Source = new MonkeysSourceFilter("FlightData.dat");

        //Intermediate processing
        MonkeysConverter Converter = new MonkeysConverter();
        MonkeysSplitter Splitter = new MonkeysSplitter();

        //Wild points detection
        MonkeysExtrapolator Extrapolator = new MonkeysExtrapolator();
        MonkeysWildDetector WildDetector = new MonkeysWildDetector();

        //Columns selection
        MonkeysColSelector ColSelectorExtrapolated = new MonkeysColSelector(columnsExtrapolated);
        MonkeysColSelector ColSelectorWildpoints = new MonkeysColSelector(columnsWildpoints);

        //Sinks
        MonkeysSinkFilter SinkExtrapolated = new MonkeysSinkFilter("outputB.txt", true);
        MonkeysSinkFilter SinkWildPoints = new MonkeysSinkFilter("WildPoints.txt", false);

        try {
            //Connecting filters
            SinkExtrapolated.Connect(ColSelectorExtrapolated);
            ColSelectorExtrapolated.Connect(Extrapolator);
            Extrapolator.Connect(Splitter);
            Splitter.Connect(Converter);
            Converter.Connect(Source);
            SinkWildPoints.Connect(ColSelectorWildpoints);
            ColSelectorWildpoints.Connect(WildDetector);
            WildDetector.Connect(Splitter);

            //Starting filters
            Source.start();
            Converter.start();
            Splitter.start();
            Extrapolator.start();
            WildDetector.start();
            ColSelectorExtrapolated.start();
            ColSelectorWildpoints.start();
            SinkExtrapolated.start();
            SinkWildPoints.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
