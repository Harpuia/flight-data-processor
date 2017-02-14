/**
 * Created by yazid on 13-Feb-17.
 */
public class MonkeysWildpointFramework extends MonkeysFilterFramework {
    /** Constants **/
    private final double UPPER_LIMIT = 1013.5;
    private final double LOWER_LIMIT = 0;
    private final double DIFF = 10;

    /**
     * NO: No wildpoint
     * FIRST: first point is a wildpoint
     * SECOND: second point is a wildpoint
     * BOTH: both are wildpoints
     */
    protected enum Wildpoint {
        NO, FIRST, SECOND, BOTH
    }

    /**
     * Determines whether a point is a wildpoint or not
     * @param pressure pressure value of the point
     * @return True if wildpoint, false otherwise
     */
    protected boolean VerifyWildpoint(double pressure){
        return pressure > UPPER_LIMIT || pressure <= LOWER_LIMIT;
    }

    /**
     * Returns whether point 1 or point 2 are wildpoints. We assume the first one is correct. We assume also that if this is the first point in a data file, then it will be filtered through the logically acceptable range (0 to 1013.5)
     * @param pressure1 Pressure point 1
     * @param pressure2 Pressure point 2
     * @return Wildpoint enumerable (NO, FIRST, SECOND, BOTH
     */
    protected Wildpoint VerifyWildpoint(double pressure1, double pressure2){
        boolean p1Wildpoint = VerifyWildpoint(pressure1);
        boolean p2Wildpoint = VerifyWildpoint(pressure2);
        if(p1Wildpoint && !p2Wildpoint)
            return Wildpoint.FIRST;
        else if(p1Wildpoint && p2Wildpoint)
            return Wildpoint.BOTH;
        else if(!p1Wildpoint && p2Wildpoint)
            return Wildpoint.SECOND;
        else{
            if(Math.abs(pressure2-pressure1)>10) //We are assuming that the first value in this case is going to be correct (when calling)
                return Wildpoint.SECOND;
            else
                return Wildpoint.NO;
        }
    }
}
