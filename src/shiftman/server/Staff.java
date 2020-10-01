package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The "Staff" class contains information relevant to a staff,
 * including the given name, family name, his or her responsibility.
 * This class also implements some methods relevant to staff.
 * @author Jigao Zeng
 *
 */
public class Staff {
	private String _givenName;
	private String _familyName;
	// _assignedAsWorker is a shift list containing shifts in which the staff has been assigned as worker.
	private List<Shift> _assignedAsWorker;
	// _assignedAsWorker is a shift list containing shifts in which the staff has been assigned as manager.
	private List<Shift> _assignedAsManager;
	//_formattedAssignedShiftAsWorker is the required string representation of _assignedAsWorker list.
	private List<String> _formattedAssignedShiftAsWorker;
	//_formattedAssignedShiftAsWorker is the required string representation of _assignedAsManager list.
	private List<String> _formattedAssignedShiftAsManager;


	public Staff(String givenName,String familyName) {
		_familyName=familyName;
		_givenName=givenName;	

		_assignedAsWorker=new ArrayList<Shift>();
		_assignedAsManager=new ArrayList<Shift>();
		_formattedAssignedShiftAsWorker=new ArrayList<String>();
		_formattedAssignedShiftAsManager=new ArrayList<String>();
	}
	public void AssignAsWorker(Shift shift){
		_assignedAsWorker.add(shift);
	}
	public void AssignAsManager(Shift shift) {
		_assignedAsManager.add(shift);
	}
	public String getFamilyName() {
		return _familyName;
	}
	public String getGivenName() {
		return _givenName;
	}

	/**
	 * This method convert the Shift type of "_assignedAsWorker" to the required String representation type
	 * @return a string list representing the shift list where the staff has been assigned as worker
	 */

	public List<String> formattedAssignedShiftAsWorker(){
		if (_assignedAsWorker.isEmpty()) {
			return Collections.emptyList();
		}
		// first sort the "_assignedAsWorker" list
		Collections.sort(_assignedAsWorker,new DayOfWeekComparator());
		// convert each shift in the list to a string representation and then add to the string list
		for (Shift shiftElement:_assignedAsWorker) {

			String convertedString=shiftElement.getDay()+"["+shiftElement.getStartTime()+"-"+shiftElement.getEndTime()+"]";
			if (!_formattedAssignedShiftAsManager.contains(convertedString)) {
				_formattedAssignedShiftAsWorker.add(convertedString);
			}
		}
		return _formattedAssignedShiftAsWorker;
	}


	/**
	 * This method works similarly as "formattedAssignedShiftAsWorker",
	 * it converts the Shift type of "_assignedAsManager" to the required String representation type
	 * @return a string list representing the shift list where the staff has been assigned as manager
	 */

	public List<String> formattedAssignedShiftAsManager(){
		if (_assignedAsManager.isEmpty()) {
			return Collections.emptyList();
		}
		// first sort the "_assignedAsManager" list
		Collections.sort(_assignedAsManager,new DayOfWeekComparator());
		// convert each shift in the list to a string representation and then add to the string list
		for (Shift shiftElement:_assignedAsManager) {
			String convertedString=shiftElement.getDay()+"["+shiftElement.getStartTime()+"-"+shiftElement.getEndTime()+"]";
			if (!_formattedAssignedShiftAsManager.contains(convertedString)) {
				_formattedAssignedShiftAsManager.add(convertedString);

			}
		}
		return _formattedAssignedShiftAsManager;
	}

}
