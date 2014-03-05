package dev.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import ch.ethz.ssh2.log.Logger;
import dev.util.DevLogger;

public class RemoteUtil {
	private static final Logger logger = Logger.getLogger(RemoteUtil.class);

	public RemoteUtil(String host, String username, String password)

	{

		this.host = host;
		this.password = password;
		this.username = username;
		if (logger.isDebugEnabled()) {
			logger.debug("connecting to " + host + " with user " + username
					+ " and pwd " + password);
		}
	}

	public void connection() throws IOException {

		conn = new Connection(host);
		conn.connect();
		boolean isAuthenticated;
		isAuthenticated = conn.authenticateWithPassword(username, password);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
	}

	public void disConnetion() {
		conn.close();
	}

	public List<String> getRemoteDirs(String cmd) throws IOException {
		List<String> dirs = new ArrayList<String>();
		if (logger.isDebugEnabled()) {
			logger.debug("running SSH cmd [" + cmd + "]");
		}
		Session sess = conn.openSession();
		sess.execCommand(cmd);
		DevLogger
				.printDebugMsg("Here is some information about the remote host:");

		InputStream stdout = new StreamGobbler(sess.getStdout());

		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			DevLogger.printDebugMsg(line);
			dirs.add(line);
		}
		DevLogger.printDebugMsg("ExitCode: " + sess.getExitStatus());
		sess.close();

		return dirs;
	}

	private Connection conn;
	private String host;
	private String username;
	private String password;
}
