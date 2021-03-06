//Anna Oelgaard Nielsen - s144437

package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestAssignProjectLeader {
	/**
	 * Tests the scenario where a project is assigned a project leader
	 * <ol>
	 * <li>Project leader is assigned
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */
	
	private Address address;
	private Company company;
	private Executive executive;
	private GregorianCalendar startDate, endDate;
	private Employee employee;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		address = new Address("City", "Street", 1);
		company = new Company("SoftwareHuset", address);
		executive = new Executive("name","Department1", company, "password");
		employee = company.createEmployee("ANDE", "password", "Project Department");
		startDate = new GregorianCalendar();
		endDate = new GregorianCalendar();
		startDate.add(Calendar.YEAR, 2015);
		endDate.add(Calendar.YEAR, 2015);
	}
	
	@Test
	public void testAssignProjectLeader() throws OperationNotAllowedException {
		//Check for executive is logged in
		assertFalse(company.executiveIsLoggedIn());
		company.executiveLogin("password");
		assertTrue(company.executiveIsLoggedIn());
		
		//The project ID is set to 150001 automatically (first project in year '15)
		company.createProject("Project 1", startDate, endDate);
		Project project = company.getProject("Project 1");
		//Check for createProject is implemented properly
		assertEquals(company.getProjects().size(), 1);
		
		project = company.getProject(150001);
		executive.assignProjectLeader("ANDE", project.getID());
		assertEquals(project.getProjectLeader(), employee);
	}

	@Test
	public void testNotLoggedIn() throws OperationNotAllowedException {
		//Create Project 1
		company.executiveLogin("password");
		Project project = company.createProject("Project 1", startDate, endDate); 
		
		//Catch Exception when executive is not logged in
		company.employeeLogout();
		try {
			executive.assignProjectLeader("ANDE", project.getID());
			fail("OperationNotAllowedOperationNotAllowedException OperationNotAllowedException should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Assign project leader is not allowed if not executive", e.getMessage());
			assertEquals("Assign project leader", e.getOperation());
		}
	}
	
	@Test
	public void testEmployeeNotFound() throws OperationNotAllowedException {
		//Executive create Project 1
		company.executiveLogin("password");
		company.createProject("Project 1", startDate, endDate);
		Project p1 = company.getProject("Project 1");
		
		//Check for createProject is implemented properly
		assertEquals(company.getProjects().size(),1);
		
		//Catch exception when wrong employee-ID is used
		try {
			executive.assignProjectLeader("ENDE", p1.getID());
			fail("OperationNotAllowedException expected");
		} catch(OperationNotAllowedException e) {
			assertEquals("Employee not found", e.getMessage());
			assertEquals("Get employee", e.getOperation());
		}
	}

	@Test
	public void testProjectNotFound() throws OperationNotAllowedException {
		//Executive login
		company.executiveLogin("password");
		
		//Catch exception when project does not exist
		try {
			executive.assignProjectLeader("ANDE", 150502);
			fail("OperationNotAllowedException expected");
		} catch(OperationNotAllowedException e) {
			assertEquals("Project could not be found", e.getMessage());
			assertEquals("Get project", e.getOperation());
		}
	}

	@Test
	public void testChangeProjectLeader() throws OperationNotAllowedException {
		//Executive create Project 1
		company.executiveLogin("password");
		company.createProject("Project 1", startDate, endDate);
		Project p1 = company.getProject("Project 1");
		
		//Check for createProject is implemented properly
		assertEquals(company.getProjects().size(),1);
		
		//Executive assign project leader
		executive.assignProjectLeader("ANDE", p1.getID());
		assertEquals(p1.getProjectLeader(), employee);
		
		Employee employee2 = company.createEmployee("KAND", "password", "Project 2 Department");
		
		//Executive change project leader
		executive.assignProjectLeader("KAND", p1.getID());
		assertEquals(p1.getProjectLeader(), employee2);
	}
}