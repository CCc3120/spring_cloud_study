package com.bingo.study.common.core.utils.stack;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * 基于 ArrayDeque 双端队列实现的栈
 *
 * @Author h-bingo
 * @Date 2023-04-28 16:32
 * @Version 1.0
 */
public class ArrayDequeStack<T> {

    private final ArrayDeque<T> arrayDeque;

    public ArrayDequeStack() {
        arrayDeque = new ArrayDeque<>();
    }

    public ArrayDequeStack(int numElements) {
        arrayDeque = new ArrayDeque<>(numElements);
    }

    public void add(T t) {
        arrayDeque.addFirst(t);
    }

    public void push(T t) {
        arrayDeque.push(t);
    }

    public T poll() {
        return arrayDeque.poll();
    }

    public T remove() {
        return arrayDeque.remove();
    }

    public T peek() {
        return arrayDeque.peek();
    }

    public T pop() {
        return arrayDeque.pop();
    }

    public int size() {
        return arrayDeque.size();
    }

    public boolean isEmpty() {
        return arrayDeque.isEmpty();
    }

    public boolean contains(T t) {
        return arrayDeque.contains(t);
    }

    public void clean() {
        arrayDeque.clear();
    }

    public Stream<T> stream() {
        return arrayDeque.stream();
    }

    public T[] toArray(T[] a) {
        return arrayDeque.toArray(a);
    }

    public Object[] toArray() {
        return arrayDeque.toArray();
    }

    public Iterator<T> iterator() {
        return arrayDeque.iterator();
    }

    public String toString() {
        return arrayDeque.toString();
    }
}
