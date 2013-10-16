package dev.model.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Test {
	static Properties p = new Properties();
	public static void main(String[] args){
		String sysEncoding=System.getProperty("file.encoding"); 
		InputStream in = Test.class.getResourceAsStream("/dictionary_zh_CN.properties");
		try {
			InputStreamReader ir = new InputStreamReader(in, "utf-8");
			p.load(new InputStreamReader(in, sysEncoding));
			System.out.println(p.getProperty("name"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
