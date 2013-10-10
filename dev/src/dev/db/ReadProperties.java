package dev.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取Properties文件
 * 
 * @author LY
 */
public class ReadProperties {

	// 不许被实例化
	private ReadProperties() {

	}

	/**
	 * 读取Properties
	 * 
	 * @return 返回一个<code>Properties</code>对象
	 * @param 传入
	 *            <code>Properties</code>文件相对src的路径
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
