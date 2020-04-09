package com.company;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

public class ForkJoinLength extends RecursiveAction {
    private int maxWords=500;
    private String[] words;
    private ConcurrentHashMap<String,Integer> wordLength;
    private int begin,end, h;
    public ForkJoinLength(String[] words,ConcurrentHashMap<String,Integer> wordLength,int begin,int end,int h) {
        this.words=words;
        this.wordLength=wordLength;
        this.begin=begin;
        this.end=end;
        this.h=h;
    }
    @Override
    public void compute(){
        if((end-begin)<maxWords) {
            for(int i=begin;i<end;i++){
                char[] arr=words[i].toCharArray();
                wordLength.put(words[i],arr.length);
            }
            return;
        }
        else {
            h=h/2;
            invokeAll(
                    new ForkJoinLength(words,wordLength,begin,begin+h,h),
                    new ForkJoinLength(words,wordLength,begin+h,end,h)
            );

        }
    }
}

