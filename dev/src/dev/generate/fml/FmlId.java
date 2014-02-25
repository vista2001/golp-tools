package dev.generate.fml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import dev.db.pojo.TDataItem;
import dev.db.service.CommonDialogServiceImpl;

public class FmlId {
	private static String path;
	public static String separator = File.separator;

	public static long getFmlId(String name, int id, String type)
			throws IOException, InterruptedException {
		getPath();
		writeFile(name, id, type);
		runCommand();
		return getFile();
	}

	public static void generateFml(String prjId) throws SQLException,
			IOException, InterruptedException {
		getPath();
		System.out.println(path);
		CommonDialogServiceImpl impl = new CommonDialogServiceImpl();
		List<TDataItem> list = impl.dataItemDialogQuery(prjId);
		File outFile = new File(path + separator + prjId + separator + "FML"
				+ separator + "fmlid.fml");
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "UTF-8"));
		for (int i = 0; i < list.size(); i++) {
			// out.write((list.get(i).getDataName() + "	"
			// + list.get(i).getDataItemId() + "	"
			// + list.get(i).getDataType2().substring(2) + "	-	"
			// + list.get(i).getDataDesc()+"\r\n"));
			out.append((list.get(i).getDataName() + "	"
					+ list.get(i).getDataItemId() + "	"
					+ list.get(i).getDataType2().substring(2).toLowerCase()
					+ "	-	" + list.get(i).getDataDesc() + "\r\n"));
		}
		out.flush();
		out.close();
		Process child = Runtime.getRuntime().exec(
				"cmd.exe /c mkfldhdr32.exe -d " + path + separator + prjId
						+ separator + "FML" + " " + path + separator + prjId
						+ separator + "FML" + separator + "fmlid.fml", null,
				new File(path + separator + ".." + separator + "FML"));
		child.waitFor();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(child.getInputStream()));
		String ls;
		while ((ls = bufferedReader.readLine()) != null)
			System.out.println(ls);
	}

	private static void writeFile(String name, int id, String type)
			throws IOException {
		File outFile = new File(path + separator + ".." + separator + "FML"
				+ separator + "fmlid.fml");
		Writer out = null;
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outFile), "UTF-8"));
		out.write(name + "	" + id + "	" + type + "	-	-");
		out.flush();
		out.close();
	}

	private static void runCommand() throws IOException, InterruptedException {
		File workDir = new File(path + separator + ".." + separator + "FML");
		Process child = Runtime.getRuntime().exec(
				"cmd.exe /c mkfldhdr32.exe " + path + separator + ".."
						+ separator + "FML" + separator + "fmlid.fml", null,
				workDir);
		child.waitFor();
	}

	private static long getFile() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File(path
				+ separator + ".." + separator + "FML" + separator
				+ "fmlid.fml.h")));
		String line;
		String fmlid = null;
		while ((line = in.readLine()) != null) {
			if (line.contains("#define")) {
				fmlid = line.substring(line.indexOf(")") + 1,
						line.lastIndexOf(")"));
				break;
			}
		}

		return Long.valueOf(fmlid);
	}

	public static String getPath() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		path = root.getRawLocationURI().toString().substring(6);
		String pathClone = "";
		String se = "";
		for (String autom : path.split("/"))
			pathClone += autom + separator;
		path = pathClone;
		return pathClone;
	}
}
