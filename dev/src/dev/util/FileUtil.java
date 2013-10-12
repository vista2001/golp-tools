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
				//System.out.println("��ʼֱ��д��");
				bw = new BufferedWriter(new FileWriter(file));
				bw.write(line);
				bw.newLine();
			} else {
				// ׷��д��
				//System.out.println("��ʼ׷��д��");
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

	/** ԭ�����ݱ���գ���д��һ�в��½�һ�У� */
	public static void WriteBlankFileByLine(String fileName, String line) {
		File file = new File(fileName);
		BufferedWriter bw = null;
		try {
			// ֱ��д��
			// System.out.println("��ʼֱ��д��");
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

	/** ׷��д�� */
	public static void addToWriteFileByLine(String fileName, String line) {
		File file = new File(fileName);
		BufferedWriter bw = null;
		// cleanFile(path);
		try {
			// ׷��д��
			// System.out.println("��ʼ׷��д��");
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

	/** ����ļ� */
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

	/** �ж��ļ��Ƿ�Ϊ�� */
	public static boolean isFileBlank(String path) {
		File file = new File(path);
		if (file.length() > 0)
			return false;
		return true;

	}
	/**����һ���ļ�*/
	public static boolean createFile(String path){
		if(path.endsWith(File.separator)){
			System.out.println("�ⲻ��һ���ļ�:"+path);
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
		//�𼶴���
		//���������������������·������ô�������ļ��п��ܻ��ڵ�ǰ�������ĸ��̷���
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
