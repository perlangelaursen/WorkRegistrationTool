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
		assertTrue(company.executiveIsLoggedIn());
		
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
		assertTrue(company.executiveIsLoggedIn());
		
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
		assertTrue(company.executiveIsLoggedIn());
		
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
	 * Tests the scenario where the executive tries to create a project with an already used project name
	 * <ol>
	 * <li>The executive is logged in
	 * <li>The employee tries to create a project with a name that already exists 
	 * <li>An exception is thrown
	 * </ol>
	 */
	@Test
	public void testCreateProjectsWithSameTitle() throws Exception {
		assertTrue(company.executiveIsLoggedIn());
		
		// Create dates
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		company.createProject("Project01", start, end);
		
		//Try to create another "Project01"
		try {
			company.createProject("Project01", start, end);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Name already exists",e.getMessage());
			assertEquals("Create project",e.getOperation());
		}
	}
	
	/**
	 * Tests the scenario where the executive tries to get a project by ID that does not exist
	 * <ol>
	 * <li>The executive is logged in
	 * <li>The employee tries to get a project with non-existing ID 
	 * <li>An exception is thrown
	 * </ol>
	 */
	@Test
	public void testGetNonExistingProject() throws Exception {
		assertTrue(company.executiveIsLoggedIn());
		
		// Create a project that will get the ID 150001
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		company.createProject("Project01", start, end);
		company.getProject(150001); //possible
		
		//Try to get a non-existing project
		try {
			company.getProject(150002);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Project could not be found",e.getMessage());
			assertEquals("Get project",e.getOperation());
		}
	}
}
