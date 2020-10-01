package shiftman.server;
import java.util.Comparator;
/**
 * The "FamilyNameComparator" is used to sort staff list
 * The "compare" method compare two staff,
 * based on the alphabet order of their family name
 * @author Jigao Zeng
 *
 */
public class FamilyNameComparator implements Comparator<Staff> {
	
	@Override
	public int compare(Staff s1,Staff s2) {
		
		return s1.getFamilyName().compareToIgnoreCase(s2.getFamilyName());
		
	}
}

	