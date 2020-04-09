package com.company;

import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class ForkJoinKeyWords extends RecursiveAction {
    private String[] keyWords={"IT","programming","software","application","hardware"};
    private Set<String> ITFiles;

    private int maxWords=200;
    private String[] words;
    private int begin,end, h;
    private String filename;
    public ForkJoinKeyWords(String[] words,String filename,Set<String> ITFiles,int begin,int end,int h) {
        this.words=words;
        this.filename=filename;
        this.ITFiles=ITFiles;
        this.begin=begin;
        this.end=end;
        this.h=h;
    }
    @Override
    public void compute(){
        if((end-begin)<maxWords) {
            for(int i=begin;i<end;i++){
                for(int j=0;j<keyWords.length;j++){
                    if(words[i].equals(keyWords[j])){
                        ITFiles.add(filename);
                    }
                }
            }
            return;
        }
        else {
            h=h/2;
            invokeAll(
                    new ForkJoinKeyWords(words,filename,ITFiles,begin,begin+h,h),//end and begin ділити навпіл
                    new ForkJoinKeyWords(words,filename,ITFiles,begin+h,end,h)
            );

        }
    }
}
