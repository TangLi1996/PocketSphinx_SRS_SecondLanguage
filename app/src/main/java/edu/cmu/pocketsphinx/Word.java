package edu.cmu.pocketsphinx;

public class Word {
    String wordname;
    int isRecord;

    public String getName() {
        return wordname;
    }

    public void setName(String name) {
        this.wordname = name;
    }

    @Override
    public String toString() {
//        return "Word{" +
//                "wordname='" + wordname + '\'' +
//                ", isRecord=" + isRecord +
//                '}';
        return wordname;
    }
}