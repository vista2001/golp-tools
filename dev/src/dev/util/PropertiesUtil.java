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
 * 读写Properties文件
 * 
 * @author LY
 */
public class PropertiesUtil {

	// 不许被实例化
	private PropertiesUtil() {

	}

	/**
	 * 读取Properties
	 * 
	 * @return 返回一个<code>Properties</code>对象
	 * @param 传入<code>Properties</code>文件相对src的路径
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

	/** 覆盖写入属性文件 */
	public static void rewriteProperties(String path,LinkedHashMap<String, String> map) {

		// 写文件
		FileUtil.cleanFile(path);
		// 遍历要输入的内容
		for (String key : map.keySet()) {
			String line = key + "=" + map.get(key);
			FileUtil.writeFileByLine(path, line);
		}
	}

	/** 遍历properties，放入到一个List中，
	 * 但遍历的结果因为properties本身使用map存储，
	 * 所以每次遍历的结果顺序可能不一致 */
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
		// 遍历所有元素
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
