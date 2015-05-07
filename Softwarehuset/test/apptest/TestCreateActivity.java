//Per Lange Laursen - s144486
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestCreateActivity {
	/**
	 * Tests the scenario where an activity is created
	 * <ol>
	 * <li>Projectleader creates an activity
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */
	
	Employee projectLeader;
	Employee test1;
	Company company;
	
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
		Project p1 = company.createProject("Project01", start, end);
		Project p2 = company.createProject("Project02");
		
		projectLeader = company.createEmployee("BAMS", "password", "RanD");
		
		executive.assignProjectLeader("BAMS",p1.getID());
		test1 = company.createEmployee("LAMP", "password", "RanD");
	}
	
	//Successfully created activity
	@Test
	public void testCreateActivity01() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		assertEquals(0, company.getProject("Project01").getActivities().size());
		company.employeeLogin("BAMS", "password");
		projectLeader.createActivity(company.getProject("Project01"), "TestActivity", start, end);
		assertEquals(1, company.getProject("Project01").getActivities().size());
	}

	//Logged in employee is not project leader
	@Test
	public void testCreateActivity02() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		assertEquals(0, company.getProject("Project01").getActivities().size());
		
		Employee test2 = company.createEmployee("JANU", "password", "RanD");
		company.employeeLogin("JANU", "password");
		try {
			test2.createActivity(company.getProject("Project01"), "TestActivity", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
	
	//End date before start date
	@Test
	public void testCreateActivity03() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 29);
		end.set(2016, Calendar.JANUARY, 25);
		
		assertEquals(0, company.getProject("Project01").getActivities().size());
		company.employeeLogin("BAMS", "password");
		try {
			projectLeader.createActivity(company.getProject("Project01"), "TestActivity", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Incorrect order of dates.",e.getMessage());
			assertEquals("Create activity",e.getOperation());
		}
		
		assertEquals(0, company.getProject("Project01").getActivities().size());
	}
	
	//Not logged in
	@Test
	public void testCreateActivity04() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		company.employeeLogout();
		try {
			projectLeader.createActivity(company.getProject("Project01"), "TestActivity", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
		assertEquals(0, company.getProject("Project01").getActivities().size());
	}
}
