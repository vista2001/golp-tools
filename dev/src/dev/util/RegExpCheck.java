/* �ļ�����       RegExpCheck.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
 */

package dev.util;

public class RegExpCheck
{
	private static final String IP_REGULAR = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	/**
	 * �涨DLL��idֻ������ĸ��ͷ����߿��Լ�����
	 */
	private static final String DLL_ID = "^[A-Za-z]+[0-9]*$";
	/**
	 * ��Լ���涨�ַ���ֻ����������
	 */
	private static final String POSITIVE_INTEGER = "^[1-9][0-9]*$";
	
	/**
	 * ��Լ�������޶����׵�IDֻ��������
	 */
	private static final String TRADE_ID = "^[0-9]+$";
	
	/**
	 * ��Լ���涨�ַ����������κ�����
	 */
	private static final String INTEGER = "^0|-?[1-9][0-9]*$";
	
	private static final String RET_CODE_VALUE = "^\\d{5}$";

	private static final String CHARACTER_NUM = "^[a-zA-Z0-9]$";

	public static boolean isPositiveInteger(String s)
	{
		if (s != null && !s.isEmpty())
		{
			if (s.matches(POSITIVE_INTEGER))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInteger(String s)
    {
        if (s != null && !s.isEmpty())
        {
            if (s.matches(INTEGER))
            {
                return true;
            }
        }
        return false;
    }
	
	public static boolean isRetCodeValue(String s)
    {
        if (s != null && !s.isEmpty())
        {
            if (s.matches(RET_CODE_VALUE))
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
	
	public static boolean isTradeId(String s)
    {
        if (s != null && !s.isEmpty())
        {
            if (s.matches(TRADE_ID))
            {
                return true;
            }
        }
        return false;
    }
	
//	public static void main(String[] args)
//	{
//		DebugOut.println(isNum("1x"));
//	}
}
