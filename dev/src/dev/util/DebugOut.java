/* �ļ�����       DebugOut.java
 * ������           ���ļ���������DebugOut�������ṩ�˷���println�����System.out.println��
 *         ���ṩ�˿��Կ����Ƿ��ӡ����Ŀ��أ�����DEBUG������DEBUGΪtrueʱ��ӡ�����Ϊfalse
 *         �򲻴�ӡ��
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.11
 */

package dev.util;

/**
 * ��DebugOut���ṩ�˴��п��ƿ��صķ���println���������System.out.println������
 * @author rxy
 */
public class DebugOut
{
    private static final boolean DEBUG = true;
    
    public static void println(Object str)
    {
        if (DEBUG)
        {
            System.out.println(str);
        }
    }
}
