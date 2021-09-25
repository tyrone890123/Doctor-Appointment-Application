package ph.edu.dlsu;

public interface MyQueueInterface<T> {

    public void push(T item);
    public T pop();
    public T front();
    public T back();
    public int size();
    public boolean empty();
}
