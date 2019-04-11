package com.inspur.bigdata.manage.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SSHUtil {
	private static Logger logger = LoggerFactory.getLogger(SSHUtil.class);

	/**
	 * 远程执行命令
	 * 
	 * @param host
	 *            远程服务器IP
	 * @param userName
	 *            远程服务器用户名
	 * @param password
	 *            远程服务器密码
	 * @param cmd
	 *            命令
	 * @return
	 * @throws IOException
	 */
	public static List<String> execCmd(String host, String userName,
			String password, String cmd) throws IOException {
		logger.debug("host:" + host + " ,userName:" + userName + " ,password:"
				+ password + " ,cmd:" + cmd);
		Connection connection = new Connection(host);
		connection.connect();
		boolean isAuth = connection.authenticateWithPassword(userName, password);
		if (!isAuth) {
			logger.error("Authentication failed.");
			throw new IOException("Authentication failed.");
		}
		Session session = connection.openSession();
		session.execCommand(cmd);

		InputStream stdoutStream = new StreamGobbler(session.getStdout());
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
				stdoutStream));
		// 执行信息
		List<String> infoList = new ArrayList<String>();
		while (true) {
			String line = stdoutReader.readLine();
			if (line == null)
				break;
			infoList.add(line);
		}
		stdoutReader.close();

		InputStream stderrStream = new StreamGobbler(session.getStderr());
		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(
				stderrStream));
		while (true) {
			String line = stderrReader.readLine();
			if (line == null)
				break;
			logger.error("Error message：" + line);
		}
		stderrReader.close();

		session.close();
		connection.close();
		logger.debug("Execution information：" + infoList);
		return infoList;
	}

	/**
	 * 从本地复制文件到远程目录
	 * 
	 * @param host
	 *            远程服务器IP
	 * @param userName
	 *            远程服务器用户名
	 * @param password
	 *            远程服务器密码
	 * @param fileData
	 *            本地待上传的文件字节
	 * @param remoteFileName
	 *            上传至服务器的文件名称
	 * @param remoteTargetDir
	 *            上传至服务器的目录
	 * @throws IOException
	 */
	public static void uploadBytes(String host, String userName,
			String password, byte[] fileData, String remoteFileName,
			String remoteTargetDir) throws IOException {
		logger.debug("host:" + host + " ,userName:" + userName + " ,password:" + password 
				+ " ,remoteFileName:" + remoteFileName + " ,remoteTargetDir:" + remoteTargetDir);
		if (fileData == null || fileData.length == 0) {
			return;
		}
		Connection connection = new Connection(host);
		connection.connect();
		boolean isAuth = connection.authenticateWithPassword(userName, password);
		if (!isAuth) {
			logger.error("Authentication failed.");
			throw new IOException("Authentication failed.");
		}
		SCPClient scpClient = connection.createSCPClient();
		scpClient.put(fileData, remoteFileName, remoteTargetDir);
		connection.close();
	}

	/**
	 * 从本地复制文件到远程目录
	 * 
	 * @param host
	 *            远程服务器IP
	 * @param userName
	 *            远程服务器用户名
	 * @param password
	 *            远程服务器密码
	 * @param fileList
	 *            本地待上传的文件列表
	 * @param remoteTargetDir
	 *            上传至服务器的目录
	 * @throws IOException
	 */
	public static void upload(String host, String userName, String password,
			List<String> fileList, String remoteTargetDir) throws IOException {
		logger.debug("host:" + host + " ,userName:" + userName + " ,password:" + password 
				+ " ,fileList:" + fileList + " ,remoteTargetDir:" + remoteTargetDir);
		if (fileList == null || fileList.isEmpty()) {
			return;
		}
		Connection connection = new Connection(host);
		connection.connect();
		boolean isAuth = connection.authenticateWithPassword(userName, password);
		if (!isAuth) {
			logger.error("Authentication failed.");
			throw new IOException("Authentication failed.");
		}
		SCPClient scpClient = connection.createSCPClient();
		if (fileList.size() == 1) {
			scpClient.put(fileList.get(0), remoteTargetDir);
		} else {
			int size = fileList.size();
			String[] localFiles = (String[]) fileList.toArray(new String[size]);
			scpClient.put(localFiles, remoteTargetDir);
		}
		connection.close();
	}

	/**
	 * 从服务器下载文件到本地
	 * 
	 * @param host
	 *            远程服务器IP
	 * @param userName
	 *            远程服务器用户名
	 * @param password
	 *            远程服务器密码
	 * @param remoteFile
	 *            待下载的服务器的文件名称（全路径）
	 * @throws IOException
	 */
	public static ByteArrayOutputStream download(String host, String userName,
			String password, String remoteFile) throws IOException {
		logger.debug("host:" + host + " ,userName:" + userName + " ,password:"
				+ password + " ,remoteFile:" + remoteFile);
		Connection connection = new Connection(host);
		connection.connect();
		boolean isAuth = connection.authenticateWithPassword(userName, password);
		if (!isAuth) {
			logger.error("Authentication failed.");
			throw new IOException("Authentication failed.");
		}
		// 10M
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 1024 * 10);
		SCPClient scpClient = connection.createSCPClient();
		scpClient.get(remoteFile, outputStream);
		connection.close();

		return outputStream;
	}
}
