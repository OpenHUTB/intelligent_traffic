package com.ruoyi.simulation.util;

public class Test {
    public static void main(String[] args) {
        for(int i=1;i<=9;i++){
            for(int j=1;j<=i;j++){
                System.out.print(j+"*"+i+"="+(j*i));
            }
            System.out.println();
        }
    }
    public static void test2(){
        for(int i=0;i<19;i++){
            if(i<19/2){
                for(int j=0;j<19-i/2;j++){
                    System.out.println(" ");
                }
                for(int j=0;j<i;j++){
                    System.out.println("*");
                }
            }else{
                for(int j=0;j<i-10;j++){
                    System.out.println(" ");
                }
                for(int j=0;j<10-(i-10)*2;j++){
                    System.out.println(" ");
                }
            }
        }
    }
}
