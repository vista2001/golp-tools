/* �ļ�����       Test.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
 */

package dev.model.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import dev.util.DebugOut;

public class Test {
	static Properties p = new Properties();
	public static void main(String[] args){
		String sysEncoding=System.getProperty("file.encoding"); 
		InputStream in = Test.class.getResourceAsStream("/dictionary_zh_CN.properties");
		try {
			InputStreamReader ir = new InputStreamReader(in, "utf-8");
			p.load(new InputStreamReader(in, sysEncoding));
			DebugOut.println(p.getProperty("name"));
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
