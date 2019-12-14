package math;

import java.lang.Math;

// Mathematical utility functions. 
public class MathUtil {
	
	///////////////
	
	// Ranges.
	
	// Limits a value to given min and max values. 
	public static double clampToRange(double val, double min, double max) {
		// Normalize.
		if (min > max) {
			double tmp = min;
			min = max;
			max = tmp;
		}

		// Clamp.
		if (val < min)
			val = min;
		if (val > max)
			val = max;

		assert min <= val && val <= max;
		return val;
	}

	// Shifts a given value into the range of given min and max values. The
	// value will end up in the same position within the range.
	// Example:
	//   780 shift into [0, 360] => 60
	public static double shiftIntoRange(double val, double min, double max) {
		// Normalize.
		if (min > max) {
			double tmp = min;
			min = max;
			max = tmp;
		}
		
		// Guard against div by zero.
		double len = max - min;
		if (len == 0.0)
			return val;
		
		// Shift.
		if (val < min)
			val = max - (min - val) % len;
		if (val > max)
			val = min + (val - min) % len;
		
		assert min <= val && val <= max;
		return val;
	}
	
	///////////////
	
	// Angle unit conversions.
	
	public static double degreesFromRadians(double rad) {
		return rad * 180.0 / Math.PI;
	}
	
	public static double radiansFromDegrees(double deg) {
		return deg * Math.PI / 180.0;
	}

	///////////////
	
	public enum Sign {
		POSITIVE,
		NEGATIVE,
		NONE
	}
	
	public static Sign sign(double val) {
		if (FpUtil.fpGreater(val, 0))
			return Sign.POSITIVE;
		else if (FpUtil.fpLess(val, 0))
			return Sign.NEGATIVE;
		return Sign.NONE;
	}
}
