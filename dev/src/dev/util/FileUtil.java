package dev.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	private static List<String> fileList=new ArrayList<String>();
	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 */
	public static void readFileByBytes(String fileName) {

	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static void readFileByChars(String fileName) {

	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static List<String> readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader br = null;
		String aline = "";
		List<String> lines = null;

		if (file.length() == 0) {
			return lines;
		}
		try {
			lines = new ArrayList<String>();
			br = new BufferedReader(new FileReader(file));
			while ((aline = br.readLine()) != null) {
				lines.add(aline);
				System.out.println(aline);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;

	}

	/**
	 * 随机读取文件内容
	 */
	public static void readFileByRandomAccess(String fileName) {
	}

	/**
	 * 显示输入流中还剩的字节数
	 */

	/** 写入一行文件，如果文件内容为空则直接开始写入，否则追加写入 */
	public static void writeFileByLine(String path, String line) {
		File file = new File(path);
		BufferedWriter bw = null;
		try {
			if (file.length() == 0) {
				// 直接写入
				//System.out.println("开始直接写入");
				bw = new BufferedWriter(new FileWriter(file));
				bw.write(line);
				bw.newLine();
			} else {
				// 追加写入
				//System.out.println("开始追加写入");
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/** 原有内容被清空，并写入一行并新建一行， */
	public static void WriteBlankFileByLine(String fileName, String line) {
		File file = new File(fileName);
		BufferedWriter bw = null;
		try {
			// 直接写入
			// System.out.println("开始直接写入");
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/** 追加写入 */
	public static void addToWriteFileByLine(String fileName, String line) {
		File file = new File(fileName);
		BufferedWriter bw = null;
		// cleanFile(path);
		try {
			// 追加写入
			// System.out.println("开始追加写入");
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** 清空文件 */
	public static void cleanFile(String path) {
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file);
			fw.write("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 判断文件是否为空 */
	public static boolean isFileBlank(String path) {
		File file = new File(path);
		if (file.length() > 0)
			return false;
		return true;

	}
	/**创建一个文件*/
	public static boolean createFile(String path){
		if(path.endsWith(File.separator)){
			System.out.println("这不是一个文件:"+path);
			return false;
		}
		File file=new File(path);
		try {
			file.createNewFile();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean createMultiDir(String absPath){
		//逐级创建
		//这个方法如果给出的是相对路径，那么创建的文件夹可能会在当前工作区的根盘符下
		File dirs=new File(absPath);
		dirs.mkdirs();
		return dirs.exists();
	}
	public static List<String> iteratorDirs(String rootPath,List<String> last){
		//List<String> last=last;
		
		  File dir = new File(rootPath); 
	        File[] files = dir.listFiles(); 
	        if (files == null){
	        	return last; 
	        }else{
			if (last == null) {
				last = new ArrayList<String>();
			}
	        for (int i = 0; i < files.length; i++) { 
	            if (files[i].isDirectory()) {
	            	//System.out.println(files[i].getAbsolutePath());
	            	last.add(files[i].getAbsolutePath()+File.separator);
	            	iteratorDirs(files[i].getAbsolutePath(),last); 
	            } else { 
	                //String strFileName = files[i].getAbsolutePath(); 
	                //System.out.println("---"+strFileName); 
	                last.add(files[i].getAbsolutePath());                    
	            } 
	        } 

		}
		return last;
	}
	public static void main(String[] args) {
		String fileName = "e:\\temp";
//		addToWriteFileByLine(fileName, "hahaha2");
//		WriteBlankFileByLine(fileName, "hahaha1");
		//createFile("f:/sss.txt");
//		System.out.println(File.separator);
//		System.out.println(File.separatorChar);
		//System.out.println(createMultiDir("f:/ggeeff/gef/gmf/g"));
		//System.out.println(createMultiDir(""));
		List<String> a=new ArrayList<String>();
		a=iteratorDirs(fileName,null);
		System.out.println(a.toArray().toString());
	}
	
}
