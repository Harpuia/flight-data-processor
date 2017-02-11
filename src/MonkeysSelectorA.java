import org.omg.CORBA.portable.IDLEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yazid on 10-Feb-17.
 */
public class MonkeysSelectorA extends FilterFramework {

    /*public MonkeysSelectorA(int []index)
    {
        for(int i=0;i<index.length;i++)
        {
            if(index[i]==0 || index[i]==2 || index[i]==4)
                run();

        }
    }*/
    public void run(){

       // int bytesread = 0;					// Number of bytes read from the input file.
        //int byteswritten = 0;				// Number of bytes written to the stream.
        //byte databyte = 0;					// The byte of data read from the file
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        int id;
        long measurement;
        int IdLength=4;
        int MeasurementLength = 8;
        byte[] byteBuffer;
        // Next we write a message to the terminal to let the world know we are alive...

        //System.out.print( "\n" + this.getName() + "::Middle Reading ");

        while (true)
        {
            /*************************************************************
             *	Here we read a byte and write a byte
             *************************************************************/

            try
            {
                byteBuffer=new byte[IdLength];
                for(int i=0;i< IdLength;i++)
                {
                    byteBuffer[i]=ReadFilterInputPort();
                }
                id=MonkeysByteManager.BytesToInt(byteBuffer);


                byteBuffer=new byte[MeasurementLength];
                for(int i=0;i<MeasurementLength;i++)
                {
                    byteBuffer[i]=ReadFilterInputPort();
                }
                measurement=MonkeysByteManager.BytesToLong(byteBuffer);


                if(id==0 || id==2 || id==4)
                {
                byteBuffer=MonkeysByteManager.IntToBytes(id);
                    for(int i=0;i<IdLength;i++)
                    WriteFilterOutputPort(byteBuffer[i]);

                    byteBuffer=MonkeysByteManager.LongToBytes(measurement);

                    for (int i = 0; i < MeasurementLength; i++)
                        WriteFilterOutputPort(byteBuffer[i]);
                }



            } // try

            catch (EndOfStreamException e)
            {
                ClosePorts();
              //  System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;

            } // catch

        } // while


    }
}
