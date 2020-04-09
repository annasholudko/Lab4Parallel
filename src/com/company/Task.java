package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;

public class Task {
    ConcurrentHashMap<String, Integer> certificationCosts = new ConcurrentHashMap<>();
    private Set<String> ITFiles = certificationCosts.newKeySet();
    public String read(String filename){
        String text="";
        try (FileReader reader = new FileReader(filename)) {
            int c;
            while ((c = reader.read()) != -1) {
                text += Character.toString(c);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }
    public String[] divideWords(String filename){
        String text=read(filename);
        String[] words=text.split(" ");
        for(int i=0;i<words.length;i++){
            words[i] = words[i].replaceAll("[^A-Za-zА-Яа-я0-9]", "").toLowerCase();
        }
        return words;
    }
    public Map<Integer,Integer> formSample(ConcurrentHashMap<String,Integer> wordLength){
        Map<Integer,Integer> targetMap=new HashMap<>();
        for (int x : wordLength.values()){
            targetMap.put(x,0);//скільки разів зустрічається кожна довжина
        }

        for(Map.Entry<String, Integer> entry: wordLength.entrySet()) {
            for(Map.Entry<Integer, Integer> entry2: targetMap.entrySet()) {
                if (entry.getValue().equals(entry2.getKey())){
                    int newval=entry2.getValue()+1;
                    targetMap.replace(entry2.getKey(),newval);
                }
            }
        }

        return targetMap;
    }
    public void findStatCharacteristics(Map<Integer,Integer> sample){
        double M=0, dispersion=0, vidhil=0;
        int sum=0,znam=0, d=0;
        for(Map.Entry<Integer, Integer> entry: sample.entrySet()) {
            //System.out.println(entry.getKey()+" "+entry.getValue());
            sum+=entry.getKey()*entry.getValue();
            znam+=entry.getValue();
            d+=entry.getKey()*entry.getKey()*entry.getValue();
        }
        M=sum/znam;
        dispersion=d/znam;
        vidhil=Math.sqrt(dispersion);
        //System.out.println("M "+M+" D "+dispersion+" vidhil "+vidhil);
        System.out.println("M "+M);
        System.out.println("D "+dispersion);
        System.out.println(""+vidhil);
    }
    public void calculateLength(){
        ConcurrentHashMap<String,Integer> wordLength= new ConcurrentHashMap<>();
        long time1=System.nanoTime();
        String[] words=divideWords("serdtse.txt");
        ForkJoinLength fb=new ForkJoinLength(words,wordLength,0,words.length,words.length);
        ForkJoinPool pool=new ForkJoinPool();
        pool.invoke(fb);
        long time2=System.nanoTime();
       /* for(Map.Entry<String, Integer> entry: wordLength.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());
        }*/
        System.out.println("pool "+(time2-time1));
        //System.out.println(wordLength.size());//5733
        Map<Integer,Integer> sample=formSample(wordLength);
        findStatCharacteristics(sample);
    }
    public void findCommonWords(){
        Set<String> commonWords = certificationCosts.newKeySet();

        String[] words1=divideWords("alye-parusa.txt");
        String[] words2=divideWords("TheOldManandtheSea.txt");

        ForkJoinCommonWords fb=new ForkJoinCommonWords(words1,words2,commonWords,0,words1.length,words1.length);
        ForkJoinPool pool=new ForkJoinPool();
        pool.invoke(fb);
        for(String wor:commonWords){
            System.out.println(wor);
        }
        System.out.println(commonWords.size());
    }
    public void findKeyWords(String filename){
        String[] words=divideWords(filename);
        ForkJoinKeyWords fb=new ForkJoinKeyWords(words,filename,ITFiles,0,words.length,words.length);
        ForkJoinPool pool=new ForkJoinPool();
        pool.invoke(fb);

    }
    public void openFiles(){
        File dir = new File("D://KPI/Paralelni_obchisl/Lab4/KeyWords");
        if(dir.isDirectory())
        {
            for(File item : dir.listFiles()){
                findKeyWords(item.getName());
            }
        }
        for (String str:ITFiles){
            System.out.println(str);
        }
    }
}
