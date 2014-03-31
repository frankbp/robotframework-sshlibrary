package com.netease.robotframework.ssh;

public class SshResult {
    private int exitCode;
    private String commandResponse;
    private String errorInfo;

    public SshResult(int exitCode, String commandResponse, String errorInfo) {
        this.exitCode = exitCode;
        this.commandResponse = commandResponse;
        this.errorInfo = errorInfo;
    }
    public int getExitCode() {
        return exitCode;
    }

    public String getCommandResponse() {
        return commandResponse;
    }

    public String getErrorInfo() {
        return errorInfo;
    }
}
