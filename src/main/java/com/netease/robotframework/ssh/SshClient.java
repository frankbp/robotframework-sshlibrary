package com.netease.robotframework.ssh;

import java.util.Properties;
import java.io.*;
import com.jcraft.jsch.*;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.javalib.annotation.ArgumentNames;

import com.netease.robotframework.ssh.logging.SshLogging;

@RobotKeywords
public class SshClient {

    private JSch jSch = new JSch();
    private Session session = null;
    private final int sessionConnectionTimeout = 10 * 1000;

    @RobotKeyword("This keyword login to SSH server via private key\n\n"
            + "| Options    | Man. | Description |\n"
    		+ "| host       | Yes  | Host name or ip |\n"
    		+ "| port       | Yes  | SSH port |\n"
    		+ "| userName   | Yes  | SSH login name |\n"
            + "| privateKey | Yes  | User private key |\n"
            + "| passphrase | Yes  | Passphrase for key |\n\n"
            + "Examples:\n"
            + "| Login With Key | 1.1.1.1 | 1046 | ndir | /home/user/id_rsa | ${EMPTY} |\n")
    @ArgumentNames({"host", "port", "userName", "privateKey", "passphrase"})
    public void loginWithKey(String host, String port, String userName, String privateKey, String passphrase) throws JSchException, IOException {
        this.jSch.addIdentity(privateKey);
        this.session = this.jSch.getSession(userName, host, Integer.parseInt(port));

        setSessionConfiguration(host, port);
    }

    @RobotKeyword("This keyword login to SSH server via password\n\n"
            + "| Options    | Man. | Description |\n"
    		+ "| host       | Yes  | Host name or ip |\n"
    		+ "| port       | Yes  | SSH port |\n"
    		+ "| userName   | Yes  | SSH login name |\n"
            + "| password   | Yes  | SSH login password |\n\n"
            + "Examples:\n"
            + "| Login With Password | 1.1.1.1 | 1046 | ndir | ${PASSWORD} |\n")
    @ArgumentNames({"host", "port", "userName", "privateKey", "passphrase"})
    public void loginWithPassword(String host, String port, String userName, String password) throws JSchException, IOException {
        this.session = this.jSch.getSession(userName, host, Integer.parseInt(port));
        this.session.setPassword(password);
        setSessionConfiguration(host, port);
    }

    /**
     * Set default SSH session configuration
     * @param host
     * @param port
     * @throws JSchException
     */
    private void setSessionConfiguration(String host, String port) throws JSchException {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        this.session.setConfig(config);
        
        SshLogging.info("Connecting to SSH server " + host + ":" + port);
        this.session.connect(sessionConnectionTimeout);
    }

    @RobotKeyword("This keyword executes shell command\n\n"
            + "| Options | Man. | Description |\n"
            + "| command | Yes  | Shell command |\n\n"
            + "Examples:\n"
            + "| Execute Shell Command | ls -al |\n")
    @ArgumentNames({"command"})
    public SshResult executeShellCommand(String command) throws JSchException, IOException {
    	SshLogging.info("Opening SSH session");
        ChannelExec channel = (ChannelExec) this.session.openChannel("exec");

        try {
            channel.setCommand(command);
            SshLogging.info("Execute Command: " + command);
            channel.connect();

            return getCommandResponseFromChannel(channel);
        }
        finally {
            channel.disconnect();
        }
    }

    private SshResult getCommandResponseFromChannel(ChannelExec channel) throws IOException {
        StringBuffer commandResponse = new StringBuffer();
        StringBuffer errorInfo = new StringBuffer();
        int exitCode;

        InputStream inputData = channel.getInputStream();
        InputStream errorData = channel.getErrStream();
        int nextChar;
        try {
            while (true) {
                while ((nextChar = inputData.read()) != -1) {
                    commandResponse.append((char) nextChar);
                }
                while ((nextChar = errorData.read()) != -1) {
                    errorInfo.append((char) nextChar);
                }
                if (channel.isClosed()) {
                    exitCode = channel.getExitStatus();
                    break;
                }
            }
        } finally {
            inputData.close();
        }

        SshLogging.info("Command Exit Code: " + exitCode);
        SshLogging.info("Command Response:\n" + commandResponse.toString());
        SshLogging.info("Command Error Information:\n" + errorInfo.toString());

        
        return new SshResult(exitCode,commandResponse.toString(), errorInfo.toString());
    }

    @RobotKeyword("This keyword deletes currection active SSH session\n\n"
            + "Examples:\n"
            + "| Delete Session |\n")
    @ArgumentNames({})
    public void deleteSession() {
        this.session.disconnect();
    }
}