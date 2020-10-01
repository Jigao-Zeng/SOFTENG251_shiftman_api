package shiftman.server;

/**
 * The "workingHour" class combine "dayOfWeek","startTime","endTime" together to connect them
 * @author Jigao Zeng
 *
 */
public class WorkingHour {
	private String _dayOfWeek;
	private String _startTime;
	private String _endTime;
	public WorkingHour(String dayOfWeek, String startTime, String endTime) {
		_dayOfWeek=dayOfWeek;
		_startTime=startTime;
		_endTime=endTime;		
	}
		
	
	public String getDay() {
		return _dayOfWeek;
	}
	public String getStartTime() {
		return _startTime;
	}
	public String getEndTime() {
		return _endTime;
	}

}
