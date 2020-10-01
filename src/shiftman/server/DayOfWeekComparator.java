
package shiftman.server;
import java.util.Comparator;
/**
 * This "DayOfWeekComparator" is used to sort shift list based on the earlist start day and earliest start time of each shift,
 * The compare method compare the "dayOfWeek" first,
 * if the "dayOfWeek" is same, then compare the "startTime"
 * Both "dayOfWeek" and "startTime" is in "int" representation for this method to work as expected.
 * @author Jigao Zeng
 *
 */
public class DayOfWeekComparator implements Comparator<Shift> {

	@Override
	public int compare(Shift sh1,Shift sh2) {

		if (sh1.getDay().equals(sh2.getDay())){
			return sh1.intRepresentationOfStartTime()-sh2.intRepresentationOfStartTime();
		}
		return sh1.intRepresentationOfDay()-sh2.intRepresentationOfDay();
	}
}



