import java.util.ArrayList;
import java.util.List;

/**
 * System C pipe-and -filter
 */
public class PlumberC {
    public static void main(String argv[]) {
        //Preparing columns to be selected
        List<Integer> columns = new ArrayList<>();
        columns.add(3);

        //Sources
        MonkeysSourceFilter Source1 = new MonkeysSourceFilter("SubSetA.dat");
        MonkeysSourceFilter Source2 = new MonkeysSourceFilter("SubSetB.dat");

        //Data processing
        MonkeysMerger Merger = new MonkeysMerger();
        MonkeysSplitter Splitter = new MonkeysSplitter();
        MonkeysRowSelector Selector = new MonkeysRowSelector();
        MonkeysWildDetector Detector = new MonkeysWildDetector();

        //Sinks
        MonkeysSinkFilter Sink1 = new MonkeysSinkFilter("LessThan10K.txt", false);
        MonkeysSinkFilter Sink2 = new MonkeysSinkFilter("PressureWildPoints.txt", false);
        try {
            //Connecting filters
            Sink2.Connect(Detector);
            Sink1.Connect(Selector);
            Detector.Connect(Splitter);
            Selector.Connect(Splitter);
            Splitter.Connect(Merger);
            Merger.Connect(Source1, Source2);

            //Starting filters
            Source1.start();
            Source2.start();
            Merger.start();
            Splitter.start();
            Selector.start();
            Detector.start();
            Sink1.start();
            Sink2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}