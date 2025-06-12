package com.cacheclient;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        NettyCacheClientImpl client=new NettyCacheClientImpl("localhost",8080);
        client.connect();
        client.put("abc","1234");
        client.get("abc");
    }
}
