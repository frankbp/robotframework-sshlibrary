package com.netease.robotframework.ssh.logging;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SshLogging {
	private static final Logger sshLogger = Logger.getLogger(SshLogging.class);
	
	{
	BasicConfigurator.configure();
	}
	
    public static void info(String msg) {
    	sshLogger.info("*INFO* " + msg);
    }
    
    public static void warn(String msg) {
    	sshLogger.info("*WARN* " + msg);
    }
    
    public static void debug(String msg) {
    	sshLogger.info("*DEBUG* " + msg);
    }
    
    public static void trace(String msg) {
    	sshLogger.info("*TRACE* " + msg);
    }

   
}