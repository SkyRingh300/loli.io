package io.loli.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileNameGenerator {
	private static String formatString = "yyyy_MM_dd_HH_mm_ss的屏幕截图";
	private static SimpleDateFormat format = new SimpleDateFormat(formatString);

	public static String generate() {
		return format.format(new Date());
	}

	public static String generate(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date());
	}

	public static String getFormatString() {
		return formatString;
	}
}
