package com.cachesystem.cacheclient;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
public class Main2 {

//    public static void main(String[] args) throws InterruptedException {

//        client.get("abc");
////        client.put("abc","2345");
////        client.get("abc");
////        client.put("abc","45667");
//    }
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(50); // or more

        for (int i = 0; i < 5; i++) {
            final int index = i;
            executor.submit(() -> {
                NettyCacheClientImpl client=new NettyCacheClientImpl("localhost",8080);
                String key = "key" +(index)+"_"+ (index%2);
                String value = "value" + index;
                try {
                    client.connect();
                    String retrieved = String.valueOf(client.get(key));
                    client.put(key, value);  // or send via Netty client
                    if (!retrieved.equals(value)) {
                        log.error("Data mismatch for key: {}" , key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            });
        }
        executor.shutdown();
    }

}
