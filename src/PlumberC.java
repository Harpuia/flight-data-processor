import com.sun.scenario.effect.Merge;

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
        MonkeysColSelector ColSelector = new MonkeysColSelector(columns);
        MonkeysSplitter Splitter1 = new MonkeysSplitter();
        MonkeysSplitter Splitter2 = new MonkeysSplitter();
        MonkeysExtrapolator Extrapolator = new MonkeysExtrapolator();
        MonkeysRowSelector SelectorLess10K = new MonkeysRowSelector(2, Utils.ComparisonOperator.Less, 10000);
        MonkeysRowSelector SelectorLargerEqual10K = new MonkeysRowSelector(2, Utils.ComparisonOperator.LargerOrEqual, 10000);
        MonkeysWildDetector WildDetector = new MonkeysWildDetector();


        //Sinks
        MonkeysSinkFilter Sink1 = new MonkeysSinkFilter("outputC.txt", true);
        MonkeysSinkFilter Sink2 = new MonkeysSinkFilter("LessThan10K.txt", false);
        MonkeysSinkFilter Sink3 = new MonkeysSinkFilter("PressureWildPoints.txt", false);
        try {
            //Connecting filters
            Sink1.Connect(Extrapolator);
            Extrapolator.Connect(SelectorLargerEqual10K);
            SelectorLargerEqual10K.Connect(Splitter1);
            Splitter1.Connect(Merger);
            Merger.Connect(Source1, Source2);
            Sink2.Connect(SelectorLess10K);
            SelectorLess10K.Connect(Splitter2);
            Splitter2.Connect(Splitter1);
            Sink3.Connect(ColSelector);
            ColSelector.Connect(WildDetector);
            WildDetector.Connect(Splitter2);


            //Starting filters
            Source1.start();
            Source2.start();
            Merger.start();
            Splitter1.start();
            SelectorLargerEqual10K.start();
            Splitter2.start();
            Extrapolator.start();
            SelectorLess10K.start();
            WildDetector.start();
            Sink1.start();
            Sink2.start();
            ColSelector.start();
            Sink3.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}