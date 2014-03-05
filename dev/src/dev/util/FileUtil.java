/* 文件名：       FileUtil.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。 
 */

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
	private static List<String> fileList = new ArrayList<String>();

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
				DevLogger.printDebugMsg(aline);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					DevLogger.printError(e);
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
				// DebugOut.println("开始直接写入");
				bw = new BufferedWriter(new FileWriter(file));
				bw.write(line);
				bw.newLine();
			} else {
				// 追加写入
				// DebugOut.println("开始追加写入");
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
					DevLogger.printError(e);
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
			// DebugOut.println("开始直接写入");
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
					DevLogger.printError(e);
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
			// DebugOut.println("开始追加写入");
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(line);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
					DevLogger.printError(e);
				}
			}
		}
	}

	/** 清空文件 */
	public static void cleanFile(String path) {
		File file = new File(path);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write("");
		} catch (IOException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				DevLogger.printError(e);
			}
		}
	}

	/** 判断文件是否为空 */
	public static boolean isFileBlank(String path) {
		File file = new File(path);
		if (file.length() > 0)
			return false;
		return true;

	}

	/** 创建一个文件 */
	public static boolean createFile(String path) {
		if (path.endsWith(File.separator)) {
			DevLogger.printDebugMsg("这不是一个文件:" + path);
			return false;
		}
		File file = new File(path);
		try {
			file.createNewFile();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		return false;
	}

	public static boolean createMultiDir(String absPath) {
		// 逐级创建
		// 这个方法如果给出的是相对路径，那么创建的文件夹可能会在当前工作区的根盘符下
		File dirs = new File(absPath);
		dirs.mkdirs();
		return dirs.exists();
	}

	public static List<String> iteratorDirs(String rootPath, List<String> last) {
		// List<String> last=last;

		File dir = new File(rootPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return last;
		} else {
			if (last == null) {
				last = new ArrayList<String>();
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					// DebugOut.println(files[i].getAbsolutePath());
					last.add(files[i].getAbsolutePath() + File.separator);
					iteratorDirs(files[i].getAbsolutePath(), last);
				} else {
					// String strFileName = files[i].getAbsolutePath();
					// DebugOut.println("---"+strFileName);
					last.add(files[i].getAbsolutePath());
				}
			}

		}
		return last;
	}

	public static void main(String[] args) {
		String fileName = "e:\\test\\2";
		// addToWriteFileByLine(fileName, "hahaha2");
		// WriteBlankFileByLine(fileName, "hahaha1");
		// createFile("f:/sss.txt");
		// DebugOut.println(File.separator);
		// DebugOut.println(File.separatorChar);
		// DebugOut.println(createMultiDir("f:/ggeeff/gef/gmf/g"));
		// DebugOut.println(createMultiDir(""));
		File f = new File(fileName);
		// try
		// {
		DevLogger.printDebugMsg(f.getParent());
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();DevLogger.printError(e);
		// }
		List<String> a = new ArrayList<String>();
		a = iteratorDirs(fileName, null);
		// for(String s : a)
		// {
		// DevLogger.printDebugMsg(s);
		// }
		// for (int i = 0; i < a.size(); i++)
		// {
		// DevLogger.printDebugMsg(a.get(i));
		// }
		String projectPath = "e:\\target";

		DevLogger.printDebugMsg("123456".substring(0));

		// Process p = null;
		//
		// try
		// {
		// p = Runtime.getRuntime().exec("cmd /c xcopy " + fileName + " " +
		// projectPath + "/e");
		// p.waitFor();
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();DevLogger.printError(e);
		// }
		// catch (InterruptedException e)
		// {
		// e.printStackTrace();DevLogger.printError(e);
		// }

		// for (String string : a)
		// {
		// String sourceString = string;
		// string = projectPath + string.substring(7);
		// DebugOut.println(string);
		// DebugOut.println(sourceString);
		//
		// // if (string.endsWith(File.separator))
		// // {
		// // FileUtil.createMultiDir(string);
		// // }
		// // else
		// // {
		// // FileUtil.createFile(string);
		// // if (!FileUtil.isFileBlank(sourceString))
		// // {
		// // List<String> sourceFile = FileUtil.readFileByLines(sourceString);
		// // for (String string2 : sourceFile)
		// // {
		// // FileUtil.writeFileByLine(string, string2);
		// // }
		// // }
		// // }
		//
		// }
		// DebugOut.println(a.toArray().toString());
	}

}
