import java.io.*; // note we must add this here since we use BufferedReader class to read from the keyboard

/**
 * Source filter implementation
 */
public class MonkeysSourceFilter extends MonkeysFilterFramework {
    //Attributes
    private String filename;

    /**
     * Constructor of the MonkeysSourceFilter class
     */
    public MonkeysSourceFilter() {
    }

    /**
     * Constructor of the MonkeysSourceFilter class
     * @param filename name of the file to read and stream
     */
    public MonkeysSourceFilter(String filename) {
        this.filename = filename;
    }

    /**
     * Runs the filter
     */
    public void run() {
        //Initializations
        String fileName = filename;
        DataInputStream in = null;
        byte databyte = 0;

        //Reading bytes from file and sending them to output port
        try {
            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source reading file...");
            while (true) {
                databyte = in.readByte();
                WriteFilterOutputPort(databyte);
            }
        } catch (EOFException eoferr) {
            try {
                in.close();
                ClosePorts();
            } catch (Exception closeerr) {
                System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeerr);

            }
        } catch (IOException iox) {
            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        }
    }
}