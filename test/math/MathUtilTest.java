package math;

import static org.junit.Assert.*;
import org.junit.Test;


public class MathUtilTest {

	///////////////
	
	@Test
	public void fpEqual_ForValuesWithNoThreshold() {
		assertTrue(MathUtil.fpEqual(2.345000001, 2.345000001, 0.0));
	}

	@Test
	public void fpEqual_ForValuesWithinThreshold() {
		assertFalse(MathUtil.fpEqual(2.3451, 2.3452, 0.00001));
	}

	@Test
	public void fpEqual_ForValuesOutsideThreshold() {
		assertTrue(MathUtil.fpEqual(2.34500000001, 2.34500000002, 0.00001));
	}

	@Test
	public void fpLess_ForValuesWithNoThreshold() {
		assertTrue(MathUtil.fpLess(2.34500001, 2.34500002, 0.0));
	}

	@Test
	public void fpLess_ForlValuesWithinThreshold() {
		assertTrue(MathUtil.fpLess(2.34501, 2.34502, 0.000001));
	}

	@Test
	public void fpLess_ForValuesOutsideThreshold() {
		assertFalse(MathUtil.fpLess(2.34500000001, 2.34500000002, 0.00001));
	}

	@Test
	public void fpLessEqual_ForValuesWithNoThreshold() {
		assertTrue(MathUtil.fpLessEqual(2.34500001, 2.34500002, 0.0));
		assertTrue(MathUtil.fpLessEqual(2.34500002, 2.34500002, 0.0));
	}

	@Test
	public void fpLessEqual_ForlValuesWithinThreshold() {
		assertTrue(MathUtil.fpLessEqual(2.34501, 2.34502, 0.000001));
		assertTrue(MathUtil.fpLessEqual(2.34502, 2.34502, 0.000001));
	}

	@Test
	public void fpLessEqual_ForValuesOutsideThreshold() {
		assertTrue(MathUtil.fpLessEqual(2.34500000003, 2.34500000002, 0.00001));
	}

	@Test
	public void fpGreater_ForValuesWithNoThreshold() {
		assertTrue(MathUtil.fpGreater(2.34500002, 2.34500001, 0.0));
	}

	@Test
	public void fpGreater_ForlValuesWithinThreshold() {
		assertTrue(MathUtil.fpGreater(2.34502, 2.34501, 0.000001));
	}

	@Test
	public void fpGreater_ForValuesOutsideThreshold() {
		assertFalse(MathUtil.fpGreater(2.34500000002, 2.34500000001, 0.00001));
	}

	@Test
	public void fpGreaterEqual_ForValuesWithNoThreshold() {
		assertTrue(MathUtil.fpGreaterEqual(2.34500002, 2.34500001, 0.0));
		assertTrue(MathUtil.fpGreaterEqual(2.34500002, 2.34500002, 0.0));
	}

	@Test
	public void fpGreaterEqual_ForlValuesWithinThreshold() {
		assertTrue(MathUtil.fpGreaterEqual(2.34502, 2.34501, 0.000001));
		assertTrue(MathUtil.fpGreaterEqual(2.34502, 2.34502, 0.000001));
	}

	@Test
	public void fpGreaterEqual_ForValuesOutsideThreshold() {
		assertTrue(MathUtil.fpGreaterEqual(2.34500000002, 2.34500000003, 0.00001));
	}

	///////////////
	
	@Test
	public void clampToRange_ForValueInRange() {
		assertEquals(2.0, MathUtil.clampToRange(2.0, 1.0, 4.0), 0.0);
		assertEquals(-2.0, MathUtil.clampToRange(-2.0, -4.0, -1.0), 0.0);
	}
	
	@Test
	public void clampToRange_ForValueLessThanMin() {
		assertEquals(2.0, MathUtil.clampToRange(1.0, 2.0, 4.0), 0.0);
		assertEquals(-2.0, MathUtil.clampToRange(-3.0, -2.0, 4.0), 0.0);
	}
	
	@Test
	public void clampToRange_ForValueGreaterThanMax() {
		assertEquals(4.0, MathUtil.clampToRange(5.0, 2.0, 4.0), 0.0);
		assertEquals(-2.0, MathUtil.clampToRange(0.0, -5.0, -2.0), 0.0);
	}
	
	@Test
	public void clampToRange_ForValueEqualToMin() {
		assertEquals(1.0, MathUtil.clampToRange(1.0, 1.0, 4.0), 0.0);
		assertEquals(-4.0, MathUtil.clampToRange(-4.0, -4.0, -1.0), 0.0);
	}
	
	@Test
	public void clampToRange_ForValueEqualToMax() {
		assertEquals(4.0, MathUtil.clampToRange(4.0, 1.0, 4.0), 0.0);
		assertEquals(-1.0, MathUtil.clampToRange(-1.0, -4.0, -1.0), 0.0);
	}
	
	@Test
	public void clampToRange_ForDenormalizedRange() {
		assertEquals(1.0, MathUtil.clampToRange(0.0, 4.0, 1.0), 0.0);
		assertEquals(-1.0, MathUtil.clampToRange(1.0, -1.0, -4.0), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForValueInRange() {
		assertEquals(2, MathUtil.shiftIntoRange(2, 0, 10), 0.0);
		assertEquals(88, MathUtil.shiftIntoRange(88, 85, 100), 0.0);
		assertEquals(-7, MathUtil.shiftIntoRange(-7, -15, -5), 0.0);
		assertEquals(70, MathUtil.shiftIntoRange(70, 0, 360), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForValueLessThanMin() {
		assertEquals(8, MathUtil.shiftIntoRange(-32, 0, 10), 0.0);
		assertEquals(94, MathUtil.shiftIntoRange(19, 85, 100), 0.0);
		assertEquals(-7, MathUtil.shiftIntoRange(-37, -15, -5), 0.0);
		assertEquals(160, MathUtil.shiftIntoRange(-560, 0, 360), 0.0);
		assertEquals(360, MathUtil.shiftIntoRange(-720, 0, 360), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForValueGreaterThanMax() {
		assertEquals(6, MathUtil.shiftIntoRange(56, 0, 10), 0.0);
		assertEquals(89, MathUtil.shiftIntoRange(119, 85, 100), 0.0);
		assertEquals(-8, MathUtil.shiftIntoRange(22, -15, -5), 0.0);
		assertEquals(60, MathUtil.shiftIntoRange(780, 0, 360), 0.0);
		assertEquals(0, MathUtil.shiftIntoRange(720, 0, 360), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForValueEqualToMin() {
		assertEquals(0, MathUtil.shiftIntoRange(0, 0, 10), 0.0);
		assertEquals(85, MathUtil.shiftIntoRange(85, 85, 100), 0.0);
		assertEquals(-15, MathUtil.shiftIntoRange(-15, -15, -5), 0.0);
		assertEquals(0, MathUtil.shiftIntoRange(0, 0, 360), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForValueEqualToMax() {
		assertEquals(10, MathUtil.shiftIntoRange(10, 0, 10), 0.0);
		assertEquals(100, MathUtil.shiftIntoRange(100, 85, 100), 0.0);
		assertEquals(-5, MathUtil.shiftIntoRange(-5, -15, -5), 0.0);
		assertEquals(360, MathUtil.shiftIntoRange(360, 0, 360), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForDenormalizedRange() {
		assertEquals(8, MathUtil.shiftIntoRange(-22, 10, 0), 0.0);
		assertEquals(86, MathUtil.shiftIntoRange(101, 100, 85), 0.0);
		assertEquals(-7, MathUtil.shiftIntoRange(-7, -5, -15), 0.0);
		assertEquals(359, MathUtil.shiftIntoRange(-1, 360, 0), 0.0);
	}
	
	@Test
	public void shiftIntoRange_ForEmptyRange() {
		assertEquals(5, MathUtil.shiftIntoRange(5, 10, 10), 0.0);
	}

	///////////////
	
	@Test
	public void degreesFromRadians() {
		assertEquals(0.0, MathUtil.degreesFromRadians(0.0), 0.0);
		assertEquals(90.0, MathUtil.degreesFromRadians(0.5 * Math.PI), 0.000001);
		assertEquals(180.0, MathUtil.degreesFromRadians(Math.PI), 0.000001);
		assertEquals(270.0, MathUtil.degreesFromRadians(1.5 * Math.PI), 0.000001);
		assertEquals(360.0, MathUtil.degreesFromRadians(2.0 * Math.PI), 0.000001);
		assertEquals(57.295779513, MathUtil.degreesFromRadians(1.0), 0.000001);
	}
	
	@Test
	public void radiansFromDegrees() {
		assertEquals(0.0, MathUtil.radiansFromDegrees(0.0), 0.0);
		assertEquals(0.5 * Math.PI, MathUtil.radiansFromDegrees(90), 0.000001);
		assertEquals(Math.PI, MathUtil.radiansFromDegrees(180), 0.000001);
		assertEquals(1.5 * Math.PI, MathUtil.radiansFromDegrees(270), 0.000001);
		assertEquals(2.0 * Math.PI, MathUtil.radiansFromDegrees(360), 0.000001);
		assertEquals(1.0, MathUtil.radiansFromDegrees(57.295779513), 0.000001);
	}
}
