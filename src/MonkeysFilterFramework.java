import java.io.*;

public class MonkeysFilterFramework extends FilterFramework
{
    /**
     * Connects input port(s) of a filter to output port of another filter
     * @param Filter Filter to connect to
     */
	void Connect( MonkeysFilterFramework Filter )
	{
		PipedOutputStream tmpOuputPort;
		// check if the Filter is a Splitter
		if(Filter instanceof MonkeysSplitter){
			MonkeysSplitter splitter = (MonkeysSplitter) Filter;
			try{
				tmpOuputPort = splitter.getSplitOutputPort();
				InputReadPort.connect( tmpOuputPort );
				InputFilter = Filter;
				splitter.AddOneConnected();
			}catch( Exception err){
				err.printStackTrace();
				System.out.println( "\n" + this.getName() + " FilterFramework error connecting::"+ err );
			}
		}else{
			try
			{
				// Connect this filter's input to the upstream pipe's output stream
				tmpOuputPort = Filter.OutputWritePort;
				InputReadPort.connect( tmpOuputPort );
				InputFilter = Filter;
			} catch( Exception Error ) {
				System.out.println( "\n" + this.getName() + " FilterFramework error connecting::"+ Error );

			}
		}

	}
}
