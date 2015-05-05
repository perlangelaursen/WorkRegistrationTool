//Mathias Enggrob Boon - s144484
package apptest;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import softwarehuset.*;

public class TestEditData {
	private Company company;
	private Employee projectLeader;
	Project p1, p2;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		Address address = new Address("City", "Street", 1);
		company = new Company("Company", address);
		Executive executive = new Executive("Name", "Department1", company, "password");
		company.setExecutive(executive);

		// Log in as executive
		company.executiveLogin("password");
		
		//Set date
		
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		
		//Create projects
		p1 = company.createProject("Project01", start, end);
		p2 = company.createProject("Project02", start, end);
		
		//Create employee and assign as project leader
		projectLeader = company.createEmployee("ABCD", "password", "RandD");
		executive.assignProjectLeader(projectLeader,company.getSpecificProject("Project01"));
		
		company.employeeLogin(projectLeader.getID(), "password");
		company.getSpecificProject("Project01").createActivity("Activity01", start, end, company.getSpecificProject("Project01"));
		projectLeader.assignEmployeeProject(projectLeader, p1);
		projectLeader.assignEmployeeActivity(projectLeader.getID(),p1.getID()+"-Activity01");
		
		}
	@Test
	public void testEditData() throws OperationNotAllowedException {
		projectLeader.editActivityStart(p1.getID()+"-Activity01", 2016, 1, 25);
		projectLeader.editActivityEnd(p1.getID()+"-Activity01", 2016, 1, 30);
		
		GregorianCalendar date = new GregorianCalendar();
		date.set(2016, Calendar.JANUARY, 25, 0, 0, 0);
		assertEquals(date.getTime(), p1.getActivity(p1.getID()+"-Activity01").getStart().getTime());
		date.set(2016, Calendar.JANUARY, 30, 0, 0, 0);
		assertEquals(date.getTime(), p1.getActivity(p1.getID()+"-Activity01").getEnd().getTime());
	}

	@Test
	public void testEditDataWrongActivity() throws OperationNotAllowedException {
		//If there is no activity with the given name
		try {
			projectLeader.editActivityStart(p1.getID()+"-Activity02", 2016, 1, 25);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Activity does not exist", e.getMessage());
			assertEquals("Edit activity", e.getOperation());
		}
		
		//If there are no projects
		company.clearProjects();
		try {
			projectLeader.editActivityEnd(p1.getID()+"-Activity01", 2016, 1, 25);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Activity does not exist", e.getMessage());
			assertEquals("Edit activity", e.getOperation());
		}
	}
	
	@Test
	public void testEditDataInvalidDate() {
		try {
			projectLeader.editActivityStart(p1.getID()+"-Activity01", 2016, 1, 35);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
	}
	
	@Test
	public void testSetData() throws OperationNotAllowedException {
		company.employeeLogin(projectLeader.getID(), "password");
		assertEquals(company.getLoggedInEmployee(), projectLeader);
		
		GregorianCalendar start = new GregorianCalendar(2015, Calendar.JANUARY, 3);
		projectLeader.editProjectStart("Project01", 2015, 1, 3);
		assertEquals(company.getSpecificProject("Project01").getStart().get(Calendar.DAY_OF_MONTH), start.get(Calendar.DAY_OF_MONTH));
		assertEquals(company.getSpecificProject("Project01").getStart().get(Calendar.MONTH), start.get(Calendar.MONTH));
		assertEquals(company.getSpecificProject("Project01").getStart().get(Calendar.YEAR), start.get(Calendar.YEAR));
		
		GregorianCalendar end = new GregorianCalendar(2015, Calendar.APRIL, 3);
		projectLeader.editProjectEnd("Project01", 2015, 4, 3);
		assertEquals(company.getSpecificProject("Project01").getEnd().get(Calendar.DAY_OF_MONTH), end.get(Calendar.DAY_OF_MONTH));
		assertEquals(company.getSpecificProject("Project01").getEnd().get(Calendar.MONTH), end.get(Calendar.MONTH));
		assertEquals(company.getSpecificProject("Project01").getEnd().get(Calendar.YEAR), end.get(Calendar.YEAR));
		
	}
}
