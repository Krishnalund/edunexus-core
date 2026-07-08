package org.example;

class QueueFullException extends Exception {
    public QueueFullException(String message) {
        super(message);
    }
}
class QueueEmptyException extends Exception {
    public QueueEmptyException(String message) {
        super(message);
    }
}
public class CircularQueue<T> {
    private T[] queueArray;
    private int front;
    private int rear;
    private int size;
    public CircularQueue(int capacity){
        this.queueArray = (T[]) new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }
    public void enqueue(T item) throws QueueFullException {
        if(size == queueArray.length){
            throw new QueueFullException("Queue is full");
        }
        rear = (rear+1) % queueArray.length;
        queueArray[rear] = item;
        size++;
    }
    public T dequeue() throws QueueEmptyException {
        if(size == 0){
            throw new QueueEmptyException("Queue is empty");
        }
        T item = queueArray[front];
        front = (front+1) % queueArray.length;
        size--;
        return item;
    }
    public T getFront() throws QueueEmptyException {
        if (size == 0) {
            throw new QueueEmptyException("Queue is empty");
        }
        return queueArray[front];
    }
}


