package com.example.kasperspychaa.rest;

/**
 * Created by Kasper Spychała on 18.01.2017.
 */

public class Greeting {

    private String command;
    private String fileName;
    private String path;

    public String getCommand() {
        return command;
    }

    public String getFileName() {
        return fileName;
    }

    public void setCommand(String command){
        this.command = command;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public void setPath(String path){ this.path = path; }

}
