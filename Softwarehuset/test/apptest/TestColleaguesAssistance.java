//Per Lange Laursen - s144486
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestColleaguesAssistance {

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
		projectLeader = company.createEmployee("ABCS", "password", "RandD");
		
		executive.assignProjectLeader(projectLeader,company.getSpecificProject("Project01"));
	}
	
	@Test
	public void testAskColleague() throws OperationNotAllowedException {
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		projectLeader.assignEmployeeProject(asker, company.getSpecificProject("Project01"));
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getSpecificProject("Project01"), "A", start, end);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		company.employeeLogin(asker.getID(), "password");
		assertNull(p1.getActivity(p1.getID()+"-A").getAssistingEmployee(selected));
		
		asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
		assertEquals(1, p1.getActivity(p1.getID()+"-A").getAssistingEmployees().size());
		assertEquals(selected.getID(), p1.getActivity(p1.getID()+"-A").getAssistingEmployee(selected).getID());
	}
	
	@Test
	public void testEmployeeNotLoggedIn() throws OperationNotAllowedException {
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		projectLeader.assignEmployeeProject(asker, company.getSpecificProject("Project01"));
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getSpecificProject("Project01"), "A", start, end);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
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
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		projectLeader.assignEmployeeProject(asker, company.getSpecificProject("Project01"));
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getSpecificProject("Project01"), "A", start, end);
		projectLeader.assignEmployeeActivity(asker.getID(),p1.getID()+"-A");
		
		company.employeeLogin(asker.getID(), "password");
		asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
		assertEquals(1, p1.getActivity(p1.getID()+"-A").getAssistingEmployees().size());
		assertEquals(selected.getID(), p1.getActivity(p1.getID()+"-A").getAssistingEmployee(selected).getID());
		
		company.employeeLogin(asker.getID(), "password");
		asker.removeAssistingEmployee(selected, p1.getActivity(p1.getID()+"-A"));
		
		assertEquals(0, p1.getActivity(p1.getID()+"-A").getAssistingEmployees().size());
	}
	
	@Test
	public void testEmployeeNotLoggedInRemoveSpecificAssistingEmployee() throws OperationNotAllowedException {
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		
		projectLeader.assignEmployeeProject(asker, company.getSpecificProject("Project01"));
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getSpecificProject("Project01"), "A", start, end);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
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
		company.employeeLogin(projectLeader.getID(), "password");
		Employee asker = company.createEmployee("HABC", "password", "RandD");
		Employee selected = company.createEmployee("SJKO", "password", "RandD");
		Employee wrong = company.createEmployee("WRON", "password", "RandD");
		
		projectLeader.assignEmployeeProject(asker, company.getSpecificProject("Project01"));
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.JANUARY, 24);
		projectLeader.createActivity(company.getSpecificProject("Project01"), "A", start, end);
		projectLeader.assignEmployeeActivity(asker.getID(), p1.getID()+"-A");
		
		company.employeeLogin(asker.getID(), "password");
		asker.requestAssistance(selected, p1.getActivity(p1.getID()+"-A"));
		assertNull(p1.getActivity(p1.getID()+"-A").getAssistingEmployee(wrong));
	}
}
