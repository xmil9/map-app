package math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FpUtilTest {

	///////////////
	
	@Test
	public void fpEqual_ForValuesWithNoThreshold() {
		assertTrue(FpUtil.fpEqual(2.345000001, 2.345000001, 0.0));
	}

	@Test
	public void fpEqual_ForValuesWithinThreshold() {
		assertFalse(FpUtil.fpEqual(2.3451, 2.3452, 0.00001));
	}

	@Test
	public void fpEqual_ForValuesOutsideThreshold() {
		assertTrue(FpUtil.fpEqual(2.34500000001, 2.34500000002, 0.00001));
	}

	@Test
	public void fpLess_ForValuesWithNoThreshold() {
		assertTrue(FpUtil.fpLess(2.34500001, 2.34500002, 0.0));
	}

	@Test
	public void fpLess_ForlValuesWithinThreshold() {
		assertTrue(FpUtil.fpLess(2.34501, 2.34502, 0.000001));
	}

	@Test
	public void fpLess_ForValuesOutsideThreshold() {
		assertFalse(FpUtil.fpLess(2.34500000001, 2.34500000002, 0.00001));
	}

	@Test
	public void fpLessEqual_ForValuesWithNoThreshold() {
		assertTrue(FpUtil.fpLessEqual(2.34500001, 2.34500002, 0.0));
		assertTrue(FpUtil.fpLessEqual(2.34500002, 2.34500002, 0.0));
	}

	@Test
	public void fpLessEqual_ForlValuesWithinThreshold() {
		assertTrue(FpUtil.fpLessEqual(2.34501, 2.34502, 0.000001));
		assertTrue(FpUtil.fpLessEqual(2.34502, 2.34502, 0.000001));
	}

	@Test
	public void fpLessEqual_ForValuesOutsideThreshold() {
		assertTrue(FpUtil.fpLessEqual(2.34500000003, 2.34500000002, 0.00001));
	}

	@Test
	public void fpGreater_ForValuesWithNoThreshold() {
		assertTrue(FpUtil.fpGreater(2.34500002, 2.34500001, 0.0));
	}

	@Test
	public void fpGreater_ForlValuesWithinThreshold() {
		assertTrue(FpUtil.fpGreater(2.34502, 2.34501, 0.000001));
	}

	@Test
	public void fpGreater_ForValuesOutsideThreshold() {
		assertFalse(FpUtil.fpGreater(2.34500000002, 2.34500000001, 0.00001));
	}

	@Test
	public void fpGreaterEqual_ForValuesWithNoThreshold() {
		assertTrue(FpUtil.fpGreaterEqual(2.34500002, 2.34500001, 0.0));
		assertTrue(FpUtil.fpGreaterEqual(2.34500002, 2.34500002, 0.0));
	}

	@Test
	public void fpGreaterEqual_ForlValuesWithinThreshold() {
		assertTrue(FpUtil.fpGreaterEqual(2.34502, 2.34501, 0.000001));
		assertTrue(FpUtil.fpGreaterEqual(2.34502, 2.34502, 0.000001));
	}

	@Test
	public void fpGreaterEqual_ForValuesOutsideThreshold() {
		assertTrue(FpUtil.fpGreaterEqual(2.34500000002, 2.34500000003, 0.00001));
	}

	///////////////
	
	@Test
	public void fpHashCode_ForValuesWithLowerPrecisionThanThreshold() {
		long hashA = FpUtil.fpHashCode(364.47521111111, 0.0001);
		long hashB = FpUtil.fpHashCode(364.47531111111, 0.0001);
		assertTrue(hashA != hashB);
	}
	
	@Test
	public void fpHashCode_ForValuesWithHigherPrecisionThanThreshold() {
		long hashA = FpUtil.fpHashCode(364.47521111111, 0.0001);
		long hashB = FpUtil.fpHashCode(364.47522111111, 0.0001);
		assertTrue(hashA == hashB);
	}
	
	@Test
	public void fpHashCode_ForGlobalFpThreshold() {
		double prevThres = FpUtil.globalFpThreshold();
		FpUtil.setGlobalFpThreshold(FpUtil.defaultFpThres);
		
		long hashA = FpUtil.fpHashCode(364.11111118);
		long hashB = FpUtil.fpHashCode(364.11111119);
		assertTrue(hashA == hashB);

		long hashC = FpUtil.fpHashCode(364.1111118);
		long hashD = FpUtil.fpHashCode(364.1111119);
		assertTrue(hashC != hashD);

		FpUtil.setGlobalFpThreshold(prevThres);
	}
}
