package shiftman.server;

/**
 * This "ValidDayTimeChecker" is used to check the given "dayOfWeek","startTime" and "endTime" is valid or not
 * @author Jigao Zeng
 *
 */
public class ValidDayTimeChecker {
	private String[] _daysInAWeek;
	private String[]_hoursInADay;
	private String[] _minutesInAnHour;



	public ValidDayTimeChecker(){
		// this three string arrays give the possible right inputs
		_daysInAWeek=new String[] {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
		_hoursInADay=new String[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
		_minutesInAnHour=new String[] {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};

	}

	public String check(String dayOfWeek, String startTime, String endTime) {
		// check if the "dayOfWeek" is valid
		boolean rightDay=false;
		for (int i=0;i<7;i++) {
			if (_daysInAWeek[i].equals(dayOfWeek)) {
				rightDay=true;
			}
		}
		if (!rightDay) {
			return "ERROR: Invalid day";
		}
		//check if the format of time is hh:mm;
		//first check if the length is 5, then check if the ":" is in right position
		boolean rightTimeFormat=true;
		if (startTime.length()!=5||endTime.length()!=5||startTime.indexOf(':')!=2) {
			rightTimeFormat=false;
			return "ERROR: Invalid time";
		}


		//check if the hour is valid
		boolean rightStartHour=false;
		boolean rightEndHour=false;
		if (rightTimeFormat) {

			// get the first two characters(which is the hour of the time) from startTime and endTime

			String startHour=""+startTime.charAt(0)+startTime.charAt(1);
			String endHour=""+endTime.charAt(0)+endTime.charAt(1);
			for (int i=0;i<_hoursInADay.length;i++) {
				if (_hoursInADay[i].equals(startHour)){
					rightStartHour=true;
				}	
			}
			if (!rightStartHour) {
				return "ERROR:Invalid time";
			}
			for (int j=0;j<_hoursInADay.length;j++) {
				if(_hoursInADay[j].equals(endHour)){
					rightEndHour=true;
				}
			}
			if (!rightEndHour) {
				return "ERROR:Invalid time";
			}
		}

		//check if the minute is valid
		boolean rightStartMinute=false;
		boolean rightEndMinute=false;
		String startMinute=""+startTime.charAt(3)+startTime.charAt(4);
		String endMinute=""+endTime.charAt(3)+endTime.charAt(4);
		if (rightStartHour&&rightEndHour) {
			for (int i=0;i<_minutesInAnHour.length;i++) {
				if (_minutesInAnHour[i].equals(startMinute)){
					rightStartMinute=true;
				}
			}
			if (!rightStartMinute) {
				return "ERROR:Invalid time";
			}
			for (int j=0;j<_minutesInAnHour.length;j++) {
				if(_minutesInAnHour[j].equals(endMinute)){

					rightEndMinute=true;
				}
			}
			if (!rightEndMinute) {
				return "ERROR:Invalid time";
			}

		}
		if (rightStartMinute&&rightEndMinute) {
			// check if the startTime is after endTime
			// create "timeConvert" object and invoke "convertingTime" method to get int time to check if one time is after the other
			TimeConvert timeConvert=new TimeConvert();
			int intStartTime=timeConvert.convertingTime(startTime);
			int intEndTime=timeConvert.convertingTime(endTime);
			if (intStartTime>intEndTime) {
				return "ERROR: the start time is the same or after the end time";
			}
		}
		// return empty string if no error found
		return "";
	}	
}



