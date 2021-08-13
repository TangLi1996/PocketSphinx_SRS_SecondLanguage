package com.zxc.pocketsphinx.util;
import java.util.List;

public class Word {
    String wordname;
    String folder;

    public String getName() {
        return wordname;
    }

    public void setName(String name) {
        this.wordname = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
//        return "Word{" +
//                "wordname='" + wordname + '\'' +
//                ", isRecord=" + isRecord +
//                '}';
        return wordname+"-"+folder;
    }
}