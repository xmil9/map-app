package math;

import java.lang.Math;
import java.util.Objects;


public class MathUtil {
	
	///////////////
	
	// Floating point threshold for equality of two floating point values.
	public static final double defaultFpThres = 0.0000001;
	private static double globalFpThres = defaultFpThres;
	
	public static double globalFpThreshold() {
		return globalFpThres;
	}
	
	public static void setGlobalFpThreshold(double thres) {
		globalFpThres = thres;
		truncationFactor = calcTruncationFactor(thres);
	}
	
	///////////////

	// Inverse of fp threshold. Used to generate hash codes for fp values.
	private static double truncationFactor = calcTruncationFactor(globalFpThres);

	private static double calcTruncationFactor(double fpThreshold) {
		return 1.0 / fpThreshold;
	}
	
	// Returns a hash code for a given floating point value. Attempts to account
	// for the equality threshold of fp values. 
	public static long fpHashCode(double fp) {
		return Objects.hash((long) (fp * truncationFactor));
	}

	// Overload for given fp threshold.
	public static long fpHashCode(double fp, double thres) {
		double truncFactor = 1.0 / thres;
		return Objects.hash((long) (fp * truncFactor));
	}
	
	///////////////
	
	// Floating point comparisions.
	
	public static boolean fpEqual(double a, double b, double thres) {
		return Math.abs(a - b) <= thres; 
	}
	
	public static boolean fpEqual(double a, double b) {
		// Should call overload taking a threshold but since this is
		// a very hot function it is considerably faster to do the
		// calculation right here.
		return Math.abs(a - b) <= globalFpThres;
	}

	public static boolean fpLess(double a, double b, double thres) {
		// Check that a is smaller than b by at least the threshold value
		// because within the threashold they would still be considered
		// equal.
		return a - b < -thres; 
	}
	
	public static boolean fpLess(double a, double b) {
		// Should call overload taking a threshold but since this is
		// a very hot function it is considerably faster to do the
		// calculation right here.
		return a - b < -globalFpThres;
	}

	public static boolean fpLessEqual(double a, double b, double thres) {
		// Check that b is larger than a by at least the threshold value.
		// because within the threashold they would still be considered
		// equal.
		return a - b <= thres;
	}
	
	public static boolean fpLessEqual(double a, double b) {
		// Should call overload taking a threshold but since this is
		// a very hot function it is considerably faster to do the
		// calculation right here.
		return a - b <= globalFpThres;
	}

	public static boolean fpGreater(double a, double b, double thres) {
		// Check that a is larger than b by at least the threshold value
		// because within the threashold they would still be considered
		// equal.
		return a - b > thres;
	}
	
	public static boolean fpGreater(double a, double b) {
		// Should call overload taking a threshold but since this is
		// a very hot function it is considerably faster to do the
		// calculation right here.
		return a - b > globalFpThres;
	}

	public static boolean fpGreaterEqual(double a, double b, double thres) {
		// Check that b is smaller than a by at least the threshold value.
		// because within the threashold they would still be considered
		// equal.
		return a - b >= -thres;
	}
	
	public static boolean fpGreaterEqual(double a, double b) {
		// Should call overload taking a threshold but since this is
		// a very hot function it is considerably faster to do the
		// calculation right here.
		return a - b >= -globalFpThres;
	}

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
		if (fpGreater(val, 0))
			return Sign.POSITIVE;
		else if (fpLess(val, 0))
			return Sign.NEGATIVE;
		return Sign.NONE;
	}
}
