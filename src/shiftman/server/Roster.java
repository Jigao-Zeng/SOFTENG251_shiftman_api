package shiftman.server;

import java.util.ArrayList;
import java.util.List;
/**
 * The "Roster" class contains the relevant information in a roster,
 * including shop name, a list of working hours, a list of shifts, a list of staffs
 * the class also provide methods to add any working hours, any shift, or any staffs into its list 
 * @author Jigao Zeng
 */
public class Roster {
	private String _shopName;
	private List<WorkingHour> _workingTimeList;
	private List<Shift> _shiftList;
	private List<Staff> _staffList;
	
	
	

	public Roster(String shopName) {
		_shopName=shopName;
		_workingTimeList=new ArrayList<WorkingHour>();
		System.out.println("working Time list object created");
		_shiftList=new ArrayList<Shift>();
		System.out.println("shift List object created");
		_staffList=new ArrayList<Staff>();
		System.out.println("staff List object created");
		System.out.println("Roster object created");
		
	}
	public void addingWorkingHour(WorkingHour workingHour) {
		_workingTimeList.add(workingHour);
	}
	public void addShift(Shift shift) {
		_shiftList.add(shift);
	}
	public void addStaff(Staff staff) {
		_staffList.add(staff);
	}
	
	
	
	
	
	public List<WorkingHour> getWorkingHourList(){
		return _workingTimeList;
	}
	public List<Shift> getShiftList(){
		return _shiftList;
	}
	public List<Staff> getStaffList(){
		return _staffList;
	}
	
	public String getShopName() {
		return _shopName;
	}
}

