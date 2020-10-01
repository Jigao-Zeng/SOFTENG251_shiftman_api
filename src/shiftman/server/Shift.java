package shiftman.server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * This "Shift" class contains relevant information for a shift,
 * including the start day, start time and end time, minimum number of workers required,the manager, and a worker list.
 * This class also implements some methods relevant to shift.
 * @author Jigao Zeng
 *
 */
public class Shift {
	private String _startTime;
	private String _endTime;
	private String _minimumWorkers;
	private String _dayOfWeek;
	private Staff _manager;
	private List<Staff> _workerList;
	private List<String> _formatOfWorkerList;



	public Shift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		_startTime=startTime;
		_endTime=endTime;
		_minimumWorkers=minimumWorkers;
		_dayOfWeek=dayOfWeek;
		_manager=null;
		_workerList=new ArrayList<Staff>();

		System.out.println("worker list used to adding worker in for a shift is created");
		_formatOfWorkerList=new ArrayList<String>();
		System.out.println("formatted worker list is created");
		System.out.println("shift object is created");

	}

	public void findManager(Staff manager) {

		_manager=manager;
	}

	public void findWorker(Staff worker) {
		_workerList.add(worker);
	}



	/**
	 * The"overlap" method check if the shift we want to add overlaps with an existing shift in the shift list in the roster.
	 * @param existingShift the existing shift we want to check if it overlaps with the shift to add
	 * @return overlap a boolean indicates whether overlap happen or not
	 */
	public boolean overlap(Shift existingShift) {

		boolean overlap=true;


		// if the day of two shifts is different, there will be no overlap
		if(!existingShift.getDay().equals(_dayOfWeek)){
			overlap=false;
		}
		else {
			/*
			 * create a "timeConvert" object and invoke the "convertingTime"
			 * to change the time type from string to int in order to compare one time is early or late than the other time.
			 */

			TimeConvert timeConvert=new TimeConvert();
			int existingStartTime=timeConvert.convertingTime(existingShift.getStartTime());
			int existingEndTime=timeConvert.convertingTime(existingShift.getEndTime());
			int newStartTime=timeConvert.convertingTime(_startTime);
			int newEndTime=timeConvert.convertingTime(_endTime);
			// if one shift' startTime is late than the other shift's endTime, there will be no overlap
			if(existingStartTime>newEndTime||newStartTime>existingEndTime) {
				overlap=false;
			}
		}
		return overlap;
	}



	/**
	 * The "formatWorkerList" change staff representation of "workerList" to its string representation. 
	 */
	public void formatWorkerList() {
		if (_workerList.isEmpty()) {
			if(_formatOfWorkerList.isEmpty()) {
				_formatOfWorkerList.add("No workers assigned");
			}
		}
		else {
			Collections.sort(_workerList,new FamilyNameComparator());
			// because workerList in this case is not empty, so we need to remove the sentence: "No workers assigned"
			if (_formatOfWorkerList.contains("No workers assigned")) {
				_formatOfWorkerList.remove("No workers assigned");
			}
			for (Staff element:_workerList) {
				if(_formatOfWorkerList.isEmpty()) {
					_formatOfWorkerList.add(element.getGivenName()+" "+element.getFamilyName());			
				}
				else if (!_formatOfWorkerList.contains(element.getGivenName()+" "+element.getFamilyName())){
					_formatOfWorkerList.add(element.getGivenName()+" "+element.getFamilyName());		
				}
			}
		}
	}

	/**
	 * The purpose of "intRepresentationOfDay" method is serving for the DayOfWeekComparator to work as expected.
	 * @return this method return a integer representing if a "dayOfWeek" is earlier or later than the other "dayOfWeek".
	 */
	public int intRepresentationOfDay() {
		if (_dayOfWeek.equals("Monday")) {
			return 1;
		}
		if (_dayOfWeek.equals("Tuesday")) {
			return 2;
		}
		if (_dayOfWeek.equals("Wednesday")) {
			return 3;
		}
		if (_dayOfWeek.equals("Thursday")) {
			return 4;
		}
		if(_dayOfWeek.equals("Friday")) {
			return 5;
		}
		if (_dayOfWeek.equals("Saturday")) {
			return 6;
		}
		return 7;
	}

	/**
	 * The "intRepresentationOfStartTime" method return an integer representation of time
	 * that can be used to compare if one time is earlier or later than the other.
	 * This method serve for "DayOfWeekComparator".
	 * @return a integer representation of time
	 */
	public int intRepresentationOfStartTime() {
		TimeConvert timeConvert=new TimeConvert();

		return timeConvert.convertingTime(_startTime);
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
	public int getMinWorkers() {
		return Integer.parseInt(_minimumWorkers);
	}

	public Staff getManager() {
		return _manager;
	}
	public int getNumberOfWorkers(){
		return _workerList.size();
	}
	public List<String> getFormattedWorkerList(){
		return _formatOfWorkerList;
	}

}
