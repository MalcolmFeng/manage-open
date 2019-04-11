package com.inspur.bigdata.manage.open.cloud.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.loushang.framework.exception.BusinessException;
import org.loushang.framework.i18n.MessageSourceExt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	private static Log logger = LogFactory.getLog(DateUtil.class);

	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date());
	}

	public static String getCurrentTime2() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String second2date(long second) {
		return new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(second * 1000);
	}

	public static String second2date(String secondStr) {
		long second = Long.valueOf(secondStr);
		return new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(second * 1000);
	}

	/**
	 * 获取时间差（秒）
	 * 
	 * @param start[起始时间，格式：yyyyMMdd
	 *            HH:mm:ss]
	 * @param end[结束时间，格式：yyyyMMdd
	 *            HH:mm:ss]
	 * @return
	 */
	public static long intervalTime(String start, String end) {
		if (!check(start)) {
			throw new BusinessException(
					MessageSourceExt.getLocaleMessage("framework.util.024", "起始时间不符合日期规范，日期规范格式：格式：")
							+ "yyyyMMdd HH:mm:ss");
		}
		if (!check(end)) {
			throw new BusinessException(
					MessageSourceExt.getLocaleMessage("framework.util.025", "结束时间不符合日期规范，日期规范格式：格式：")
							+ "yyyyMMdd HH:mm:ss");
		}
		try {
			Date start1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(start);
			Date end1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(end);
			long diff = end1.getTime() - start1.getTime();
			if (diff <= 0) {
				return 0;
			}
			return diff / 1000;
		} catch (Exception e) {
			logger.error(MessageSourceExt.getLocaleMessage("framework.util.026", "计算时间差失败，异常信息："), e);
		}
		return 0;
	}

	/**
	 * 获取时间差（秒）
	 * 
	 * @param start[起始时间，格式：yyyyMMdd
	 *            HH:mm:ss]
	 * @param end[结束时间，格式：yyyyMMdd
	 *            HH:mm:ss]
	 * @return
	 */
	public static long intervalTime(String end) {
		if (!check(end)) {
			throw new BusinessException(
					MessageSourceExt.getLocaleMessage("framework.util.025", "结束时间不符合日期规范，日期规范格式：格式：")
							+ "yyyyMMdd HH:mm:ss");
		}
		try {
			Date start1 = new Date();
			Date end1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(end);
			long diff = end1.getTime() - start1.getTime();
			if (diff <= 0) {
				return 0;
			}
			return diff / 1000;
		} catch (Exception e) {
			logger.error(MessageSourceExt.getLocaleMessage("framework.util.026", "计算时间差失败，异常信息："), e);
		}
		return 0;
	}

	/**
	 * 判断当前时间是否在指定时间段内 判断一个时间是否在一个时间段内,不在时间段内，则获取时间差
	 * 
	 * @param minDate
	 *            [时间格式：HH:mm]
	 * @param maxDate
	 *            [时间格式：HH:mm]
	 * @return
	 */
	public static boolean judgeTime(String minDate, String maxDate) {
		return judgeTime(getCurrentTime(), minDate, maxDate);
	}

	/**
	 * 判断指定时间是否在指定时间段内 判断一个时间是否在一个时间段内,不在时间段内，则获取时间差
	 * 
	 * @param date
	 *            [日期格式：yyyyMMdd HH:mm:ss]
	 * @param minDate
	 *            [时间格式：HH:mm]
	 * @param maxDate
	 *            [时间格式：HH:mm]
	 * @return
	 */
	public static boolean judgeTime(String date, String minDate, String maxDate) {
		if (!check(date)) {
			throw new BusinessException(MessageSourceExt.getLocaleMessage("framework.util.027", "不符合日期规范，日期规范格式：格式：")
					+ "yyyyMMdd HH:mm:ss");
		}
		long realTime = 0;
		long minTime = 0;
		long maxTime = 0;
		try {
			if (date.length() == 17) {
				minDate = date.substring(0, 9) + minDate + ":00";
				maxDate = date.substring(0, 9) + maxDate + ":00";
				realTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(date).getTime();
				minTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(minDate).getTime();
				maxTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(maxDate).getTime();
			}
			if (date.length() == 19) {
				minDate = date.substring(0, 11) + minDate + ":00";
				maxDate = date.substring(0, 11) + maxDate + ":00";
				realTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
				minTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(minDate).getTime();
				maxTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(maxDate).getTime();
			}
		} catch (Exception e) {
			logger.error(MessageSourceExt.getLocaleMessage("framework.util.026", "时间格式转换失败，异常信息："), e);
		}
		if (realTime >= minTime && realTime <= maxTime) {
			return true;
		}

		return false;
	}

	/**
	 * 日期格式判断
	 * 
	 * @param timeStr
	 * @return
	 */
	private static boolean check(String timeStr) {
		String rule = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(timeStr);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

}
