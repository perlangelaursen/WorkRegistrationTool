//Per Lange Laursen - s144486
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestColleaguesAssistance {
	/**
	 * Tests the scenario where a employee requires the assistance from
	 * another employee
	 * <ol>
	 * <li>An employee is requesting support from another employee in a activity
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
		
		// Project creation and project leader assigned
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		p1 = company.createProject("Project01", start, end);
		projectLeader = company.createEmployee("ABCS", "password", "RandD");
		
		executive.assignProjectLeader("ABCS",p1.getID());
	}
	
	@Test
	public void testAskColleagueForAssistance() throws OperationNotAllowedException {
		// Test employees asker and selected
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		// Asker is assigned to project and activity
		projectLeader.assignEmployeeProject(asker.getID(), "Project01");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getProject("Project01"), "A", start, end,3);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		// Asker logs in
		company.employeeLogin(asker.getID(), "password");
		assertNull(p1.getActivity(p1.getID()+"-A").getAssistingEmployee(selected));
		
		// Asker requests assistance from selected
		asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
		assertEquals(1, p1.getActivity(p1.getID()+"-A").getAssistingEmployees().size());
		assertEquals(selected.getID(), p1.getActivity(p1.getID()+"-A").getAssistingEmployee(selected).getID());
	}
	
	@Test
	public void testEmployeeNotLoggedInAskColleague() throws OperationNotAllowedException {
		// Test employees asker and selected
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		// Asker is assigned to project and activity
		projectLeader.assignEmployeeProject(asker.getID(), "Project01");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getProject("Project01"), "A", start, end,3);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		// Asker forgets to log in
		try {
			asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("User not logged in",e.getMessage());
			assertEquals("Need For Assistance",e.getOperation());
		}
	}
	
	@Test
	public void testRemoveSpecificAssistingEmployee() throws OperationNotAllowedException {
		// Test employees asker and selected
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		// Asker is assigned to project and activity
		projectLeader.assignEmployeeProject(asker.getID(), "Project01");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getProject("Project01"), "A", start, end,3);
		projectLeader.assignEmployeeActivity(asker.getID(),p1.getID()+"-A");
		
		// Asker requests assistance from selected
		company.employeeLogin(asker.getID(), "password");
		asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
		assertEquals(1, p1.getActivity(p1.getID()+"-A").getAssistingEmployees().size());
		assertEquals(selected.getID(), p1.getActivity(p1.getID()+"-A").getAssistingEmployee(selected).getID());
		
		// Asker has got the need help and remove selected from activity
		company.employeeLogin(asker.getID(), "password");
		asker.removeAssistingEmployee(selected, p1.getActivity(p1.getID()+"-A"));
		
		assertEquals(0, p1.getActivity(p1.getID()+"-A").getAssistingEmployees().size());
	}
	
	@Test
	public void testEmployeeNotLoggedInRemoveSpecificAssistingEmployee() throws OperationNotAllowedException {
		// Test employees asker and selected
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		// Asker is assigned to project and activity
		projectLeader.assignEmployeeProject(asker.getID(), "Project01");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getProject("Project01"), "A", start, end,3);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		// Asker forgets to log in
		try {
			asker.removeAssistingEmployee(selected, p1.getActivity(p1.getID()+"-A"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("User not logged in",e.getMessage());
			assertEquals("Remove Assisting Employee from activity",e.getOperation());
		}
	}
	
	@Test
	public void testAssistanceNotFound() throws OperationNotAllowedException {
		// Test employees asker, wrong and selected
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		Employee wrong = company.createEmployee("WRON", "password", "RandD");
		
		// Asker is assigned to project and activity
		projectLeader.assignEmployeeProject(asker.getID(), "Project01");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getProject("Project01"), "A", start, end,3);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		// Testing if an employee is assisting in an activity
		company.employeeLogin(asker.getID(), "password");
		asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
		assertNull(p1.getActivity(p1.getID()+"-A").getAssistingEmployee(wrong));
	}
	
	@Test
	public void testInvalidActivity() throws OperationNotAllowedException {
		// Test employees asker and selected
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		// Asker is assigned to project and activity
		projectLeader.assignEmployeeProject(asker.getID(), "Project01");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getProject("Project01"), "A", start, end,3);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		// Asker tries to get help in a non-existent activity
		company.employeeLogin(asker.getID(), asker.getPassword());
		try {
			asker.requestAssistance(selected, null);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Activity not found",e.getMessage());
			assertEquals("Need for Assistance", e.getOperation());
		}
	}
}
