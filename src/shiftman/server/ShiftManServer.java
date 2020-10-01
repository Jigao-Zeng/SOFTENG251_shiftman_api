package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class implements the "shiftman.server.ShiftMan" interface,
 * The main purpose of this class is to managing a roster in a shop, more details are given in the interface which the class implements,
 * each method details are given in the shiftman.server.ShiftMan interface.
 * 
 * @author Jigao Zeng
 *
 */
public class ShiftManServer implements shiftman.server.ShiftMan{

	private Roster _roster;

	public ShiftManServer() {		
		System.out.println("shiftManServer object created");
	}
	@Override
	public String newRoster(String shopName) {

		if (shopName==null||shopName.isEmpty()) {
			return "ERROR:The supplied name is null or empty";
		}

		else {
			Roster roster=new Roster(shopName);
			_roster=roster;
			return "";
		}
	}

	@Override
	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {
		//create a ValidDayTime Checker object to check
		// the checker.check(dayOfWeek,startTime,endTime) method returns empty string if the day and time is valid, it returns a string containing error message when day or time is invalid. 
		ValidDayTimeChecker checker=new ValidDayTimeChecker();
		if (checker.check(dayOfWeek, startTime, endTime).isEmpty()) {
			WorkingHour workingHour=new WorkingHour(dayOfWeek,startTime,endTime);
			// try and catch the NullPointerException when the roster has not been set
			try{_roster.addingWorkingHour(workingHour);

			}
			catch (NullPointerException e) {
				return "ERROR:roster has not been set";
			}

		}

		return checker.check(dayOfWeek, startTime, endTime);
	}



	@Override
	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		// create checker object to check if the given day and time is valid.
		ValidDayTimeChecker checker=new ValidDayTimeChecker();
		if (checker.check(dayOfWeek, startTime, endTime).isEmpty()) {
			Shift shift=new Shift(dayOfWeek,startTime,endTime,minimumWorkers);
			//First check if "dayOfWeek" belongs to workingHour List, then check if the shift is within the working hour.
			boolean withinDay=false;
			WorkingHour toCheck = null;
			// try and catch the NullPointerException when the roster has not been set.
			try{
				for (int i=0;i<_roster.getWorkingHourList().size();i++){
					if (_roster.getWorkingHourList().get(i).getDay().equals(dayOfWeek)){
						toCheck=_roster.getWorkingHourList().get(i);
						withinDay=true;		
					}
				}
			}
			catch(NullPointerException e) {
				return "ERROR: roster has not been set";
			}
			if (!withinDay) {

				return "shift is not within the workinghour";
			}
			// create "timeConvert" object and call the "convertingTime" method to convert time into a integer in order to compare the time.
			TimeConvert timeConvert=new TimeConvert ();
			int shiftStartTime=timeConvert.convertingTime(startTime);
			int shiftEndTime=timeConvert.convertingTime(endTime);
			int workStartTime=timeConvert.convertingTime(toCheck.getStartTime());
			int workEndTime=timeConvert.convertingTime(toCheck.getEndTime());

			if (shiftStartTime<workStartTime||shiftEndTime>workEndTime) {

				return "shift not within working hour";
			}
			//loop through the current shiftList in roster and use "overlap" method to check if the shift overlaps with existing shift. 
			for (int i=0;i<_roster.getShiftList().size();i++) {
				if (shift.overlap(_roster.getShiftList().get(i))){
					return "shift overlaps with the existing shift";
				}
			}
			_roster.addShift(shift);	
			return "";
		}
		else {
			return checker.check(dayOfWeek, startTime, endTime);
		}
	}

	@Override
	public String registerStaff(String givenname, String familyName)  {

		if (givenname.isEmpty()||familyName.isEmpty()) {
			return "ERROR:one or both of givenname and familyName is empty";
		}
		// try and catch the NullPointerException if the roster has not been set
		try {
			// loop through the staffList in the roster to check if the staff has already been registered
			for (int i=0;i<_roster.getStaffList().size();i++) {
				if(givenname.equalsIgnoreCase(_roster.getStaffList().get(i).getGivenName())&&familyName.equalsIgnoreCase(_roster.getStaffList().get(i).getFamilyName())) {
					return "ERROR:the staff has already registered";				
				}
			}
		}
		catch(NullPointerException e) {
			return "ERROR:the roster has not been set";
		}
		Staff staff=new Staff(givenname,familyName);
		_roster.addStaff(staff);
		return "";
	}



	@Override
	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName,
			boolean isManager)  {

		Staff staff=null;
		Shift shift=null;
		ValidDayTimeChecker checker=new ValidDayTimeChecker();
		// first check if the three inputs:"dayOfWeek","startTime","endTime" is valid.
		if (checker.check(dayOfWeek, startTime, endTime).isEmpty()) {
			boolean shiftBeenSet=false;
			// try and catch the NullPointerException if the roster has not been set
			try {
				// check if the shift has been set or not.
				for (Shift shiftElement:_roster.getShiftList()) {
					if (shiftElement.getDay().equals(dayOfWeek)&&(shiftElement.getStartTime().equals(startTime))&&shiftElement.getEndTime().equals(endTime)){
						shiftBeenSet=true;
						shift=shiftElement;

					}
				}
			}
			catch(NullPointerException e) {
				return "ERROR:the roster has not been set";
			}
			if(!shiftBeenSet) {
				return "ERROR: the shift has not been set";
			}
			// check if the staff has been registered
			boolean staffBeenRegistered=false;
			for (Staff staffElement:_roster.getStaffList()) {
				if (staffElement.getFamilyName().equalsIgnoreCase(familyName)&&staffElement.getGivenName().equalsIgnoreCase(givenName)) {
					staffBeenRegistered=true;
					staff=staffElement;
				}
			}
			if (!staffBeenRegistered) {
				return "ERROR:the staff has not been registered";
			}

			// Three different situations when assigning a staff to a shift are given below 
			if (isManager&&shift.getManager()!=null) {
				return "ERROR:a manager already exists";					
			}

			else if (isManager&&shift.getManager()==null) {
				shift.findManager(staff);
				staff.AssignAsManager(shift);
			}
			else if (!isManager) {
				shift.findWorker(staff);
				staff.AssignAsWorker(shift);
			}	

			return "";
		}else {
			return checker.check(dayOfWeek, startTime, endTime);
		}
	}


	@Override
	public List<String> getRegisteredStaff()  {

		//again, try and catch the exception when roster has not been set
		// call the staffListToStringList method to convert staffList to a stringList representing the staffList.
		try{
			if (_roster.getStaffList().isEmpty()) {
				return Collections.emptyList();
			}
		}
		catch (NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;
		}
		// First sort the given staffList
		Collections.sort(_roster.getStaffList(),new FamilyNameComparator());
		List<String> stringList=new ArrayList<String>();
		// convert each staff element to its string representation, then add to stringList.
		for (Staff element:_roster.getStaffList()) {
			String convertedString=element.getGivenName()+" "+element.getFamilyName();
			if (!stringList.contains(convertedString)) {
				stringList.add(element.getGivenName()+" "+element.getFamilyName());
			}

		}		
		return stringList;
	}


	@Override
	public List<String> getUnassignedStaff() {

		List<Staff> unAssignedStaffList=new ArrayList<Staff>();
		//again, try and catch the NullPointerException when roster has not been set
		try{
			//in terms of each staff in staff list,if any staff is not manager nor worker,add them to unAssignedStaff list
			for (Staff element:_roster.getStaffList()) {

				if (element.formattedAssignedShiftAsManager().isEmpty()&&element.formattedAssignedShiftAsWorker().isEmpty()) {
					if (!unAssignedStaffList.contains(element)) {
						unAssignedStaffList.add(element);
					}
				}
			}
		}
		catch (NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;
		}
		//convert to a string list representation
		if (unAssignedStaffList.size()==0) {
			return Collections.emptyList();
		}
		// First sort the given staffList
		Collections.sort(unAssignedStaffList,new FamilyNameComparator());
		List<String> stringList=new ArrayList<String>();
		// convert each staff element to its string representation, then add to stringList.
		for (Staff element:unAssignedStaffList) {
			String convertedString=element.getGivenName()+" "+element.getFamilyName();
			if (!stringList.contains(convertedString)) {
				stringList.add(element.getGivenName()+" "+element.getFamilyName());
			}

		}		
		return stringList;

	}



	@Override
	public List<String> shiftsWithoutManagers() {
		List<Shift> shiftsWithoutManager=new ArrayList<Shift>();
		//again, try and catch the NullPointerException when roster has not been set
		try {
			// in terms of each shift in the shift list, if any shift has no manager,add to the "shiftsWithourManager" list.
			for (Shift element:_roster.getShiftList()){
				if (element.getManager()==null&&!shiftsWithoutManager.contains(element)){
					shiftsWithoutManager.add(element);	
				}
			}
		}
		catch(NullPointerException e){
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;		
		}
		// convert the shift type list to a string representation list
		if (shiftsWithoutManager.size()==0) {
			return Collections.emptyList();
		}
		// First sort the  shiftList 
		Collections.sort(shiftsWithoutManager,new DayOfWeekComparator());
		List<String> stringList=new ArrayList<String>();
		// convert each shift element to its string representation, then add to stringList.
		for (Shift element:shiftsWithoutManager) {
			String convertedString=element.getDay()+"["+element.getStartTime()+"-"+element.getEndTime()+"]";
			if (!stringList.contains(convertedString)){
				stringList.add(convertedString);
			}
		}
		return stringList;
	}



	/**
	 * <b>(1 Mark)</b> Request the shifts that do not have enough workers assigned.
	 * @return A list of strings describing shifts that have fewer workers than the minimum required,
	 * one string describing one shift.
	 * The format of the shift description is as described in the notes above.
	 * The order of the shifts is in the order of day of the week, and then by start time.
	 * If there are no such shifts, return an empty list.
	 * @throws Exception 
	 */

	@Override
	public List<String> understaffedShifts()  {

		List<Shift> underStaffedShifts=new ArrayList<Shift>();
		//again, try and catch the NullPointerException when roster has not been set
		try {
			// in terms of each shift in the shift list, if the current number of workers is less than the minimum number of workers required, add the shift to the list created.
			for (Shift element:_roster.getShiftList()){
				if(element.getMinWorkers()>element.getNumberOfWorkers()&&!underStaffedShifts.contains(element)){
					underStaffedShifts.add(element);
				}
			}
		}
		catch(NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;				
		}
		// convert the shift type list to a string representation list
		if (underStaffedShifts.size()==0) {
			return Collections.emptyList();
		}
		// First sort the  shiftList 
		Collections.sort(underStaffedShifts,new DayOfWeekComparator());
		List<String> stringList=new ArrayList<String>();
		// convert each shift element to its string representation, then add to stringList.
		for (Shift element:underStaffedShifts) {
			String convertedString=element.getDay()+"["+element.getStartTime()+"-"+element.getEndTime()+"]";
			if (!stringList.contains(convertedString)){
				stringList.add(convertedString);
			}
		}
		return stringList;
	}

	@Override
	public List<String> overstaffedShifts()  {

		List<Shift> overStaffedShifts=new ArrayList<Shift>();
		//again, try and catch the NullPointerException when roster has not been set
		try {
			for (Shift element:_roster.getShiftList()){
				if (element.getMinWorkers()<element.getNumberOfWorkers()&&!overStaffedShifts.contains(element)){
					overStaffedShifts.add(element);
				}
			}
		}
		catch(NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;	

		}
		// convert the shift type list to a string representation list
		if (overStaffedShifts.size()==0) {
			return Collections.emptyList();
		}
		// First sort the shiftList 
		Collections.sort(overStaffedShifts,new DayOfWeekComparator());
		List<String> stringList=new ArrayList<String>();
		// convert each shift element to its string representation, then add to stringList.
		for (Shift element:overStaffedShifts) {
			String convertedString=element.getDay()+"["+element.getStartTime()+"-"+element.getEndTime()+"]";
			if (!stringList.contains(convertedString)){
				stringList.add(convertedString);
			}
		}
		return stringList;
	}



	@Override
	public List<String> getRosterForDay(String dayOfWeek)  {
		//again, try and catch the NullPointerException when roster has not been set
		try {
			if (_roster.getShopName().isEmpty()) {
				String errorMessage=  "ERROR: No shop name has not been given";
				List<String> errorList=new ArrayList<String>();
				errorList.add(errorMessage);
				return errorList;
			}
		}
		catch (NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;
		}
		// check if the given "dayOfWeek" is valid.
		boolean rightDay=false;
		String[] daysOfWeek= {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
		for (int i=0;i<7;i++) {
			if (daysOfWeek[i].equals(dayOfWeek)) {
				rightDay=true;
			}
		}
		if (!rightDay) {
			String errorMessage=  "ERROR: Invalid day";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;
		}
		WorkingHour workingHour=null;
		// check if there is no working hour on the given day
		boolean noWorkingHourOnThisDay=true;
		for (WorkingHour eachWorkingHour:_roster.getWorkingHourList()) {
			if (eachWorkingHour.getDay().equals(dayOfWeek)) {
				workingHour=eachWorkingHour;
				noWorkingHourOnThisDay=false;
			}
		}
		if (noWorkingHourOnThisDay) {
			return Collections.emptyList();
		}
		// check any shifts on this given day
		boolean shiftexist=false;
		List<Shift> ThisDayShifts=new ArrayList<Shift>(); 
		for (Shift element:_roster.getShiftList()) {
			if (element.getDay().equals(dayOfWeek)&&!ThisDayShifts.contains(element)) {
				ThisDayShifts.add(element);
				shiftexist=true;
			}

		}
		if (!shiftexist) {
			return Collections.emptyList();
		}
		List<String> FormatOfRosterForDay=new ArrayList<String>();

		//add the shop name
		FormatOfRosterForDay.add(_roster.getShopName());


		//add the working hour 
		FormatOfRosterForDay.add(workingHour.getDay()+" " + workingHour.getStartTime()+"-"+workingHour.getEndTime());


		//sort "ThisDayShifts" list based on the rule of "DayOfWeekComparator"
		Collections.sort(ThisDayShifts,new DayOfWeekComparator());
		//get string representation of worker list of each shift by invoking "formatWorkerList" method.
		for (Shift eachShift:ThisDayShifts) {
			eachShift.formatWorkerList();
			// add the required string format to the "FormatOfRosterForDay"
			if(eachShift.getManager()==null) {
				String stringFormat=eachShift.getDay()+"["+eachShift.getStartTime()+"-"+eachShift.getEndTime()+"]"+" "+"[No manager assigned]"+" "+eachShift.getFormattedWorkerList().toString();
				if (!FormatOfRosterForDay.contains(stringFormat));
				FormatOfRosterForDay.add(stringFormat);
			}else {
				String format=eachShift.getDay()+"["+eachShift.getStartTime()+"-"+eachShift.getEndTime()+"]"+" "+" Manager:"+eachShift.getManager().getFamilyName()+", "+eachShift.getManager().getGivenName()+" "+eachShift.getFormattedWorkerList().toString();
				if (!FormatOfRosterForDay.contains(format)) {
					FormatOfRosterForDay.add(format);
				}
			}
		}
		return FormatOfRosterForDay;
	}
	/**
	 * <b>(2 Marks)</b> Request the shifts that the staff member with the supplied name is assigned to (i.e not as manager).
	 * @param workerName The name of the staff member in format: <b><tt>given name</tt></b>" "<b><tt>family name</tt></b>
	 * @return a list of strings describing the shifts the staff member is a worker for.
	 * <ul>
	 * <li>
	 * The first entry should have the name of the worker with the format <b><tt>family name</tt></b>", "<b><tt>given name</tt></b> (i.e. family
	 * name first)
	 * <li>
	 * The remaining strings are the shifts, one string per shift, using the format described in the notes above.
	 * </ul>
	 * The order of the shifts is in the order of day of the week, and then by start time.
	 * If there are no such shifts, return an empty list. <span style="color:red">Empty list is inconsistent with the requirement that the first entry should be the worker's name, however the requirement remains that in this case what is returned is an empty list.</span>
	 * <p>Possible problems include: the staff member is not registered.
	 * @throws Exception 
	 */


	@Override
	public List<String> getRosterForWorker(String workerName) {

		List<String> rosterForWorker=new ArrayList<String>();

		String[] nameArray=workerName.split(" ");
		String familyName=nameArray[1];
		String givenName=nameArray[0];

		// check if the staff is registered
		boolean registered=false;
		Staff staff=null;
		//again, try and catch the NullPointerException when roster has not been set
		try {
			for (Staff staffElement:_roster.getStaffList()) {
				if (staffElement.getFamilyName().equalsIgnoreCase(familyName)&&staffElement.getGivenName().equalsIgnoreCase(givenName)) {
					registered=true;
					staff=staffElement;
				}
			}
		}
		catch(NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;		
		}
		if (!registered) {
			String errorMessage= "ERROR: the staff is not registered";
			rosterForWorker.add(errorMessage);
			return rosterForWorker;
		}
		// check if the staff has been assigened as worker
		if (staff.formattedAssignedShiftAsWorker().isEmpty()) {
			return Collections.emptyList();
		}	
		rosterForWorker.add(familyName+", "+givenName);
		// add all shifts in the shift list where the staff has been assigned as worker
		rosterForWorker.addAll(staff.formattedAssignedShiftAsWorker());
		return rosterForWorker;
	}

	/**
	 * <b>(1 Mark)</b> Request the shifts that the staff member with the supplied name is the manager for.
	 * @param managerName The name of the staff member in format: <b><tt>given name</tt></b>" "<b><tt>family name</tt></b>
	 * @return a list of strings describing the shifts the staff member is the manager for.
	 * <ul>
	 * <li>
	 * The first entry should have the name of the manager with the format <b><tt>family name</tt></b>", "<b><tt>given name</tt></b> (i.e. family
	 * name first)
	 * <li>
	 * The remaining strings are the shifts, one string per shift, using the format described in the notes above.
	 * </ul>
	 * The order of the shifts is in the order of day of the week, and then by start time.
	 * If there are no such shifts, return an empty list. <span style="color:red">Empty list is inconsistent with requirement for first two entrys, however the requirement remains that in this case what is returned is an empty list.</span>
	 * <P>Possible problems include: the staff member is not registered.
	 * @throws Exception 
	 */

	@Override
	public List<String> getShiftsManagedBy(String managerName)  {

		List<String> rosterForManager=new ArrayList<String>();
		// figure out the family name and given name of the manager.
		String[] nameArray=managerName.split(" ");
		String familyName=nameArray[1];
		String givenName=nameArray[0];

		// check if the staff is registered
		boolean registered=false;
		Staff staff=null;
		//again, try and catch the NullPointerException when roster has not been set
		try {
			for (Staff staffElement:_roster.getStaffList()) {
				if (staffElement.getFamilyName().equalsIgnoreCase(familyName)&&staffElement.getGivenName().equalsIgnoreCase(givenName)) {
					registered=true;
					staff=staffElement;
				}
			}
		}
		catch (NullPointerException e) {
			String errorMessage= "ERROR:no roster has been set";
			List<String> errorList=new ArrayList<String>();
			errorList.add(errorMessage);
			return errorList;		
		}
		if (!registered) {
			List<String>errorList=new ArrayList<String>();
			String errorMessage= "ERROR: the staff is not registered";
			errorList.add(errorMessage);
			return errorList;
		}
		// check if the staff has been assigned as manager in any shift
		else if (staff.formattedAssignedShiftAsManager().isEmpty()) {
			return Collections.emptyList();
		}

		else {
			rosterForManager.add(familyName+", "+givenName);
			// add all shifts in the shift list where the staff has been assigned as worker
			rosterForManager.addAll(staff.formattedAssignedShiftAsManager());
			return rosterForManager;
		}
	}



	@Override
	public String reportRosterIssues() {

		return null;
	}

	@Override
	public String displayRoster() {

		return null;
	}

}
