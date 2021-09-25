package ph.edu.dlsu;

import java.util.LinkedList;

public class MyQueue implements MyQueueInterface {

    LinkedList a = new LinkedList();

    @Override
    public void push(Object item) {
        a.add(item);
    }

    @Override
    public Object pop() {
        Object b=a.getFirst();
        a.removeFirst();
        return b;
    }

    @Override
    public Object front() {
        return a.getFirst();
    }

    @Override
    public Object back() {
        return a.getLast();
    }

    @Override
    public int size() {
        return a.size();
    }

    @Override
    public boolean empty() {
        return a.size()==0;
    }

}
