package dev.util;

public class RegExpCheck {
	private static final String IP_REGULAR = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

	public static boolean isNum(String s) {
		return false;
	}

	public static boolean isIp(String s) {
		if (s != null && !s.isEmpty()) {
			if (s.matches(IP_REGULAR)) {
				return true;
			}
		}
		return false;
	}
}
