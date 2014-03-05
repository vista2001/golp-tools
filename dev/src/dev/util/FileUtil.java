/* �ļ�����       FileUtil.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
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
	 * ���ֽ�Ϊ��λ��ȡ�ļ��������ڶ��������ļ�����ͼƬ��������Ӱ����ļ���
	 */
	public static void readFileByBytes(String fileName) {

	}

	/**
	 * ���ַ�Ϊ��λ��ȡ�ļ��������ڶ��ı������ֵ����͵��ļ�
	 */
	public static void readFileByChars(String fileName) {

	}

	/**
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
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
	 * �����ȡ�ļ�����
	 */
	public static void readFileByRandomAccess(String fileName) {
	}

	/**
	 * ��ʾ�������л�ʣ���ֽ���
	 */

	/** д��һ���ļ�������ļ�����Ϊ����ֱ�ӿ�ʼд�룬����׷��д�� */
	public static void writeFileByLine(String path, String line) {
		File file = new File(path);
		BufferedWriter bw = null;
		try {
			if (file.length() == 0) {
				// ֱ��д��
				// DebugOut.println("��ʼֱ��д��");
				bw = new BufferedWriter(new FileWriter(file));
				bw.write(line);
				bw.newLine();
			} else {
				// ׷��д��
				// DebugOut.println("��ʼ׷��д��");
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

	/** ԭ�����ݱ���գ���д��һ�в��½�һ�У� */
	public static void WriteBlankFileByLine(String fileName, String line) {
		File file = new File(fileName);
		BufferedWriter bw = null;
		try {
			// ֱ��д��
			// DebugOut.println("��ʼֱ��д��");
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

	/** ׷��д�� */
	public static void addToWriteFileByLine(String fileName, String line) {
		File file = new File(fileName);
		BufferedWriter bw = null;
		// cleanFile(path);
		try {
			// ׷��д��
			// DebugOut.println("��ʼ׷��д��");
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

	/** ����ļ� */
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

	/** �ж��ļ��Ƿ�Ϊ�� */
	public static boolean isFileBlank(String path) {
		File file = new File(path);
		if (file.length() > 0)
			return false;
		return true;

	}

	/** ����һ���ļ� */
	public static boolean createFile(String path) {
		if (path.endsWith(File.separator)) {
			DevLogger.printDebugMsg("�ⲻ��һ���ļ�:" + path);
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
		// �𼶴���
		// ���������������������·������ô�������ļ��п��ܻ��ڵ�ǰ�������ĸ��̷���
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
