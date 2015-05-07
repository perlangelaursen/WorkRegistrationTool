//Van Anh Thi Trinh - s144449
package apptest;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import softwarehuset.*;

public class TestWeekConverter {
	private Address address;
	private Company company;
	private Executive ex;

	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		address = new Address("City", "Street", 1);
		company = new Company("Softwarehuset", address);
	}

	/**
	 * Tests the week converter for the user interface
	 * <ol>
	 * <li>The user types in year and week
	 * <li>The time is converted to dates into the system
	 * </ol>
	 * 
	 * @throws OperationNotAllowedException
	 */

	@Test
	public void testConvertStartWeek() throws OperationNotAllowedException {
		// 2015, week 2 should return January 5th 2015
		GregorianCalendar date = company.convertStartToDate(2015, 2);
		assertEquals(date.get(Calendar.YEAR), 2015);
		assertEquals(date.get(Calendar.MONTH), Calendar.JANUARY);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 5);

		// 2015, week 1 should return December 29th 2014
		date = company.convertStartToDate(2015, 1);
		assertEquals(date.get(Calendar.YEAR), 2014);
		assertEquals(date.get(Calendar.MONTH), Calendar.DECEMBER);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 29);

		// 2015, week 53 should return December 28th 2015
		date = company.convertStartToDate(2015, 53);
		assertEquals(date.get(Calendar.YEAR), 2015);
		assertEquals(date.get(Calendar.MONTH), Calendar.DECEMBER);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 28);

		// 2018, week 1 should return January 1st 2018
		date = company.convertStartToDate(2018, 1);
		assertEquals(date.get(Calendar.YEAR), 2018);
		assertEquals(date.get(Calendar.MONTH), Calendar.JANUARY);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 1);
	}

	@Test
	public void testInvalidStartWeek() throws OperationNotAllowedException {
		// Week 53 is possible for 2015, week 54 is not
		company.convertStartToDate(2015, 53);
		try {
			company.convertStartToDate(2015, 54);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid week", e.getMessage());
			assertEquals("Choose week", e.getOperation());
		}

		// Week 52 is possible for 2016, week 53 is not
		company.convertStartToDate(2016, 52);
		try {
			company.convertStartToDate(2016, 53);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid week", e.getMessage());
			assertEquals("Choose week", e.getOperation());
		}

		// Test week 0
		try {
			company.convertStartToDate(2016, 0);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid week", e.getMessage());
			assertEquals("Choose week", e.getOperation());
		}
	}
	
	@Test
	public void testConvertEndWeek() throws OperationNotAllowedException {
		// 2015, week 2 should return January 11th 2015
		GregorianCalendar date = company.convertEndToDate(2015, 2);
		assertEquals(date.get(Calendar.YEAR), 2015);
		assertEquals(date.get(Calendar.MONTH), Calendar.JANUARY);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 11);

		// 2015, week 53 should return January 3rd 2016
		date = company.convertEndToDate(2015, 53);
		assertEquals(date.get(Calendar.YEAR), 2016);
		assertEquals(date.get(Calendar.MONTH), Calendar.JANUARY);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 3);

		// 2017, week 52 should return December 31st 2017
		date = company.convertEndToDate(2017, 52);
		assertEquals(date.get(Calendar.YEAR), 2017);
		assertEquals(date.get(Calendar.MONTH), Calendar.DECEMBER);
		assertEquals(date.get(Calendar.DAY_OF_MONTH), 31);

	}
}
