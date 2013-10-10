package dev.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ��ȡProperties�ļ�
 * 
 * @author LY
 */
public class ReadProperties {

	// ����ʵ����
	private ReadProperties() {

	}

	/**
	 * ��ȡProperties
	 * 
	 * @return ����һ��<code>Properties</code>����
	 * @param ����
	 *            <code>Properties</code>�ļ����src��·��
	 */

	public static Properties readProperties(String path) {
		Properties properties = null;
		InputStream in = Object.class.getResourceAsStream(path);
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

	public static void main(String[] args) {
		Properties properties = readProperties("/dev/db/dbConnect.properties");
		System.out.println(properties.getProperty("url"));
		System.out.println(properties.getProperty("driver"));
	}

}
