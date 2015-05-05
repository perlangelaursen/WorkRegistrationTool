//Per Lange Laursen - s144486
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestRelieveEmployee {
	/**
         * Tests the scenario where the project leader want to relieve
         * an employee from a project
         * <ol>
         * <li>Project Leader relieves an employee
         * </ol>
         * @throws OperationNotAllowedException
         */


	public Company company;
	public Employee projectLeader, test1;
	
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
		company.createProject("Project01", start, end);
		company.createProject("Project02");
				
		projectLeader = company.createEmployee("BENR", "password", "RandD");
				
		executive.assignProjectLeader(projectLeader,company.getSpecificProject("Project01"));
		test1 = company.createEmployee("SAMU", "password", "RandD");
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.assignEmployeeProject(test1, company.getSpecificProject("Project01"));
	}
	
	@Test
	public void testRelieveEmployee01() throws OperationNotAllowedException {
		assertEquals(1, company.getSpecificProject("Project01").getEmployees().size());
		
		projectLeader.relieveEmployeeProject(test1, company.getSpecificProject("Project01"));
		
		assertEquals(0, company.getSpecificProject("Project01").getEmployees().size());
	}
	
	@Test
	public void testNotLoggedIn() throws OperationNotAllowedException {
		company.employeeLogout();
		
		try {
			projectLeader.relieveEmployeeProject(test1, company.getSpecificProject("Project01"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}
	
	@Test
	public void testNotProjectLeader() throws OperationNotAllowedException {
		assertEquals(1, company.getSpecificProject("Project01").getEmployees().size());
		Employee test2 = company.createEmployee("HANS", "password", "RandD");
		company.employeeLogin(test2.getID(), "password");
		try {
			test2.relieveEmployeeProject(test1, company.getSpecificProject("Project01"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}

}
