package com.thangtranit.notificationservice.entity;

import java.util.ArrayDeque;

public class FixedSizeQueue<E> {
    private final ArrayDeque<E> queue;
    private static final int MAX_SIZE = 100;

    public FixedSizeQueue() {
        this.queue = new ArrayDeque<>(MAX_SIZE);
    }

    public void add(E element) {
        if (queue.size() >= MAX_SIZE) {
            queue.poll(); // Xóa phần tử cũ nhất
        }
        queue.add(element);
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
