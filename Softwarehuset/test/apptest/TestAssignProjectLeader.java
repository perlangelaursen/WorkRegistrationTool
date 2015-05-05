//Anna Ølgaard Nielsen - s144437

package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestAssignProjectLeader {
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
		em = new Employee("Anders", "password", com, "Project Department");
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
		p1 = com.getSpecificProject("p1");
		assertEquals(com.getProjects().size(), 1);
		p1 = com.getSpecificProject(150001);
		assertEquals(com.getProjects().size(), 1);
		ex.assignProjectLeader(em, p1);
		assertEquals(p1.getProjectLeader(), em);
	}

	@Test
	public void testNotLoggedIn() throws OperationNotAllowedException {
		try {
			com.createProject("p1", d1, d2);
			fail("OperationNotAllowedOperationNotAllowedException OperationNotAllowedException should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Create project operation is not allowed if not executive.", e.getMessage());
			assertEquals("Create project", e.getOperation());
		}
		
		assertEquals(com.getProjects().size(), 0);
		
		try {
			ex.assignProjectLeader(em, p1);
			fail("OperationNotAllowedException exception");
		} catch(OperationNotAllowedException e) {
			assertEquals("Assign project leader is not allowed if not executive.", e.getMessage());
			assertEquals("Assign project leader", e.getOperation());
		}
	}
	
	@Test
	public void testEmployeeNotFound() throws OperationNotAllowedException {
		assertFalse(com.executiveIsLoggedIn());
		com.executiveLogin("password");
		assertTrue(com.executiveIsLoggedIn());
		
		com.createProject("p1", d1, d2);
		Project p1 = com.getSpecificProject("p1");
		assertEquals(com.getProjects().size(),1);
		
		Employee em2 = null;
		
		try {
			ex.assignProjectLeader(em2, p1);
			fail("OperationNotAllowedException expected");
		} catch(OperationNotAllowedException e) {
			assertEquals("Employee not found", e.getMessage());
			assertEquals("Employee not found", e.getOperation());
		}
	}

	@Test
	public void testProjectNotFound() throws OperationNotAllowedException {
		assertNull(com.getSpecificProject("p1"));
		assertNull(com.getSpecificProject(150502));
	}

	@Test
	public void testChangeProjectLeader() throws OperationNotAllowedException {
		assertFalse(com.executiveIsLoggedIn());
		com.executiveLogin("password");
		assertTrue(com.executiveIsLoggedIn());
		com.createProject("p1", d1, d2);
		Project p1 = com.getSpecificProject("p1");
		assertEquals(com.getProjects().size(),1);
		ex.assignProjectLeader(em, p1);
		assertEquals(p1.getProjectLeader(), em);
		Employee em2 = new Employee("Anders", "password", com, "Project 2 Department");
		ex.assignProjectLeader(em2, p1);
		assertEquals(p1.getProjectLeader(), em2);
	}
}