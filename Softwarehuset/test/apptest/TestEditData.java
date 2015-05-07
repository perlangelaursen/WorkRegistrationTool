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
		executive.assignProjectLeader("ABCD",p1.getID());
		
		company.employeeLogin(projectLeader.getID(), "password");
		p1.createActivity("Activity01", start, end, company.getProject("Project01"));
		projectLeader.assignEmployeeProject(projectLeader.getID(), p1.getName());
		projectLeader.assignEmployeeActivity(projectLeader.getID(),p1.getID()+"-Activity01");
		
		}
	@Test
	public void testEditData() throws OperationNotAllowedException {
		GregorianCalendar date = new GregorianCalendar();
		date.set(2016, Calendar.JANUARY, 25, 0, 0, 0);
		
		projectLeader.changeActivityStart(p1.getID()+"-Activity01", date);
		assertEquals(date.getTime(), p1.getActivity(p1.getID()+"-Activity01").getStart().getTime());
		
		date.set(2016, Calendar.JANUARY, 30, 0, 0, 0);
		projectLeader.changeActivityEnd(p1.getID()+"-Activity01", date);
		assertEquals(date.getTime(), p1.getActivity(p1.getID()+"-Activity01").getEnd().getTime());
		
		//Change name of project
		projectLeader.changeProjectName(p1, "new project name");
		assertEquals("new project name", p1.getName());
		//Change name of activity
		projectLeader.changeActivityName(p1.getActivity(p1.getID()+"-Activity01"), "new name");
		assertTrue(p1.getActivities().contains(p1.getActivity(p1.getID()+"-new name")));
	}

	@Test
	public void testEditDataWrongActivity() throws OperationNotAllowedException {
		GregorianCalendar date = new GregorianCalendar();
		date.set(2016, Calendar.JANUARY, 25, 0, 0, 0);
		
		//If there is no activity with the given name
		try {
			projectLeader.changeActivityStart(p1.getID()+"-Activity02", date);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Activity could not be found", e.getMessage());
			assertEquals("Edit activity", e.getOperation());
		}
	}
	
	@Test
	public void testSetData() throws OperationNotAllowedException {
		company.employeeLogin(projectLeader.getID(), "password");
		assertEquals(company.getLoggedInEmployee(), projectLeader);
		
		GregorianCalendar start = new GregorianCalendar(2016, Calendar.JANUARY, 3);
		projectLeader.changeProjectStart("Project01", start);
		assertEquals(company.getProject("Project01").getStart().get(Calendar.DAY_OF_MONTH), start.get(Calendar.DAY_OF_MONTH));
		assertEquals(company.getProject("Project01").getStart().get(Calendar.MONTH), start.get(Calendar.MONTH));
		assertEquals(company.getProject("Project01").getStart().get(Calendar.YEAR), start.get(Calendar.YEAR));
		
		GregorianCalendar end = new GregorianCalendar(2016, Calendar.APRIL, 3);
		projectLeader.changeProjectEnd("Project01", end);
		assertEquals(company.getProject("Project01").getEnd().get(Calendar.DAY_OF_MONTH), end.get(Calendar.DAY_OF_MONTH));
		assertEquals(company.getProject("Project01").getEnd().get(Calendar.MONTH), end.get(Calendar.MONTH));
		assertEquals(company.getProject("Project01").getEnd().get(Calendar.YEAR), end.get(Calendar.YEAR));
		
	}
}
