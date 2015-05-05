//Per Lange Laursen - s144486
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestPlanActivity {
	/**
	 * Tests the scenario where the project leader creates a activity
	 * with start and end date.
	 * <ol>
	 * <li>Project Leader creates a activity with two different dates or same date
	 * <li>Project Leader creates a activity with an end date before start date.
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */

	Company company;
	Employee projectLeader;
	Project p1;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		Address address = new Address("City", "Street", 1);
		company = new Company("Company", address);
		Executive executive = new Executive("Name", "Department1", company, "password");
				
		// Log in as executive
		assertFalse(company.executiveIsLoggedIn());
		company.executiveLogin("password");
		assertTrue(company.executiveIsLoggedIn());
				
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		p1 = company.createProject("Project01", start, end);
		projectLeader = company.createEmployee("Test", "password", "RandD");
		
		executive.assignProjectLeader(projectLeader,company.getSpecificProject("Project01"));
	}
	
	@Test
	public void testPlanActivityRightDates() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.createActivity(company.getSpecificProject("Project01"), "TestActivity", start, end);
		
		assertEquals(1, company.getSpecificProject("Project01").getActivities().size());
		Activity a = p1.getActivity(p1.getID()+"-TestActivity");
		assertEquals(p1.getID()+"-TestActivity", a.getName());
		assertEquals(start, a.getStart());
		assertEquals(end, a.getEnd());
	}
	
	@Test
	public void testPlanActivityWrongDates() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 22);
		
		company.employeeLogin(projectLeader.getID(), "password");
		try {
			projectLeader.createActivity(company.getSpecificProject("Project01"), "TestActivity", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Incorrect order of dates.",e.getMessage());
			assertEquals("Create activity",e.getOperation());
		}
		
		assertEquals(0, company.getSpecificProject("Project01").getActivities().size());
	}
	
	@Test
	public void testNotProjectLeader() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		Employee test2 = new Employee("Test2", "password", company, "RandD");
		try {
			test2.createActivity(company.getSpecificProject("Project01"), "TestActivity", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
}
