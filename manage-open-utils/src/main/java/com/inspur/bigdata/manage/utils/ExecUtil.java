package com.inspur.bigdata.manage.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExecUtil {


    /**
     * 本地执行命令并返回结果
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static List<String> localExec(String command) throws Exception {
        List<String> list = new ArrayList<String>();
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        String buf;
        try (final InputStream inputStream = process.getErrorStream();
             final InputStreamReader isr = new InputStreamReader(inputStream);
             final BufferedReader reader = new BufferedReader(isr)) {
            while ((buf = reader.readLine()) != null) {
                list.add(buf);
            }
        }
        return list;
    }


    /**
     * 远程执行命令并返回结果，调用过程是同步的（执行完才会返回）
     *
     * @param host
     * @param user
     * @param psw
     * @param command
     * @return
     * @throws Exception
     */
    public static String syncExec(String host, String user, String psw, String command) throws Exception {
        StringBuffer buffer = new StringBuffer();
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(psw);
        ChannelExec openChannel = null;
        try {
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            try (final InputStream inputStream = openChannel.getInputStream();
                 final InputStream extInputStream = openChannel.getExtInputStream()) {
                openChannel.connect();
                byte[] tmp = new byte[1024];
                int i;
                while ((i = inputStream.read(tmp, 0, 1024)) > 0) {
                    buffer.append(new String(tmp, 0, i));
                }
                while ((i = extInputStream.read(tmp, 0, 1024)) > 0) {
                    buffer.append(new String(tmp, 0, i));
                }
            }
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return buffer.toString();
    }

}
