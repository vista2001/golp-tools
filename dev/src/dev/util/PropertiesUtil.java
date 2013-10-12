package dev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;


/**
 * ��дProperties�ļ�
 * 
 * @author LY
 */
public class PropertiesUtil {

	// ����ʵ����
	private PropertiesUtil() {

	}

	/**
	 * ��ȡProperties
	 * 
	 * @return ����һ��<code>Properties</code>����
	 * @param ����<code>Properties</code>�ļ����src��·��
	 */

	public static Properties readProperties(String path) {
		Properties properties = null;
		// InputStream in = Object.class.getResourceAsStream(path);
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (in != null) {
			try {
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	/** ����д�������ļ� */
	public static void rewriteProperties(String path,LinkedHashMap<String, String> map) {

		// д�ļ�
		FileUtil.cleanFile(path);
		// ����Ҫ���������
		for (String key : map.keySet()) {
			String line = key + "=" + map.get(key);
			FileUtil.writeFileByLine(path, line);
		}
	}

	/** ����properties�����뵽һ��List�У�
	 * �������Ľ����Ϊproperties����ʹ��map�洢��
	 * ����ÿ�α����Ľ��˳����ܲ�һ�� */
	public static List<?> getProperties(Properties properties) {
		List<?> l = new ArrayList<>();
		if (properties != null) {
			for (Object object : properties.entrySet()) {
				System.out.println(object + "="
						+ properties.getProperty(object.toString()));
			}
		}
		return l;
	}

	public static void main(String[] args) {
		// Properties properties =
		// readProperties("/dev/db/dbConnect.properties");
		// Enumeration<?> a=properties.propertyNames();
		// ��������Ԫ��
		// System.out.println(properties.getProperty("url"));
		// System.out.println(properties.getProperty("driver"));
/*		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("a", "a");
		map.put("a1", "a1");
		map.put("user", "golp1");
		map.put("password", "golppwd1");
		map.put("t", "t");
		System.out.println(map);
		String s = Object.class.getResource("/dev/db/db.properties").getPath().substring(1).replace('/', '\\');
		System.out.println(s);
		PropertiesUtil.rewriteProperties(s, map);*/
		
	}
}
