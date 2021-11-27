package com.simulation.shop.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ApexUtil {

	private static final String APEX_DATE_FORMAT = "yyyy,MM,dd,HH,mm,ss,SSS";

	public static String formatApexDate(Instant instant) {
		DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(APEX_DATE_FORMAT)
				.withZone(ZoneId.systemDefault());

		String formattedDate = DATE_TIME_FORMATTER.format(instant);

		String[] split = formattedDate.split(",");

		// trim prepended zeros
		for (int i = 0; i < split.length; i++) {
			split[i] = "" + Integer.parseInt(split[i]);
		}

		return String.join(",", split);

	}
	
	

}
