package com.cacheclient;

public class Main2 {

    public static void main(String[] args) throws InterruptedException {
        NettyCacheClientImpl client=new NettyCacheClientImpl("localhost",8080);
        client.connect();
        client.get("abc");
//        client.put("abc","2345");
//        client.get("abc");
//        client.put("abc","45667");
    }

}
