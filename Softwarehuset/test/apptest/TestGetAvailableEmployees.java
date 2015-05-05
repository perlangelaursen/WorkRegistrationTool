//Anna Ølgaard Nielsen - s144437

package apptest;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestGetAvailableEmployees {
	private Address add;
	private Company com;
	private Executive ex;
	private GregorianCalendar d1, d2, d3, d4, d5, d6, d7;
	private Employee em, em2, em3, em4;
	private Project p1;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		add = new Address("City", "Street", 1);
		com = new Company("SoftwareHuset", add);
		ex = new Executive("name","Department1", com, "password");
		com.setExecutive(ex);
		em = com.createEmployee("ANDS", "password", "Project Department");
		em2 = com.createEmployee("HENR", "password", "Project Department");
		em3 = com.createEmployee("KLIS", "password", "Project Department");
		em4 = com.createEmployee("HSNF", "password", "Project Department");
		
		d1 = new GregorianCalendar();
		d2 = new GregorianCalendar();
		d3 = new GregorianCalendar();
		d4 = new GregorianCalendar();
		d5 = new GregorianCalendar();
		d6 = new GregorianCalendar();
		d7 = new GregorianCalendar();
		
		com.executiveLogin(ex.getPassword());
		p1 = com.createProject("p1");

		com.employeeLogin(em.getID(),em.getPassword());
		
	}
	
	@Test
	public void testAvaiableEmployeesOnePerson() throws OperationNotAllowedException {
		com.executiveLogin("password");
		ex.assignProjectLeader("ANDS", p1.getID());
		assertEquals(p1.getProjectLeader(), em);
		
		d1.set(2000, 3, 1);
		d2.set(2000, 4, 1);
		d3.set(2000, 5, 1);
		d4.set(2000, 6, 1);
		
		em.createActivity(p1, "activity1", d1, d2);
		em.createActivity(p1, "activity2", d3, d4);
		em.assignEmployeeActivity(em2.getID(), p1.getID() + "-activity1");
		em.assignEmployeeActivity(em2.getID(), p1.getID() + "-activity2");
		
		d5.set(2000, 1, 1);
		d6.set(2000, 2, 1);
		
		assertTrue(com.getAvailableEmployees(d5, d6).contains(em2));
		
		
	}
	
	@Test
	public void testAvailableEmployeesThreePersons() throws OperationNotAllowedException {
		com.executiveLogin("password");
		ex.assignProjectLeader("ANDS", p1.getID());
		assertEquals(p1.getProjectLeader(), em);
		
		d1.set(2000, 3, 1);
		d2.set(2000, 4, 1);
		d3.set(2000, 5, 1);
		d4.set(2000, 6, 1);
		em.createActivity(p1, "activity1", d1, d2);
		em.createActivity(p1, "activity2", d3, d4);
		em.assignEmployeeActivity(em2.getID(), p1.getID() + "-activity1");
		em.assignEmployeeActivity(em3.getID(), p1.getID() + "-activity2");
		em.assignEmployeeActivity(em4.getID(), p1.getID() + "-activity2");
		
		d5.set(2000, 1, 1);
		d6.set(2000, 2, 1);
		
		assertTrue(com.getAvailableEmployees(d5, d6).contains(em2));
		assertTrue(com.getAvailableEmployees(d5, d7).contains(em2));
		assertTrue(com.getAvailableEmployees(d5, d6).contains(em4));
	}
	
	@Test
	public void testAvailableEmployeesOverlap() throws OperationNotAllowedException {
		com.executiveLogin("password");
		ex.assignProjectLeader("ANDS", p1.getID());
		assertEquals(p1.getProjectLeader(), em);
		
		d1.set(2000, 3, 1);
		d2.set(2000, 4, 1);
		d3.set(2000, 5, 1);
		d4.set(2000, 6, 1);
		
		em.createActivity(p1, "activity1", d1, d2);
		em.createActivity(p1, "activity2", d3, d4);
		
		em.assignEmployeeActivity(em2.getID(), p1.getID() + "-activity1");
		assertTrue(em2.getActivities().contains(p1.getActivity(p1.getID() + "-activity1")));
		em.assignEmployeeActivity(em2.getID(), p1.getID() + "-activity2");
		assertTrue(em2.getActivities().contains(p1.getActivity(p1.getID() + "-activity2")));
		
		d5.set(2000, 5, 5);
		d6.set(2000, 5, 20);
		
		assertFalse(com.getAvailableEmployees(d5, d6).contains(em2));
		
	}

}
