package com.active4j.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ArithUtil {
	private static final int DEF_DIV_SCALE = 10;

	private ArithUtil() {
	}

	// 加
	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.add(b2).doubleValue();

	}

	// 减
	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.subtract(b2).doubleValue();

	}

	// 乘以
	public static double mul(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.multiply(b2).doubleValue();

	}

	// 除以
	public static double div(double d1, double d2) {

		return div(d1, d2, DEF_DIV_SCALE);

	}

	public static double div(double d1, double d2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 计算成功率
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static String getSuccessRate(double a, double b, NumberFormat nt) {
		// 设置百分数保留两位小数
		nt.setMinimumFractionDigits(2);
		// 计算成功率
		String successRate = "0.00%";
		if (b != 0) {
			successRate = nt.format(a / b);
		}

		return successRate;
	}

	/**
	 * 字符串转2位小数
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToTwoDecimal(String s) {
		Double a = null;
		a = Double.parseDouble(s);

		// 四舍五入保留两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		String str = df.format(a);

		return str;

	}

	/**
	 * double转2位小数
	 * 
	 * @param s
	 * @return
	 */
	public static String doubleToTwoDecimal(double s) {

		// 四舍五入保留两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		String str = df.format(s);

		return str;

	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v     需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
	}
}
