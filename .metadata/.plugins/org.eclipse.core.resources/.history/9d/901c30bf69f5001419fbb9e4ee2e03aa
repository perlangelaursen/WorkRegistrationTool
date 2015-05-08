//Anna �lgaard Nielsen - s144437

package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestAssignProjectLeader {
	/**
	 * Tests the scenario where a project is assigned a projectleader
	 * <ol>
	 * <li>Projectleader is assigned
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */
	
	private Address add;
	private Company com;
	private Executive ex;
	private GregorianCalendar d1, d2;
	private Employee em;
	private Project p1;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		add = new Address("City", "Street", 1);
		com = new Company("SoftwareHuset", add);
		ex = new Executive("name","Department1", com, "password");
		em = com.createEmployee("ANDE", "password", "Project Department");
		d1 = new GregorianCalendar();
		d2 = new GregorianCalendar();
		d1.add(Calendar.YEAR, 1);
		d2.add(Calendar.YEAR, 1);
	}
	
	@Test
	public void testAssignProjectLeader() throws OperationNotAllowedException {
		assertFalse(com.executiveIsLoggedIn());
		com.executiveLogin("password");
		assertTrue(com.executiveIsLoggedIn());
		com.createProject("p1", d1, d2);
		p1 = com.getProject("p1");
		assertEquals(com.getProjects().size(), 1);
		p1 = com.getProject(150001);
		assertEquals(com.getProjects().size(), 1);
		ex.assignProjectLeader("ANDE", p1.getID());
		assertEquals(p1.getProjectLeader(), em);
	}

	@Test
	public void testNotLoggedIn() throws OperationNotAllowedException {
		com.executiveLogin("password");
		Project p1 = com.createProject("p1", d1, d2);
		com.employeeLogout();
		try {
			ex.assignProjectLeader("ANDE", p1.getID());
			fail("OperationNotAllowedOperationNotAllowedException OperationNotAllowedException should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Assign project leader is not allowed if not executive", e.getMessage());
			assertEquals("Assign project leader", e.getOperation());
		}
	}
	
	@Test
	public void testEmployeeNotFound() throws OperationNotAllowedException {
		assertFalse(com.executiveIsLoggedIn());
		com.executiveLogin("password");
		assertTrue(com.executiveIsLoggedIn());
		
		com.createProject("p1", d1, d2);
		Project p1 = com.getProject("p1");
		assertEquals(com.getProjects().size(),1);
		
		try {
			ex.assignProjectLeader("ENDE", p1.getID());
			fail("OperationNotAllowedException expected");
		} catch(OperationNotAllowedException e) {
			assertEquals("Employee not found", e.getMessage());
			assertEquals("Get employee", e.getOperation());
		}
	}

	@Test
	public void testProjectNotFound() throws OperationNotAllowedException {
		com.executiveLogin("password");
		try {
			ex.assignProjectLeader("em", 150502);
			fail("OperationNotAllowedException expected");
		} catch(OperationNotAllowedException e) {
			assertEquals("Employee not found", e.getMessage());
			assertEquals("Get employee", e.getOperation());
		}
	}

	@Test
	public void testChangeProjectLeader() throws OperationNotAllowedException {
		assertFalse(com.executiveIsLoggedIn());
		com.executiveLogin("password");
		assertTrue(com.executiveIsLoggedIn());
		com.createProject("p1", d1, d2);
		Project p1 = com.getProject("p1");
		assertEquals(com.getProjects().size(),1);
		ex.assignProjectLeader("ANDE", p1.getID());
		assertEquals(p1.getProjectLeader(), em);
		Employee em2 = com.createEmployee("KAND", "password", "Project 2 Department");
		ex.assignProjectLeader("KAND", p1.getID());
		assertEquals(p1.getProjectLeader(), em2);
	}
}