//Van Anh Thi Trinh - s144449
package apptest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import softwarehuset.*;

public class TestEmptySets {
	private Address address;
	private Company company;

	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		address = new Address("City", "Street", 1);
		company = new Company("Softwarehuset", address);
	}

	/**
	 * Tests the scenarios where there are no proejcts or employees
	 */

	@Test
	public void testNoEmployees() throws OperationNotAllowedException {
		// Login when there are no employees
		company.employeeLogin("ASFJ", "password");
		
		// Edit activity when there are no projects and therefore no activities
		Employee em = company.createEmployee("AFJJ", "password", "Department");
		GregorianCalendar newEnd = new GregorianCalendar(2016, 0, 25, 0, 0, 0);
		
		try {
			em.changeActivityEnd("150001-Activity01", newEnd);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Activity does not exist", e.getMessage());
			assertEquals("Edit activity", e.getOperation());
		}
	}
}
