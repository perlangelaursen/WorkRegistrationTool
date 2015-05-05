//Per Lange Laursen - s144486

package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestAssignEmployeeToProjectAndActivity {
	/**
	 * Tests the scenario where a project or activity is created
	 * then a projectleader or activityleader is assigned.
	 * <ol>
	 * <li>Projectleader is assigned and a employee is added to a project
	 * <li>Activityleader is assigned and a employee is added to a activity
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */
	
	Employee projectLeader;
	Employee test1;
	Company company;
	Project p1, p2;
	
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
		p2 = company.createProject("Project02");
		
		projectLeader = company.createEmployee("ABCD", "password", "Department1");
		
		executive.assignProjectLeader("ABCD",p1.getID());
		test1 = company.createEmployee("HFBJ", "password", "Department1");
	}
	
	@Test
	public void testAssignEmployeeProject() throws OperationNotAllowedException {
		
		try {
			company.createEmployee("Anders", "password", "department");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee ID must be the length of 4 letters",e.getMessage());
			assertEquals("Create employee",e.getOperation());
		}
		
		try {
			company.createEmployee("AND1", "password", "department");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee ID must not contain any numbers",e.getMessage());
			assertEquals("Create employee",e.getOperation());
		}	
		
		company.employeeLogin("ABCD", "password");
		Employee test3 = company.createEmployee("ANDS", "password", "department");
			
		//test3 employee is not assigned to project p1
		assertNull(p1.getEmployee("ANDS"));
		projectLeader.assignEmployeeProject(test3.getID(), p1.getName());
		assertEquals(p1.getEmployee("ANDS"), test3);
		
		//Check a random not-employee is not assigned to project p1
		assertNull(p1.getEmployee("STEF"));
		
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.assignEmployeeProject(test1.getID(), "Project01");
		// Check for ID and department
		assertEquals(company.getProject("Project01").getEmployee("HFBJ").getID(), test1.getID());
		assertEquals(company.getProject("Project01").getEmployee("HFBJ").getDepartment(), test1.getDepartment());
	}
	
	@Test
	public void testNotLoggedIn() throws OperationNotAllowedException {
		Employee test2 = company.createEmployee("HFBJ", "password", "Department1");
		
		company.employeeLogin("HAVD", "password");
		assertTrue(company.executiveIsLoggedIn());
		
		company.employeeLogin("BAMS", "password2");
		assertTrue(company.executiveIsLoggedIn());
		
		try {
			test2.assignEmployeeProject(test1.getID(), "Project01");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
	
	@Test
	public void testNotProjectLeader() throws OperationNotAllowedException {
		Employee test2 = company.createEmployee("KKKK", "password", "Department1");
		company.employeeLogin("KKKK", "password");
		try {
			test2.assignEmployeeProject(test1.getID(), "Project01");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
	
	@Test
	public void testAssignEmployeeActivity() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.createActivity(company.getProject("Project01"), "TestActivity", start, end);
		
		Activity a = p1.getActivity(p1.getID()+"-TestActivity");
		projectLeader.assignEmployeeActivity(test1.getID(), a.getName());
	}
	
	@Test
	public void testNotLoggedInActivity() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.createActivity(p1, "TestActivity", start, end);
		
		company.employeeLogout();
		
		try {
			Activity a = p1.getActivity(p1.getID()+"-TestActivity");
			projectLeader.assignEmployeeActivity(test1.getID(), a.getName());
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
	
	@Test
	public void testNotProjectLeaderActivity() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.createActivity(company.getProject("Project01"), "TestActivity", start, end);
		
		Employee test2 = company.createEmployee("KKKK", "password", "Department1");
		company.employeeLogin("KKKK", "password");
		try {
			Activity a = p1.getActivity(p1.getID()+"-TestActivity");
			test2.assignEmployeeActivity(test1.getID(), a.getName());
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
	
	@Test
	public void testEmployeeNotExist() throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 25);
		
		company.employeeLogin(projectLeader.getID(), "password");
		projectLeader.createActivity(company.getProject("Project01"), "TestActivity", start, end);
		
		Activity a = p1.getActivity(p1.getID()+"-TestActivity");
		
		try {
			projectLeader.assignEmployeeActivity("NOON", a.getName());
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee not found", e.getMessage());
			assertEquals("Get employee", e.getOperation());
		}
	}
	
	@Test
	public void testProjectNotExist() throws OperationNotAllowedException {
		try {
			projectLeader.assignEmployeeProject("HFBJ", "Wrong project");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Project could not be found", e.getMessage());
			assertEquals("Get project", e.getOperation());
		}
	}
}
