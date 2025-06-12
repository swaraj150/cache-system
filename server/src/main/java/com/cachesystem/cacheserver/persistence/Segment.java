package com.cachesystem.cacheserver.persistence;

import lombok.Data;

import java.util.concurrent.*;

@Data
public class Segment<K,V> {
    private final ConcurrentHashMap<K,Node<K,V>> map;
    private final BlockingQueue<Runnable> jobs;
    private final ExecutorService worker;
    private Node<K,V> head;
    private Node<K,V> tail;
    private final int capacity;

    public Segment(int capacity){
        this.map=new ConcurrentHashMap<>();
        this.jobs=new LinkedBlockingQueue<>();
        this.worker=Executors.newSingleThreadExecutor();
        this.head=null;
        this.tail=null;
        this.capacity=capacity;
        this.worker.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task = jobs.take();
                    task.run();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
    public Segment(){
       this(16);
    }
    public V get(K key){
        if(!map.containsKey(key)){
            throw new RuntimeException("Key not found "+key);
        }
        Node<K,V> node=map.get(key);
        jobs.offer(()->moveToHead(node));
        return node.getValue();
    }

    public void put(K key,V value){
        if(map.containsKey(key)){
            Node<K,V> node=map.get(key);
            node.setValue(value);
            jobs.offer(()->moveToHead(node));

        }else{
            if(map.size()==capacity){
                map.remove(tail.getKey());
                jobs.offer(()->removeNode(tail));

            }
            Node<K,V> node= new Node<>(key,value,null, head);
            jobs.offer(()->addNodeAtHead(node));
            map.put(key,node);

        }
    }

    private void moveToHead(Node<K,V> node){
        removeNode(node);
        addNodeAtHead(node);
    }
    private void addNodeAtHead(Node<K, V> node) {
        node.setNext(head);
        if (head != null) head.setPrev(node);
        head =node;
        if (tail == null) tail = node;
    }
    private void removeNode(Node<K,V> node){
        Node<K,V> prevNode=node.getPrev();
        Node<K,V> nextNode=node.getNext();
        if(prevNode!=null){
            prevNode.setNext(nextNode);

        }else{
            head=nextNode;
        }
        if(nextNode!=null){
            nextNode.setPrev(prevNode);
        }else{
            tail=prevNode;
        }
        node.setPrev(null);
        node.setNext(null);
    }

    public void shutdown() {
        worker.shutdownNow();
    }



}
