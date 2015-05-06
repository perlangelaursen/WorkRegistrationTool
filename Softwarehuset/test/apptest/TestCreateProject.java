//Van Anh Thi Trinh - s144449
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestCreateProject {
	private Address address;
	private Company company;
	private Executive ex;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		address = new Address("City", "Street", 1);
		company = new Company("Softwarehuset", address);
		ex = new Executive("Name", "Department", company, "password");
		// Log in as executive
		company.executiveLogin("password");
	}
	/**
	 * Tests the scenario where the executive successfully creates a project
	 * with start and end date and a project with only a name
	 * <ol>
	 * <li>The executive is logged in
	 * <li>The executive creates two projects
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */
	
	@Test
	public void testNewProjectSuccess() throws OperationNotAllowedException {
		// Create a project with start and end date
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		company.createProject("Project01", start, end);
		company.createProject("Project02");

		// Check number of projects
		List<Project> projects = (List<Project>) company.getProjects();
		int numberOfProjects = projects.size();
		assertEquals(2, numberOfProjects);
	}

	/**
	 * Tests the scenario where the executive is not logged in and tries to
	 * create projects
	 * <ol>
	 * <li>The executive is not logged in
	 * <li>The executive tries to create projects
	 * <li>An exception is thrown
	 * </ol>
	 */
	
	@Test
	public void testNewProjectLoggedOut() throws Exception {
		// Executive is not logged in
		company.employeeLogout();
		company.executiveLogin("wrongPassword");
		assertFalse(company.executiveIsLoggedIn());

		// Try to create project with given dates		
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2015, Calendar.JANUARY, 23);
		end.set(2015, Calendar.FEBRUARY, 23);
		
		try {
			company.createProject("Project01", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Create project operation is not allowed if not executive.",e.getMessage());
			assertEquals("Create project",e.getOperation());
		}
		
		//Try to create project without given dates
		try {
			company.createProject("Project02");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Create project operation is not allowed if not executive.",e.getMessage());
			assertEquals("Create project",e.getOperation());
		}
		
		// Check number of projects
		List<Project> projects = (List<Project>) company.getProjects();
		int numberOfProjects = projects.size();
		assertEquals(0, numberOfProjects);
	}
	
	/**
	 * Tests the scenario where the end date comes before the start date 
	 * create a project
	 * <ol>
	 * <li>The executive is logged in
	 * <li>The executive tries to create a project with wrong date order
	 * <li>An exception is thrown
	 * </ol>
	 */
	
	@Test
	public void testNewProjectDateOrder() throws Exception {
		// Create a project
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.FEBRUARY, 23);
		end.set(2016, Calendar.JANUARY, 23);

		// Try to create a project
		try {
			company.createProject("Project01", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("The end date is set before the start date",e.getMessage());
			assertEquals("Create project",e.getOperation());
		}

		// Check number of projects
		List<Project> projects = (List<Project>) company.getProjects();
		int numberOfProjects = projects.size();
		assertEquals(0, numberOfProjects);
	}
	
	/**
	 * Tests the scenario where the chosen dates have already been passed
	 * <ol>
	 * <li>The executive is logged in
	 * <li>The executive tries to create a project with dates in the past
	 * <li>An exception is thrown
	 * </ol>
	 */
	
	@Test
	public void testNewProjectPassedDates() throws Exception {
		// Create dates
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2015, Calendar.JANUARY, 23);
		end.set(2015, Calendar.FEBRUARY, 23);

		// Try to create a project
		try {
			company.createProject("Project01", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("The start date has already been passed",e.getMessage());
			assertEquals("Create project",e.getOperation());
		}

		// Check number of projects
		List<Project> projects = (List<Project>) company.getProjects();
		int numberOfProjects = projects.size();
		assertEquals(0, numberOfProjects);
	}
	
	/**
	 * Tests the scenario where an employee searches for a project by its name
	 * and several projects have that name (only the project's 5-digit ID is unique) 
	 * <ol>
	 * <li>The user searches for a project name that two projects have 
	 * <li>An exception is thrown
	 * </ol>
	 */
	@Test
	public void testCreateAndGetProjectsWithSameTitle() throws Exception {
		// Create dates
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		company.createProject("Project01", start, end);
		
		// Create project with the same name:
		company.createProject("Project01", start, end);

		//Try to get the project
		try {
			company.getProject("Project01");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Several projects have the requested title. Search by ID instead.",e.getMessage());
			assertEquals("Get project",e.getOperation());
		}
	}
}
