package com.company;

import java.util.concurrent.RecursiveAction;
import java.util.*;
public class ForkJoinCommonWords extends RecursiveAction {
    private int maxWords=2000;
    private String[] words1,words2;
    private Set<String> commonWords;
    private int begin,end, h;
    public ForkJoinCommonWords(String[] words1,String[] words2,Set<String> commonWords,int begin,int end,int h) {
        this.words1=words1;
        this.words2=words2;
        this.commonWords=commonWords;
        this.begin=begin;
        this.end=end;
        this.h=h;
    }
    @Override
    public void compute(){
        if((end-begin)<maxWords) {
            for(int i=begin;i<end;i++){
                for(int j=0;j<words2.length;j++){
                    if(words1[i].equals(words2[j])){
                        commonWords.add(words1[i]);
                    }
                }
            }
            return;
        }
        else {
            h=h/2;
            invokeAll(
                    new ForkJoinCommonWords(words1,words2,commonWords,begin,begin+h,h),//end and begin ділити навпіл
                    new ForkJoinCommonWords(words1,words2,commonWords,begin+h,end,h)
            );

        }
    }
}


