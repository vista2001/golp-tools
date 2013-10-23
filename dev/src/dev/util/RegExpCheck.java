package dev.util;

public class RegExpCheck
{
	private static final String IP_REGULAR = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	
	private static final String DLL_ID = "^[A-Za-z]+[0-9]*$";
	
	private static final String NUMBER = "^[1-9][0-9]*$";

	private static final String CHARACTER_NUM = "^[a-zA-Z0-9]$";

	public static boolean isNum(String s)
	{
		if (s != null && !s.isEmpty())
		{
			if (s.matches(NUMBER))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isIp(String s)
	{
		if (s != null && !s.isEmpty())
		{
			if (s.matches(IP_REGULAR))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDllId(String s)
	{
		if (s != null && !s.isEmpty())
		{
			if (s.matches(DLL_ID))
			{
				return true;
			}
		}
		return false;
	}
	
//	public static void main(String[] args)
//	{
//		System.out.println(isNum("1x"));
//	}
}
