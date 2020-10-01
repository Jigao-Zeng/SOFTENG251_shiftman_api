package shiftman.server;

/**
 * This class convert a String representation of time to a integer representation
 * in order to compare if one time is earlier or later than the other time.
 * @author Jigao Zeng
 *
 */
public class TimeConvert {
	private int _hour;
	private int _minute;
	
	public TimeConvert() {
		System.out.println("Time convert object created");

	}
	
	
	public int convertingTime(String time) {
		// split the "hh:mm" at position ":"
		String[] splitedTime=time.split(":");
		_hour=Integer.parseInt(splitedTime[0]);
		_minute=Integer.parseInt(splitedTime[1]);
		return _hour*60+_minute;
	}
	
	
	
}
	
