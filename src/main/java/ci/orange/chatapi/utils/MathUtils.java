
/*
 * Created on 2026-01-03 ( Time 10:00:17 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils;


/**
 * Math Utils
 * 
 * @author Geo
 *
 */
public class MathUtils {
	/**
	 * @param percentage
	 * @param total
	 * @return
	 */
	public static Double getValueByPercentage(Integer percentage, Double total, int valuePrecision) {
		return getValueByPercentage(Double.parseDouble(percentage.toString()), total, valuePrecision);
	}

	/**
	 * @param percentage
	 * @param total
	 * @return
	 */
	public static Double getValueByPercentage(Float percentage, Double total, int valuePrecision) {
		return getValueByPercentage(Double.parseDouble(percentage.toString()), total, valuePrecision);
	}

	/**
	 * @param percentage
	 * @param total
	 * @return
	 */
	public static Double getValueByPercentage(Double percentage, Double total, int valuePrecision) {
		Double value = 0d;
		Double pe    = ((double) percentage / 100);
		value = total * pe;
		value = round(value, valuePrecision);
		return value;
	}

	/**
	 * @param value
	 * @param total
	 * @return
	 */
	public static Double getPercentageByValue(Double value, Double total, int valuePrecision) {
		Double percentage = 0d;
		percentage = ((value * 100) / total);
		percentage = round(percentage, valuePrecision);
		return percentage;
	}

	/**
	 * @param value
	 * @param total
	 * @return
	 */
	public static float getPercentageByValue(float value, float total, int valuePrecision) {
		float percentage = 0L;
		percentage = ((value * 100) / total);
		percentage = round(percentage, valuePrecision);
		return percentage;
	}

	/**
	 * @param value
	 * @param total
	 * @return
	 */
	public static float getPercentageByValue(float value, float total) {
		float percentage = 0L;
		percentage = ((value * 100) / total);
		percentage = round(percentage);
		return percentage;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static double round(double value) {
		return round(value, 2);
	}

	public static float round(float value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (float) tmp / factor;
	}

	public static float round(float value) {
		return round(value, 2);
	}

	public static boolean isNegative(double d) {
		return Double.doubleToRawLongBits(d) < 0;
	}
	public static boolean isPositive(double d) {
		return Double.doubleToRawLongBits(d) > 0;
	}
}