package com.netease.robotframework.ssh.logging;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SshLogging {
    
    public static void info(String msg) {
    	System.out.println("*INFO* " + msg);
    }
    
    public static void warn(String msg) {
    	System.out.println("*WARN* " + msg);
    }
    
    public static void debug(String msg) {
    	System.out.println("*DEBUG* " + msg);
    }
    
    public static void trace(String msg) {
    	System.out.println("*TRACE* " + msg);
    }

   
}